<%-- 
    Document   : ArticleListInquiry
    Created on : 15.6.2011, 16:53:57
    Author     : berk
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@page import="java.text.DecimalFormat"%>
<%@page import="cz.svjis.bean.InquiryOption"%>
<%@page import="cz.svjis.bean.Inquiry"%>
<%@page import="java.util.Iterator"%>

<jsp:useBean id="language" scope="session" class="cz.svjis.bean.Language" />
<jsp:useBean id="inquiryList" scope="request" class="java.util.ArrayList" />

<%
 DecimalFormat df = new DecimalFormat("###");
 Iterator<Inquiry> iI = inquiryList.iterator();
 while (iI.hasNext()) {
     Inquiry inquiry = iI.next();
 %>
 <!-- Inquiry -->
 <div class="box-02-top"></div>
 <div class="box-02-top-b box">
     <span class="f-left"><strong><%=language.getText("Inquiry") %></strong></span>
 </div> <!-- /box-02-top-b -->
 <div class="box-02 box">
     <strong><%=inquiry.getDescription() %></strong>
     <% if (inquiry.isUserCanVote() && inquiry.isOpenForVoting()) { %>
     <form action="Dispatcher" method="post">
         <input type="hidden" name="page" value="inquiryVote">
         <input type="hidden" name="id" value="<%=inquiry.getId() %>">
         <%
         int v = 1;
         Iterator<InquiryOption> iIO = inquiry.getOptionList().iterator();
         while (iIO.hasNext()) {
             InquiryOption io = iIO.next();
             String bar = (io.getCount() != inquiry.getMaximum()) ? "gfx/inq_0242.gif" : "gfx/inq_0212.gif";
             String pct = (inquiry.getCount() != 0) ? df.format(100 * io.getCount() / inquiry.getCount()) : "0";
         %>
             <p><input id="<%="vote-" + v++ %>" type="radio" name="i_<%=inquiry.getId() %>" value="o_<%=io.getId() %>">&nbsp;<%=io.getDescription() %>&nbsp;-&nbsp;<em><%=pct %>%</em><br>
                 <img src="<%=bar %>" width="<%=Integer.valueOf(pct) * 2 %>" height="22" alt="Bar">
         <%
         }
         %>
         <p>
             <input type="submit" id="vote-submit" value="<%=language.getText("Vote") %>" />
         </p>
     </form>
     <% } else { %>
         <%
         Iterator<InquiryOption> iIO = inquiry.getOptionList().iterator();
         while (iIO.hasNext()) {
             InquiryOption io = iIO.next();
             String bar = (io.getCount() != inquiry.getMaximum()) ? "gfx/inq_0242.gif" : "gfx/inq_0212.gif";
             String pct = (inquiry.getCount() != 0) ? df.format(100 * io.getCount() / inquiry.getCount()) : "0";
         %>
             <p><%=io.getDescription() %>&nbsp;-&nbsp;<em><%=pct %>%</em><br>
                 <img src="<%=bar %>" width="<%=Integer.valueOf(pct) * 2 %>" height="22" alt="Bar">
         <%
         }
         %>
             <p id="vote-result"><%=inquiry.getCount() %> <%=language.getText("votes") %>
             <%
                 if (!inquiry.isOpenForVoting()) {
             %>
                 <p><%=language.getText("Voting was closed.") %>
             <%
                 }
             %>
     <% } %>
 </div> <!-- /box-02 -->
 <div class="box-02-bottom"></div>
 <%
 }
 %>
