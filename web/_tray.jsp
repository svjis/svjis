<%-- 
    Document   : _tray
    Created on : 15.6.2011, 17:05:04
    Author     : berk
--%>

<%@page import="cz.svjis.bean.SystemMenuEntry"%>
<%@page import="java.util.Iterator"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="cz.svjis.bean.User" />
<jsp:useBean id="language" scope="session" class="cz.svjis.bean.Language" />
<jsp:useBean id="systemMenu" scope="request" class="java.util.ArrayList" />

    <!-- Tray -->
    <div id="tray">
        <ul class="box">
            <%
               Iterator systemMenuI = systemMenu.iterator();
               while (systemMenuI.hasNext()) {
                    SystemMenuEntry me = (SystemMenuEntry) systemMenuI.next();
            %>   
            <!-- <li id="tray-active"> -->
            <li>
                <a href="<%=me.getLink() %>"><%=language.getText(me.getDescription()) %></a>
            </li>
            <%
               }
            %> 
        </ul>
    <hr class="noscreen" />
    </div> <!-- /tray -->