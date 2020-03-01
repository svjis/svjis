/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
