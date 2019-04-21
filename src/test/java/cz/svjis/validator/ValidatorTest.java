/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.svjis.validator;

import org.junit.Test;

/**
 *
 * @author jaroslav_b
 */
public class ValidatorTest {
    
    @Test
    public void testValidatePositiveInteger() {
        org.junit.Assert.assertFalse("PositiveInteger < null", Validator.validatePositiveInteger(null));
        org.junit.Assert.assertFalse("PositiveInteger < \"\"", Validator.validatePositiveInteger(""));
        org.junit.Assert.assertFalse("PositiveInteger < 0hello", Validator.validatePositiveInteger("0hello"));
        org.junit.Assert.assertFalse("PositiveInteger < -1", Validator.validatePositiveInteger("-1"));
        org.junit.Assert.assertTrue("PositiveInteger < 0", Validator.validatePositiveInteger("0"));
        org.junit.Assert.assertTrue("PositiveInteger < 1", Validator.validatePositiveInteger("1"));
    }
    
    @Test
    public void testValidateString() {
        org.junit.Assert.assertFalse("String < null", Validator.validateString(null ,1, 100));
        org.junit.Assert.assertFalse("String < \"\"", Validator.validateString("" ,1, 100));
        org.junit.Assert.assertFalse("String < over max", Validator.validateString("123456" ,1, 5));
        org.junit.Assert.assertTrue("String < \"\"", Validator.validateString("" ,0, 5));
        org.junit.Assert.assertTrue("String < 1", Validator.validateString("1" ,1, 5));
        org.junit.Assert.assertTrue("String < 5", Validator.validateString("12345" ,1, 5));
    }
}
