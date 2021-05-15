<%-- 
    Document   : Redaction_ArticleEdit
    Created on : 23.6.2011, 16:03:00
    Author     : berk
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="cz.svjis.bean.Attachment"%>
<%@page import="cz.svjis.bean.Role"%>
<%@page import="cz.svjis.bean.Language"%>
<%@page import="cz.svjis.bean.MenuItem"%>
<%@page import="cz.svjis.common.JspSnippets"%>
<%@page import="cz.svjis.servlet.Cmd"%>
<%@page import="java.util.Iterator"%>

<jsp:useBean id="language" scope="session" class="cz.svjis.bean.Language" />
<jsp:useBean id="user" scope="session" class="cz.svjis.bean.User" />
<jsp:useBean id="article" scope="request" class="cz.svjis.bean.Article" />
<jsp:useBean id="languageList" scope="request" class="java.util.ArrayList" />
<jsp:useBean id="roleList" scope="request" class="java.util.ArrayList" />
<jsp:useBean id="menu" scope="request" class="cz.svjis.bean.Menu" />
<jsp:useBean id="message" scope="request" class="java.lang.String" />


<jsp:include page="_header.jsp" />
<jsp:include page="_tray.jsp" />


<!-- TinyMCE -->
<script type="text/javascript" src="js/tiny_mce/tiny_mce.js"></script>
<script type="text/javascript">
	tinyMCE.init({
                language : "<%=language.getText("tiny_mce.language") %>",
                entity_encoding : "raw",
		mode : "textareas",
		theme : "advanced",
                plugins : "table",
                
                // Theme options
		theme_advanced_buttons1 : "bold,italic,underline,strikethrough,|,bullist,numlist,|,justifyleft,justifycenter,justifyright,justifyfull,formatselect,|,link,unlink,|,undo,redo",
		theme_advanced_toolbar_location : "top",
		theme_advanced_toolbar_align : "left",
		theme_advanced_statusbar_location : "bottom",
                
                // Example content CSS (should be your site CSS)
		content_css : "css/main.css"
	});
</script>
<!-- /TinyMCE -->

    <!-- Columns -->
    <div id="cols" class="box">

        <!-- Content -->
        <div id="content">
            <div id="content-main">
                <div id="content-main-in">
                    <h1 class="page-title"><%=language.getText("Article") %></h1>
                    <strong class="message"><%=message %></strong>
                    <form action="Dispatcher" method="post">
                        <input type="hidden" name="page" value="<%=Cmd.REDACTION_ARTICLE_SAVE %>">
                        <input type="hidden" name="id" value="<%=article.getId() %>">
                        <input type="hidden" name="authorId" value="<%=article.getAuthorId() %>">
                        <input type="hidden" name="creationDate" value="<%=JspSnippets.renderDateTime(article.getCreationDate()) %>">

                        <p>
                            <%=language.getText("Header") %><br>
                            <input class="common-input" id="header-input" type="text" name="header" maxlength="50" size="80" value="<%=article.getHeader() %>">
                        </p>
                        
                        <p>
                        <%=language.getText("Description") %><br>
                        <textarea
                            name="description"
                            id="common-textarea-desc"
                            rows=10 cols=80
                            wrap
                            ><%=article.getDescription() %></textarea>
                        </p>
                        <p>
                        <%=language.getText("Body") %><br>
                        <textarea
                            name="body"
                            id="common-textarea-text"
                            rows=20 cols=80
                            wrap
                            ><%=article.getBody() %></textarea>
                        </p>
                        <fieldset>
                            <legend><%=language.getText("Properties") %></legend>
                            <p>
                                <label class="common-label" id="menu-label" for="menu-input"><%=language.getText("Menu") %></label>
                                <select class="common-input" id="menu-input" name="menuId">
                                    <%=menu.writeOptions(menu.getMenu(), 0, article.getMenuNodeId()) %>
                                </select>
                            </p>
                            <p>
                                <label class="common-label" id="lang-label" for="lang-input"><%=language.getText("Language") %></label>
                                <select class="common-input" id="lang-input" name="language">
                                    <%
                                        Iterator langI = languageList.iterator();
                                        while (langI.hasNext()) {
                                            Language l = (Language) langI.next();
                                    %>
                                        <option value="<%=l.getId() %>" <%=(l.getId() == article.getLanguageId()) ? "SELECTED" : "" %>><%=l.getDescription() %></option>
                                    <%
                                        }
                                    %>
                                </select>
                            </p>
                            <p>
                                <label class="common-label" id="comments-label" for="comments-input"><%=language.getText("Enable comments") %></label>
                                <input class="common-input" id="comments-input" type="checkbox" name="commentsAllowed" <%=(article.isCommentsAllowed()) ? "checked" : "" %>>
                            </p>
                            <p>
                                <label class="common-label" id="publish-label" for="publish-input"><%=language.getText("Publish article") %></label>
                                <input class="common-input" id="publish-input" type="checkbox" name="publish" <%=(article.isPublished()) ? "checked" : "" %>>
                            </p>
                        </fieldset>
                        <fieldset>
                            <legend><%=language.getText("Visible for") %></legend>
                            <%
                                Iterator roleI = roleList.iterator();
                                while (roleI.hasNext()) {
                                    Role r = (Role) roleI.next();
                            %>
                            <p>
                                <label class="common-label" id="r<%=r.getId() %>-label" for="r<%=r.getId() %>-input"><%=r.getDescription() %></label>
                                <input class="common-input" id="r<%=r.getId() %>-input" type="checkbox" name="r_<%=r.getId() %>" <%=(article.getRoles().get(new Integer(r.getId())) != null) ? "checked" : "" %> />
                            </p>
                            <%
                                }
                            %>        
                        </fieldset>
                        <p>
                            <input class="my-button" id="submit" type="submit" value="<%=language.getText("Save") %>" />
                        </p>
                    </form>
                        
                    <% if (article.getId() != 0) { %>
                    <h1 class="page-title" id="tbl-desc"><%=language.getText("Attachments") %></h1>

                    <form action="Dispatcher?page=<%=Cmd.REDACTION_ARTICLE_ATT_SAVE %>&articleId=<%=article.getId() %>" enctype="multipart/form-data" method="post">
                        <fieldset>
                            <legend class="hidden-legend"><%=language.getText("General") %></legend>
                            <%=JspSnippets.renderAttachments(article.getAttachmentList(), request, "File", "download", "redactionArticleAttachmentDelete", false, false, true, false, "tbl-desc") %>
                            <p>
                                <input id="file-upload" type="file" name="attachment" size="40">
                                <input id="file-submit" type="submit" value="<%=language.getText("Insert attachment") %>">
                            </p>
                        </fieldset>
                    </form>
                    <% } %>
                        
                </div> <!-- /content-main-in -->
            </div> <!-- /content-main -->
            <hr class="noscreen" />
        </div> <!-- /content -->

        <jsp:include page="Redaction__menu.jsp" />
    
    </div> <!-- /cols -->

<jsp:include page="_footer.jsp" />
