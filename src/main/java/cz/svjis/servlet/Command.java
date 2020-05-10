/*
 *       Command.java
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

import cz.svjis.bean.Company;
import cz.svjis.bean.Language;
import cz.svjis.bean.User;
import java.sql.Connection;
import java.util.Properties;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author jaroslav_b
 */
public abstract class Command {
    
    private CmdContext cmdCtx;
    
    public Command(CmdContext cmdCtx) {
        this.cmdCtx = cmdCtx;
    }
    
    public abstract void execute() throws Exception;
    
    public Company getCompany() {
        return cmdCtx.getCompany();
    }
    
    public Properties getSetup() {
        return cmdCtx.getSetup();
    }
    
    public Language getLanguage() {
        return cmdCtx.getLanguage();
    }
    
    public User getUser() {
        return cmdCtx.getUser();
    }
    
    public HttpServletRequest getRequest() {
        return cmdCtx.getRequest();
    }
    
    public HttpServletResponse getResponse() {
        return cmdCtx.getResponse();
    }
    
    public Connection getCnn() {
        return cmdCtx.getCnn();
    }
}
