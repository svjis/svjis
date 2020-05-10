/*
 *       MailSenderJob.java
 *
 *       This file is part of SVJIS project.
 *       https://github.com/svjis/svjis
 *
 *       SVJIS is free software; you can redistribute it and/or modify
 *       it under the terms of the GNU General Public License as published by
 *       the Free Software Foundation; either version 3 of the License, or
 *       (at your option) any later version. <http://www.gnu.org/licenses/>
 */

package cz.svjis.scheduler;

import cz.svjis.bean.ApplicationSetupDAO;
import cz.svjis.bean.Company;
import cz.svjis.bean.CompanyDAO;
import cz.svjis.bean.MailDAO;
import cz.svjis.bean.Message;
import cz.svjis.servlet.Dispatcher;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import org.apache.commons.mail.EmailException;

/**
 *
 * @author berk
 */
public class MailSenderJob implements Runnable {
    private static final Logger LOGGER = Logger.getLogger(MailSenderJob.class.getName());
    
    @Override
    public void run() {
        Connection cnn = null;
        try {
            cnn = createConnection();
            CompanyDAO companyDao = new CompanyDAO(cnn);
            List<Company> companyList = companyDao.getCompanyList();
            for(Company c: companyList) {
                processAllMessagesForCompany(cnn, c);
            }
        } catch (SQLException | NamingException ex) {
            LOGGER.log(Level.SEVERE, "Could not run MailSenderJob", ex);
        } finally {
            closeConnection(cnn);
        }
    }
    
    private void processAllMessagesForCompany(Connection cnn, Company c) {
        try {
            ApplicationSetupDAO setup = new ApplicationSetupDAO(cnn);
            Properties props = setup.getApplicationSetup(c.getId());
            
            MailDAO mailDao = new MailDAO(
                    cnn,
                    props.getProperty("mail.smtp"),
                    props.getProperty("mail.login"),
                    props.getProperty("mail.password"),
                    props.getProperty("mail.sender"));
            
            List<Message> messageList = mailDao.getWaitingMessages(c.getId());
            
            for (Message m: messageList) {
                mailDao.sendInstantMail(m.getRecipient(), m.getSubject(), m.getBody());
                mailDao.updateMessageStatus(m.getId(), 1, new Date());
            }
            
        } catch (SQLException | EmailException ex) {
            LOGGER.log(Level.SEVERE, "Could not process all messages", ex);
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
    }
}
