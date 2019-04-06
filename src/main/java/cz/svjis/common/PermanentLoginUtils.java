/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.svjis.common;

import cz.svjis.bean.User;
import cz.svjis.bean.UserDAO;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author jaroslav_b
 */
public class PermanentLoginUtils {
    
    public static void clearPermanentLogin(HttpServletRequest request, HttpServletResponse response) {
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
    
    public static void savePermanentLogin(HttpServletRequest request, HttpServletResponse response, User user, UserDAO userDao) throws NoSuchAlgorithmException, SQLException {
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
    
    public static int checkPermanentLogin(HttpServletRequest request, HttpServletResponse response, UserDAO userDao, int companyId) throws SQLException, NoSuchAlgorithmException {
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
    
    private static String getCookie(Cookie cookies[], String key) {
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
    
}
