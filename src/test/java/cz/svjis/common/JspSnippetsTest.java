/*
 *       JspSnippetsTest.java
 *
 *       This file is part of SVJIS project.
 *       https://github.com/svjis/svjis
 *
 *       SVJIS is free software; you can redistribute it and/or modify
 *       it under the terms of the GNU General Public License as published by
 *       the Free Software Foundation; either version 3 of the License, or
 *       (at your option) any later version. <http://www.gnu.org/licenses/>
 */

package cz.svjis.common;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 *
 * @author jarberan
 */
public class JspSnippetsTest {
    
    @Test
    @DisplayName("Highlight regex test")
    public void testValidatePositiveInteger() {
        assertEquals("Ahoj jak se máš?", JspSnippets.envelStrInHtml("Ahoj jak se máš?", "", "*", "#"), "Negative test");
        assertEquals("Ahoj jak se máš?", JspSnippets.envelStrInHtml("Ahoj jak se máš?", "jak sem", "*", "#"), "Negative test");
        assertEquals("Ahoj *jak se# máš?", JspSnippets.envelStrInHtml("Ahoj jak se máš?", "jak se", "*", "#"), "Plain text");
        assertEquals("Ahoj <b>*jak se#</b> máš?", JspSnippets.envelStrInHtml("Ahoj <b>jak se</b> máš?", "jak se", "*", "#"), "Html 1");
        assertEquals("Ahoj *jak <b>se#</b> máš?", JspSnippets.envelStrInHtml("Ahoj jak <b>se</b> máš?", "jak se", "*", "#"), "Html 2");
        assertEquals("Ahoj *ja<b>k s</b>e# máš?", JspSnippets.envelStrInHtml("Ahoj ja<b>k s</b>e máš?", "jak se", "*", "#"), "Html 3");
        assertEquals("Ahoj <strong>jak se</strong> máš?", JspSnippets.envelStrInHtml("Ahoj <strong>jak se</strong> máš?", "strong", "*", "#"), "Html 3");
        assertEquals("Ahoj <font color=\"red\">jak se</font> máš?", JspSnippets.envelStrInHtml("Ahoj <font color=\"red\">jak se</font> máš?", "red", "*", "#"), "Html 3");
    }
    
    @Test
    @DisplayName("Make hyperlinks")
    public void testMakeHyperlinks() {
        assertEquals("Ahoj jak se máš?", JspSnippets.makeHyperlinks("Ahoj jak se máš?"), "Negative test");
        
        assertEquals("<a href=\"http://www.seznam.cz\" target=\"_blank\">http://www.seznam.cz</a>", JspSnippets.makeHyperlinks("http://www.seznam.cz"), "http alone");
        assertEquals("<a href=\"http://www.seznam.cz\" target=\"_blank\">http://www.seznam.cz</a> bla.", JspSnippets.makeHyperlinks("http://www.seznam.cz bla."), "http begin");
        assertEquals("Bla <a href=\"http://www.seznam.cz\" target=\"_blank\">http://www.seznam.cz</a>", JspSnippets.makeHyperlinks("Bla http://www.seznam.cz"), "http end");
        assertEquals("Bla <a href=\"http://www.seznam.cz\" target=\"_blank\">http://www.seznam.cz</a>.", JspSnippets.makeHyperlinks("Bla http://www.seznam.cz."), "http end dot");
        assertEquals("Bla <a href=\"http://www.seznam.cz\" target=\"_blank\">http://www.seznam.cz</a>,", JspSnippets.makeHyperlinks("Bla http://www.seznam.cz,"), "http end comma");
        assertEquals("vím: <a href=\"http://www.vybor.cz/Dispatcher?page=articleDetail&id=563\" target=\"_blank\">http://www.vybor.cz/Dispatcher?page=articleDetail&id=563</a>", JspSnippets.makeHyperlinks("vím: http://www.vybor.cz/Dispatcher?page=articleDetail&id=563"), "https query string");
        
        assertEquals("<a href=\"https://www.seznam.cz\" target=\"_blank\">https://www.seznam.cz</a>", JspSnippets.makeHyperlinks("https://www.seznam.cz"), "https alone");
        assertEquals("<a href=\"https://www.seznam.cz\" target=\"_blank\">https://www.seznam.cz</a> bla.", JspSnippets.makeHyperlinks("https://www.seznam.cz bla."), "https begin");
        assertEquals("Bla <a href=\"https://www.seznam.cz\" target=\"_blank\">https://www.seznam.cz</a>", JspSnippets.makeHyperlinks("Bla https://www.seznam.cz"), "https end");
        assertEquals("Bla <a href=\"https://www.seznam.cz\" target=\"_blank\">https://www.seznam.cz</a>.", JspSnippets.makeHyperlinks("Bla https://www.seznam.cz."), "https end dot");
        assertEquals("Bla <a href=\"https://www.seznam.cz\" target=\"_blank\">https://www.seznam.cz</a>,", JspSnippets.makeHyperlinks("Bla https://www.seznam.cz,"), "https end comma");
        assertEquals("vím: <a href=\"https://www.vybor.cz/Dispatcher?page=articleDetail&id=563\" target=\"_blank\">https://www.vybor.cz/Dispatcher?page=articleDetail&id=563</a>,", JspSnippets.makeHyperlinks("vím: https://www.vybor.cz/Dispatcher?page=articleDetail&id=563,"), "https query string");
        
        assertEquals("<a href=\"https://www.seznam.cz\" target=\"_blank\">https://www.seznam.cz</a>", JspSnippets.makeHyperlinks("<a href=\"https://www.seznam.cz\" target=\"_blank\">https://www.seznam.cz</a>"), "https link alone");
        
        assertEquals("Ahoj @Jarda", JspSnippets.makeHyperlinks("Ahoj @Jarda"), "Negative test");
        assertEquals("<a href=\"mailto:test@test.edu\">test@test.edu</a>", JspSnippets.makeHyperlinks("test@test.edu"), "email alone");
        assertEquals("<a href=\"mailto:test@test.edu\">test@test.edu</a>  bla.", JspSnippets.makeHyperlinks("test@test.edu  bla."), "email begin");
        assertEquals("Bla <a href=\"mailto:test@test.edu\">test@test.edu</a>", JspSnippets.makeHyperlinks("Bla test@test.edu"), "email end");
        assertEquals("Bla <a href=\"mailto:test@test.edu\">test@test.edu</a>.", JspSnippets.makeHyperlinks("Bla test@test.edu."), "email dot");
        assertEquals("Bla <a href=\"mailto:test@test.edu\">test@test.edu</a>,", JspSnippets.makeHyperlinks("Bla test@test.edu,"), "email comma");
        
        assertEquals("<a href=\"mailto:test@test.edu\">test@test.edu</a>", JspSnippets.makeHyperlinks("<a href=\"mailto:test@test.edu\">test@test.edu</a>"), "email link alone");
        
    }
    
}
