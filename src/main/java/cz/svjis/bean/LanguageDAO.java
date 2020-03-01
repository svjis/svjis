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

/**
 *
 * @author berk
 */
public class LanguageDAO extends DAO {
    
    public LanguageDAO (Connection cnn) {
        super(cnn);
    }
    
    public ArrayList<Language> getLanguageList() throws SQLException {
        ArrayList<Language> result = new ArrayList<Language>();
        String select = "SELECT a.ID, a.DESCRIPTION FROM LANGUAGE a ORDER BY a.ID";
        Statement st = cnn.createStatement();
        ResultSet rs = st.executeQuery(select);
        while (rs.next()) {
            Language l = new Language();
            l.setId(rs.getInt("ID"));
            l.setDescription(rs.getString("DESCRIPTION"));
            result.add(l);
        }
        rs.close();
        st.close();
        return result;
    }
    
    public Language getDictionary(int languageId) throws SQLException {
        Language result = getLanguage(languageId);
        
        String select2 = "SELECT a.ID, a.TEXT FROM LANGUAGE_DICTIONARY a WHERE a.LANGUAGE_ID = ?";
        PreparedStatement ps = cnn.prepareStatement(select2);
        ps.setInt(1, languageId);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            result.getPhrases().put(rs.getString("ID"), rs.getString("TEXT"));
        }
        rs.close();
        ps.close();
        return result;
    }
    
    public Language getLanguage(int languageId) throws SQLException {
        Language result = new Language();
        String select = "SELECT a.ID, a.DESCRIPTION FROM LANGUAGE a WHERE a.ID = ?";
        PreparedStatement ps = cnn.prepareStatement(select);
        ps.setInt(1, languageId);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            result.setId(rs.getInt("ID"));
            result.setDescription(rs.getString("DESCRIPTION"));
        }
        rs.close();
        ps.close();
        
        return result;
    }
}
