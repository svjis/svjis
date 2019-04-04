/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.svjis.servlet.cmd;

import cz.svjis.bean.Menu;
import cz.svjis.bean.MenuDAO;
import cz.svjis.servlet.CmdContext;
import cz.svjis.servlet.Command;
import javax.servlet.RequestDispatcher;

/**
 *
 * @author jaroslav_b
 */
public class RedactionArticleMenuCmd extends Command {

    public RedactionArticleMenuCmd(CmdContext ctx) {
        super(ctx);
    }

    @Override
    public void execute() throws Exception {
        MenuDAO menuDao = new MenuDAO(getCnn());

        Menu menu = menuDao.getMenu(getCompany().getId());
        menu.setActiveSection(-1);
        getRequest().setAttribute("menu", menu);
        RequestDispatcher rd = getRequest().getRequestDispatcher("/Redaction_ArticleMenu.jsp");
        rd.forward(getRequest(), getResponse());
    }
}
