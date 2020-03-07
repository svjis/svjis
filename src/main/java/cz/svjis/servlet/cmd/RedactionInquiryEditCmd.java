/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
        RequestDispatcher rd = getRequest().getRequestDispatcher("/Redaction_InquiryEdit.jsp");
        rd.forward(getRequest(), getResponse());
    }
}
