/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.svjis.servlet.cmd;

import cz.svjis.bean.BoardMember;
import cz.svjis.bean.BoardMemberDAO;
import cz.svjis.bean.Company;
import cz.svjis.bean.CompanyDAO;
import cz.svjis.servlet.CmdContext;
import cz.svjis.servlet.Command;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;

/**
 *
 * @author jarberan
 */
public class BoardListCmd extends Command {

    public BoardListCmd(CmdContext ctx) {
        super(ctx);
    }

    @Override
    public void execute() throws Exception {

        CompanyDAO compDao = new CompanyDAO(getCnn());
        BoardMemberDAO boardDao = new BoardMemberDAO(getCnn());

        Company currCompany = compDao.getCompany(getCompany().getId());
        getRequest().setAttribute("currCompany", currCompany);

        ArrayList<BoardMember> boardList = new ArrayList(boardDao.getBoardMembers(getCompany().getId()));
        getRequest().setAttribute("boardList", boardList);

        RequestDispatcher rd = getRequest().getRequestDispatcher("/Administration_boardList.jsp");
        rd.forward(getRequest(), getResponse());
    }
}