<%-- 
    Document   : Faults_reportEdit
    Created on : 20.12.2019, 14:43:42
    Author     : jarberan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@page import="cz.svjis.bean.Permission"%>
<%@page import="cz.svjis.bean.BuildingEntrance"%>

<%@page import="cz.svjis.bean.Language"%>
<%@page import="cz.svjis.bean.User"%>
<%@page import="java.util.ArrayList"%>

<jsp:useBean id="language" scope="session" class="cz.svjis.bean.Language" />
<jsp:useBean id="user" scope="session" class="cz.svjis.bean.User" />
<jsp:useBean id="report" scope="request" class="cz.svjis.bean.FaultReport" />
<jsp:useBean id="entranceList" scope="request" class="java.util.ArrayList" />
<jsp:useBean id="resolverList" scope="request" class="java.util.ArrayList" />

<jsp:include page="_header.jsp" />
<jsp:include page="_tray.jsp" />

    <!-- Columns -->
    <div id="cols" class="box">

        <!-- Content -->
        <div id="content">
            <div id="content-main">
                <div id="content-main-in">
                    <h1 class="page-title"><%=language.getText("Fault reporting") %></h1>
                    <form action="Dispatcher" method="post">
                        <input type="hidden" name="page" value="faultReportingSave">
                        <input type="hidden" name="id" value="<%=report.getId()  %>">
                        
                        <p>
                            <%=language.getText("Subject") %><br>
                            <input class="common-input" id="subject-input" type="text" name="subject" maxlength="50" size="80" value="<%=report.getSubject() %>">
                        </p>
                        
                        <p>
                        <%=language.getText("Building entrance") %>&nbsp;(<%=language.getText("optional") %>)<br>
                        <select class="common-input"  id="entrance-input" name="entranceId">
                            <option value="0" ></option>
                            <%
                                for (BuildingEntrance be : (ArrayList<BuildingEntrance>) entranceList) {
                            %>
                            <option value="<%=be.getId() %>" <%=((report.getBuildingEntrance() != null) && (be.getId() == report.getBuildingEntrance().getId())) ? "SELECTED" : "" %>><%=be.getDescription() %>&nbsp;(<%=be.getAddress() %>)</option>
                            <%
                                }
                            %>
                        </select>
                        </p>
                        
                        <p>
                        <%=language.getText("Description") %><br>
                        <textarea
                            class="common-textarea"
                            id="body-textarea"
                            name="body"
                            rows=5 cols=80
                            wrap
                            ><%=report.getDescription() %></textarea>
                        </p>
                        
                        <% if (user.hasPermission(Permission.FAULT_REPORTING_RESOLVER)) { %>

                        <p>
                        <%=language.getText("Resolver") %><br>
                        <select class="common-input" id="resolver-input" name="resolverId">
                            <option value="0" ></option>
                            <%
                                for (User r : (ArrayList<User>) resolverList) {
                            %>
                            <option value="<%=r.getId() %>" <%=((report.getAssignedToUser() != null) && (r.getId() == report.getAssignedToUser().getId())) ? "SELECTED" : "" %>><%=r.getFirstName() %>&nbsp;<%=r.getLastName() %></option>
                            <%
                                }
                            %>
                        </select>
                        </p>
                        
                        <p>
                            <%=language.getText("Closed") %><br>
                            <input class="common-input" id="closed-input" type="checkbox" name="closed" <%=(report.isClosed()) ? "checked" : "" %>>
                        </p>
                        
                        <% } %>

                        <p>
                            <input class="my-button" id="submit" type="submit" value="<%=language.getText("Save") %>" />
                        </p>
                    </form>
                </div> <!-- /content-main-in -->
            </div> <!-- /content-main -->
            <hr class="noscreen" />
        </div> <!-- /content -->

        <jsp:include page="Faults__menu.jsp" />
    
    </div> <!-- /cols -->

<jsp:include page="_footer.jsp" />
