<%-- 
    Document   : Redaction_ArticleSendNotifications
    Created on : 6.11.2012, 16:33:01
    Author     : berk
--%>

<%@page import="java.util.Iterator"%>
<%@page import="cz.svjis.bean.User"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="language" scope="session" class="cz.svjis.bean.Language" />
<jsp:useBean id="user" scope="session" class="cz.svjis.bean.User" />
<jsp:useBean id="menu" scope="request" class="cz.svjis.bean.Menu" />
<jsp:useBean id="userList" scope="request" class="java.util.ArrayList" />
<jsp:useBean id="article" scope="request" class="cz.svjis.bean.Article" />

<jsp:include page="_header.jsp" />
<jsp:include page="_tray.jsp" />

    <!-- Columns -->
    <div id="cols" class="box">

        <!-- Content -->
        <div id="content">
            <div id="content-main">
                <div id="content-main-in">
                    <h2><%=language.getText("Send notifications") %> - <%=article.getHeader() %></h2>
                    
                    <form action="Dispatcher" method="post">
                        <input type="hidden" name="page" value="redactionArticleSendNotificationsConfirmation">
                        <input type="hidden" name="id" value="<%=article.getId() %>">
                            
                    <table class="list" width="100%">
                        <tr>
                            <th class="list">&nbsp;</th>
                            <th class="list"><%=language.getText("Last name") %></th>
                            <th class="list"><%=language.getText("First name") %></th>
                            <th class="list"><%=language.getText("E-Mail") %></th>
                        </tr>
                    <%
                        Iterator userListI = userList.iterator();
                        while (userListI.hasNext()) {
                            User u = (User) userListI.next();
                    %>
                        <tr>
                            <td class="list"><input type="checkbox" name="u_<%=u.getId() %>" checked /></td>
                            <td class="list"><%=u.getLastName() %></td>
                            <td class="list"><%=u.getFirstName() %></td>
                            <td class="list"><a href="mailto:<%=u.geteMail() %>"><%=u.geteMail() %></a></td>
                        </tr>
                    <%
                        }
                    %>
                    </table>
                    
                        <p>
                            <input type="submit" value="<%=language.getText("Send") %>" />
                        </p>
                    </form>
                    
                </div> <!-- /content-main-in -->
            </div> <!-- /content-main -->
            <hr class="noscreen" />
        </div> <!-- /content -->

        <jsp:include page="Redaction__menu.jsp" />
    
    </div> <!-- /cols -->

<jsp:include page="_footer.jsp" />