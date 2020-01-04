<%-- 
    Document   : Redaction_ArticleMenuEdit
    Created on : 8.7.2011, 13:11:41
    Author     : berk
--%>

<%@page import="cz.svjis.bean.MenuNode"%>
<%@page import="java.util.Iterator"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="language" scope="session" class="cz.svjis.bean.Language" />
<jsp:useBean id="menuNode" scope="request" class="cz.svjis.bean.MenuNode" />
<jsp:useBean id="menuNodeList" scope="request" class="java.util.ArrayList" />

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
                                <label id="common-label" for="common-input"><%=language.getText("Description") %></label>
                                <input id="common-input" type="text" name="description" maxlength="50" value="<%=menuNode.getDescription() %>">
                            </p>
                            <p>
                                <label id="common-label" for="common-input"><%=language.getText("Parent node") %></label>
                                <select name="parent" id="common-input">
                                    <option value="0"><%=language.getText("(top level)") %></option>
                                    <%
                                        Iterator<MenuNode> nodeI = menuNodeList.iterator();
                                        while (nodeI.hasNext()) {
                                            MenuNode n = nodeI.next();
                                            if (n.getId() == menuNode.getId()) {
                                                continue;
                                            }
                                    %>
                                        <option value="<%=n.getId() %>" <%=(n.getId() == menuNode.getParentId()) ? "SELECTED" : "" %>><%=n.getDescription() %></option>
                                    <%
                                        }
                                    %>
                                </select>
                            </p>
                        </fieldset>
                        <p>
                            <input class="my-button" type="submit" value="<%=language.getText("Save") %>" />
                        </p>
                    </form>
                </div> <!-- /content-main-in -->
            </div> <!-- /content-main -->
            <hr class="noscreen" />
        </div> <!-- /content -->

        <jsp:include page="Redaction__menu.jsp" />
    
    </div> <!-- /cols -->

<jsp:include page="_footer.jsp" />
