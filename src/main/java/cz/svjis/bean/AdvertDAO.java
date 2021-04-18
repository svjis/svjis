/*
 *       AdvertDAO.java
 *
 *       This file is part of SVJIS project.
 *       https://github.com/svjis/svjis
 *
 *       SVJIS is free software; you can redistribute it and/or modify
 *       it under the terms of the GNU General Public License as published by
 *       the Free Software Foundation; either version 3 of the License, or
 *       (at your option) any later version. <http://www.gnu.org/licenses/>
 */

package cz.svjis.bean;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AdvertDAO extends DAO {

    public AdvertDAO(Connection cnn) {
        super(cnn);
    }
    
    public List<AdvertType> getAdvertTypeList() throws SQLException {
        ArrayList<AdvertType> result = new ArrayList<>();
        String select = "SELECT r.ID, r.DESCRIPTION FROM ADVERT_TYPE r ORDER BY r.ID";
        
        try (Statement st = cnn.createStatement(); ResultSet rs = st.executeQuery(select)) {
            
            while (rs.next()) {
                AdvertType t = new AdvertType();
                t.setId(rs.getInt("ID"));
                t.setDescription(rs.getString("DESCRIPTION"));
                result.add(t);
            }
        }
        
        return result;
    }

}
