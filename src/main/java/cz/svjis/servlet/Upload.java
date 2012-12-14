/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.svjis.servlet;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import cz.svjis.bean.ArticleAttachment;
import cz.svjis.bean.ArticleDAO;
import cz.svjis.bean.BuildingDAO;
import cz.svjis.bean.BuildingUnit;
import cz.svjis.bean.Company;
import cz.svjis.bean.Language;
import cz.svjis.bean.LanguageDAO;
import cz.svjis.bean.MailDAO;
import cz.svjis.bean.User;
import cz.svjis.bean.UserDAO;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
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
import jxl.Workbook;
import jxl.format.Colour;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.NumberFormats;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

/**
 *
 * @author berk
 */
public class Upload extends HttpServlet {

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
        
        HttpSession session = request.getSession();
        Company company = (Company) session.getAttribute("company");
        User user = (User) session.getAttribute("user");
        
        String page = request.getParameter("page");
        if (page == null) {
            page = "";
        }
        
        Connection cnn = null;
        
        try {
            cnn = createConnection();
            
            if (page.equals("download")) {
                ArticleDAO dao = new ArticleDAO(cnn);
                ArticleAttachment aa = dao.getArticleAttachment(Integer.parseInt(request.getParameter("id")));
                if (dao.getArticle(user, aa.getArticleId()) == null) {
                    return;
                }
                writeBinaryData(aa.getContentType(), aa.getFileName(), aa.getData(), request, response);
                return;
            }
            
            if (page.equals("getBuildingPicture")) {
                writeBinaryData(company.getPictureContentType(), company.getPictureFilename(), company.getPictureData(), request, response);
                return;
            }
            if (user.hasPermission("menu_administration")) {
                if (page.equals("exportBuildingUnitListToXls")) {
                    exportBuildingUnitListToXls(user, cnn, request, response);
                    return;
                }
                if (page.equals("exportUserListToXls")) {
                    exportUserListToXls(user, cnn, request, response);
                    return;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            
            Properties setup = (Properties) session.getAttribute("setup");
            if ((setup != null) && (setup.getProperty("error.report.recipient") != null)) {
                MailDAO mailDao = new MailDAO(
                                cnn,
                                setup.getProperty("mail.smtp"),
                                setup.getProperty("mail.login"),
                                setup.getProperty("mail.password"),
                                setup.getProperty("mail.sender"));
                try {
                    mailDao.sendErrorReport(
                            setup.getProperty("error.report.recipient"), 
                            request.getRequestURL().toString() + "/" + request.getQueryString(), 
                            ex);
                } catch (Exception e) {}
            }
            RequestDispatcher rd = request.getRequestDispatcher("/Error.jsp");
            rd.forward(request, response);
        } finally {            
            closeConnection(cnn);
        }
    }
    
    private void writeBinaryData(String contentType, String fileName, byte[] data, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String userAgent = request.getHeader("User-Agent");
        String encodedFileName = null;
        if (userAgent.contains("MSIE") || userAgent.contains("Opera")) {
            encodedFileName = URLEncoder.encode(fileName.replace(" ", "_"), "UTF-8");
        } else {
            encodedFileName = "=?UTF-8?B?" + Base64.encode(fileName.replace(" ", "_").getBytes("UTF-8")) + "?=";
        }

        response.setContentType(contentType);
        response.setHeader("Content-Disposition", "attachment; filename=\"" + encodedFileName + "\"");
        response.setDateHeader("Expires", 0);
        java.io.OutputStream outb = response.getOutputStream();
        outb.write(data);
        outb.close();
    }
    
    protected void exportBuildingUnitListToXls(User user, Connection cnn, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException, WriteException {

        OutputStream outb = null;

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment; filename=BuildingUnitList_" + sdf.format(new Date()) + ".xls");
            outb = response.getOutputStream();
            WritableWorkbook w = Workbook.createWorkbook(outb);
            WritableSheet s = w.createSheet("RequestList", 0);
            
            LanguageDAO languageDao = new LanguageDAO(cnn);
            Language lang = languageDao.getDictionary(user.getLanguageId());
            BuildingDAO buildingDao = new BuildingDAO(cnn);
            ArrayList<BuildingUnit> buildingUnitList = buildingDao.getBuildingUnitList(
                            buildingDao.getBuilding(user.getCompanyId()).getId(),
                            0);
            
            WritableCellFormat bold = new WritableCellFormat(new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD));
            WritableCellFormat boldGr = new WritableCellFormat(new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD));
            boldGr.setBackground(Colour.GREY_25_PERCENT);
            
            WritableCellFormat std = new WritableCellFormat(new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD));
            std.setShrinkToFit(true);
            std.setWrap(true);
            std.setVerticalAlignment(VerticalAlignment.TOP);
            
