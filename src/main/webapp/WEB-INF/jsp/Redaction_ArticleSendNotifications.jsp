<%-- 
    Document   : Redaction_ArticleSendNotifications
    Created on : 6.11.2012, 16:33:01
    Author     : berk
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@page import="java.util.Iterator"%>
<%@page import="cz.svjis.bean.User"%>

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
                    <h1 class="page-title" id="tbl-desc"><%=language.getText("Send notifications") %> - <%=article.getHeader() %></h1>
                    
                    <form action="Dispatcher" method="post">
                        <input type="hidden" name="page" value="redactionArticleSendNotificationsConfirmation">
                        <input type="hidden" name="id" value="<%=article.getId() %>">
                            
                    <table id="recipient-list" class="list" width="100%" aria-describedby="tbl-desc">
                        <tr>
                            <th class="list" scope="col"><input id="check-all" type="checkbox" checked onchange="checkAll();"/></th>
                            <th class="list" scope="col"><%=language.getText("Last name") %></th>
                            <th class="list" scope="col"><%=language.getText("First name") %></th>
                            <th class="list" scope="col"><%=language.getText("E-Mail") %></th>
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
                            <input id="submit" class="my-button" type="submit" value="<%=language.getText("Send") %>" />
                        </p>
                    </form>
                    
                </div> <!-- /content-main-in -->
            </div> <!-- /content-main -->
            <hr class="noscreen" />
        </div> <!-- /content -->

        <jsp:include page="Redaction__menu.jsp" />
    
    </div> <!-- /cols -->
    
    <script src="js/Redaction_ArticleSendNotifications.js"></script>
<jsp:include page="_footer.jsp" />