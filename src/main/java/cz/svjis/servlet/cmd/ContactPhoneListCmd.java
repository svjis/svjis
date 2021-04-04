/*
 *       ContactPhoneListCmd.java
 *
 *       This file is part of SVJIS project.
 *       https://github.com/svjis/svjis
 *
 *       SVJIS is free software; you can redistribute it and/or modify
 *       it under the terms of the GNU General Public License as published by
 *       the Free Software Foundation; either version 3 of the License, or
 *       (at your option) any later version. <http://www.gnu.org/licenses/>
 */

package cz.svjis.servlet.cmd;

import cz.svjis.bean.User;
import cz.svjis.bean.UserDAO;
import cz.svjis.servlet.CmdContext;
import cz.svjis.servlet.Command;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;

/**
 *
 * @author jaroslav_b
 */
public class ContactPhoneListCmd extends Command {

    public ContactPhoneListCmd(CmdContext ctx) {
        super(ctx);
    }

    @Override
    public void execute() throws Exception {
        UserDAO userDao = new UserDAO(getCnn());
        
        ArrayList<User> userList = new ArrayList<>(userDao.getUserList(getCompany().getId(), true, 0, true));
        getRequest().setAttribute("userList", userList);
        RequestDispatcher rd = getRequest().getRequestDispatcher("/WEB-INF/jsp/Contact_userList.jsp");
        rd.forward(getRequest(), getResponse());
    }
}
