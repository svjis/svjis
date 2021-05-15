<%-- 
    Document   : ArticleListNews
    Created on : 15.6.2011, 16:53:57
    Author     : berk
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@page import="cz.svjis.bean.MiniNews"%>
<%@page import="cz.svjis.common.JspSnippets"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.util.List"%>

<jsp:useBean id="language" scope="session" class="cz.svjis.bean.Language" />
<jsp:useBean id="miniNewsList" scope="request" class="java.util.ArrayList" />

<!-- News -->
 <% if (!miniNewsList.isEmpty()) { %>
 <div class="box-01-top"></div>
 <div class="box-01-top-b box">
     <span class="f-right">&nbsp;</span>
     <span class="f-left"><strong><%=language.getText("News") %></strong></span>
 </div> <!-- /box-01-top-b -->
 <div class="box-01">
     
     <%
         boolean even = false;
         for (MiniNews n: (List<MiniNews>) miniNewsList) {
             Calendar c = Calendar.getInstance();
             c.setTime(n.getTime());
             String months[] = {"JAN","FEB","MAR","APR","MAY","JUN","JUL","AUG","SEP","OCT","NOV","DEC"};
             if (even) {
                 out.println("<div class=\"bg\">");
             }
     %>
     <dl class="news box">
         <dt><%=language.getText(months[c.get(Calendar.MONTH)]) %><br /><span><%=c.get(Calendar.DAY_OF_MONTH) %></span></dt>
         <dd><span>@ <%=JspSnippets.renderTime(n.getTime()) %></span><br /><%=n.getBody() %></dd>
     </dl>        
     <%
             if (even) {
                 out.println("</div> <!-- /bg -->");
             }
             even = (even) ? false : true;
         }
     %>
     
 </div> <!-- /box-01 -->
 <div class="box-01-bottom"></div>
 <% } %>
 