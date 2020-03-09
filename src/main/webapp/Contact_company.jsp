<%-- 
    Document   : Contact_company
    Created on : 6.7.2011, 23:18:57
    Author     : berk
--%>

<%@page import="cz.svjis.bean.BoardMember"%>
<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="language" scope="session" class="cz.svjis.bean.Language" />
<jsp:useBean id="company" scope="session" class="cz.svjis.bean.Company" />
<jsp:useBean id="boardList" scope="request" class="java.util.ArrayList" />

<jsp:include page="_header.jsp" />
<jsp:include page="_tray.jsp" />

    <!-- Columns -->
    <div id="cols" class="box">

        <!-- Content -->
        <div id="content">
            <div id="content-main">
                <div id="content-main-in">
                    <h1 class="page-title" id="tbl-desc"><%=company.getName() %></h1>
                    
                    <table border="0" aria-describedby="tbl-desc">
                        <% if (!company.getAddress().equals("")) { %>
                        <tr>
                            <th scope="row" style="text-align: left"><%=language.getText("Address") %>:&nbsp;</th>
                            <td><%=company.getAddress() %></td>
                        </tr>
                        <% } %>
                        <% if (!company.getCity().equals("")) { %>
                        <tr>
                            <th scope="row" style="text-align: left"><%=language.getText("City") %>:&nbsp;</th>
                            <td><%=company.getCity() %></td>
                        </tr>
                        <% } %>
                        <% if (!company.getPostCode().equals("")) { %>
                        <tr>
                            <th scope="row" style="text-align: left"><%=language.getText("Post code") %>:&nbsp;</th>
                            <td><%=company.getPostCode() %></td>
                        </tr>
                        <% } %>
                        <% if (!company.getPhone().equals("")) { %>
                        <tr>
                            <th scope="row" style="text-align: left"><%=language.getText("Phone") %>:&nbsp;</th>
                            <td><%=company.getPhone() %></td>
                        </tr>
                        <% } %>
                        <% if (!company.getFax().equals("")) { %>
                        <tr>
                            <th scope="row" style="text-align: left"><%=language.getText("Fax") %>:&nbsp;</th>
                            <td><%=company.getFax() %></td>
                        </tr>
                        <% } %>
                        <% if (!company.geteMail().equals("")) { %>
                        <tr>
                            <th scope="row" style="text-align: left"><%=language.getText("E-Mail") %>:&nbsp;</th>
                            <td><a href="mailto:<%=company.geteMail() %>"><%=company.geteMail() %></a></td>
                        </tr>
                        <% } %>
                        <% if (!company.getRegistrationNo().equals("")) { %>
                        <tr>
                            <th scope="row" style="text-align: left"><%=language.getText("Registration No.") %>:&nbsp;</th>
                            <td><%=company.getRegistrationNo() %></td>
                        </tr>
                        <% } %>
                    </table>

                    <% if (!boardList.isEmpty()) { %>
                    <table border="0" aria-describedby="tbl-desc">
                        <tr>
                            <th scope="col" style="text-align: left" colspan="3"><%=language.getText("Board") %>:&nbsp;</th>
                        </tr>
                    <%
                        for (BoardMember bm: (ArrayList<BoardMember>) boardList) {
                    %>
                        <tr>
                            <td><%=bm.getUser().getSalutation() %>&nbsp;</td>
                            <td><%=bm.getUser().getFirstName() %>&nbsp;<%=bm.getUser().getLastName() %>&nbsp;&nbsp;</td>
                            <td><%=bm.getBoardMemberType().getDescription() %>&nbsp;</td>
                        </tr>
                    <% 
                        } 
                    %>
                    </table>
                    <% } %>

                </div> <!-- /content-main-in -->
            </div> <!-- /content-main -->
            <hr class="noscreen" />
        </div> <!-- /content -->

        <jsp:include page="Contact__menu.jsp" />
    
    </div> <!-- /cols -->

<jsp:include page="_footer.jsp" />
