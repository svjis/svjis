<%-- 
    Document   : Administration_messageList
    Created on : 9.11.2012, 11:38:08
    Author     : berk
--%>

<%@page import="java.text.SimpleDateFormat"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="language" scope="session" class="cz.svjis.bean.Language" />
<jsp:useBean id="messageList" scope="request" class="java.util.ArrayList" />

<jsp:include page="_header.jsp" />
<jsp:include page="_tray.jsp" />

    <!-- Columns -->
    <div id="cols" class="box">

        <!-- Content -->
        <div id="content">
            <div id="content-main">
                <div id="content-main-in">
                    <h1 class="page-title"><%=language.getText("Message list") %></h1>
                    
                    <table class="list">
                        <tr>
                            <th class="list">&nbsp;</th>
                            <th class="list"><%=language.getText("Recipient") %></th>
                            <th class="list"><%=language.getText("Subject") %></th>
                            <th class="list"><%=language.getText("Time of creation") %></th>
                        </tr>
                    <%
                    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyy HH:mm");
                    int i = 0;
                    java.util.Iterator<cz.svjis.bean.Message> messageI = messageList.iterator();
                    while (messageI.hasNext()) {
                        cz.svjis.bean.Message m = messageI.next();
                    %>
                        <tr>
                            <td class="list" style="text-align: right"><%=++i %></td>
                            <td class="list"><%=m.getRecipient() %></td>
                            <td class="list"><%=m.getSubject() %></td>
                            <td class="list" style="text-align: right"><%=sdf.format(m.getCreationTime()) %></td>
                        </tr>
                    <%
                    }
                    %>
                    </table>
                </div> <!-- /content-main-in -->
            </div> <!-- /content-main -->
            <hr class="noscreen" />
        </div> <!-- /content -->

        <jsp:include page="Administration__menu.jsp" />
    
    </div> <!-- /cols -->

<jsp:include page="_footer.jsp" />
