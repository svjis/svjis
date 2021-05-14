<%-- 
    Document   : Units__menu
    Created on : 6.7.2011, 13:12:19
    Author     : berk
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@page import="cz.svjis.servlet.Cmd"%>

<jsp:useBean id="language" scope="session" class="cz.svjis.bean.Language" />

<%
    String p = request.getParameter("page");
    if (p == null) {
        p = Cmd.MY_BUILDING_UNITS;
    }
%>

<!-- Aside -->
        <div id="aside">

            <div id="aside-top"></div>
            
            <!-- Categories -->
            <div class="padding">
                <h4 class="nom"><%=language.getText("Units") %>:</h4>
            </div> <!-- /padding -->
            
            <ul class="nav">
                <li <%=(p.startsWith("myBuildingUnitList") ? "id=\"nav-active\"" : "") %>><a href="Dispatcher?page=<%=Cmd.MY_BUILDING_UNITS %>"><%=language.getText("Building unit list") %></a></li>
            </ul>
            
            <div class="padding">
            <jsp:include page="_menu_login.jsp" />
            </div> <!-- /padding -->
            
        <hr class="noscreen" />          
        </div> <!-- /aside -->
        
        <div id="aside-bottom"></div>

