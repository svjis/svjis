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

import javax.servlet.RequestDispatcher;

import cz.svjis.bean.Advert;
import cz.svjis.bean.AdvertDAO;
import cz.svjis.bean.Attachment;
import cz.svjis.bean.Permission;
import cz.svjis.servlet.CmdContext;
import cz.svjis.servlet.CmdFactory;
import cz.svjis.servlet.Command;
import cz.svjis.validator.Validator;

public class AdvertAttachmentDeleteCmd extends Command {

    public AdvertAttachmentDeleteCmd(CmdContext ctx) {
        super(ctx);
    }

    @Override
    public void execute() throws Exception {

        int parId = Validator.getInt(getRequest(), "id", 0, Validator.MAX_INT_ALLOWED, false);

        AdvertDAO advertDao = new AdvertDAO(getCnn());

        int id = parId;
        Attachment at = advertDao.getAttachment(id);
        if (at == null) {
            RequestDispatcher rd = getRequest().getRequestDispatcher("/WEB-INF/jsp/InputValidationError.jsp");
            rd.forward(getRequest(), getResponse());
            return;
        }
        Advert a = advertDao.getAdvert(getCompany().getId(), at.getDocumentId());
        if ((a != null) && getUser().hasPermission(Permission.CAN_INSERT_ADVERT) && (at.getUser().getId() == getUser().getId()) ) {
            advertDao.deleteAttachment(id);
        }
        String url = String.format("Dispatcher?page=%s&id=%d", CmdFactory.ADVERT_EDIT, at.getDocumentId());
        getResponse().sendRedirect(url);
    }
}
