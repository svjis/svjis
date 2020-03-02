<%-- 
    Document   : Administration_roleList
    Created on : 21.6.2011, 16:55:21
    Author     : berk
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="language" scope="session" class="cz.svjis.bean.Language" />
<jsp:useBean id="roleList" scope="request" class="java.util.ArrayList" />

<jsp:include page="_header.jsp" />
<jsp:include page="_tray.jsp" />

    <!-- Columns -->
    <div id="cols" class="box">

        <!-- Content -->
        <div id="content">
            <div id="content-main">
                <div id="content-main-in">
                    <h1 class="page-title"><%=language.getText("Role list") %></h1>
                    [<a href="Dispatcher?page=roleEdit&id=0"><%=language.getText("Add new role") %></a>]<br>
                    <table class="list">
                        <tr>
                            <th class="list" scope="col">&nbsp;</th>
                            <th class="list" scope="col">&nbsp;</th>
                            <th class="list" scope="col">&nbsp;</th>
                            <th class="list" scope="col">&nbsp;</th>
                            <th class="list" scope="col"><%=language.getText("Description") %></th>
                            <th class="list" scope="col"><%=language.getText("Num. of users") %></th>
                        </tr>
                    <%
                    int i = 0;
                    java.util.Iterator<cz.svjis.bean.Role> roleI = roleList.iterator();
                    while (roleI.hasNext()) {
                        cz.svjis.bean.Role r = roleI.next();
                    %>
                        <tr>
                            <td class="list" style="text-align: right"><%=++i %></td>
                            <td class="list"><a href="Dispatcher?page=roleEdit&id=<%=r.getId() %>"><img src="gfx/pencil.png" border="0" title="<%=language.getText("Edit") %>"></a></td>
                            <td class="list"><a href="Dispatcher?page=userList&roleId=<%=r.getId() %>"><img src="gfx/user.png" border="0" title="<%=language.getText("User list") %>"></a></td>
                            <td class="list"><a onclick="if (!confirm('<%=language.getText("Really do you want to remove role") %> <%=r.getDescription() %> ?')) return false;" href="Dispatcher?page=roleDelete&id=<%=r.getId() %>"><img src="gfx/delete.png" border="0" title="<%=language.getText("Delete") %>"></a></td>
                            <td class="list"><%=r.getDescription() %></td>
                            <td class="list" style="text-align: right"><%=r.getNumOfUsers() %></td>
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
