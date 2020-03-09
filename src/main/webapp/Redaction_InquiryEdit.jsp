<%-- 
    Document   : Redaction_InquiryEdit
    Created on : 1.7.2011, 23:12:13
    Author     : berk
--%>

<%@page import="cz.svjis.bean.InquiryOption"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="language" scope="session" class="cz.svjis.bean.Language" />
<jsp:useBean id="user" scope="session" class="cz.svjis.bean.User" />
<jsp:useBean id="inquiry" scope="request" class="cz.svjis.bean.Inquiry" />

<jsp:include page="_header.jsp" />
<jsp:include page="_tray.jsp" />

<%
    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
%>

    <!-- Columns -->
    <div id="cols" class="box">

        <!-- Content -->
        <div id="content">
            <div id="content-main">
                <div id="content-main-in">
                    <h1 class="page-title"><%=language.getText("Inquiry") %></h1>
                    <form action="Dispatcher" method="post">
                        <input type="hidden" name="page" value="redactionInquirySave">
                        <input type="hidden" name="id" value="<%=inquiry.getId() %>">
                        
                        <p>
                        <%=language.getText("Description") %><br>
                        <textarea
                            name="description"
                            id="common-textarea"
                            rows=5 cols=80
                            maxlength="250"
                            wrap
                            ><%=inquiry.getDescription() %></textarea>
                        </p>
                        
                        <fieldset>
                            <legend id="tbl-desc"><%=language.getText("Options") %></legend>
                            <table border="0" aria-describedby="tbl-desc">
                            <%
                                int i = 0;
                                Iterator<InquiryOption> ioI = inquiry.getOptionList().iterator();
                                while (ioI.hasNext()) {
                                    InquiryOption io = ioI.next();
                            %>
                                <tr>
                                    <th width="10%" scope="row" style="text-align: left"><%=++i %></th>
                                    <td width="80%">
                                        <input type="hidden" name="oid_<%=i %>" value="<%=io.getId() %>">
                                        <input id="common-input" type="text" name="o_<%=i %>" size="50" maxlength="250" value="<%=io.getDescription() %>">
                                    </td>
                                    <% if (inquiry.getCount() == 0) { %>
                                    <td width="10%"><a href="Dispatcher?page=redactionInquiryOptionDelete&id=<%=io.getId() %>"><img src="gfx/delete.png" border="0" title="<%=language.getText("Delete") %>" alt="<%=language.getText("Delete") %>"></td>
                                    <% } else { %>
                                    <td width="10%" style="text-align: right"><%=io.getCount() %></td>
                                    <% } %>
                                </tr>
                            <%
                               }
                            %>
                                <tr>
                                    <th width="10%" scope="row" style="text-align: left"><%=++i %></th>
                                    <td width="80%">
                                        <input type="hidden" name="oid_<%=i %>" value="0">
                                        <input id="common-input" type="text" name="o_<%=i %>" size="50" maxlength="250" value="">
                                    </td>
                                    <td width="10%">&nbsp;</td>
                                </tr>
                            </table>
                        </fieldset>
                        
                        <fieldset>
                            <legend><%=language.getText("Properties") %></legend>
                            <p>
                                <label id="common-label" for="common-input"><%=language.getText("Starting date") %></label>
                                <input id="common-input" type="text" name="startingDate" value="<%=(inquiry.getStartingDate() != null) ? sdf.format(inquiry.getStartingDate()) : sdf.format(new Date()) %>">
                            </p>
                            <p>
                                <label id="common-label" for="common-input"><%=language.getText("Ending date") %></label>
                                <input id="common-input" type="text" name="endingDate" value="<%=(inquiry.getEndingDate() != null) ? sdf.format(inquiry.getEndingDate()) : sdf.format(new Date()) %>">
                            </p>
                            <p>
                                <label id="common-label" for="common-input"><%=language.getText("Publish") %></label>
                                <input id="common-input" type="checkbox" name="publish" <%=(inquiry.isEnabled()) ? "checked" : "" %>>
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
