/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.svjis.validator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 *
 * @author jaroslav_b
 */
public class ValidatorTest {
    
    @Test
    @DisplayName("Positive int validation")
    public void testValidatePositiveInteger() {
        assertFalse(Validator.validateInteger(null ,0, 1), "PositiveInteger < null");
        assertFalse(Validator.validateInteger("" ,0, 1), "PositiveInteger < \"\"");
        assertFalse(Validator.validateInteger("0hello" ,0, 1), "PositiveInteger < 0hello");
        assertFalse(Validator.validateInteger("-1" ,0, 1), "PositiveInteger < -1");
        assertTrue(Validator.validateInteger("0" ,0, 1), "PositiveInteger < 0");
        assertTrue(Validator.validateInteger("1" ,0, 1), "PositiveInteger < 1");
        assertFalse(Validator.validateInteger("2" ,0, 1), "PositiveInteger < over max");
    }
    
    @Test
    @DisplayName("String validation")
    public void testValidateString() {
        assertFalse(Validator.validateString(null ,1, 100), "String < null");
        assertFalse(Validator.validateString("" ,1, 100), "String < \"\"");
        assertFalse(Validator.validateString("123456" ,1, 5), "String < over max");
        assertTrue(Validator.validateString("" ,0, 5), "String < \"\"");
        assertTrue(Validator.validateString("1" ,1, 5), "String < 1");
        assertTrue(Validator.validateString("12345" ,1, 5), "String < 5");
        assertFalse(Validator.validateString("select * from user;" ,1, 100), "SQL injection");
    }
    
    @Test
    @DisplayName("fixTextInput")
    public void testFixTextInput() {
        
        assertEquals("<b>Ahoj</b>", Validator.fixTextInput("<b>Ahoj</b>", true), "HTML enabled");
        assertEquals("&lt;b&gt;Ahoj&lt;/b&gt;", Validator.fixTextInput("<b>Ahoj</b>", false), "HTML disabled");
        assertEquals(null, Validator.fixTextInput(null, false), "null pointer");
    }
}
