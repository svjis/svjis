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
import javax.servlet.RequestDispatcher;

/**
 *
 * @author jaroslav_b
 */
public class RedactionNewsDeleteCmd extends Command {

    public RedactionNewsDeleteCmd(CmdContext ctx) {
        super(ctx);
    }

    @Override
    public void execute() throws Exception {
        MiniNewsDAO newsDao = new MiniNewsDAO(getCnn());

        int id = Integer.parseInt(getRequest().getParameter("id"));
        MiniNews n = new MiniNews();
        n.setId(id);
        n.setCompanyId(getUser().getCompanyId());
        newsDao.deleteMiniNews(n);
        String url = "Dispatcher?page=redactionNewsList";
        getRequest().setAttribute("url", url);
        RequestDispatcher rd = getRequest().getRequestDispatcher("/_refresh.jsp");
        rd.forward(getRequest(), getResponse());
    }
}
