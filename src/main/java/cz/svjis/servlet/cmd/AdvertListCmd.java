/*
 *       AdvertListCmd.java
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
import cz.svjis.bean.SliderImpl;
import cz.svjis.servlet.CmdContext;
import cz.svjis.servlet.Command;
import cz.svjis.validator.Validator;

public class AdvertListCmd extends Command {
    
    public AdvertListCmd(CmdContext ctx) {
        super(ctx);
    }

    @Override
    public void execute() throws Exception {
        
        String parPage = Validator.getString(getRequest(), "page", 0, Validator.MAX_STRING_LEN_ALLOWED, false, false);
        int parTypeId = Validator.getInt(getRequest(), "typeId", 0, Validator.MAX_INT_ALLOWED, true);
        int parPageNo = Validator.getInt(getRequest(), "pageNo", 0, Validator.MAX_INT_ALLOWED, true);
        
        AdvertDAO advertDao = new AdvertDAO(getCnn());
        
        if (parTypeId == 0) {
            parTypeId = getSetup().getAdvertMenuDefault();
        }
        
        int pageNo = (parPageNo == 0) ? 1 : parPageNo;
        int pageSize = getSetup().getAdvertPageSize();
        
        SliderImpl sl = new SliderImpl();
        sl.setPageId(parPage);
        sl.setSliderWide(10);
        sl.setCurrentPage(pageNo);
        sl.setNumOfItemsAtPage(pageSize);
        int listSize;
        if (parTypeId == AdvertDAO.MY_ADVERTS_TYPE_ID) {
            listSize = advertDao.getAdverListSize(AdvertDAO.AdvertListType.USER, getCompany().getId(), getUser().getId());
        } else {
            listSize = advertDao.getAdverListSize(AdvertDAO.AdvertListType.ADVERT_TYPE, getCompany().getId(), parTypeId);
        }
        sl.setTotalNumOfItems(listSize);
        getRequest().setAttribute("slider", sl);
        
        getRequest().setAttribute("menuId", Integer.toString(parTypeId));
        
        ArrayList<AdvertType> advertMenuList = new ArrayList<>(advertDao.getAdvertTypeList(getCompany().getId()));
        AdvertType at = advertDao.getMyAdvertType(getCompany().getId(), getUser().getId());
        at.setDescription(getLanguage().getText(at.getDescription()));
        advertMenuList.add(at);
        getRequest().setAttribute("advertMenuList", advertMenuList);
        
        ArrayList<AdvertType> advertTypeList = new ArrayList<>(advertDao.getAdvertTypeList(getCompany().getId()));
        getRequest().setAttribute("advertTypeList", advertTypeList);
        
        ArrayList<Advert> advertList;
        RequestDispatcher rd;
        if (parTypeId == AdvertDAO.MY_ADVERTS_TYPE_ID) {
            advertList = new ArrayList<>(advertDao.getAdvertList(pageNo, pageSize, AdvertDAO.AdvertListType.USER, getCompany().getId(), getUser().getId()));
            rd = getRequest().getRequestDispatcher("/WEB-INF/jsp/AdvertMyList.jsp");
        } else {
            advertList = new ArrayList<>(advertDao.getAdvertList(pageNo, pageSize, AdvertDAO.AdvertListType.ADVERT_TYPE, getCompany().getId(), parTypeId));
            rd = getRequest().getRequestDispatcher("/WEB-INF/jsp/AdvertList.jsp");
        }
        getRequest().setAttribute("advertList", advertList);
            
        rd.forward(getRequest(), getResponse());
    }

}
