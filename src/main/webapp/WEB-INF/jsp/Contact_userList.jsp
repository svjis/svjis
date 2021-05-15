<%-- 
    Document   : Phonelist_userList
    Created on : 25.6.2011, 14:22:36
    Author     : berk
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<jsp:useBean id="language" scope="session" class="cz.svjis.bean.Language" />
<jsp:useBean id="userList" scope="request" class="java.util.ArrayList" />

<jsp:include page="_header.jsp" />
<jsp:include page="_tray.jsp" />

    <!-- Columns -->
    <div id="cols" class="box">

        <!-- Content -->
        <div id="content">
            <div id="content-main">
                <div id="content-main-in">
                    <h1 class="page-title" id="tbl-desc"><%=language.getText("Contact list") %></h1>
                    
                    <table class="list" aria-describedby="tbl-desc">
                        <tr>
                            <th class="list" scope="col">&nbsp;</th>
                            <th class="list" scope="col"><%=language.getText("Last name") %></th>
                            <th class="list" scope="col"><%=language.getText("First name") %></th>
                            <th class="list" scope="col"><%=language.getText("Fixed phone") %></th>
                            <th class="list" scope="col"><%=language.getText("Cell phone") %></th>
                            <th class="list" scope="col"><%=language.getText("E-Mail") %></th>
                        </tr>
                    <%
                    int i = 0;
                    java.util.Iterator<cz.svjis.bean.User> userI = userList.iterator();
                    while (userI.hasNext()) {
                        cz.svjis.bean.User u = userI.next();
                    %>
                        <tr>
                            <td class="list" style="text-align: right"><%=++i %></td>
                            <td class="list"><%=u.getLastName() %></td>
                            <td class="list"><%=u.getFirstName() %></td>
                            <td class="list"><%=u.getFixedPhone() %></td>
                            <td class="list"><%=u.getCellPhone() %></td>
                            <td class="list"><a href="mailto:<%=u.geteMail() %>"><%=u.geteMail() %></a></td>
                        </tr>
                    <%
                    }
                    %>
                    </table>
                </div> <!-- /content-main-in -->
            </div> <!-- /content-main -->
            <hr class="noscreen" />
        </div> <!-- /content -->

        <jsp:include page="Contact__menu.jsp" />
    
    </div> <!-- /cols -->

<jsp:include page="_footer.jsp" />
