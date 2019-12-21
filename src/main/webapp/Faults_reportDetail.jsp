<%-- 
    Document   : Faults_reportDetail
    Created on : 20.12.2019, 17:49:33
    Author     : jarberan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="cz.svjis.bean.Language"%>
<%@page import="java.text.SimpleDateFormat"%>
<jsp:useBean id="language" scope="session" class="cz.svjis.bean.Language" />
<jsp:useBean id="user" scope="session" class="cz.svjis.bean.User" />
<jsp:useBean id="report" scope="request" class="cz.svjis.bean.FaultReport" />

<jsp:include page="_header.jsp" />
<jsp:include page="_tray.jsp" />

<%
    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
%>
    <!-- Columns -->
    <div id="cols" class="box">

        <!-- Content -->
        <div id="content">
            <div id="content-main">
                <div id="content-main-in">
                    <h1 class="page-title"><%=language.getText("Fault reporting") %></h1>
                    <% if (user.hasPermission("fault_reporting_resolver")) { %>
                    [<a href="Dispatcher?page=faultReportingEdit&id=<%=report.getId() %>"><%=language.getText("Edit") %></a>]<br>
                    <% } %>
                    <table class="list">
                        <tr>
                            <th class="list"><%=language.getText("Ticket id") %></th>
                            <td class="list" style="text-align: right"><%=report.getId() %></td>
                        </tr>
                        <tr>
                            <th class="list"><%=language.getText("Date") %></th>
                            <td class="list"><%=sdf.format(report.getCreationDate()) %></td>
                        </tr>
                        <tr>
                            <th class="list"><%=language.getText("Subject") %></th>
                            <td class="list"><%=report.getSubject() %></td>
                        </tr>
                        <tr>
                            <th class="list"><%=language.getText("User") %></th>
                            <td class="list"><%=(report.getCreatedByUser() != null) ? (report.getCreatedByUser().getFirstName() + "&nbsp;" +report.getCreatedByUser().getLastName()) : "&nbsp;" %></td>
                        </tr>
                        <tr>
                            <th class="list"><%=language.getText("Resolver") %></th>
                            <td class="list"><%=(report.getAssignedToUser() != null) ? (report.getAssignedToUser().getFirstName() + "&nbsp;" +report.getAssignedToUser().getLastName()) : "&nbsp;" %></td>
                        </tr>
                        <tr>
                            <th class="list"><%=language.getText("Closed") %></th>
                            <td class="list"><%=(report.isClosed()) ? language.getText("yes") : language.getText("no") %></td>
                        </tr>
                        <tr>
                            <th class="list"><%=language.getText("Description") %></th>
                            <td class="list"><%=report.getDescription().replaceAll("\n", "<br>") %></td>
                        </tr>
                    </table>
                </div> <!-- /content-main-in -->
            </div> <!-- /content-main -->
            <hr class="noscreen" />
        </div> <!-- /content -->

        <jsp:include page="Faults__menu.jsp" />
    
    </div> <!-- /cols -->

<jsp:include page="_footer.jsp" />
