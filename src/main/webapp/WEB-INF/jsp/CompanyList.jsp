<%-- 
    Document   : CompanyList
    Created on : 28.4.2011, 14:54:06
    Author     : berk
--%>


<%@page import="java.util.List"%>
<%@page import="cz.svjis.bean.Company"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="companyList" scope="request" class="java.util.ArrayList" />

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link type="text/css" rel="stylesheet" href="css/style-for-list.css" />
        <title>Company list</title>
    </head>

    <body>
        <h1>List of SVJ</h1>
        <%
        String letter = "";
        for (Company c: (List<Company>) companyList) {

            if (!letter.equals(c.getName().substring(0, 1))) {
                letter = c.getName().substring(0,1);
        %>
                <h2><%=letter %></h2>
        <%
            }
        %>
                <a href="Dispatcher?setcompany=<%=c.getId() %>"><%=c.getName() %>, <%=c.getAddress() %></a><br>
        <%
        }
        %>
    </body>
</html>



