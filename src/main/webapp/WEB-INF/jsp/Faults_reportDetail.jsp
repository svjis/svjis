<%-- 
    Document   : Faults_reportDetail
    Created on : 20.12.2019, 17:49:33
    Author     : jarberan
--%>

<%@page import="cz.svjis.bean.Permission"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@page import="java.io.File"%>
<%@page import="cz.svjis.bean.FaultReportAttachment"%>
<%@page import="cz.svjis.bean.FaultReportComment"%>
<%@page import="cz.svjis.common.HttpUtils"%>
<%@page import="cz.svjis.bean.Language"%>
<%@page import="java.text.SimpleDateFormat"%>

<jsp:useBean id="language" scope="session" class="cz.svjis.bean.Language" />
<jsp:useBean id="user" scope="session" class="cz.svjis.bean.User" />
<jsp:useBean id="report" scope="request" class="cz.svjis.bean.FaultReport" />
<jsp:useBean id="watching" scope="request" class="java.lang.String" />

<jsp:include page="_header.jsp" />
<jsp:include page="_tray.jsp" />

<%
    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
    String stl = (report.isClosed()) ? "text-decoration: line-through;" : "";
%>
    <!-- Columns -->
    <div id="cols" class="box">

        <!-- Content -->
        <div id="content">
            <div id="content-main">
                <div id="content-main-in">
                    <h1 class="page-title" style="<%=stl %>" id="tbl-desc">#<%=report.getId() %>&nbsp;-&nbsp;<%=HttpUtils.highlight(report.getSubject(), request.getParameter("search")) %></h1>

                    <% if (user.hasPermission(Permission.FAULT_REPORTING_RESOLVER)) { %>
                    [<a href="Dispatcher?page=faultReportingEdit&id=<%=report.getId() %>"><%=language.getText("Edit") %></a>]&nbsp;
                        <% if (report.getAssignedToUser() == null) { %>
                        [<a href="Dispatcher?page=faultReportingFast&id=<%=report.getId() %>&takeTicket=1"><%=language.getText("Take this ticket") %></a>]&nbsp;
                        <% } %>
                        <% if ((report.getAssignedToUser() != null) && (report.getAssignedToUser().getId() == user.getId()) && !report.isClosed()) { %>
                        [<a href="Dispatcher?page=faultReportingFast&id=<%=report.getId() %>&closeTicket=1"><%=language.getText("Close this ticket") %></a>]&nbsp;
                        <% } %>
                    <% } %>

                    <% if (watching.equals("0")) { %>
                        [<a href="Dispatcher?page=faultReportingFast&id=<%=report.getId() %>&watch=1"><%=language.getText("Start watching") %></a>]&nbsp;
                    <% } else { %>
                        [<a href="Dispatcher?page=faultReportingFast&id=<%=report.getId() %>&watch=0"><%=language.getText("Stop watching") %></a>]&nbsp;
                    <% } %>

                    <table class="list" width="95%" aria-describedby="tbl-desc">
                        <tr>
                            <th class="list" width="25%" scope="row"><%=language.getText("Date") %></th>
                            <td class="list" width="75%"><%=sdf.format(report.getCreationDate()) %></td>
                        </tr>
                        <tr>
                            <th class="list" scope="row"><%=language.getText("Reporter") %></th>
                            <td class="list"><%=(report.getCreatedByUser() != null) ? (report.getCreatedByUser().getFullName(false)) : "&nbsp;" %></td>
                        </tr>
                        <tr>
                            <th class="list" scope="row"><%=language.getText("Resolver") %></th>
                            <td class="list"><%=(report.getAssignedToUser() != null) ? (report.getAssignedToUser().getFullName(false)) : "&nbsp;" %></td>
                        </tr>
                        <tr>
                            <th class="list" scope="row"><%=language.getText("Closed") %></th>
                            <td class="list"><%=(report.isClosed()) ? language.getText("yes") : language.getText("no") %></td>
                        </tr>
                        <tr>
                            <th class="list" scope="row"><%=language.getText("Building entrance") %></th>
                            <td class="list"><%=(report.getBuildingEntrance() != null) ? report.getBuildingEntrance().getDescription() + "&nbsp;(" + report.getBuildingEntrance().getAddress() + ")"  : "&nbsp;" %></td>
                        </tr>
                        <tr>
                            <th class="list" scope="row"><%=language.getText("Description") %></th>
                            <td class="list"><%=HttpUtils.highlight(HttpUtils.makeHyperlinks(report.getDescription()), request.getParameter("search")).replaceAll("\n", "<br>") %></td>
                        </tr>
                    </table>
                        
                    <% if (report.getId() != 0) { %>
                    <form action="Dispatcher?page=faultReportingAttachmentSave&reportId=<%=report.getId() %>" enctype="multipart/form-data" method="post">
                        <fieldset>
                            <legend id="tbl2-desc"><%=language.getText("Attachments") %></legend>
                            <%
                                if ((report.getAttachmentList() != null) && (report.getAttachmentList().size() != 0)) {
                            %>
                            <p>
                            <table class="list" aria-describedby="tbl2-desc">
                                <tr>
                                    <th class="list" scope="col">&nbsp;</th>
                                    <th class="list" scope="col"><%=language.getText("File") %></th>
                                    <th class="list" scope="col"><%=language.getText("User") %></th>
                                    <th class="list" scope="col"><%=language.getText("Time") %></th>
                                    <th class="list" scope="col">&nbsp;</th>
                                </tr>
                                <%
                                for (FaultReportAttachment a: report.getAttachmentList()) {
                                    String icon = "gfx/Files_unknown.gif";
                                    String extension = a.getFileName().toLowerCase().substring(a.getFileName().lastIndexOf(".") + 1);
                                    File f = new File(request.getServletContext().getRealPath("/gfx") + "/Files_" + extension + ".gif");
                                    if (f.exists()) {
                                        icon = "gfx/Files_" + extension + ".gif";
                                    }
                                %>
                                <tr>
                                    <td class="list"><img src="<%=icon%>" border="0" alt="<%=a.getFileName() %>"></td>
                                    <td class="list"><a href="Upload?page=faultReportingDownload&id=<%=a.getId() %>"><%=a.getFileName() %></a></td>
                                    <td class="list"><%=a.getUser().getFullName(false) %></td>
                                    <td class="list"><%=sdf.format(a.getUploadTime()) %></td>
                                    <td class="list">
                                        <% if ((!report.isClosed()) && (user.getId() == a.getUser().getId())) { %>
                                            <a onclick="if (!confirm('<%=language.getText("Really do you want to remove attachment") %> <%=a.getFileName() %> ?')) return false;" href="Dispatcher?page=faultReportingAttachmentDelete&id=<%=a.getId() %>"><%=language.getText("Delete") %></a>
                                        <% } else { %>
                                            &nbsp;
                                        <% } %>
                                    </td>
                                </tr>
                                <%
                                }
                                %>
                            </table>
                            <p>
                            <%
                               }
                            %>
                            <% if (!report.isClosed()) { %>
                            <p>
                                <input type="file" name="attachment" size="40">
                                <input type="submit" id="file-submit" value="<%=language.getText("Insert attachment") %>">
                            </p>
                            <% } %>
                        </fieldset>
                    </form>
                    <% } %>
                        
                    <%
                        if ((report.getFaultReportCommentList() != null) && (report.getFaultReportCommentList().size() != 0)) {
                    %>
                        <h2 class="article-title"><%=language.getText("Comments:") %></h2>

                        <%
                            SimpleDateFormat sdft = new SimpleDateFormat("dd.MM.yyyy HH:mm");
                            for (FaultReportComment c : report.getFaultReportCommentList()) {
                        %>
                            <div class="article box">
                            <strong><%=c.getUser().getFullName(false) %> <%=sdft.format(c.getInsertionTime()) %></strong><br>
                            <%=HttpUtils.makeHyperlinks(c.getBody().replace("\n", "<br>")) %>
                            </div>
                        <%
                            }
                        %>
                    <%
                       }
                    %>
                      
                    
                    <%
                        if (user.hasPermission(Permission.FAULT_REPORTING_COMMENT) && (!report.isClosed())) {
                    %>
                        <form action="Dispatcher" method="post">
                             <input type="hidden" name="page" value="faultInsertComment">
                             <input type="hidden" name="id" value="<%=report.getId() %>">
                             <p>
                                 <textarea
                                    id="body"
                                    name="body"
                                    rows=5 cols=50
                                    wrap
                                 ></textarea>
                             </p>
                             <p>
                                 <input class="my-button" id="submit" type="submit" value="<%=language.getText("Insert comment") %>" name="submit">
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
