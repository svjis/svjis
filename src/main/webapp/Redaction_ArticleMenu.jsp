<%-- 
    Document   : Redaction_ArticleMenu
    Created on : 8.7.2011, 12:39:20
    Author     : berk
--%>

<%@page import="cz.svjis.bean.Language"%>
<%@page import="java.util.Iterator"%>
<%@page import="cz.svjis.bean.MenuItem"%>
<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="language" scope="session" class="cz.svjis.bean.Language" />
<jsp:useBean id="menu" scope="request" class="cz.svjis.bean.Menu" />

<%!
    private String writeSubMenu(ArrayList<MenuItem> menu, int level, Language language) {
        String ident = "&nbsp;&nbsp;&nbsp;";
        String output = "";

        for (MenuItem ami: menu) {
            output += "<tr>";
            output += "<td class=\"list\">";
            for (int n = 0; n < level; n++) {
                output += ident;
            }
            output += ami.getSection().getDescription();
            output += "</td>";
            output += "<td class=\"list\"><a href=\"Dispatcher?page=redactionArticleMenuEdit&id=" + ami.getSection().getId() + "\"><img src=\"gfx/pencil.png\" border=\"0\" title=\"" + language.getText("Edit") + "\"></a></td>";
            if (ami.getSubSections().size() == 0) {
                output += "<td class=\"list\"><a onclick=\"if (!confirm('" + language.getText("Really do you want to remove menu node") + " " + ami.getSection().getDescription() + " ?')) return false;\" href=\"Dispatcher?page=redactionArticleMenuDelete&id=" + ami.getSection().getId() + "\"><img src=\"gfx/delete.png\" border=\"0\" title=\"" + language.getText("Delete") + "\"></a></td>";
            } else {
                output += "<td class=\"list\">&nbsp;</td>";
            }

            output += "</tr>\n";
            if ((ami.getSubSections() != null) && (!ami.getSubSections().isEmpty()))
                output += writeSubMenu(ami.getSubSections(), level + 1, language);
        }
        return output;
    }
%>

<jsp:include page="_header.jsp" />
<jsp:include page="_tray.jsp" />

    <!-- Columns -->
    <div id="cols" class="box">

        <!-- Content -->
        <div id="content">
            <div id="content-main">
                <div id="content-main-in">
                    <h1 class="page-title"><%=language.getText("Article menu") %></h1>
                    [<a href="Dispatcher?page=redactionArticleMenuEdit&id=0"><%=language.getText("Add new menu node") %></a>]
                    <p>
                        <table class="list">
                            <tr>
                                <th class="list"><%=language.getText("Menu node name") %></th>
                                <th class="list">&nbsp;</th>
                                <th class="list">&nbsp;</th>
                            </tr>
                            <%=writeSubMenu(menu.getMenu(), 0, language) %>
                        </table>
                    </p>
                        
                </div> <!-- /content-main-in -->
            </div> <!-- /content-main -->
            <hr class="noscreen" />
        </div> <!-- /content -->

        <jsp:include page="Redaction__menu.jsp" />
    
    </div> <!-- /cols -->

<jsp:include page="_footer.jsp" />
