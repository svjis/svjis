/*
 *       ValidatorTest.java
 *
 *       This file is part of SVJIS project.
 *       https://github.com/svjis/svjis
 *
 *       SVJIS is free software; you can redistribute it and/or modify
 *       it under the terms of the GNU General Public License as published by
 *       the Free Software Foundation; either version 3 of the License, or
 *       (at your option) any later version. <http://www.gnu.org/licenses/>
 */

package cz.svjis.validator;

import javax.servlet.http.HttpServletRequest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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

    @Test
    @DisplayName("getString")
    public void testGetString() throws InputValidationException {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getParameter("testNull")).thenReturn(null);
        when(request.getParameter("testValue")).thenReturn("value");
        when(request.getParameter("testInject")).thenReturn("Commit;");
        when(request.getParameter("testHtml")).thenReturn("<b>Hello</b>");

        assertEquals(null, Validator.getString(request, "testNull", 0, 20, true, false));
        assertThrows(InputValidationException.class, new Executable() {
            public void execute() throws Exception {
                Validator.getString(request, "testNull", 0, 10, false, false);
            }
        });

        assertEquals("value", Validator.getString(request, "testValue", 0, 20, false, true), "Positive test");
        assertThrows(InputValidationException.class, new Executable() {
            public void execute() throws Exception {
                Validator.getString(request, "testValue", 0, 4, false, false);
            }
        });

        assertThrows(InputValidationException.class, new Executable() {
            public void execute() throws Exception {
                Validator.getString(request, "testInject", 0, 10, false, false);
            }
        });

        assertEquals("<b>Hello</b>", Validator.getString(request, "testHtml", 0, 20, false, true), "HTML enabled");
        
        assertThrows(InputValidationException.class, new Executable() {
            public void execute() throws Exception {
                Validator.getString(request, "testHtml", 0, 20, false, false);
            }
        });
        
        assertEquals("&lt;b&gt;Hello&lt;/b&gt;", Validator.getString(request, "testHtml", 0, 50, false, false), "HTML disabled");
    }

    @Test
    @DisplayName("getInt")
    public void testGetInt() throws InputValidationException {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getParameter("testNull")).thenReturn(null);
        when(request.getParameter("testValue")).thenReturn("10");
        when(request.getParameter("testInject")).thenReturn("10Commit;");

        assertEquals(0, Validator.getInt(request, "testNull", 0, 20, true));
        assertThrows(InputValidationException.class, new Executable() {
            public void execute() throws Exception {
                Validator.getInt(request, "testNull", 0, 20, false);
            }
        });

        assertEquals(10, Validator.getInt(request, "testValue", 0, 20, true), "Positive test");
        assertThrows(InputValidationException.class, new Executable() {
            public void execute() throws Exception {
                Validator.getInt(request, "testValue", 0, 5, false);
            }
        });

        assertThrows(InputValidationException.class, new Executable() {
            public void execute() throws Exception {
                Validator.getInt(request, "testInject", 0, 20, true);
            }
        });
    }

    @Test
    @DisplayName("getBoolean")
    public void testGetBoolean() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getParameter("testNull")).thenReturn(null);
        when(request.getParameter("testValue")).thenReturn("10");
        when(request.getParameter("testZero")).thenReturn("0");

        assertFalse(Validator.getBoolean(request, "testNull"));
        assertTrue(Validator.getBoolean(request, "testValue"));
        assertFalse(Validator.getBoolean(request, "testZero"));
    }
}
