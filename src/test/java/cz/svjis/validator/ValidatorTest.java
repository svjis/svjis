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
    
}
