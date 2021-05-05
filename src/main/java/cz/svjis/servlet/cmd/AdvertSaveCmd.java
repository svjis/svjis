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

import javax.servlet.RequestDispatcher;

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
        String parPhone = Validator.getString(getRequest(), "phone", 0, 30, false, false);
        String parEmail = Validator.getString(getRequest(), "e-mail", 0, 50, false, false);
        int parType = Validator.getInt(getRequest(), "typeId", 0, Validator.MAX_INT_ALLOWED, true);
        boolean parPublished = Validator.getBoolean(getRequest(), "published");
        
        AdvertDAO advertDao = new AdvertDAO(getCnn());
        
        if (!getUser().hasPermission(Permission.CAN_INSERT_ADVERT)) {
            RequestDispatcher rd = getRequest().getRequestDispatcher("/WEB-INF/jsp/404_NotFound.jsp");
            getResponse().setStatus(404);
            rd.forward(getRequest(), getResponse());
            return;
        }
        
        Advert a = null;
            
        if (parId == 0) {
            a = new Advert();
            a.setCompanyId(getCompany().getId());
            a.getUser().setId(getUser().getId());
        } else {
            a = advertDao.getAdvert(getCompany().getId(), parId);
        }
        
        a.setHeader(parHeader);
        a.setBody(parBody);
        a.setPhone(parPhone);
        a.seteMail(parEmail);
        a.getType().setId(parType);
        a.setPublished(parPublished);
        
        if (a.getId() == 0) {
            advertDao.insertAdvert(a);
        } else {
            if (getUser().getId() == a.getUser().getId()) {
                advertDao.modifyAdvert(a);
            } else {
                RequestDispatcher rd = getRequest().getRequestDispatcher("/WEB-INF/jsp/404_NotFound.jsp");
                getResponse().setStatus(404);
                rd.forward(getRequest(), getResponse());
                return;
            }
        }
        
        int typeId = a.getType().getId();
        if (!a.isPublished()) {
            typeId = AdvertDAO.MY_ADVERTS_TYPE_ID;
        }

        String url = String.format("Dispatcher?page=%s&typeId=%d", CmdFactory.ADVERT_LIST, typeId);
        getResponse().sendRedirect(url);
    }

}
