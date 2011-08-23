/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.svjis.bean;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

/**
 *
 * @author berk
 */
public class MailDAO {
    private String smtp;
    private String login;
    private String password;
    private String sender;

    public MailDAO(String smtp, String login, String password, String sender) {
        this.smtp = smtp;
        this.login = login;
        this.password = password;
        this.sender = sender;
    }

    public void sendMail(String recipient, String subject, String body) throws EmailException {
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
}
