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
<%@page import="cz.svjis.servlet.CmdFactory"%>
<%@page import="java.io.File"%>
<%@page import="java.text.SimpleDateFormat"%>
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
                    <h1 class="page-title" id="tbl-desc"><%=language.getText("Adverts") %>: <%=currMenu.getDescription() %></h1>
                                        
                    <form action="Dispatcher" method="post">
                        <input type="hidden" name="page" value="<%=CmdFactory.ADVERT_SAVE %>">
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

                    <form action="Dispatcher?page=<%=CmdFactory.ADVERT_ATTACHMENT_SAVE %>&advertId=<%=advert.getId() %>" enctype="multipart/form-data" method="post">
                        <fieldset>
                            <legend class="hidden-legend"><%=language.getText("General") %></legend>
                            <%
                                if ((advert.getAttachmentList() != null) && (advert.getAttachmentList().size() != 0)) {
                            %>
                            <p>
                            <table class="list" aria-describedby="tbl-desc">
                                <tr>
                                    <th class="list" colspan="3" scope="col"><%=language.getText("File") %></th>
                                </tr>
                                <%
                                for (Attachment a: advert.getAttachmentList()) {
                                    String icon = "gfx/Files_unknown.gif";
                                    String extension = a.getFileName().toLowerCase().substring(a.getFileName().lastIndexOf(".") + 1);
                                    File f = new File(request.getServletContext().getRealPath("/gfx") + "/Files_" + extension + ".gif");
                                    if (f.exists()) {
                                        icon = "gfx/Files_" + extension + ".gif";
                                    }
                                %>
                                <tr>
                                    <td class="list"><img src="<%=icon%>" border="0" alt="<%=a.getFileName() %>"></td>
                                    <td class="list"><a href="Upload?page=download&id=<%=a.getId() %>"><%=a.getFileName() %></a></td>
                                    <td class="list"><a onclick="if (!confirm('<%=language.getText("Really do you want to remove attachment") %> <%=a.getFileName() %> ?')) return false;" href="Dispatcher?page=<%=CmdFactory.ADVERT_ATTACHMENT_DELETE %>&id=<%=a.getId() %>"><%=language.getText("Delete") %></a></td>
                                </tr>
                                <%
                                }
                                %>
                            </table>
                            <p>
                            <%
                               }
                            %>
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
