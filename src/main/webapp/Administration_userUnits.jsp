<%-- 
    Document   : Administration_userUnits
    Created on : 5.7.2011, 23:58:54
    Author     : berk
--%>

<%@page import="cz.svjis.bean.BuildingUnit"%>
<%@page import="java.util.Iterator"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="language" scope="session" class="cz.svjis.bean.Language" />
<jsp:useBean id="cUser" scope="request" class="cz.svjis.bean.User" />
<jsp:useBean id="userHasUnitList" scope="request" class="java.util.ArrayList" />
<jsp:useBean id="unitList" scope="request" class="java.util.ArrayList" />

<jsp:include page="_header.jsp" />
<jsp:include page="_tray.jsp" />

    <!-- Columns -->
    <div id="cols" class="box">

        <!-- Content -->
        <div id="content">
            <div id="content-main">
                <div id="content-main-in">
                    <h1 class="page-title"><%=cUser.getFirstName() %>&nbsp;<%=cUser.getLastName() %> - <%=language.getText("Building unit list") %></h1>
                    
                    <table class="list">
                        <tr>
                            <th class="list" scope="col">&nbsp;</th>
                            <th class="list" scope="col">&nbsp;</th>
                            <th class="list" scope="col"><%=language.getText("Type") %></th>
                            <th class="list" scope="col"><%=language.getText("Registration Id.") %></th>
                            <th class="list" scope="col"><%=language.getText("Description") %></th>
                        </tr>
                        <%
                        int i = 0;
                        Iterator<BuildingUnit> buI = userHasUnitList.iterator();
                        while (buI.hasNext()) {
                            BuildingUnit u = buI.next();
                        %>
                            <tr>
                                <td class="list" style="text-align: right"><%=++i %></td>
                                <td class="list"><a onclick="if (!confirm('<%=language.getText("Really do you want to remove unit") %> <%=u.getId() %> ?')) return false;" href="Dispatcher?page=userBuildingUnitRemove&unitId=<%=u.getId() %>&userId=<%=cUser.getId() %>"><img src="gfx/delete.png" border="0" title="<%=language.getText("Delete") %>" alt="<%=language.getText("Delete") %>"></a></td>
                                <td class="list"><%=u.getBuildingUnitType() %></td>
                                <td class="list"><%=u.getRegistrationId() %></td>
                                <td class="list"><%=u.getDescription() %></td>
                            </tr>
                        <%
                        }
                        %>
                    </table>
                                        
                    <form action="Dispatcher" method="post">
                        <input type="hidden" name="page" value="userBuildingUnitAdd" />
                        <input type="hidden" name="userId" value="<%=cUser.getId() %>" />
                        <select id="common-input" name="unitId">
                            <option value="0"><%=language.getText("(choose)") %></option>
                        <%
                            Iterator<BuildingUnit> unitI = unitList.iterator();
                            while (unitI.hasNext()) {
                                BuildingUnit bu = unitI.next();
                        %>
                            <option value="<%=bu.getId() %>"><%=bu.getBuildingUnitType() %> - <%=bu.getRegistrationId() %> - <%=bu.getDescription() %></option>
                        <%
                            }
                        %>
                        </select>
                        <input type="submit" value="<%=language.getText("Add") %>" />
                    </form>
                </div> <!-- /content-main-in -->
            </div> <!-- /content-main -->
            <hr class="noscreen" />
        </div> <!-- /content -->

        <jsp:include page="Administration__menu.jsp" />
    
    </div> <!-- /cols -->

<jsp:include page="_footer.jsp" />
