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
import java.io.File;
import java.util.Date;
import java.util.List;
import javax.servlet.RequestDispatcher;
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

        int parReportId = Validator.getInt(getRequest(), "reportId", 0, Validator.maxIntAllowed, false);
        
        FaultReportDAO faultDao = new FaultReportDAO(getCnn());
        
        int reportId = parReportId;
        FileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload upload = new ServletFileUpload(factory);
        //upload.setSizeMax(yourMaxRequestSize);
        List<FileItem> items = upload.parseRequest(getRequest());
        for (FileItem item: items) {
            File f = new File(item.getName());
            if (!item.isFormField()) {
                FaultReportAttachment fa = new FaultReportAttachment();
                String fileName = f.getName().replace(" ", "_");
                if (fileName.lastIndexOf("\\") > -1) {
                    fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
                }
                fa.setFileName(fileName);
                fa.setContentType(item.getContentType());
                fa.setData(item.get());
                fa.setUser(getUser());
                fa.setFaultReportId(reportId);
                fa.setUploadTime(new Date());
                
                FaultReport fr = faultDao.getFault(getCompany().getId(), fa.getFaultReportId());
                if ((fr != null) && (!fr.isClosed()) && !fa.getFileName().equals("")) {
                    faultDao.insertFaultReportAttachment(fa);
                }
            }
        }
        String url = "Dispatcher?page=faultDetail&id=" + reportId;
        getRequest().setAttribute("url", url);
        RequestDispatcher rd = getRequest().getRequestDispatcher("/_refresh.jsp");
        rd.forward(getRequest(), getResponse());
    }
}
