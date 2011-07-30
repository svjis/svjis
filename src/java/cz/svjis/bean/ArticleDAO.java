/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.svjis.bean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 *
 * @author berk
 */
public class ArticleDAO {
    private Connection cnn;
    
    public ArticleDAO (Connection cnn) {
        this.cnn = cnn;
    }
    
    public ArrayList<Article> getArticleTopList(User u, int top) throws SQLException {
        ArrayList<Article> result = new ArrayList<Article>();
        
        String select = "SELECT FIRST " + top + " "
                + "a.ID, "
                + "a.HEADER, "
                + "count(*) as \"COUNT\" "
                + "FROM LOG l "
                + "LEFT JOIN ARTICLE a ON (a.ID = l.ARTICLE_ID) "
                + "WHERE "
                + "(l.OPERATION_ID = 3) AND "
                + createFilter(u ,true, false)
                + "GROUP BY "
                + "a.ID, "
                + "a.HEADER "
                + "ORDER BY count(*) desc";
        Statement st = cnn.createStatement();
        ResultSet rs = st.executeQuery(select);
        while (rs.next()) {
            Article a = new Article();
            a.setId(rs.getInt("ID"));
            a.setHeader(rs.getString("HEADER"));
            a.setNumOfReads(rs.getInt("COUNT"));
            result.add(a);
        }
        rs.close();
        st.close();
        
        return result;        
    }
    
    public int getNumOfArticles(User u, int menuNodeId, boolean publishedOnly, boolean ownedOnly) throws SQLException {
        int result = 0;
        
        String menuNodeFilter = "";
        if (menuNodeId != 0) {
            menuNodeFilter = "AND (a.MENU_NODE_ID = " + menuNodeId + ") ";
        }
        
        String select = "SELECT COUNT(*) AS CNT FROM ARTICLE a WHERE "
                + createFilter(u ,publishedOnly, ownedOnly)
                + menuNodeFilter;
        //System.err.println(select);
        Statement st = cnn.createStatement();
        ResultSet rs = st.executeQuery(select);
        if (rs.next()) {
            result = rs.getInt("CNT");
        }
        rs.close();
        st.close();
        return result;
    }
    
    public ArrayList<Article> getArticleList(User u, int menuNodeId, int pageNo, int pageSize, boolean publishedOnly, boolean ownedOnly) throws SQLException {
        ArrayList<Article> result = new ArrayList<Article>();
        
        String menuNodeFilter = "";
        if (menuNodeId != 0) {
            menuNodeFilter = "AND (a.MENU_NODE_ID = " + menuNodeId + ") ";
        }
        
        String select = "SELECT FIRST " + (pageNo * pageSize) + " "
                + "a.ID, "
                + "a.COMPANY_ID, "
                + "a.MENU_NODE_ID, "
                + "m.DESCRIPTION AS MENU_NODE, "
                + "a.LANGUAGE_ID, "
                + "a.HEADER, "
                + "a.DESCRIPTION, "
                //+ "a.BODY, "
                + "a.CREATED_BY_USER_ID, "
                + "a.CREATION_DATE, "
                + "a.PUBLISHED, "
                + "a.COMMENTS_ALLOWED, "
                + "u.FIRST_NAME, "
                + "u.LAST_NAME, "
                + "(SELECT COUNT(*) FROM ARTICLE_COMMENT c WHERE c.ARTICLE_ID = a.ID) AS COMMENT_CNT "
                + "FROM ARTICLE a "
                + "LEFT JOIN \"USER\" u ON (u.ID = a.CREATED_BY_USER_ID) "
                + "LEFT JOIN MENU_TREE m ON (m.ID = a.MENU_NODE_ID) "
                + "WHERE "
                + createFilter(u ,publishedOnly, ownedOnly)
                + menuNodeFilter
                + "ORDER BY a.CREATION_DATE desc, a.ID desc ";
        
        Statement st = cnn.createStatement();
        ResultSet rs = st.executeQuery(select);
        
        int cPageNo = 1;
        int cArtNo = 0;
        
        while (rs.next()) {
            if (cPageNo == pageNo) {
                Article a = new Article();
                a.setId(rs.getInt("ID"));
                a.setCompanyId(rs.getInt("COMPANY_ID"));
                a.setMenuNodeId(rs.getInt("MENU_NODE_ID"));
                a.setMenuNodeDescription(rs.getString("MENU_NODE"));
                a.setLanguageId(rs.getInt("LANGUAGE_ID"));
                a.setHeader(rs.getString("HEADER"));
                a.setDescription(rs.getString("DESCRIPTION"));
                //a.setBody(rs.getString("BODY"));
                a.setAuthorId(rs.getInt("CREATED_BY_USER_ID"));
                a.setCreationDate(rs.getTimestamp("CREATION_DATE"));
                a.setPublished(rs.getBoolean("PUBLISHED"));
                a.setCommentsAllowed(rs.getBoolean("COMMENTS_ALLOWED"));
                a.setAuthor(new User());
                a.getAuthor().setFirstName(rs.getString("FIRST_NAME"));
                a.getAuthor().setLastName(rs.getString("LAST_NAME"));
                a.setNumOfComments(rs.getInt("COMMENT_CNT"));
                result.add(a);
            }
            
            cArtNo++;
            if (cArtNo == pageSize) {
                cPageNo++;
                cArtNo = 0;
            }
        }
        rs.close();
        st.close();
        
        return result;
    }
    
