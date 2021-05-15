/*
 *       AdvertAttachmentDeleteCmd.java
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
import cz.svjis.servlet.Cmd;
import cz.svjis.servlet.CmdContext;
import cz.svjis.servlet.Command;
import cz.svjis.validator.Validator;

public class AdvertAttachmentDeleteCmd extends Command {

    public AdvertAttachmentDeleteCmd(CmdContext ctx) {
        super(ctx);
    }

    @Override
    public void execute() throws Exception {
        
        if (!getUser().hasPermission(Permission.CAN_INSERT_ADVERT)) {
            new Error401UnauthorizedCmd(getCtx()).execute();
            return;
        }

        int parId = Validator.getInt(getRequest(), "id", 0, Validator.MAX_INT_ALLOWED, false);

        AdvertDAO advertDao = new AdvertDAO(getCnn());

        int id = parId;
        
        Attachment at = advertDao.getAttachment(id);
        if (at == null) {
            new Error404NotFoundCmd(getCtx()).execute();
            return;
        }
        
        Advert a = advertDao.getAdvert(getCompany().getId(), at.getDocumentId());
        if (a == null) {
            new Error404NotFoundCmd(getCtx()).execute();
            return;
        }
        
        if (at.getUser().getId() != getUser().getId()) {
            new Error401UnauthorizedCmd(getCtx()).execute();
            return;
        }
        
        advertDao.deleteAttachment(id);
        
        String url = String.format("Dispatcher?page=%s&id=%d", Cmd.ADVERT_EDIT, at.getDocumentId());
        getResponse().sendRedirect(url);
    }
}
