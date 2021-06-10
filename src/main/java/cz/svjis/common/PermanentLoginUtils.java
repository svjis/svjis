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
import cz.svjis.validator.InputValidationException;
import cz.svjis.validator.Validator;

import java.sql.SQLException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author jaroslav_b
 */
public class PermanentLoginUtils {

    public static final String PM_COMPANY = "company";
    public static final String PM_LOGIN = "login";
    public static final String PM_PASSWORD = "password";
    
    private PermanentLoginUtils() {}

    /**
     * 
     * @param response
     * @param user
     * @param userDao
     * @param setup
     * @throws SQLException 
     */
    public static void clearPermanentLogin(HttpServletResponse response, User user, UserDAO userDao, Setup setup) throws SQLException {
        int age = 60;
        response.addCookie(createCookie(PM_COMPANY, "0", age));
        response.addCookie(createCookie(PM_LOGIN, "", age));
        response.addCookie(createCookie(PM_PASSWORD, "", age));
        userDao.createNewAuthToken(user.getCompanyId(), user.getLogin(), setup.getPermanentLoginInHours());
    }

    /**
     *
     * @param response
     * @param user
     * @param userDao
     * @param setup
     * @throws SQLException
     */
    public static void savePermanentLogin(HttpServletResponse response, User user, UserDAO userDao, Setup setup) throws SQLException {
        int age = setup.getPermanentLoginInHours() * 3600;
        String token = userDao.createNewAuthToken(user.getCompanyId(), user.getLogin(), setup.getPermanentLoginInHours());

        response.addCookie(createCookie(PM_COMPANY, String.valueOf(user.getCompanyId()), age));
        response.addCookie(createCookie(PM_LOGIN, user.getLogin(), age));
        response.addCookie(createCookie(PM_PASSWORD, token, age));
    }

    /**
     *
     * @param request
     * @param userDao
     * @param companyId
     * @return returns userId in case of success or 0 in case of failure
     * @throws SQLException
     */
    public static int checkPermanentLogin(HttpServletRequest request, UserDAO userDao, int companyId) throws SQLException {
        int result = 0;
        int company;
        String login;
        String token;

        try {
            company = Validator.getIntFromCookie(request, PM_COMPANY, 0, Validator.MAX_INT_ALLOWED, false);
            login = Validator.getStringFromCookie(request, PM_LOGIN, 0, 50, false, false);
            token = Validator.getStringFromCookie(request, PM_PASSWORD, 0, 100, false, false);
        } catch (InputValidationException ex) {
            return result;
        }
        
        User u = userDao.getUserByLogin(company, login);
        if ((company == companyId) && (u != null) && u.isEnabled() && verifyAuthToken(userDao, company, login, token)) {
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
     * @param name
     * @param value
     * @param expiry
     * @return cookie
     */
    private static Cookie createCookie(String name, String value, int expiry) {
        Cookie c = new Cookie(name, value);
        c.setMaxAge(expiry);
        c.setHttpOnly(true);
        c.setSecure(true);
        return c;
    }
    
}
