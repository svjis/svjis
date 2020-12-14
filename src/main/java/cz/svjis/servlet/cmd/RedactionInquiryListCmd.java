/*
 *       RedactionInquiryListCmd.java
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
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;

/**
 *
 * @author jaroslav_b
 */
public class RedactionInquiryListCmd extends Command {

    public RedactionInquiryListCmd(CmdContext ctx) {
        super(ctx);
    }

    @Override
    public void execute() throws Exception {
        InquiryDAO inquiryDao = new InquiryDAO(getCnn());
        ArrayList<Inquiry> inquiryList = new ArrayList(inquiryDao.getInquiryList(getUser(), false));
        getRequest().setAttribute("inquiryList", inquiryList);
        RequestDispatcher rd = getRequest().getRequestDispatcher("/WEB-INF/jsp/Redaction_InquiryList.jsp");
        rd.forward(getRequest(), getResponse());
    }
}
