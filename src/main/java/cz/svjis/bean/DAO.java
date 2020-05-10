/*
 *       DAO.java
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

/**
 *
 * @author jarberan
 */
public class DAO {
    
    protected Connection cnn;
    
    public DAO (Connection cnn) {
        this.cnn = cnn;
    }
}
