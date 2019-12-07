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
public class InputTest {

    @Test
    @DisplayName("fixTextInput")
    public void testFixTextInput() {
        
        assertEquals("<b>Ahoj</b>", Input.fixTextInput("<b>Ahoj</b>", true), "HTML enabled");
        assertEquals("&lt;b&gt;Ahoj&lt;/b&gt;", Input.fixTextInput("<b>Ahoj</b>", false), "HTML disabled");
    }

}
