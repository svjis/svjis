<%-- 
    Document   : Redaction_ArticleSendNotificationsConfirmation
    Created on : 6.11.2012, 22:38:38
    Author     : berk
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="language" scope="session" class="cz.svjis.bean.Language" />
<jsp:useBean id="user" scope="session" class="cz.svjis.bean.User" />
<jsp:useBean id="menu" scope="request" class="cz.svjis.bean.Menu" />
<jsp:useBean id="article" scope="request" class="cz.svjis.bean.Article" />

<jsp:include page="_header.jsp" />
<jsp:include page="_tray.jsp" />

    <!-- Columns -->
    <div id="cols" class="box">

        <!-- Content -->
        <div id="content">
            <div id="content-main">
                <div id="content-main-in">
                    <h1 class="page-title"><%=language.getText("Send notifications") %> - <%=article.getHeader() %></h1>
                    <p><%=article.getNumOfReads() %> <%=language.getText("notifications has been sent.") %></p>
                    <p><a href="Dispatcher?page=redactionArticleList"><%=language.getText("Continue here") %></a></p>
                    
                </div> <!-- /content-main-in -->
            </div> <!-- /content-main -->
            <hr class="noscreen" />
        </div> <!-- /content -->

        <jsp:include page="Redaction__menu.jsp" />
    
    </div> <!-- /cols -->

<jsp:include page="_footer.jsp" />