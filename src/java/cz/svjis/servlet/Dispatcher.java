/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.svjis.servlet;

import cz.svjis.bean.ApplicationSetupDAO;
import cz.svjis.bean.Article;
import cz.svjis.bean.ArticleAttachment;
import cz.svjis.bean.ArticleComment;
import cz.svjis.bean.ArticleDAO;
import cz.svjis.bean.ArticleListInfo;
import cz.svjis.bean.Building;
import cz.svjis.bean.BuildingDAO;
import cz.svjis.bean.BuildingUnit;
import cz.svjis.bean.BuildingUnitType;
import cz.svjis.bean.Company;
import cz.svjis.bean.CompanyDAO;
import cz.svjis.bean.Inquiry;
import cz.svjis.bean.InquiryDAO;
import cz.svjis.bean.InquiryOption;
import cz.svjis.bean.Language;
import cz.svjis.bean.LanguageDAO;
import cz.svjis.bean.LogDAO;
import cz.svjis.bean.Menu;
import cz.svjis.bean.MenuDAO;
import cz.svjis.bean.MenuItem;
import cz.svjis.bean.MenuNode;
import cz.svjis.bean.MiniNews;
import cz.svjis.bean.MiniNewsDAO;
import cz.svjis.bean.Permission;
import cz.svjis.bean.Role;
import cz.svjis.bean.RoleDAO;
import cz.svjis.bean.SystemMenuEntry;
import cz.svjis.bean.User;
import cz.svjis.bean.UserDAO;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 *
 * @author berk
 */
public class Dispatcher extends HttpServlet {

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        String page = request.getParameter("page");
        if (page == null) {
            page = "articleList";
        }
        
        Connection cnn = null;
        
