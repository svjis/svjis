/*
 *       AdvertAttachmentDownloadCmd.java
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

import cz.svjis.bean.Advert;
import cz.svjis.bean.AdvertDAO;
import cz.svjis.bean.Attachment;
import cz.svjis.bean.Permission;
import cz.svjis.common.JspSnippets;
import cz.svjis.servlet.CmdContext;
import cz.svjis.servlet.Command;
import cz.svjis.validator.Validator;

public class AdvertAttachmentDownloadCmd extends Command {

    public AdvertAttachmentDownloadCmd(CmdContext ctx) {
        super(ctx);
    }
    
    @Override
    public void execute() throws Exception {
        
        if (!getUser().hasPermission(Permission.MENU_ADVERTS)) {
            new Error401UnauthorizedCmd(getCtx()).execute();
            return;
        }
        
        int parId = Validator.getInt(getRequest(), "id", 0, Validator.MAX_INT_ALLOWED, false);

        AdvertDAO advertDao = new AdvertDAO(getCnn());
        
        Attachment at = advertDao.getAttachment(parId);
        if (at == null) {
            new Error404NotFoundCmd(getCtx()).execute();
            return;
        }
        
        Advert a = advertDao.getAdvert(getCompany().getId(), at.getDocumentId());
        if (a == null) {
            new Error404NotFoundCmd(getCtx()).execute();
            return;
        }
        
        JspSnippets.writeBinaryData(at.getContentType(), at.getFileName(), at.getData(), getRequest(), getResponse());
    }
}
