/*
 *       AdvertEditCmd.java
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

import java.util.ArrayList;

import javax.servlet.RequestDispatcher;

import cz.svjis.bean.Advert;
import cz.svjis.bean.AdvertDAO;
import cz.svjis.bean.AdvertType;
import cz.svjis.servlet.CmdContext;
import cz.svjis.servlet.Command;
import cz.svjis.validator.Validator;

public class AdvertEditCmd extends Command {

    public AdvertEditCmd(CmdContext cmdCtx) {
        super(cmdCtx);
    }

    @Override
    public void execute() throws Exception {

        int parId = Validator.getInt(getRequest(), "id", 0, Validator.MAX_INT_ALLOWED, false);
        
        AdvertDAO advertDao = new AdvertDAO(getCnn());
        
        ArrayList<AdvertType> advertMenuList = new ArrayList<>(advertDao.getAdvertTypeList(getCompany().getId()));
        AdvertType at = advertDao.getMyAdvertType(getCompany().getId(), getUser().getId());
        at.setDescription(getLanguage().getText(at.getDescription()));
        advertMenuList.add(at);
        getRequest().setAttribute("advertMenuList", advertMenuList);
        
        getRequest().setAttribute("currMenu", at);
        
        ArrayList<AdvertType> advertTypeList = new ArrayList<>(advertDao.getAdvertTypeList(getCompany().getId()));
        getRequest().setAttribute("advertTypeList", advertTypeList);
        

        Advert advert = new Advert();
        if (parId != 0) {
            advert = advertDao.getAdvert(getCompany().getId(), parId);
            if (getUser().getId() != advert.getUser().getId()) {
                RequestDispatcher rd = getRequest().getRequestDispatcher("/WEB-INF/jsp/404_NotFound.jsp");
                getResponse().setStatus(404);
                rd.forward(getRequest(), getResponse());
                return;
            }
        } else {
            advert.setPhone(getUser().getCellPhone());
            advert.seteMail(getUser().geteMail());
            advert.setPublished(true);
        }
        getRequest().setAttribute("advert", advert);
        
        RequestDispatcher rd = getRequest().getRequestDispatcher("/WEB-INF/jsp/AdvertEdit.jsp");
        rd.forward(getRequest(), getResponse());
    }

}
