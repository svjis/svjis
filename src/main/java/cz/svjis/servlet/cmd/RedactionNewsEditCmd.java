/*
 *       RedactionNewsEditCmd.java
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

import cz.svjis.bean.Language;
import cz.svjis.bean.LanguageDAO;
import cz.svjis.bean.MiniNews;
import cz.svjis.bean.MiniNewsDAO;
import cz.svjis.servlet.CmdContext;
import cz.svjis.servlet.Command;
import cz.svjis.validator.Validator;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;

/**
 *
 * @author jaroslav_b
 */
public class RedactionNewsEditCmd extends Command {

    public RedactionNewsEditCmd(CmdContext ctx) {
        super(ctx);
    }

    @Override
    public void execute() throws Exception {

        int parId = Validator.getInt(getRequest(), "id", 0, Validator.MAX_INT_ALLOWED, false);

        MiniNewsDAO newsDao = new MiniNewsDAO(getCnn());
        LanguageDAO languageDao = new LanguageDAO(getCnn());

        MiniNews miniNews = new MiniNews();
        if (parId != 0) {
            miniNews = newsDao.getMiniNews(getUser(), parId);
        }
        getRequest().setAttribute("miniNews", miniNews);
        ArrayList<Language> languageList = new ArrayList(languageDao.getLanguageList());
        getRequest().setAttribute("languageList", languageList);
        RequestDispatcher rd = getRequest().getRequestDispatcher("/Redaction_MiniNewsEdit.jsp");
        rd.forward(getRequest(), getResponse());
    }
}
