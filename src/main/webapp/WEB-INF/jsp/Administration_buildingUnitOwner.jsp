<%-- 
    Document   : Administration_buildingUnitOwner
    Created on : 6.7.2011, 14:21:05
    Author     : berk
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="cz.svjis.servlet.Cmd"%>
<jsp:useBean id="language" scope="session" class="cz.svjis.bean.Language" />
<jsp:useBean id="buildingUnit" scope="request" class="cz.svjis.bean.BuildingUnit" />
<jsp:useBean id="userList" scope="request" class="java.util.ArrayList" />

<jsp:include page="_header.jsp" />
<jsp:include page="_tray.jsp" />

    <!-- Columns -->
    <div id="cols" class="box">

        <!-- Content -->
        <div id="content">
            <div id="content-main">
                <div id="content-main-in">
                    <h1 class="page-title" id="tbl-desc"><%=buildingUnit.getBuildingUnitType() %> - <%=buildingUnit.getRegistrationId() %> <%=buildingUnit.getDescription() %> - <%=language.getText("Owner list") %></h1>
                    <table class="list" aria-describedby="tbl-desc">
                        <tr>
                            <th class="list" scope="col">&nbsp;</th>
                            <th class="list" scope="col">&nbsp;</th>
                            <th class="list" scope="col">&nbsp;</th>
                            <th class="list" scope="col"><%=language.getText("Salutation") %></th>
                            <th class="list" scope="col"><%=language.getText("First name") %></th>
                            <th class="list" scope="col"><%=language.getText("Last name") %></th>
                        </tr>
                    <%
                    int i = 0;
                    java.util.Iterator<cz.svjis.bean.User> userI = userList.iterator();
                    while (userI.hasNext()) {
                        cz.svjis.bean.User u = userI.next();
                    %>
                        <tr>
                            <td class="list" style="text-align: right"><%=++i %></td>
                            <td class="list"><a href="Dispatcher?page=<%=Cmd.USER_EDIT %>&id=<%=u.getId() %>"><img src="gfx/pencil.png" border="0" title="<%=language.getText("Edit") %>"  alt="<%=language.getText("Edit") %>"></a></td>
                            <td class="list"><a href="Dispatcher?page=<%=Cmd.USER_BU %>userBuildingUnits&id=<%=u.getId() %>"><img src="gfx/house.png" border="0" title="<%=language.getText("Building unit list") %>"  alt="<%=language.getText("Building unit list") %>"></a></td>
                            <td class="list"><%=u.getSalutation() %></td>
                            <td class="list"><%=u.getFirstName() %></td>
                            <td class="list"><%=u.getLastName() %></td>
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
