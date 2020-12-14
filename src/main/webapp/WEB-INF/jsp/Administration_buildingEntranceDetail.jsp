<%-- 
    Document   : Administration_buildingEntranceDetail
    Created on : 4.4.2020, 22:35:46
    Author     : jarberan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="language" scope="session" class="cz.svjis.bean.Language" />
<jsp:useBean id="buildingEntrance" scope="request" class="cz.svjis.bean.BuildingEntrance" />

<jsp:include page="_header.jsp" />
<jsp:include page="_tray.jsp" />

    <!-- Columns -->
    <div id="cols" class="box">

        <!-- Content -->
        <div id="content">
            <div id="content-main">
                <div id="content-main-in">
                    <h1 class="page-title"><%=language.getText("Building entrance") %></h1>
                    <form action="Dispatcher" method="post">
                        <input type="hidden" name="page" value="buildingEntranceSave" />
                        <input type="hidden" name="id" value="<%=buildingEntrance.getId() %>" />
                        <fieldset>
                            <legend class="hidden-legend"><%=language.getText("General") %></legend>
                            <p>
                                <label class="common-label" id="desc-label" for="desc-input"><%=language.getText("Description") %></label>
                                <input class="common-input" id="desc-input" type="text" name="description" maxlength="50" size="50" value="<%=buildingEntrance.getDescription() %>" />
                            </p>
                            <p>
                                <label class="common-label" id="address-label" for="address-input"><%=language.getText("Address") %></label>
                                <input class="common-input" id="address-input" type="text" name="address" maxlength="50" size="50" value="<%=buildingEntrance.getAddress() %>" />
                            </p>
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
