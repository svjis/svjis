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

import cz.svjis.bean.User;
import cz.svjis.bean.UserDAO;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author jaroslav_b
 */
public class PermanentLoginUtils {

    private static final Logger LOGGER = Logger.getLogger(PermanentLoginUtils.class.getName());
    public static final String PERMANENT_LOGIN_TTL = "permanent.login.hours";

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
    public static void savePermanentLogin(HttpServletResponse response, User user, UserDAO userDao, Properties setup) throws NoSuchAlgorithmException, SQLException {
        int ttl;

        if (setup.getProperty(PERMANENT_LOGIN_TTL) == null) {
            ttl = 1;
        } else {
            try {
                ttl = Integer.valueOf(setup.getProperty(PERMANENT_LOGIN_TTL));
            } catch (NumberFormatException ex) {
                LOGGER.log(Level.SEVERE, "Invalid TTL value", ex);
                ttl = 1;
            }
        }
        int age = ttl * 3600;

        Cookie cookie;
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

    /**
     *
     * @param request
     * @param userDao
     * @param companyId
     * @return returns userId
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
        if ((company == companyId) && (u != null) && userDao.verifyAuthToken(company, login, token) && (u.isEnabled())) {
            result = u.getId();
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