    public int getNumOfArticlesFromSearch(String search, User u, int menuNodeId, boolean publishedOnly, boolean ownedOnly) throws SQLException {
        int result = 0;
        
        String menuNodeFilter = "";
        if (menuNodeId != 0) {
            menuNodeFilter = "AND (a.MENU_NODE_ID = " + menuNodeId + ") ";
        }
        
        String select = "SELECT COUNT(*) AS CNT FROM ARTICLE a WHERE "
                + createFilter(u ,publishedOnly, ownedOnly)
                + menuNodeFilter
                + " AND "
                + "((UPPER(a.HEADER) like UPPER('%" + search + "%')) OR "
                + "(UPPER(a.DESCRIPTION) like UPPER('%" + search + "%')) OR "
                + "(UPPER(a.BODY) like UPPER('%" + search + "%'))) ";
        //System.err.println(select);
        Statement st = cnn.createStatement();
        ResultSet rs = st.executeQuery(select);
        if (rs.next()) {
            result = rs.getInt("CNT");
        }
        rs.close();
        st.close();
        return result;
    }
    
    public ArrayList<Article> getArticleListFromSearch(String search, User u, int menuNodeId, int pageNo, int pageSize, boolean publishedOnly, boolean ownedOnly) throws SQLException {
        ArrayList<Article> result = new ArrayList<Article>();
        
        String menuNodeFilter = "";
        if (menuNodeId != 0) {
            menuNodeFilter = "AND (a.MENU_NODE_ID = " + menuNodeId + ") ";
        }
        
        String select = "SELECT FIRST " + (pageNo * pageSize) + " "
                + "a.ID, "
                + "a.COMPANY_ID, "
                + "a.MENU_NODE_ID, "
                + "m.DESCRIPTION AS MENU_NODE, "
                + "a.LANGUAGE_ID, "
                + "a.HEADER, "
                + "a.DESCRIPTION, "
                //+ "a.BODY, "
                + "a.CREATED_BY_USER_ID, "
                + "a.CREATION_DATE, "
                + "a.PUBLISHED, "
                + "a.COMMENTS_ALLOWED, "
                + "u.FIRST_NAME, "
                + "u.LAST_NAME, "
                + "(SELECT COUNT(*) FROM ARTICLE_COMMENT c WHERE c.ARTICLE_ID = a.ID) AS COMMENT_CNT "
                + "FROM ARTICLE a "
                + "LEFT JOIN \"USER\" u ON (u.ID = a.CREATED_BY_USER_ID) "
                + "LEFT JOIN MENU_TREE m ON (m.ID = a.MENU_NODE_ID) "
                + "WHERE "
                + createFilter(u ,publishedOnly, ownedOnly)
                + menuNodeFilter
                + " AND "
                + "((UPPER(a.HEADER) like UPPER('%" + search + "%')) OR "
                + "(UPPER(a.DESCRIPTION) like UPPER('%" + search + "%')) OR "
                + "(UPPER(a.BODY) like UPPER('%" + search + "%'))) "
                + "ORDER BY a.CREATION_DATE desc, a.ID desc ";
        
        Statement st = cnn.createStatement();
        ResultSet rs = st.executeQuery(select);
        
        int cPageNo = 1;
        int cArtNo = 0;
        
        while (rs.next()) {
            if (cPageNo == pageNo) {
                Article a = new Article();
                a.setId(rs.getInt("ID"));
                a.setCompanyId(rs.getInt("COMPANY_ID"));
                a.setMenuNodeId(rs.getInt("MENU_NODE_ID"));
                a.setMenuNodeDescription(rs.getString("MENU_NODE"));
                a.setLanguageId(rs.getInt("LANGUAGE_ID"));
                a.setHeader(rs.getString("HEADER"));
                a.setDescription(rs.getString("DESCRIPTION"));
                //a.setBody(rs.getString("BODY"));
                a.setAuthorId(rs.getInt("CREATED_BY_USER_ID"));
                a.setCreationDate(rs.getTimestamp("CREATION_DATE"));
                a.setPublished(rs.getBoolean("PUBLISHED"));
                a.setCommentsAllowed(rs.getBoolean("COMMENTS_ALLOWED"));
                a.setAuthor(new User());
                a.getAuthor().setFirstName(rs.getString("FIRST_NAME"));
                a.getAuthor().setLastName(rs.getString("LAST_NAME"));
                a.setNumOfComments(rs.getInt("COMMENT_CNT"));
                result.add(a);
            }
            
            cArtNo++;
            if (cArtNo == pageSize) {
                cPageNo++;
                cArtNo = 0;
            }
        }
        rs.close();
        st.close();
        
        return result;
    }
    
