/*
 *       FaultReportingAttachmentSaveCmd.java
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

import cz.svjis.bean.FaultReport;
import cz.svjis.bean.Attachment;
import cz.svjis.bean.FaultReportDAO;
import cz.svjis.bean.LogDAO;
import cz.svjis.bean.Permission;
import cz.svjis.servlet.Cmd;
import cz.svjis.servlet.CmdContext;
import cz.svjis.servlet.Command;
import cz.svjis.validator.Validator;
import java.io.File;
import java.util.Date;
import java.util.List;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 *
 * @author jarberan
 */
public class FaultReportingAttachmentSaveCmd extends Command {
    
    public FaultReportingAttachmentSaveCmd(CmdContext ctx) {
        super(ctx);
    }

    @Override
    public void execute() throws Exception {
        
        if (!getUser().hasPermission(Permission.MENU_FAULT_REPORTING)) {
            new Error401UnauthorizedCmd(getCtx()).execute();
            return;
        }

        int parReportId = Validator.getInt(getRequest(), "reportId", 0, Validator.MAX_INT_ALLOWED, false);

        FaultReportDAO faultDao = new FaultReportDAO(getCnn());
        LogDAO logDao = new LogDAO(getCnn());

        int reportId = parReportId;
        FileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload upload = new ServletFileUpload(factory);
        List<FileItem> items = upload.parseRequest(getRequest());
        for (FileItem item: items) {
            File f = new File(item.getName());
            if (!item.isFormField()) {
                Attachment fa = new Attachment();
                String fileName = f.getName().replace(" ", "_");
                if (fileName.lastIndexOf('\\') > -1) {
                    fileName = fileName.substring(fileName.lastIndexOf('\\') + 1);
                }
                fa.setFileName(fileName);
                fa.setContentType(item.getContentType());
                fa.setData(item.get());
                fa.setUser(getUser());
                fa.setDocumentId(reportId);
                fa.setUploadTime(new Date());
                
                FaultReport fr = faultDao.getFault(getCompany().getId(), fa.getDocumentId());
                if (fr == null) {
                    new Error404NotFoundCmd(getCtx()).execute();
                    return;
                }
                if (!fr.isClosed() && !fa.getFileName().equals("")) {
                    faultDao.insertFaultReportAttachment(fa);
                    logDao.log(getUser().getId(), LogDAO.OPERATION_TYPE_INSERT_FAULT_ATTACHMENT, fr.getId(), getRequest().getRemoteAddr(), getRequest().getHeader("User-Agent"));
                }
            }
        }
        String url = String.format("Dispatcher?page=%s&id=%d", Cmd.FAULT_DETAIL, reportId);
        getResponse().sendRedirect(url);
    }
}
