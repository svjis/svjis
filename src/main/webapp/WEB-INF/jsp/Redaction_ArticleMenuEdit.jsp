<%-- 
    Document   : Redaction_ArticleMenuEdit
    Created on : 8.7.2011, 13:11:41
    Author     : berk
--%>

<%@page import="cz.svjis.bean.MenuNode"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="language" scope="session" class="cz.svjis.bean.Language" />
<jsp:useBean id="menuNode" scope="request" class="cz.svjis.bean.MenuNode" />
<jsp:useBean id="menu" scope="request" class="cz.svjis.bean.Menu" />

<jsp:include page="_header.jsp" />
<jsp:include page="_tray.jsp" />

    <!-- Columns -->
    <div id="cols" class="box">

        <!-- Content -->
        <div id="content">
            <div id="content-main">
                <div id="content-main-in">
                    <h1 class="page-title"><%=language.getText("Article menu") %></h1>
                    <form action="Dispatcher" method="post">
                        <input type="hidden" name="page" value="redactionArticleMenuSave">
                        <input type="hidden" name="id" value="<%=menuNode.getId() %>">
                        
                        <fieldset>
                            <legend><%=language.getText("Article menu node") %></legend>
                            <p>
                                <label class="common-label" id="desc-label" for="desc-input"><%=language.getText("Description") %></label>
                                <input class="common-input" id="desc-input" type="text" name="description" maxlength="50" value="<%=menuNode.getDescription() %>">
                            </p>
                            <p>
                                <label class="common-label" id="parent-label" for="parent-input"><%=language.getText("Parent node") %></label>
                                <select class="common-input" id="parent-input" name="parent">
                                    <option value="0"><%=language.getText("(top level)") %></option>
                                    <%=menu.writeOptions(menu.getMenu(), 0, menuNode.getParentId()) %>
                                </select>
                            </p>
                            <p>
                                <label class="common-label" id="hide-label" for="hide-input"><%=language.getText("Hide") %></label>
                                <input class="common-input" id="hide-input" type="checkbox" name="hide" <%=(menuNode.isHide()) ? "checked" : "" %> />
                            </p>
                        </fieldset>
                        <p>
                            <input class="my-button" id="submit" type="submit" value="<%=language.getText("Save") %>" />
                        </p>
                    </form>
                </div> <!-- /content-main-in -->
            </div> <!-- /content-main -->
            <hr class="noscreen" />
        </div> <!-- /content -->

        <jsp:include page="Redaction__menu.jsp" />
    
    </div> <!-- /cols -->

<jsp:include page="_footer.jsp" />