            WritableCellFormat num = new WritableCellFormat (NumberFormats.INTEGER);
            num.setVerticalAlignment(VerticalAlignment.TOP);
            WritableCellFormat dateTimeFormat = new WritableCellFormat (new jxl.write.DateFormat ("dd.MM.yyyy HH:mm"));
            dateTimeFormat.setVerticalAlignment(VerticalAlignment.TOP);
            
            int l = 0;
            int c = 0;

            //-- Header
            s.addCell(new Label(c, l, lang.getText("Building unit list"), bold));
            l += 2;
            s.setColumnView(c, 20);
            s.addCell(new Label(c++, l, lang.getText("Type"), boldGr));
            s.setColumnView(c, 14);
            s.addCell(new Label(c++, l, lang.getText("Registration Id."), boldGr));
            s.setColumnView(c, 20);
            s.addCell(new Label(c++, l, lang.getText("Description"), boldGr));
            s.setColumnView(c, 30);
            s.addCell(new Label(c++, l, lang.getText("Owner list"), boldGr));
            s.setColumnView(c, 14);
            s.addCell(new Label(c++, l, lang.getText("Numerator"), boldGr));
            s.setColumnView(c, 14);
            s.addCell(new Label(c++, l, lang.getText("Denominator"), boldGr));
            
            Iterator<BuildingUnit> i = buildingUnitList.iterator();
            while(i.hasNext()) {
                BuildingUnit u = i.next();
                l++;
                c = 0;

                s.addCell(new jxl.write.Label(c++, l, u.getBuildingUnitType(), std));
                s.addCell(new jxl.write.Label(c++, l, u.getRegistrationId(), std));
                s.addCell(new jxl.write.Label(c++, l, u.getDescription(), std));
                c++;
                s.addCell(new jxl.write.Number(c++, l, u.getNumerator(), num));
                s.addCell(new jxl.write.Number(c++, l, u.getDenominator(), num));
                
                ArrayList<User> userList = buildingDao.getBuildingUnitHasUserList(u.getId());
                Iterator<User> userIterator = userList.iterator();
                while (userIterator.hasNext()) {
                    User owner = userIterator.next();
                    l++;
                    c = 3;
                    s.addCell(new jxl.write.Label(c++, l, 
                            String.valueOf(owner.getSalutation() + " " + owner.getFirstName() + " " + owner.getLastName()).trim(), std));
                }
            }
            
