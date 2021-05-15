/*
 *       ArticleInquiryVoteCmd.java
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

import cz.svjis.bean.Inquiry;
import cz.svjis.bean.InquiryDAO;
import cz.svjis.bean.InquiryOption;
import cz.svjis.bean.Permission;
import cz.svjis.servlet.Cmd;
import cz.svjis.servlet.CmdContext;
import cz.svjis.servlet.Command;
import cz.svjis.validator.Validator;
import java.util.Iterator;

/**+
 *
 * @author jaroslav_b
 */
public class ArticleInquiryVoteCmd extends Command {

    public ArticleInquiryVoteCmd(CmdContext ctx) {
        super(ctx);
    }

    @Override
    public void execute() throws Exception {
        
        if (!getUser().hasPermission(Permission.CAN_VOTE_INQUIRY)) {
            new Error401UnauthorizedCmd(getCtx()).execute();
            return;
        }
        
        int parId = Validator.getInt(getRequest(), "id", 0, Validator.MAX_INT_ALLOWED, false);

        InquiryDAO inquiryDao = new InquiryDAO(getCnn());

        int id = parId;
        Inquiry i = inquiryDao.getInquiry(getUser(), id);
        if (i == null) {
            new Error404NotFoundCmd(getCtx()).execute();
            return;
        }
        
        if ((i.isUserCanVote()) && (i.isOpenForVoting()) && (getRequest().getParameter("i_" + i.getId()) != null)) {
            String value = getRequest().getParameter("i_" + i.getId());
            Iterator<InquiryOption> ioI = i.getOptionList().iterator();
            while (ioI.hasNext()) {
                InquiryOption io = ioI.next();
                if (value.equals("o_" + io.getId())) {
                    inquiryDao.insertInquiryVote(io.getId(), getUser().getId());
                }
            }
        }
        String url = String.format("Dispatcher?page=%s", Cmd.ARTICLE_LIST);
        getResponse().sendRedirect(url);
    }
}
