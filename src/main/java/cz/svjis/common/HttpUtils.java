/*
 *       HttpUtils.java
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

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.codec.binary.Base64;

import cz.svjis.bean.Attachment;
import cz.svjis.bean.Language;
import cz.svjis.bean.User;

/**
 *
 * @author jaroslav_b
 */
public class HttpUtils {
    
    private static final Logger LOGGER = Logger.getLogger(HttpUtils.class.getName());
    
    private HttpUtils() {}

    public static void writeBinaryData(String contentType, String fileName, byte[] data, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        
        String userAgent = request.getHeader("User-Agent");
        String encodedFileName;
        if (userAgent.contains("MSIE") || userAgent.contains("Edge") || userAgent.contains("Opera") || userAgent.contains("Trident")) {
            encodedFileName = URLEncoder.encode(fileName.replace(" ", "_"), "UTF-8");
        } else {
            encodedFileName = "=?UTF-8?B?" + Base64.encodeBase64String(fileName.replace(" ", "_").getBytes("UTF-8")) + "?=";
        }

        response.setContentType(contentType);
        response.setHeader("Content-Disposition", "attachment; filename=\"" + encodedFileName + "\"");
        response.setDateHeader("Expires", 0);

        
        try (OutputStream outb= response.getOutputStream()) {
            outb.write(data, 0, data.length);
        } catch (java.io.IOException ex) {
            LOGGER.log(Level.SEVERE, "ClientAbortException:  java.io.IOException: Roura přerušena (SIGPIPE) ", ex);
        }
    }
    
    
    public static String makeHyperlinks(String s) {
        String result = s;
        String httpUrlRegex = "\\b(?![^<]*<\\/)(((http:\\/\\/|https:\\/\\/))[^,\"\\s<)]*)\\b";
        String httpUrlReplacement = "<a href=\"%s\" target=\"_blank\">%s</a>";
        String emailUrlRegex = "\\b(?![^<]*<\\/)([a-zA-Z_0-9.-]+\\@[a-zA-Z_0-9.-]+\\.\\w+)\\b";
        String emailUrlReplacement = "<a href=\"mailto:%s\">%s</a>";

        result = makeHyperlinks(result, httpUrlRegex, httpUrlReplacement);
        result = makeHyperlinks(result, emailUrlRegex, emailUrlReplacement);

        return result;
    }
    
    protected static String makeHyperlinks(String s, String regex, String replacement) {
        String result = s;

        java.util.regex.Pattern p = java.util.regex.Pattern.compile(regex, java.util.regex.Pattern.CASE_INSENSITIVE);
        java.util.regex.Matcher m = p.matcher(s);

        while (m.find()) {
            result = result.replaceFirst(m.group(1).replace("?", "\\?"), String.format(replacement, m.group(1), m.group(1)));
        }

        return result;
    }
    
    
    public static String highlight(String html, String textToFind) {
        return envelStrInHtml(html, textToFind, "<b style=\"color:black;background-color:#ffff66\">", "</b>");
    }
    
    protected static String envelStrInHtml(String html, String textToFind, String atBegin, String atEnd) {
        String result = html;
        String subp1 = "(?![^<]*>)";
        String subp2 = "(?:<[^>]+>)*";

        if ((textToFind != null) && !textToFind.equals("")) {

            char[] arr = textToFind.toCharArray();
            StringBuilder sbu = new StringBuilder();
            for (int i = 0; i < arr.length; i++) {
                if (i > 0) {
                    sbu.append(subp2);
                } else {
                    sbu.append(subp1);
                }

                sbu.append(String.valueOf(arr[i]));
            }
            String regexp = sbu.toString();
            
            java.util.regex.Pattern p = java.util.regex.Pattern.compile(regexp, java.util.regex.Pattern.CASE_INSENSITIVE);
            java.util.regex.Matcher m = p.matcher(html);
            StringBuffer sb = new StringBuffer();
            while (m.find()) {
                String replacement = "";
                replacement += atBegin;
                replacement += m.group();
                replacement += atEnd;
                m.appendReplacement(sb, replacement);
            }
            m.appendTail(sb);
            result = sb.toString();
        }
        return result;
    }
    
