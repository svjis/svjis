/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.svjis.servlet.cmd;

import cz.svjis.bean.InquiryDAO;
import cz.svjis.bean.InquiryOption;
import cz.svjis.servlet.CmdContext;
import cz.svjis.servlet.Command;
import javax.servlet.RequestDispatcher;

/**
 *
 * @author jaroslav_b
 */
public class RedactionInquiryOptionDeleteCmd extends Command {

    public RedactionInquiryOptionDeleteCmd(CmdContext ctx) {
        super(ctx);
    }

    @Override
    public void execute() throws Exception {
        InquiryDAO inquiryDao = new InquiryDAO(getCnn());

        int id = Integer.parseInt(getRequest().getParameter("id"));
        InquiryOption io = inquiryDao.getInquiryOption(getUser().getCompanyId(), id);
        inquiryDao.deleteInquiryOption(io);
        String url = "Dispatcher?page=redactionInquiryEdit&id=" + io.getInquiryId();
        getRequest().setAttribute("url", url);
        RequestDispatcher rd = getRequest().getRequestDispatcher("/_refresh.jsp");
        rd.forward(getRequest(), getResponse());
    }
}
