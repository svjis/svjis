<%-- 
    Document   : Administration_userList
    Created on : 21.6.2011, 15:32:47
    Author     : berk
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="cz.svjis.bean.Role"%>
<%@page import="cz.svjis.common.JspSnippets"%>
<%@page import="cz.svjis.servlet.Cmd"%>
<jsp:useBean id="language" scope="session" class="cz.svjis.bean.Language" />
<jsp:useBean id="roleList" scope="request" class="java.util.ArrayList" />
<jsp:useBean id="userList" scope="request" class="java.util.ArrayList" />
<jsp:useBean id="disabledUsers" scope="request" class="cz.svjis.bean.Boolean" />

<jsp:include page="_header.jsp" />
<jsp:include page="_tray.jsp" />

    <!-- Columns -->
    <div id="cols" class="box">

        <!-- Content -->
        <div id="content">
            <div id="content-main">
                <div id="content-main-in">
                    <h1 class="page-title" id="tbl-desc"><%=language.getText("User list") %></h1>

                    <div class="container">
                        <div class="row">

                            <div class="left">
                                <p>[<a href="Dispatcher?page=<%=Cmd.USER_EDIT %>&id=0"><%=language.getText("Add new user") %></a>]</p>
                            </div>
                            <div class="left">
                                <p>[<a href="Upload?page=<%=Cmd.USER_XLS %>"><%=language.getText("Export to Excel") %></a>]</p>
                            </div>
                            
                            <div class="right">
                                <p><%=language.getText("Disabled users") %></p>
                            </div>
                            <div class="middle">
                                <p>
                                <form action="Dispatcher" method="post">
                                    <input type="hidden" name="page" value="<%=Cmd.USER_LIST %>" />
                                    <input type="checkbox" name="disabledUsers"  onchange='this.form.submit()' <%=(disabledUsers.isValue()) ? "checked" : "" %> />
                                </form>
                                </p>
                            </div>
                            <div class="right">
                                <p><%=language.getText("Role filter") %></p>
                            </div>
                            <div class="middle">
                                <p>
                                <form action="Dispatcher" method="post">
                                    <input type="hidden" name="page" value="<%=Cmd.USER_LIST %>" />
                                    <select name='roleId' onchange='this.form.submit()'>
                                        <option value="0"><%=language.getText("all") %></option>
                                        <%
                                            int roleId = Integer.valueOf((request.getParameter("roleId") == null) ? "0" : request.getParameter("roleId"));
                                            for (Role r: (List<Role>) roleList) {
                                                String sel;
                                                if (roleId == r.getId()) {
                                                    sel = "selected";
                                                } else {
                                                    sel = "";
                                                }
                                        %>
                                        <option value="<%=r.getId() %>" <%=sel %>><%=r.getDescription() %></option>
                                        <% } %>
                                    </select>
                                </form>
                                </p>
                            </div>

                        </div>
                    </div>
                    
                    <table class="list" width="100%" aria-describedby="tbl-desc">
                        <tr>
                            <th class="list" scope="col">&nbsp;</th>
                            <th class="list" scope="col">&nbsp;</th>
                            <th class="list" scope="col">&nbsp;</th>
                            <th class="list" scope="col"><%=language.getText("Last name") %></th>
                            <th class="list" scope="col"><%=language.getText("First name") %></th>
                            <th class="list" scope="col"><%=language.getText("Enabled") %></th>
                            <th class="list" scope="col"><%=language.getText("E-Mail") %></th>
                            <th class="list" scope="col"><%=language.getText("Last login") %></th>
                        </tr>
                    <%
                    int i = 0;
                    java.util.Iterator<cz.svjis.bean.User> userI = userList.iterator();
                    while (userI.hasNext()) {
                        cz.svjis.bean.User u = userI.next();
                    %>
                        <tr>
                            <td class="list" style="text-align: right"><%=++i %></td>
                            <td class="list"><a href="Dispatcher?page=<%=Cmd.USER_EDIT %>&id=<%=u.getId() %>"><img src="gfx/pencil.png" border="0" title="<%=language.getText("Edit") %>" alt="<%=language.getText("Edit") %>"></a></td>
                            <td class="list"><a href="Dispatcher?page=<%=Cmd.USER_BU %>&id=<%=u.getId() %>"><img src="gfx/house.png" border="0" title="<%=language.getText("Building unit list") %>" alt="<%=language.getText("Building unit list") %>"></a></td>
                            <td class="list"><%=u.getLastName() %></td>
                            <td class="list"><%=u.getFirstName() %></td>
                            <td class="list"><%=(u.isEnabled()) ? language.getText("yes") : language.getText("no") %></td>
                            <td class="list"><a href="mailto:<%=u.geteMail() %>"><%=u.geteMail() %></a></td>
                            <td class="list"><%=(u.getLastLogin() == null) ? "&nbsp;" : JspSnippets.renderDateTime(u.getLastLogin()) %></td>
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
