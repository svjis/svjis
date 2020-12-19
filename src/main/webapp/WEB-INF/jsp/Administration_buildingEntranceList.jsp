<%-- 
    Document   : Administration_buildingEntranceList
    Created on : 4.4.2020, 22:13:05
    Author     : jarberan
--%>

<%@page import="java.util.List"%>
<%@page import="cz.svjis.bean.BuildingEntrance"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="language" scope="session" class="cz.svjis.bean.Language" />
<jsp:useBean id="buildingEntranceList" scope="request" class="java.util.ArrayList" />

<jsp:include page="_header.jsp" />
<jsp:include page="_tray.jsp" />

    <!-- Columns -->
    <div id="cols" class="box">

        <!-- Content -->
        <div id="content">
            <div id="content-main">
                <div id="content-main-in">
                    <h1 class="page-title" id="tbl-desc"><%=language.getText("Building entrance list") %></h1>

                    [<a href="Dispatcher?page=buildingEntranceEdit&id=0"><%=language.getText("Add new building entrance") %></a>]
                    
                    <table class="list" aria-describedby="tbl-desc">
                        <tr>
                            <th class="list" scope="col">&nbsp;</th>
                            <th class="list" scope="col">&nbsp;</th>
                            <th class="list" scope="col">&nbsp;</th>
                            <th class="list" scope="col"><%=language.getText("Description") %></th>
                            <th class="list" scope="col"><%=language.getText("Address") %></th>
                        </tr>
                    <%
                    int i = 0;
                    for (BuildingEntrance be: (List<BuildingEntrance>) buildingEntranceList) {
                    %>
                        <tr>
                            <td class="list" style="text-align: right"><%=++i %></td>
                            <td class="list"><a href="Dispatcher?page=buildingEntranceEdit&id=<%=be.getId() %>"><img src="gfx/pencil.png" border="0" title="<%=language.getText("Edit") %>" alt="<%=language.getText("Edit") %>"></a></td>
                            <td class="list"><a onclick="if (!confirm('<%=language.getText("Really do you want to remove entrance") %> <%=be.getDescription() %> ?')) return false;" href="Dispatcher?page=buildingEntranceDelete&id=<%=be.getId() %>"><img src="gfx/delete.png" border="0" title="<%=language.getText("Delete") %>" alt="<%=language.getText("Delete") %>"></a></td>
                            <td class="list"><%=be.getDescription() %></td>
                            <td class="list"><%=be.getAddress() %></td>
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
