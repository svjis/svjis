<%-- 
    Document   : ArticleDetail
    Created on : 17.6.2011, 23:15:59
    Author     : berk
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@page import="cz.svjis.bean.Permission"%>
<%@page import="cz.svjis.bean.Attachment"%>
<%@page import="cz.svjis.common.JspSnippets"%>
<%@page import="java.util.Iterator"%>

<jsp:useBean id="user" scope="session" class="cz.svjis.bean.User" />
<jsp:useBean id="language" scope="session" class="cz.svjis.bean.Language" />
<jsp:useBean id="article" scope="request" class="cz.svjis.bean.Article" />
<jsp:useBean id="watching" scope="request" class="java.lang.String" />

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
                            Iterator<Attachment> attachI = article.getAttachmentList().iterator();
                            while (attachI.hasNext()) {
                                Attachment a = attachI.next();
                                body = body.replaceAll("\\{" + a.getFileName() + "\\}", "<img src=\"Upload?page=download&id=" + a.getId() + "\" alt=\"" + a.getFileName() + "\">");
                            }
                        }

                    %>
                    <div class="article-detail">
	                    <div class="article-desc">
	                    <h1 class="article-title" id="tbl-desc"><%=JspSnippets.highlight(article.getHeader(), request.getParameter("search")) %></h1>
	                    <p class="info">
	                        <%=language.getText("Published:") %> <strong><%=JspSnippets.renderDate(article.getCreationDate()) %></strong>, 
	                        <%=language.getText("by:") %> <strong><%=article.getAuthor().getFullName(false) %></strong><%=(article.getCommentList().size() != 0) ? ", " + language.getText("Comments:") + " <strong>" + article.getCommentList().size() + "</strong>" : "" %>
	                    </p> 
	                    <%=JspSnippets.highlight(article.getDescription(), request.getParameter("search")) %>
	                    <%=JspSnippets.highlight(body, request.getParameter("search")) %>
	                    </div>
                    </div>
                    
                    <%=JspSnippets.renderAttachments(article.getAttachmentList(), request, "Attachments:", "download", "", false, false, false, false, "tbl-desc") %>
                    
                    <%=JspSnippets.renderComments(article.getCommentList(), request) %>

                    <% if ((article.isCommentsAllowed()) && (user.hasPermission(Permission.CAN_INSERT_ARTICLE_COMMENT))) { %>
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
                                    id="body"
                                    name="body"
                                    rows=5 cols=50
                                    wrap
                                 ></textarea>
                             </p>
                             <p>
                                 <input class="my-button" id="submit" type="submit" value="<%=language.getText("Insert comment") %>" name="submit">
                             </p>
                         </form>
                    <% } %>
                    
                </div> <!-- /content-main-in -->
            </div> <!-- /content-main -->
            <hr class="noscreen" />
        </div> <!-- /content -->

        <jsp:include page="Article__menu.jsp" />
    
    </div> <!-- /cols -->

<jsp:include page="_footer.jsp" />