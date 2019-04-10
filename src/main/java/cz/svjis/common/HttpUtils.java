/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.svjis.common;

import java.io.IOException;
import java.net.URLEncoder;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.codec.binary.Base64;

/**
 *
 * @author jaroslav_b
 */
public class HttpUtils {

    public static void writeBinaryData(String contentType, String fileName, byte[] data, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String userAgent = request.getHeader("User-Agent");
        String encodedFileName = null;
        if (userAgent.contains("MSIE") || userAgent.contains("Opera") || userAgent.contains("Trident")) {
            encodedFileName = URLEncoder.encode(fileName.replace(" ", "_"), "UTF-8");
        } else {
            encodedFileName = "=?UTF-8?B?" + Base64.encodeBase64String(fileName.replace(" ", "_").getBytes("UTF-8")) + "?=";
        }

        response.setContentType(contentType);
        response.setHeader("Content-Disposition", "attachment; filename=\"" + encodedFileName + "\"");
        //response.setContentLength(data.length);
        response.setDateHeader("Expires", 0);

        java.io.OutputStream outb = null;
        try {
            outb = response.getOutputStream();
            outb.write(data, 0, data.length);
        } catch (java.io.IOException ex) {
            //ClientAbortException:  java.io.IOException: Roura přerušena (SIGPIPE) 
        } finally {
            if (outb != null) {
                outb.close();
            }
        }
    }
}
