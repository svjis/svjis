<%-- 
    Document   : AdvertList
    Created on : 18.04.2021, 19:18:00
    Author     : jarberan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@page import="cz.svjis.bean.Advert"%>
<%@page import="cz.svjis.bean.AdvertType"%>
<%@page import="cz.svjis.bean.Permission"%>
<%@page import="cz.svjis.servlet.CmdFactory"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.ArrayList"%>

<jsp:useBean id="language" scope="session" class="cz.svjis.bean.Language" />
<jsp:useBean id="user" scope="session" class="cz.svjis.bean.User" />
<jsp:useBean id="advertList" scope="request" class="java.util.ArrayList" />

<jsp:include page="_header.jsp" />
<jsp:include page="_tray.jsp" />

    <!-- Columns -->
    <div id="cols" class="box">

        <!-- Content -->
        <div id="content">
            <div id="content-main">
                <div id="content-main-in">
                    <h1 class="page-title" id="tbl-desc"><%=language.getText("Adverts") %></h1>
                    
                    <% if (user.hasPermission(Permission.CAN_INSERT_ADVERT)) { %>
                    [<a href="Dispatcher?page=<%=CmdFactory.ADVERT_EDIT %>&id=0"><%=language.getText("Create new advert") %></a>]
                    <% } %>
                    
                    <table width="100%" class="list" aria-describedby="tbl-desc">
                        <tr>
                            <th class="list" scope="col">&nbsp;</th>
                            <th class="list" scope="col">&nbsp;</th>
                            <th class="list" scope="col"><%=language.getText("Date") %></th>
                            <th class="list" scope="col"><%=language.getText("Header") %></th>
                        </tr>
                        <%
                        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
		                for (Advert a: (ArrayList<Advert>) advertList) {
		                %>
		                <tr>
		                  <td class="list"><a href="Dispatcher?page=<%=CmdFactory.ADVERT_DETAIL %>&id=<%=a.getId() %>"><img src="gfx/find.png" border="0" title="View" alt="View"></a></td>
		                  <td class="list"><a href="Dispatcher?page=<%=CmdFactory.ADVERT_EDIT %>&id=<%=a.getId() %>"><img src="gfx/pencil.png" border="0" title="Edit" alt="Edit"></a></td>
		                  <td class="list"><%=sdf.format(a.getCreationDate()) %></td>
		                  <td class="list"><%=a.getHeader() %></td>
		                </tr>
		                <%    
		                }
		                %>
                    </table>
                    
                </div> <!-- /content-main-in -->
            </div> <!-- /content-main -->
            <hr class="noscreen" />
        </div> <!-- /content -->

        <jsp:include page="Advert__menu.jsp" />
    
    </div> <!-- /cols -->

<jsp:include page="_footer.jsp" />
