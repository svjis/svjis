<%-- 
    Document   : Redaction_InquiryList
    Created on : 1.7.2011, 16:50:13
    Author     : berk
--%>

<%@page import="cz.svjis.bean.SliderItem"%>
<%@page import="cz.svjis.bean.Inquiry"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="language" scope="session" class="cz.svjis.bean.Language" />
<jsp:useBean id="inquiryList" scope="request" class="java.util.ArrayList" />
<jsp:useBean id="slider" scope="request" class="cz.svjis.bean.SliderImpl" />

<jsp:include page="_header.jsp" />
<jsp:include page="_tray.jsp" />

    <!-- Columns -->
    <div id="cols" class="box">

        <!-- Content -->
        <div id="content">
            <div id="content-main">
                <div id="content-main-in">
                    <h1 class="page-title" id="tbl-desc"><%=language.getText("Inquiry list") %></h1>
                    [<a href="Dispatcher?page=redactionInquiryEdit&id=0"><%=language.getText("Add new inquiry") %></a>]<br>

                    <table class="list" width="100%" aria-describedby="tbl-desc">
                        <tr>
                            <th class="list" scope="col">&nbsp;</th>
                            <th class="list" scope="col">&nbsp;</th>
                            <th class="list" scope="col">&nbsp;</th>
                            <th class="list" scope="col"><%=language.getText("Description") %></th>
                            <th class="list" scope="col"><%=language.getText("Starting date") %></th>
                            <th class="list" scope="col"><%=language.getText("Ending date") %></th>
                            <th class="list" scope="col"><%=language.getText("Author") %></th>
                            <th class="list" scope="col"><%=language.getText("Published") %></th>
                        </tr>
                    <%
                        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
                        Iterator inquiryListI = inquiryList.iterator();
                        while (inquiryListI.hasNext()) {
                            Inquiry i = (Inquiry) inquiryListI.next();
                    %>
                        <tr>
                            <td class="list"><a href="Dispatcher?page=redactionInquiryEdit&id=<%=i.getId() %>"><img src="gfx/pencil.png" border="0" title="<%=language.getText("Edit") %>" alt="<%=language.getText("Edit") %>"></a></td>
                            <td class="list"><a href="Dispatcher?page=redactionInquiryLog&id=<%=i.getId() %>"><img src="gfx/chart_bar.png" border="0" title="<%=language.getText("Log") %>" alt="<%=language.getText("Log") %>"></a></td>
                            <td class="list"><a href="Upload?page=exportInquiryLogToXls&id=<%=i.getId() %>"><img src="gfx/Files_xls.gif" border="0" title="<%=language.getText("Export to Excel") %>" alt="<%=language.getText("Export to Excel") %>"></a></td>
                            <td class="list"><%=i.getDescription() %></td>
                            <td class="list"><%=sdf.format(i.getStartingDate()) %></td>
                            <td class="list"><%=sdf.format(i.getEndingDate()) %></td>
                            <td class="list"><%=i.getUser().getFullName(false) %></td>
                            <td class="list"><%=(i.isEnabled()) ? language.getText("yes") : language.getText("no") %></td>
                        </tr>
                    <%
                        }
                    %>
                    </table>
                    
                    <p class="t-left">
                        <% if (slider.getTotalNumOfPages() > 1) { %>
                        <strong><%=language.getText("Pages:") %></strong>&nbsp;
                        <%
                        String pageId = "page=" + slider.getPageId() + "&";
                        for (SliderItem item : slider.getItemList()) {
                            if (item.isCurrent()) {
                                out.println("<b>" + item.getLabel() + "</b>&nbsp;");
                            } else {
                                out.println("<a href=\"Dispatcher?" + pageId + "pageNo=" + item.getPage() + "\">" + item.getLabel() + "</a>&nbsp;");
                            }
                        }
                        %>
                        
                        <% } %>
                    </p>
                    
                </div> <!-- /content-main-in -->
            </div> <!-- /content-main -->
            <hr class="noscreen" />
        </div> <!-- /content -->

        <jsp:include page="Redaction__menu.jsp" />
    
    </div> <!-- /cols -->

<jsp:include page="_footer.jsp" />
