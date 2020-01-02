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

        int parId = Validator.getInt(getRequest(), "id", 0, Validator.maxIntAllowed, false);
        String parDescription = Validator.getString(getRequest(), "description", 0, 50, false, false);
        int parParentId = Validator.getInt(getRequest(), "parent", 0, Validator.maxIntAllowed, false);

        MenuDAO menuDao = new MenuDAO(getCnn());
        MenuNode n = new MenuNode();
        
        n.setId(parId);
        n.setDescription(parDescription);
        n.setParentId(parParentId);
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
        String url = "Dispatcher?page=redactionArticleMenu";
        getRequest().setAttribute("url", url);
        RequestDispatcher rd = getRequest().getRequestDispatcher("/_refresh.jsp");
        rd.forward(getRequest(), getResponse());
    }
}