    public Article getArticle(User u, int articleId) throws SQLException {
        Article result = null;

        String select = "SELECT "
                + "a.ID, "
                + "a.COMPANY_ID, "
                + "a.MENU_NODE_ID, "
                + "m.DESCRIPTION AS MENU_NODE, "
                + "a.LANGUAGE_ID, "
                + "a.HEADER, "
                + "a.DESCRIPTION, "
                + "a.BODY, "
                + "a.CREATED_BY_USER_ID, "
                + "a.CREATION_DATE, "
                + "a.PUBLISHED, "
                + "a.COMMENTS_ALLOWED, "
                + "u.FIRST_NAME, "
                + "u.LAST_NAME "
                + "FROM ARTICLE a "
                + "LEFT JOIN \"USER\" u ON (u.ID = a.CREATED_BY_USER_ID) "
                + "LEFT JOIN MENU_TREE m ON (m.ID = a.MENU_NODE_ID) "
                + "WHERE "
                + createFilter(u, false, false)
                + "AND (a.ID = ?) ";
        
        PreparedStatement ps = cnn.prepareStatement(select);
        ps.setInt(1, articleId);
        ResultSet rs = ps.executeQuery();
        
        if (rs.next()) {
            result = new Article();
            result.setId(rs.getInt("ID"));
            result.setCompanyId(rs.getInt("COMPANY_ID"));
            result.setMenuNodeId(rs.getInt("MENU_NODE_ID"));
            result.setMenuNodeDescription(rs.getString("MENU_NODE"));
            result.setLanguageId(rs.getInt("LANGUAGE_ID"));
            result.setHeader(rs.getString("HEADER"));
            result.setDescription(rs.getString("DESCRIPTION"));
            result.setBody(rs.getString("BODY"));
            result.setAuthorId(rs.getInt("CREATED_BY_USER_ID"));
            result.setCreationDate(rs.getTimestamp("CREATION_DATE"));
            result.setPublished(rs.getBoolean("PUBLISHED"));
            result.setCommentsAllowed(rs.getBoolean("COMMENTS_ALLOWED"));
            result.setAuthor(new User());
            result.getAuthor().setFirstName(rs.getString("FIRST_NAME"));
            result.getAuthor().setLastName(rs.getString("LAST_NAME"));
        }
        rs.close();
        ps.close();
        
        result.setAttachmentList(getArticleAttachmentList(result.getId()));
        result.setCommentList(getArticleCommentList(result.getId()));
        result.setRoles(getArticleRoles(result.getId()));
        return result;
    }
    