        try {
            cnn = createConnection();
            CompanyDAO compDao = new CompanyDAO(cnn);
            LanguageDAO languageDao = new LanguageDAO(cnn);
            UserDAO userDao = new UserDAO(cnn);
            RoleDAO roleDao = new RoleDAO(cnn);
            ApplicationSetupDAO setupDao = new ApplicationSetupDAO(cnn);
            MenuDAO menuDao = new MenuDAO(cnn);
            ArticleDAO articleDao = new ArticleDAO(cnn);
            BuildingDAO buildingDao = new BuildingDAO(cnn);
            LogDAO logDao = new LogDAO(cnn);
            MiniNewsDAO newsDao = new MiniNewsDAO(cnn);
            InquiryDAO inquiryDao = new InquiryDAO(cnn);
            
            HttpSession session = request.getSession();
            
            // *****************
            // * Company       *
            // *****************
            
            Company company = (Company) session.getAttribute("company");
            if (company == null) {
                company = compDao.getCompanyByDomain(request.getServerName());
                session.setAttribute("company", company);
                session.setAttribute("user", null);
                session.setAttribute("setup", null);
                session.setAttribute("language", null);
            }
            
            if (request.getParameter("setcompany") != null) {
                company = compDao.getCompany(Integer.valueOf(request.getParameter("setcompany")));
                session.setAttribute("company", company);
                session.setAttribute("user", null);
                session.setAttribute("setup", null);
                session.setAttribute("language", null);
            }
            
            // *****************
            // * Setup         *
            // *****************
            
            Properties setup = (Properties) session.getAttribute("setup");
            if (setup == null) {
                if (company != null) {
                    setup = setupDao.getApplicationSetup(company.getId());
                } else {
                    setup = new Properties();
                }
                session.setAttribute("setup", setup);
            }
            
            // *****************
            // * Login         *
            // *****************
            
            
            User user = (User) session.getAttribute("user");
            if (user == null) {
                user = new User();
                if (company != null) {
                    user.setCompanyId(company.getId());
                }
                if ((setup.getProperty("anonymous.user.id") != null) && (userDao.getUser(company.getId(), 
                        Integer.valueOf(setup.getProperty("anonymous.user.id"))) != null)) {
                    user = userDao.getUser(company.getId(), Integer.valueOf(setup.getProperty("anonymous.user.id")));
                }
                session.setAttribute("user", user);
            }
            
            if (page.equals("login") && (company != null)) {
                User u = userDao.getUserByLogin(company.getId(), request.getParameter("login"));
                if ((u != null) && (u.login(request.getParameter("password")))) {
                    user = u;
                    session.setAttribute("user", user);
                    session.setAttribute("language", null);
                    page = "articleList";
                    logDao.log(user.getId(), LogDAO.operationTypeLogin, LogDAO.idNull);
                } else {
                    RequestDispatcher rd = request.getRequestDispatcher("/BadLogin.jsp");
                    rd.forward(request, response);
                    return;
                }
            }
            
            if (page.equals("logout")) {
                logDao.log(user.getId(), LogDAO.operationTypeLogout, LogDAO.idNull);
                session.setAttribute("user", null);
                session.setAttribute("language", null);
                String url = "Dispatcher";
                request.setAttribute("url", url);
                RequestDispatcher rd = request.getRequestDispatcher("/_refresh.jsp");
                rd.forward(request, response);
                return;
            }
            
            // *****************
            // * Language      *
            // *****************
            
            Language language = (Language) session.getAttribute("language");
            if (language == null) {
                language = languageDao.getDictionary(user.getLanguageId());
                session.setAttribute("language", language);
            }
            
            // *****************
            // * System menu   *
            // *****************
            
            ArrayList<SystemMenuEntry> systemMenu = createSystemMenu(user);
            request.setAttribute("systemMenu", systemMenu);
            
            // *****************
            // * Default page  *
            // *****************
            
            if (company == null) {
                page = "companyList";
            }
            
            // *****************
            // * Company       *
            // *****************
            
            if (page.equals("companyList")) {
                ArrayList<Company> companyList = compDao.getCompanyList();
                request.setAttribute("companyList", companyList);
                RequestDispatcher rd = request.getRequestDispatcher("/CompanyList.jsp");
                rd.forward(request, response);
                return;
            }
            
            // *****************
            // * Article       *
            // *****************
            if (user.hasPermission("menu_articles")) {
                if (page.equals("articleList")) {
                    Menu menu = menuDao.getMenu(company.getId());
                    int section = 0;
                    if (request.getParameter("section") != null) {
                        section = Integer.valueOf(request.getParameter("section"));
                    }
                    if ((section == 0) && (setup.get("article.menu.default.item") != null)) {
                        section = Integer.valueOf(setup.getProperty("article.menu.default.item"));
                    }
                    menu.setActiveSection(section);
                    request.setAttribute("menu", menu);

                    int pageNo = 1;
                    if (request.getParameter("pageNo") != null) {
                        pageNo = Integer.valueOf(request.getParameter("pageNo"));
                    }
                    ArrayList<Article> articleList = articleDao.getArticleList(
                            user,
                            section,
                            pageNo, 
                            Integer.valueOf(setup.getProperty("article.page.size")),
                            true, false);
                    request.setAttribute("articleList", articleList);
                    ArrayList<Article> articleTopList = articleDao.getArticleTopList(
                            user, 
                            Integer.valueOf(setup.getProperty("article.top.size")));
                    request.setAttribute("articleTopList", articleTopList);
                    ArticleListInfo articleListInfo = new ArticleListInfo();
                    articleListInfo.setNumOfArticles(articleDao.getNumOfArticles(
                            user,
                            section,
                            true, false));
                    articleListInfo.setPageSize(Integer.valueOf(setup.getProperty("article.page.size")));
                    articleListInfo.setActualPage(pageNo);
                    articleListInfo.setMenuNodeId(section);
                    request.setAttribute("articleListInfo", articleListInfo);
                    ArrayList<MiniNews> miniNewsList = newsDao.getMiniNews(user, true);
                    request.setAttribute("miniNewsList", miniNewsList);
                    ArrayList<Inquiry> inquiryList = inquiryDao.getInquiryList(user, true);
                    request.setAttribute("inquiryList", inquiryList);
                    RequestDispatcher rd = request.getRequestDispatcher("/ArticleList.jsp");
                    rd.forward(request, response);
                    return;
                }
                
                if (page.equals("search")) {
                    String search = request.getParameter("search");
                    
                    Menu menu = menuDao.getMenu(company.getId());
                    int section = 0;
                    if (request.getParameter("section") != null) {
                        section = Integer.valueOf(request.getParameter("section"));
                    }
                    menu.setActiveSection(section);
                    request.setAttribute("menu", menu);

                    int pageNo = 1;
                    if (request.getParameter("pageNo") != null) {
                        pageNo = Integer.valueOf(request.getParameter("pageNo"));
                    }
                    ArrayList<Article> articleList = articleDao.getArticleListFromSearch(
                            search,
                            user,
                            section,
                            pageNo, 
                            Integer.valueOf(setup.getProperty("article.page.size")),
                            true, false);
                    request.setAttribute("articleList", articleList);
                    ArrayList<Article> articleTopList = articleDao.getArticleTopList(
                            user, 
                            Integer.valueOf(setup.getProperty("article.top.size")));
                    request.setAttribute("articleTopList", articleTopList);
                    ArticleListInfo articleListInfo = new ArticleListInfo();
                    articleListInfo.setNumOfArticles(articleDao.getNumOfArticlesFromSearch(
                            search,
                            user,
                            section,
                            true, false));
                    articleListInfo.setPageSize(Integer.valueOf(setup.getProperty("article.page.size")));
                    articleListInfo.setActualPage(pageNo);
                    articleListInfo.setMenuNodeId(section);
                    request.setAttribute("articleListInfo", articleListInfo);
                    ArrayList<MiniNews> miniNewsList = newsDao.getMiniNews(user, true);
                    request.setAttribute("miniNewsList", miniNewsList);
                    ArrayList<Inquiry> inquiryList = inquiryDao.getInquiryList(user, true);
                    request.setAttribute("inquiryList", inquiryList);
                    RequestDispatcher rd = request.getRequestDispatcher("/ArticleList.jsp");
                    rd.forward(request, response);
                    return;
                }
                if (page.equals("inquiryVote")) {
                    int id = Integer.parseInt(request.getParameter("id"));
                    Inquiry i = inquiryDao.getInquiry(user, id);
                    if ((i != null) && (i.isUserCanVote())) {
                        Iterator<InquiryOption> ioI = i.getOptionList().iterator();
                        while (ioI.hasNext()) {
                            InquiryOption io = ioI.next();
                            if (request.getParameter("o_" + io.getId()) != null) {
                                inquiryDao.insertInquiryVote(io.getId(), user.getId());
                            }
                        }
                    }
                    String url = "Dispatcher?page=articleList";
                    request.setAttribute("url", url);
                    RequestDispatcher rd = request.getRequestDispatcher("/_refresh.jsp");
                    rd.forward(request, response);
                    return;
                }
                if (page.equals("articleDetail")) {
                    int articleId = 0;
                    if (request.getParameter("id") != null) {
                        articleId = Integer.valueOf(request.getParameter("id"));
                    } 
                    Article article = articleDao.getArticle(
                            user, 
                            articleId);
                    request.setAttribute("article", article);

                    Menu menu = menuDao.getMenu(company.getId());
                    menu.setActiveSection(article.getMenuNodeId());
                    request.setAttribute("menu", menu);
                    RequestDispatcher rd = request.getRequestDispatcher("/ArticleDetail.jsp");
                    rd.forward(request, response);
                    logDao.log(user.getId(), LogDAO.operationTypeRead, article.getId());
                    return;
                }
                
                if (page.equals("insertArticleComment")) {
                    int articleId = Integer.valueOf(request.getParameter("id"));
                    Article article = articleDao.getArticle(
                            user, 
                            articleId);
                    request.setAttribute("article", article);
                    
                    if ((article != null) && (article.isCommentsAllowed()) && (user.hasPermission("can_insert_article_comment"))) {
                        ArticleComment ac = new ArticleComment();
                        ac.setArticleId(article.getId());
                        ac.setUserId(user.getId());
                        ac.setInsertionTime(new Date());
                        ac.setBody(request.getParameter("body"));
                        articleDao.insertArticleComment(ac);
                    }
                    
                    String url = "Dispatcher?page=articleDetail&id=" + article.getId();
                    request.setAttribute("url", url);
                    RequestDispatcher rd = request.getRequestDispatcher("/_refresh.jsp");
                    rd.forward(request, response);
                    return;
                }
            }
            
            // *****************
            // * Contact       *
            // *****************
            if (user.hasPermission("menu_contact")) {
                
                if (page.equals("contact")) {
                    RequestDispatcher rd = request.getRequestDispatcher("/Contact_company.jsp");
                    rd.forward(request, response);
                    return;
                }
            
                if (page.equals("phonelist")) {
                    ArrayList<User> userList = userDao.getUserList(company.getId(), true);
                    request.setAttribute("userList", userList);
                    RequestDispatcher rd = request.getRequestDispatcher("/Contact_userList.jsp");
                    rd.forward(request, response);
                    return;
                }
            }
            
            // ***********************
            // * My bulilding units  *
            // ***********************
            if (user.hasPermission("menu_building_units")) {
                if (page.equals("myBuildingUnitList")) {
                    ArrayList<BuildingUnit> userHasUnitList = buildingDao.getUserHasBuildingUnitList(user.getId());
                    request.setAttribute("userHasUnitList", userHasUnitList);
                    RequestDispatcher rd = request.getRequestDispatcher("/Units_userUnitList.jsp");
                    rd.forward(request, response);
                    return;
                }
            }
            
            // **********************
            // * Personal settings  *
            // **********************
            if (user.hasPermission("menu_personal_settings")) {
                if (page.equals("psUserDetail")) {
                    ArrayList<Language> languageList = languageDao.getLanguageList();
                    request.setAttribute("languageList", languageList);
                    RequestDispatcher rd = request.getRequestDispatcher("/PersonalSettings_userDetail.jsp");
                    rd.forward(request, response);
                    return;
                }
                
                if (page.equals("psUserDetailSave")) {
                    user.setSalutation(request.getParameter("salutation"));
                    user.setFirstName(request.getParameter("firstName"));
                    user.setLastName(request.getParameter("lastName"));
                    user.setLanguageId(Integer.valueOf(request.getParameter("language")));
                    user.setAddress(request.getParameter("address"));
                    user.setCity(request.getParameter("city"));
                    user.setPostCode(request.getParameter("postCode"));
                    user.setCountry(request.getParameter("country"));
                    user.setFixedPhone(request.getParameter("fixedPhone"));
                    user.setCellPhone(request.getParameter("cellPhone"));
                    user.seteMail(request.getParameter("eMail"));
                    user.setShowInPhoneList(request.getParameter("phoneList") != null);
                    userDao.modifyUser(user);
                    language = languageDao.getDictionary(user.getLanguageId());
                    session.setAttribute("language", language);
                    String url = "Dispatcher?page=psUserDetail";
                    request.setAttribute("url", url);
                    RequestDispatcher rd = request.getRequestDispatcher("/_refresh.jsp");
                    rd.forward(request, response);
                    return;
                }
                
                if (page.equals("psPasswordChange")) {
                    String message = "";
                    request.setAttribute("message", message);
                    RequestDispatcher rd = request.getRequestDispatcher("/PersonalSettings_passwordChange.jsp");
                    rd.forward(request, response);
                    return;
                }
                if (page.equals("psPasswordChangeSave")) {
                    String message = "";
                    String oldPass = request.getParameter("oldPassword");
                    String newPass1 = request.getParameter("newPassword");
                    String newPass2 = request.getParameter("newPassword2");
                    if (!oldPass.equals(user.getPassword())) {
                        message += language.getText("You typed wrong current password.") + "<br>";
                    }
                    if (!newPass1.equals(newPass2)) {
                        message += language.getText("New passwords doesnt match.") + "<br>";
                    }
                    if (!userDao.testPasswordValidity(newPass2)) {
                        message += language.getText("Password is too short. Minimum is 6 characters.") + "<br>";
                    }
                    if (message.equals("")) {
                        user.setPassword(newPass1);
                        userDao.modifyUser(user);
                        message += language.getText("New password has been set.") + "<br>";
                    }
                    request.setAttribute("message", message);
                    RequestDispatcher rd = request.getRequestDispatcher("/PersonalSettings_passwordChange.jsp");
                    rd.forward(request, response);
                    return;
                }
            }
            
            // *****************
            // * Redaction     *
            // *****************
            if (user.hasPermission("menu_redaction")) {
                if (page.equals("redactionArticleList")) {
                    Menu menu = menuDao.getMenu(company.getId());
                    int section = 0;
                    if (request.getParameter("section") != null) {
                        section = Integer.valueOf(request.getParameter("section"));
                    }
                    menu.setActiveSection(section);
                    request.setAttribute("menu", menu);

                    int pageNo = 1;
                    if (request.getParameter("pageNo") != null) {
                        pageNo = Integer.valueOf(request.getParameter("pageNo"));
                    }
                    ArrayList<Article> articleList = articleDao.getArticleList(
                            user,
                            section,
                            pageNo, 
                            Integer.valueOf(setup.getProperty("article.page.size")),
                            false,
                            !user.hasPermission("redaction_articles_all"));
                    request.setAttribute("articleList", articleList);

                    ArticleListInfo articleListInfo = new ArticleListInfo();
                    articleListInfo.setNumOfArticles(articleDao.getNumOfArticles(
                            user,
                            section,
                            false,
                            !user.hasPermission("redaction_articles_all")));
                    articleListInfo.setPageSize(Integer.valueOf(setup.getProperty("article.page.size")));
                    articleListInfo.setActualPage(pageNo);
                    articleListInfo.setMenuNodeId(section);
                    request.setAttribute("articleListInfo", articleListInfo);

                    RequestDispatcher rd = request.getRequestDispatcher("/Redaction_ArticleList.jsp");
                    rd.forward(request, response);
                    return;
                }
                
                if (page.equals("redactionArticleEdit")) {
                    int articleId = 0;
                    if (request.getParameter("id") != null) {
                        articleId = Integer.valueOf(request.getParameter("id"));
                    } 
                    Article article = null;
                    if (articleId == 0) {
                        article = new Article();
                    } else {
                        article = articleDao.getArticle(user, articleId);
                    }
                    request.setAttribute("article", article);
                    
                    ArrayList<MenuItem> menuNodeList = menuDao.getMenu(company.getId()).getMenu();
                    request.setAttribute("menuNodeList", menuNodeList);
                    ArrayList<Language> languageList = languageDao.getLanguageList();
                    request.setAttribute("languageList", languageList);
                    ArrayList<Role> roleList = roleDao.getRoleList(company.getId());
                    request.setAttribute("roleList", roleList);

                    RequestDispatcher rd = request.getRequestDispatcher("/Redaction_ArticleEdit.jsp");
                    rd.forward(request, response);
                    return;
                }
                
                if (page.equals("redactionArticleSave")) {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
                    Article a = new Article();
                    a.setId(Integer.valueOf(request.getParameter("id")));
                    a.setCompanyId(company.getId());
                    a.setHeader(request.getParameter("header"));
                    a.setDescription(request.getParameter("description"));
                    a.setBody(request.getParameter("body"));
                    a.setLanguageId(Integer.valueOf(request.getParameter("language")));
                    a.setCommentsAllowed(request.getParameter("commentsAllowed") != null);
                    a.setPublished(request.getParameter("publish") != null);
                    a.setAuthorId((Integer.valueOf(request.getParameter("authorId")) == 0) ? user.getId() : Integer.valueOf(request.getParameter("authorId")));
                    a.setCreationDate(sdf.parse(request.getParameter("creationDate")));
                    a.setMenuNodeId(Integer.valueOf(request.getParameter("menuId")));
                    
                    HashMap uRoles = new HashMap();
                    ArrayList<Role> roles = roleDao.getRoleList(company.getId());
                    Iterator<Role> roleI = roles.iterator();
                    while (roleI.hasNext()) {
                        Role r = roleI.next();
                        if (request.getParameter("r_" + r.getId()) != null) {
                            uRoles.put(new Integer(r.getId()), r.getDescription());
                        }
                    }
                    a.setRoles(uRoles);
                    if (a.getId() == 0) {
                        a.setId(articleDao.insertArticle(a));
                    } else {
                        articleDao.modifyArticle(a);
                    }
                    String url = "Dispatcher?page=redactionArticleEdit&id=" + a.getId();
                    request.setAttribute("url", url);
                    RequestDispatcher rd = request.getRequestDispatcher("/_refresh.jsp");
                    rd.forward(request, response);
                    return;
                }
                
                if (page.equals("redactionArticleAttachmentSave")) {
                    int articleId = Integer.parseInt(request.getParameter("articleId"));
                    FileItemFactory factory = new DiskFileItemFactory();
                    ServletFileUpload upload = new ServletFileUpload(factory);
                    //upload.setSizeMax(yourMaxRequestSize);
                    List items = upload.parseRequest(request);
                    java.util.Iterator iterator = items.iterator();
                    while (iterator.hasNext()) {
                        FileItem item = (FileItem) iterator.next();
                        File f = new File(item.getName());
                        if (!item.isFormField()) {
                            ArticleAttachment aa = new ArticleAttachment();
                            aa.setFileName(f.getName());
                            aa.setContentType(item.getContentType());
                            aa.setData(item.get());
                            aa.setUserId(user.getId());
                            aa.setArticleId(articleId);
                            aa.setUploadTime(new Date());
                            articleDao.insertArticleAttachment(aa);
                        }
                    }
                    String url = "Dispatcher?page=redactionArticleEdit&id=" + articleId;
                    request.setAttribute("url", url);
                    RequestDispatcher rd = request.getRequestDispatcher("/_refresh.jsp");
                    rd.forward(request, response);
                    return;
                }
                if (page.equals("redactionArticleAttachmentDelete")) {
                    int id = Integer.parseInt(request.getParameter("id"));
                    int articleId = Integer.parseInt(request.getParameter("articleId"));
                    articleDao.deleteArticleAttachment(id);
                    String url = "Dispatcher?page=redactionArticleEdit&id=" + articleId;
                    request.setAttribute("url", url);
                    RequestDispatcher rd = request.getRequestDispatcher("/_refresh.jsp");
                    rd.forward(request, response);
                    return;
                }
                
                if (page.equals("redactionNewsList")) {
                    ArrayList<MiniNews> miniNewsList = newsDao.getMiniNews(user, false);
                    request.setAttribute("miniNewsList", miniNewsList);
                    RequestDispatcher rd = request.getRequestDispatcher("/Redaction_MiniNewsList.jsp");
                    rd.forward(request, response);
                    return;
                }
                if (page.equals("redactionNewsEdit")) {
                    int id = Integer.parseInt(request.getParameter("id"));
                    MiniNews miniNews = new MiniNews();
                    if (id != 0) {
                        miniNews = newsDao.getMiniNews(user, id);
                    }
                    request.setAttribute("miniNews", miniNews);
                    RequestDispatcher rd = request.getRequestDispatcher("/Redaction_MiniNewsEdit.jsp");
                    ArrayList<Language> languageList = languageDao.getLanguageList();
                    request.setAttribute("languageList", languageList);
                    rd.forward(request, response);
                    return;
                }
                if (page.equals("redactionNewsEditSave")) {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
                    int id = Integer.parseInt(request.getParameter("id"));
                    MiniNews n = new MiniNews();
                    n.setId(id);
                    n.setCreatedById(user.getId());
                    n.setCompanyId(user.getCompanyId());
                    n.setTime(sdf.parse(request.getParameter("time")));
                    n.setLanguageId(Integer.parseInt(request.getParameter("language")));
                    n.setPublished(request.getParameter("publish") != null);
                    n.setBody(request.getParameter("body"));
                    if (n.getId() == 0) {
                        n.setId(newsDao.insertMiniNews(n));
                    } else {
                        newsDao.modifyMiniNews(n);
                    }
                    String url = "Dispatcher?page=redactionNewsEdit&id=" + n.getId();
                    request.setAttribute("url", url);
                    RequestDispatcher rd = request.getRequestDispatcher("/_refresh.jsp");
                    rd.forward(request, response);
                    return;
                }
                if (page.equals("redactionNewsDelete")) {
                    int id = Integer.parseInt(request.getParameter("id"));
                    MiniNews n = new MiniNews();
                    n.setId(id);
                    n.setCompanyId(user.getCompanyId());
                    newsDao.deleteMiniNews(n);
                    String url = "Dispatcher?page=redactionNewsList";
                    request.setAttribute("url", url);
                    RequestDispatcher rd = request.getRequestDispatcher("/_refresh.jsp");
                    rd.forward(request, response);
                    return;
                }
                
                if (page.equals("redactionInquiryList")) {
                    ArrayList<Inquiry> inquiryList = inquiryDao.getInquiryList(user, false);
                    request.setAttribute("inquiryList", inquiryList);
                    RequestDispatcher rd = request.getRequestDispatcher("/Redaction_InquiryList.jsp");
                    rd.forward(request, response);
                    return;
                }
                if (page.equals("redactionInquiryEdit")) {
                    int id = Integer.parseInt(request.getParameter("id"));
                    Inquiry inquiry = new Inquiry();
                    if (id != 0) {
                        inquiry = inquiryDao.getInquiry(user, id);
                    }
                    request.setAttribute("inquiry", inquiry);
                    RequestDispatcher rd = request.getRequestDispatcher("/Redaction_InquiryEdit.jsp");
                    rd.forward(request, response);
                    return;
                }
                if (page.equals("redactionInquirySave")) {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
                    int id = Integer.parseInt(request.getParameter("id"));
                    Inquiry i = new Inquiry();
                    i.setId(id);
                    i.setCompanyId(user.getCompanyId());
                    i.setUserId(user.getId());
                    i.setDescription(request.getParameter("description"));
                    i.setStartingDate(sdf.parse(request.getParameter("startingDate")));
                    i.setEndingDate(sdf.parse(request.getParameter("endingDate")));
                    i.setEnabled(request.getParameter("publish") != null);
                    ArrayList<InquiryOption> ioList = new ArrayList<InquiryOption>();
                    int n = 1;
                    while (request.getParameter("oid_" + n) != null) {
                        InquiryOption io = new InquiryOption();
                        io.setId(Integer.valueOf(request.getParameter("oid_" + n)));
                        io.setInquiryId(i.getId());
                        io.setDescription(request.getParameter("o_" + n));
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
                    request.setAttribute("url", url);
                    RequestDispatcher rd = request.getRequestDispatcher("/_refresh.jsp");
                    rd.forward(request, response);
                    return;
                }
                if (page.equals("redactionInquiryOptionDelete")) {
                    int id = Integer.parseInt(request.getParameter("id"));
                    InquiryOption io = inquiryDao.getInquiryOption(user.getCompanyId(), id);
                    inquiryDao.deleteInquiryOption(io);
                    String url = "Dispatcher?page=redactionInquiryEdit&id=" + io.getInquiryId();
                    request.setAttribute("url", url);
                    RequestDispatcher rd = request.getRequestDispatcher("/_refresh.jsp");
                    rd.forward(request, response);
                    return;
                }
                
                if (page.equals("redactionArticleMenu")) {
                    Menu menu = menuDao.getMenu(company.getId());
                    menu.setActiveSection(-1);
                    request.setAttribute("menu", menu);
                    RequestDispatcher rd = request.getRequestDispatcher("/Redaction_ArticleMenu.jsp");
                    rd.forward(request, response);
                    return;
                }
                if (page.equals("redactionArticleMenuEdit")) {
                    int id = Integer.parseInt(request.getParameter("id"));
                    MenuNode menuNode = new MenuNode();
                    if (id != 0) {
                        menuNode = menuDao.getMenuNode(id, user.getCompanyId());
                    }
                    request.setAttribute("menuNode", menuNode);
                    ArrayList<MenuNode> menuNodeList = menuDao.getMenuNodeList(user.getCompanyId());
                    request.setAttribute("menuNodeList", menuNodeList);
                    RequestDispatcher rd = request.getRequestDispatcher("/Redaction_ArticleMenuEdit.jsp");
                    rd.forward(request, response);
                    return;
                }
                if (page.equals("redactionArticleMenuSave")) {
                    MenuNode n = new MenuNode();
                    n.setId(Integer.parseInt(request.getParameter("id")));
                    n.setDescription(request.getParameter("description"));
                    n.setParentId(Integer.parseInt(request.getParameter("parent")));
                    //-- disable recursive join
                    if ((n.getId() != 0) && (n.getId() == n.getParentId())) {
                        n.setParentId(0);
                    }
                    //--
                    if (n.getId() == 0) {
                        n.setId(menuDao.insertMenuNode(n, user.getCompanyId()));
                    } else {
                        menuDao.updateMenuNode(n, user.getCompanyId());
                    }
                    String url = "Dispatcher?page=redactionArticleMenuEdit&id=" + n.getId();
                    request.setAttribute("url", url);
                    RequestDispatcher rd = request.getRequestDispatcher("/_refresh.jsp");
                    rd.forward(request, response);
                    return;
                }
                if (page.equals("redactionArticleMenuDelete")) {
                    MenuNode n = new MenuNode();
                    n.setId(Integer.parseInt(request.getParameter("id")));
                    menuDao.deleteMenuNode(n, user.getCompanyId());
                    String url = "Dispatcher?page=redactionArticleMenu";
                    request.setAttribute("url", url);
                    RequestDispatcher rd = request.getRequestDispatcher("/_refresh.jsp");
                    rd.forward(request, response);
                    return;
                }
            }
            
        
            // ******************
            // * Administration *
            // ******************
            if (user.hasPermission("menu_administration")) {
                if (page.equals("companyDetail")) {
                    Company currCompany = compDao.getCompany(company.getId());
                    request.setAttribute("currCompany", currCompany);
                    RequestDispatcher rd = request.getRequestDispatcher("/Administration_companyDetail.jsp");
                    rd.forward(request, response);
                    return;
                }

                if (page.equals("companySave")) {
                    Company c = new Company();
                    c.setId(company.getId());
                    c.setName(request.getParameter("name"));
                    c.setAddress(request.getParameter("address"));
                    c.setCity(request.getParameter("city"));
                    c.setPostCode(request.getParameter("postCode"));
                    c.setPhone(request.getParameter("phone"));
                    c.setFax(request.getParameter("fax"));
                    c.seteMail(request.getParameter("eMail"));
                    c.setRegistrationNo(request.getParameter("registrationNo"));
                    c.setVatRegistrationNo(request.getParameter("vatRegistrationNo"));
                    c.setInternetDomain(request.getParameter("internetDomain"));
                    compDao.modifyCompany(c);
                    session.setAttribute("company", compDao.getCompany(company.getId()));
                    String url = "Dispatcher?page=companyDetail";
                    request.setAttribute("url", url);
                    RequestDispatcher rd = request.getRequestDispatcher("/_refresh.jsp");
                    rd.forward(request, response);
                    return;
                }
                
                if (page.equals("buildingDetail")) {
                    Company currCompany = compDao.getCompany(company.getId());
                    request.setAttribute("currCompany", currCompany);
                    Building building = buildingDao.getBuilding(company.getId());
                    request.setAttribute("building", building);
                    RequestDispatcher rd = request.getRequestDispatcher("/Administration_buildingDetail.jsp");
                    rd.forward(request, response);
                    return;
                }
                
                if (page.equals("buildingSave")) {
                    Building b = buildingDao.getBuilding(company.getId());
                    b.setAddress(request.getParameter("address"));
                    b.setCity(request.getParameter("city"));
                    b.setPostCode(request.getParameter("postCode"));
                    b.setRegistrationNo(request.getParameter("registrationNo"));
                    buildingDao.modifyBuilding(b);
                    String url = "Dispatcher?page=buildingDetail";
                    request.setAttribute("url", url);
                    RequestDispatcher rd = request.getRequestDispatcher("/_refresh.jsp");
                    rd.forward(request, response);
                    return;
                }
                
                if (page.equals("buildingPictureSave")) {
                    FileItemFactory factory = new DiskFileItemFactory();
                    ServletFileUpload upload = new ServletFileUpload(factory);
                    //upload.setSizeMax(yourMaxRequestSize);
                    List items = upload.parseRequest(request);
                    java.util.Iterator iterator = items.iterator();
                    while (iterator.hasNext()) {
                        FileItem item = (FileItem) iterator.next();
                        File f = new File(item.getName());
                        if (!item.isFormField()) {
                            compDao.savePicture(company.getId(), item.getContentType(), f.getName(), item.get());
                        }
                    }
                    session.setAttribute("company", compDao.getCompany(company.getId()));
                    String url = "Dispatcher?page=buildingDetail";
                    request.setAttribute("url", url);
                    RequestDispatcher rd = request.getRequestDispatcher("/_refresh.jsp");
                    rd.forward(request, response);
                    return;
                }
                
                if (page.equals("buildingUnitList")) {
                    Company currCompany = compDao.getCompany(company.getId());
                    request.setAttribute("currCompany", currCompany);
                    ArrayList<BuildingUnitType> buildingUnitType = buildingDao.getBuildingUnitTypeList();
                    request.setAttribute("buildingUnitType", buildingUnitType);
                    int typeId = 0;
                    if (request.getParameter("typeId") != null) {
                        typeId = Integer.valueOf(request.getParameter("typeId"));
                    }
                    ArrayList<BuildingUnit> buildingUnitList = buildingDao.getBuildingUnitList(
                            buildingDao.getBuilding(company.getId()).getId(),
                            typeId);
                    request.setAttribute("buildingUnitList", buildingUnitList);
                    RequestDispatcher rd = request.getRequestDispatcher("/Administration_buildingUnitList.jsp");
                    rd.forward(request, response);
                    return;
                }
                
                if (page.equals("buildingUnitEdit")) {
                    Company currCompany = compDao.getCompany(company.getId());
                    request.setAttribute("currCompany", currCompany);
                    ArrayList<BuildingUnitType> buildingUnitType = buildingDao.getBuildingUnitTypeList();
                    request.setAttribute("buildingUnitType", buildingUnitType);
                    BuildingUnit buildingUnit = null;
                    int id = Integer.valueOf(request.getParameter("id"));
                    if (id == 0) {
                        buildingUnit = new BuildingUnit();
                        buildingUnit.setBuildingId(buildingDao.getBuilding(company.getId()).getId());
                    } else {
                        buildingUnit = buildingDao.getBuildingUnit(id);
                    }
                    request.setAttribute("buildingUnit", buildingUnit);
                    RequestDispatcher rd = request.getRequestDispatcher("/Administration_buildingUnitDetail.jsp");
                    rd.forward(request, response);
                    return;
                }
                
                if (page.equals("buildingUnitSave")) {
                    BuildingUnit u = new BuildingUnit();
                    u.setId(Integer.valueOf(request.getParameter("id")));
                    u.setBuildingId(buildingDao.getBuilding(company.getId()).getId());
                    u.setBuildingUnitTypeId(Integer.valueOf(request.getParameter("typeId")));
                    u.setRegistrationId(request.getParameter("registrationNo"));
                    u.setDescription(request.getParameter("description"));
                    u.setNumerator(Integer.valueOf(request.getParameter("numerator")));
                    u.setDenominator(Integer.valueOf(request.getParameter("denominator")));
                    if (u.getId() == 0) {
                        u.setId(buildingDao.insertBuildingUnit(u));
                    } else {
                        buildingDao.modifyBuildingUnit(u);
                    }
                    String url = "Dispatcher?page=buildingUnitEdit&id=" + u.getId();
                    request.setAttribute("url", url);
                    RequestDispatcher rd = request.getRequestDispatcher("/_refresh.jsp");
                    rd.forward(request, response);
                    return;
                }
                
                if (page.equals("buildingUnitDelete")) {
                    BuildingUnit u = new BuildingUnit();
                    u.setId(Integer.valueOf(request.getParameter("id")));
                    u.setBuildingId(buildingDao.getBuilding(company.getId()).getId());
                    buildingDao.deleteBuildingUnit(u);
                    String url = "Dispatcher?page=buildingUnitList";
                    request.setAttribute("url", url);
                    RequestDispatcher rd = request.getRequestDispatcher("/_refresh.jsp");
                    rd.forward(request, response);
                    return;
                }
                
                if (page.equals("buildingUnitOwner")) {
                    Company currCompany = compDao.getCompany(company.getId());
                    request.setAttribute("currCompany", currCompany);
                    int id = Integer.valueOf(request.getParameter("id"));
                    BuildingUnit buildingUnit = buildingDao.getBuildingUnit(id);
                    request.setAttribute("buildingUnit", buildingUnit);
                    ArrayList<User> userList = buildingDao.getBuildingUnitHasUserList(id);
                    request.setAttribute("userList", userList);
                    RequestDispatcher rd = request.getRequestDispatcher("/Administration_buildingUnitOwner.jsp");
                    rd.forward(request, response);
                    return;
                }

                if (page.equals("userList")) {
                    Company currCompany = compDao.getCompany(company.getId());
                    request.setAttribute("currCompany", currCompany);
                    ArrayList<User> userList = userDao.getUserList(company.getId(), false);
                    request.setAttribute("userList", userList);
                    RequestDispatcher rd = request.getRequestDispatcher("/Administration_userList.jsp");
                    rd.forward(request, response);
                    return;
                }

                if (page.equals("userEdit")) {
                    Company currCompany = compDao.getCompany(company.getId());
                    request.setAttribute("currCompany", currCompany);
                    User cUser = null;
                    int id = Integer.valueOf(request.getParameter("id"));
                    if (id == 0) {
                        cUser = new User();
                        cUser.setCompanyId(company.getId());
                    } else {
                        cUser = userDao.getUser(company.getId(), id);
                    }
                    request.setAttribute("cUser", cUser);
                    ArrayList<Language> languageList = languageDao.getLanguageList();
                    request.setAttribute("languageList", languageList);
                    ArrayList<Role> roleList = roleDao.getRoleList(company.getId());
                    request.setAttribute("roleList", roleList);
                    request.setAttribute("message", "");
                    RequestDispatcher rd = request.getRequestDispatcher("/Administration_userDetail.jsp");
                    rd.forward(request, response);
                    return;
                }

                if (page.equals("userSave")) {
                    User u = new User();
                    u.setId(Integer.valueOf(request.getParameter("id")));
                    u.setCompanyId(company.getId());
                    u.setSalutation(request.getParameter("salutation"));
                    u.setFirstName(request.getParameter("firstName"));
                    u.setLastName(request.getParameter("lastName"));
                    u.setLanguageId(Integer.valueOf(request.getParameter("language")));
                    u.setAddress(request.getParameter("address"));
                    u.setCity(request.getParameter("city"));
                    u.setPostCode(request.getParameter("postCode"));
                    u.setCountry(request.getParameter("country"));
                    u.setFixedPhone(request.getParameter("fixedPhone"));
                    u.setCellPhone(request.getParameter("cellPhone"));
                    u.seteMail(request.getParameter("eMail"));
                    u.setShowInPhoneList(request.getParameter("phoneList") != null);
                    u.setLogin(request.getParameter("login"));
                    u.setPassword(request.getParameter("password"));
                    u.setEnabled(request.getParameter("enabled") != null);
                    HashMap uRoles = new HashMap();
                    ArrayList<Role> roles = roleDao.getRoleList(company.getId());
                    Iterator<Role> roleI = roles.iterator();
                    while (roleI.hasNext()) {
                        Role r = roleI.next();
                        if (request.getParameter("r_" + r.getId()) != null) {
                            uRoles.put(new Integer(r.getId()), r.getDescription());
                        }
                    }
                    u.setRoles(uRoles);
                    String message = "";
                    if (!userDao.testLoginValidity(u.getLogin())) {
                        message += language.getText("Login is not valid.") + " (" + u.getLogin() + ")<br>";
                    }
                    if (!userDao.testLoginDuplicity(u.getLogin(), u.getId())) {
                        message += language.getText("Login already exists.") + " (" + u.getLogin() + ")<br>";
                    }
                    if (!userDao.testPasswordValidity(u.getPassword())) {
                        message += language.getText("Password is too short. Minimum is 6 characters.") + "<br>";
                    }
                    if (message.equals("")) {
                        if (u.getId() == 0) {
                            u.setId(userDao.insertUser(u));
                        } else {
                            userDao.modifyUser(u);
                        }
                        message = language.getText("User has been saved.");
                    }
                    Company currCompany = compDao.getCompany(company.getId());
                    request.setAttribute("currCompany", currCompany);
                    request.setAttribute("cUser", u);
                    ArrayList<Language> languageList = languageDao.getLanguageList();
                    request.setAttribute("languageList", languageList);
                    ArrayList<Role> roleList = roleDao.getRoleList(company.getId());
                    request.setAttribute("roleList", roleList);
                    request.setAttribute("message", message);
                    RequestDispatcher rd = request.getRequestDispatcher("/Administration_userDetail.jsp");
                    rd.forward(request, response);
                    return;
                }
                
                if (page.equals("userDelete")) {
                    User u = new User();
                    u.setId(Integer.valueOf(request.getParameter("id")));
                    u.setCompanyId(company.getId());
                    userDao.deleteUser(u);
                    String url = "Dispatcher?page=userList";
                    request.setAttribute("url", url);
                    RequestDispatcher rd = request.getRequestDispatcher("/_refresh.jsp");
                    rd.forward(request, response);
                    return;
                }
                
                if (page.equals("userBuildingUnits")) {
                    Company currCompany = compDao.getCompany(company.getId());
                    request.setAttribute("currCompany", currCompany);
                    User cUser = userDao.getUser(company.getId(), Integer.valueOf(request.getParameter("id")));
                    request.setAttribute("cUser", cUser);
                    ArrayList<BuildingUnit> userHasUnitList = buildingDao.getUserHasBuildingUnitList(cUser.getId());
                    request.setAttribute("userHasUnitList", userHasUnitList);
                    ArrayList<BuildingUnit> unitList = buildingDao.getUnconnectedBuildingUnitList(company.getId());
                    request.setAttribute("unitList", unitList);
                    RequestDispatcher rd = request.getRequestDispatcher("/Administration_userUnits.jsp");
                    rd.forward(request, response);
                    return;
                }
                
                if (page.equals("userBuildingUnitAdd")) {
                    buildingDao.addUserHasBuildingUnitConnection(
                            Integer.valueOf(request.getParameter("userId")), 
                            Integer.valueOf(request.getParameter("unitId")));
                    String url = "Dispatcher?page=userBuildingUnits&id=" + request.getParameter("userId");
                    request.setAttribute("url", url);
                    RequestDispatcher rd = request.getRequestDispatcher("/_refresh.jsp");
                    rd.forward(request, response);
                    return;
                }
                
                if (page.equals("userBuildingUnitRemove")) {
                    buildingDao.deleteUserHasBuildingUnitConnection(
                            Integer.valueOf(request.getParameter("userId")), 
                            Integer.valueOf(request.getParameter("unitId")));
                    String url = "Dispatcher?page=userBuildingUnits&id=" + request.getParameter("userId");
                    request.setAttribute("url", url);
                    RequestDispatcher rd = request.getRequestDispatcher("/_refresh.jsp");
                    rd.forward(request, response);
                    return;
                }

                if (page.equals("roleList")) {
                    Company currCompany = compDao.getCompany(company.getId());
                    request.setAttribute("currCompany", currCompany);
                    ArrayList<Role> roleList = roleDao.getRoleList(company.getId());
                    request.setAttribute("roleList", roleList);
                    RequestDispatcher rd = request.getRequestDispatcher("/Administration_roleList.jsp");
                    rd.forward(request, response);
                    return;
                }

                if (page.equals("roleEdit")) {
                    Company currCompany = compDao.getCompany(company.getId());
                    request.setAttribute("currCompany", currCompany);
                    Role role = null;
                    int id = Integer.valueOf(request.getParameter("id"));
                    if (id == 0) {
                        role = new Role();
                        role.setCompanyId(company.getId());
                    } else {
                        role = roleDao.getRole(company.getId(), Integer.valueOf(request.getParameter("id")));
                    }
                    request.setAttribute("role", role);
                    ArrayList<Permission> permissionList = roleDao.getPermissionList();
                    request.setAttribute("permissionList", permissionList);
                    RequestDispatcher rd = request.getRequestDispatcher("/Administration_roleDetail.jsp");
                    rd.forward(request, response);
                    return;
                }

                if (page.equals("roleSave")) {
                    Role role = new Role();
                    role.setId(Integer.valueOf(request.getParameter("id")));
                    role.setCompanyId(company.getId());
                    role.setDescription(request.getParameter("description"));
                    HashMap props = new HashMap();
                    ArrayList<Permission> perms = roleDao.getPermissionList();
                    Iterator<Permission> permsI = perms.iterator();
                    while (permsI.hasNext()) {
                        Permission p = permsI.next();
                        if (request.getParameter("p_" + p.getId()) != null) {
                            props.put(new Integer(p.getId()), p.getDescription());
                        }
                    }
                    role.setPermissions(props);
                    if (role.getId() == 0) {
                        role.setId(roleDao.insertRole(role));
                    } else {
                        roleDao.modifyRole(role);
                    }
                    String url = "Dispatcher?page=roleEdit&id=" + role.getId();
                    request.setAttribute("url", url);
                    RequestDispatcher rd = request.getRequestDispatcher("/_refresh.jsp");
                    rd.forward(request, response);
                    return;
                }

                if (page.equals("roleDelete")) {
                    Role role = new Role();
                    role.setId(Integer.valueOf(request.getParameter("id")));
                    role.setCompanyId(company.getId());
                    roleDao.deleteRole(role);
                    String url = "Dispatcher?page=roleList";
                    request.setAttribute("url", url);
                    RequestDispatcher rd = request.getRequestDispatcher("/_refresh.jsp");
                    rd.forward(request, response);
                    return;
                }
                
                if (page.equals("propertyList")) {
                    Company currCompany = compDao.getCompany(company.getId());
                    request.setAttribute("currCompany", currCompany);
                    RequestDispatcher rd = request.getRequestDispatcher("/Administration_propertyList.jsp");
                    rd.forward(request, response);
                    return;
                }
                
                if (page.equals("propertyEdit")) {
                    Company currCompany = compDao.getCompany(company.getId());
                    request.setAttribute("currCompany", currCompany);
                    String key = request.getParameter("key");
                    request.setAttribute("key", key);
                    RequestDispatcher rd = request.getRequestDispatcher("/Administration_propertyDetail.jsp");
                    rd.forward(request, response);
                    return;
                }
                
                if (page.equals("propertySave")) {
                    String origKey = request.getParameter("origKey");
                    String key = request.getParameter("key");
                    String value = request.getParameter("value");
                    if (setup.getProperty(origKey) != null) {
                        setupDao.deleteProperty(company.getId(), origKey);
                    }
                    setupDao.insertProperty(company.getId(), key, value);
                    session.setAttribute("setup", null);
                    String url = "Dispatcher?page=propertyEdit&key=" + key;
                    request.setAttribute("url", url);
                    RequestDispatcher rd = request.getRequestDispatcher("/_refresh.jsp");
                    rd.forward(request, response);
                    return;
                }
                
                if (page.equals("propertyDelete")) {
                    String key = request.getParameter("key");
                    setupDao.deleteProperty(company.getId(), key);
                    session.setAttribute("setup", null);
                    String url = "Dispatcher?page=propertyList";
                    request.setAttribute("url", url);
                    RequestDispatcher rd = request.getRequestDispatcher("/_refresh.jsp");
                    rd.forward(request, response);
                    return;
                }
            }
            
            // ************************
            // * Page does not exists *
            // ************************
            
            RequestDispatcher rd = request.getRequestDispatcher("/BadPage.jsp");
            rd.forward(request, response);
            return;
            
        } catch (Exception ex) {
            out.println(ex.getMessage());
        } finally {            
            out.close();
            closeConnection(cnn);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
    
    private ArrayList<SystemMenuEntry> createSystemMenu(User u) {
        ArrayList<SystemMenuEntry> result = new ArrayList<SystemMenuEntry>();
        if (u.hasPermission("menu_articles")) result.add(new SystemMenuEntry("Articles", "Dispatcher?page=articleList"));
        if (u.hasPermission("menu_contact")) result.add(new SystemMenuEntry("Contact", "Dispatcher?page=contact"));
        if (u.hasPermission("menu_building_units")) result.add(new SystemMenuEntry("Units", "Dispatcher?page=myBuildingUnitList"));
        if (u.hasPermission("menu_personal_settings")) result.add(new SystemMenuEntry("Personal settings", "Dispatcher?page=psUserDetail"));
        if (u.hasPermission("menu_redaction")) result.add(new SystemMenuEntry("Redaction", "Dispatcher?page=redactionArticleList"));
        if (u.hasPermission("menu_administration")) result.add(new SystemMenuEntry("Administration", "Dispatcher?page=companyDetail"));
        
        return result;
    }
    
    private Connection createConnection() throws javax.naming.NamingException, SQLException {
        Context initCtx = new InitialContext();
        Context envCtx = (Context) initCtx.lookup("java:comp/env");
        DataSource ds = (DataSource) envCtx.lookup("jdbc/SVJIS");
        return ds.getConnection();
    }
    
    private void closeConnection(Connection cnn) {
        if (cnn != null) {
            try {
                cnn.close();
            } catch (SQLException ex) {
                Logger.getLogger(Dispatcher.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
