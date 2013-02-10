<%-- 
    Document   : Administration_userList
    Created on : 21.6.2011, 15:32:47
    Author     : berk
--%>

<%@page import="java.text.SimpleDateFormat"%>
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
                    <h1 class="page-title"><%=language.getText("User list") %></h1>
                    [<a href="Dispatcher?page=userEdit&id=0"><%=language.getText("Add new user") %></a>]&nbsp;
                    [<a href="Upload?page=exportUserListToXls"><%=language.getText("Export to Excel") %></a>]<br>
                    <table class="list">
                        <tr>
                            <th class="list">&nbsp;</th>
                            <th class="list">&nbsp;</th>
                            <th class="list">&nbsp;</th>
                            <th class="list"><%=language.getText("Last name") %></th>
                            <th class="list"><%=language.getText("First name") %></th>
                            <th class="list"><%=language.getText("Enabled") %></th>
                            <th class="list"><%=language.getText("E-Mail") %></th>
                            <th class="list"><%=language.getText("Last login") %></th>
                        </tr>
                    <%
                    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
                    int i = 0;
                    java.util.Iterator<cz.svjis.bean.User> userI = userList.iterator();
                    while (userI.hasNext()) {
                        cz.svjis.bean.User u = userI.next();
                    %>
                        <tr>
                            <td class="list" style="text-align: right"><%=++i %></td>
                            <td class="list"><a href="Dispatcher?page=userEdit&id=<%=u.getId() %>"><img src="gfx/pencil.png" border="0" title="<%=language.getText("Edit") %>"></a></td>
                            <td class="list"><a href="Dispatcher?page=userBuildingUnits&id=<%=u.getId() %>"><img src="gfx/house.png" border="0" title="<%=language.getText("Building unit list") %>"></a></td>
                            <td class="list"><%=u.getLastName() %></td>
                            <td class="list"><%=u.getFirstName() %></td>
                            <td class="list"><%=(u.isEnabled()) ? language.getText("yes") : language.getText("no") %></td>
                            <td class="list"><a href="mailto:<%=u.geteMail() %>"><%=u.geteMail() %></a></td>
                            <td class="list"><%=(u.getLastLogin() == null) ? "&nbsp;" : sdf.format(u.getLastLogin()) %></td>
                        </tr>
                    <%
                    }
                    %>
                    </table>
                </div> <!-- /content-main-in -->
            </div> <!-- /content-main -->
            <hr class="noscreen" />
        </div> <!-- /content -->

        <jsp:include page="Administration__menu.jsp" />
    
    </div> <!-- /cols -->

<jsp:include page="_footer.jsp" />
