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
import cz.svjis.bean.Permission;
import cz.svjis.servlet.CmdContext;
import cz.svjis.servlet.Command;
import cz.svjis.validator.Validator;

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
        
        if (!getUser().hasPermission(Permission.MENU_ADMINISTRATION)) {
            new Error401UnauthorizedCmd(getCtx()).execute();
            return;
        }

        int parTypeId = Validator.getInt(getRequest(), "typeId", 0, Validator.MAX_INT_ALLOWED, false);
        int parUserId = Validator.getInt(getRequest(), "userId", 0, Validator.MAX_INT_ALLOWED, false);
        int parOrigTypeId = Validator.getInt(getRequest(), "origTypeId", 0, Validator.MAX_INT_ALLOWED, false);
        int parOrigUserId = Validator.getInt(getRequest(), "origUserId", 0, Validator.MAX_INT_ALLOWED, false);

        BoardMemberDAO boardDao = new BoardMemberDAO(getCnn());

        if ((parOrigUserId != 0) && (parOrigTypeId != 0)) {
            boardDao.deleteBoardMember(getCompany().getId(), parOrigUserId, parOrigTypeId);
        }
        
        if ((parUserId != 0) && (parTypeId != 0)) {
            boardDao.addBoardMember(getCompany().getId(), parUserId, parTypeId);
        }

        String url = "Dispatcher?page=boardMemberList";
        getResponse().sendRedirect(url);
    }
}
