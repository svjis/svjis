<%-- 
    Document   : Faults_reportEdit
    Created on : 20.12.2019, 14:43:42
    Author     : jarberan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="cz.svjis.bean.Language"%>
<%@page import="cz.svjis.bean.User"%>
<%@page import="java.util.ArrayList"%>

<jsp:useBean id="language" scope="session" class="cz.svjis.bean.Language" />
<jsp:useBean id="user" scope="session" class="cz.svjis.bean.User" />
<jsp:useBean id="report" scope="request" class="cz.svjis.bean.FaultReport" />
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
                            <input id="common-input" type="text" name="subject" maxlength="50" size="100" value="<%=report.getSubject() %>">
                        </p>
                        
                        <p>
                        <%=language.getText("Description") %><br>
                        <textarea
                            name="body"
                            id="common-textarea"
                            rows=5 cols=80
                            wrap
                            ><%=report.getDescription() %></textarea>
                        </p>
                        
                        <% if (user.hasPermission("fault_reporting_resolver")) { %>

                        <p>
                        <%=language.getText("Resolver") %><br>
                        <select name="resolverId" id="common-input">
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
                            <input id="common-input" type="checkbox" name="closed" <%=(report.isClosed()) ? "checked" : "" %>>
                        </p>
                        
                        <% } %>

                        <p>
                            <input type="submit" value="<%=language.getText("Save") %>" />
                        </p>
                    </form>
                </div> <!-- /content-main-in -->
            </div> <!-- /content-main -->
            <hr class="noscreen" />
        </div> <!-- /content -->

        <jsp:include page="Faults__menu.jsp" />
    
    </div> <!-- /cols -->

<jsp:include page="_footer.jsp" />