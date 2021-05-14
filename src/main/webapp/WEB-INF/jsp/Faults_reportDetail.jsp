<%-- 
    Document   : Faults_reportDetail
    Created on : 20.12.2019, 17:49:33
    Author     : jarberan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@page import="cz.svjis.bean.Permission"%>
<%@page import="cz.svjis.bean.Attachment"%>
<%@page import="cz.svjis.bean.Language"%>
<%@page import="cz.svjis.common.JspSnippets"%>
<%@page import="cz.svjis.servlet.Cmd"%>

<jsp:useBean id="language" scope="session" class="cz.svjis.bean.Language" />
<jsp:useBean id="user" scope="session" class="cz.svjis.bean.User" />
<jsp:useBean id="report" scope="request" class="cz.svjis.bean.FaultReport" />
<jsp:useBean id="watching" scope="request" class="java.lang.String" />

<jsp:include page="_header.jsp" />
<jsp:include page="_tray.jsp" />

<%
    String stl = (report.isClosed()) ? "text-decoration: line-through;" : "";
%>
    <!-- Columns -->
    <div id="cols" class="box">

        <!-- Content -->
        <div id="content">
            <div id="content-main">
                <div id="content-main-in">
                    <h1 class="page-title" style="<%=stl %>" id="tbl-desc">#<%=report.getId() %>&nbsp;-&nbsp;<%=JspSnippets.highlight(report.getSubject(), request.getParameter("search")) %></h1>

                    <% if (user.hasPermission(Permission.FAULT_REPORTING_RESOLVER)) { %>
                    [<a href="Dispatcher?page=<%=Cmd.FAULT_EDIT %>&id=<%=report.getId() %>"><%=language.getText("Edit") %></a>]&nbsp;
                        <% if (report.getAssignedToUser() == null) { %>
                        [<a href="Dispatcher?page=<%=Cmd.FAULT_FAST_OP %>&id=<%=report.getId() %>&takeTicket=1"><%=language.getText("Take this ticket") %></a>]&nbsp;
                        <% } %>
                        <% if ((report.getAssignedToUser() != null) && (report.getAssignedToUser().getId() == user.getId()) && !report.isClosed()) { %>
                        [<a href="Dispatcher?page=<%=Cmd.FAULT_FAST_OP %>&id=<%=report.getId() %>&closeTicket=1"><%=language.getText("Close this ticket") %></a>]&nbsp;
                        <% } %>
                    <% } %>

                    <% if (watching.equals("0")) { %>
                        [<a href="Dispatcher?page=<%=Cmd.FAULT_FAST_OP %>&id=<%=report.getId() %>&watch=1"><%=language.getText("Start watching") %></a>]&nbsp;
                    <% } else { %>
                        [<a href="Dispatcher?page=<%=Cmd.FAULT_FAST_OP %>&id=<%=report.getId() %>&watch=0"><%=language.getText("Stop watching") %></a>]&nbsp;
                    <% } %>

                    <table class="list" width="95%" aria-describedby="tbl-desc">
                        <tr>
                            <th class="list" width="25%" scope="row"><%=language.getText("Date") %></th>
                            <td class="list" width="75%"><%=JspSnippets.renderDateTime(report.getCreationDate()) %></td>
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
                            <td class="list"><%=JspSnippets.highlight(JspSnippets.makeHyperlinks(report.getDescription()), request.getParameter("search")).replaceAll("\n", "<br>") %></td>
                        </tr>
                    </table>
                        
                    <% if (report.getId() != 0) { %>
                    <form action="Dispatcher?page=<%=Cmd.FAULT_ATT_SAVE %>&reportId=<%=report.getId() %>" enctype="multipart/form-data" method="post">
                        <fieldset>
                            <legend id="tbl2-desc"><%=language.getText("Attachments") %></legend>
                            <%=JspSnippets.renderAttachments(report.getAttachmentList(), request, "File", "faultReportingDownload", "faultReportingAttachmentDelete", true, true, false, !report.isClosed(), "tbl2-desc") %>
                            
                            <% if (!report.isClosed()) { %>
                            <p>
                                <input id="file-upload" type="file" name="attachment" size="40">
                                <input id="file-submit" type="submit" value="<%=language.getText("Insert attachment") %>">
                            </p>
                            <% } %>
                        </fieldset>
                    </form>
                    <% } %>
                        
                    <%=JspSnippets.renderComments(report.getFaultReportCommentList(), request) %>
                      
                    <% if (user.hasPermission(Permission.FAULT_REPORTING_COMMENT) && (!report.isClosed())) { %>
                        <form action="Dispatcher" method="post">
                             <input type="hidden" name="page" value="<%=Cmd.FAULT_COMMENT %>">
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
                    <% } %>

                </div> <!-- /content-main-in -->
            </div> <!-- /content-main -->
            <hr class="noscreen" />
        </div> <!-- /content -->

        <jsp:include page="Faults__menu.jsp" />
    
    </div> <!-- /cols -->

<jsp:include page="_footer.jsp" />
