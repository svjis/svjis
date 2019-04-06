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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;

/**
 *
 * @author jaroslav_b
 */
public class RedactionInquirySaveCmd extends Command {

    public RedactionInquirySaveCmd(CmdContext ctx) {
        super(ctx);
    }

    @Override
    public void execute() throws Exception {
        InquiryDAO inquiryDao = new InquiryDAO(getCnn());

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        int id = Integer.parseInt(getRequest().getParameter("id"));
        Inquiry i = new Inquiry();
        i.setId(id);
        i.setCompanyId(getUser().getCompanyId());
        i.setUserId(getUser().getId());
        i.setDescription(getRequest().getParameter("description"));
        i.setStartingDate(sdf.parse(getRequest().getParameter("startingDate")));
        i.setEndingDate(sdf.parse(getRequest().getParameter("endingDate")));
        i.setEnabled(getRequest().getParameter("publish") != null);
        ArrayList<InquiryOption> ioList = new ArrayList<InquiryOption>();
        int n = 1;
        while (getRequest().getParameter("oid_" + n) != null) {
            InquiryOption io = new InquiryOption();
            io.setId(Integer.valueOf(getRequest().getParameter("oid_" + n)));
            io.setInquiryId(i.getId());
            io.setDescription(getRequest().getParameter("o_" + n));
            if (!io.getDescription().equals("")) {
                ioList.add(io);
            }
            n++;
        }
        i.setOptionList(ioList);

        if (i.getId() == 0) {
            i.setId(inquiryDao.insertInquiry(i));
        } else {
            inquiryDao.modifyInquiry(i);
        }
        String url = "Dispatcher?page=redactionInquiryEdit&id=" + i.getId();
        getRequest().setAttribute("url", url);
        RequestDispatcher rd = getRequest().getRequestDispatcher("/_refresh.jsp");
        rd.forward(getRequest(), getResponse());
    }
}
