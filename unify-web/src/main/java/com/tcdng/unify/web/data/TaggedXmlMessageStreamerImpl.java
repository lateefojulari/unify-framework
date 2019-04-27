/*
 * Copyright 2018-2019 The Code Department.
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

package com.tcdng.unify.web.data;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.Stack;

import javax.xml.parsers.SAXParser;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.data.TaggedXmlMessage;
import com.tcdng.unify.core.stream.AbstractObjectStreamer;
import com.tcdng.unify.core.util.StringUtils;
import com.tcdng.unify.core.util.XmlUtils;
import com.tcdng.unify.web.util.HtmlUtils;

/**
 * Tagged XML message streamer.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component("taggedxmlmessage-streamer")
public class TaggedXmlMessageStreamerImpl extends AbstractObjectStreamer implements TaggedXmlMessageStreamer {

    @Override
    public <T> T unmarshal(Class<T> type, InputStream inputStream, Charset charset) throws UnifyException {
        return unmarshal(type, new InputSource(inputStream));
    }

    @Override
    public <T> T unmarshal(Class<T> type, Reader reader) throws UnifyException {
        return unmarshal(type, new InputSource(reader));
    }

    @Override
    public <T> T unmarshal(Class<T> type, String string) throws UnifyException {
        return unmarshal(type, new InputSource(new StringReader(string)));
    }

    @Override
    public void marshal(Object object, OutputStream outputStream, Charset charset) throws UnifyException {
        if (charset != null) {
            marshal(object, new OutputStreamWriter(outputStream, charset));
        } else {
            marshal(object, new OutputStreamWriter(outputStream));
        }
    }

    @Override
    public void marshal(Object object, Writer writer) throws UnifyException {
        SAXParser saxParser = null;
        try {
            if (TaggedXmlMessageParams.class.equals(object.getClass())) {
                saxParser = XmlUtils.borrowSAXParser();
                TaggedXmlMessageParams params = (TaggedXmlMessageParams) object;
                TaggedXmlMessage msg = params.getTaggedMessage();
                writer.write("<TaggedXmlMessageParams");
                writeAttribute(writer, "methodCode", params.getMethodCode());
                writeAttribute(writer, "clientAppCode", params.getClientAppCode());
                if (msg != null) {
                    writeAttribute(writer, "tag", msg.getTag());
                    writeAttribute(writer, "consumer", msg.getConsumer());
                }
                writer.write(">");
                if (msg != null) {
                    String xml = msg.getMessage();
                    if (!StringUtils.isBlank(xml)) {
                        // Validate
                        InputStream is = new ByteArrayInputStream(xml.getBytes("UTF-8"));
                        saxParser.parse(is, new DefaultHandler());
                        writer.write(xml);
                    }
                }
                writer.write("</TaggedXmlMessageParams>");
            } else if (TaggedXmlMessageResult.class.equals(object.getClass())) {
                TaggedXmlMessageResult result = (TaggedXmlMessageResult) object;
                writer.write("<TaggedXmlMessageResult");
                writeAttribute(writer, "methodCode", result.getMethodCode());
                writeAttribute(writer, "errorCode", result.getErrorCode());
                writer.write(">");
                if (!StringUtils.isBlank(result.getErrorMsg())) {
                    writer.write("<errorMsg>");
                    writer.write(HtmlUtils.getStringWithHtmlEscape(result.getErrorMsg()));
                    writer.write("</errorMsg>");
                }
                writer.write("</TaggedXmlMessageResult>");
            } else {
                throwOperationErrorException(
                        new RuntimeException("Unsupported stream object type - " + object.getClass()));
            }
            writer.flush();
        } catch (UnifyException e) {
            throw e;
        } catch (Exception e) {
            throwOperationErrorException(e);
        } finally {
            if (saxParser != null) {
                XmlUtils.restoreSAXParser(saxParser);
            }
        }
    }

    @Override
    public byte[] marshal(Object object) throws UnifyException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        marshal(object, baos);
        return baos.toByteArray();
    }

    @SuppressWarnings("unchecked")
    private <T> T unmarshal(Class<T> type, InputSource inputSource) throws UnifyException {
        SAXParser saxParser = null;
        try {
            saxParser = XmlUtils.borrowSAXParser();
            if (TaggedXmlMessageParams.class.equals(type)) {
                TaggedXmlMessageParamsReader readerHandler = new TaggedXmlMessageParamsReader();
                saxParser.parse(inputSource, readerHandler);
                return (T) readerHandler.getParams();
            } else if (TaggedXmlMessageResult.class.equals(type)) {
                TaggedXmlMessageResultReader readerHandler = new TaggedXmlMessageResultReader();
                saxParser.parse(inputSource, readerHandler);
                return (T) readerHandler.getResult();
            } else {
                throwOperationErrorException(new RuntimeException("Unsupported stream object type - " + type));
            }
        } catch (UnifyException e) {
            throw e;
        } catch (Exception e) {
            throwOperationErrorException(e);
        } finally {
            if (saxParser != null) {
                XmlUtils.restoreSAXParser(saxParser);
            }
        }

        return null;
    }

    private void writeAttribute(Writer writer, String qname, String val) throws IOException {
        if (!StringUtils.isBlank(val)) {
            writer.write(" ");
            writer.write(qname);
            writer.write(" = \"");
            writer.write(val);
            writer.write("\"");
        }
    }

    private void writeAttribute(StringBuilder sb, String qname, String val) throws IOException {
        if (!StringUtils.isBlank(val)) {
            sb.append(" ").append(qname).append(" = \"").append(val).append("\"");
        }
    }

    private class TaggedXmlMessageParamsReader extends DefaultHandler {

        private StringBuilder sb;

        private String methodCode;

        private String clientAppCode;

        private String tag;

        private String consumer;

        private Stack<String> track;

        private TaggedXmlMessageParams params;

        public TaggedXmlMessageParamsReader() {
            track = new Stack<String>();
        }

        public TaggedXmlMessageParams getParams() {
            return params;
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes)
                throws SAXException {
            if ("TaggedXmlMessageParams".equals(qName)) {
                if (track.size() != 0) {
                    throw new SAXException("Bad tagged XML message params structure!");
                }

                methodCode = attributes.getValue("methodCode");
                if (StringUtils.isBlank(methodCode)) {
                    throw new SAXException("Missing 'methodCode' attribute");
                }

                clientAppCode = attributes.getValue("clientAppCode");

                tag = attributes.getValue("tag");
                if (StringUtils.isBlank(tag)) {
                    throw new SAXException("Missing 'tag' attribute");
                }

                consumer = attributes.getValue("consumer");
                sb = new StringBuilder();
            } else {
                if (track.size() == 0) {
                    throw new SAXException("Invalid root element!");
                }

                sb.append("<").append(qName);
                try {
                    // Append attributes
                    int len = attributes.getLength();
                    for(int i = 0;  i < len; i++) {
                        writeAttribute(sb, attributes.getQName(i), attributes.getValue(i));
                    }
                } catch (IOException e) {
                    throw new SAXException(e);
                }
                sb.append(">");
            }

            track.push(qName);
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            sb.append(new String(ch, start, length));
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            track.pop();
            if ("TaggedXmlMessageParams".equals(qName)) {
                if (track.size() != 0 && !"TaggedXmlMessage".equals(qName)) {
                    throw new SAXException("Bad tagged XML message structure!");
                }

                String xml = sb.toString();
                if (StringUtils.isBlank(xml)) {
                    xml = null;
                }
                params = new TaggedXmlMessageParams(methodCode, clientAppCode,
                        new TaggedXmlMessage(tag, consumer, xml));
            } else {
                sb.append("</").append(qName);
                sb.append(">");
            }
        }

    }

    private class TaggedXmlMessageResultReader extends DefaultHandler {

        private String methodCode;

        private String errorCode;

        private String errorMsg;

        private Stack<String> track;

        private TaggedXmlMessageResult result;

        public TaggedXmlMessageResultReader() {
            track = new Stack<String>();
        }

        public TaggedXmlMessageResult getResult() {
            return result;
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes)
                throws SAXException {
            if (track.size() > 1) {
                throw new SAXException("Unexpected elements!");
            }

            if ("TaggedXmlMessageResult".equals(qName)) {
                methodCode = attributes.getValue("methodCode");
                errorCode = attributes.getValue("errorCode");
                errorMsg = attributes.getValue("errorMsg");
            } else {
                if (track.size() == 0) {
                    throw new SAXException("Invalid root element!");
                }
            }

            track.push(qName);
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            if ("errorMsg".equals(track.peek())) {
                errorMsg = new String(ch, start, length);
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            track.pop();
            if (track.size() == 0) {
                result = new TaggedXmlMessageResult(methodCode, errorCode, errorMsg);
            }
        }

    }

}