    public void modifyArticle(Article a) throws SQLException {
        int updated = 0;
        cnn.setAutoCommit(false);
        String update = "UPDATE ARTICLE SET "
                + "MENU_NODE_ID = ?, "
                + "LANGUAGE_ID = ?, "
                + "HEADER = ?, "
                + "DESCRIPTION = ?, "
                + "BODY = ?, "
                + "CREATED_BY_USER_ID = ?, "
                + "CREATION_DATE = ?, "
                + "PUBLISHED = ?, "
                + "COMMENTS_ALLOWED = ? "
                + "WHERE (ID = ?) AND (COMPANY_ID = ?)";
        PreparedStatement ps = cnn.prepareStatement(update);
        ps.setInt(1, a.getMenuNodeId());
        ps.setInt(2, a.getLanguageId());
        ps.setString(3, a.getHeader());
        ps.setString(4, a.getDescription());
        ps.setString(5, a.getBody());
        ps.setInt(6, a.getAuthorId());
        ps.setTimestamp(7, new java.sql.Timestamp(a.getCreationDate().getTime()));
        ps.setBoolean(8, a.isPublished());
        ps.setBoolean(9, a.isCommentsAllowed());
        ps.setInt(10, a.getId());
        ps.setInt(11, a.getCompanyId());
        updated = ps.executeUpdate();
        ps.close();
        if (updated == 1) {
            modifyArticleRoles(a);
        }
        cnn.commit();
        cnn.setAutoCommit(true);
    }
    
    public int insertArticle(Article a) throws SQLException {
        int result = 0;
        cnn.setAutoCommit(false);
        String update = "INSERT INTO ARTICLE ("
                + "COMPANY_ID, "
                + "MENU_NODE_ID, "
                + "LANGUAGE_ID, "
                + "HEADER, "
                + "DESCRIPTION, "
                + "BODY, "
                + "CREATED_BY_USER_ID, "
                + "CREATION_DATE, "
                + "PUBLISHED, "
                + "COMMENTS_ALLOWED"
                + ") VALUES (?,?,?,?,?,?,?,?,?,?) returning ID";
        
        PreparedStatement ps = cnn.prepareStatement(update);
        ps.setInt(1, a.getCompanyId());
        ps.setInt(2, a.getMenuNodeId());
        ps.setInt(3, a.getLanguageId());
        ps.setString(4, a.getHeader());
        ps.setString(5, a.getDescription());
        ps.setString(6, a.getBody());
        ps.setInt(7, a.getAuthorId());
        ps.setTimestamp(8, new java.sql.Timestamp(a.getCreationDate().getTime()));
        ps.setBoolean(9, a.isPublished());
        ps.setBoolean(10, a.isCommentsAllowed());
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            result = rs.getInt("ID");
        }
        ps.close();
        
        a.setId(result);
        modifyArticleRoles(a);
        
        cnn.commit();
        cnn.setAutoCommit(true);
        
