<%-- 
    Document   : Administration_propertyDetail
    Created on : 21.6.2011, 17:28:27
    Author     : berk
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="language" scope="session" class="cz.svjis.bean.Language" />
<jsp:useBean id="setup" scope="session" class="cz.svjis.bean.Setup" />
<jsp:useBean id="key" scope="request" class="java.lang.String" />

<jsp:include page="_header.jsp" />
<jsp:include page="_tray.jsp" />

    <!-- Columns -->
    <div id="cols" class="box">

        <!-- Content -->
        <div id="content">
            <div id="content-main">
                <div id="content-main-in">
                    <h1 class="page-title"><%=language.getText("Property") %></h1>
                    <form action="Dispatcher" method="post">
                        <input type="hidden" name="page" value="propertySave" />
                        <input type="hidden" name="origKey" value="<%=key %>" />
                        <fieldset>
                            <legend class="hidden-legend"><%=language.getText("General") %></legend>
                            <p>
                                <label id="common-label" for="common-input"><%=language.getText("Key") %></label>
                                <input id="common-input" type="text" name="key" maxlength="50" value="<%=key %>" />
                            </p>
                            <p>
                                <label id="common-label" for="common-input"><%=language.getText("Value") %></label>
                                <input id="common-input" type="text" name="value" maxlength="1000" value="<%=(setup.getSetupProps().getProperty(key) != null) ? setup.getSetupProps().getProperty(key) : ""  %>" />
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
