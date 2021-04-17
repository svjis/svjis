/*
 *       BoardEditCmd.java
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

import cz.svjis.bean.BoardMember;
import cz.svjis.bean.BoardMemberDAO;
import cz.svjis.bean.BoardMemberType;
import cz.svjis.bean.Company;
import cz.svjis.bean.CompanyDAO;
import cz.svjis.bean.User;
import cz.svjis.bean.UserDAO;
import cz.svjis.servlet.CmdContext;
import cz.svjis.servlet.Command;
import cz.svjis.validator.Validator;

import java.util.ArrayList;
import javax.servlet.RequestDispatcher;

/**
 *
 * @author jarberan
 */
public class BoardEditCmd extends Command {

    public BoardEditCmd(CmdContext ctx) {
        super(ctx);
    }

    @Override
    public void execute() throws Exception {
        
        int parTypeId = Validator.getInt(getRequest(), "typeId", 0, Validator.MAX_INT_ALLOWED, false);
        int parUserId = Validator.getInt(getRequest(), "userId", 0, Validator.MAX_INT_ALLOWED, false);

        CompanyDAO compDao = new CompanyDAO(getCnn());
        UserDAO userDao = new UserDAO(getCnn());
        BoardMemberDAO boardDao = new BoardMemberDAO(getCnn());

        BoardMember boardMember = boardDao.getBoardMember(getCompany().getId(), parUserId, parTypeId);
        getRequest().setAttribute("boardMember", boardMember);
        
        Company currCompany = compDao.getCompany(getCompany().getId());
        getRequest().setAttribute("currCompany", currCompany);

        ArrayList<BoardMemberType> boardTypes = new ArrayList<>(boardDao.getBoardMemberTypes());
        getRequest().setAttribute("boardTypes", boardTypes);

        ArrayList<User> userList = new ArrayList<>(userDao.getUserList(getCompany().getId(), false, 0, true));
        getRequest().setAttribute("userList", userList);

        RequestDispatcher rd = getRequest().getRequestDispatcher("/WEB-INF/jsp/Administration_boardEdit.jsp");
        rd.forward(getRequest(), getResponse());
    }
}
