/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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

        int parTypeId = Validator.getInt(getRequest(), "typeId", 0, Validator.maxIntAllowed, false);
        int parUserId = Validator.getInt(getRequest(), "userId", 0, Validator.maxIntAllowed, false);

        BoardMemberDAO boardDao = new BoardMemberDAO(getCnn());

        if ((parUserId != 0) && (parTypeId != 0)) {
            boardDao.addBoardMember(getCompany().getId(), parUserId, parTypeId);
        }

        String url = "Dispatcher?page=boardMemberList";
        getRequest().setAttribute("url", url);
        RequestDispatcher rd = getRequest().getRequestDispatcher("/_refresh.jsp");
        rd.forward(getRequest(), getResponse());
    }
}
