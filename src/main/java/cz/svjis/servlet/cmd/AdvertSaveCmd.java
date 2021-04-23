/*
 *       AdvertSaveCmd.java
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
import cz.svjis.bean.Permission;
import cz.svjis.servlet.CmdContext;
import cz.svjis.servlet.CmdFactory;
import cz.svjis.servlet.Command;
import cz.svjis.validator.Validator;

public class AdvertSaveCmd extends Command {

    public AdvertSaveCmd(CmdContext cmdCtx) {
        super(cmdCtx);
    }

    @Override
    public void execute() throws Exception {
        
        int parId = Validator.getInt(getRequest(), "id", 0, Validator.MAX_INT_ALLOWED, false);
        String parHeader = Validator.getString(getRequest(), "header", 0, 50, false, false);
        String parBody = Validator.getString(getRequest(), "body", 0, Validator.MAX_STRING_LEN_ALLOWED, false, getUser().hasPermission(Permission.CAN_WRITE_HTML));
        int parType = Validator.getInt(getRequest(), "typeId", 0, Validator.MAX_INT_ALLOWED, true);
        boolean parPublished = Validator.getBoolean(getRequest(), "published");
        
        AdvertDAO advertDao = new AdvertDAO(getCnn());
        
        Advert a = null;
        
        if (getUser().hasPermission(Permission.CAN_INSERT_ADVERT)) {
            if (parId == 0) {
                a = new Advert();
                a.setCompanyId(getCompany().getId());
                a.getUser().setId(getUser().getId());
            } else {
                a = advertDao.getAdvert(getCompany().getId(), parId);
            }
            
            a.setHeader(parHeader);
            a.setBody(parBody);
            a.getType().setId(parType);
            a.setPublished(parPublished);
            
            if (a.getId() == 0) {
                advertDao.insertAdvert(a);
            } else {
                advertDao.modifyAdvert(a);
            }
        }
        
        String url = String.format("Dispatcher?page=%s&typeId=%d", CmdFactory.ADVERT_LIST, (a != null) ? a.getType().getId() : 0);
        getResponse().sendRedirect(url);
    }

}
