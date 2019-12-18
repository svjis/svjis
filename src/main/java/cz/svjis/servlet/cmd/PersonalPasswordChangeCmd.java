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
public class PersonalPasswordChangeCmd extends Command {
    
    public PersonalPasswordChangeCmd(CmdContext ctx) {
        super(ctx);
    }
    
    @Override
    public void execute() throws Exception {
        getRequest().setAttribute("message", "");
        getRequest().setAttribute("errorMessage", "");
        RequestDispatcher rd = getRequest().getRequestDispatcher("/PersonalSettings_passwordChange.jsp");
        rd.forward(getRequest(), getResponse());
    }
}
