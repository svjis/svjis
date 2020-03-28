<%-- 
    Document   : LostPassword_form
    Created on : 22.8.2011, 17:27:27
    Author     : berk
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="language" scope="session" class="cz.svjis.bean.Language" />

<jsp:include page="_header.jsp" />
<jsp:include page="_tray.jsp" />

        <!-- Content -->
        <div id="content-width">
            <div id="content-width-in">
            
                    <h1 class="page-title"><%=language.getText("Password assistance")%></h1>
                    
                        <form action="Dispatcher" method="post">
                        <input type="hidden" name="page" value="lostPassword_submit" />
                        <fieldset>
                            <legend class="hidden-legend"><%=language.getText("General") %></legend>
                            <p>
                                <label id="common-label" for="common-input"><%=language.getText("Your e-mail")%></label>
                                <input id="common-input" type="text" name="email" maxlength="50" value="" />
                            </p>
                            <p id="common-submit">
                                <input class="my-button" type="submit" value="<%=language.getText("Send") %>" />
                            </p>
                        </fieldset>
                    </form>
                            
            <hr class="noscreen" />
            </div>
        </div> <!-- /content -->


<jsp:include page="_footer.jsp" />
