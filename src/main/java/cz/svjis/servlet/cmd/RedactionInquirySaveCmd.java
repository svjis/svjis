/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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

        String parId = getRequest().getParameter("id");
        String parDescription = Validator.fixTextInput(getRequest().getParameter("description"), true);
        String parStartDate = Validator.fixTextInput(getRequest().getParameter("startingDate"), false);
        String parEndDate = Validator.fixTextInput(getRequest().getParameter("endingDate"), false);
        
        if (!validateInput(parId, parDescription, parStartDate, parEndDate)) {
            RequestDispatcher rd = getRequest().getRequestDispatcher("/InputValidationError.jsp");
            rd.forward(getRequest(), getResponse());
            return;
        }
        
        InquiryDAO inquiryDao = new InquiryDAO(getCnn());

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        int id = Integer.parseInt(parId);
        Inquiry i = new Inquiry();
        i.setId(id);
        i.setCompanyId(getUser().getCompanyId());
        i.setUserId(getUser().getId());
        i.setDescription(parDescription);
        i.setStartingDate(sdf.parse(parStartDate));
        i.setEndingDate(sdf.parse(parEndDate));
        i.setEnabled(getRequest().getParameter("publish") != null);
        ArrayList<InquiryOption> ioList = new ArrayList<InquiryOption>();
        int n = 1;
        while (getRequest().getParameter("oid_" + n) != null) {
            InquiryOption io = new InquiryOption();
            io.setId(Integer.valueOf(getRequest().getParameter("oid_" + n)));
            io.setInquiryId(i.getId());
            
            String parOptDesc = Validator.fixTextInput(getRequest().getParameter("o_" + n), false);
            if (!validateInput(parOptDesc)) {
                RequestDispatcher rd = getRequest().getRequestDispatcher("/InputValidationError.jsp");
                rd.forward(getRequest(), getResponse());
                return;
            }
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
        String url = "Dispatcher?page=redactionInquiryEdit&id=" + i.getId();
        getRequest().setAttribute("url", url);
        RequestDispatcher rd = getRequest().getRequestDispatcher("/_refresh.jsp");
        rd.forward(getRequest(), getResponse());
    }
    
    private boolean validateInput(String parId, String parDescription, String parStartDate, String parEndDate) {
        boolean result = true;
        
        if (!Validator.validateInteger(parId, 0, Validator.maxIntAllowed)) {
            result = false;
        }
        
        if (!Validator.validateString(parDescription, 0, Validator.maxStringLenAllowed)) {
            result = false;
        }
        
        if (!Validator.validateString(parStartDate, 0, 30)) {
            result = false;
        }
        
        if (!Validator.validateString(parEndDate, 0, 30)) {
            result = false;
        }

        return result;
    }
    
    private boolean validateInput(String parDescription) {
        boolean result = true;
        
        if (!Validator.validateString(parDescription, 0, 250)) {
            result = false;
        }

        return result;
    }
}


