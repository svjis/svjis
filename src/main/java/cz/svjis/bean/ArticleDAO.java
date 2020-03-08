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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author berk
 */
public class ArticleDAO extends DAO {
    
    public ArticleDAO (Connection cnn) {
        super(cnn);
    }
    
    public List<Article> getArticleTopList(User u, int top, int cntLastMonths) throws SQLException {
        ArrayList<Article> result = new ArrayList<>();
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date d = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.add(Calendar.MONTH, -1 * cntLastMonths);
        d = c.getTime();

        String select = "SELECT FIRST " + top + " " +
                        "    a.ID, " +
                        "    a.HEADER, " +
                        "    l.CNT " +
                        "FROM ARTICLE a " +
                        "LEFT JOIN (SELECT l.ARTICLE_ID, count(*) as CNT FROM LOG l WHERE l.OPERATION_ID = 3 AND l.\"TIME\" > '" + sdf.format(d) + "' GROUP BY l.ARTICLE_ID) l " +
                        "    ON (l.ARTICLE_ID = a.ID) " +
                        "LEFT JOIN ARTICLE_IS_VISIBLE_TO_ROLE b " +
                        "    ON (b.ARTICLE_ID = a.ID) " +
                        "LEFT JOIN USER_HAS_ROLE c " +
                        "    ON (c.USER_ID = " + u.getId() + ") AND (c.ROLE_ID = b.ROLE_ID) " +
                        "WHERE " +
                        "    (a.COMPANY_ID = " + u.getCompanyId() + ") AND " +
                        "    (l.CNT is not null) AND " +
                        "    (a.PUBLISHED = 1)  AND " +
                        "    (c.ROLE_ID is not null) " +
                        "GROUP BY " +
                        "    a.ID, " +
                        "    a.HEADER, " +
                        "    l.CNT " +
                        "ORDER BY " +
                        "    l.CNT DESC ";
        try (Statement st = cnn.createStatement(); ResultSet rs = st.executeQuery(select)) {
            while (rs.next()) {
                Article a = new Article();
                a.setId(rs.getInt("ID"));
                a.setHeader(rs.getString("HEADER"));
                a.setNumOfReads(rs.getInt("CNT"));
                result.add(a);
            }
        }
        
        return result;        
    }
    
    /**
     * 
     * Returns number of all articles.
     * 
     * @param u Current user.
     * @param menuNodeId Menu node Id. Fill 0 for all articles.
     * @param publishedOnly If value is true all published articles will be taken.
     * @param ownedOnly If value is true only owned articles will be taken.
     * @param role Will take only articles which hase this role. Fill 0 for all articles.
     * @return Returns number of all articles.
     * @throws SQLException 
     */
    
    public int getNumOfArticles(User u, int menuNodeId, boolean publishedOnly, boolean ownedOnly, int role) throws SQLException {
        int result = 0;
        
        String menuNodeFilter = "";
        if (menuNodeId != 0) {
            menuNodeFilter = "AND (a.MENU_NODE_ID = " + menuNodeId + ") ";
        }
        
        String roleFilter = "";
        if (role != 0) {
            roleFilter = "AND (b.ROLE_ID is not null) ";
        }
        
        String select = "SELECT COUNT(*) AS CNT FROM ARTICLE a "
                + "LEFT JOIN ARTICLE_IS_VISIBLE_TO_ROLE b ON b.ARTICLE_ID = a.ID and b.ROLE_ID = " + role + " "
                + "WHERE "
                + createFilter(u ,publishedOnly, ownedOnly)
                + menuNodeFilter
                + roleFilter;
        try (Statement st = cnn.createStatement(); ResultSet rs = st.executeQuery(select)) {
            if (rs.next()) {
                result = rs.getInt("CNT");
            }
        }
        return result;
    }
    
    /**
     * 
     * Returns all articles.
     * 
     * @param u Current user.
     * @param menuNodeId Menu node Id. Fill 0 for all articles.
     * @param pageNo Number of page.
     * @param pageSize Size of page.
     * @param publishedOnly If value is true all published articles will be taken.
     * @param ownedOnly If value is true only owned articles will be taken.
     * @param role Will take only articles which hase this role. Fill 0 for all articles.
     * @return
     * @throws SQLException 
     */
    
