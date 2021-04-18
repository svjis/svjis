<%-- 
    Document   : Advert__menu
    Created on : 18.04.2021, 19:07:00
    Author     : jarberan
--%>

<%@page import="cz.svjis.bean.AdvertType"%>
<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="language" scope="session" class="cz.svjis.bean.Language" />
<jsp:useBean id="user" scope="session" class="cz.svjis.bean.User" />
<jsp:useBean id="advertTypeList" scope="request" class="java.util.ArrayList" />

<%
    String p = request.getParameter("section");
    if (p == null) {
        p = "0";
    }
%>

<!-- Aside -->
        <div id="aside">

            <div id="aside-top"></div>
            
            <!-- Categories -->
            <div class="padding">
                <h4 class="nom"><%=language.getText("Adverts") %>:</h4>
            </div> <!-- /padding -->
            
            <ul class="nav">
                <%
                for (AdvertType t: (ArrayList<AdvertType>) advertTypeList) {
                %>
                    <li <%=(p.equals(Integer.toString(t.getId())) ? "id=\"nav-active\"" : "") %>><a href="Dispatcher?page=advertList&section=<%=t.getId() %>"><%=language.getText(t.getDescription()) %>)</a></li>
                <%    
                }
                %>
            </ul>
            
            <div class="padding">
            <jsp:include page="_menu_login.jsp" />
            </div> <!-- /padding -->
            
        <hr class="noscreen" />          
        </div> <!-- /aside -->
        
        <div id="aside-bottom"></div>

