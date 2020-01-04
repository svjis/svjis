<%-- 
    Document   : Administration_buildingDetail
    Created on : 19.6.2011, 23:21:27
    Author     : berk
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="language" scope="session" class="cz.svjis.bean.Language" />
<jsp:useBean id="company" scope="session" class="cz.svjis.bean.Company" />
<jsp:useBean id="building" scope="request" class="cz.svjis.bean.Building" />

<jsp:include page="_header.jsp" />
<jsp:include page="_tray.jsp" />

    <!-- Columns -->
    <div id="cols" class="box">

        <!-- Content -->
        <div id="content">
            <div id="content-main">
                <div id="content-main-in">
                    <h1 class="page-title"><%=language.getText("Building") %></h1>
                    <form action="Dispatcher" method="post">
                        <input type="hidden" name="page" value="buildingSave" />
                        <fieldset>
                            <p>
                                <label id="common-label" for="common-input"><%=language.getText("Address") %></label>
                                <input id="common-input" type="text" name="address" maxlength="50" size="50" value="<%=building.getAddress() %>">
                            </p>
                            <p>
                                <label id="common-label" for="common-input"><%=language.getText("City") %></label>
                                <input id="common-input" type="text" name="city" maxlength="50" size="50" value="<%=building.getCity() %>">
                            </p>
                            <p>
                                <label id="common-label" for="common-input"><%=language.getText("Post code") %></label>
                                <input id="common-input" type="text" name="postCode" maxlength="10" size="50" value="<%=building.getPostCode() %>">
                            </p>
                            <p>
                                <label id="common-label" for="common-input"><%=language.getText("Registration Id.") %></label>
                                <input id="common-input" type="text" name="registrationNo" maxlength="20" size="50" value="<%=building.getRegistrationNo() %>">
                            </p>
                        </fieldset>
                        <p>
                            <input type="submit" value="<%=language.getText("Save") %>" />
                        </p>
                    </form>
                            
                    <form action="Dispatcher?page=buildingPictureSave" enctype="multipart/form-data" method="post">
                        <fieldset>
                            <legend><%=language.getText("Building picture (size 529 x 94 px)") %></legend>
                            <% if (company.getPictureFilename() != null) { %>
                            <p>
                                <img src="<%=company.getPictureUrl(request.getServletContext().getRealPath("/")) %>" />
                            </p>
                            <% } %>
                            <p>
                                <input type="file" name="attachment" size="40">
                                <input type="submit" value="<%=language.getText("Insert picture") %>">
                            </p>
                        </fieldset>
                    </form>
                </div> <!-- /content-main-in -->
            </div> <!-- /content-main -->
            <hr class="noscreen" />
        </div> <!-- /content -->

        <jsp:include page="Administration__menu.jsp" />
    
    </div> <!-- /cols -->

<jsp:include page="_footer.jsp" />
