<%-- 
    Document   : Faults__menu
    Created on : 20.12.2019, 10:34:57
    Author     : jarberan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@page import="cz.svjis.bean.Permission"%>

<jsp:useBean id="language" scope="session" class="cz.svjis.bean.Language" />
<jsp:useBean id="user" scope="session" class="cz.svjis.bean.User" />
<jsp:useBean id="counters" scope="request" class="cz.svjis.bean.FaultReportMenuCounters" />

<%
    String p = request.getParameter("page");
    if (p == null) {
        p = "faultReportingList";
    }
%>

<!-- Aside -->
        <div id="aside">

            <div id="aside-top"></div>
            
            <!-- Categories -->
            <div class="padding">
                <h4 class="nom"><%=language.getText("Fault reporting") %>:</h4>
            </div> <!-- /padding -->
            
            <ul class="nav">
                <li <%=(p.equals("faultReportingList") ? "id=\"nav-active\"" : "") %>><a href="Dispatcher?page=faultReportingList"><%=language.getText("All open") %>&nbsp;(<%=counters.getAllOpenCnt() %>)</a></li>
                <% if (user.hasPermission(Permission.FAULT_REPORTING_REPORTER)) { %>
                <li <%=(p.equals("faultReportingListCreatedByMe") ? "id=\"nav-active\"" : "") %>><a href="Dispatcher?page=faultReportingListCreatedByMe"><%=language.getText("Reported by me") %>&nbsp;(<%=counters.getAllCreatedByMeCnt() %>)</a></li>
                <% } %>
                <% if (user.hasPermission(Permission.FAULT_REPORTING_RESOLVER)) { %>
                <li <%=(p.equals("faultReportingListAssignedToMe") ? "id=\"nav-active\"" : "") %>><a href="Dispatcher?page=faultReportingListAssignedToMe"><%=language.getText("Assigned to me") %>&nbsp;(<%=counters.getAllAssignedToMeCnt() %>)</a></li>
                <% } %>
                <li <%=(p.equals("faultReportingListClosed") ? "id=\"nav-active\"" : "") %>><a href="Dispatcher?page=faultReportingListClosed"><%=language.getText("All closed") %>&nbsp;(<%=counters.getAllClosedCnt() %>)</a></li>
            </ul>
            
            <div class="padding">
            <jsp:include page="_menu_search_faults.jsp" />
            <jsp:include page="_menu_login.jsp" />
            </div> <!-- /padding -->
            
        <hr class="noscreen" />          
        </div> <!-- /aside -->
        
        <div id="aside-bottom"></div>

