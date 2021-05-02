<%-- 
    Document   : ArticleList
    Created on : 15.6.2011, 16:53:57
    Author     : berk
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@page import="java.text.DecimalFormat"%>
<%@page import="cz.svjis.bean.InquiryOption"%>
<%@page import="cz.svjis.bean.Inquiry"%>
<%@page import="java.util.Calendar"%>
<%@page import="cz.svjis.bean.MiniNews"%>
<%@page import="cz.svjis.bean.Article"%>
<%@page import="cz.svjis.common.JspSnippets"%>
<%@page import="java.util.Iterator"%>

<jsp:useBean id="language" scope="session" class="cz.svjis.bean.Language" />
<jsp:useBean id="slider" scope="request" class="cz.svjis.bean.SliderImpl" />
<jsp:useBean id="articleList" scope="request" class="java.util.ArrayList" />
<jsp:useBean id="articleTopList" scope="request" class="java.util.ArrayList" />
<jsp:useBean id="sectionId" scope="request" class="java.lang.String" />
<jsp:useBean id="miniNewsList" scope="request" class="java.util.ArrayList" />
<jsp:useBean id="inquiryList" scope="request" class="java.util.ArrayList" />
<jsp:useBean id="menu" scope="request" class="cz.svjis.bean.Menu" />


<jsp:include page="_header.jsp" />
<jsp:include page="_tray.jsp" />

    <!-- Columns -->
    <div id="cols" class="box">

        <!-- Content -->
        <div id="content">
            <div id="content-left">
                <div id="content-left-in">

                    <!-- Recent Articles -->
                    <h1 class="article-page-title"><%=menu.getActiveSection(language).getDescription() %></h1>
                    
                    <%
                        Iterator articleListI = articleList.iterator();
                        while (articleListI.hasNext()) {
                            Article a = (Article) articleListI.next();
                    %>
                    <!-- Article -->
                    <div class="article box">
                        <div class="article-desc">
                            <h1 class="article-title-list"><a href="Dispatcher?page=articleDetail&id=<%=a.getId() %><%=(request.getParameter("search") != null) ? "&search=" + JspSnippets.encodeUrl(request.getParameter("search")) : "" %>"><%=JspSnippets.highlight(a.getHeader(), request.getParameter("search")) %></a></h1>
                            <p class="info">
                                <a href="Dispatcher?page=articleList&section=<%=a.getMenuNodeId() %>"><%=a.getMenuNodeDescription() %></a>:
                                <%=JspSnippets.renderDate(a.getCreationDate()) %>,
                                <%=language.getText("by:") %> <%=a.getAuthor().getFullName(false) %><%=(a.getNumOfComments() != 0) ? ", " + language.getText("Comments:") + " " + a.getNumOfComments() : "" %></p> 
                            <%=JspSnippets.highlight(a.getDescription(), request.getParameter("search")) %>
                        </div>
                    </div> <!-- /article -->
                    <%
                        }
                    %>

                    <p class="t-left">
                        <%=JspSnippets.renderPaginator(slider, request.getParameter("search"), null, request) %>
                    </p>

                </div> <!-- /content-left-in -->

            </div> <!-- /content-left -->

            <hr class="noscreen" />

            <div id="content-right">
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
                
                <!-- News -->
                <% if (!miniNewsList.isEmpty()) { %>
                <div class="box-01-top"></div>
                <div class="box-01-top-b box">
                    <span class="f-right">&nbsp;</span>
                    <span class="f-left"><strong><%=language.getText("News") %></strong></span>
                </div> <!-- /box-01-top-b -->
                <div class="box-01">
                    
                    <%
                        Iterator<MiniNews> newsI = miniNewsList.iterator();
                        boolean even = false;
                        while (newsI.hasNext()) {
                            MiniNews n = newsI.next();
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
                <!-- Most readed articles -->
                <div class="box-02-top"></div>
                <div class="box-02-top-b box">
                    <span class="f-left"><strong><%=language.getText("Most readed articles") %></strong></span>
                </div> <!-- /box-02-top-b -->
                <div class="box-02 box">

                    <ul class="mostreaded">
                        <%
                        Iterator<Article> topArtI = articleTopList.iterator();
                        while (topArtI.hasNext()) {
                            Article a = topArtI.next();
                        %>
                        <li><span class="f-right"><%=a.getNumOfReads() %>&times</span><a href="Dispatcher?page=articleDetail&id=<%=a.getId() %>"><%=a.getHeader() %></a></li>
                        <%
                        }
                        %>
                    </ul>

                </div> <!-- /box-02 -->
                <div class="box-02-bottom"></div>

            </div> <!-- /content-right -->

        <hr class="noscreen" />
        </div> <!-- /content -->

        <jsp:include page="Article__menu.jsp" />
    
    </div> <!-- /cols -->

<jsp:include page="_footer.jsp" />
