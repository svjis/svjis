/*
 *       BoardSaveCmd.java
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

import cz.svjis.bean.BoardMemberDAO;
import cz.svjis.servlet.CmdContext;
import cz.svjis.servlet.Command;
import cz.svjis.validator.Validator;
import javax.servlet.RequestDispatcher;

/**
 *
 * @author jarberan
 */
public class BoardSaveCmd extends Command {

    public BoardSaveCmd(CmdContext ctx) {
        super(ctx);
    }

    @Override
    public void execute() throws Exception {

        int parTypeId = Validator.getInt(getRequest(), "typeId", 0, Validator.MAX_INT_ALLOWED, false);
        int parUserId = Validator.getInt(getRequest(), "userId", 0, Validator.MAX_INT_ALLOWED, false);

        BoardMemberDAO boardDao = new BoardMemberDAO(getCnn());

        if ((parUserId != 0) && (parTypeId != 0)) {
            boardDao.addBoardMember(getCompany().getId(), parUserId, parTypeId);
        }

        String url = "Dispatcher?page=boardMemberList";
        getRequest().setAttribute("url", url);
        RequestDispatcher rd = getRequest().getRequestDispatcher("/WEB-INF/jsp/_refresh.jsp");
        rd.forward(getRequest(), getResponse());
    }
}