    public static String renderAttachments(List<Attachment> list, HttpServletRequest request, String name, String dwlPage, String delPage, boolean showUser, boolean showTime, boolean showDelete, boolean showDeleteOwnerOnly, String ariaDescribedby) {
        StringBuilder result = new StringBuilder();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        Language language = (Language) request.getSession().getAttribute("language");
        User user =  (User) request.getSession().getAttribute("user");
        
        if ((list == null) || list.isEmpty())
            return "";
        
        result.append("<p>");
        result.append(String.format("<table class=\"list\" aria-describedby=\"%s\">", ariaDescribedby));
        result.append("<tr>");
        result.append(String.format("<th class=\"list\" scope=\"col\" colspan=\"2\">%s</th>", language.getText(name)));
        if (showUser) {
            result.append(String.format("<th class=\"list\" scope=\"col\">%s</th>", language.getText("User")));
        }
        if (showTime) {
            result.append(String.format("<th class=\"list\" scope=\"col\">%s</th>", language.getText("Time")));
        }
        if (showDelete || showDeleteOwnerOnly) {
            result.append("<th class=\"list\" scope=\"col\">&nbsp;</th>");
        }
        result.append("</tr>");
        
        for (Attachment a: list) {
            String icon = "gfx/Files_unknown.gif";
            String extension = a.getFileName().toLowerCase().substring(a.getFileName().lastIndexOf(".") + 1);
            File f = new File(request.getServletContext().getRealPath("/gfx") + "/Files_" + extension + ".gif");
            if (f.exists()) {
                icon = "gfx/Files_" + extension + ".gif";
            }
            
            result.append("<tr>");
            result.append(String.format("<td class=\"list\"><img src=\"%s\" border=\"0\" alt=\"%s\"></td>", icon, a.getFileName()));
            result.append(String.format("<td class=\"list\"><a href=\"Upload?page=%s&id=%d\">%s</a></td>", dwlPage, a.getId(), a.getFileName()));
            if (showUser) {
                result.append(String.format("<td class=\"list\">%s</td>", a.getUser().getFullName(false)));
            }
            if (showUser) {
                result.append(String.format("<td class=\"list\">%s</td>", sdf.format(a.getUploadTime())));
            }
            if (showDelete || showDeleteOwnerOnly) {
                result.append("<td class=\"list\">");
                if (showDelete || (user.getId() == a.getUser().getId())) {
                    result.append(String.format("<a onclick=\"if (!confirm('%s %s ?')) return false;\" href=\"Dispatcher?page=%s&id=%d\">%s</a>", 
                            language.getText("Really do you want to remove attachment"), a.getFileName(), delPage, a.getId(), language.getText("Delete")));
                } else {
                    result.append("&nbsp;");
                }
                result.append("</td>");
            }
            result.append("</tr>");
        }
        result.append("</table>");
        result.append("<p>");
        
        return result.toString();
    }
    
    public static String renderAttachments(List<Attachment> list, HttpServletRequest request, String dwlPage) {
        StringBuilder result = new StringBuilder();
        
        if ((list == null) || list.isEmpty())
            return "";
        
        for (Attachment a: list) {
            String icon = "gfx/Files_unknown.gif";
            String extension = a.getFileName().toLowerCase().substring(a.getFileName().lastIndexOf(".") + 1);
            File f = new File(request.getServletContext().getRealPath("/gfx") + "/Files_" + extension + ".gif");
            if (f.exists()) {
                icon = "gfx/Files_" + extension + ".gif";
            }
            result.append(String.format("<img src=\"%s\" border=\"0\" alt=\"%s\">&nbsp;", icon, a.getFileName()));
            result.append(String.format("<a href=\"Upload?page=%s&id=%d\">%s</a>&nbsp; ", dwlPage, a.getId(), a.getFileName()));
        }
        
        return result.toString();
    }
}
