<%-- 
    Document   : Administration__menu
    Created on : 18.6.2011, 23:26:39
    Author     : berk
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="cz.svjis.servlet.Cmd"%>
<jsp:useBean id="language" scope="session" class="cz.svjis.bean.Language" />
<jsp:useBean id="currCompany" scope="request" class="cz.svjis.bean.Company" />

<%
    String p = request.getParameter("page");
    if (p == null) {
        p = Cmd.COMPANY_DETAIL;
    }
%>

<!-- Aside -->
        <div id="aside">

            <div id="aside-top"></div>
            
            <!-- Categories -->
            <div class="padding">
                <h4 class="nom"><%=language.getText("Administration") %>:</h4>
            </div> <!-- /padding -->
            
            <ul class="nav">
                <li <%=(p.startsWith("companyDetail") ? "id=\"nav-active\"" : "") %>><a href="Dispatcher?page=<%=Cmd.COMPANY_DETAIL %>"><%=language.getText("Company") %></a></li>
                <li <%=(p.startsWith("buildingDetail") ? "id=\"nav-active\"" : "") %>><a href="Dispatcher?page=<%=Cmd.BUILDING_DETAIL %>"><%=language.getText("Building") %></a></li>
                <li <%=(p.startsWith("buildingEntranceList") ? "id=\"nav-active\"" : "") %>><a href="Dispatcher?page=<%=Cmd.BUILDING_ENT_LIST %>"><%=language.getText("Building entrance list") %> (<%=currCompany.getEntranceCnt() %>)</a></li>
                <li <%=(p.startsWith("buildingUnitList") ? "id=\"nav-active\"" : "") %>><a href="Dispatcher?page=<%=Cmd.BUILDING_UNIT_LIST %>"><%=language.getText("Building unit list") %> (<%=currCompany.getUnitCnt() %>)</a></li>
                <li <%=(p.startsWith("boardMemberList") ? "id=\"nav-active\"" : "") %>><a href="Dispatcher?page=<%=Cmd.BOARD_LIST %>"><%=language.getText("Board") %> (<%=currCompany.getBoardCnt() %>)</a></li>
                <li <%=(p.startsWith("userList") ? "id=\"nav-active\"" : "") %>><a href="Dispatcher?page=<%=Cmd.USER_LIST %>"><%=language.getText("User list") %> (<%=currCompany.getUserCnt() %>)</a></li>
                <li <%=(p.startsWith("roleList") ? "id=\"nav-active\"" : "") %>><a href="Dispatcher?page=<%=Cmd.ROLE_LIST %>"><%=language.getText("Role list") %> (<%=currCompany.getRoleCnt() %>)</a></li>
                <li <%=(p.startsWith("propertyList") ? "id=\"nav-active\"" : "") %>><a href="Dispatcher?page=<%=Cmd.PROPERTY_LIST %>"><%=language.getText("Properties") %></a></li>
                <li <%=(p.startsWith("messagesPending") ? "id=\"nav-active\"" : "") %>><a href="Dispatcher?page=<%=Cmd.MESSAGES %>"><%=language.getText("Messages pending") %> (<%=currCompany.getMessageCnt() %>)</a></li>
            </ul>
            
            <div class="padding">
            <jsp:include page="_menu_login.jsp" />
            </div> <!-- /padding -->
            
        <hr class="noscreen" />          
        </div> <!-- /aside -->
        
        <div id="aside-bottom"></div>

