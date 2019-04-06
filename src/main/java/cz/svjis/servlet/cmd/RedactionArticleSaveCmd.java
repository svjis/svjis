/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.svjis.servlet.cmd;

import cz.svjis.bean.Article;
import cz.svjis.bean.ArticleDAO;
import cz.svjis.bean.LogDAO;
import cz.svjis.bean.Role;
import cz.svjis.bean.RoleDAO;
import cz.svjis.servlet.CmdContext;
import cz.svjis.servlet.Command;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import javax.servlet.RequestDispatcher;

/**
 *
 * @author jaroslav_b
 */
public class RedactionArticleSaveCmd extends Command {

    public RedactionArticleSaveCmd(CmdContext ctx) {
        super(ctx);
    }

    @Override
    public void execute() throws Exception {

        RoleDAO roleDao = new RoleDAO(getCnn());
        ArticleDAO articleDao = new ArticleDAO(getCnn());
        LogDAO logDao = new LogDAO(getCnn());

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        Article a = new Article();
        a.setId(Integer.valueOf(getRequest().getParameter("id")));
        a.setCompanyId(getCompany().getId());
        a.setHeader(getRequest().getParameter("header"));
        a.setDescription(getRequest().getParameter("description"));
        a.setBody(getRequest().getParameter("body"));
        a.setLanguageId(Integer.valueOf(getRequest().getParameter("language")));
        a.setCommentsAllowed(getRequest().getParameter("commentsAllowed") != null);
        a.setPublished(getRequest().getParameter("publish") != null);
        a.setAuthorId((Integer.valueOf(getRequest().getParameter("authorId")) == 0) ? getUser().getId() : Integer.valueOf(getRequest().getParameter("authorId")));
        a.setCreationDate(sdf.parse(getRequest().getParameter("creationDate")));
        a.setMenuNodeId(Integer.valueOf(getRequest().getParameter("menuId")));

        HashMap uRoles = new HashMap();
        ArrayList<Role> roles = roleDao.getRoleList(getCompany().getId());
        Iterator<Role> roleI = roles.iterator();
        while (roleI.hasNext()) {
            Role r = roleI.next();
            if (getRequest().getParameter("r_" + r.getId()) != null) {
                uRoles.put(new Integer(r.getId()), r.getDescription());
            }
        }
        a.setRoles(uRoles);
        if (a.getId() == 0) {
            a.setId(articleDao.insertArticle(a));
            logDao.log(getUser().getId(), LogDAO.operationTypeCreateArticle, a.getId(), getRequest().getRemoteAddr(), getRequest().getHeader("User-Agent"));
        } else {
            articleDao.modifyArticle(a);
            logDao.log(getUser().getId(), LogDAO.operationTypeModifyArticle, a.getId(), getRequest().getRemoteAddr(), getRequest().getHeader("User-Agent"));
        }
        String url = "Dispatcher?page=redactionArticleEdit&id=" + a.getId();
        getRequest().setAttribute("url", url);
        RequestDispatcher rd = getRequest().getRequestDispatcher("/_refresh.jsp");
        rd.forward(getRequest(), getResponse());
    }
}
