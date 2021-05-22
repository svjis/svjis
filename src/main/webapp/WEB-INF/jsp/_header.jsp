<%-- 
    Document   : _header
    Created on : 15.6.2011, 16:58:08
    Author     : berk
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<jsp:useBean id="company" scope="session" class="cz.svjis.bean.Company" />
<jsp:useBean id="language" scope="session" class="cz.svjis.bean.Language" />
<jsp:useBean id="setup" scope="session" class="cz.svjis.bean.Setup" />
<jsp:useBean id="pageTitle" scope="request" class="java.lang.String" />

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8" />
    <meta http-equiv="content-language" content="cs" />
    <meta name="robots" content="all,follow" />

    <meta name="author" lang="cs" content="All: SVJIS [https://github.com/svjis]; e-mail: svjis@seznam.cz" />
    <meta name="copyright" lang="cs" content="All: SVJIS [https://github.com/svjis]; e-mail: svjis@seznam.cz" />

    <% if (!setup.getHttpMetaDescription().equals("")) { %>
    <meta name="description" content="<%=setup.getHttpMetaDescription() %>" />
    <% } %>
    <% if (!setup.getHttpMetaKeywords().equals("")) { %>
    <meta name="keywords" content="<%=setup.getHttpMetaKeywords() %>" />
    <% } %>

    <link rel="stylesheet" media="screen,projection" type="text/css" href="css/reset.css" />
    <link rel="stylesheet" media="screen,projection" type="text/css" href="css/main.css" />
    <!--[if lte IE 6]><link rel="stylesheet" type="text/css" href="css/main-msie.css" /><![endif]-->
    <link rel="stylesheet" media="screen,projection" type="text/css" href="css/style.css" />
    <link rel="stylesheet" media="print" type="text/css" href="css/print.css" />

    <title><%=(!pageTitle.equals("")) ? pageTitle : company.getName() %></title>
    
    <% if (!setup.getGoogleAnalyticsId().equals("")) { %>
    <!-- Global site tag (gtag.js) - Google Analytics -->
    <script async src="https://www.googletagmanager.com/gtag/js?id=<%=setup.getGoogleAnalyticsId() %>"></script>
    <script>
        window.dataLayer = window.dataLayer || [];
        function gtag(){dataLayer.push(arguments);}
        gtag('js', new Date());

        gtag('config', '<%=setup.getGoogleAnalyticsId() %>');
    </script>
    <!-- Google analytics end -->
    <% } %>
</head>

<body>

<div id="main">

    <!-- Header -->
    <div id="header">
<% 
    String picture = company.getPictureUrl(request.getServletContext().getRealPath("/")); 
    if (picture != null) {
%>
        <div id="logo"><a href="Dispatcher" title="[<%=language.getText("Go to homepage") %>]"><span style="background:url('<%=picture %>') 0 0 no-repeat;"></span></a></div>
<%
   }
%>
        <div id="company_name"><%=company.getName() %></div>
        <hr class="noscreen" />          

    </div> <!-- /header -->
    