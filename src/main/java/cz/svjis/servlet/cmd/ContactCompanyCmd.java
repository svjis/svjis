/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
        
        RequestDispatcher rd = getRequest().getRequestDispatcher("/Contact_company.jsp");
        rd.forward(getRequest(), getResponse());
    }
}
