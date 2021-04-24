<%-- 
    Document   : AdvertEdit
    Created on : 21.04.2021, 14:40:00
    Author     : jarberan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@page import="cz.svjis.bean.Advert"%>
<%@page import="cz.svjis.bean.AdvertType"%>
<%@page import="cz.svjis.bean.Permission"%>
<%@page import="cz.svjis.servlet.CmdFactory"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.ArrayList"%>

<jsp:useBean id="language" scope="session" class="cz.svjis.bean.Language" />
<jsp:useBean id="user" scope="session" class="cz.svjis.bean.User" />
<jsp:useBean id="advert" scope="request" class="cz.svjis.bean.Advert" />
<jsp:useBean id="advertTypeList" scope="request" class="java.util.ArrayList" />
<jsp:useBean id="currMenu" scope="request" class="cz.svjis.bean.AdvertType" />

<jsp:include page="_header.jsp" />
<jsp:include page="_tray.jsp" />

    <!-- Columns -->
    <div id="cols" class="box">

        <!-- Content -->
        <div id="content">
            <div id="content-main">
                <div id="content-main-in">
                    <h1 class="page-title" id="tbl-desc"><%=language.getText("Adverts") %>: <%=currMenu.getDescription() %></h1>
                                        
                    <form action="Dispatcher" method="post">
                        <input type="hidden" name="page" value="<%=CmdFactory.ADVERT_SAVE %>">
                        <input type="hidden" name="id" value="<%=advert.getId()  %>">
                        
                        <p>
                            <%=language.getText("Header") %><br>
                            <input class="common-input" id="header-input" type="text" name="header" maxlength="50" size="80" value="<%=advert.getHeader() %>">
                        </p>
                        
                        <p>
                        <%=language.getText("Type") %><br>
                        <select class="common-input"  id="type-input" name="typeId">
                            <%
                                for (AdvertType t : (ArrayList<AdvertType>) advertTypeList) {
                            %>
                            <option value="<%=t.getId() %>" <%=(t.getId() == advert.getType().getId()) ? "SELECTED" : "" %>><%=t.getDescription() %></option>
                            <%
                                }
                            %>
                        </select>
                        </p>
                        
                        <p>
                        <%=language.getText("Body") %><br>
                        <textarea
                            class="common-textarea"
                            id="body-textarea"
                            name="body"
                            rows=5 cols=80
                            wrap
                            ><%=advert.getBody() %></textarea>
                        </p>
                        
                        <p>
                            <%=language.getText("Published") %><br>
                            <input class="common-input" id="published-input" type="checkbox" name="published" <%=(advert.isPublished()) ? "checked" : "" %>>
                        </p>
                        
                        <p>
                            <input class="my-button" id="submit" type="submit" value="<%=language.getText("Save") %>" />
                        </p>
                    </form>
                    
                </div> <!-- /content-main-in -->
            </div> <!-- /content-main -->
            <hr class="noscreen" />
        </div> <!-- /content -->

        <jsp:include page="Advert__menu.jsp" />
    
    </div> <!-- /cols -->

<jsp:include page="_footer.jsp" />
