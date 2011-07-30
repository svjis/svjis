<%-- 
    Document   : PersonalSettings_userDetail
    Created on : 25.6.2011, 14:47:44
    Author     : berk
--%>

<%@page import="cz.svjis.bean.Language"%>
<%@page import="java.util.Iterator"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="language" scope="session" class="cz.svjis.bean.Language" />
<jsp:useBean id="user" scope="session" class="cz.svjis.bean.User" />
<jsp:useBean id="languageList" scope="request" class="java.util.ArrayList" />

<jsp:include page="_header.jsp" />
<jsp:include page="_tray.jsp" />

    <!-- Columns -->
    <div id="cols" class="box">

        <!-- Content -->
        <div id="content">
            <div id="content-main">
                <div id="content-main-in">
                    <h2><%=user.getSalutation() %> <%=user.getFirstName() %> <%=user.getLastName() %></h2>
                    <form action="Dispatcher" method="post">
                        <input type="hidden" name="page" value="psUserDetailSave" />
                        <fieldset>
                            <legend><%=language.getText("Person") %></legend>
                            <p>
                                <label id="common-label" for="common-input"><%=language.getText("Salutation") %></label>
                                <input id="common-input" type="text" name="salutation" maxlength="30" size="50" value="<%=user.getSalutation() %>" />
                            </p>
                            <p>
                                <label id="common-label" for="common-input"><%=language.getText("First name") %></label>
                                <input id="common-input" type="text" name="firstName" maxlength="30" size="50" value="<%=user.getFirstName() %>" />
                            </p>
                            <p>
                                <label id="common-label" for="common-input"><%=language.getText("Last name") %></label>
                                <input id="common-input" type="text" name="lastName" maxlength="30" size="50" value="<%=user.getLastName() %>" />
                            </p>
                            <p>
                                <label id="common-label" for="common-input"><%=language.getText("Language") %></label>
                                <select id="common-input" name="language">
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
                                <label id="common-label" for="common-input"><%=language.getText("Address") %></label>
                                <input id="common-input" type="text" name="address" maxlength="50" size="50" value="<%=user.getAddress() %>" />
                            </p>
                            <p>
                                <label id="common-label" for="common-input"><%=language.getText("City") %></label>
                                <input id="common-input" type="text" name="city" maxlength="50" size="50" value="<%=user.getCity() %>" />
                            </p>
                            <p>
                                <label id="common-label" for="common-input"><%=language.getText("Post code") %></label>
                                <input id="common-input" type="text" name="postCode" maxlength="10" size="50" value="<%=user.getPostCode() %>" />
                            </p>
                            <p>
                                <label id="common-label" for="common-input"><%=language.getText("Country") %></label>
                                <input id="common-input" type="text" name="country" maxlength="50" size="50" value="<%=user.getCountry() %>" />
                            </p>
                            <p>
                                <label id="common-label" for="common-input"><%=language.getText("Fixed phone") %></label>
                                <input id="common-input" type="text" name="fixedPhone" maxlength="30" size="50" value="<%=user.getFixedPhone() %>" />
                            </p>
                            <p>
                                <label id="common-label" for="common-input"><%=language.getText("Cell phone") %></label>
                                <input id="common-input" type="text" name="cellPhone" maxlength="30" size="50" value="<%=user.getCellPhone() %>" />
                            </p>
                            <p>
                                <label id="common-label" for="common-input"><%=language.getText("E-Mail") %></label>
                                <input id="common-input" type="text" name="eMail" maxlength="50" size="50" value="<%=user.geteMail() %>" />
                            </p>
                            <p>
                                <label id="common-label" for="common-input"><%=language.getText("Show in contact list") %></label>
                                <input id="common-input" type="checkbox" name="phoneList" <%=(user.isShowInPhoneList()) ? "checked" : "" %> />
                            </p>
                        </fieldset>
                        <p>
                            <input type="submit" value="<%=language.getText("Save") %>" />
                        </p>
                    </form>
                </div> <!-- /content-main-in -->
            </div> <!-- /content-main -->
            <hr class="noscreen" />
        </div> <!-- /content -->

        <jsp:include page="PersonalSettings__menu.jsp" />
    
    </div> <!-- /cols -->

<jsp:include page="_footer.jsp" />
