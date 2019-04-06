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
        MiniNewsDAO newsDao = new MiniNewsDAO(getCnn());
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");

        int id = Integer.parseInt(getRequest().getParameter("id"));
        MiniNews n = null;
        if (id == 0) {
            n = new MiniNews();
            n.setId(id);
            n.setCreatedById(getUser().getId());
            n.setCompanyId(getUser().getCompanyId());
        } else {
            n = newsDao.getMiniNews(getUser(), id);
        }
        n.setTime(sdf.parse(getRequest().getParameter("time")));
        n.setLanguageId(Integer.parseInt(getRequest().getParameter("language")));
        n.setPublished(getRequest().getParameter("publish") != null);
        n.setBody(getRequest().getParameter("body"));
        if (id == 0) {
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
