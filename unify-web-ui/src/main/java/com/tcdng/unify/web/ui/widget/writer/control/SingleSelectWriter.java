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
import com.tcdng.unify.core.data.Listable;
import com.tcdng.unify.core.util.StringUtils;
import com.tcdng.unify.core.util.json.JsonWriter;
import com.tcdng.unify.web.ui.widget.ListControlInfo;
import com.tcdng.unify.web.ui.widget.ResponseWriter;
import com.tcdng.unify.web.ui.widget.control.AbstractPopupTextField;
import com.tcdng.unify.web.ui.widget.control.SingleSelect;

/**
 * Single select field writer.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Writes(SingleSelect.class)
@Component("singleselect-writer")
public class SingleSelectWriter extends AbstractPopupTextFieldWriter {

    @Override
    protected void writePopupContent(ResponseWriter writer, AbstractPopupTextField popupTextField)
            throws UnifyException {
        SingleSelect singleSelect = (SingleSelect) popupTextField;

        writer.write("<div id=\"").write(singleSelect.getFramePanelId())
                .write("\" class=\"ssborder\" style=\"overflow-y:auto;overflow-x:hidden;\" tabindex=\"0\">");
        writer.write("<div id=\"").write(singleSelect.getListPanelId()).write("\" class=\"sslist\">");
        List<? extends Listable> listableList = singleSelect.getListables();
        int length = listableList.size();

        String blankOption = singleSelect.getBlankOption();
        if (blankOption != null) {
            writer.write("<a id=\"").write(singleSelect.getBlankOptionId()).write("\">");
            if (StringUtils.isBlank(blankOption)) {
                writer.writeHtmlFixedSpace();
            } else {
                writer.writeWithHtmlEscape(blankOption);

            }

            writer.write("</a>");
        }

        for (int i = 0; i < length; i++) {
            writer.write("<a");
            writeTagId(writer, singleSelect.getNamingIndexedId(i));
            writer.write(">");
            writer.write("</a>");
        }
        writer.write("</div>");
        writer.write("</div>");
    }

    @Override
    protected void doWritePopupTextFieldBehaviour(ResponseWriter writer, AbstractPopupTextField popupTextField,
            boolean popupEnabled) throws UnifyException {
        SingleSelect singleSelect = (SingleSelect) popupTextField;
        ListControlInfo listControlInfo = singleSelect.getListControlInfo(singleSelect.getFormatter());

        // Append rigging
        writer.beginFunction("ux.rigSingleSelect");
        writer.writeParam("pId", singleSelect.getId());
        writer.writeParam("pFacId", singleSelect.getFacadeId());
        writer.writeParam("pFrmId", singleSelect.getFramePanelId());
        writer.writeParam("pLstId", singleSelect.getListPanelId());
        writer.writeParam("pBlnkId", singleSelect.getBlankOptionId());
        writer.writeParam("pICnt", listControlInfo.size());
        writer.writeParam("pSelectIds", listControlInfo.getSelectIds());
        writer.writeParam("pKeys", listControlInfo.getKeys());
        writer.writeParam("pLabels", listControlInfo.getLabels());
        writer.writeParam("pIsBlankOption", singleSelect.getBlankOption() != null);
        writer.writeParam("pNormCls", "norm");
        writer.writeParam("pSelCls", getUserColorStyleClass("sel"));
        writer.writeParam("pEnabled", popupEnabled);
        writer.writeParam("pVal", singleSelect.getStringValue());
        writer.endFunction();
    }

    @Override
    protected String getOnShowAction() throws UnifyException {
        return "ux.ssOnShow";
    }

    @Override
    protected String getOnShowParam(AbstractPopupTextField popupTextField) throws UnifyException {
        SingleSelect singleSelect = (SingleSelect) popupTextField;
        JsonWriter jw = new JsonWriter();
        jw.beginObject();
        jw.write("pFrmId", singleSelect.getFramePanelId());
        jw.endObject();
        return jw.toString();
    }

    @Override
    protected String getOnHideAction() throws UnifyException {
        return null;
    }

    @Override
    protected String getOnHideParam(AbstractPopupTextField popupTextField) throws UnifyException {
        return null;
    }

}
