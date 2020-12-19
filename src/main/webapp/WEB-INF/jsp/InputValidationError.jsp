<%-- 
    Document   : InputValidationError
    Created on : 7.12.2019, 17:00:47
    Author     : jarberan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="message" scope="request" class="java.lang.String" />
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta http-equiv="refresh" content="5;url=Dispatcher">
        <title>Input validation error.</title>
    </head>

    <body>
        <h1>Input validation error.</h1>
        <p>
            <strong><%=message %></strong>
        </p>
        <p>
            You can continue <a href="Dispatcher">here</a>.
        </p>
    </body>
</html>