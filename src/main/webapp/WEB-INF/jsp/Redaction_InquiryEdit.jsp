<%-- 
    Document   : Redaction_InquiryEdit
    Created on : 1.7.2011, 23:12:13
    Author     : berk
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@page import="cz.svjis.bean.InquiryOption"%>
<%@page import="cz.svjis.common.JspSnippets"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.Date"%>

<jsp:useBean id="language" scope="session" class="cz.svjis.bean.Language" />
<jsp:useBean id="user" scope="session" class="cz.svjis.bean.User" />
<jsp:useBean id="inquiry" scope="request" class="cz.svjis.bean.Inquiry" />
<jsp:useBean id="message" scope="request" class="java.lang.String" />

<jsp:include page="_header.jsp" />
<jsp:include page="_tray.jsp" />

    <!-- Columns -->
    <div id="cols" class="box">

        <!-- Content -->
        <div id="content">
            <div id="content-main">
                <div id="content-main-in">
                    <h1 class="page-title"><%=language.getText("Inquiry") %></h1>
                    <strong class="message"><%=message %></strong>
                    <form action="Dispatcher" method="post">
                        <input type="hidden" name="page" value="redactionInquirySave">
                        <input type="hidden" name="id" value="<%=inquiry.getId() %>">
                        
                        <p>
                        <%=language.getText("Description") %><br>
                        <textarea
                            class="common-textarea"
                            id="desc-textarea"
                            name="description"
                            rows=5 cols=80
                            maxlength="250"
                            wrap
                            ><%=inquiry.getDescription() %></textarea>
                        </p>
                        
                        <fieldset>
                            <legend id="tbl-desc"><%=language.getText("Options") %></legend>
                            <table border="0" aria-describedby="tbl-desc">
                                <tbody id="opt_parent">
                            <%
                                int i = 1;
                                for (InquiryOption io: inquiry.getOptionList()) {
                            %>
                                    <tr id="opt_<%=i %>">
                                        <th scope="row" style="text-align: left"><%=language.getText("Option") %>&nbsp;<%=i %>:&nbsp;</th>
                                        <td>
                                            <input type="hidden" name="oid_<%=i %>" value="<%=io.getId() %>">
                                            <input class="common-input" id="o<%=i %>-input" type="text" name="o_<%=i %>" size="50" maxlength="250" value="<%=io.getDescription() %>">
                                        </td>
                                        <% if (inquiry.getCount() == 0) { %>
                                        <td>&nbsp;<a href="Dispatcher?page=redactionInquiryOptionDelete&id=<%=io.getId() %>"><img src="gfx/delete.png" border="0" title="<%=language.getText("Delete") %>" alt="<%=language.getText("Delete") %>"></td>
                                        <% } else { %>
                                        <td style="text-align: right"><%=io.getCount() %></td>
                                        <% } %>
                                    </tr>
                            <%
                                    i++;
                                }
                            %>
                                    <tr id="opt_<%=i %>">
                                        <th scope="row" style="text-align: left"><%=language.getText("Option") %>&nbsp;<%=i %>:&nbsp;</th>
                                        <td>
                                            <input type="hidden" name="oid_<%=i %>" value="0">
                                            <input class="common-input" id="o<%=i %>-input" type="text" name="o_<%=i %>" size="50" maxlength="250" value="">
                                        </td>
                                        <td>&nbsp;</td>
                                    </tr>
                                </tbody>
                            </table>

                            <div id="add-remove-buttons" class="container" style="display: none;">
                                <div class="row">
                                    <div class="left">
                                        <p><input id="add-option" type="button" value="<%=language.getText("Add option") %>" onclick="addOption();" /></p>
                                    </div>
                                    <div class="left">
                                        <p><input id="remove-option" type="button" value="<%=language.getText("Remove option") %>" onclick="removeOption();" disabled /></p>
                                    </div>
                                </div>
                            </div>
                        </fieldset>
                        
                        <fieldset>
                            <legend><%=language.getText("Properties") %></legend>
                            <p>
                                <label class="common-label" id="start-label" for="start-input"><%=language.getText("Starting date") %></label>
                                <input class="common-input" id="start-input" type="text" name="startingDate" value="<%=(inquiry.getStartingDate() != null) ? JspSnippets.renderDate(inquiry.getStartingDate()) : JspSnippets.renderDate(new Date()) %>">
                            </p>
                            <p>
                                <label class="common-label" id="end-label" for="end-input"><%=language.getText("Ending date") %></label>
                                <input class="common-input" id="end-input" type="text" name="endingDate" value="<%=(inquiry.getEndingDate() != null) ? JspSnippets.renderDate(inquiry.getEndingDate()) : JspSnippets.renderDate(new Date()) %>">
                            </p>
                            <p>
                                <label class="common-label" id="publish-label" for="publish-input"><%=language.getText("Publish") %></label>
                                <input class="common-input" id="publish-input" type="checkbox" name="publish" <%=(inquiry.isEnabled()) ? "checked" : "" %>>
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

    <script src="js/Redaction_InquiryEdit.js"></script>
<jsp:include page="_footer.jsp" />
