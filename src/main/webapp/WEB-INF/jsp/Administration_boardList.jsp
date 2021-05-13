<%-- 
    Document   : Administration_boardList
    Created on : 4.2.2020, 11:44:51
    Author     : jarberan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.ArrayList"%>
<%@page import="cz.svjis.bean.BoardMember"%>
<%@page import="cz.svjis.servlet.Cmd"%>
<jsp:useBean id="language" scope="session" class="cz.svjis.bean.Language" />
<jsp:useBean id="boardList" scope="request" class="java.util.ArrayList" />

<jsp:include page="_header.jsp" />
<jsp:include page="_tray.jsp" />

    <!-- Columns -->
    <div id="cols" class="box">

        <!-- Content -->
        <div id="content">
            <div id="content-main">
                <div id="content-main-in">
                    <h1 class="page-title" id="tbl-desc"><%=language.getText("Board") %></h1>

                    [<a href="Dispatcher?page=<%=Cmd.BOARD_EDIT %>&typeId=0&userId=0"><%=language.getText("Add new member") %></a>]
                    
                    <table class="list" aria-describedby="tbl-desc">
                        <tr>
                            <th class="list" scope="col">&nbsp;</th>
                            <th class="list" scope="col"><%=language.getText("Name") %></th>
                            <th class="list" scope="col"><%=language.getText("Position") %></th>
                            <th class="list" scope="col">&nbsp;</th>
                            <th class="list" scope="col">&nbsp;</th>
                        </tr>
                    <%
                    int i = 0;

                    for (BoardMember bm: (ArrayList<BoardMember>) boardList) {
                    %>
                        <tr>
                            <td class="list" style="text-align: right"><%=++i %></td>
                            <td class="list"><%=bm.getUser().getFullName(true) %></td>
                            <td class="list"><%=bm.getBoardMemberType().getDescription() %></td>
                            <td class="list"><a href="Dispatcher?page=<%=Cmd.BOARD_EDIT %>&typeId=<%=bm.getBoardMemberType().getId() %>&userId=<%=bm.getUser().getId() %>"><img src="gfx/pencil.png" border="0" title="<%=language.getText("Edit") %>" alt="<%=language.getText("Edit") %>"></a></td>
                            <td class="list"><a onclick="if (!confirm('<%=language.getText("Really do you want to remove board member") %>&nbsp;<%=bm.getUser().getFirstName() %>&nbsp;<%=bm.getUser().getLastName() %> ?')) return false;" href="Dispatcher?page=<%=Cmd.BOARD_DELETE %>&typeId=<%=bm.getBoardMemberType().getId() %>&userId=<%=bm.getUser().getId() %>"><img src="gfx/delete.png" border="0" title="<%=language.getText("Delete") %>" alt="<%=language.getText("Delete") %>"></a></td>
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

