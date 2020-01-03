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
import cz.svjis.validator.InputValidationException;
import cz.svjis.validator.Validator;

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
        
        int parId = Validator.getInt(getRequest(), "id", 0, Validator.maxIntAllowed, false);

        FaultReportDAO faultDao = new FaultReportDAO(getCnn());
        FaultReportAttachment fa = faultDao.getFaultReportAttachment(parId);
        if ((fa == null) || (faultDao.getFault(getCompany().getId(), fa.getFaultReportId()) == null)) {
            throw new InputValidationException("Bad attachment id");
        }
        HttpUtils.writeBinaryData(fa.getContentType(), fa.getFileName(), fa.getData(), getRequest(), getResponse());
    }
}
