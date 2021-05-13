<%-- 
    Document   : Administration_companyDetail
    Created on : 18.6.2011, 23:40:54
    Author     : berk
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="cz.svjis.servlet.Cmd"%>
<%@page import="cz.svjis.common.JspSnippets"%>
<jsp:useBean id="language" scope="session" class="cz.svjis.bean.Language" />
<jsp:useBean id="company" scope="session" class="cz.svjis.bean.Company" />
<jsp:useBean id="message" scope="request" class="java.lang.String" />

<jsp:include page="_header.jsp" />
<jsp:include page="_tray.jsp" />

    <!-- Columns -->
    <div id="cols" class="box">

        <!-- Content -->
        <div id="content">
            <div id="content-main">
                <div id="content-main-in">
                    <h1 class="page-title"><%=language.getText("Company") %></h1>
                    <strong class="message"><%=message %></strong>
                    <form action="Dispatcher" method="post">
                        <input type="hidden" name="page" value="<%=Cmd.COMPANY_SAVE %>" />
                        <fieldset>
                            <legend class="hidden-legend"><%=language.getText("General") %></legend>
                            <p>
                                <label class="common-label" id="name-label" for="name-input"><%=language.getText("Name") %></label>
                                <input class="common-input" id="name-input" type="text" name="name" maxlength="50" size="50" value="<%=company.getName() %>">
                            </p>

                            <p>
                                <label class="common-label" id="address-label" for="address-input"><%=language.getText("Address") %></label>
                                <input class="common-input" id="address-input" type="text" name="address" maxlength="50" size="50" value="<%=company.getAddress() %>">
                            </p>
                            <p>
                                <label class="common-label" id="city-label" for="city-input"><%=language.getText("City") %></label>
                                <input class="common-input" id="city-input" type="text" name="city" maxlength="50" size="50" value="<%=company.getCity() %>">
                            </p>
                            <p>
                                <label class="common-label" id="postcode-label" for="postcode-input"><%=language.getText("Post code") %></label>
                                <input class="common-input" id="postcode-input" type="text" name="postCode" maxlength="10" size="50" value="<%=company.getPostCode() %>">
                            </p>
                            <p>
                                <label class="common-label" id="phone-label" for="phone-input"><%=language.getText("Phone") %></label>
                                <input class="common-input" id="phone-input" type="text" name="phone" maxlength="30" size="50" value="<%=company.getPhone() %>">
                            </p>
                            <p>
                                <label class="common-label" id="fax-label" for="fax-input"><%=language.getText("Fax") %></label>
                                <input class="common-input" id="fax-input" type="text" name="fax" maxlength="30" size="50" value="<%=company.getFax() %>">
                            </p>
                            <p>
                                <label class="common-label" id="email-label" for="email-input"><%=language.getText("E-Mail") %></label>
                                <input class="common-input" id="email-input" type="text" name="eMail" maxlength="30" size="50" value="<%=company.geteMail() %>">
                            </p>
                            <p>
                                <label class="common-label" id="regno-label" for="regno-input"><%=language.getText("Registration No.") %></label>
                                <input class="common-input" id="regno-input" type="text" name="registrationNo" maxlength="20" size="50" value="<%=company.getRegistrationNo() %>">
                            </p>
                            <p>
                                <label class="common-label" id="vatregno-label" for="vatregno-input"><%=language.getText("VAT Registration No.") %></label>
                                <input class="common-input" id="vatregno-input" type="text" name="vatRegistrationNo" maxlength="20" size="50" value="<%=company.getVatRegistrationNo() %>">
                            </p>
                            <p>
                                <label class="common-label" id="idomain-label" for="idomain-input"><%=language.getText("Internet domain") %></label>
                                <input class="common-input" id="idomain-input" type="text" name="internetDomain" maxlength="50" size="50" value="<%=company.getInternetDomain() %>">
                            </p>
                            <p>
                                <label class="common-label" id="created-label" for="created-input"><%=language.getText("Database creation date") %></label>
                                <input class="common-input" id="created-input" type="text" name="cretionDate" maxlength="50" size="50" value="<%=JspSnippets.renderDate(company.getDatabaseCreationDate()) %>" readonly="readonly">
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
