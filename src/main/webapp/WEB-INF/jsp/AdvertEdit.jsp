<%-- 
    Document   : AdvertEdit
    Created on : 21.04.2021, 14:40:00
    Author     : jarberan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@page import="cz.svjis.bean.Advert"%>
<%@page import="cz.svjis.bean.AdvertType"%>
<%@page import="cz.svjis.bean.Attachment"%>
<%@page import="cz.svjis.bean.Permission"%>
<%@page import="cz.svjis.common.JspSnippets"%>
<%@page import="cz.svjis.servlet.Cmd"%>
<%@page import="java.util.ArrayList"%>

<jsp:useBean id="language" scope="session" class="cz.svjis.bean.Language" />
<jsp:useBean id="user" scope="session" class="cz.svjis.bean.User" />
<jsp:useBean id="advert" scope="request" class="cz.svjis.bean.Advert" />
<jsp:useBean id="advertTypeList" scope="request" class="java.util.ArrayList" />
<jsp:useBean id="currMenu" scope="request" class="cz.svjis.bean.AdvertType" />

<jsp:include page="_header.jsp" />
<jsp:include page="_tray.jsp" />

    <!-- Columns -->
    <div id="cols" class="box">

        <!-- Content -->
        <div id="content">
            <div id="content-main">
                <div id="content-main-in">
                    <h1 class="page-title"><%=language.getText("Adverts") %>: <%=currMenu.getDescription() %></h1>
                                        
                    <form action="Dispatcher" method="post">
                        <input type="hidden" name="page" value="<%=Cmd.ADVERT_SAVE %>">
                        <input type="hidden" name="id" value="<%=advert.getId()  %>">
                        
                        <p>
                            <%=language.getText("Header") %><br>
                            <input class="common-input" id="header-input" type="text" name="header" maxlength="50" size="80" value="<%=advert.getHeader() %>">
                        </p>
                        
                        <p>
                        <%=language.getText("Type") %><br>
                        <select class="common-input"  id="type-input" name="typeId">
                            <%
                                for (AdvertType t : (ArrayList<AdvertType>) advertTypeList) {
                            %>
                            <option value="<%=t.getId() %>" <%=(t.getId() == advert.getType().getId()) ? "SELECTED" : "" %>><%=t.getDescription() %></option>
                            <%
                                }
                            %>
                        </select>
                        </p>
                        
                        <p>
                        <%=language.getText("Body") %><br>
                        <textarea
                            class="common-textarea"
                            id="body-textarea"
                            name="body"
                            rows=5 cols=80
                            wrap
                            ><%=advert.getBody() %></textarea>
                        </p>
                        
                        <p>
                            <%=language.getText("Phone") %><br>
                            <input class="common-input" id="phone-input" type="text" name="phone" maxlength="30" size="80" value="<%=advert.getPhone() %>">
                        </p>
                        
                        <p>
                            <%=language.getText("E-Mail") %><br>
                            <input class="common-input" id="email-input" type="text" name="e-mail" maxlength="50" size="80" value="<%=advert.geteMail() %>">
                        </p>
                        
                        <p>
                            <%=language.getText("Publish") %><br>
                            <input class="common-input" id="published-input" type="checkbox" name="published" <%=(advert.isPublished()) ? "checked" : "" %>>
                        </p>
                        
                        <p>
                            <input class="my-button" id="submit" type="submit" value="<%=language.getText("Save") %>" />
                        </p>
                    </form>
                    
                    <% if (advert.getId() != 0) { %>
                    <h1 class="page-title" id="tbl-desc"><%=language.getText("Attachments") %></h1>

                    <form action="Dispatcher?page=<%=Cmd.ADVERT_ATT_SAVE %>&advertId=<%=advert.getId() %>" enctype="multipart/form-data" method="post">
                        <fieldset>
                            <legend class="hidden-legend"><%=language.getText("General") %></legend>
                            <%=JspSnippets.renderAttachments(advert.getAttachmentList(), request, "File", Cmd.ADVERT_ATT_DOWNLOAD, Cmd.ADVERT_ATT_DELETE, false, false, true, false, "tbl-desc") %>
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

        <jsp:include page="Advert__menu.jsp" />
    
    </div> <!-- /cols -->

<jsp:include page="_footer.jsp" />
