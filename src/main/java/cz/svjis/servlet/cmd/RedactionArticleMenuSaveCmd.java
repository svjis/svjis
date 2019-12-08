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

        String parId = getRequest().getParameter("id");
        String parDescription = Validator.fixTextInput(getRequest().getParameter("description"), false);
        String parParentId = getRequest().getParameter("parent");

        if (!validateInput(parId, parDescription, parParentId)) {
            RequestDispatcher rd = getRequest().getRequestDispatcher("/InputValidationError.jsp");
            rd.forward(getRequest(), getResponse());
            return;
        }
        
        MenuDAO menuDao = new MenuDAO(getCnn());
        MenuNode n = new MenuNode();
        
        n.setId(Integer.parseInt(parId));
        n.setDescription(parDescription);
        n.setParentId(Integer.parseInt(parParentId));
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
    
    private boolean validateInput(String parId, String parDescription, String parParentId) {
        boolean result = true;
        
        if (!Validator.validateInteger(parId, 0, Validator.maxIntAllowed)) {
            result = false;
        }
        
        if (!Validator.validateString(parDescription, 0, 50)) {
            result = false;
        }
        
        if (!Validator.validateInteger(parParentId, 0, Validator.maxIntAllowed)) {
            result = false;
        }
        
        return result;
    }
}
