/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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

        int parId = Validator.getInt(getRequest(), "id", 0, Validator.maxIntAllowed, false);
        
        InquiryDAO inquiryDao = new InquiryDAO(getCnn());

        Inquiry inquiry = new Inquiry();
        ArrayList<InquiryLog> log = new ArrayList<InquiryLog>();
        if (parId != 0) {
            inquiry = inquiryDao.getInquiry(getUser(), parId);
            log = inquiryDao.getInquiryLog(getUser(), parId);
        }
        getRequest().setAttribute("inquiry", inquiry);
        getRequest().setAttribute("log", log);
        RequestDispatcher rd = getRequest().getRequestDispatcher("/Redaction_InquiryLog.jsp");
        rd.forward(getRequest(), getResponse());
    }
}
