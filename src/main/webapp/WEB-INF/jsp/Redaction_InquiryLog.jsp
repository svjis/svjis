<%-- 
    Document   : Redaction_InquiryLog
    Created on : 14.12.2013, 22:22:19
    Author     : jaroslav_b
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@page import="java.util.ArrayList"%>
<%@page import="cz.svjis.common.JspSnippets"%>
<%@page import="cz.svjis.bean.InquiryLog"%>
<%@page import="cz.svjis.bean.InquiryOption"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.Date"%>

<jsp:useBean id="language" scope="session" class="cz.svjis.bean.Language" />
<jsp:useBean id="user" scope="session" class="cz.svjis.bean.User" />
<jsp:useBean id="inquiry" scope="request" class="cz.svjis.bean.Inquiry" />
<jsp:useBean id="log" scope="request" class="java.util.ArrayList" />

<jsp:include page="_header.jsp" />
<jsp:include page="_tray.jsp" />

    <!-- Columns -->
    <div id="cols" class="box">

        <!-- Content -->
        <div id="content">
            <div id="content-main">
                <div id="content-main-in">
                    
                    <h1 class="page-title"><%=language.getText("Inquiry") %></h1>
                        
                    <p id="tbl-desc"><%=inquiry.getDescription() %></p>

                    <p>
                    <table class="list" aria-describedby="tbl-desc">
                        <tr>
                            <th class="list" scope="col">&nbsp;</th>
                            <th class="list" scope="col"><%=language.getText("Description") %></th>
                            <th class="list" scope="col"><%=language.getText("Votes") %></th>
                        </tr>
                        <%
                            int i = 0;
                            for (InquiryOption io: inquiry.getOptionList()) {
                        %>
                        <tr>
                            <td><%=++i %></td>
                            <td><%=io.getDescription() %></td>
                            <td style="text-align: right"><%=io.getCount() %></td>
                        </tr>
                        <%
                           }
                        %>
                    </table>
                    </p>

                    <h2 class="page-title" id="tbl2-desc"><%=language.getText("Inquiry log") %></h2>
                    
                    <p>
                    <table class="list" aria-describedby="tbl2-desc">
                        <tr>
                            <th class="list" scope="col">&nbsp;</th>
                            <th class="list" scope="col"><%=language.getText("Time") %></th>
                            <th class="list" scope="col"><%=language.getText("User") %></th>
                            <th class="list" scope="col"><%=language.getText("Option") %></th>
                        </tr>
                        <%
                            i = 0;
                            for (InquiryLog il: (ArrayList<InquiryLog>)log) {
                        %>
                        <tr>
                            <td><%=++i %></td>
                            <td style="text-align: right"><%=JspSnippets.renderDateTime(il.getTime()) %></td>
                            <td><%=il.getUser() %></td>
                            <td><%=il.getOptionDescription() %></td>
                        </tr>
                        <%
                           }
                        %>
                    </table>
                    </p>
                        
                </div> <!-- /content-main-in -->
            </div> <!-- /content-main -->
            <hr class="noscreen" />
        </div> <!-- /content -->

        <jsp:include page="Redaction__menu.jsp" />
    
    </div> <!-- /cols -->

<jsp:include page="_footer.jsp" />
