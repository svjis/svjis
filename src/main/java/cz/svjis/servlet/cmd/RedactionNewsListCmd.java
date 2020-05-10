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
import cz.svjis.servlet.CmdContext;
import cz.svjis.servlet.Command;
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
        MiniNewsDAO newsDao = new MiniNewsDAO(getCnn());
        
        ArrayList<MiniNews> miniNewsList = new ArrayList(newsDao.getMiniNews(getUser(), false));
        getRequest().setAttribute("miniNewsList", miniNewsList);
        RequestDispatcher rd = getRequest().getRequestDispatcher("/Redaction_MiniNewsList.jsp");
        rd.forward(getRequest(), getResponse());
    }
}
