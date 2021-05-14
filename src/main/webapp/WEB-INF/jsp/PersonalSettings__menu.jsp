<%-- 
    Document   : PersonalSettings__menu
    Created on : 25.6.2011, 14:59:38
    Author     : berk
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@page import="cz.svjis.servlet.Cmd"%>

<jsp:useBean id="language" scope="session" class="cz.svjis.bean.Language" />
<jsp:useBean id="currCompany" scope="request" class="cz.svjis.bean.Company" />

<%
    String p = request.getParameter("page");
    if (p == null) {
        p = Cmd.PERSONAL_DETAIL;
    }
%>

<!-- Aside -->
        <div id="aside">

            <div id="aside-top"></div>
            
            <!-- Categories -->
            <div class="padding">
                <h4 class="nom"><%=language.getText("Personal settings") %>:</h4>
            </div> <!-- /padding -->
            
            <ul class="nav">
                <li <%=(p.startsWith("psUserDetail") ? "id=\"nav-active\"" : "") %>><a href="Dispatcher?page=<%=Cmd.PERSONAL_DETAIL %>"><%=language.getText("Personal settings") %></a></li>
                <li <%=(p.startsWith("psPasswordChange") ? "id=\"nav-active\"" : "") %>><a href="Dispatcher?page=<%=Cmd.PERSONAL_PW_CHANGE %>"><%=language.getText("Password change") %></a></li>
            </ul>
            
            <div class="padding">
            <jsp:include page="_menu_login.jsp" />
            </div> <!-- /padding -->
            
        <hr class="noscreen" />          
        </div> <!-- /aside -->
        
        <div id="aside-bottom"></div>
