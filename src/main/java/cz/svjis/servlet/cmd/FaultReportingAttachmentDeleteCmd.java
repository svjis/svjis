/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.svjis.servlet.cmd;

import cz.svjis.bean.FaultReport;
import cz.svjis.bean.FaultReportAttachment;
import cz.svjis.bean.FaultReportDAO;
import cz.svjis.servlet.CmdContext;
import cz.svjis.servlet.Command;
import cz.svjis.validator.Validator;
import javax.servlet.RequestDispatcher;

/**
 *
 * @author jarberan
 */
public class FaultReportingAttachmentDeleteCmd extends Command {
    
    public FaultReportingAttachmentDeleteCmd(CmdContext ctx) {
        super(ctx);
    }
    
    @Override
    public void execute() throws Exception {

        int parId = Validator.getInt(getRequest(), "id", 0, Validator.maxIntAllowed, false);

        FaultReportDAO faultDao = new FaultReportDAO(getCnn());

        int id = parId;
        FaultReportAttachment fa = faultDao.getFaultReportAttachment(id);
        if (fa == null) {
            RequestDispatcher rd = getRequest().getRequestDispatcher("/InputValidationError.jsp");
            rd.forward(getRequest(), getResponse());
            return;
        }
        FaultReport f = faultDao.getFault(getCompany().getId(), fa.getFaultReportId());
        if ((f != null) && (!f.isClosed()) && (fa.getUser().getId() == getUser().getId())) {
            faultDao.deleteFaultAttachment(id);
        }
        String url = "Dispatcher?page=faultDetail&id=" + fa.getFaultReportId();
        getRequest().setAttribute("url", url);
        RequestDispatcher rd = getRequest().getRequestDispatcher("/_refresh.jsp");
        rd.forward(getRequest(), getResponse());
    }
}