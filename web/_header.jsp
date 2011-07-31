<%-- 
    Document   : _header
    Created on : 15.6.2011, 16:58:08
    Author     : berk
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="company" scope="session" class="cz.svjis.bean.Company" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8" />
    <meta http-equiv="content-language" content="cs" />
    <meta name="robots" content="all,follow" />

    <meta name="author" lang="cs" content="All: ... [www.url.cz]; e-mail: jaroslav.beran@gmail.com" />
    <meta name="copyright" lang="cs" content="Webdesign: Nuvio [www.nuvio.cz]; e-mail: ahoj@nuvio.cz" />

    <meta name="description" content="..." />
    <meta name="keywords" content="svj, společenství, rižská" />

    <link rel="stylesheet" media="screen,projection" type="text/css" href="css/reset.css" />
    <link rel="stylesheet" media="screen,projection" type="text/css" href="css/main.css" />
    <!--[if lte IE 6]><link rel="stylesheet" type="text/css" href="css/main-msie.css" /><![endif]-->
    <link rel="stylesheet" media="screen,projection" type="text/css" href="css/style.css" />
    <link rel="stylesheet" media="print" type="text/css" href="css/print.css" />

    <title><%=company.getName() %></title>
</head>

<body>

<div id="main">

    <!-- Header -->
    <div id="header">

        <div id="logo"><span style="background:url('<%=(company.getPictureFilename() != null) ? "Upload?page=getBuildingPicture" : "" %>') 0 0 no-repeat;"></span></div> 
        <div id="company_name"><%=company.getName() %></div>
        <hr class="noscreen" />          

    </div> <!-- /header -->
    