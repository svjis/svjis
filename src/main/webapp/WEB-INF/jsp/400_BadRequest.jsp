<%-- 
    Document   : 400_BadRequest
    Created on : 3.5.2021, 23:50:47
    Author     : jarberan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="message" scope="request" class="java.lang.String" />

<jsp:include page="_header.jsp" />
<jsp:include page="_tray.jsp" />

        <!-- Content -->
        <div id="content-width">
            <div id="content-width-in">
                <h1>400 Bad Request</h1>
                <p><%=message %></p>
                <p>Prosím pokračujte <a href="Dispatcher">zde</a>.</p>
            <hr class="noscreen" />
            </div>
        </div> <!-- /content -->

<jsp:include page="_footer.jsp" />
