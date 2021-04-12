/*
 *       MailDAO.java
 *
 *       This file is part of SVJIS project.
 *       https://github.com/svjis/svjis
 *
 *       SVJIS is free software; you can redistribute it and/or modify
 *       it under the terms of the GNU General Public License as published by
 *       the Free Software Foundation; either version 3 of the License, or
 *       (at your option) any later version. <http://www.gnu.org/licenses/>
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
import java.util.List;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

/**
 *
 * @author berk
 */
public class MailDAO extends DAO {
    
    public static final int MAIL = 1;
    public static final int SMS = 2;
    
    private String smtp;
    private int smtpPort;
    private boolean smtpTLS;
    private String login;
    private String password;
    private String sender;

    public MailDAO(Connection cnn, String smtp, int smtpPort, boolean smtpTLS, String login, String password, String sender) {
        super(cnn);
        this.smtp = smtp;
        this.smtpPort = smtpPort;
        this.smtpTLS = smtpTLS;
        this.login = login;
        this.password = password;
        this.sender = sender;
    }

    public void sendInstantMail(String recipient, String subject, String body) throws EmailException {
        HtmlEmail email = new HtmlEmail();
        email.setCharset("UTF-8");
        email.setHostName(smtp);
        if (smtpTLS) {
            email.setStartTLSEnabled(true);
            email.setSmtpPort(smtpPort);
        } else {
            email.setStartTLSEnabled(false);
            email.setSmtpPort(smtpPort);
        }
        email.setAuthentication(login, password);
        email.addTo(recipient, recipient);
        email.setFrom(sender, sender);
        email.setSubject(subject);
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
        try (PreparedStatement ps = cnn.prepareStatement(insert)) {
            ps.setInt(1, MailDAO.MAIL);
            ps.setString(2, truncate(recipient,50));
            ps.setString(3, truncate(subject,100));
            ps.setString(4, body);
            ps.setTimestamp(5, new java.sql.Timestamp(new Date().getTime()));
            ps.setInt(6, 0);
            ps.setInt(7, companyId);
            ps.execute();
        }
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
    
    public List<Message> getWaitingMessages(int companyId) throws SQLException {
        ArrayList<Message> result = new ArrayList<>();
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
        
        try (PreparedStatement psSelect = cnn.prepareStatement(select)) {
            psSelect.setInt(1, MailDAO.MAIL);
            psSelect.setInt(2, companyId);
            try (ResultSet rs = psSelect.executeQuery()) {
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
            }
        }
        
        return result;
    }
    
    public void updateMessageStatus(int messageId, int status, Date sendingTime) throws SQLException {
        String update = "UPDATE MESSAGE_QUEUE a SET "
            + "a.STATUS = ?, "
            + "a.SENDING_TIME = ? "
            + "WHERE a.ID = ?";
        
        try (PreparedStatement psUpdate = cnn.prepareStatement(update)) {
            psUpdate.setInt(1, status);
            psUpdate.setTimestamp(2, new java.sql.Timestamp(sendingTime.getTime()));
            psUpdate.setInt(3, messageId);
            psUpdate.executeUpdate();
        }
    }
    
    public void sendErrorReport(String recipient, String url, String user, String userAgent, Throwable throwable) throws EmailException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        String subject = "SVJIS: Error report";
        String body = "<p>Time: " + sdf.format(new Date()) + "</p>"
                + "<p>User: " + user + "</p>"
                + "<p>User Agent: " + userAgent + "</p>"
                + "<p>URL: " + url + "</p>"
                + "<p>" + getStackTrace(throwable).replace("\n", "<br>") + "</p>";
        sendInstantMail(recipient, subject, body);
    }
    
    private String getStackTrace(Throwable throwable) {
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        throwable.printStackTrace(printWriter);
        return writer.toString();
    }
}
