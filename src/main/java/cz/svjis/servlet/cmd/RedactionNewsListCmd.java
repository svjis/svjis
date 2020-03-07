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
