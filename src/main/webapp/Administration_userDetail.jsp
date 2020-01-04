<%-- 
    Document   : Administration_userDetail
    Created on : 21.6.2011, 15:54:59
    Author     : berk
--%>

<%@page import="cz.svjis.bean.Role"%>
<%@page import="cz.svjis.bean.Language"%>
<%@page import="java.util.Iterator"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
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
                        <input type="hidden" name="page" value="userSave" />
                        <input type="hidden" name="id" value="<%=cUser.getId() %>" />
                        <fieldset>
                            <legend><%=language.getText("Person") %></legend>
                            <p>
                                <label id="common-label" for="common-input"><%=language.getText("Salutation") %></label>
                                <input id="common-input" type="text" name="salutation" maxlength="30" size="50" value="<%=cUser.getSalutation() %>" />
                            </p>
                            <p>
                                <label id="common-label" for="common-input"><%=language.getText("First name") %></label>
                                <input id="common-input" type="text" name="firstName" maxlength="30" size="50" value="<%=cUser.getFirstName() %>" />
                            </p>
                            <p>
                                <label id="common-label" for="common-input"><%=language.getText("Last name") %></label>
                                <input id="common-input" type="text" name="lastName" maxlength="30" size="50" value="<%=cUser.getLastName() %>" />
                            </p>
                            <p>
                                <label id="common-label" for="common-input"><%=language.getText("Language") %></label>
                                <select id="common-input" name="language">
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
                        </fieldset>
                        <fieldset>
                            <legend><%=language.getText("Contact information") %></legend>
                            <p>
                                <label id="common-label" for="common-input"><%=language.getText("Address") %></label>
                                <input id="common-input" type="text" name="address" maxlength="50" size="50" value="<%=cUser.getAddress() %>" />
                            </p>
                            <p>
                                <label id="common-label" for="common-input"><%=language.getText("City") %></label>
                                <input id="common-input" type="text" name="city" maxlength="50" size="50" value="<%=cUser.getCity() %>" />
                            </p>
                            <p>
                                <label id="common-label" for="common-input"><%=language.getText("Post code") %></label>
                                <input id="common-input" type="text" name="postCode" maxlength="10" size="50" value="<%=cUser.getPostCode() %>" />
                            </p>
                            <p>
                                <label id="common-label" for="common-input"><%=language.getText("Country") %></label>
                                <input id="common-input" type="text" name="country" maxlength="50" size="50" value="<%=cUser.getCountry() %>" />
                            </p>
                            <p>
                                <label id="common-label" for="common-input"><%=language.getText("Fixed phone") %></label>
                                <input id="common-input" type="text" name="fixedPhone" maxlength="30" size="50" value="<%=cUser.getFixedPhone() %>" />
                            </p>
                            <p>
                                <label id="common-label" for="common-input"><%=language.getText("Cell phone") %></label>
                                <input id="common-input" type="text" name="cellPhone" maxlength="30" size="50" value="<%=cUser.getCellPhone() %>" />
                            </p>
                            <p>
                                <label id="common-label" for="common-input"><%=language.getText("E-Mail") %></label>
                                <input id="common-input" type="text" name="eMail" maxlength="50" size="50" value="<%=cUser.geteMail() %>" />
                            </p>
                            <p>
                                <label id="common-label" for="common-input"><%=language.getText("Show in contact list") %></label>
                                <input id="common-input" type="checkbox" name="phoneList" <%=(cUser.isShowInPhoneList()) ? "checked" : "" %> />
                            </p>
                        </fieldset>
                        <fieldset>
                            <legend><%=language.getText("Login information") %></legend>
                            <p>
                                <label id="common-label" for="common-input"><%=language.getText("Login") %></label>
                                <input id="common-input" type="text" name="login" maxlength="50" size="30" value="<%=cUser.getLogin() %>" />
                            </p>
                            <p>
                                <label id="common-label" for="common-input"><%=language.getText("Password") %></label>
                                <input id="common-input" type="password" name="password" maxlength="20" size="30" value="<%=cUser.getPassword() %>" />
                            </p>
                            <p>
                                <label id="common-label" for="common-input"><%=language.getText("Send credentials by e-mail") %></label>
                                <input id="common-input" type="checkbox" name="sendCredentials" <%=(sendCredentials.isValue()) ? "checked" : "" %> />
                            </p>
                            <p>
                                <label id="common-label" for="common-input"><%=language.getText("Enabled") %></label>
                                <input id="common-input" type="checkbox" name="enabled" <%=(cUser.isEnabled()) ? "checked" : "" %> />
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
                                <label id="common-label" for="common-input"><%=r.getDescription() %></label>
                                <input id="common-input" type="checkbox" name="r_<%=r.getId() %>" <%=(cUser.getRoles().get(new Integer(r.getId())) != null) ? "checked" : "" %> />
                            </p>
                            <%
                            }
                            %>        
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
