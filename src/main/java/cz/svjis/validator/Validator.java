/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.svjis.validator;

/**
 *
 * @author jaroslav_b
 */
public class Validator {
    
    public static boolean validatePositiveInteger(String s) {
        int i;
        
        try {
            i = Integer.valueOf(s);
        } catch (Exception ex) {
            return false;
        }
        
        if (i < 0) {
            return false;
        }
        
        return true;
    }
    
    public static boolean validateString(String s, int minLen, int maxLen) {
        if (s == null) {
            return false;
        }
        
        if (s.length() < minLen) {
            return false;
        }
        
        if (s.length() > maxLen) {
            return false;
        }
        
        return true;
    }
}
