<%-- 
    Document   : Redaction_ArticleList
    Created on : 21.6.2011, 23:10:13
    Author     : berk
--%>

<%@page import="java.net.URLEncoder"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Iterator"%>
<%@page import="cz.svjis.bean.Article"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="language" scope="session" class="cz.svjis.bean.Language" />
<jsp:useBean id="user" scope="session" class="cz.svjis.bean.User" />
<jsp:useBean id="menu" scope="request" class="cz.svjis.bean.Menu" />
<jsp:useBean id="articleList" scope="request" class="java.util.ArrayList" />
<jsp:useBean id="articleListInfo" scope="request" class="cz.svjis.bean.ArticleListInfo" />

<jsp:include page="_header.jsp" />
<jsp:include page="_tray.jsp" />

    <!-- Columns -->
    <div id="cols" class="box">

        <!-- Content -->
        <div id="content">
            <div id="content-main">
                <div id="content-main-in">
                    <h2><%=language.getText("Article list") %></h2>
                    [<a href="Dispatcher?page=redactionArticleEdit&id=0"><%=language.getText("Add new article") %></a>]<br>

                    <table class="list" width="100%">
                        <tr>
                            <th class="list">&nbsp;</th>
                            <th class="list">&nbsp;</th>
                            <th class="list">&nbsp;</th>
                            <th class="list"><%=language.getText("Article") %></th>
                            <th class="list"><%=language.getText("Menu") %></th>
                            <th class="list"><%=language.getText("Author") %></th>
                            <th class="list"><%=language.getText("Date") %></th>
                            <th class="list"><%=language.getText("Published") %></th>
                        </tr>
                    <%
                        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
                        Iterator articleListI = articleList.iterator();
                        while (articleListI.hasNext()) {
                            Article a = (Article) articleListI.next();
                    %>
                        <tr>
                            <td class="list"><a href="Dispatcher?page=articleDetail&id=<%=a.getId() %>"><img src="gfx/find.png" border="0" title="View"></a></td>
                            <td class="list"><a href="Dispatcher?page=redactionArticleEdit&id=<%=a.getId() %>"><img src="gfx/pencil.png" border="0" title="<%=language.getText("Edit") %>"></a></td>
                            <td class="list"><a href="Dispatcher?page=redactionArticleSendNotifications&id=<%=a.getId() %>"><img src="gfx/email_open_image.png" border="0" title="<%=language.getText("Send notifications") %>"></td>
                            <td class="list"><%=a.getHeader() %></td>
                            <td class="list"><%=a.getMenuNodeDescription() %></td>
                            <td class="list"><%=a.getAuthor().getFirstName() %>&nbsp;<%=a.getAuthor().getLastName() %></td>
                            <td class="list"><%=sdf.format(a.getCreationDate()) %></td>
                            <td class="list"><%=(a.isPublished()) ? language.getText("yes") : language.getText("no") %></td>
                        </tr>
                    <%
                        }
                    %>
                    </table>
                    
                    <br>
                    <strong><%=language.getText("Pages:") %></strong>&nbsp;
                    <%
                        for (int i = 0; i < articleListInfo.getNumOfPages(); i ++) {
                            if ((i + 1) != articleListInfo.getActualPage()) {
                                if (request.getParameter("search") != null) {
                                    String search = URLEncoder.encode(request.getParameter("search"), "UTF-8");
                    %>
                                <a href="Dispatcher?page=redactionSearch&search=<%=search %>&pageNo=<%=(i + 1) %>"><%=(i + 1) %></a>&nbsp;
                    <%
                                } else {
                    %>
                                <a href="Dispatcher?page=redactionArticleList&section=<%= articleListInfo.getMenuNodeId() %>&pageNo=<%=(i + 1) %>"><%=(i + 1) %></a>&nbsp;
                    <%
                                }
                            } else {
                    %>
                                <b><%=(i + 1) %></b>
                    <%            
                            }
                        }
                    %>
                </div> <!-- /content-main-in -->
            </div> <!-- /content-main -->
            <hr class="noscreen" />
        </div> <!-- /content -->

        <jsp:include page="Redaction__menu.jsp" />
    
    </div> <!-- /cols -->

<jsp:include page="_footer.jsp" />
