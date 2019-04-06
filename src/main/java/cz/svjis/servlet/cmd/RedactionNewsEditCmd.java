/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.svjis.servlet.cmd;

import cz.svjis.bean.Language;
import cz.svjis.bean.LanguageDAO;
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
public class RedactionNewsEditCmd extends Command {

    public RedactionNewsEditCmd(CmdContext ctx) {
        super(ctx);
    }

    @Override
    public void execute() throws Exception {
        MiniNewsDAO newsDao = new MiniNewsDAO(getCnn());
        LanguageDAO languageDao = new LanguageDAO(getCnn());

        int id = Integer.parseInt(getRequest().getParameter("id"));
        MiniNews miniNews = new MiniNews();
        if (id != 0) {
            miniNews = newsDao.getMiniNews(getUser(), id);
        }
        getRequest().setAttribute("miniNews", miniNews);
        RequestDispatcher rd = getRequest().getRequestDispatcher("/Redaction_MiniNewsEdit.jsp");
        ArrayList<Language> languageList = languageDao.getLanguageList();
        getRequest().setAttribute("languageList", languageList);
        rd.forward(getRequest(), getResponse());
    }
}
