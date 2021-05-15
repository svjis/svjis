<%-- 
    Document   : Advert__menu
    Created on : 18.04.2021, 19:07:00
    Author     : jarberan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@page import="cz.svjis.bean.AdvertType"%>
<%@page import="cz.svjis.servlet.Cmd"%>
<%@page import="java.util.ArrayList"%>

<jsp:useBean id="language" scope="session" class="cz.svjis.bean.Language" />
<jsp:useBean id="user" scope="session" class="cz.svjis.bean.User" />
<jsp:useBean id="advertMenuList" scope="request" class="java.util.ArrayList" />
<jsp:useBean id="currMenu" scope="request" class="cz.svjis.bean.AdvertType" />


<!-- Aside -->
        <div id="aside">

            <div id="aside-top"></div>
            
            <!-- Categories -->
            <div class="padding">
                <h4 class="nom"><%=language.getText("Adverts") %>:</h4>
            </div> <!-- /padding -->
            
            <ul class="nav">
                <%
                for (AdvertType t: (ArrayList<AdvertType>) advertMenuList) {
                %>
                    <li <%=(currMenu.getId() == t.getId() ? "id=\"nav-active\"" : "") %>><a href="Dispatcher?page=<%=Cmd.ADVERT_LIST %>&typeId=<%=t.getId() %>"><%=t.getDescription() %>&nbsp;(<%=t.getCnt() %>)</a></li>
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