            w.write();
            w.close();
        } finally {
            if (outb != null) outb.close();
        }
    }
    
    protected void exportUserListToXls(User user, Connection cnn, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException, WriteException {

        OutputStream outb = null;

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment; filename=UserList_" + sdf.format(new Date()) + ".xls");
            outb = response.getOutputStream();
            WritableWorkbook w = Workbook.createWorkbook(outb);
            WritableSheet s = w.createSheet("RequestList", 0);
            
            LanguageDAO languageDao = new LanguageDAO(cnn);
            Language lang = languageDao.getDictionary(user.getLanguageId());
            UserDAO userDao = new UserDAO(cnn);
            ArrayList<User> userList = userDao.getUserList(user.getCompanyId(), false);
            
            WritableCellFormat bold = new WritableCellFormat(new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD));
            WritableCellFormat boldGr = new WritableCellFormat(new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD));
            boldGr.setBackground(Colour.GREY_25_PERCENT);
            
            WritableCellFormat std = new WritableCellFormat(new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD));
            std.setShrinkToFit(true);
            std.setWrap(true);
            std.setVerticalAlignment(VerticalAlignment.TOP);
            
            WritableCellFormat num = new WritableCellFormat (NumberFormats.INTEGER);
            num.setVerticalAlignment(VerticalAlignment.TOP);
            WritableCellFormat dateTimeFormat = new WritableCellFormat (new jxl.write.DateFormat ("dd.MM.yyyy HH:mm"));
            dateTimeFormat.setVerticalAlignment(VerticalAlignment.TOP);
            
            int l = 0;
            int c = 0;

            //-- Header
            s.addCell(new Label(c, l, lang.getText("User list"), bold));
            l += 2;
            s.setColumnView(c, 14);
            s.addCell(new Label(c++, l, lang.getText("Salutation"), boldGr));
            s.setColumnView(c, 14);
            s.addCell(new Label(c++, l, lang.getText("First name"), boldGr));
            s.setColumnView(c, 14);
            s.addCell(new Label(c++, l, lang.getText("Last name"), boldGr));
            s.setColumnView(c, 14);
            s.addCell(new Label(c++, l, lang.getText("Language"), boldGr));
            s.setColumnView(c, 30);
            s.addCell(new Label(c++, l, lang.getText("Address"), boldGr));
            s.setColumnView(c, 14);
            s.addCell(new Label(c++, l, lang.getText("City"), boldGr));
            s.setColumnView(c, 14);
            s.addCell(new Label(c++, l, lang.getText("Post code"), boldGr));
            s.setColumnView(c, 14);
            s.addCell(new Label(c++, l, lang.getText("Country"), boldGr));
            s.setColumnView(c, 20);
            s.addCell(new Label(c++, l, lang.getText("Fixed phone"), boldGr));
            s.setColumnView(c, 20);
            s.addCell(new Label(c++, l, lang.getText("Cell phone"), boldGr));
            s.setColumnView(c, 30);
            s.addCell(new Label(c++, l, lang.getText("E-Mail"), boldGr));
            s.setColumnView(c, 14);
            s.addCell(new Label(c++, l, lang.getText("Enabled"), boldGr));
            s.setColumnView(c, 20);
            s.addCell(new Label(c++, l, lang.getText("Last login"), boldGr));
            
            Iterator<User> i = userList.iterator();
            while(i.hasNext()) {
                User u = i.next();
                l++;
                c = 0;

                s.addCell(new jxl.write.Label(c++, l, u.getSalutation(), std));
                s.addCell(new jxl.write.Label(c++, l, u.getFirstName(), std));
                s.addCell(new jxl.write.Label(c++, l, u.getLastName(), std));
                //s.addCell(new jxl.write.Number(c++, l, u.getLanguageId(), num));
                s.addCell(new jxl.write.Label(c++, l, languageDao.getLanguage(u.getLanguageId()).getDescription(), std));
                s.addCell(new jxl.write.Label(c++, l, u.getAddress(), std));
                s.addCell(new jxl.write.Label(c++, l, u.getCity(), std));
                s.addCell(new jxl.write.Label(c++, l, u.getPostCode(), std));
                s.addCell(new jxl.write.Label(c++, l, u.getCountry(), std));
                s.addCell(new jxl.write.Label(c++, l, u.getFixedPhone(), std));
                s.addCell(new jxl.write.Label(c++, l, u.getCellPhone(), std));
                s.addCell(new jxl.write.Label(c++, l, u.geteMail(), std));
                s.addCell(new jxl.write.Label(c++, l, (u.isEnabled()) ? lang.getText("yes") : lang.getText("no"), std));
                if (u.getLastLogin() != null) {
                    s.addCell(new jxl.write.DateTime(c++, l, u.getLastLogin(), dateTimeFormat));
                } else {
                    c++;
                }
            }
            
            w.write();
            w.close();
        } finally {
            if (outb != null) outb.close();
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
                Logger.getLogger(Dispatcher.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
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
}
