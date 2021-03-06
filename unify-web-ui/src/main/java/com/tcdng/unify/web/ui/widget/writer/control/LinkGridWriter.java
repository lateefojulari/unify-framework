/*
 * Copyright 2018-2020 The Code Department.
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

package com.tcdng.unify.web.ui.widget.writer.control;

import java.util.List;

import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Writes;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.json.JsonWriter;
import com.tcdng.unify.web.ui.widget.ResponseWriter;
import com.tcdng.unify.web.ui.widget.Widget;
import com.tcdng.unify.web.ui.widget.control.LinkGrid;
import com.tcdng.unify.web.ui.widget.data.LinkCategoryInfo;
import com.tcdng.unify.web.ui.widget.data.LinkGridInfo;
import com.tcdng.unify.web.ui.widget.data.LinkInfo;
import com.tcdng.unify.web.ui.widget.writer.AbstractControlWriter;

/**
 * Link grid writer.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Writes(LinkGrid.class)
@Component("linkgrid-writer")
public class LinkGridWriter extends AbstractControlWriter {

    @Override
    protected void doWriteStructureAndContent(ResponseWriter writer, Widget widget) throws UnifyException {
        LinkGrid linkGrid = (LinkGrid) widget;
        writer.write("<div");
        writeTagAttributes(writer, linkGrid);
        writer.write(">");
        LinkGridInfo linkGridInfo = linkGrid.getValue(LinkGridInfo.class);
        if (linkGridInfo != null) {
            for (LinkCategoryInfo linkCategoryInfo : linkGridInfo.getLinkCategoryList()) {
                writer.write("<div>");
                String caption = linkCategoryInfo.getCaption();
                if (caption != null) {
                    writer.write("<div><span class=\"lgcatcap\">");
                    writer.writeWithHtmlEscape(caption);
                    writer.write("</span></div>");
                }

                List<LinkInfo> linkInfoList = linkCategoryInfo.getLinkInfoList();
                if (DataUtils.isNotBlank(linkInfoList)) {
                    writer.write("<div class=\"lgcatsec\"><table style=\"width:100%;\">");
                    String catName = linkGrid.getPrefixedId(linkCategoryInfo.getName());

                    int columns = linkGrid.getUplAttribute(int.class, "columns");
                    if (columns <= 0) {
                        columns = 1;
                    }

                    Integer colWidth = 100 / columns; // Column with in percentage
                    int len = linkInfoList.size();
                    for (int i = 0; i < len;) {
                        writer.write("<tr>");
                        for (int k = 0; k < columns; k++, i++) {
                            writer.write("<td style=\"width:").write(colWidth).write("%;\">");
                            if (i < len) {
                                LinkInfo linkInfo = linkInfoList.get(i);
                                writer.write("<a class=\"lglink\"");
                                writeTagId(writer, catName + i);
                                writer.write(">");
                                writer.writeWithHtmlEscape(linkInfo.getCaption());
                                writer.write("</a>");
                            }
                            writer.write("</td>");
                        }
                        writer.write("</tr>");
                    }
                    writer.write("</table></div>");
                }
                writer.write("</div>");
            }
        }
        writer.write("</div>");
    }

    @Override
    protected void doWriteBehavior(ResponseWriter writer, Widget widget) throws UnifyException {
        super.doWriteBehavior(writer, widget);
        LinkGrid linkGrid = (LinkGrid) widget;

        // Append link grid rigging
        writer.beginFunction("ux.rigLinkGrid");
        JsonWriter jw = new JsonWriter();
        jw.beginArray();
        LinkGridInfo linkGridInfo = linkGrid.getValue(LinkGridInfo.class);
        if (linkGridInfo != null) {
            for (LinkCategoryInfo linkCategoryInfo : linkGridInfo.getLinkCategoryList()) {

                jw.beginObject();
                jw.write("pURL", getContextURL(linkCategoryInfo.getPath()));
                jw.beginArray("links");
                List<LinkInfo> linkInfoList = linkCategoryInfo.getLinkInfoList();
                if (DataUtils.isNotBlank(linkInfoList)) {
                    String catName = linkGrid.getPrefixedId(linkCategoryInfo.getName());
                    int len = linkInfoList.size();
                    for (int i = 0; i < len; i++) {
                        LinkInfo linkInfo = linkInfoList.get(i);
                        jw.beginObject();
                        jw.write("pId", catName + i);
                        jw.write("pCode", linkInfo.getCode());
                        jw.endObject();
                    }
                }
                jw.endArray();
                jw.endObject();

            }
        }
        jw.endArray();
        writer.writeParam("categories", jw);
        
        writer.endFunction();
    }
}
