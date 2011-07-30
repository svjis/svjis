<%-- 
    Document   : Redaction__menu
    Created on : 21.6.2011, 23:25:12
    Author     : berk
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="cz.svjis.bean.User" />
<jsp:useBean id="language" scope="session" class="cz.svjis.bean.Language" />

<%
    String p = request.getParameter("page");
    if (p == null) {
        p = "redactionArticleList";
    }
%>

<!-- Aside -->
        <div id="aside">

            <div id="aside-top"></div>
            
            <!-- Categories -->
            <div class="padding">
                <h4 class="nom"><%=language.getText("Redaction") %>:</h4>
            </div> <!-- /padding -->
            
            <ul class="nav">
                <% if (user.hasPermission("redaction_articles") || user.hasPermission("redaction_articles_all")) { %>
                <li <%=(p.startsWith("redactionArticleList") ? "id=\"nav-active\"" : "") %>><a href="Dispatcher?page=redactionArticleList"><%=language.getText("Article list") %></a></li>
                <% } %>
                <% if (user.hasPermission("redaction_mini_news")) { %>
                <li <%=(p.startsWith("redactionNewsList") ? "id=\"nav-active\"" : "") %>><a href="Dispatcher?page=redactionNewsList"><%=language.getText("News list") %></a></li>
                <% } %>
                <% if (user.hasPermission("redaction_inquiry")) { %>
                <li <%=(p.startsWith("redactionInquiryList") ? "id=\"nav-active\"" : "") %>><a href="Dispatcher?page=redactionInquiryList"><%=language.getText("Inquiry list") %></a></li>
                <% } %>
                <% if (user.hasPermission("redaction_menu")) { %>
                <li <%=(p.startsWith("redactionArticleMenu") ? "id=\"nav-active\"" : "") %>><a href="Dispatcher?page=redactionArticleMenu"><%=language.getText("Article menu") %></a></li>
                <% } %>
            </ul>
            
            <jsp:include page="_menu_login.jsp" />
            
        <hr class="noscreen" />          
        </div> <!-- /aside -->
        
        <div id="aside-bottom"></div>

