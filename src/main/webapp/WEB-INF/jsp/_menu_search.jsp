<%-- 
    Document   : _menu_search
    Created on : 30.12.2019, 10:58:12
    Author     : jarberan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@page import="cz.svjis.servlet.Cmd"%>

<jsp:useBean id="language" scope="session" class="cz.svjis.bean.Language" />
<jsp:useBean id="searchKey" scope="request" class="java.lang.String" />

                <h4 class="margin"><%=language.getText("Search:") %></h4>

                <form action="Dispatcher" method="get">
                    <input type="hidden" name="page" value="<%=Cmd.ARTICLE_SEARCH %>" />
                    <div id="search" class="box">
                        <input type="text" size="20" id="search-input" name="search" value="<%=(searchKey != null) ? searchKey : "" %>" /><input class="my-button" type="submit" id="search-submit" value="<%=language.getText("Search") %>" />
                    </div> <!-- /search -->
                </form>