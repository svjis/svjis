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
                            <table id="opt_parent" border="0" aria-describedby="tbl-desc">
                            <%
                                int i = 1;
                                for (InquiryOption io: inquiry.getOptionList()) {
                            %>
                                    <tr id="opt_<%=i %>">
                                        <th width="20%" scope="row" style="text-align: left"><%=language.getText("Option") %>&nbsp;<%=i %>:&nbsp;</th>
                                        <td width="60%">
                                            <input type="hidden" name="oid_<%=i %>" value="<%=io.getId() %>">
                                            <input id="common-input" type="text" name="o_<%=i %>" size="50" maxlength="250" value="<%=io.getDescription() %>">
                                        </td>
                                        <% if (inquiry.getCount() == 0) { %>
                                        <td width="20%">&nbsp;<a href="Dispatcher?page=redactionInquiryOptionDelete&id=<%=io.getId() %>"><img src="gfx/delete.png" border="0" title="<%=language.getText("Delete") %>" alt="<%=language.getText("Delete") %>"></td>
                                        <% } else { %>
                                        <td width="20%" style="text-align: right"><%=io.getCount() %></td>
                                        <% } %>
                                    </tr>
                            <%
                                    i++;
                                }

                                if (i == 1) {
                            %>
                                    <tr id="opt_<%=i %>">
                                        <th width="20%" scope="row" style="text-align: left"><%=language.getText("Option") %>&nbsp;<%=i %>:&nbsp;</th>
                                        <td width="60%">
                                            <input type="hidden" name="oid_<%=i %>" value="0">
                                            <input id="common-input" type="text" name="o_<%=i %>" size="50" maxlength="250" value="">
                                        </td>
                                        <td width="20%">&nbsp;</td>
                                    </tr>
                            <%
                                    i++;
                                }
                            %>
                            </table>

                            <div class="container">
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

    <script>
        "use strict";

        function getLastOptionNo() {
            var i = 0;
            var p = null;
        
            do {
                i++;
                p = document.getElementById('opt_' + i);
            } while (p !== null);
            
            return i - 1;
        }
        
        function addOption() {
            var lastOptionNo = getLastOptionNo();
            lastOptionNo++;
            var html =  '    <th width="20%" scope="row" style="text-align: left"><%=language.getText("Option") %>&nbsp;' + lastOptionNo + ':&nbsp;</th>\n' +
                        '    <td width="60%">\n' +
                        '        <input type="hidden" name="oid_' + lastOptionNo + '" value="0">\n' +
                        '        <input id="common-input" type="text" name="o_' + lastOptionNo + '" size="50" maxlength="250" value="">\n' +
                        '    </td>\n' +
                        '    <td width="20%">&nbsp;</td>\n';
            
            var p = document.getElementById('opt_parent');
            var newElement = document.createElement('tr');
            newElement.setAttribute('id', 'opt_' + lastOptionNo);
            newElement.innerHTML = html;
            p.appendChild(newElement);
            
            var button = document.getElementById('remove-option');
            button.disabled = false;
        }

        function removeOption() {
            var lastOptionNo = getLastOptionNo();
            var noDelete = <%=i-1 %>;
            if (lastOptionNo !== noDelete) {
                var element = document.getElementById('opt_' + lastOptionNo);
                element.parentNode.removeChild(element);
            }
            if (noDelete === (lastOptionNo - 1)) {
                var button = document.getElementById('remove-option');
                button.disabled = true;
            }
        }

    </script>
<jsp:include page="_footer.jsp" />
