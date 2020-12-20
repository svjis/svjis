<%-- 
    Document   : Faults_reportList
    Created on : 20.12.2019, 10:34:31
    Author     : jarberan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="cg" uri="/WEB-INF/tld/customTagLibrary" %>

<jsp:include page="_header.jsp" />
<jsp:include page="_tray.jsp" />

    <!-- Columns -->
    <div id="cols" class="box">

        <!-- Content -->
        <div id="content">
            <div id="content-main">
                <div id="content-main-in">
                    <h1 class="page-title" id="tbl-desc">${cg:tr("Fault reporting", language)}</h1>
                    <c:if test="${user.hasPermission(\"fault_reporting_reporter\")}">
                        <a href="Dispatcher?page=faultReportingEdit&id=0" class="create-button">${cg:tr("Report new fault", language)}</a>
                    </c:if>
                    <table width="100%" class="list" aria-describedby="tbl-desc">
                        <tr>
                            <th class="list" scope="col">&nbsp;</th>
                            <th class="list" scope="col">${cg:tr("Ticket id", language)}</th>
                            <th class="list" scope="col">${cg:tr("Date", language)}</th>
                            <th class="list" scope="col">${cg:tr("Subject", language)}</th>
                            <th class="list" scope="col">${cg:tr("Building entrance", language)}</th>
                            <th class="list" scope="col">${cg:tr("Reporter", language)}</th>
                            <th class="list" scope="col">${cg:tr("Resolver", language)}</th>
                        </tr>
                        <c:forEach items="${reportList}" var="f">
                            <c:if test="${f.closed}">
                                <c:set var="stl" value="background-color:#d0d0d0;text-decoration: line-through;" />
                            </c:if>
                            <c:if test="${searchKey != ''}">
                                <c:set var="searchFilter" value="&search=${cg:UrlEncode(searchKey, 'UTF-8')}" />
                            </c:if>
                            <tr>
                                <td class="list" style="${stl}"><a href="Dispatcher?page=faultDetail&id=${f.id}${searchFilter}"><img src="gfx/find.png" border="0" title="View" alt="View"></a></td>
                                <td class="list" style="${stl}text-align: right;">${f.id}</td>
                                <td class="list" style="${stl}"><fmt:formatDate pattern="dd.MM.yyyy" value="${f.creationDate}" /></td>
                                <td class="list" style="${stl}">${cg:highlight(f.subject, searchKey)}</td>
                                <td class="list" style="${stl}">${cg:nbsp(f.buildingEntrance.description)}</td>
                                <td class="list" style="${stl}">${f.createdByUser.getFullName(false)}</td>
                                <td class="list" style="${stl}">${cg:nbsp(f.assignedToUser.getFullName(false))}</td>
                            </tr>
                        </c:forEach>
                    </table>
                    
                    <p class="t-left">
                        ${cg:printSlider(slider, searchKey, language)}
                    </p>
                    
                </div> <!-- /content-main-in -->
            </div> <!-- /content-main -->
            <hr class="noscreen" />
        </div> <!-- /content -->

        <jsp:include page="Faults__menu.jsp" />
    
    </div> <!-- /cols -->

<jsp:include page="_footer.jsp" />
