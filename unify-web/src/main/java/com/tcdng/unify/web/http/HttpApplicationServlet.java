/*
 * Copyright 2014 The Code Department
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.tcdng.unify.web.http;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.tcdng.unify.core.ApplicationComponents;
import com.tcdng.unify.core.ApplicationController;
import com.tcdng.unify.core.RequestContextManager;
import com.tcdng.unify.core.UnifyContainer;
import com.tcdng.unify.core.UnifyContainerConfig;
import com.tcdng.unify.core.UnifyContainerEnvironment;
import com.tcdng.unify.core.UnifyCorePropertyConstants;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.UserSession;
import com.tcdng.unify.core.UserToken;
import com.tcdng.unify.core.constant.UserPlatform;
import com.tcdng.unify.core.system.UserSessionManager;
import com.tcdng.unify.core.util.IOUtils;
import com.tcdng.unify.core.util.StringUtils;
import com.tcdng.unify.core.util.TypeRepository;
import com.tcdng.unify.core.util.UnifyConfigUtils;
import com.tcdng.unify.web.RemoteCallClient;
import com.tcdng.unify.web.UnifyWebInterface;
import com.tcdng.unify.web.WebApplicationComponents;
import com.tcdng.unify.web.constant.RequestParameterConstants;
import com.tcdng.unify.web.constant.ReservedPageControllerConstants;
import com.tcdng.unify.web.util.WebTypeUtils;

/**
 * Serves as an interface between the servlet container and the Unify container;
 * handling the exchange of http requests and responses between both containers.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@MultipartConfig
public class HttpApplicationServlet extends HttpServlet {

	/** The serial version ID */
	private static final long serialVersionUID = 3971544226497014269L;

	private UnifyContainer unifyContainer;

	private UnifyWebInterface webInterface;

	private RequestContextManager requestContextManager;

	private ApplicationController applicationController;

	private UserSessionManager userSessionManager;

	private String contextPath;

	private boolean standalone;

	public HttpApplicationServlet() {
		this(false);
	}

	public HttpApplicationServlet(boolean standalone) {
		this.standalone = standalone;
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);

		if (!standalone) {
			ServletContext servletContext = config.getServletContext();
			String workingFolder = servletContext.getRealPath("");
			String configFolder = servletContext.getRealPath("/WEB-INF");
			String configFilename = config.getInitParameter("application-config");
			if (StringUtils.isBlank(configFilename)) {
				configFilename = "unify.xml";
			}

			InputStream configInputStream = null;
			try {
				contextPath = servletContext.getContextPath();
				TypeRepository tr = WebTypeUtils.buildTypeRepositoryFromServletContext(servletContext);
				UnifyContainerEnvironment uce = new UnifyContainerEnvironment(tr, workingFolder);
				UnifyContainerConfig.Builder uccb = UnifyContainerConfig.newBuilder();

				// Scan configuration
				UnifyConfigUtils.readConfigFromTypeRepository(uccb, tr);
				uccb.deploymentMode(true);
				configInputStream = new FileInputStream(IOUtils.fileInstance(configFilename, configFolder));

				// Read xml configuration
				UnifyConfigUtils.readConfigFromXml(uccb, configInputStream);
				uccb.setPropertyIfBlank(UnifyCorePropertyConstants.APPLICATION_HOME,
						ReservedPageControllerConstants.APPLICATION_HOME);

				// Create container
				UnifyContainerConfig ucc = uccb.build();
				unifyContainer = new UnifyContainer();
				unifyContainer.startup(uce, ucc);
				requestContextManager = (RequestContextManager) unifyContainer
						.getComponent(ApplicationComponents.APPLICATION_REQUESTCONTEXTMANAGER);
				applicationController = (ApplicationController) unifyContainer
						.getComponent(WebApplicationComponents.APPLICATION_HTTPCONTROLLER);
				userSessionManager = (UserSessionManager) unifyContainer
						.getComponent(ApplicationComponents.APPLICATION_USERSESSIONMANAGER);
			} catch (Exception e) {
				if (unifyContainer != null && unifyContainer.isStarted()) {
					unifyContainer.shutdown();
				}
				throw new ServletException(e);
			} finally {
				IOUtils.close(configInputStream);
			}
		}
	}

	@Override
	public void destroy() {
		if (unifyContainer != null && unifyContainer.isStarted()) {
			unifyContainer.shutdown();
		}
		super.destroy();
	}

	public void setup(UnifyWebInterface webInterface, RequestContextManager requestContextManager,
			ApplicationController applicationController, UserSessionManager userSessionManager) {
		this.webInterface = webInterface;
		this.requestContextManager = requestContextManager;
		this.applicationController = applicationController;
		this.userSessionManager = userSessionManager;
		contextPath = webInterface.getContextPath();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (!standalone || webInterface.isServicingRequests()) {
			try {
				requestContextManager.loadRequestContext(getUserSession(request), request.getServletPath());
				applicationController.execute(request, response);
			} catch (Exception e) {
				throw new ServletException(e);
			} finally {
				try {
					userSessionManager.updateCurrentSessionLastAccessTime();
				} catch (Exception e) {
				}
				requestContextManager.unloadRequestContext();
			}
		}
	}

	private UserSession getUserSession(HttpServletRequest request) throws UnifyException {
		HttpUserSession userSession = null;
		if (RemoteCallClient.USER_AGENT_ID.equalsIgnoreCase(request.getHeader("User-Agent"))) {
			// Handle remote call
			HttpSession httpSession = request.getSession(false);
			if (httpSession != null) {
				httpSession.invalidate();
			}
			// Create single use session object
			userSession = createHttpUserSession(request);
		} else {
			if (!StringUtils.isBlank((String) request.getParameter(RequestParameterConstants.REMOTE_VIEWER))) {
				// Handle remote view
				HttpSession httpSession = request.getSession(false);
				if (httpSession != null) {
					httpSession.invalidate();
				}

				String sessionId = (String) request.getParameter(RequestParameterConstants.REMOTE_SESSION_ID);
				if (!StringUtils.isBlank(sessionId)) {
					userSession = (HttpUserSession) userSessionManager.getUserSession(sessionId);
				}

				if (userSession == null) {
					userSession = createHttpUserSession(request);
					userSessionManager.addUserSession(userSession);

					String userLoginId = (String) request.getParameter(RequestParameterConstants.REMOTE_USERLOGINID);
					String userName = (String) request.getParameter(RequestParameterConstants.REMOTE_USERNAME);
					String roleCode = (String) request.getParameter(RequestParameterConstants.REMOTE_ROLECD);
					UserToken userToken = new UserToken(userLoginId, userName, userSession.getRemoteAddress(), null,
							null, true, true, true);
					userToken.setRoleCode(roleCode);
					userSession.getSessionContext().setUserToken(userToken);
				}

			} else {
				// Handle document request
				HttpSession httpSession = request.getSession();
				userSession = (HttpUserSession) httpSession.getAttribute(HttpConstants.USER_SESSION);
				if (userSession == null) {
					synchronized (httpSession) {
						userSession = (HttpUserSession) httpSession.getAttribute(HttpConstants.USER_SESSION);
						if (userSession == null) {
							userSession = createHttpUserSession(request);
							httpSession.setAttribute(HttpConstants.USER_SESSION, userSession);
						}
					}
				}
			}
		}

		userSession.setTransient(userSessionManager);
		return userSession;
	}

	private HttpUserSession createHttpUserSession(HttpServletRequest request) throws UnifyException {
		String remoteIpAddress = request.getHeader("X-FORWARDED-FOR");
		if (remoteIpAddress == null || remoteIpAddress.trim().isEmpty()
				|| "unknown".equalsIgnoreCase(remoteIpAddress)) {
			remoteIpAddress = request.getHeader("Proxy-Client-IP");
		}

		if (remoteIpAddress == null || remoteIpAddress.trim().isEmpty()
				|| "unknown".equalsIgnoreCase(remoteIpAddress)) {
			remoteIpAddress = request.getHeader("WL-Proxy-Client-IP");
		}

		if (remoteIpAddress == null || remoteIpAddress.trim().isEmpty()
				|| "unknown".equalsIgnoreCase(remoteIpAddress)) {
			remoteIpAddress = request.getRemoteAddr();
		}

		String uriBase = request.getScheme() + "://" + request.getServerName();
		if (!(("http".equals(request.getScheme()) && request.getServerPort() == 80)
				|| ("https".equals(request.getScheme()) && request.getServerPort() == 443))) {
			uriBase = uriBase + ":" + request.getServerPort();
		}

		UserPlatform platform = detectRequestPlatform(request);
		HttpUserSession userSession = new HttpUserSession(uriBase, contextPath, request.getRemoteHost(),
				remoteIpAddress, request.getRemoteUser(),
				(String) request.getParameter(RequestParameterConstants.REMOTE_VIEWER), platform);
		userSession.setTransient(userSessionManager);
		return userSession;
	}

	private UserPlatform detectRequestPlatform(HttpServletRequest request) {
		UserPlatform platform = UserPlatform.DEFAULT;
		if (request.getHeader("User-Agent").indexOf("Mobile") >= 0) {
			platform = UserPlatform.MOBILE;
		}
		return platform;
	}
}
