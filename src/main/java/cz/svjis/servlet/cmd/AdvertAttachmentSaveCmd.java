/*
 *       AdvertAttachmentSaveCmd.java
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

import java.io.File;
import java.util.Date;
import java.util.List;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import cz.svjis.bean.Advert;
import cz.svjis.bean.AdvertDAO;
import cz.svjis.bean.Attachment;
import cz.svjis.bean.Permission;
import cz.svjis.servlet.CmdContext;
import cz.svjis.servlet.CmdFactory;
import cz.svjis.servlet.Command;
import cz.svjis.validator.Validator;

public class AdvertAttachmentSaveCmd extends Command {

    public AdvertAttachmentSaveCmd(CmdContext ctx) {
        super(ctx);
    }

    @Override
    public void execute() throws Exception {
        
        if (!getUser().hasPermission(Permission.CAN_INSERT_ADVERT)) {
            new Error401UnauthorizedCmd(getCtx()).execute();
            return;
        }

        int parAdvertId = Validator.getInt(getRequest(), "advertId", 0, Validator.MAX_INT_ALLOWED, false);

        AdvertDAO advertDao = new AdvertDAO(getCnn());

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
                fa.setDocumentId(parAdvertId);
                fa.setUploadTime(new Date());
                
                Advert ad = advertDao.getAdvert(getCompany().getId(), fa.getDocumentId());
                if (ad == null) {
                    new Error404NotFoundCmd(getCtx()).execute();
                    return;
                }
                if (getUser().getId() != ad.getUser().getId()) {
                    new Error401UnauthorizedCmd(getCtx()).execute();
                    return;
                }
                if (!fa.getFileName().equals("")) {
                    advertDao.insertAttachment(fa);
                }
            }
        }
        String url = String.format("Dispatcher?page=%s&id=%d", CmdFactory.ADVERT_EDIT, parAdvertId);
        getResponse().sendRedirect(url);
    }
}
