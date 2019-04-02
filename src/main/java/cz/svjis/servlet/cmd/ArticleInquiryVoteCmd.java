/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.svjis.servlet.cmd;

import cz.svjis.bean.Inquiry;
import cz.svjis.bean.InquiryDAO;
import cz.svjis.bean.InquiryOption;
import cz.svjis.bean.User;
import cz.svjis.servlet.ICommand;
import java.sql.Connection;
import java.util.Iterator;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author jaroslav_b
 */
public class ArticleInquiryVoteCmd implements ICommand {

    private User user;

    @Override
    public void run(HttpServletRequest request, HttpServletResponse response, Connection cnn) throws Exception {

        InquiryDAO inquiryDao = new InquiryDAO(cnn);

        int id = Integer.parseInt(request.getParameter("id"));
        Inquiry i = inquiryDao.getInquiry(getUser(), id);
        if ((i != null) && (i.isUserCanVote()) && (request.getParameter("i_" + i.getId()) != null)) {
            String value = request.getParameter("i_" + i.getId());
            Iterator<InquiryOption> ioI = i.getOptionList().iterator();
            while (ioI.hasNext()) {
                InquiryOption io = ioI.next();
                if (value.equals("o_" + io.getId())) {
                    inquiryDao.insertInquiryVote(io.getId(), getUser().getId());
                }
            }
        }
        String url = "Dispatcher?page=articleList";
        request.setAttribute("url", url);
        RequestDispatcher rd = request.getRequestDispatcher("/_refresh.jsp");
        rd.forward(request, response);
        return;
    }

    /**
     * @return the user
     */
    public User getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(User user) {
        this.user = user;
    }

}
