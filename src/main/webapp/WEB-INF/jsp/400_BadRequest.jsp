<%-- 
    Document   : 400_BadRequest
    Created on : 3.5.2021, 23:50:47
    Author     : jarberan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="message" scope="request" class="java.lang.String" />
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta http-equiv="refresh" content="5;url=Dispatcher">
        <title>400 Bad Request</title>
    </head>

    <body>
        <h1>400 Bad Request</h1>
        <p>
            <strong><%=message %></strong>
        </p>
        <p>
            You can continue <a href="Dispatcher">here</a>.
        </p>
    </body>
</html>