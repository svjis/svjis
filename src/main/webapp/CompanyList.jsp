<%-- 
    Document   : CompanyList
    Created on : 28.4.2011, 14:54:06
    Author     : berk
--%>


<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="companyList" scope="request" class="java.util.ArrayList" />

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Company list</title>
    </head>

    <body>
        <h1>SVJ list</h1>
        <%
        String letter = "";
        java.util.Iterator<cz.svjis.bean.Company> compI = companyList.iterator();
        while (compI.hasNext()) {
            cz.svjis.bean.Company c = compI.next();

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



