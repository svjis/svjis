/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.svjis.bean;

import org.junit.Test;

/**
 *
 * @author berk
 */

public class UserTest {
    
    @Test
    public void testLogin() {
        User u = new User();
        
        u.setLogin("Jarda");
        u.setPassword("test");
        u.setEnabled(true);
        org.junit.Assert.assertFalse("Null password", u.login(null));
        
        u.setLogin("Jarda");
        u.setPassword(null);
        u.setEnabled(true);
        org.junit.Assert.assertFalse("Null password", u.login(null));
        
        u.setLogin("Jarda");
        u.setPassword("test");
        u.setEnabled(true);
        org.junit.Assert.assertFalse("Empty password", u.login(""));
        
        u.setLogin("Jarda");
        u.setPassword("");
        u.setEnabled(true);
        org.junit.Assert.assertFalse("Empty password", u.login(""));
        
        u.setLogin("");
        u.setPassword("test");
        u.setEnabled(true);
        org.junit.Assert.assertFalse("Empty login", u.login("test"));
        
        u.setLogin("Jarda");
        u.setPassword("test");
        u.setEnabled(false);
        org.junit.Assert.assertFalse("Disabled account", u.login("test"));
        
        u.setLogin("Jarda");
        u.setPassword("test");
        u.setEnabled(true);
        org.junit.Assert.assertTrue("Correct login", u.login("test"));
    }
}
