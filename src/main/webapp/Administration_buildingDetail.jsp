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
                            <legend class="hidden-legend"><%=language.getText("General") %></legend>
                            <p>
                                <label class="common-label" id="address-label" for="address-input"><%=language.getText("Address") %></label>
                                <input class="common-input" id="address-input" type="text" name="address" maxlength="50" size="50" value="<%=building.getAddress() %>">
                            </p>
                            <p>
                                <label class="common-label" id="city-label" for="city-input"><%=language.getText("City") %></label>
                                <input class="common-input" id="city-input" type="text" name="city" maxlength="50" size="50" value="<%=building.getCity() %>">
                            </p>
                            <p>
                                <label class="common-label" id="postcode-label" for="postcode-input"><%=language.getText("Post code") %></label>
                                <input class="common-input" id="postcode-input" type="text" name="postCode" maxlength="10" size="50" value="<%=building.getPostCode() %>">
                            </p>
                            <p>
                                <label class="common-label" id="regno-label" for="regno-input"><%=language.getText("Registration Id.") %></label>
                                <input class="common-input" id="regno-input" type="text" name="registrationNo" maxlength="20" size="50" value="<%=building.getRegistrationNo() %>">
                            </p>
                        </fieldset>
                        <p>
                            <input class="my-button" id="submit" type="submit" value="<%=language.getText("Save") %>" />
                        </p>
                    </form>
                            
                    <form action="Dispatcher?page=buildingPictureSave" enctype="multipart/form-data" method="post">
                        <fieldset>
                            <legend><%=language.getText("Building picture (size 529 x 94 px)") %></legend>
                            <% if (company.getPictureFilename() != null) { %>
                            <p>
                                <img src="<%=company.getPictureUrl(request.getServletContext().getRealPath("/")) %>" alt="<%=language.getText("Building picture (size 529 x 94 px)") %>" />
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
