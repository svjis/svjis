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
        InquiryDAO inquiryDao = new InquiryDAO(getCnn());
        
        int id = Integer.parseInt(getRequest().getParameter("id"));
        Inquiry inquiry = new Inquiry();
        ArrayList<InquiryLog> log = new ArrayList<InquiryLog>();
        if (id != 0) {
            inquiry = inquiryDao.getInquiry(getUser(), id);
            log = inquiryDao.getInquiryLog(getUser(), id);
        }
        getRequest().setAttribute("inquiry", inquiry);
        getRequest().setAttribute("log", log);
        RequestDispatcher rd = getRequest().getRequestDispatcher("/Redaction_InquiryLog.jsp");
        rd.forward(getRequest(), getResponse());
    }
}
