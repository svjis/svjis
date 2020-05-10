/*
 *       RedactionInquiryLogCmd.java
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
import cz.svjis.bean.InquiryLog;
import cz.svjis.servlet.CmdContext;
import cz.svjis.servlet.Command;
import cz.svjis.validator.Validator;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;

/**
 *
 * @author jaroslav_b
 */
public class RedactionInquiryLogCmd extends Command {

    public RedactionInquiryLogCmd(CmdContext ctx) {
        super(ctx);
    }

    @Override
    public void execute() throws Exception {

        int parId = Validator.getInt(getRequest(), "id", 0, Validator.MAX_INT_ALLOWED, false);
        
        InquiryDAO inquiryDao = new InquiryDAO(getCnn());

        Inquiry inquiry = new Inquiry();
        ArrayList<InquiryLog> log = new ArrayList<>();
        if (parId != 0) {
            inquiry = inquiryDao.getInquiry(getUser(), parId);
            log = new ArrayList<>(inquiryDao.getInquiryLog(getUser(), parId));
        }
        getRequest().setAttribute("inquiry", inquiry);
        getRequest().setAttribute("log", log);
        RequestDispatcher rd = getRequest().getRequestDispatcher("/Redaction_InquiryLog.jsp");
        rd.forward(getRequest(), getResponse());
    }
}
