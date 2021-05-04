/*
 *       RedactionNewsListCmd.java
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

import cz.svjis.bean.MiniNews;
import cz.svjis.bean.MiniNewsDAO;
import cz.svjis.bean.Permission;
import cz.svjis.bean.SliderImpl;
import cz.svjis.servlet.CmdContext;
import cz.svjis.servlet.Command;
import cz.svjis.validator.Validator;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;

/**
 *
 * @author jaroslav_b
 */
public class RedactionNewsListCmd extends Command {

    public RedactionNewsListCmd(CmdContext ctx) {
        super(ctx);
    }

    @Override
    public void execute() throws Exception {
        
        if (!getUser().hasPermission(Permission.REDACTION_MINI_NEWS)) {
            new Error401UnauthorizedCmd(getCtx()).execute();
            return;
        }
        
        String parPage = Validator.getString(getRequest(), "page", 0, Validator.MAX_STRING_LEN_ALLOWED, false, false);
        int parPageNo = Validator.getInt(getRequest(), "pageNo", 0, Validator.MAX_INT_ALLOWED, true);
        
        MiniNewsDAO newsDao = new MiniNewsDAO(getCnn());
        
        int pageNo = (parPageNo == 0) ? 1 : parPageNo;
        int pageSize = getSetup().getArticlePageSize();
        
        SliderImpl sl = new SliderImpl();
        sl.setPageId(parPage);
        sl.setSliderWide(10);
        sl.setCurrentPage(pageNo);
        sl.setNumOfItemsAtPage(pageSize);
        sl.setTotalNumOfItems(newsDao.getMiniNewsSize(getUser(), false));
        getRequest().setAttribute("slider", sl);
        
        ArrayList<MiniNews> miniNewsList = new ArrayList<>(newsDao.getMiniNewsList(pageNo, pageSize, getUser(), false));
        getRequest().setAttribute("miniNewsList", miniNewsList);
        RequestDispatcher rd = getRequest().getRequestDispatcher("/WEB-INF/jsp/Redaction_MiniNewsList.jsp");
        rd.forward(getRequest(), getResponse());
    }
}
