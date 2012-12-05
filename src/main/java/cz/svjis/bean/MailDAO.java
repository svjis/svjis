/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.svjis.bean;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

/**
 *
 * @author berk
 */
public class MailDAO {
    
    public static final int messageTypeMail = 1;
    public static final int messageTypeSMS = 2;
    
    private Connection cnn;
    private String smtp;
    private String login;
    private String password;
    private String sender;

    public MailDAO(Connection cnn, String smtp, String login, String password, String sender) {
        this.cnn = cnn;
        this.smtp = smtp;
        this.login = login;
        this.password = password;
        this.sender = sender;
    }

    public void sendInstantMail(String recipient, String subject, String body) throws EmailException {
        HtmlEmail email = new HtmlEmail();
        email.setCharset("UTF-8");
        email.setHostName(smtp);
        email.setAuthentication(login, password);
        email.addTo(recipient, recipient);
        email.setFrom(sender, sender);
        email.setSubject(subject);
        // embed the image and get the content id
        //URL url = new URL("http://www.apache.org/images/asf_logo_wide.gif");
        //String cid = email.embed(url, "Apache logo");
        // set the html message
        //email.setHtmlMsg("<html>The apache logo - <img src=\"cid:"+cid+"\"></html>");
        // set the alternative message
        //email.setTextMsg("Your email client does not support HTML messages");
        email.setHtmlMsg(body);
        email.send();
    }   
    
    public void queueMail(int companyId, String recipient, String subject, String body) throws SQLException {
        String insert = "INSERT INTO MESSAGE_QUEUE ("
                + "MESSAGE_TYPE_ID, "
                + "RECIPIENT, "
                + "SUBJECT, "
                + "BODY, "
                + "CREATION_TIME, "
                + "STATUS, "
                + "COMPANY_ID) VALUES (?,?,?,?,?,?,?)";
        PreparedStatement ps = cnn.prepareStatement(insert);
        ps.setInt(1, MailDAO.messageTypeMail);
        ps.setString(2, truncate(recipient,50));
        ps.setString(3, truncate(subject,100));
        ps.setString(4, body);
        ps.setTimestamp(5, new java.sql.Timestamp(new Date().getTime()));
        ps.setInt(6, 0);
        ps.setInt(7, companyId);
        ps.execute();
        ps.close();
    }
    
    private String truncate(String str, int maxLength) {
        if (str == null) {
            str = "";
        }
        if (str.length() > maxLength) {
            str = str.substring(0, maxLength-1);
        }
        return str;
    }
    
    public ArrayList<Message> getWaitingMessages(int companyId) throws SQLException {
        ArrayList<Message> result = new ArrayList<Message>();
        String select = "SELECT "
            + "a.ID, "
            + "a.MESSAGE_TYPE_ID, "
            + "a.RECIPIENT, "
            + "a.SUBJECT, "
            + "a.BODY, "
            + "a.CREATION_TIME, "
            + "a.SENDING_TIME, "
            + "a.STATUS, "
            + "a.COMPANY_ID "
            + "FROM MESSAGE_QUEUE a "
            + "WHERE (a.STATUS = 0) and (a.MESSAGE_TYPE_ID = ?) and (a.COMPANY_ID = ?)";
        
        PreparedStatement psSelect = cnn.prepareStatement(select);
        psSelect.setInt(1, MailDAO.messageTypeMail);
        psSelect.setInt(2, companyId);
        ResultSet rs = psSelect.executeQuery();
        while (rs.next()) {
            Message m = new Message();
            m.setId(rs.getInt("ID"));
            m.setTypeId(rs.getInt("MESSAGE_TYPE_ID"));
            m.setRecipient(rs.getString("RECIPIENT"));
            m.setSubject(rs.getString("SUBJECT"));
            m.setBody(rs.getString("BODY"));
            m.setCreationTime(rs.getTimestamp("CREATION_TIME"));
            m.setSendingTime(rs.getTimestamp("SENDING_TIME"));
            m.setStatus(rs.getInt("STATUS"));
            m.setCompany(rs.getInt("COMPANY_ID"));
            result.add(m);
        }
        rs.close();
        psSelect.close();
        
        return result;
    }
    
    public void updateMessageStatus(int messageId, int status, Date sendingTime) throws SQLException {
        String update = "UPDATE MESSAGE_QUEUE a SET "
            + "a.STATUS = ?, "
            + "a.SENDING_TIME = ? "
            + "WHERE a.ID = ?";
        
        PreparedStatement psUpdate = cnn.prepareStatement(update);
        psUpdate.setInt(1, status);
        psUpdate.setTimestamp(2, new java.sql.Timestamp(sendingTime.getTime()));
        psUpdate.setInt(3, messageId);
        psUpdate.executeUpdate();
        psUpdate.close();
    }
    
    public void sendErrorReport(String recipient, String url, Throwable throwable) throws EmailException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        HtmlEmail email = new HtmlEmail();
        email.setCharset("UTF-8");
        email.setHostName(smtp);
        email.setAuthentication(login, password);
        email.addTo(recipient, recipient);
        email.setFrom(sender, sender);
        email.setSubject("SVJIS: Error report");
        email.setHtmlMsg("<p>Time: " + sdf.format(new Date()) + "</p><p>URL: " + url + "</p><p>" + getStackTrace(throwable).replace("\n", "<br>") + "</p>");
        email.send();
    }
    
    private String getStackTrace(Throwable throwable) {
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        throwable.printStackTrace(printWriter);
        return writer.toString();
    }
}
