/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

/**
 *
 * @author berk
 */
public class MailSenderJob implements Runnable {
    
    @Override
    public void run() {
        Connection cnn = null;
        try {
            cnn = createConnection();
            CompanyDAO companyDao = new CompanyDAO(cnn);
            ArrayList<Company> companyList = companyDao.getCompanyList();
            for(Company c: companyList) {
                processAllMessagesForCompany(cnn, c);
            }
        } catch (Exception e) {
            e.printStackTrace();
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
            
            ArrayList<Message> messageList = mailDao.getWaitingMessages(c.getId());
            
            for (Message m: messageList) {
                mailDao.sendInstantMail(m.getRecipient(), m.getSubject(), m.getBody());
                mailDao.updateMessageStatus(m.getId(), 1, new Date());
            }
            
        } catch (Exception e) {
            e.printStackTrace();
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
