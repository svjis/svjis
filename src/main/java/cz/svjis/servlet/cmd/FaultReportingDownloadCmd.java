/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.svjis.servlet.cmd;

import cz.svjis.bean.FaultReportAttachment;
import cz.svjis.bean.FaultReportDAO;
import cz.svjis.common.HttpUtils;
import cz.svjis.servlet.CmdContext;
import cz.svjis.servlet.Command;
import cz.svjis.validator.Validator;
import javax.servlet.RequestDispatcher;

/**
 *
 * @author jarberan
 */
public class FaultReportingDownloadCmd extends Command {
    
    public FaultReportingDownloadCmd(CmdContext ctx) {
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
        
        FaultReportDAO faultDao = new FaultReportDAO(getCnn());
        FaultReportAttachment fa = faultDao.getFaultReportAttachment(Integer.parseInt(parId));
        if ((fa == null) || (faultDao.getFault(getCompany().getId(), fa.getFaultReportId()) == null)) {
            RequestDispatcher rd = getRequest().getRequestDispatcher("/InputValidationError.jsp");
            rd.forward(getRequest(), getResponse());
            return;
        }
        HttpUtils.writeBinaryData(fa.getContentType(), fa.getFileName(), fa.getData(), getRequest(), getResponse());
    }
    
    private boolean validateInput(String id) {
        boolean result = true;
        
        if (!Validator.validateInteger(id, 0, Validator.maxIntAllowed)) {
            result = false;
        }
        
        return result;
    }
}
