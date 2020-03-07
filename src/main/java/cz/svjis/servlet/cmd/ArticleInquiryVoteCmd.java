/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.svjis.servlet.cmd;

import cz.svjis.bean.Inquiry;
import cz.svjis.bean.InquiryDAO;
import cz.svjis.bean.InquiryOption;
import cz.svjis.servlet.CmdContext;
import cz.svjis.servlet.Command;
import cz.svjis.validator.Validator;
import java.util.Iterator;
import javax.servlet.RequestDispatcher;

/**
 *
 * @author jaroslav_b
 */
public class ArticleInquiryVoteCmd extends Command {

    public ArticleInquiryVoteCmd(CmdContext ctx) {
        super(ctx);
    }

    @Override
    public void execute() throws Exception {
        
        int parId = Validator.getInt(getRequest(), "id", 0, Validator.MAX_INT_ALLOWED, false);

        InquiryDAO inquiryDao = new InquiryDAO(getCnn());

        int id = parId;
        Inquiry i = inquiryDao.getInquiry(getUser(), id);
        if ((i != null) && (i.isUserCanVote()) && (getRequest().getParameter("i_" + i.getId()) != null)) {
            String value = getRequest().getParameter("i_" + i.getId());
            Iterator<InquiryOption> ioI = i.getOptionList().iterator();
            while (ioI.hasNext()) {
                InquiryOption io = ioI.next();
                if (value.equals("o_" + io.getId())) {
                    inquiryDao.insertInquiryVote(io.getId(), getUser().getId());
                }
            }
        }
        String url = "Dispatcher?page=articleList";
        getRequest().setAttribute("url", url);
        RequestDispatcher rd = getRequest().getRequestDispatcher("/_refresh.jsp");
        rd.forward(getRequest(), getResponse());
    }
}
