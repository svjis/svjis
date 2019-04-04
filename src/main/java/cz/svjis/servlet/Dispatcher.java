/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.svjis.servlet;

import cz.svjis.servlet.cmd.HandleErrorCmd;
import cz.svjis.bean.ApplicationSetupDAO;
import cz.svjis.bean.ArticleDAO;
import cz.svjis.bean.BuildingDAO;
import cz.svjis.bean.BuildingUnit;
import cz.svjis.bean.Company;
import cz.svjis.bean.CompanyDAO;
import cz.svjis.bean.InquiryDAO;
import cz.svjis.bean.Language;
import cz.svjis.bean.LanguageDAO;
import cz.svjis.bean.LogDAO;
import cz.svjis.bean.MailDAO;
import cz.svjis.bean.MenuDAO;
import cz.svjis.bean.Message;
import cz.svjis.bean.MiniNewsDAO;
import cz.svjis.bean.Permission;
import cz.svjis.bean.Role;
import cz.svjis.bean.RoleDAO;
import cz.svjis.bean.SystemMenuEntry;
import cz.svjis.bean.User;
import cz.svjis.bean.UserDAO;
import cz.svjis.servlet.cmd.ArticleDetailCmd;
import cz.svjis.servlet.cmd.ArticleInquiryVoteCmd;
import cz.svjis.servlet.cmd.ArticleInsertCommentCmd;
import cz.svjis.servlet.cmd.ArticleListCmd;
import cz.svjis.servlet.cmd.ArticleSearchCmd;
import cz.svjis.servlet.cmd.BuildingDetailCmd;
import cz.svjis.servlet.cmd.BuildingPictureSaveCmd;
import cz.svjis.servlet.cmd.BuildingSaveCmd;
import cz.svjis.servlet.cmd.BuildingUnitDeleteCmd;
import cz.svjis.servlet.cmd.BuildingUnitEditCmd;
import cz.svjis.servlet.cmd.BuildingUnitListCmd;
import cz.svjis.servlet.cmd.BuildingUnitOwnerCmd;
import cz.svjis.servlet.cmd.BuildingUnitSaveCmd;
import cz.svjis.servlet.cmd.CompanyDetailCmd;
import cz.svjis.servlet.cmd.CompanySaveCmd;
import cz.svjis.servlet.cmd.ContactCompanyCmd;
import cz.svjis.servlet.cmd.ContactPhoneListCmd;
import cz.svjis.servlet.cmd.LostPasswordCmd;
import cz.svjis.servlet.cmd.LostPasswordSubmitCmd;
import cz.svjis.servlet.cmd.MyBuildingUnitsCmd;
import cz.svjis.servlet.cmd.PersonalPasswordChangeCmd;
import cz.svjis.servlet.cmd.PersonalPasswordChangeSaveCmd;
import cz.svjis.servlet.cmd.PersonalUserDetailCmd;
import cz.svjis.servlet.cmd.PersonalUserDetailSaveCmd;
import cz.svjis.servlet.cmd.RedactionArticleAttachmentDeleteCmd;
import cz.svjis.servlet.cmd.RedactionArticleAttachmentSaveCmd;
import cz.svjis.servlet.cmd.RedactionArticleEditCmd;
import cz.svjis.servlet.cmd.RedactionArticleListCmd;
import cz.svjis.servlet.cmd.RedactionArticleMenuCmd;
import cz.svjis.servlet.cmd.RedactionArticleMenuDeleteCmd;
import cz.svjis.servlet.cmd.RedactionArticleMenuEditCmd;
import cz.svjis.servlet.cmd.RedactionArticleMenuSaveCmd;
import cz.svjis.servlet.cmd.RedactionArticleSaveCmd;
import cz.svjis.servlet.cmd.RedactionArticleSendNotificationsCmd;
import cz.svjis.servlet.cmd.RedactionArticleSendNotificationsConfirmationCmd;
import cz.svjis.servlet.cmd.RedactionInquiryEditCmd;
import cz.svjis.servlet.cmd.RedactionInquiryListCmd;
import cz.svjis.servlet.cmd.RedactionInquiryLogCmd;
import cz.svjis.servlet.cmd.RedactionInquiryOptionDeleteCmd;
import cz.svjis.servlet.cmd.RedactionInquirySaveCmd;
import cz.svjis.servlet.cmd.RedactionNewsDeleteCmd;
import cz.svjis.servlet.cmd.RedactionNewsEditCmd;
import cz.svjis.servlet.cmd.RedactionNewsEditSaveCmd;
import cz.svjis.servlet.cmd.RedactionNewsListCmd;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

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
        
        CmdContext ctx = new CmdContext();
        ctx.setRequest(request);
        ctx.setResponse(response);
        
        try {
            cnn = createConnection();
            ctx.setCnn(cnn);
            
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
            
            if (company == null) {
                ArrayList<Company> companyList = compDao.getCompanyList();
                request.setAttribute("companyList", companyList);
                RequestDispatcher rd = request.getRequestDispatcher("/CompanyList.jsp");
                rd.forward(request, response);
                return;
            }
            
            // *****************
            // * Setup         *
            // *****************
            
            Properties setup = (Properties) session.getAttribute("setup");
            if (setup == null) {
                setup = setupDao.getApplicationSetup(company.getId());
                session.setAttribute("setup", setup);
            }
            
            // *****************
            // * Login         *
            // *****************
            
            User user = (User) session.getAttribute("user");
            Language language = (Language) session.getAttribute("language");
            
            if (page.equals("logout") || (user == null)) {
                if (page.equals("logout")) {
                    logDao.log(user.getId(), LogDAO.operationTypeLogout, LogDAO.idNull, request.getRemoteAddr(), request.getHeader("User-Agent"));
                    clearPermanentLogin(request, response);
                }
                user = new User();
                user.setCompanyId(company.getId());
                if ((!page.equals("logout")) && (checkPermanentLogin(request, response, userDao, company.getId()) != 0)) {
                    user = userDao.getUser(company.getId(), checkPermanentLogin(request, response, userDao, company.getId()));
                    user.setUserLogged(true);
                    logDao.log(user.getId(), LogDAO.operationTypeLogin, LogDAO.idNull, request.getRemoteAddr(), request.getHeader("User-Agent"));
                    savePermanentLogin(request, response, user, userDao);
                } else {
                    if ((setup.getProperty("anonymous.user.id") != null) && (userDao.getUser(company.getId(), 
                            Integer.valueOf(setup.getProperty("anonymous.user.id"))) != null)) {
                        user = userDao.getUser(company.getId(), Integer.valueOf(setup.getProperty("anonymous.user.id")));
                    }
                }
                session.setAttribute("user", user);
                language = languageDao.getDictionary(user.getLanguageId());
                session.setAttribute("language", language);
                if (page.equals("logout")) {
                    page = "articleList";
                }
            }
            
            if (page.equals("login") && (company != null)) {
                User u = userDao.getUserByLogin(company.getId(), request.getParameter("login"));
                if ((u != null) && userDao.verifyPassword(u, request.getParameter("password"), true)) {
                    user = u;
                    session.setAttribute("user", user);
                    language = languageDao.getDictionary(user.getLanguageId());
                    session.setAttribute("language", language);
                    page = "articleList";
                    logDao.log(user.getId(), LogDAO.operationTypeLogin, LogDAO.idNull, request.getRemoteAddr(), request.getHeader("User-Agent"));
                    savePermanentLogin(request, response, user, userDao);
                } else {
                    request.setAttribute("messageHeader", language.getText("Bad login"));
                    request.setAttribute("message", "<p>" + language.getText("You can continue") + " <a href=\"Dispatcher\">" + language.getText("here") + "</a>.</p><p><a href=\"Dispatcher?page=lostPassword\">" + language.getText("Forgot password?") + "</a></p>");
                    RequestDispatcher rd = request.getRequestDispatcher("/_message.jsp");
                    rd.forward(request, response);
                    return;
                }
            }
            
            // *****************
            // * Context       *
            // *****************
            ctx.setCompany(company);
            ctx.setSetup(setup);
            ctx.setLanguage(language);
            ctx.setUser(user);
            
            // *****************
            // * System menu   *
            // *****************
            ArrayList<SystemMenuEntry> systemMenu = createSystemMenu(user);
            request.setAttribute("systemMenu", systemMenu);
            
            // *****************
            // * Lost login    *
            // *****************
            if (page.equals("lostPassword")) {
                new LostPasswordCmd(ctx).execute();
                return;
            }
            
            if (page.equals("lostPassword_submit")) {
                new LostPasswordSubmitCmd(ctx).execute();
                return;
            }
            
            // *****************
            // * Article       *
            // *****************
            if (user.hasPermission("menu_articles")) {
                
                if (page.equals("articleList")) {
                    new ArticleListCmd(ctx).execute();
                    return;
                }

                if (page.equals("search")) {
                    new ArticleSearchCmd(ctx).execute();
                    return;
                }
                
                if (page.equals("inquiryVote")) {
                    new ArticleInquiryVoteCmd(ctx).execute();
                    return;
                }
                
                if (page.equals("articleDetail")) {
                    new ArticleDetailCmd(ctx).execute();
                    return;
                }
                
                if (page.equals("insertArticleComment")) {
                    new ArticleInsertCommentCmd(ctx).execute();
                    return;
                }
            }
            
            // *****************
            // * Contact       *
            // *****************
            if (user.hasPermission("menu_contact")) {
                
                if (page.equals("contact")) {
                    new ContactCompanyCmd(ctx).execute();
                    return;
                }
            
                if (page.equals("phonelist")) {
                    new ContactPhoneListCmd(ctx).execute();
                    return;
                }
            }
            
            // ***********************
            // * My bulilding units  *
            // ***********************
            if (user.hasPermission("menu_building_units")) {
                if (page.equals("myBuildingUnitList")) {
                    new MyBuildingUnitsCmd(ctx).execute();
                    return;
                }
            }
            
            // **********************
            // * Personal settings  *
            // **********************
            if (user.hasPermission("menu_personal_settings")) {
                if (page.equals("psUserDetail")) {
                    new PersonalUserDetailCmd(ctx).execute();
                    return;
                }
                
                if (page.equals("psUserDetailSave")) {
                    new PersonalUserDetailSaveCmd(ctx).execute();
                    return;
                }
                
                if (page.equals("psPasswordChange")) {
                    new PersonalPasswordChangeCmd(ctx).execute();
                    return;
                }
                if (page.equals("psPasswordChangeSave")) {
                    new PersonalPasswordChangeSaveCmd(ctx).execute();
                    return;
                }
            }
            
            // *****************
            // * Redaction     *
            // *****************
            if (user.hasPermission("menu_redaction")) {
                if (page.equals("redactionArticleList")) {
                    new RedactionArticleListCmd(ctx).execute();
                    return;
                }
                
                if (page.equals("redactionArticleEdit")) {
                    new RedactionArticleEditCmd(ctx).execute();
                    return;
                }
                
                if (page.equals("redactionArticleSave")) {
                    new RedactionArticleSaveCmd(ctx).execute();
                    return;
                }
                
                if (page.equals("redactionArticleSendNotifications")) {
                    new RedactionArticleSendNotificationsCmd(ctx).execute();
                    return;
                }
                
                if (page.equals("redactionArticleSendNotificationsConfirmation")) {
                    new RedactionArticleSendNotificationsConfirmationCmd(ctx).execute();
                    return;
                }
                
                if (page.equals("redactionArticleAttachmentSave")) {
                    new RedactionArticleAttachmentSaveCmd(ctx).execute();
                    return;
                }
                if (page.equals("redactionArticleAttachmentDelete")) {
                    new RedactionArticleAttachmentDeleteCmd(ctx).execute();
                    return;
                }
                
                if (page.equals("redactionNewsList")) {
                    new RedactionNewsListCmd(ctx).execute();
                    return;
                }
                if (page.equals("redactionNewsEdit")) {
                    new RedactionNewsEditCmd(ctx).execute();
                    return;
                }
                if (page.equals("redactionNewsEditSave")) {
                    new RedactionNewsEditSaveCmd(ctx).execute();
                    return;
                }
                if (page.equals("redactionNewsDelete")) {
                    new RedactionNewsDeleteCmd(ctx).execute();
                    return;
                }
                
                if (page.equals("redactionInquiryList")) {
                    new RedactionInquiryListCmd(ctx).execute();
                    return;
                }
                if (page.equals("redactionInquiryLog")) {
                    new RedactionInquiryLogCmd(ctx).execute();
                    return;
                }
                if (page.equals("redactionInquiryEdit")) {
                    new RedactionInquiryEditCmd(ctx).execute();
                    return;
                }
                if (page.equals("redactionInquirySave")) {
                    new RedactionInquirySaveCmd(ctx).execute();
                    return;
                }
                if (page.equals("redactionInquiryOptionDelete")) {
                    new RedactionInquiryOptionDeleteCmd(ctx).execute();
                    return;
                }
                
                if (page.equals("redactionArticleMenu")) {
                    new RedactionArticleMenuCmd(ctx).execute();
                    return;
                }
                if (page.equals("redactionArticleMenuEdit")) {
                    new RedactionArticleMenuEditCmd(ctx).execute();
                    return;
                }
                if (page.equals("redactionArticleMenuSave")) {
                    new RedactionArticleMenuSaveCmd(ctx).execute();
                    return;
                }
                if (page.equals("redactionArticleMenuDelete")) {
                    new RedactionArticleMenuDeleteCmd(ctx).execute();
                    return;
                }
            }
            
        
            // ******************
            // * Administration *
            // ******************
            if (user.hasPermission("menu_administration")) {
                if (page.equals("companyDetail")) {
                    new CompanyDetailCmd(ctx).execute();
                    return;
                }

                if (page.equals("companySave")) {
                    new CompanySaveCmd(ctx).execute();
                    return;
                }
                
                if (page.equals("buildingDetail")) {
                    new BuildingDetailCmd(ctx).execute();
                    return;
                }
                
                if (page.equals("buildingSave")) {
                    new BuildingSaveCmd(ctx).execute();
                    return;
                }
                
                if (page.equals("buildingPictureSave")) {
                    new BuildingPictureSaveCmd(ctx).execute();
                    return;
                }
                
                if (page.equals("buildingUnitList")) {
                    new BuildingUnitListCmd(ctx).execute();
                    return;
                }
                
                if (page.equals("buildingUnitEdit")) {
                    new BuildingUnitEditCmd(ctx).execute();
                    return;
                }
                
                if (page.equals("buildingUnitSave")) {
                    new BuildingUnitSaveCmd(ctx).execute();
                    return;
                }
                
                if (page.equals("buildingUnitDelete")) {
                    new BuildingUnitDeleteCmd(ctx).execute();
                    return;
                }
                
                if (page.equals("buildingUnitOwner")) {
                    new BuildingUnitOwnerCmd(ctx).execute();
                    return;
                }

                if (page.equals("userList")) {
                    Company currCompany = compDao.getCompany(company.getId());
                    request.setAttribute("currCompany", currCompany);
                    ArrayList<Role> roleList = roleDao.getRoleList(company.getId());
                    request.setAttribute("roleList", roleList);
                    int roleId = Integer.valueOf((request.getParameter("roleId") == null) ? "0" : request.getParameter("roleId"));
                    ArrayList<User> userList = userDao.getUserList(company.getId(), false, roleId);
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
                    //if (!userDao.testPasswordValidity(u.getPassword())) {
                    //    message += language.getText("Password is too short. Minimum is 6 characters.") + "<br>";
                    //}
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
                    ArrayList<BuildingUnit> unitList = buildingDao.getBuildingUnitList(company.getId(), 0);
                    request.setAttribute("unitList", unitList);
                    RequestDispatcher rd = request.getRequestDispatcher("/Administration_userUnits.jsp");
                    rd.forward(request, response);
                    return;
                }
                
                if (page.equals("userBuildingUnitAdd")) {
                    if (Integer.valueOf(request.getParameter("unitId")) != 0) {
                        buildingDao.addUserHasBuildingUnitConnection(
                                Integer.valueOf(request.getParameter("userId")), 
                                Integer.valueOf(request.getParameter("unitId")));
                    }
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
                
                if (page.equals("messagesPending")) {
                    Company currCompany = compDao.getCompany(company.getId());
                    request.setAttribute("currCompany", currCompany);
                    MailDAO mailDao = new MailDAO(
                            cnn,
                            setup.getProperty("mail.smtp"),
                            setup.getProperty("mail.login"),
                            setup.getProperty("mail.password"),
                            setup.getProperty("mail.sender"));
                    ArrayList<Message> messageList = mailDao.getWaitingMessages(company.getId());
                    request.setAttribute("messageList", messageList);
                    RequestDispatcher rd = request.getRequestDispatcher("/Administration_messageList.jsp");
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
            ex.printStackTrace();
            HandleErrorCmd errCmd = new HandleErrorCmd(ctx, ex);
            try {
                errCmd.execute();
            } catch (Exception exx) {
                exx.printStackTrace();
            }
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
    
    private void clearPermanentLogin(HttpServletRequest request, HttpServletResponse response) {
        int age = 365 * 24 * 60 * 60;
        Cookie cookie = null;
        cookie = new Cookie("company", "0");
        cookie.setMaxAge(age);
        response.addCookie(cookie);
        cookie = new Cookie("login", "");
        cookie.setMaxAge(age);
        response.addCookie(cookie);
        cookie = new Cookie("password", "");
        cookie.setMaxAge(age);
        response.addCookie(cookie);
    }
    
    private void savePermanentLogin(HttpServletRequest request, HttpServletResponse response, User user, UserDAO userDao) throws NoSuchAlgorithmException, SQLException {
        int age = 365 * 24 * 60 * 60;
        Cookie cookie = null;
        cookie = new Cookie("company", String.valueOf(user.getCompanyId()));
        cookie.setMaxAge(age);
        response.addCookie(cookie);
        cookie = new Cookie("login", user.getLogin());
        cookie.setMaxAge(age);
        response.addCookie(cookie);
        cookie = new Cookie("password", userDao.getAuthToken(user.getCompanyId(), user.getLogin()));
        cookie.setMaxAge(age);
        response.addCookie(cookie);
    }
    
    private int checkPermanentLogin(HttpServletRequest request, HttpServletResponse response, UserDAO userDao, int companyId) throws SQLException, NoSuchAlgorithmException {
        int result = 0;
        Cookie cookies[] = request.getCookies();
        int company = (getCookie(cookies, "company").equals("")) ? 0 : Integer.valueOf(getCookie(cookies, "company"));
        String login = getCookie(cookies, "login");
        String token = getCookie(cookies, "password");
        User u = userDao.getUserByLogin(company, login);
        if ((company == companyId) && (u != null) && userDao.verifyAuthToken(company, login, token) && (u.isEnabled())) {
            result = u.getId();
        }
        return result;
    }
    
    private String getCookie(Cookie cookies[], String key) {
        String result = "";
        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                if (cookies [i].getName().equals(key)) {
                    result = cookies[i].getValue();
                    break;
                }
            }
        }
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
