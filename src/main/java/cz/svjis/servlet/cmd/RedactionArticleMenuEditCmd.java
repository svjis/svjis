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
import cz.svjis.validator.Validator;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;

/**
 *
 * @author jaroslav_b
 */
public class RedactionArticleMenuEditCmd extends Command {

    public RedactionArticleMenuEditCmd(CmdContext ctx) {
        super(ctx);
    }

    @Override
    public void execute() throws Exception {

        int parId = Validator.getInt(getRequest(), "id", 0, Validator.maxIntAllowed, false);

        MenuDAO menuDao = new MenuDAO(getCnn());
        
        MenuNode menuNode = new MenuNode();
        if (parId != 0) {
            menuNode = menuDao.getMenuNode(parId, getUser().getCompanyId());
        }
        getRequest().setAttribute("menuNode", menuNode);
        ArrayList<MenuNode> menuNodeList = menuDao.getMenuNodeList(getUser().getCompanyId());
        getRequest().setAttribute("menuNodeList", menuNodeList);
        RequestDispatcher rd = getRequest().getRequestDispatcher("/Redaction_ArticleMenuEdit.jsp");
        rd.forward(getRequest(), getResponse());
    }
}
