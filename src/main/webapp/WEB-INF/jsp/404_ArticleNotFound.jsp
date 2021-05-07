<%-- 
    Document   : 404_ArticleNotFound
    Created on : 7.11.2012, 10:44:21
    Author     : berk
--%>


<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="language" scope="session" class="cz.svjis.bean.Language" />
<jsp:useBean id="user" scope="session" class="cz.svjis.bean.User" />
<jsp:useBean id="menu" scope="request" class="cz.svjis.bean.Menu" />

<jsp:include page="_header.jsp" />
<jsp:include page="_tray.jsp" />

    <!-- Columns -->
    <div id="cols" class="box">

        <!-- Content -->
        <div id="content">
            <div id="content-main">
                <div id="content-main-in">
                    <h1 class="page-title"><%=language.getText("Article was not found") %></h1>
                    <p><%=language.getText("Probably you have insufficient permissions.") %></p>
                    <% if (!user.isUserLogged()) { %>
                        <p><%=language.getText("Try to login with your login name and password.") %></p>
                    <% } %>
                    
                    <p><a href="Dispatcher"><%=language.getText("Continue here") %></a></p>
                    
                </div> <!-- /content-main-in -->
            </div> <!-- /content-main -->
            <hr class="noscreen" />
        </div> <!-- /content -->

        <jsp:include page="Article__menu.jsp" />
    
    </div> <!-- /cols -->

<jsp:include page="_footer.jsp" />