    public List<Article> getArticleList(User u, int menuNodeId, int pageNo, int pageSize, boolean publishedOnly, boolean ownedOnly, int role) throws SQLException {
        ArrayList<Article> result = new ArrayList<>();
        
        String menuNodeFilter = "";
        if (menuNodeId != 0) {
            menuNodeFilter = "AND (a.MENU_NODE_ID = " + menuNodeId + ") ";
        }
        
        String roleFilter = "";
        if (role != 0) {
            roleFilter = "AND (b.ROLE_ID is not null) ";
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
                + "LEFT JOIN ARTICLE_IS_VISIBLE_TO_ROLE b ON b.ARTICLE_ID = a.ID and b.ROLE_ID = " + role + " "
                + "WHERE "
                + createFilter(u ,publishedOnly, ownedOnly)
                + menuNodeFilter
                + roleFilter
                + "ORDER BY a.CREATION_DATE desc, a.ID desc ";
        
        try (Statement st = cnn.createStatement(); ResultSet rs = st.executeQuery(select)) {
            
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
        }
        
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
                + "((UPPER(a.HEADER) like UPPER(?)) OR "
                + "(UPPER(a.DESCRIPTION) like UPPER(?)) OR "
                + "(UPPER(a.BODY) like UPPER(?))) ";

        try (PreparedStatement ps = cnn.prepareStatement(select)) {
            ps.setString(1, "%" + search + "%");
            ps.setString(2, "%" + search + "%");
            ps.setString(3, "%" + search + "%");
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    result = rs.getInt("CNT");
                }
            }
        }
        return result;
    }
    
    public List<Article> getArticleListFromSearch(String search, User u, int menuNodeId, int pageNo, int pageSize, boolean publishedOnly, boolean ownedOnly) throws SQLException {
        ArrayList<Article> result = new ArrayList<>();
        
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
                + "((UPPER(a.HEADER) like UPPER(?)) OR "
                + "(UPPER(a.DESCRIPTION) like UPPER(?)) OR "
                + "(UPPER(a.BODY) like UPPER(?))) "
                + "ORDER BY a.CREATION_DATE desc, a.ID desc ";
        
        try (PreparedStatement ps = cnn.prepareStatement(select)) {
            ps.setString(1, "%" + search + "%");
            ps.setString(2, "%" + search + "%");
            ps.setString(3, "%" + search + "%");
            try (ResultSet rs = ps.executeQuery()) {
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
            }
        }
        
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
        
        try (PreparedStatement ps = cnn.prepareStatement(select)) {
            ps.setInt(1, articleId);
            try (ResultSet rs = ps.executeQuery()) {
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
            }
        }
        
        if (result != null) {
            result.setAttachmentList(getArticleAttachmentList(result.getId()));
            result.setCommentList(getArticleCommentList(result.getId()));
            result.setRoles(getArticleRoles(result.getId()));
        }
        return result;
    }
    
    public void modifyArticle(Article a) throws SQLException {
        int updated;
        
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
        try (PreparedStatement ps = cnn.prepareStatement(update)) {
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
        }
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
        
        try (PreparedStatement ps = cnn.prepareStatement(update)) {
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
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    result = rs.getInt("ID");
                }
            }
        }
        
        a.setId(result);
        modifyArticleRoles(a);
        
        cnn.commit();
        cnn.setAutoCommit(true);
        
        return result;
    }
    
    private String createFilter(User u, boolean publishedOnly, boolean ownedOnly) {
        String result;
        
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
            result = company + " AND (" + roles + " OR " + creator + ")";
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
        try (PreparedStatement ps = cnn.prepareStatement(select)) {
            ps.setInt(1, articleId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.put(rs.getInt("ROLE_ID"), rs.getString("DESCRIPTION"));
                }
            }
        }
        return result;
    }
    
    private void modifyArticleRoles(Article a) throws SQLException {
        String delete = "DELETE FROM ARTICLE_IS_VISIBLE_TO_ROLE WHERE (ARTICLE_ID = ?)";
        try (PreparedStatement ps = cnn.prepareStatement(delete)) {
            ps.setInt(1, a.getId());
            ps.execute();
        }

        String insert = "INSERT INTO ARTICLE_IS_VISIBLE_TO_ROLE (ARTICLE_ID, ROLE_ID) VALUES (?, ?)";
        try (PreparedStatement ps = cnn.prepareStatement(insert)) {
            Iterator<Integer> roleI = a.getRoles().keySet().iterator();
            while (roleI.hasNext()) {
                int roleId = roleI.next();
                ps.setInt(1, a.getId());
                ps.setInt(2, roleId);
                ps.execute();
            }
        }
    }
    
    private ArrayList<ArticleAttachment> getArticleAttachmentList(int articleId) throws SQLException {
        ArrayList<ArticleAttachment> result = new ArrayList<>();
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
        
        try (PreparedStatement ps = cnn.prepareStatement(select)) {
            ps.setInt(1, articleId);
            try (ResultSet rs = ps.executeQuery()) {
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
            }
        }
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
        
        try (PreparedStatement ps = cnn.prepareStatement(select)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    result = new ArticleAttachment();
                    result.setId(rs.getInt("ID"));
                    result.setArticleId(rs.getInt("ARTICLE_ID"));
                    result.setUserId(rs.getInt("USER_ID"));
                    result.setUploadTime(rs.getTimestamp("UPLOAD_TIME"));
                    result.setContentType(rs.getString("CONTENT_TYPE"));
                    result.setFileName(rs.getString("FILENAME"));
                    java.sql.Blob blob;
                    blob = rs.getBlob("DATA");
                    result.setData(blob.getBytes(1, (int) blob.length()));
                }
            }
        }
        return result;
    }
    
    public void insertArticleAttachment(ArticleAttachment aa) throws SQLException {
        String insert = "INSERT INTO ARTICLE_ATTACHMENT (ARTICLE_ID, USER_ID, UPLOAD_TIME, CONTENT_TYPE, FILENAME, DATA) VALUES (?,?,?,?,?,?)";
        try (PreparedStatement ps = cnn.prepareStatement(insert)) {
            ps.setInt(1, aa.getArticleId());
            ps.setInt(2, aa.getUserId());
            ps.setTimestamp(3, new java.sql.Timestamp(aa.getUploadTime().getTime()));
            ps.setString(4, aa.getContentType());
            ps.setString(5, aa.getFileName());
            ps.setBytes(6, aa.getData());
            ps.execute();
        }
    }
    
    public void deleteArticleAttachment(int id) throws SQLException {
        String delete = "DELETE FROM ARTICLE_ATTACHMENT a WHERE (a.ID = ?)";
        try (PreparedStatement ps = cnn.prepareStatement(delete)) {
            ps.setInt(1, id);
            ps.execute();
        }
    }
    
    private ArrayList<ArticleComment> getArticleCommentList(int articleId) throws SQLException {
        ArrayList<ArticleComment> result = new ArrayList<>();
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
        
        try (PreparedStatement ps = cnn.prepareStatement(select)) {
            ps.setInt(1, articleId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ArticleComment a = new ArticleComment();
                    a.setId(rs.getInt("ID"));
                    a.setArticleId(rs.getInt("ARTICLE_ID"));
                    a.setUser(new User());
                    a.getUser().setId(rs.getInt("USER_ID"));
                    a.getUser().setFirstName(rs.getString("FIRST_NAME"));
                    a.getUser().setLastName(rs.getString("LAST_NAME"));
                    a.setInsertionTime(rs.getTimestamp("INSERTION_TIME"));
                    a.setBody(rs.getString("BODY"));
                    result.add(a);
                }
            }
        }
        return result;
    }
    
    public void insertArticleComment(ArticleComment c) throws SQLException {
        String insert = "INSERT INTO ARTICLE_COMMENT (ARTICLE_ID, USER_ID, INSERTION_TIME, BODY) VALUES (?,?,?,?)";
        try (PreparedStatement ps = cnn.prepareStatement(insert)) {
            ps.setInt(1, c.getArticleId());
            ps.setInt(2, c.getUser().getId());
            ps.setTimestamp(3, new java.sql.Timestamp(c.getInsertionTime().getTime()));
            ps.setString(4, c.getBody());
            ps.execute();
        }
    }
    
    public List<User> getUserListForNotificationAboutNewArticle(int articleId) throws SQLException {
        ArrayList<User> result = new ArrayList<>();
        String select = "SELECT "
                + "a.ARTICLE_ID, "
                + "u.ID, "
                + "u.LAST_NAME, "
                + "u.FIRST_NAME, "
                + "u.E_MAIL "
                + "FROM ARTICLE_IS_VISIBLE_TO_ROLE a "
                + "LEFT JOIN USER_HAS_ROLE r on (r.ROLE_ID = a.ROLE_ID) "
                + "LEFT JOIN \"USER\" u on (u.ID = r.USER_ID) "
                + "LEFT JOIN ARTICLE ar on (ar.ID = a.ARTICLE_ID) "
                + "WHERE (a.ARTICLE_ID = ?) AND (u.E_MAIL <> '') AND (u.ENABLED = 1) AND (ar.PUBLISHED = 1) "
                + "GROUP BY "
                + "a.ARTICLE_ID, "
                + "u.ID, "
                + "u.FIRST_NAME, "
                + "u.LAST_NAME, "
                + "u.E_MAIL "
                + "ORDER BY "
                + "u.LAST_NAME collate UNICODE_CI_AI";
        
        try (PreparedStatement ps = cnn.prepareStatement(select)) {
            ps.setInt(1, articleId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    User u = new User();
                    u.setId(rs.getInt("ID"));
                    u.setFirstName(rs.getString("FIRST_NAME"));
                    u.setLastName(rs.getString("LAST_NAME"));
                    u.seteMail(rs.getString("E_MAIL"));
                    result.add(u);
                }
            }
        }
        return result;
    }
    
    
    public List<User> getUserListWatchingArticle(int articleId) throws SQLException {
        ArrayList<User> result = new ArrayList<>();
        String select = "SELECT \n" +
                        "    a.ID, \n" +
                        "    a.LAST_NAME, \n" +
                        "    a.FIRST_NAME, \n" +
                        "    a.E_MAIL \n" +
                        "FROM \"USER\" a \n" +
                        "LEFT JOIN ARTICLE_WATCHING b ON b.USER_ID = a.ID \n" +
                        "WHERE (a.ENABLED = 1) AND (a.E_MAIL <> '') AND (b.ARTICLE_ID = ?)";
        
        try (PreparedStatement ps = cnn.prepareStatement(select)) {
            ps.setInt(1, articleId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    User u = new User();
                    u.setId(rs.getInt("ID"));
                    u.setFirstName(rs.getString("FIRST_NAME"));
                    u.setLastName(rs.getString("LAST_NAME"));
                    u.seteMail(rs.getString("E_MAIL"));
                    result.add(u);
                }
            }
        }
        return result;
    }

    public boolean isUserWatchingArticle(int articleId, int userId) throws SQLException {
        String select = "SELECT count(*) as CNT FROM ARTICLE_WATCHING a WHERE a.ARTICLE_ID = ? AND a.USER_ID = ?";

        int cnt;
        try (PreparedStatement ps = cnn.prepareStatement(select)) {
            ps.setInt(1, articleId);
            ps.setInt(2, userId);
            try (ResultSet rs = ps.executeQuery()) {
                cnt = 0;
                if (rs.next()) {
                    cnt = rs.getInt("CNT");
                }
            }
        }
        return (cnt != 0);
    }

    public void setUserWatchingArticle(int articleId, int userId) throws SQLException {
        String insert = "INSERT INTO ARTICLE_WATCHING (ARTICLE_ID, USER_ID) VALUES (?, ?)";

        if (!isUserWatchingArticle(articleId, userId)) {
            try (PreparedStatement ps = cnn.prepareStatement(insert)) {
                ps.setInt(1, articleId);
                ps.setInt(2, userId);
                ps.execute();
            }
        }
    }

    public void unsetUserWatchingArticle(int articleId, int userId) throws SQLException {
        String delete = "DELETE FROM ARTICLE_WATCHING a WHERE a.ARTICLE_ID = ? AND a.USER_ID = ?";
        try (PreparedStatement ps = cnn.prepareStatement(delete)) {
            ps.setInt(1, articleId);
            ps.setInt(2, userId);
            ps.execute();
        }
    }
}
