<%-- 
    Document   : AdvertList
    Created on : 18.04.2021, 19:18:00
    Author     : jarberan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@page import="cz.svjis.bean.Advert"%>
<%@page import="cz.svjis.bean.AdvertType"%>
<%@page import="cz.svjis.bean.Permission"%>
<%@page import="cz.svjis.common.JspSnippets"%>
<%@page import="cz.svjis.servlet.CmdFactory"%>
<%@page import="cz.svjis.servlet.CmdFactoryUpload"%>
<%@page import="java.util.ArrayList"%>

<jsp:useBean id="language" scope="session" class="cz.svjis.bean.Language" />
<jsp:useBean id="user" scope="session" class="cz.svjis.bean.User" />
<jsp:useBean id="advertList" scope="request" class="java.util.ArrayList" />
<jsp:useBean id="menuId" scope="request" class="java.lang.String" />
<jsp:useBean id="slider" scope="request" class="cz.svjis.bean.SliderImpl" />
<jsp:useBean id="currMenu" scope="request" class="cz.svjis.bean.AdvertType" />

<jsp:include page="_header.jsp" />
<jsp:include page="_tray.jsp" />

    <!-- Columns -->
    <div id="cols" class="box">

        <!-- Content -->
        <div id="content">
            <div id="content-main">
                <div id="content-main-in">
                    <h1 class="page-title" id="tbl-desc"><%=language.getText("Adverts") %>: <%=currMenu.getDescription() %></h1>
                    
                    <% if (user.hasPermission(Permission.CAN_INSERT_ADVERT)) { %>
                    <a href="Dispatcher?page=<%=CmdFactory.ADVERT_EDIT %>&id=0" class="create-button"><%=language.getText("Create new advert") %></a><br>
                    <% } %>

                    <%
	                for (Advert a: (ArrayList<Advert>) advertList) {
	                    String stl = (!a.isPublished()) ? "text-decoration: line-through;" : "";
	                %>
                    <!-- advert -->&nbsp;
                    <div id="advert" class="box">
                        <div id="advert-desc">
                            <h2 style="<%=stl %>"><%=a.getHeader() %></h2>
                            <p class="info"><%=language.getText("Date") %>: <strong><%=JspSnippets.renderDate(a.getCreationDate()) %></strong> <%=language.getText("Author") %>: <strong><%=a.getUser().getFullName(false) %></strong>
                            <% if (a.getUser().getId() == user.getId()) { %>
                                &nbsp;[<a href="Dispatcher?page=<%=CmdFactory.ADVERT_EDIT %>&id=<%=a.getId() %>"><%=language.getText("Edit") %></a>]
                            <% } %>
                            </p>
                            <p class="nomb"  style="<%=stl %>"><%=a.getBody().replace("\n", "<br>") %></p>
                            <% if (!a.getAttachmentList().isEmpty()) { %>
                            <p class="nomb"><%=JspSnippets.renderAttachments(a.getAttachmentList(), request, CmdFactoryUpload.ADVERT_ATTACHMENT_DOWNLOAD) %></p>
                            <% } %>
                            <p class="contact"  style="<%=stl %>">
                                <% if ((a.getPhone() != null) && !a.getPhone().trim().equals("")) { %><%=language.getText("Phone") %>:&nbsp;<strong><%=a.getPhone() %></strong>&nbsp;<% } %> 
                                <% if ((a.geteMail() != null) && !a.geteMail().trim().equals("")) { %><%=language.getText("E-Mail") %>:&nbsp;<strong><a href="mailto:<%=a.geteMail() %>"><%=a.geteMail() %></a></strong><% } %>
                            </p>
                        </div> <!-- /advert-desc -->
                    </div> <!-- /advert -->
	                <%    
	                }
	                %>
                    
                    <p class="t-left">
                        <%=JspSnippets.renderPaginator(slider, null, null, request) %>
                    </p>
                    
                </div> <!-- /content-main-in -->
            </div> <!-- /content-main -->
            <hr class="noscreen" />
        </div> <!-- /content -->

        <jsp:include page="Advert__menu.jsp" />
    
    </div> <!-- /cols -->

<jsp:include page="_footer.jsp" />
