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
<jsp:useBean id="roleIds" scope="request" class="java.lang.String" />
<jsp:useBean id="searchKey" scope="request" class="java.lang.String" />

<jsp:include page="_header.jsp" />
<jsp:include page="_tray.jsp" />

    <!-- Columns -->
    <div id="cols" class="box">

        <!-- Content -->
        <div id="content">
            <div id="content-main">
                <div id="content-main-in">
                    <h1 class="page-title" id="tbl-desc"><%=language.getText("Article list") %></h1>
                    
                    <div class="container">
                        <div class="row">
                            
                            <div class="left">
                                <p>[<a href="Dispatcher?page=redactionArticleEdit&id=0"><%=language.getText("Add new article") %></a>]</p>
                            </div>
                            <div class="middle" style="padding-left: 300px">
                                <p>&nbsp;</p>
                            </div>
                            <div class="right">
                                <p><%=language.getText("Role filter") %>:</p>
                            </div>
                            <div class="middle">
                                <p>
                                    <form action="Dispatcher" method="post">
                                        <input type="hidden" name="page" value="redactionArticleList" />
                                        <select name='roleId' onchange='this.form.submit()'>
                                            <option value="0"><%=language.getText("all") %></option>
                                            <%
                                                int roleId = Integer.valueOf(roleIds);
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
                                </p>
                            </div>
                            
                        </div>
                    </div>

                    <table class="list" width="100%" aria-describedby="tbl-desc">
                        <tr>
                            <th class="list" scope="col">&nbsp;</th>
                            <th class="list" scope="col">&nbsp;</th>
                            <th class="list" scope="col">&nbsp;</th>
                            <th class="list" scope="col"><%=language.getText("Article") %></th>
                            <th class="list" scope="col"><%=language.getText("Menu") %></th>
                            <th class="list" scope="col"><%=language.getText("Author") %></th>
                            <th class="list" scope="col"><%=language.getText("Date") %></th>
                            <th class="list" scope="col"><%=language.getText("Published") %></th>
                        </tr>
                    <%
                        String search = "";
                        if (searchKey != null) {
                            search = "&search=" + searchKey;
                        }

                        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
                        Iterator articleListI = articleList.iterator();
                        while (articleListI.hasNext()) {
                            Article a = (Article) articleListI.next();
                    %>
                        <tr>
                            <td class="list"><a href="Dispatcher?page=articleDetail&id=<%=a.getId() %><%=search %>"><img src="gfx/find.png" border="0" title="View" alt="View"></a></td>
                            <td class="list"><a href="Dispatcher?page=redactionArticleEdit&id=<%=a.getId() %>"><img src="gfx/pencil.png" border="0" title="<%=language.getText("Edit") %>" alt="<%=language.getText("Edit") %>"></a></td>
                            <td class="list"><a href="Dispatcher?page=redactionArticleSendNotifications&id=<%=a.getId() %>"><img src="gfx/email_open_image.png" border="0" title="<%=language.getText("Send notifications") %>" alt="<%=language.getText("Send notifications") %>"></td>
                            <td class="list"><%=HttpUtils.highlight(a.getHeader(), request.getParameter("search")) %></td>
                            <td class="list"><%=a.getMenuNodeDescription() %></td>
                            <td class="list"><%=a.getAuthor().getFullName(false) %></td>
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
                        if (!roleIds.equals("0")) {
                            roleFilter = "&roleId=" + roleIds;
                        }

                        for (SliderItem item: slider.getItemList()) {
                            if (item.isCurrent()) {
                                out.println("<b>" + item.getLabel() + "</b>&nbsp;");
                            } else {
                                out.println("<a href=\"Dispatcher?page=" + slider.getPageId() + "&section=0&pageNo=" + item.getPage() + roleFilter + search +"\">" + item.getLabel() + "</a>&nbsp;");
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
