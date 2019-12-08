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

        String parId = getRequest().getParameter("id");
        
        if (!validateInput(parId)) {
            RequestDispatcher rd = getRequest().getRequestDispatcher("/InputValidationError.jsp");
            rd.forward(getRequest(), getResponse());
            return;
        }
        
        InquiryDAO inquiryDao = new InquiryDAO(getCnn());

        int id = Integer.parseInt(parId);
        Inquiry inquiry = new Inquiry();
        if (id != 0) {
            inquiry = inquiryDao.getInquiry(getUser(), id);
        }
        getRequest().setAttribute("inquiry", inquiry);
        RequestDispatcher rd = getRequest().getRequestDispatcher("/Redaction_InquiryEdit.jsp");
        rd.forward(getRequest(), getResponse());
    }
    
    private boolean validateInput(String parId) {
        boolean result = true;
        
        if (!Validator.validateInteger(parId, 0, Validator.maxIntAllowed)) {
            result = false;
        }

        return result;
    }
}
