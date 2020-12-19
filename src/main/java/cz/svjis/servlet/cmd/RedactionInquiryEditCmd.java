/*
 *       RedactionInquiryEditCmd.java
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
import cz.svjis.servlet.CmdContext;
import cz.svjis.servlet.Command;
import cz.svjis.validator.Validator;
import javax.servlet.RequestDispatcher;

/**
 *
 * @author jaroslav_b
 */
public class RedactionInquiryEditCmd extends Command {

    public RedactionInquiryEditCmd(CmdContext ctx) {
        super(ctx);
    }

    @Override
    public void execute() throws Exception {

        int parId = Validator.getInt(getRequest(), "id", 0, Validator.MAX_INT_ALLOWED, false);

        InquiryDAO inquiryDao = new InquiryDAO(getCnn());

        Inquiry inquiry = new Inquiry();
        if (parId != 0) {
            inquiry = inquiryDao.getInquiry(getUser(), parId);
        }
        getRequest().setAttribute("inquiry", inquiry);
        RequestDispatcher rd = getRequest().getRequestDispatcher("/WEB-INF/jsp/Redaction_InquiryEdit.jsp");
        rd.forward(getRequest(), getResponse());
    }
}
