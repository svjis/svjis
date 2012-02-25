<%-- 
    Document   : Administration_buildingUnitList
    Created on : 19.6.2011, 23:35:42
    Author     : berk
--%>

<%@page import="cz.svjis.bean.BuildingUnitType"%>
<%@page import="java.util.Iterator"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="language" scope="session" class="cz.svjis.bean.Language" />
<jsp:useBean id="buildingUnitType" scope="request" class="java.util.ArrayList" />
<jsp:useBean id="buildingUnitList" scope="request" class="java.util.ArrayList" />

<jsp:include page="_header.jsp" />
<jsp:include page="_tray.jsp" />

    <!-- Columns -->
    <div id="cols" class="box">

        <!-- Content -->
        <div id="content">
            <div id="content-main">
                <div id="content-main-in">
                    <h2><%=language.getText("Building unit list") %></h2>
                    <%=language.getText("Filter") %>:
                    <form action="Dispatcher" method="post" >
                        <input type="hidden" name="page" value="buildingUnitList">
                        <select name="typeId" onchange="this.form.submit()">
                        <option value="0">(<%=language.getText("all") %>)</option>
                        <%
                            int typeId = 0;
                            if (request.getParameter("typeId") != null) {
                                typeId = Integer.valueOf(request.getParameter("typeId"));
                            }
                            Iterator typeI = buildingUnitType.iterator();
                            while (typeI.hasNext()) {
                                BuildingUnitType t = (BuildingUnitType) typeI.next();
                        %>
                            <option value="<%=t.getId() %>" <%=(typeId == t.getId()) ? "SELECTED" : "" %>><%=t.getDescription() %></option>
                        <%
                            }
                        %>
                        </select>
                    </form>
                    <br>

                    [<a href="Dispatcher?page=buildingUnitEdit&id=0"><%=language.getText("Add new building unit") %></a>]<br>
                    <table class="list">
                        <tr>
                            <th class="list">&nbsp;</th>
                            <th class="list">&nbsp;</th>
                            <th class="list">&nbsp;</th>
                            <th class="list">&nbsp;</th>
                            <th class="list"><%=language.getText("Id") %></th>
                            <th class="list"><%=language.getText("Type") %></th>
                            <th class="list"><%=language.getText("Registration Id.") %></th>
                            <th class="list"><%=language.getText("Description") %></th>
                            <th class="list"><%=language.getText("Numerator") %></th>
                            <th class="list"><%=language.getText("Denominator") %></th>
                        </tr>
                    <%
                    int i = 0;
                    java.util.Iterator<cz.svjis.bean.BuildingUnit> buI = buildingUnitList.iterator();
                    while (buI.hasNext()) {
                        cz.svjis.bean.BuildingUnit u = buI.next();
                    %>
                        <tr>
                            <td class="list" style="text-align: right"><%=++i %></td>
                            <td class="list"><a href="Dispatcher?page=buildingUnitEdit&id=<%=u.getId() %>"><img src="gfx/pencil.png" border="0" title="<%=language.getText("Edit") %>"></a></td>
                            <td class="list"><a href="Dispatcher?page=buildingUnitOwner&id=<%=u.getId() %>"><img src="gfx/user.png" border="0" title="<%=language.getText("Owner list") %>"></a></td>
                            <td class="list"><a onclick="if (!confirm('<%=language.getText("Really do you want to remove unit") %> <%=u.getId() %> ?')) return false;" href="Dispatcher?page=buildingUnitDelete&id=<%=u.getId() %>"><img src="gfx/delete.png" border="0" title="<%=language.getText("Delete") %>"></a></td>
                            <td class="list" style="text-align: right"><%=u.getId() %></td>
                            <td class="list"><%=u.getBuildingUnitType() %></td>
                            <td class="list"><%=u.getRegistrationId() %></td>
                            <td class="list"><%=u.getDescription() %></td>
                            <td class="list" style="text-align: right"><%=u.getNumerator() %></td>
                            <td class="list" style="text-align: right"><%=u.getDenominator() %></td>
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
