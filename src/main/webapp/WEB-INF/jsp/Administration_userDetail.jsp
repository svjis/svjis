<%-- 
    Document   : Administration_userDetail
    Created on : 21.6.2011, 15:54:59
    Author     : berk
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="cz.svjis.bean.Role"%>
<%@page import="cz.svjis.bean.Language"%>
<%@page import="cz.svjis.servlet.Cmd"%>
<%@page import="java.util.Iterator"%>
<jsp:useBean id="language" scope="session" class="cz.svjis.bean.Language" />
<jsp:useBean id="cUser" scope="request" class="cz.svjis.bean.User" />
<jsp:useBean id="languageList" scope="request" class="java.util.ArrayList" />
<jsp:useBean id="roleList" scope="request" class="java.util.ArrayList" />
<jsp:useBean id="sendCredentials" scope="request" class="cz.svjis.bean.Boolean" />
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
                    <h1 class="page-title"><%=language.getText("User") %></h1>
                    <strong class="message"><%=message %></strong>
                    <strong class="error-message"><%=errorMessage %></strong>
                    <form action="Dispatcher" method="post">
                        <input type="hidden" name="page" value="<%=Cmd.USER_SAVE %>" />
                        <input type="hidden" name="id" value="<%=cUser.getId() %>" />
                        <fieldset>
                            <legend><%=language.getText("Person") %></legend>
                            <p>
                                <label class="common-label" id="salutation-label" for="salutation-input"><%=language.getText("Salutation") %></label>
                                <input class="common-input" id="salutation-input" type="text" name="salutation" maxlength="30" size="50" value="<%=cUser.getSalutation() %>" />
                            </p>
                            <p>
                                <label class="common-label" id="firstname-label" for="firstname-input"><%=language.getText("First name") %></label>
                                <input class="common-input" id="firstname-input" type="text" name="firstName" maxlength="30" size="50" value="<%=cUser.getFirstName() %>" />
                            </p>
                            <p>
                                <label class="common-label" id="lastname-label" for="lastname-input"><%=language.getText("Last name") %></label>
                                <input class="common-input" id="lastname-input" type="text" name="lastName" maxlength="30" size="50" value="<%=cUser.getLastName() %>" />
                            </p>
                            <p>
                                <label class="common-label" id="lang-label" for="lang-input"><%=language.getText("Language") %></label>
                                <select class="common-input" id="lang-input" name="language">
                                <%
                                    Iterator langI = languageList.iterator();
                                    while (langI.hasNext()) {
                                        Language l = (Language) langI.next();
                                %>
                                    <option value="<%=l.getId() %>" <%=(l.getId() == cUser.getLanguageId()) ? "SELECTED" : "" %>><%=l.getDescription() %></option>
                                <%
                                    }
                                %>
                                </select>
                            </p>
                            <p>
                                <label class="common-label" id="common-label" for="common-input"><%=language.getText("Internal note") %></label>
                                <textarea
                                 class="common-textarea"
                                 id="note-textarea"
                                 name="internalNote"
                                 rows=5 cols=40
                                 wrap maxlength="250"
                                 ><%=cUser.getInternalNote() %></textarea>
                            </p>
                        </fieldset>
                        <fieldset>
                            <legend><%=language.getText("Contact information") %></legend>
                            <p>
                                <label class="common-label" id="address-label" for="address-input"><%=language.getText("Address") %></label>
                                <input class="common-input" id="address-input" type="text" name="address" maxlength="50" size="50" value="<%=cUser.getAddress() %>" />
                            </p>
                            <p>
                                <label class="common-label" id="city-label" for="city-input"><%=language.getText("City") %></label>
                                <input class="common-input" id="city-input" type="text" name="city" maxlength="50" size="50" value="<%=cUser.getCity() %>" />
                            </p>
                            <p>
                                <label class="common-label" id="postcode-label" for="postcode-input"><%=language.getText("Post code") %></label>
                                <input class="common-input" id="postcode-input" type="text" name="postCode" maxlength="10" size="50" value="<%=cUser.getPostCode() %>" />
                            </p>
                            <p>
                                <label class="common-label" id="country-label" for="country-input"><%=language.getText("Country") %></label>
                                <input class="common-input" id="country-input" type="text" name="country" maxlength="50" size="50" value="<%=cUser.getCountry() %>" />
                            </p>
                            <p>
                                <label class="common-label" id="fphone-label" for="fphone-input"><%=language.getText("Fixed phone") %></label>
                                <input class="common-input" id="fphone-input" type="text" name="fixedPhone" maxlength="30" size="50" value="<%=cUser.getFixedPhone() %>" />
                            </p>
                            <p>
                                <label class="common-label" id="cphone-label" for="cphone-input"><%=language.getText("Cell phone") %></label>
                                <input class="common-input" id="cphone-input" type="text" name="cellPhone" maxlength="30" size="50" value="<%=cUser.getCellPhone() %>" />
                            </p>
                            <p>
                                <label class="common-label" id="email-label" for="email-input"><%=language.getText("E-Mail") %></label>
                                <input class="common-input" id="email-input" type="text" name="eMail" maxlength="50" size="50" value="<%=cUser.geteMail() %>" />
                            </p>
                            <p>
                                <label class="common-label" id="show-label" for="show-input"><%=language.getText("Show in contact list") %></label>
                                <input class="common-input" id="show-input" type="checkbox" name="phoneList" <%=(cUser.isShowInPhoneList()) ? "checked" : "" %> />
                            </p>
                        </fieldset>
                        <fieldset>
                            <legend><%=language.getText("Login information") %></legend>
                            <p>
                                <label class="common-label" id="lname-label" for="lname-input"><%=language.getText("Login") %></label>
                                <input class="common-input" id="lname-input" type="text" name="login" maxlength="50" size="30" value="<%=cUser.getLogin() %>" />
                            </p>
                            <p>
                                <label class="common-label" id="lpass-label" for="lpass-input"><%=language.getText("Password") %></label>
                                <input class="common-input" id="lpass-input" type="password" name="password" maxlength="20" size="30" value="<%=cUser.getPassword() %>" />
                            </p>
                            <p>
                                <label class="common-label" id="sendc-label" for="sendc-input"><%=language.getText("Send credentials by e-mail") %></label>
                                <input class="common-input" id="sendc-input" type="checkbox" name="sendCredentials" <%=(sendCredentials.isValue()) ? "checked" : "" %> />
                            </p>
                            <p>
                                <label class="common-label" id="enabled-label" for="enabled-input"><%=language.getText("Enabled") %></label>
                                <input class="common-input" id="enabled-input" type="checkbox" name="enabled" <%=(cUser.isEnabled()) ? "checked" : "" %> />
                            </p>
                        </fieldset>
                        <fieldset>
                            <legend><%=language.getText("Member of") %></legend>
                            <%
                            Iterator roleI = roleList.iterator();
                            while (roleI.hasNext()) {
                                Role r = (Role) roleI.next();
                            %>
                            <p>
                                <label class="common-label" id="r<%=r.getId() %>-label" for="r<%=r.getId() %>-input"><%=r.getDescription() %></label>
                                <input class="common-input" id="r<%=r.getId() %>-input" type="checkbox" name="r_<%=r.getId() %>" <%=(cUser.getRoles().get(new Integer(r.getId())) != null) ? "checked" : "" %> />
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
