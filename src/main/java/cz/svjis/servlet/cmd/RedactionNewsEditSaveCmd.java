/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.svjis.servlet.cmd;

import cz.svjis.bean.MiniNews;
import cz.svjis.bean.MiniNewsDAO;
import cz.svjis.servlet.CmdContext;
import cz.svjis.servlet.Command;
import cz.svjis.validator.Validator;
import java.text.SimpleDateFormat;
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
        
        int parId = Validator.getInt(getRequest(), "id", 0, Validator.maxIntAllowed, false);
        String parTime = Validator.getString(getRequest(), "time", 0, 30, false, false);
        int parLangId = Validator.getInt(getRequest(), "language", 0, Validator.maxIntAllowed, false);
        String parBody = Validator.getString(getRequest(), "body", 0, Validator.maxStringLenAllowed, false, true);
        boolean parPublished = Validator.getBoolean(getRequest(), "publish");

        MiniNewsDAO newsDao = new MiniNewsDAO(getCnn());
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");

        MiniNews n = null;
        if (parId == 0) {
            n = new MiniNews();
            n.setId(parId);
            n.setCreatedById(getUser().getId());
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
        String url = "Dispatcher?page=redactionNewsEdit&id=" + n.getId();
        getRequest().setAttribute("url", url);
        RequestDispatcher rd = getRequest().getRequestDispatcher("/_refresh.jsp");
        rd.forward(getRequest(), getResponse());
    }
}
