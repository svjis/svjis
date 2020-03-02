<%-- 
    Document   : _refresh
    Created on : 22.5.2011, 22:59:22
    Author     : berk
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="url" class="java.lang.String" scope="request"/>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html lang="en">
    <head>
        <title>Refresh</title>
        <%= "<meta http-equiv=\"refresh\" content=\"0;url=" + url + "\">" %>
    </head>
    <body>
    </body>
</html>