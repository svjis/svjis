<%-- 
    Document   : Administration_companyDetail
    Created on : 18.6.2011, 23:40:54
    Author     : berk
--%>

<%@page import="java.text.SimpleDateFormat"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="language" scope="session" class="cz.svjis.bean.Language" />
<jsp:useBean id="company" scope="session" class="cz.svjis.bean.Company" />

<jsp:include page="_header.jsp" />
<jsp:include page="_tray.jsp" />

<%
    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
%>

    <!-- Columns -->
    <div id="cols" class="box">

        <!-- Content -->
        <div id="content">
            <div id="content-main">
                <div id="content-main-in">
                    <h1 class="page-title"><%=language.getText("Company") %></h1>
                    <form action="Dispatcher" method="post">
                        <input type="hidden" name="page" value="companySave" />
                        <fieldset>
                            <legend class="hidden-legend"><%=language.getText("General") %></legend>
                            <p>
                                <label id="common-label" for="common-input"><%=language.getText("Name") %></label>
                                <input id="common-input" type="text" name="name" maxlength="50" size="50" value="<%=company.getName() %>">
                            </p>

                            <p>
                                <label id="common-label" for="common-input"><%=language.getText("Address") %></label>
                                <input id="common-input" type="text" name="address" maxlength="50" size="50" value="<%=company.getAddress() %>">
                            </p>
                            <p>
                                <label id="common-label" for="common-input"><%=language.getText("City") %></label>
                                <input id="common-input" type="text" name="city" maxlength="50" size="50" value="<%=company.getCity() %>">
                            </p>
                            <p>
                                <label id="common-label" for="common-input"><%=language.getText("Post code") %></label>
                                <input id="common-input" type="text" name="postCode" maxlength="10" size="50" value="<%=company.getPostCode() %>">
                            </p>
                            <p>
                                <label id="common-label" for="common-input"><%=language.getText("Phone") %></label>
                                <input id="common-input" type="text" name="phone" maxlength="30" size="50" value="<%=company.getPhone() %>">
                            </p>
                            <p>
                                <label id="common-label" for="common-input"><%=language.getText("Fax") %></label>
                                <input id="common-input" type="text" name="fax" maxlength="30" size="50" value="<%=company.getFax() %>">
                            </p>
                            <p>
                                <label id="common-label" for="common-input"><%=language.getText("E-Mail") %></label>
                                <input id="common-input" type="text" name="eMail" maxlength="30" size="50" value="<%=company.geteMail() %>">
                            </p>
                            <p>
                                <label id="common-label" for="common-input"><%=language.getText("Registration No.") %></label>
                                <input id="common-input" type="text" name="registrationNo" maxlength="20" size="50" value="<%=company.getRegistrationNo() %>">
                            </p>
                            <p>
                                <label id="common-label" for="common-input"><%=language.getText("VAT Registration No.") %></label>
                                <input id="common-input" type="text" name="vatRegistrationNo" maxlength="20" size="50" value="<%=company.getVatRegistrationNo() %>">
                            </p>
                            <p>
                                <label id="common-label" for="common-input"><%=language.getText("Internet domain") %></label>
                                <input id="common-input" type="text" name="internetDomain" maxlength="50" size="50" value="<%=company.getInternetDomain() %>">
                            </p>
                            <p>
                                <label id="common-label" for="common-input"><%=language.getText("Database creation date") %></label>
                                <input id="common-input" type="text" name="cretionDate" maxlength="50" size="50" value="<%=sdf.format(company.getDatabaseCreationDate()) %>" readonly="readonly">
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
