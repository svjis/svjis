<%-- 
    Document   : Administration_buildingUnitList
    Created on : 19.6.2011, 23:35:42
    Author     : berk
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="cz.svjis.bean.BuildingUnitType"%>
<%@page import="cz.svjis.servlet.Cmd"%>
<%@page import="java.util.Iterator"%>
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
                    <h1 class="page-title" id="tbl-desc"><%=language.getText("Building unit list") %></h1>
                    <%=language.getText("Filter") %>:
                    <form action="Dispatcher" method="post" >
                        <input type="hidden" name="page" value="<%=Cmd.BUILDING_UNIT_LIST %>">
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

                    [<a href="Dispatcher?page=<%=Cmd.BUILDING_UNIT_EDIT %>&id=0"><%=language.getText("Add new building unit") %></a>]&nbsp;
                    [<a href="Upload?page=<%=Cmd.BUILDING_UNIT_XLS %>"><%=language.getText("Export to Excel") %></a>]<br>
                    <table class="list" aria-describedby="tbl-desc">
                        <tr>
                            <th class="list" scope="col">&nbsp;</th>
                            <th class="list" scope="col">&nbsp;</th>
                            <th class="list" scope="col">&nbsp;</th>
                            <th class="list" scope="col">&nbsp;</th>
                            <th class="list" scope="col"><%=language.getText("Type") %></th>
                            <th class="list" scope="col"><%=language.getText("Registration Id.") %></th>
                            <th class="list" scope="col"><%=language.getText("Description") %></th>
                            <th class="list" scope="col"><%=language.getText("Numerator") %></th>
                            <th class="list" scope="col"><%=language.getText("Denominator") %></th>
                        </tr>
                    <%
                    int i = 0;
                    java.util.Iterator<cz.svjis.bean.BuildingUnit> buI = buildingUnitList.iterator();
                    while (buI.hasNext()) {
                        cz.svjis.bean.BuildingUnit u = buI.next();
                    %>
                        <tr>
                            <td class="list" style="text-align: right"><%=++i %></td>
                            <td class="list"><a href="Dispatcher?page=<%=Cmd.BUILDING_UNIT_EDIT %>&id=<%=u.getId() %>"><img src="gfx/pencil.png" border="0" title="<%=language.getText("Edit") %>" alt="<%=language.getText("Edit") %>"></a></td>
                            <td class="list"><a href="Dispatcher?page=<%=Cmd.BUILDING_UNIT_OWNER %>&id=<%=u.getId() %>"><img src="gfx/user.png" border="0" title="<%=language.getText("Owner list") %>" alt="<%=language.getText("Owner list") %>"></a></td>
                            <td class="list"><a onclick="if (!confirm('<%=language.getText("Really do you want to remove unit") %> <%=u.getId() %> ?')) return false;" href="Dispatcher?page=<%=Cmd.BUILDING_UNIT_DELETE %>&id=<%=u.getId() %>"><img src="gfx/delete.png" border="0" title="<%=language.getText("Delete") %>" alt="<%=language.getText("Delete") %>"></a></td>
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
