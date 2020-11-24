/*
 *       RedactionInquirySaveCmd.java
 *
 *       This file is part of SVJIS project.
 *       https://github.com/svjis/svjis
 *
 *       SVJIS is free software; you can redistribute it and/or modify
 *       it under the terms of the GNU General Public License as published by
 *       the Free Software Foundation; either version 3 of the License, or
 *       (at your option) any later version. <http://www.gnu.org/licenses/>
 */

package cz.svjis.servlet.cmd;

import cz.svjis.bean.Inquiry;
import cz.svjis.bean.InquiryDAO;
import cz.svjis.bean.InquiryOption;
import cz.svjis.servlet.CmdContext;
import cz.svjis.servlet.Command;
import cz.svjis.validator.Validator;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;

/**
 *
 * @author jaroslav_b
 */
public class RedactionInquirySaveCmd extends Command {

    public RedactionInquirySaveCmd(CmdContext ctx) {
        super(ctx);
    }

    @Override
    public void execute() throws Exception {

        int parId = Validator.getInt(getRequest(), "id", 0, Validator.MAX_INT_ALLOWED, false);
        String parDescription = Validator.getString(getRequest(), "description", 0, 250, false, getUser().hasPermission("can_write_html"));
        String parStartDate = Validator.getString(getRequest(), "startingDate", 0, 30, false, false);
        String parEndDate = Validator.getString(getRequest(), "endingDate", 0, 30, false, false);
        boolean parEnabled = Validator.getBoolean(getRequest(), "publish");

        InquiryDAO inquiryDao = new InquiryDAO(getCnn());

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        Inquiry i;
        if (parId == 0) {
            i= new Inquiry();
            i.getUser().setId(getUser().getId());
            i.setCompanyId(getUser().getCompanyId());
        } else {
            i = inquiryDao.getInquiry(getUser(), parId);
        }
        i.setDescription(parDescription);
        i.setStartingDate(sdf.parse(parStartDate));
        i.setEndingDate(sdf.parse(parEndDate));
        i.setEnabled(parEnabled);
        ArrayList<InquiryOption> ioList = new ArrayList<>();
        int n = 1;
        while (getRequest().getParameter("oid_" + n) != null) {
            InquiryOption io = new InquiryOption();
            io.setId(Integer.valueOf(getRequest().getParameter("oid_" + n)));
            io.setInquiryId(i.getId());
            
            String parOptDesc = Validator.getString(getRequest(), "o_" + n, 0, 250, false, false);
            io.setDescription(parOptDesc);
            
            if (!io.getDescription().equals("")) {
                ioList.add(io);
            }
            n++;
        }
        i.setOptionList(ioList);

        if (i.getId() == 0) {
            i.setId(inquiryDao.insertInquiry(i));
        } else {
            inquiryDao.modifyInquiry(i);
        }
        
        getRequest().setAttribute("inquiry", i);
        String message = getLanguage().getText("Saved") + "<br>";
        getRequest().setAttribute("message", message);
        RequestDispatcher rd = getRequest().getRequestDispatcher("/Redaction_InquiryEdit.jsp");
        rd.forward(getRequest(), getResponse());
    }
}
