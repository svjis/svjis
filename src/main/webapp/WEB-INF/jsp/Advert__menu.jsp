<%-- 
    Document   : Advert__menu
    Created on : 18.04.2021, 19:07:00
    Author     : jarberan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@page import="cz.svjis.bean.AdvertType"%>
<%@page import="cz.svjis.servlet.CmdFactory"%>
<%@page import="java.util.ArrayList"%>

<jsp:useBean id="language" scope="session" class="cz.svjis.bean.Language" />
<jsp:useBean id="user" scope="session" class="cz.svjis.bean.User" />
<jsp:useBean id="advertTypeList" scope="request" class="java.util.ArrayList" />

<%
    String typeId = request.getParameter("typeId");
    if (typeId == null) {
        typeId = "0";
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
                    <li <%=(typeId.equals(Integer.toString(t.getId())) ? "id=\"nav-active\"" : "") %>><a href="Dispatcher?page=<%=CmdFactory.ADVERT_LIST %>&typeId=<%=t.getId() %>"><%=language.getText(t.getDescription()) %>&nbsp;(<%=t.getCnt() %>)</a></li>
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

