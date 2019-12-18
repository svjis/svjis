<%-- 
    Document   : _footer
    Created on : 15.6.2011, 16:58:20
    Author     : berk
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="cz.svjis.bean.User" />

    <!-- Footer -->
    <div id="footer">

        <!-- Do you want remove this backlinks? Look at www.nuviotemplates.com/payment.php -->
        <p class="f-right"><a href="http://www.nuviotemplates.com/">Free web templates</a> presented by <a href="http://www.qartin.cz/">QARTIN</a> &ndash; Our tip: <a href="http://last-minute.invia.sk" title="Last Minute">Last Minute</a> <a href="http://wycieczki.invia.pl" title="Wczasy">Wczasy</a></p>
        <!-- Do you want remove this backlinks? Look at www.nuviotemplates.com/payment.php -->

        <p>
            <%
                String link = "%s";
                if (user.isUserLogged()) {
                    link = "<a href=\"https://github.com/svjis/svjis\">%s</a>";
                }
            %>
            System <strong><%= String.format(link, "SVJIS 1.6.0-SNAPSHOT") %></strong>, released 18.12.2019, All Rights Reserved &reg 2007-2019
        </p>

    </div> <!-- /footer -->

</div> <!-- /main -->

</body>
</html>