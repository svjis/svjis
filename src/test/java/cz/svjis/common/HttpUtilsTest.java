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
    }
    
}
