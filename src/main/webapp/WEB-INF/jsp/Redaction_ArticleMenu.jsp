<%-- 
    Document   : Redaction_ArticleMenu
    Created on : 8.7.2011, 12:39:20
    Author     : berk
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@page import="cz.svjis.bean.Language"%>
<%@page import="cz.svjis.bean.MenuItem"%>
<%@page import="cz.svjis.servlet.Cmd"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>

<jsp:useBean id="language" scope="session" class="cz.svjis.bean.Language" />
<jsp:useBean id="menu" scope="request" class="cz.svjis.bean.Menu" />


<jsp:include page="_header.jsp" />
<jsp:include page="_tray.jsp" />

    <!-- Columns -->
    <div id="cols" class="box">

        <!-- Content -->
        <div id="content">
            <div id="content-main">
                <div id="content-main-in">
                    <h1 class="page-title" id="tbl-desc"><%=language.getText("Article menu") %></h1>
                    [<a href="Dispatcher?page=<%=Cmd.REDACTION_MENU_EDIT %>&id=0"><%=language.getText("Add new menu node") %></a>]
                    
                    <table class="list" aria-describedby="tbl-desc">
                        <tr>
                            <th class="list" scope="col"><%=language.getText("Menu node name") %></th>
                            <th class="list" scope="col">&nbsp;</th>
                            <th class="list" scope="col">&nbsp;</th>
                        </tr>
                        <%=menu.writeRedactionSubMenu(menu.getMenu(), 0, language) %>
                    </table>
                        
                </div> <!-- /content-main-in -->
            </div> <!-- /content-main -->
            <hr class="noscreen" />
        </div> <!-- /content -->

        <jsp:include page="Redaction__menu.jsp" />
    
    </div> <!-- /cols -->

<jsp:include page="_footer.jsp" />
