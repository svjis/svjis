<%-- 
    Document   : Redaction_ArticleList
    Created on : 21.6.2011, 23:10:13
    Author     : berk
--%>

<%@page import="cz.svjis.bean.Article"%>
<%@page import="cz.svjis.bean.SliderItem"%>
<%@page import="cz.svjis.common.HttpUtils"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Iterator"%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="language" scope="session" class="cz.svjis.bean.Language" />
<jsp:useBean id="user" scope="session" class="cz.svjis.bean.User" />
<jsp:useBean id="menu" scope="request" class="cz.svjis.bean.Menu" />
<jsp:useBean id="slider" scope="request" class="cz.svjis.bean.SliderImpl" />
<jsp:useBean id="articleList" scope="request" class="java.util.ArrayList" />
<jsp:useBean id="roleList" scope="request" class="java.util.ArrayList" />

<jsp:include page="_header.jsp" />
<jsp:include page="_tray.jsp" />

    <!-- Columns -->
    <div id="cols" class="box">

        <!-- Content -->
        <div id="content">
            <div id="content-main">
                <div id="content-main-in">
                    <h1 class="page-title"><%=language.getText("Article list") %></h1>
                    <table width="100%">
                        <tr>
                            <td align="left">
                                [<a href="Dispatcher?page=redactionArticleEdit&id=0"><%=language.getText("Add new article") %></a>]<br>
                                </td>
                            <td align="right">
                            <td width="40%">&nbsp;</td>
                            <td align="right">
                                <%=language.getText("Role filter") %>:&nbsp;
                            </td>
                            <td align="right">
                                <form action="Dispatcher" method="post">
                                    <input type="hidden" name="page" value="redactionArticleList" />
                                    <select name='roleId' onchange='this.form.submit()'>
                                        <option value="0"><%=language.getText("all") %></option>
                                        <%
                                            int roleId = Integer.valueOf((request.getParameter("roleId") == null) ? "0" : request.getParameter("roleId"));
                                            java.util.Iterator<cz.svjis.bean.Role> roleI = roleList.iterator();
                                            while (roleI.hasNext()) {
                                                cz.svjis.bean.Role r = roleI.next();
                                                String sel;
                                                if (roleId == r.getId()) {
                                                    sel = "selected";
                                                } else {
                                                    sel = "";
                                                }
                                        %>
                                        <option value="<%=r.getId() %>" <%=sel %>><%=r.getDescription() %></option>
                                        <% } %>
                                        </select>
                                </form>
                            </td>
                        </tr>
                    </table>

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
                        String search = "";
                        if (request.getParameter("search") != null) {
                            search = "&search=" + request.getParameter("search");
                        }

                        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
                        Iterator articleListI = articleList.iterator();
                        while (articleListI.hasNext()) {
                            Article a = (Article) articleListI.next();
                    %>
                        <tr>
                            <td class="list"><a href="Dispatcher?page=articleDetail&id=<%=a.getId() %><%=search %>"><img src="gfx/find.png" border="0" title="View"></a></td>
                            <td class="list"><a href="Dispatcher?page=redactionArticleEdit&id=<%=a.getId() %>"><img src="gfx/pencil.png" border="0" title="<%=language.getText("Edit") %>"></a></td>
                            <td class="list"><a href="Dispatcher?page=redactionArticleSendNotifications&id=<%=a.getId() %>"><img src="gfx/email_open_image.png" border="0" title="<%=language.getText("Send notifications") %>"></td>
                            <td class="list"><%=HttpUtils.highlight(a.getHeader(), request.getParameter("search")) %></td>
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
                    <% if (slider.getTotalNumOfPages() > 1) { %>
                    <strong><%=language.getText("Pages:") %></strong>&nbsp;
                    <%
                        String roleFilter = "";
                        if (request.getParameter("roleId") != null) {
                            roleFilter = "&roleId=" + request.getParameter("roleId");
                        }

                        for (SliderItem item: slider.getItemList()) {
                            if (item.isCurrent()) {
                                out.println("<b>" + item.getLabel() + "</b>&nbsp;");
                            } else {
                                out.println("<a href=\"Dispatcher?page=" + request.getParameter("page") + "&section=0&pageNo=" + item.getPage() + roleFilter + search +"\">" + item.getLabel() + "</a>&nbsp;");
                            }
                        }
                    %>
                    <% } %>
                </div> <!-- /content-main-in -->
            </div> <!-- /content-main -->
            <hr class="noscreen" />
        </div> <!-- /content -->

        <jsp:include page="Redaction__menu.jsp" />
    
    </div> <!-- /cols -->

<jsp:include page="_footer.jsp" />
