/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.svjis.servlet.cmd;

import cz.svjis.servlet.CmdContext;
import cz.svjis.servlet.Command;
import javax.servlet.RequestDispatcher;

/**
 *
 * @author jaroslav_b
 */
public class ContactCompanyCmd extends Command {
    
    public ContactCompanyCmd(CmdContext ctx) {
        super(ctx);
    }
    
    @Override
    public void execute() throws Exception {
        RequestDispatcher rd = getRequest().getRequestDispatcher("/Contact_company.jsp");
        rd.forward(getRequest(), getResponse());
    }
}
