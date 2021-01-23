<%-- 
    Document   : PersonalSettings_userDetail
    Created on : 25.6.2011, 14:47:44
    Author     : berk
--%>

<%@page import="cz.svjis.bean.Language"%>
<%@page import="java.util.Iterator"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="language" scope="session" class="cz.svjis.bean.Language" />
<jsp:useBean id="user" scope="request" class="cz.svjis.bean.User" />
<jsp:useBean id="languageList" scope="request" class="java.util.ArrayList" />
<jsp:useBean id="message" scope="request" class="java.lang.String" />
<jsp:useBean id="errorMessage" scope="request" class="java.lang.String" />

<jsp:include page="_header.jsp" />
<jsp:include page="_tray.jsp" />

    <!-- Columns -->
    <div id="cols" class="box">

        <!-- Content -->
        <div id="content">
            <div id="content-main">
                <div id="content-main-in">
                    <h1 class="page-title"><%=user.getSalutation() %> <%=user.getFirstName() %> <%=user.getLastName() %></h1>
                    <strong class="message"><%=message %></strong>
                    <strong class="error-message"><%=errorMessage %></strong>
                    <form action="Dispatcher" method="post">
                        <input type="hidden" name="page" value="psUserDetailSave" />
                        <fieldset>
                            <legend><%=language.getText("Person") %></legend>
                            <p>
                                <label class="common-label" id="salutation-label" for="salutation-input"><%=language.getText("Salutation") %></label>
                                <input class="common-input" id="salutation-input" type="text" name="salutation" maxlength="30" size="50" value="<%=user.getSalutation() %>" />
                            </p>
                            <p>
                                <label class="common-label" id="fname-label" for="fname-input"><%=language.getText("First name") %></label>
                                <input class="common-input" id="fname-input" type="text" name="firstName" maxlength="30" size="50" value="<%=user.getFirstName() %>" />
                            </p>
                            <p>
                                <label class="common-label" id="lname-label" for="lname-input"><%=language.getText("Last name") %></label>
                                <input class="common-input" id="lname-input" type="text" name="lastName" maxlength="30" size="50" value="<%=user.getLastName() %>" />
                            </p>
                            <p>
                                <label class="common-label" id="lang-label" for="lang-input"><%=language.getText("Language") %></label>
                                <select class="common-input" id="lang-input" name="language">
                                <%
                                    Iterator langI = languageList.iterator();
                                    while (langI.hasNext()) {
                                        Language l = (Language) langI.next();
                                %>
                                    <option value="<%=l.getId() %>" <%=(l.getId() == user.getLanguageId()) ? "SELECTED" : "" %>><%=l.getDescription() %></option>
                                <%
                                    }
                                %>
                                </select>
                            </p>
                        </fieldset>
                        <fieldset>
                            <legend><%=language.getText("Contact information") %></legend>
                            <p>
                                <label class="common-label" id="address-label" for="address-input"><%=language.getText("Address") %></label>
                                <input class="common-input" id="address-input" type="text" name="address" maxlength="50" size="50" value="<%=user.getAddress() %>" />
                            </p>
                            <p>
                                <label class="common-label" id="city-label" for="city-input"><%=language.getText("City") %></label>
                                <input class="common-input" id="city-input" type="text" name="city" maxlength="50" size="50" value="<%=user.getCity() %>" />
                            </p>
                            <p>
                                <label class="common-label" id="pcode-label" for="pcode-input"><%=language.getText("Post code") %></label>
                                <input class="common-input" id="pcode-input" type="text" name="postCode" maxlength="10" size="50" value="<%=user.getPostCode() %>" />
                            </p>
                            <p>
                                <label class="common-label" id="country-label" for="country-input"><%=language.getText("Country") %></label>
                                <input class="common-input" id="country-input" type="text" name="country" maxlength="50" size="50" value="<%=user.getCountry() %>" />
                            </p>
                            <p>
                                <label class="common-label" id="fphone-label" for="fphone-input"><%=language.getText("Fixed phone") %></label>
                                <input class="common-input" id="fphone-input" type="text" name="fixedPhone" maxlength="30" size="50" value="<%=user.getFixedPhone() %>" />
                            </p>
                            <p>
                                <label class="common-label" id="cphone-label" for="cphone-input"><%=language.getText("Cell phone") %></label>
                                <input class="common-input" id="cphone-input" type="text" name="cellPhone" maxlength="30" size="50" value="<%=user.getCellPhone() %>" />
                            </p>
                            <p>
                                <label class="common-label" id="email-label" for="email-input"><%=language.getText("E-Mail") %></label>
                                <input class="common-input" id="email-input" type="text" name="eMail" maxlength="50" size="50" value="<%=user.geteMail() %>" />
                            </p>
                            <p>
                                <label class="common-label" id="show-label" for="show-input"><%=language.getText("Show in contact list") %></label>
                                <input class="common-input" id="show-input" type="checkbox" name="phoneList" <%=(user.isShowInPhoneList()) ? "checked" : "" %> />
                            </p>
                        </fieldset>
                        <fieldset>
                            <legend><%=language.getText("Login") %></legend>
                            <p>
                                <label class="common-label" id="lname-label" for="lname-input"><%=language.getText("Login") %></label>
                                <input class="common-input" id="lname-input" type="text" name="login" maxlength="50" size="50" value="<%=user.getLogin() %>" />
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

        <jsp:include page="PersonalSettings__menu.jsp" />
    
    </div> <!-- /cols -->

<jsp:include page="_footer.jsp" />
