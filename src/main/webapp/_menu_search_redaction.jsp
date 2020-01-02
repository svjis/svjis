<%-- 
    Document   : _menu_search_redaction
    Created on : 2.1.2020, 13:31:52
    Author     : jarberan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="language" scope="session" class="cz.svjis.bean.Language" />

                <h4 class="margin"><%=language.getText("Search:") %></h4>

                <form action="Dispatcher" method="get">
                    <input type="hidden" name="page" value="redactionArticleSearch" />
                    <div id="search" class="box">
                        <input type="text" size="20" id="search-input" name="search" value="<%=(request.getParameter("search") != null) ? request.getParameter("search") : "" %>" /><input type="submit" id="search-submit" value="<%=language.getText("Search") %>" />
                    </div> <!-- /search -->
                </form>