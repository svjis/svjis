<%-- 
    Document   : Redaction_MiniNewsList
    Created on : 29.6.2011, 17:01:39
    Author     : berk
--%>

<%@page import="cz.svjis.bean.MiniNews"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="language" scope="session" class="cz.svjis.bean.Language" />
<jsp:useBean id="miniNewsList" scope="request" class="java.util.ArrayList" />

<jsp:include page="_header.jsp" />
<jsp:include page="_tray.jsp" />

    <!-- Columns -->
    <div id="cols" class="box">

        <!-- Content -->
        <div id="content">
            <div id="content-main">
                <div id="content-main-in">
                    <h1 class="page-title"><%=language.getText("News list") %></h1>
                    [<a href="Dispatcher?page=redactionNewsEdit&id=0"><%=language.getText("Add new mini news") %></a>]<br>

                    <table class="list" width="100%">
                        <tr>
                            <th class="list" scope="col">&nbsp;</th>
                            <th class="list" scope="col">&nbsp;</th>
                            <th class="list" scope="col"><%=language.getText("Time") %></th>
                            <th class="list" scope="col"><%=language.getText("Language") %></th>
                            <th class="list" scope="col"><%=language.getText("Author") %></th>
                            <th class="list" scope="col"><%=language.getText("Published") %></th>
                            <th class="list" scope="col"><%=language.getText("Body") %></th>
                        </tr>
                    <%
                        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
                        Iterator articleListI = miniNewsList.iterator();
                        while (articleListI.hasNext()) {
                            MiniNews n = (MiniNews) articleListI.next();
                    %>
                        <tr>
                            <td class="list"><a href="Dispatcher?page=redactionNewsEdit&id=<%=n.getId() %>"><img src="gfx/pencil.png" border="0" title="<%=language.getText("Edit") %>"></a></td>
                            <td class="list"><a onclick="if (!confirm('<%=language.getText("Really do you want to remove news") %> ?')) return false;" href="Dispatcher?page=redactionNewsDelete&id=<%=n.getId() %>"><img src="gfx/delete.png" border="0" title="<%=language.getText("Delete") %>"></a></td>
                            <td class="list"><%=sdf.format(n.getTime()) %></td>
                            <td class="list"><%=n.getLanguage() %></td>
                            <td class="list"><%=n.getCreatedBy() %></td>
                            <td class="list"><%=(n.isPublished()) ? language.getText("yes") : language.getText("no") %></td>
                            <td class="list"><%=n.getBody() %></td>
                        </tr>
                    <%
                        }
                    %>
                    </table>
                </div> <!-- /content-main-in -->
            </div> <!-- /content-main -->
            <hr class="noscreen" />
        </div> <!-- /content -->

        <jsp:include page="Redaction__menu.jsp" />
    
    </div> <!-- /cols -->

<jsp:include page="_footer.jsp" />
