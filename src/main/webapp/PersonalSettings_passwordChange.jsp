<%-- 
    Document   : PersonalSettings_passwordChange
    Created on : 7.7.2011, 15:39:26
    Author     : berk
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="language" scope="session" class="cz.svjis.bean.Language" />
<jsp:useBean id="user" scope="session" class="cz.svjis.bean.User" />
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
                        <input type="hidden" name="page" value="psPasswordChangeSave" />
                        <fieldset>
                            <legend><%=language.getText("Password change") %></legend>
                            <p>
                                <label class="common-label" id="currpass-label" for="currpass-input"><%=language.getText("Current password") %></label>
                                <input class="common-input" id="currpass-input" type="password" name="oldPassword" maxlength="30" size="50" value="" />
                            </p>
                            <p>
                                <label class="common-label" id="newpass1-label" for="newpass1-input"><%=language.getText("New password") %></label>
                                <input class="common-input" id="newpass1-input" type="password" name="newPassword" maxlength="30" size="50" value="" />
                            </p>
                            <p>
                                <label class="common-label" id="newpass2-label" for="newpass2-input"><%=language.getText("New password again") %></label>
                                <input class="common-input" id="newpass2-input" type="password" name="newPassword2" maxlength="30" size="50" value="" />
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
