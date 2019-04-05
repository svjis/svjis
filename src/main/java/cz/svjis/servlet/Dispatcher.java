/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.svjis.servlet;

import cz.svjis.servlet.cmd.HandleErrorCmd;
import cz.svjis.bean.ApplicationSetupDAO;
import cz.svjis.bean.Company;
import cz.svjis.bean.CompanyDAO;
import cz.svjis.bean.Language;
import cz.svjis.bean.LanguageDAO;
import cz.svjis.bean.LogDAO;
import cz.svjis.bean.SystemMenuEntry;
import cz.svjis.bean.User;
import cz.svjis.bean.UserDAO;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
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
            ApplicationSetupDAO setupDao = new ApplicationSetupDAO(cnn);
            LogDAO logDao = new LogDAO(cnn);
            
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
            // * Run command   *
            // *****************
            Command cmd = CmdFactory.create(page, ctx);
            cmd.execute();
            
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
