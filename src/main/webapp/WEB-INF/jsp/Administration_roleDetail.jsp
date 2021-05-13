<%-- 
    Document   : Administration_roleDetail
    Created on : 21.6.2011, 17:03:53
    Author     : berk
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="cz.svjis.servlet.Cmd"%>
<%@page import="cz.svjis.bean.Permission"%>
<%@page import="java.util.Iterator"%>
<jsp:useBean id="language" scope="session" class="cz.svjis.bean.Language" />
<jsp:useBean id="role" scope="request" class="cz.svjis.bean.Role" />
<jsp:useBean id="permissionList" scope="request" class="java.util.ArrayList" />

<jsp:include page="_header.jsp" />
<jsp:include page="_tray.jsp" />

    <!-- Columns -->
    <div id="cols" class="box">

        <!-- Content -->
        <div id="content">
            <div id="content-main">
                <div id="content-main-in">
                    <h1 class="page-title"><%=language.getText("Role") %></h1>
                    <form action="Dispatcher" method="post">
                        <input type="hidden" name="page" value="<%=Cmd.ROLE_SAVE %>" />
                        <input type="hidden" name="id" value="<%=role.getId() %>" />
                        <fieldset>
                            <legend><%=language.getText("Role") %></legend>
                            <p>
                                <label class="common-label" id="name-label" for="name-input"><%=language.getText("Name") %></label>
                                <input class="common-input" id="name-input" type="text" name="description" maxlength="50" value="<%=role.getDescription() %>" />
                            </p>
                        </fieldset>
                        <fieldset>
                            <legend><%=language.getText("Permissions") %></legend>
                            <%
                                Iterator permI = permissionList.iterator();
                                while (permI.hasNext()) {
                                    Permission p = (Permission) permI.next();
                            %>
                            <p>
                                <label class="common-label" id="p<%=p.getId() %>-label" for="p<%=p.getId() %>-input"><%=p.getDescription() %></label>
                                <input class="common-input" id="p<%=p.getId() %>-input" type="checkbox" name="p_<%=p.getId() %>" <%=(role.getPermissions().get(new Integer(p.getId())) != null) ? "checked" : "" %> />
                            </p>
                            <%
                                }
                            %>      
                        </fieldset>
                        <p>
                            <input class="my-button" id="submit" type="submit" value="<%=language.getText("Save") %>" />
                        </p>
                    </form>
                </div> <!-- /content-main-in -->
            </div> <!-- /content-main -->
            <hr class="noscreen" />
        </div> <!-- /content -->

        <jsp:include page="Administration__menu.jsp" />
    
    </div> <!-- /cols -->

<jsp:include page="_footer.jsp" />
