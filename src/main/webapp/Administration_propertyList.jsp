<%-- 
    Document   : Administration_propertyList
    Created on : 21.6.2011, 17:18:03
    Author     : berk
--%>

<%@page import="java.util.Collections"%>
<%@page import="java.util.Iterator"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="language" scope="session" class="cz.svjis.bean.Language" />
<jsp:useBean id="setup" scope="session" class="java.util.Properties" />

<jsp:include page="_header.jsp" />
<jsp:include page="_tray.jsp" />

    <!-- Columns -->
    <div id="cols" class="box">

        <!-- Content -->
        <div id="content">
            <div id="content-main">
                <div id="content-main-in">
                    <h1 class="page-title"><%=language.getText("Property list") %></h1>
                    [<a href="Dispatcher?page=propertyEdit&key="><%=language.getText("Add new property") %></a>]<br>
                    <table class="list">
                        <tr>
                            <th class="list">&nbsp;</th>
                            <th class="list">&nbsp;</th>
                            <th class="list">&nbsp;</th>
                            <th class="list"><%=language.getText("Key") %></th>
                            <th class="list"><%=language.getText("Value") %></th>
                        </tr>
                        <%
                        int i = 0;
                        java.util.ArrayList<String> keys = new java.util.ArrayList<String>(setup.stringPropertyNames());
                        Collections.sort(keys);
                        Iterator<String> iKeys = keys.iterator();
                        while (iKeys.hasNext()) {
                            String key = iKeys.next();
                        %>
                        <tr>
                            <td class="list" style="text-align: right"><%=++i %></td>
                            <td class="list"><a href="Dispatcher?page=propertyEdit&key=<%=key %>"><img src="gfx/pencil.png" border="0" title="<%=language.getText("Edit") %>"></a></td>
                            <td class="list"><a onclick="if (!confirm('<%=language.getText("Really do you want to remove property") %> <%=key %> ?')) return false;" href="Dispatcher?page=propertyDelete&key=<%=key %>"><img src="gfx/delete.png" border="0" title="<%=language.getText("Delete") %>"></a></td>
                            <td class="list"><%=key %></td>
                            <td class="list"><%=setup.getProperty(key) %></td>
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
