/*
 *       SetupTest.java
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

import java.util.Properties;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 *
 * @author jarberan
 */
public class SetupTest {
    
    @Test
    @DisplayName("Anonymous user id test")
    public void testGetAnonymousUserId() {
        Properties p = new Properties();
        p.setProperty("anonymous.user.id", "22");
        Setup s = new Setup(p);
        assertEquals(22, s.getAnonymousUserId());
    }
    
    @Test
    @DisplayName("Menu default item test")
    public void testGetMenuDefaultItem() {
        Properties p = new Properties();
        Setup s = new Setup(p);
        assertEquals(0, s.getMenuDefaultItem());
    }
    
    @Test
    @DisplayName("Article page size test")
    public void testGetArticlePageSize() {
        Properties p = new Properties();
        p.setProperty("article.page.size", "test");
        Setup s = new Setup(p);
        assertEquals(10, s.getArticlePageSize());
    }
    
    @Test
    @DisplayName("Mail login test")
    public void testGetMailLogin() {
        Properties p = new Properties();
        p.setProperty("mail.login", "test");
        Setup s = new Setup(p);
        assertEquals("test", s.getMailLogin());
    }
    
    @Test
    @DisplayName("Mail password test")
    public void testGetMailPassword() {
        Properties p = new Properties();
        Setup s = new Setup(p);
        assertEquals("", s.getMailPassword());
    }
}
