/*
 *       ContactCompanyCmd.java
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
import cz.svjis.servlet.CmdContext;
import cz.svjis.servlet.Command;
import java.util.ArrayList;
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
        
        BoardMemberDAO boardDao = new BoardMemberDAO(getCnn());
        
        ArrayList<BoardMember> boardList = new ArrayList(boardDao.getBoardMembers(getCompany().getId()));
        getRequest().setAttribute("boardList", boardList);
        
        RequestDispatcher rd = getRequest().getRequestDispatcher("/WEB-INF/jsp/Contact_company.jsp");
        rd.forward(getRequest(), getResponse());
    }
}
