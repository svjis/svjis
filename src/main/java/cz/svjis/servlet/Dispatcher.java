/*
 *       Dispatcher.java
 *
 *       This file is part of SVJIS project.
 *       https://github.com/svjis/svjis
 *
 *       SVJIS is free software; you can redistribute it and/or modify
 *       it under the terms of the GNU General Public License as published by
 *       the Free Software Foundation; either version 3 of the License, or
 *       (at your option) any later version. <http://www.gnu.org/licenses/>
 */

package cz.svjis.servlet;

import cz.svjis.servlet.cmd.HandleErrorCmd;
import cz.svjis.bean.ApplicationSetupDAO;
import cz.svjis.bean.Company;
import cz.svjis.bean.CompanyDAO;
import cz.svjis.bean.Language;
import cz.svjis.bean.LanguageDAO;
import cz.svjis.bean.LogDAO;
import cz.svjis.bean.Permission;
import cz.svjis.bean.Setup;
import cz.svjis.bean.SystemMenuEntry;
import cz.svjis.bean.User;
import cz.svjis.bean.UserDAO;
import cz.svjis.common.PermanentLoginUtils;
import cz.svjis.servlet.cmd.SelectCompanyCmd;
import cz.svjis.validator.Validator;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
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

    private static final long serialVersionUID = 1462376190610692513L;
    private static final Logger LOGGER = Logger.getLogger(Dispatcher.class.getName());
    
    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) {
        
        CmdContext ctx = new CmdContext();
        ctx.setRequest(request);
        ctx.setResponse(response);
        
        PrintWriter out = null;
        Connection cnn = null;
        
        try {
            response.setContentType("text/html;charset=UTF-8");
            request.setCharacterEncoding("UTF-8");
            out = response.getWriter();
        
            cnn = createConnection();
            ctx.setCnn(cnn);
            
            int parSetCompany = Validator.getInt(request, "setcompany", 0, Validator.MAX_INT_ALLOWED, true);
            String parPage = Validator.getString(request, "page", 0, 100, true, false);
            
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
            
            if (parSetCompany != 0) {
                company = compDao.getCompany(parSetCompany);
                session.setAttribute("company", company);
                session.setAttribute("user", null);
                session.setAttribute("setup", null);
                session.setAttribute("language", null);
            }
            
            if (company == null) {
                new SelectCompanyCmd(ctx).execute();
                return;
            }
            ctx.setCompany(company);
            
            // *****************
            // * Setup         *
            // *****************
            
            Setup setup = (Setup) session.getAttribute("setup");
            if (setup == null) {
                setup = setupDao.getApplicationSetup(company.getId());
                session.setAttribute("setup", setup);
            }
            ctx.setSetup(setup);
            
            // *****************
            // * User          *
            // *****************
            
            User user = (User) session.getAttribute("user");
            Language language = (Language) session.getAttribute("language");
                        
            if (user == null) {
                if (PermanentLoginUtils.checkPermanentLogin(request, userDao, company.getId()) != 0) {
                    user = userDao.getUser(company.getId(), PermanentLoginUtils.checkPermanentLogin(request, userDao, company.getId()));
                    user.setUserLogged(true);
                    logDao.log(user.getId(), LogDAO.OPERATION_TYPE_LOGIN, LogDAO.ID_NULL, request.getRemoteAddr(), request.getHeader("User-Agent"));
                    PermanentLoginUtils.savePermanentLogin(response, user, userDao, setup);
                } else {
                    user = new User();
                    user.setCompanyId(company.getId());
                    if (userDao.getUser(company.getId(), setup.getAnonymousUserId()) != null) {
                        user = userDao.getUser(company.getId(), setup.getAnonymousUserId());
                    }
                }
                session.setAttribute("user", user);
                language = languageDao.getDictionary(user.getLanguageId());
                session.setAttribute("language", language);
            }
            
            ctx.setUser(user);
            ctx.setLanguage(language);
            
            // *****************
            // * System menu   *
            // *****************
            ArrayList<SystemMenuEntry> systemMenu = createSystemMenu(user);
            request.setAttribute("systemMenu", systemMenu);
            
            // *****************
            // * Run command   *
            // *****************
            String page = parPage;
            
            if (page == null) {
                page = Cmd.ARTICLE_LIST;
            }
            
            Command cmd = CmdFactory.create(page, ctx);
            cmd.execute();
            
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Could not dispatch page", ex);

            //-- send e-mail
            HandleErrorCmd errCmd = new HandleErrorCmd(ctx, ex);
            errCmd.execute();
        } finally {
            if (out != null) {
                out.close();
            }
            closeConnection(cnn);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        processRequest(request, response);
    }

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
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
        String d = "Dispatcher?page=%s";
        ArrayList<SystemMenuEntry> result = new ArrayList<>();
        if (u.hasPermission(Permission.MENU_ARTICLES)) result.add(new SystemMenuEntry("Articles", String.format(d, Cmd.ARTICLE_LIST)));
        if (u.hasPermission(Permission.MENU_CONTACT)) result.add(new SystemMenuEntry("Contact", String.format(d, Cmd.CONTACT)));
        if (u.hasPermission(Permission.MENU_BUILDING_UNITS)) result.add(new SystemMenuEntry("Units", String.format(d, Cmd.MY_BUILDING_UNITS)));
        if (u.hasPermission(Permission.MENU_PERSONAL_SETTINGS)) result.add(new SystemMenuEntry("Personal settings", String.format(d, Cmd.PERSONAL_DETAIL)));
        if (u.hasPermission(Permission.MENU_REDACTION)) result.add(new SystemMenuEntry("Redaction", String.format(d, Cmd.REDACTION_ARTICLE_LIST)));
        if (u.hasPermission(Permission.MENU_FAULT_REPORTING)) result.add(new SystemMenuEntry("Fault reporting", String.format(d, Cmd.FAULT_LIST)));
        if (u.hasPermission(Permission.MENU_ADVERTS)) result.add(new SystemMenuEntry("Adverts", String.format(d, Cmd.ADVERT_LIST)));
        if (u.hasPermission(Permission.MENU_ADMINISTRATION)) result.add(new SystemMenuEntry("Administration", String.format(d, Cmd.COMPANY_DETAIL)));
        
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
                LOGGER.log(Level.SEVERE, null, ex);
            }
        }
    }
}