        return result;
    }
    
    private String createFilter(User u, boolean publishedOnly, boolean ownedOnly) {
        String result = "";
        
        //-- Company filter
        String company = "(a.COMPANY_ID = " + u.getCompanyId() + ") ";
        //-- Published filter
        String published = "(a.PUBLISHED = 1) ";
        //-- Creator
        String creator = "(a.CREATED_BY_USER_ID = " + u.getId() + ") ";
        //-- Roles filter
        String roles = "";
        Iterator<Integer> roleI = u.getRoles().keySet().iterator();
        while (roleI.hasNext()) {
            roles += roleI.next().toString();
            if (roleI.hasNext()) roles += ",";
        }
        if (roles.equals("")) roles = "-1";
        roles = "(a.ID in (SELECT distinct b.ARTICLE_ID FROM ARTICLE_IS_VISIBLE_TO_ROLE b WHERE (b.ROLE_ID in (" + roles + ")))) "; 
        
        if (publishedOnly) {
            result = company + " AND " + published + " AND (" + roles + " OR " + creator + ")";
        } else {
            result = company + " AND ((" + published + " AND " + roles + ") OR " + creator + ")";
        }
        
        if (ownedOnly) {
            result = company + " AND " + creator;
        }
        
        return result;
    }    
    
    private HashMap getArticleRoles(int articleId) throws SQLException {
        HashMap result = new HashMap();
        String select = "SELECT "
                + "a.ROLE_ID, "
                + "r.DESCRIPTION "
                + "FROM ARTICLE_IS_VISIBLE_TO_ROLE a "
                + "LEFT JOIN \"ROLE\" r ON (r.ID = a.ROLE_ID) "
                + "WHERE (a.ARTICLE_ID = ?) ";
        PreparedStatement ps = cnn.prepareStatement(select);
        ps.setInt(1, articleId);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            result.put(new Integer(rs.getInt("ROLE_ID")), rs.getString("DESCRIPTION"));
        }
        rs.close();
        ps.close();
        return result;
    }
    
    private void modifyArticleRoles(Article a) throws SQLException {
        String delete = "DELETE FROM ARTICLE_IS_VISIBLE_TO_ROLE WHERE (ARTICLE_ID = ?)";
        PreparedStatement ps = cnn.prepareStatement(delete);
        ps.setInt(1, a.getId());
        ps.execute();
        ps.close();

        String insert = "INSERT INTO ARTICLE_IS_VISIBLE_TO_ROLE (ARTICLE_ID, ROLE_ID) VALUES (?, ?)";
        ps = cnn.prepareStatement(insert);
        Iterator<Integer> roleI = a.getRoles().keySet().iterator();
        while (roleI.hasNext()) {
            int roleId = roleI.next();
            ps.setInt(1, a.getId());
            ps.setInt(2, roleId);
            ps.execute();
        }
        ps.close();
    }
    
    private ArrayList<ArticleAttachment> getArticleAttachmentList(int articleId) throws SQLException {
        ArrayList<ArticleAttachment> result = new ArrayList<ArticleAttachment>();
        String select = "SELECT "
                + "a.ID, "
                + "a.ARTICLE_ID, "
                + "a.USER_ID, "
                + "a.UPLOAD_TIME, "
                + "a.CONTENT_TYPE, "
                + "a.FILENAME "
                //+ "a.DATA "
                + "FROM ARTICLE_ATTACHMENT a "
                + "WHERE (a.ARTICLE_ID = ?) "
                + "ORDER BY a.ID";
        
        PreparedStatement ps = cnn.prepareStatement(select);
        ps.setInt(1, articleId);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            ArticleAttachment a = new ArticleAttachment();
            a.setId(rs.getInt("ID"));
            a.setArticleId(rs.getInt("ARTICLE_ID"));
            a.setUserId(rs.getInt("USER_ID"));
            a.setUploadTime(rs.getTimestamp("UPLOAD_TIME"));
            a.setContentType(rs.getString("CONTENT_TYPE"));
            a.setFileName(rs.getString("FILENAME"));
            result.add(a);
        }
        ps.close();
        return result;
    }
    
    public ArticleAttachment getArticleAttachment(int id) throws SQLException {
        ArticleAttachment result = null;
        String select = "SELECT "
                + "a.ID, "
                + "a.ARTICLE_ID, "
                + "a.USER_ID, "
                + "a.UPLOAD_TIME, "
                + "a.CONTENT_TYPE, "
                + "a.FILENAME, "
                + "a.DATA "
                + "FROM ARTICLE_ATTACHMENT a "
                + "WHERE (a.ID = ?) ";
        
        PreparedStatement ps = cnn.prepareStatement(select);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            result = new ArticleAttachment();
            result.setId(rs.getInt("ID"));
            result.setArticleId(rs.getInt("ARTICLE_ID"));
            result.setUserId(rs.getInt("USER_ID"));
            result.setUploadTime(rs.getTimestamp("UPLOAD_TIME"));
            result.setContentType(rs.getString("CONTENT_TYPE"));
            result.setFileName(rs.getString("FILENAME"));
            java.sql.Blob blob = null;
            blob = rs.getBlob("DATA");
            result.setData(blob.getBytes(1, (int) blob.length()));
        }
        ps.close();
        return result;
    }
    
    public void insertArticleAttachment(ArticleAttachment aa) throws SQLException {
        String insert = "INSERT INTO ARTICLE_ATTACHMENT (ARTICLE_ID, USER_ID, UPLOAD_TIME, CONTENT_TYPE, FILENAME, DATA) VALUES (?,?,?,?,?,?)";
        PreparedStatement ps = cnn.prepareStatement(insert);
        ps.setInt(1, aa.getArticleId());
        ps.setInt(2, aa.getUserId());
        ps.setTimestamp(3, new java.sql.Timestamp(aa.getUploadTime().getTime()));
        ps.setString(4, aa.getContentType());
        ps.setString(5, aa.getFileName());
        ps.setBytes(6, aa.getData());
        ps.execute();
        ps.close();
    }
    
    public void deleteArticleAttachment(int id) throws SQLException {
        String delete = "DELETE FROM ARTICLE_ATTACHMENT a WHERE (a.ID = ?)";
        PreparedStatement ps = cnn.prepareStatement(delete);
        ps.setInt(1, id);
        ps.execute();
        ps.close();
    }
    
    private ArrayList<ArticleComment> getArticleCommentList(int articleId) throws SQLException {
        ArrayList<ArticleComment> result = new ArrayList<ArticleComment>();
        String select = "SELECT "
                + "a.ID, "
                + "a.ARTICLE_ID, "
                + "a.USER_ID, "
                + "u.FIRST_NAME, "
                + "u.LAST_NAME, "
                + "a.INSERTION_TIME, "
                + "a.BODY "
                + "FROM ARTICLE_COMMENT a "
                + "LEFT JOIN \"USER\" u ON (u.ID = a.USER_ID) "
                + "WHERE a.ARTICLE_ID = ? "
                + "ORDER BY a.ID";
        
        PreparedStatement ps = cnn.prepareStatement(select);
        ps.setInt(1, articleId);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            ArticleComment a = new ArticleComment();
            a.setId(rs.getInt("ID"));
            a.setArticleId(rs.getInt("ARTICLE_ID"));
            a.setUserId(rs.getInt("USER_ID"));
            a.setUser(new User());
            a.getUser().setFirstName(rs.getString("FIRST_NAME"));
            a.getUser().setLastName(rs.getString("LAST_NAME"));
            a.setInsertionTime(rs.getTimestamp("INSERTION_TIME"));
            a.setBody(rs.getString("BODY"));
            result.add(a);
        }
        ps.close();
        return result;
    }
    
    public void insertArticleComment(ArticleComment c) throws SQLException {
        String insert = "INSERT INTO ARTICLE_COMMENT (ARTICLE_ID, USER_ID, INSERTION_TIME, BODY) VALUES (?,?,?,?)";
        PreparedStatement ps = cnn.prepareStatement(insert);
        ps.setInt(1, c.getArticleId());
        ps.setInt(2, c.getUserId());
        ps.setTimestamp(3, new java.sql.Timestamp(c.getInsertionTime().getTime()));
        ps.setString(4, c.getBody());
        ps.execute();
        ps.close();
    }
}
