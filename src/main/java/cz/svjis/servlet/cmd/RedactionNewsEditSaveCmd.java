/*
 *       RedactionNewsEditSaveCmd.java
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
import cz.svjis.bean.Permission;
import cz.svjis.servlet.CmdContext;
import cz.svjis.servlet.Command;
import cz.svjis.validator.Validator;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;

/**
 *
 * @author jaroslav_b
 */
public class RedactionNewsEditSaveCmd extends Command {

    public RedactionNewsEditSaveCmd(CmdContext ctx) {
        super(ctx);
    }

    @Override
    public void execute() throws Exception {
        
        int parId = Validator.getInt(getRequest(), "id", 0, Validator.MAX_INT_ALLOWED, false);
        String parTime = Validator.getString(getRequest(), "time", 0, 30, false, false);
        int parLangId = Validator.getInt(getRequest(), "language", 0, Validator.MAX_INT_ALLOWED, false);
        String parBody = Validator.getString(getRequest(), "body", 0, Validator.MAX_STRING_LEN_ALLOWED, false, getUser().hasPermission(Permission.CAN_WRITE_HTML));
        boolean parPublished = Validator.getBoolean(getRequest(), "publish");

        MiniNewsDAO newsDao = new MiniNewsDAO(getCnn());
        LanguageDAO languageDao = new LanguageDAO(getCnn());
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");

        MiniNews n = null;
        if (parId == 0) {
            n = new MiniNews();
            n.setId(parId);
            n.getCreatedBy().setId(getUser().getId());
            n.setCompanyId(getUser().getCompanyId());
        } else {
            n = newsDao.getMiniNews(getUser(), parId);
        }
        n.setTime(sdf.parse(parTime));
        n.setLanguageId(parLangId);
        n.setPublished(parPublished);
        n.setBody(parBody);
        if (parId == 0) {
            n.setId(newsDao.insertMiniNews(n));
        } else {
            newsDao.modifyMiniNews(n);
        }
        
        n = newsDao.getMiniNews(getUser(), n.getId());
        getRequest().setAttribute("miniNews", n);
        ArrayList<Language> languageList = new ArrayList(languageDao.getLanguageList());
        getRequest().setAttribute("languageList", languageList);
        String message = getLanguage().getText("Saved") + "<br>";
        getRequest().setAttribute("message", message);
        RequestDispatcher rd = getRequest().getRequestDispatcher("/WEB-INF/jsp/Redaction_MiniNewsEdit.jsp");
        rd.forward(getRequest(), getResponse());
    }
}
