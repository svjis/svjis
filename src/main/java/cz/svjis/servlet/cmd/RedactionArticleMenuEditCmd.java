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

        String parId = getRequest().getParameter("id");

        if (!validateInput(parId)) {
            RequestDispatcher rd = getRequest().getRequestDispatcher("/InputValidationError.jsp");
            rd.forward(getRequest(), getResponse());
            return;
        }
        
        MenuDAO menuDao = new MenuDAO(getCnn());
        
        int id = Integer.parseInt(parId);
        MenuNode menuNode = new MenuNode();
        if (id != 0) {
            menuNode = menuDao.getMenuNode(id, getUser().getCompanyId());
        }
        getRequest().setAttribute("menuNode", menuNode);
        ArrayList<MenuNode> menuNodeList = menuDao.getMenuNodeList(getUser().getCompanyId());
        getRequest().setAttribute("menuNodeList", menuNodeList);
        RequestDispatcher rd = getRequest().getRequestDispatcher("/Redaction_ArticleMenuEdit.jsp");
        rd.forward(getRequest(), getResponse());
    }
    
    private boolean validateInput(String parId) {
        boolean result = true;
        
        if (!Validator.validateInteger(parId, 0, Validator.maxIntAllowed)) {
            result = false;
        }
        
        return result;
    }
}
