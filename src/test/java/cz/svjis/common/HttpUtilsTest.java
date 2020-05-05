/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.svjis.common;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 *
 * @author jarberan
 */
public class HttpUtilsTest {
    
    @Test
    @DisplayName("Highlight regex test")
    public void testValidatePositiveInteger() {
        assertEquals("Ahoj jak se máš?", HttpUtils.envelStrInHtml("Ahoj jak se máš?", "", "*", "#"), "Negative test");
        assertEquals("Ahoj jak se máš?", HttpUtils.envelStrInHtml("Ahoj jak se máš?", "jak sem", "*", "#"), "Negative test");
        assertEquals("Ahoj *jak se# máš?", HttpUtils.envelStrInHtml("Ahoj jak se máš?", "jak se", "*", "#"), "Plain text");
        assertEquals("Ahoj <b>*jak se#</b> máš?", HttpUtils.envelStrInHtml("Ahoj <b>jak se</b> máš?", "jak se", "*", "#"), "Html 1");
        assertEquals("Ahoj *jak <b>se#</b> máš?", HttpUtils.envelStrInHtml("Ahoj jak <b>se</b> máš?", "jak se", "*", "#"), "Html 2");
        assertEquals("Ahoj *ja<b>k s</b>e# máš?", HttpUtils.envelStrInHtml("Ahoj ja<b>k s</b>e máš?", "jak se", "*", "#"), "Html 3");
        assertEquals("Ahoj <strong>jak se</strong> máš?", HttpUtils.envelStrInHtml("Ahoj <strong>jak se</strong> máš?", "strong", "*", "#"), "Html 3");
        assertEquals("Ahoj <font color=\"red\">jak se</font> máš?", HttpUtils.envelStrInHtml("Ahoj <font color=\"red\">jak se</font> máš?", "red", "*", "#"), "Html 3");
    }
    
    @Test
    @DisplayName("Make hyperlinks")
    public void testMakeHyperlins() {
        assertEquals("Ahoj jak se máš?", HttpUtils.makeHyperlins("Ahoj jak se máš?"), "Negative test");
        
        assertEquals("<a href=\"http://www.seznam.cz\" target=\"_blank\">http://www.seznam.cz</a>", HttpUtils.makeHyperlins("http://www.seznam.cz"), "http alone");
        assertEquals("<a href=\"http://www.seznam.cz\" target=\"_blank\">http://www.seznam.cz</a> bla.", HttpUtils.makeHyperlins("http://www.seznam.cz bla."), "http begin");
        assertEquals("Bla <a href=\"http://www.seznam.cz\" target=\"_blank\">http://www.seznam.cz</a>", HttpUtils.makeHyperlins("Bla http://www.seznam.cz"), "http end");
        assertEquals("Bla <a href=\"http://www.seznam.cz\" target=\"_blank\">http://www.seznam.cz</a>.", HttpUtils.makeHyperlins("Bla http://www.seznam.cz."), "http end dot");
        assertEquals("Bla <a href=\"http://www.seznam.cz\" target=\"_blank\">http://www.seznam.cz</a>,", HttpUtils.makeHyperlins("Bla http://www.seznam.cz,"), "http end comma");
        assertEquals("vím: <a href=\"http://www.vybor1491.cz/Dispatcher?page=articleDetail&id=563\" target=\"_blank\">http://www.vybor1491.cz/Dispatcher?page=articleDetail&id=563</a>", HttpUtils.makeHyperlins("vím: http://www.vybor1491.cz/Dispatcher?page=articleDetail&id=563"), "https query string");
        
        assertEquals("<a href=\"https://www.seznam.cz\" target=\"_blank\">https://www.seznam.cz</a>", HttpUtils.makeHyperlins("https://www.seznam.cz"), "https alone");
        assertEquals("<a href=\"https://www.seznam.cz\" target=\"_blank\">https://www.seznam.cz</a> bla.", HttpUtils.makeHyperlins("https://www.seznam.cz bla."), "https begin");
        assertEquals("Bla <a href=\"https://www.seznam.cz\" target=\"_blank\">https://www.seznam.cz</a>", HttpUtils.makeHyperlins("Bla https://www.seznam.cz"), "https end");
        assertEquals("Bla <a href=\"https://www.seznam.cz\" target=\"_blank\">https://www.seznam.cz</a>.", HttpUtils.makeHyperlins("Bla https://www.seznam.cz."), "https end dot");
        assertEquals("Bla <a href=\"https://www.seznam.cz\" target=\"_blank\">https://www.seznam.cz</a>,", HttpUtils.makeHyperlins("Bla https://www.seznam.cz,"), "https end comma");
        assertEquals("vím: <a href=\"https://www.vybor1491.cz/Dispatcher?page=articleDetail&id=563\" target=\"_blank\">https://www.vybor1491.cz/Dispatcher?page=articleDetail&id=563</a>,", HttpUtils.makeHyperlins("vím: https://www.vybor1491.cz/Dispatcher?page=articleDetail&id=563,"), "https query string");
        
        assertEquals("<a href=\"https://www.seznam.cz\" target=\"_blank\">https://www.seznam.cz</a>", HttpUtils.makeHyperlins("<a href=\"https://www.seznam.cz\" target=\"_blank\">https://www.seznam.cz</a>"), "https link alone");
        
        assertEquals("Ahoj @Jarda", HttpUtils.makeHyperlins("Ahoj @Jarda"), "Negative test");
        assertEquals("<a href=\"mailto:test@test.edu\">test@test.edu</a>", HttpUtils.makeHyperlins("test@test.edu"), "email alone");
        assertEquals("<a href=\"mailto:test@test.edu\">test@test.edu</a>  bla.", HttpUtils.makeHyperlins("test@test.edu  bla."), "email begin");
        assertEquals("Bla <a href=\"mailto:test@test.edu\">test@test.edu</a>", HttpUtils.makeHyperlins("Bla test@test.edu"), "email end");
        assertEquals("Bla <a href=\"mailto:test@test.edu\">test@test.edu</a>.", HttpUtils.makeHyperlins("Bla test@test.edu."), "email dot");
        assertEquals("Bla <a href=\"mailto:test@test.edu\">test@test.edu</a>,", HttpUtils.makeHyperlins("Bla test@test.edu,"), "email comma");
        
        assertEquals("<a href=\"mailto:test@test.edu\">test@test.edu</a>", HttpUtils.makeHyperlins("<a href=\"mailto:test@test.edu\">test@test.edu</a>"), "email link alone");
        
    }
    
}
