/*
 *       PermanentLoginUtils.java
 *
 *       This file is part of SVJIS project.
 *       https://github.com/svjis/svjis
 *
 *       SVJIS is free software; you can redistribute it and/or modify
 *       it under the terms of the GNU General Public License as published by
 *       the Free Software Foundation; either version 3 of the License, or
 *       (at your option) any later version. <http://www.gnu.org/licenses/>
 */

package cz.svjis.common;

import cz.svjis.bean.Setup;
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

    private PermanentLoginUtils() {}

    /**
     *
     * @param response
     */
    public static void clearPermanentLogin(HttpServletResponse response) {
        int age = 60;
        Cookie cookie;
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

    /**
     *
     * @param response
     * @param user
     * @param userDao
     * @param setup
     * @throws NoSuchAlgorithmException
     * @throws SQLException
     */
    public static void savePermanentLogin(HttpServletResponse response, User user, UserDAO userDao, Setup setup) throws NoSuchAlgorithmException, SQLException {
        int age = setup.getPermanentLoginInHours() * 3600;
        String token = userDao.getAuthToken(user.getCompanyId(), user.getLogin());
        
        if (token == null) {
            token = userDao.createNewAuthToken(user.getCompanyId(), user.getLogin(), setup.getPermanentLoginInHours());
        }

        Cookie cookie;
        cookie = new Cookie("company", String.valueOf(user.getCompanyId()));
        cookie.setMaxAge(age);
        response.addCookie(cookie);
        cookie = new Cookie("login", user.getLogin());
        cookie.setMaxAge(age);
        response.addCookie(cookie);
        cookie = new Cookie("password", token);
        cookie.setMaxAge(age);
        response.addCookie(cookie);
    }

    /**
     *
     * @param request
     * @param userDao
     * @param companyId
     * @return returns userId in case of success or 0 in case of failure
     * @throws SQLException
     * @throws NoSuchAlgorithmException
     */
    public static int checkPermanentLogin(HttpServletRequest request, UserDAO userDao, int companyId) throws SQLException, NoSuchAlgorithmException {
        int result = 0;
        Cookie[] cookies = request.getCookies();
        int company = (getCookie(cookies, "company").equals("")) ? 0 : Integer.valueOf(getCookie(cookies, "company"));
        String login = getCookie(cookies, "login");
        String token = getCookie(cookies, "password");
        User u = userDao.getUserByLogin(company, login);
        if ((company == companyId) && (u != null) && verifyAuthToken(userDao, company, login, token) && (u.isEnabled())) {
            result = u.getId();
        }
        return result;
    }
    
    private static boolean verifyAuthToken(UserDAO userDao, int companyId, String login, String token) throws SQLException {
        boolean result = false;
                
        String t = userDao.getAuthToken(companyId, login);
        if ((t != null) && (token != null) && !token.equals("") && token.equals(t)) {
            result = true;
        }
        
        return result;
    }

    /**
     *
     * @param cookies
     * @param key
     * @return cookie value
     */
    private static String getCookie(Cookie[] cookies, String key) {
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
