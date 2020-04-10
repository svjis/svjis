<%-- 
    Document   : _menu
    Created on : 15.6.2011, 17:14:58
    Author     : berk
--%>

<%@page import="java.util.List"%>
<%@page import="java.util.Iterator"%>
<%@page import="cz.svjis.bean.MenuItem"%>
<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="cz.svjis.bean.User" />
<jsp:useBean id="language" scope="session" class="cz.svjis.bean.Language" />
<jsp:useBean id="menu" scope="request" class="cz.svjis.bean.Menu" />

<%!
    private String writeSubMenu(List<MenuItem> menu, int activeSection, boolean isTopLevel) {
        String output = "";
        if (!isTopLevel) {
            output += "<ul>";
        }

        for (MenuItem ami: menu) {
            String active = "";
            if ((isTopLevel) && ((activeSection == ami.getSection().getId()) || (ami.getSubSections() != null))) {
                active = " id=\"nav-active\"";
            }
            output = output + "<li" + active + "><a href=\"Dispatcher?page=articleList&section=" + ami.getSection().getId() + "\">" + ami.getSection().getDescription() + "</a>";
            if (ami.getSubSections() != null)
                output = output + writeSubMenu(ami.getSubSections(), activeSection, false);
            output = output + "</li>" + "\n";
        }

        if (!isTopLevel) {
            output += "</ul>";
        }
        return output;
    }
%>

<!-- Aside -->
        <div id="aside">

            <div id="aside-top"></div>
            
            <!-- Categories -->
            <div class="padding">
                <h4 class="nom"><%=language.getText("Articles") %>:</h4>
            </div> <!-- /padding -->
            
            <ul class="nav">
                <%
                    int iSection = (request.getParameter("section") != null) ? Integer.valueOf(request.getParameter("section")) : 0;
                    String active = "";
                    if (iSection == 0) {
                        active = " id=\"nav-active\"";
                    }
                %>
                <li <%=active %>><a href="Dispatcher?page=articleList"><%=language.getText("All articles") %></a></li>
                <%=writeSubMenu(menu.getMenu(), iSection, true) %>
            </ul>
                
            <div class="padding">
            <jsp:include page="_menu_search.jsp" />
            <jsp:include page="_menu_login.jsp" />
            </div> <!-- /padding -->
            
        <hr class="noscreen" />          
        </div> <!-- /aside -->
        
        <div id="aside-bottom"></div>
