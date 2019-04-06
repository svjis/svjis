/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.svjis.servlet.cmd;

import cz.svjis.bean.MenuDAO;
import cz.svjis.bean.MenuNode;
import cz.svjis.servlet.CmdContext;
import cz.svjis.servlet.Command;
import javax.servlet.RequestDispatcher;

/**
 *
 * @author jaroslav_b
 */
public class RedactionArticleMenuSaveCmd extends Command {

    public RedactionArticleMenuSaveCmd(CmdContext ctx) {
        super(ctx);
    }

    @Override
    public void execute() throws Exception {
        MenuDAO menuDao = new MenuDAO(getCnn());
        MenuNode n = new MenuNode();

        n.setId(Integer.parseInt(getRequest().getParameter("id")));
        n.setDescription(getRequest().getParameter("description"));
        n.setParentId(Integer.parseInt(getRequest().getParameter("parent")));
        //-- disable recursive join
        if ((n.getId() != 0) && (n.getId() == n.getParentId())) {
            n.setParentId(0);
        }
        //--
        if (n.getId() == 0) {
            n.setId(menuDao.insertMenuNode(n, getUser().getCompanyId()));
        } else {
            menuDao.updateMenuNode(n, getUser().getCompanyId());
        }
        String url = "Dispatcher?page=redactionArticleMenuEdit&id=" + n.getId();
        getRequest().setAttribute("url", url);
        RequestDispatcher rd = getRequest().getRequestDispatcher("/_refresh.jsp");
        rd.forward(getRequest(), getResponse());
    }
}
