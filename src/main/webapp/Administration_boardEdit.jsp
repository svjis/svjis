<%-- 
    Document   : Administration_boardEdit
    Created on : 4.2.2020, 13:28:08
    Author     : jarberan
--%>

<%@page import="cz.svjis.bean.User"%>
<%@page import="java.util.ArrayList"%>
<%@page import="cz.svjis.bean.BoardMemberType"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="language" scope="session" class="cz.svjis.bean.Language" />
<jsp:useBean id="boardTypes" scope="request" class="java.util.ArrayList" />
<jsp:useBean id="userList" scope="request" class="java.util.ArrayList" />

<jsp:include page="_header.jsp" />
<jsp:include page="_tray.jsp" />

    <!-- Columns -->
    <div id="cols" class="box">

        <!-- Content -->
        <div id="content">
            <div id="content-main">
                <div id="content-main-in">
                    <h1 class="page-title"><%=language.getText("Board") %></h1>
                    <form action="Dispatcher" method="post">
                        <input type="hidden" name="page" value="boardMemberSave" />
                        <fieldset>
                            <p>
                                <label id="common-label" for="common-input"><%=language.getText("Position") %></label>
                                <select id="common-input" name="typeId">
                                    <%
                                        for (BoardMemberType t: (ArrayList<BoardMemberType>) boardTypes) {
                                    %>
                                        <option value="<%=t.getId() %>"><%=t.getDescription() %></option>
                                    <%
                                        }
                                    %>
                                </select>
                            </p>
                            <p>
                                <label id="common-label" for="common-input"><%=language.getText("User") %></label>
                                <select id="common-input" name="userId">
                                    <%
                                        for (User u: (ArrayList<User>) userList) {
                                    %>
                                    <option value="<%=u.getId() %>"><%=u.getLastName() %>&nbsp;<%=u.getFirstName() %></option>
                                    <%
                                        }
                                    %>
                                </select>
                            </p>
                        </fieldset>
                        <p>
                            <input class="my-button" type="submit" value="<%=language.getText("Save") %>" />
                        </p>
                    </form>
                </div> <!-- /content-main-in -->
            </div> <!-- /content-main -->
            <hr class="noscreen" />
        </div> <!-- /content -->

        <jsp:include page="Administration__menu.jsp" />
    
    </div> <!-- /cols -->

<jsp:include page="_footer.jsp" />
