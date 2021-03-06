<%-- 
    Document   : Faults_reportList
    Created on : 20.12.2019, 10:34:31
    Author     : jarberan
--%>

<%@page import="cz.svjis.bean.Permission"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.ArrayList"%>
<%@page import="cz.svjis.bean.FaultReport"%>
<%@page import="cz.svjis.bean.SliderItem"%>
<%@page import="cz.svjis.common.HttpUtils"%>

<jsp:useBean id="language" scope="session" class="cz.svjis.bean.Language" />
<jsp:useBean id="user" scope="session" class="cz.svjis.bean.User" />
<jsp:useBean id="reportList" scope="request" class="java.util.ArrayList" />
<jsp:useBean id="slider" scope="request" class="cz.svjis.bean.SliderImpl" />
<jsp:useBean id="searchKey" scope="request" class="java.lang.String" />

<jsp:include page="_header.jsp" />
<jsp:include page="_tray.jsp" />

    <!-- Columns -->
    <div id="cols" class="box">

        <!-- Content -->
        <div id="content">
            <div id="content-main">
                <div id="content-main-in">
                    <h1 class="page-title" id="tbl-desc"><%=language.getText("Fault reporting") %></h1>
                    <% if (user.hasPermission(Permission.FAULT_REPORTING_REPORTER)) { %>
                    <a href="Dispatcher?page=faultReportingEdit&id=0" class="create-button"><%=language.getText("Report new fault") %></a>
                    <% } %>
                    <table width="100%" class="list" aria-describedby="tbl-desc">
                        <tr>
                            <th class="list" scope="col">&nbsp;</th>
                            <th class="list" scope="col"><%=language.getText("Ticket id") %></th>
                            <th class="list" scope="col"><%=language.getText("Date") %></th>
                            <th class="list" scope="col"><%=language.getText("Subject") %></th>
                            <th class="list" scope="col"><%=language.getText("Building entrance") %></th>
                            <th class="list" scope="col"><%=language.getText("Reporter") %></th>
                            <th class="list" scope="col"><%=language.getText("Resolver") %></th>
                        </tr>
                        <%
                        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
                        for (FaultReport f : (ArrayList<FaultReport>) reportList) {
                            String stl = (f.isClosed()) ? "background-color:#d0d0d0;text-decoration: line-through;" : "";
                        %>
                        <tr>
                            <td class="list" style="<%=stl %>"><a href="Dispatcher?page=faultDetail&id=<%=f.getId() %><%=(request.getParameter("search") != null) ? "&search=" + URLEncoder.encode(request.getParameter("search"), "UTF-8") : "" %>"><img src="gfx/find.png" border="0" title="View" alt="View"></a></td>
                            <td class="list" style="<%=stl %>text-align: right;"><%=f.getId() %></td>
                            <td class="list" style="<%=stl %>"><%=sdf.format(f.getCreationDate()) %></td>
                            <td class="list" style="<%=stl %>"><%=HttpUtils.highlight(f.getSubject(), request.getParameter("search")) %></td>
                            <td class="list" style="<%=stl %>"><%=(f.getBuildingEntrance() != null) ? f.getBuildingEntrance().getDescription() : "&nbsp;" %></td>
                            <td class="list" style="<%=stl %>"><%=f.getCreatedByUser().getFullName(false) %></td>
                            <td class="list" style="<%=stl %>"><%=(f.getAssignedToUser() != null) ? (f.getAssignedToUser().getFullName(false)) : "&nbsp;" %></td>
                        </tr>
                        <%
                            }
                        %>
                    </table>
                    
                    <p class="t-left">
                        <% if (slider.getTotalNumOfPages() > 1) { %>
                        <strong><%=language.getText("Pages:") %></strong>&nbsp;
                        <%
                        String search = "";
                        String pageId = "page=" + slider.getPageId() + "&";
                        if ((searchKey != null) && (!searchKey.equals(""))) {
                            search = "search=" + URLEncoder.encode(searchKey, "UTF-8") + "&";
                        }
                        for (SliderItem item : slider.getItemList()) {
                            if (item.isCurrent()) {
                                out.println("<b>" + item.getLabel() + "</b>&nbsp;");
                            } else {
                                out.println("<a href=\"Dispatcher?" + pageId + search + "pageNo=" + item.getPage() + "\">" + item.getLabel() + "</a>&nbsp;");
                            }
                        }
                        %>
                        
                        <% } %>
                    </p>
                    
                </div> <!-- /content-main-in -->
            </div> <!-- /content-main -->
            <hr class="noscreen" />
        </div> <!-- /content -->

        <jsp:include page="Faults__menu.jsp" />
    
    </div> <!-- /cols -->

<jsp:include page="_footer.jsp" />
