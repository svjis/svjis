/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.svjis.servlet.cmd;

import cz.svjis.bean.ArticleAttachment;
import cz.svjis.bean.ArticleDAO;
import cz.svjis.common.HttpUtils;
import cz.svjis.servlet.CmdContext;
import cz.svjis.servlet.Command;

/**
 *
 * @author jaroslav_b
 */
public class DownloadCmd extends Command {

    public DownloadCmd(CmdContext ctx) {
        super(ctx);
    }

    @Override
    public void execute() throws Exception {
        ArticleDAO dao = new ArticleDAO(getCnn());
        ArticleAttachment aa = dao.getArticleAttachment(Integer.parseInt(getRequest().getParameter("id")));
        if (dao.getArticle(getUser(), aa.getArticleId()) == null) {
            return;
        }
        HttpUtils.writeBinaryData(aa.getContentType(), aa.getFileName(), aa.getData(), getRequest(), getResponse());
    }
}
