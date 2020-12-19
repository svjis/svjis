<%-- 
    Document   : _message
    Created on : 22.8.2011, 22:37:47
    Author     : berk
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="language" scope="session" class="cz.svjis.bean.Language" />
<jsp:useBean id="messageHeader" scope="request" class="java.lang.String" />
<jsp:useBean id="message" scope="request" class="java.lang.String" />

<jsp:include page="_header.jsp" />
<jsp:include page="_tray.jsp" />

        <!-- Content -->
        <div id="content-width">
            <div id="content-width-in">
                <h2><%=messageHeader %></h2>
                <p><%=message %></p>
            <hr class="noscreen" />
            </div>
        </div> <!-- /content -->


<jsp:include page="_footer.jsp" />
