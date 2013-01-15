<%-- 
    Document   : Units_userUnitList
    Created on : 6.7.2011, 13:06:55
    Author     : berk
--%>

<%@page import="cz.svjis.bean.BuildingUnit"%>
<%@page import="java.util.Iterator"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="language" scope="session" class="cz.svjis.bean.Language" />
<jsp:useBean id="user" scope="session" class="cz.svjis.bean.User" />
<jsp:useBean id="userHasUnitList" scope="request" class="java.util.ArrayList" />

<jsp:include page="_header.jsp" />
<jsp:include page="_tray.jsp" />

    <!-- Columns -->
    <div id="cols" class="box">

        <!-- Content -->
        <div id="content">
            <div id="content-main">
                <div id="content-main-in">
                    <h1 class="page-title"><%=user.getFirstName() %>&nbsp;<%=user.getLastName() %> - <%=language.getText("Building unit list") %></h1>
                    
                    <table class="list">
                        <tr>
                            <th class="list">&nbsp;</th>
                            <th class="list"><%=language.getText("Type") %></th>
                            <th class="list"><%=language.getText("Registration Id.") %></th>
                            <th class="list"><%=language.getText("Description") %></th>
                        </tr>
                        <%
                        int i = 0;
                        Iterator<BuildingUnit> buI = userHasUnitList.iterator();
                        while (buI.hasNext()) {
                            BuildingUnit u = buI.next();
                        %>
                            <tr>
                                <td class="list" style="text-align: right"><%=++i %></td>
                                <td class="list"><%=u.getBuildingUnitType() %></td>
                                <td class="list"><%=u.getRegistrationId() %></td>
                                <td class="list"><%=u.getDescription() %></td>
                            </tr>
                        <%
                        }
                        %>
                    </table>
                </div> <!-- /content-main-in -->
            </div> <!-- /content-main -->
            <hr class="noscreen" />
        </div> <!-- /content -->

        <jsp:include page="Units__menu.jsp" />
    
    </div> <!-- /cols -->

<jsp:include page="_footer.jsp" />
