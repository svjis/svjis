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
public class BoardDeleteCmd extends Command {

    public BoardDeleteCmd(CmdContext ctx) {
        super(ctx);
    }

    @Override
    public void execute() throws Exception {

        int parTypeId = Validator.getInt(getRequest(), "typeId", 0, Validator.MAX_INT_ALLOWED, false);
        int parUserId = Validator.getInt(getRequest(), "userId", 0, Validator.MAX_INT_ALLOWED, false);

        BoardMemberDAO boardDao = new BoardMemberDAO(getCnn());

        boardDao.deleteBoardMember(getCompany().getId(), parUserId, parTypeId);

        String url = "Dispatcher?page=boardMemberList";
        getRequest().setAttribute("url", url);
        RequestDispatcher rd = getRequest().getRequestDispatcher("/_refresh.jsp");
        rd.forward(getRequest(), getResponse());
    }
}
