<%-- 
    Document   : ArticleDetail
    Created on : 17.6.2011, 23:15:59
    Author     : berk
--%>

<%@page import="cz.svjis.bean.ArticleAttachment"%>
<%@page import="cz.svjis.common.HttpUtils"%>
<%@page import="cz.svjis.bean.ArticleComment"%>
<%@page import="java.io.File"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="cz.svjis.bean.User" />
<jsp:useBean id="language" scope="session" class="cz.svjis.bean.Language" />
<jsp:useBean id="article" scope="request" class="cz.svjis.bean.Article" />
<jsp:useBean id="watching" scope="request" class="java.lang.String" />


<%
    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
%>
<jsp:include page="_header.jsp" />
<jsp:include page="_tray.jsp" />

    <!-- Columns -->
    <div id="cols" class="box">

        <!-- Content -->
        <div id="content">
            <div id="content-main">
                <div id="content-main-in">
                    <%
                        String body = article.getBody();
                        if ((article.getAttachmentList() != null) && (article.getAttachmentList().size() != 0)) {
                            Iterator<ArticleAttachment> attachI = article.getAttachmentList().iterator();
                            while (attachI.hasNext()) {
                                ArticleAttachment a = attachI.next();
                                body = body.replaceAll("\\{" + a.getFileName() + "\\}", "<img src=\"Upload?page=download&id=" + a.getId() + "\" alt=\"" + a.getFileName() + "\">");
                            }
                        }

                    %>
                    <div class="article-detail">
                    <div class="article-desc">
                    <h1 class="article-title"><%=HttpUtils.highlight(article.getHeader(), request.getParameter("search")) %></h1>
                    <p class="info">
                        <%=language.getText("Published:") %> <strong><%=sdf.format(article.getCreationDate()) %></strong> 
                        <%=language.getText("by:") %> <strong><%=article.getAuthor().getFirstName() %> <%=article.getAuthor().getLastName() %></strong> 
                        <%=(article.getCommentList().size() != 0) ? language.getText("Comments:") + " <strong>" + article.getCommentList().size() + "</strong>" : "" %>
                    </p> 
                    <%=HttpUtils.highlight(article.getDescription(), request.getParameter("search")) %>
                    <%=HttpUtils.highlight(body, request.getParameter("search")) %>
                    </div>
                    </div>
                    
                    <%
                        if ((article.getAttachmentList() != null) && (article.getAttachmentList().size() != 0)) {
                    %>
                    <p>
                    <table class="list">
                        <tr>
                            <th class="list" scope="col">&nbsp;</th>
                            <th class="list" scope="col"><%=language.getText("Attachments:") %></th>
                        </tr>
                        <%
                        Iterator<ArticleAttachment> attachI = article.getAttachmentList().iterator();
                        while (attachI.hasNext()) {
                            ArticleAttachment a = attachI.next();
                            String icon = "gfx/Files_unknown.gif";
                            String extension = a.getFileName().toLowerCase().substring(a.getFileName().lastIndexOf(".") + 1);
                            File f = new File(request.getServletContext().getRealPath("/gfx") + "/Files_" + extension + ".gif");
                            if (f.exists()) {
                                icon = "gfx/Files_" + extension + ".gif";
                            }
                        %>
                        <tr>
                            <td class="list"><img src="<%=icon%>" alt="<%=a.getFileName() %>" border="0"></td>
                            <td class="list"><a href="Upload?page=download&id=<%=a.getId() %>"><%=a.getFileName() %></a></td>
                        </tr>
                        <%
                        }
                        %>
                    </table>
                    </p>
                    <%
                       }
                    %>
                    
                    <%
                        if (((article.getCommentList() != null) && (article.getCommentList().size() != 0)) || (article.isCommentsAllowed())) {
                    %>
                        <h2 class="article-title"><%=language.getText("Comments:") %></h2>
                    <%
                       }
                    %>
                    
                    <%
                        if ((article.getCommentList() != null) && (article.getCommentList().size() != 0)) {
                            SimpleDateFormat sdft = new SimpleDateFormat("dd.MM.yyyy HH:mm");
                            Iterator<ArticleComment> commI = article.getCommentList().iterator();
                            while (commI.hasNext()) {
                                ArticleComment c = commI.next();
                    %>
                            <div class="article box">
                            <strong><%=c.getUser().getFirstName() %>&nbsp;<%=c.getUser().getLastName() %> <%=sdft.format(c.getInsertionTime()) %></strong><br>
                            <%=c.getBody().replace("\n", "<br>") %>
                            </div>
                        <%
                            }
                        %>
                    <%
                       }
                    %>
                

                    <%
                        if ((article.isCommentsAllowed()) && (user.hasPermission("can_insert_article_comment"))) {
                    %>
                        <p>
                            <% if (watching.equals("0")) { %>
                                [<a href="Dispatcher?page=articleFast&id=<%=article.getId() %>&watch=1"><%=language.getText("Start watching discussion") %></a>]&nbsp;
                            <% } else { %>
                                [<a href="Dispatcher?page=articleFast&id=<%=article.getId() %>&watch=0"><%=language.getText("Stop watching discussion") %></a>]&nbsp;
                            <% } %>
                        </p>

                        <form action="Dispatcher" method="post">
                             <input type="hidden" name="page" value="insertArticleComment">
                             <input type="hidden" name="id" value="<%=article.getId() %>">
                             <p>
                                 <textarea
                                 name="body"
                                 rows=5 cols=50
                                 wrap
                                 ></textarea>
                             </p>
                             <p>
                                 <input class="my-button" type="submit" value="<%=language.getText("Insert comment") %>" name="submit">
                             </p>
                         </form>
                    <%
                       }
                    %>
                </div> <!-- /content-main-in -->
            </div> <!-- /content-main -->
            <hr class="noscreen" />
        </div> <!-- /content -->

        <jsp:include page="Article__menu.jsp" />
    
    </div> <!-- /cols -->

<jsp:include page="_footer.jsp" />