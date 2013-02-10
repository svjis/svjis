<%-- 
    Document   : Contact_company
    Created on : 6.7.2011, 23:18:57
    Author     : berk
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="language" scope="session" class="cz.svjis.bean.Language" />
<jsp:useBean id="company" scope="session" class="cz.svjis.bean.Company" />

<jsp:include page="_header.jsp" />
<jsp:include page="_tray.jsp" />

    <!-- Columns -->
    <div id="cols" class="box">

        <!-- Content -->
        <div id="content">
            <div id="content-main">
                <div id="content-main-in">
                    <h1 class="page-title"><%=company.getName() %></h1>
                    
                    <table border="0">
                        <% if (!company.getAddress().equals("")) { %>
                        <tr>
                            <td><strong><%=language.getText("Address") %>:&nbsp;</strong></td>
                            <td><%=company.getAddress() %></td>
                        </tr>
                        <% } %>
                        <% if (!company.getCity().equals("")) { %>
                        <tr>
                            <td><strong><%=language.getText("City") %>:&nbsp;</strong></td>
                            <td><%=company.getCity() %></td>
                        </tr>
                        <% } %>
                        <% if (!company.getPostCode().equals("")) { %>
                        <tr>
                            <td><strong><%=language.getText("Post code") %>:&nbsp;</strong></td>
                            <td><%=company.getPostCode() %></td>
                        </tr>
                        <% } %>
                        <% if (!company.getPhone().equals("")) { %>
                        <tr>
                            <td><strong><%=language.getText("Phone") %>:&nbsp;</strong></td>
                            <td><%=company.getPhone() %></td>
                        </tr>
                        <% } %>
                        <% if (!company.getFax().equals("")) { %>
                        <tr>
                            <td><strong><%=language.getText("Fax") %>:&nbsp;</strong></td>
                            <td><%=company.getFax() %></td>
                        </tr>
                        <% } %>
                        <% if (!company.geteMail().equals("")) { %>
                        <tr>
                            <td><strong><%=language.getText("E-Mail") %>:&nbsp;</strong></td>
                            <td><a href="mailto:<%=company.geteMail() %>"><%=company.geteMail() %></a></td>
                        </tr>
                        <% } %>
                        <% if (!company.getRegistrationNo().equals("")) { %>
                        <tr>
                            <td><strong><%=language.getText("Registration No.") %>:&nbsp;</strong></td>
                            <td><%=company.getRegistrationNo() %></td>
                        </tr>
                        <% } %>
                    </table>
                </div> <!-- /content-main-in -->
            </div> <!-- /content-main -->
            <hr class="noscreen" />
        </div> <!-- /content -->

        <jsp:include page="Contact__menu.jsp" />
    
    </div> <!-- /cols -->

<jsp:include page="_footer.jsp" />
