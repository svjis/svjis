<%-- 
    Document   : Administration_buildingUnitDetail
    Created on : 19.6.2011, 23:50:02
    Author     : berk
--%>

<%@page import="cz.svjis.bean.BuildingUnitType"%>
<%@page import="java.util.Iterator"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="language" scope="session" class="cz.svjis.bean.Language" />
<jsp:useBean id="buildingUnit" scope="request" class="cz.svjis.bean.BuildingUnit" />
<jsp:useBean id="buildingUnitType" scope="request" class="java.util.ArrayList" />

<jsp:include page="_header.jsp" />
<jsp:include page="_tray.jsp" />

    <!-- Columns -->
    <div id="cols" class="box">

        <!-- Content -->
        <div id="content">
            <div id="content-main">
                <div id="content-main-in">
                    <h1 class="page-title"><%=language.getText("Building unit") %></h1>
                    <form action="Dispatcher" method="post">
                        <input type="hidden" name="page" value="buildingUnitSave" />
                        <input type="hidden" name="id" value="<%=buildingUnit.getId() %>" />
                        <fieldset>
                            <p>
                                <label id="common-label" for="common-input"><%=language.getText("Type") %></label>
                                <select id="common-input" name="typeId">
                                    <%
                                        int typeId = 0;
                                        Iterator typeI = buildingUnitType.iterator();
                                        while (typeI.hasNext()) {
                                            BuildingUnitType t = (BuildingUnitType) typeI.next();
                                    %>
                                        <option value="<%=t.getId() %>" <%=(buildingUnit.getBuildingUnitTypeId() == t.getId()) ? "SELECTED" : "" %>><%=t.getDescription() %></option>
                                    <%
                                        }
                                    %>
                                </select>
                            </p>
                            <p>
                                <label id="common-label" for="common-input"><%=language.getText("Registration Id.") %></label>
                                <input id="common-input" type="text" name="registrationNo" maxlength="50" size="50" value="<%=buildingUnit.getRegistrationId() %>" />
                            </p>
                            <p>
                                <label id="common-label" for="common-input"><%=language.getText("Description") %></label>
                                <input id="common-input" type="text" name="description" maxlength="50" size="50" value="<%=buildingUnit.getDescription() %>" />
                            </p>
                            <p>
                                <label id="common-label" for="common-input"><%=language.getText("Numerator") %></label>
                                <input id="common-input" type="text" name="numerator" maxlength="50" size="50" value="<%=buildingUnit.getNumerator() %>" />
                            </p>
                            <p>
                                <label id="common-label" for="common-input"><%=language.getText("Denominator") %></label>
                                <input id="common-input" type="text" name="denominator" maxlength="50" size="50" value="<%=buildingUnit.getDenominator() %>" />
                            </p>
                            <p id="common-submit">
                                <input type="submit" value="<%=language.getText("Save") %>" />
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
