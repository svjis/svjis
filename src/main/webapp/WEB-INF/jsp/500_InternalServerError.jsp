<%-- 
    Document   : 500_InternalServerError
    Created on : 3.5.2021, 23:53:08
    Author     : berk
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<jsp:include page="_header.jsp" />
<jsp:include page="_tray.jsp" />

        <!-- Content -->
        <div id="content-width">
            <div id="content-width-in">
                <h1>500 Internal Server Error</h1>
                <p><strong>Litujeme ale v aplikaci nastala chyba.</strong></p>
        
		        <p>Report o chybě byl odeslán správci webu.<br>
		        Prosím pokračujte <a href="Dispatcher">zde</a>.</p>
            <hr class="noscreen" />
            </div>
        </div> <!-- /content -->

<jsp:include page="_footer.jsp" />
