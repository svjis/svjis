<%-- 
    Document   : Faults_reportDetail
    Created on : 20.12.2019, 17:49:33
    Author     : jarberan
--%>

<%@page import="cz.svjis.bean.FaultReportComment"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="cz.svjis.bean.Language"%>
<%@page import="java.text.SimpleDateFormat"%>
<jsp:useBean id="language" scope="session" class="cz.svjis.bean.Language" />
<jsp:useBean id="user" scope="session" class="cz.svjis.bean.User" />
<jsp:useBean id="report" scope="request" class="cz.svjis.bean.FaultReport" />

<jsp:include page="_header.jsp" />
<jsp:include page="_tray.jsp" />

<%
    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
%>
    <!-- Columns -->
    <div id="cols" class="box">

        <!-- Content -->
        <div id="content">
            <div id="content-main">
                <div id="content-main-in">
                    <h1 class="page-title">#<%=report.getId() %>&nbsp;-&nbsp;<%=report.getSubject() %></h1>
                    <% if (user.hasPermission("fault_reporting_resolver")) { %>
                    [<a href="Dispatcher?page=faultReportingEdit&id=<%=report.getId() %>"><%=language.getText("Edit") %></a>]&nbsp;
                        <% if (report.getAssignedToUser() == null) { %>
                        [<a href="Dispatcher?page=faultReportingFast&id=<%=report.getId() %>&takeTicket=1"><%=language.getText("Take this ticket") %></a>]&nbsp;
                        <% } %>
                        <% if ((report.getAssignedToUser() != null) && (report.getAssignedToUser().getId() == user.getId()) && !report.isClosed()) { %>
                        [<a href="Dispatcher?page=faultReportingFast&id=<%=report.getId() %>&closeTicket=1"><%=language.getText("Close this ticket") %></a>]&nbsp;
                        <% } %>
                        <br>
                    <% } %>
                    <table class="list" width="90%">
                        <tr>
                            <th class="list" width="25%"><%=language.getText("Date") %></th>
                            <td class="list" width="75%"><%=sdf.format(report.getCreationDate()) %></td>
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
                        
                    <%
                        if ((report.getFaultReportCommentList() != null) && (report.getFaultReportCommentList().size() != 0)) {
                    %>
                        <h2 class="article-title"><%=language.getText("Comments:") %></h2>

                        <%
                            SimpleDateFormat sdft = new SimpleDateFormat("dd.MM.yyyy HH:mm");
                            for (FaultReportComment c : report.getFaultReportCommentList()) {
                        %>
                            <div class="article box">
                            <strong><%=c.getUser().getFirstName() %>&nbsp;<%=c.getUser().getLastName() %> <%=sdft.format(c.getInsertionTime()) %></strong><br>
                            <%=c.getBody().replace("\n", "<br>") %>
                            </div>
                        <%
                            }
                        %>
                    <%
                       }
                    %>
                      
                    
                    <%
                        if (user.hasPermission("fault_reporting_comment") && (!report.isClosed())) {
                    %>
                        <form action="Dispatcher" method="post">
                             <input type="hidden" name="page" value="faultInsertComment">
                             <input type="hidden" name="id" value="<%=report.getId() %>">
                             <p>
                                 <textarea
                                 name="body"
                                 rows=5 cols=50
                                 wrap
                                 ></textarea>
                             </p>
                             <p>
                                 <input id="comment-submit" type="submit" value="<%=language.getText("Insert comment") %>" name="submit">
                             </p>
                         </form>
                    <%
                       }
                    %>
                </div> <!-- /content-main-in -->
            </div> <!-- /content-main -->
            <hr class="noscreen" />
        </div> <!-- /content -->

        <jsp:include page="Faults__menu.jsp" />
    
    </div> <!-- /cols -->

<jsp:include page="_footer.jsp" />
