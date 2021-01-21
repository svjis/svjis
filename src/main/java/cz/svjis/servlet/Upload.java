/*
 *       Upload.java
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
import cz.svjis.bean.Company;
import cz.svjis.bean.User;
import cz.svjis.validator.Validator;
import java.sql.Connection;
import java.sql.SQLException;
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
public class Upload extends HttpServlet {
    
    private static final Logger LOGGER = Logger.getLogger(Upload.class.getName());

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/html;charset=UTF-8");
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1
        response.setHeader("Pragma", "no-cache"); // HTTP 1.0
        response.setDateHeader("Expires", 0); // Proxies.

        CmdContext ctx = new CmdContext();
        ctx.setRequest(request);
        ctx.setResponse(response);

        HttpSession session = request.getSession();
        Company company = (Company) session.getAttribute("company");
        User user = (User) session.getAttribute("user");
        ctx.setCompany(company);
        ctx.setUser(user);

        if ((company == null) || (user == null)) {
            return;
        }

        Connection cnn = null;

        try {
            request.setCharacterEncoding("UTF-8");
            
            cnn = createConnection();
            ctx.setCnn(cnn);
            
            String parPage = Validator.getString(request, "page", 0, 100, true, false);

            // *****************
            // * Run command   *
            // *****************
            
            String page = parPage;
            if (page == null) {
                page = "";
            }

            Command cmd = CmdFactoryUpload.create(page, ctx);
            if (cmd != null) {
                cmd.execute();
            }

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Could not dispatch upload", ex);

            //-- send e-mail
            HandleErrorCmd errCmd = new HandleErrorCmd(ctx, ex);
            errCmd.execute();
        } finally {
            closeConnection(cnn);
        }
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
    }    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
