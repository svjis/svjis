/*
 *       Validator.java
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

/**
 *
 * @author jaroslav_b
 */
public class Validator {
    
    public static final int MAX_INT_ALLOWED = 10000000;
    public static final int MAX_STRING_LEN_ALLOWED = 1000000;

    private Validator() {}
    
    public static String getString(HttpServletRequest request, String parName, int minLen, int maxLen, boolean canBeNull, boolean canContainHtmlTags) throws InputValidationException {
        String val = request.getParameter(parName);
        String msg = "String parameter %s[%s, %s] is not valid. (%s)";
        
        if ((val == null) && canBeNull) {
            return val;
        }
        
        if (!Validator.validateString(val, minLen, maxLen)) {
            throw new InputValidationException(String.format(msg, parName, String.valueOf(minLen), String.valueOf(maxLen), val));
        }
        
        return Validator.fixTextInput(val, canContainHtmlTags);
    }
    
    
    public static int getInt(HttpServletRequest request, String parName, int minInt, int maxInt, boolean canBeNull) throws InputValidationException {
        String val = request.getParameter(parName);
        String msg = "Integer parameter %s[%s, %s] is not valid. (%s)";
        
        if ((val == null) && canBeNull) {
            return 0;
        }
        
        if (!Validator.validateInteger(val, minInt, maxInt)) {
            throw new InputValidationException(String.format(msg, parName, String.valueOf(minInt), String.valueOf(maxInt), val));
        }

        return Integer.valueOf(val);
    }
    
    
    public static boolean getBoolean(HttpServletRequest request, String parName) {
        boolean result;
        if ((request.getParameter(parName) == null) || (request.getParameter(parName).equals("0"))) {
            result = false;
        } else {
            result = true;
        }
        return result;
    }
    
    
    protected static boolean validateInteger(String s, int minInt, int maxInt) {
        int i;
        
        try {
            i = Integer.valueOf(s);
        } catch (NumberFormatException ex) {
            return false;
        }
        
        if (i < minInt) {
            return false;
        }
        
        if (i > maxInt) {
            return false;
        }

        return isInjectionFree(s);
    }
    
    protected static boolean validateString(String s, int minLen, int maxLen) {
        if (s == null) {
            return false;
        }
        
        if (s.length() < minLen) {
            return false;
        }
        
        if (s.length() > maxLen) {
            return false;
        }
        
        return isInjectionFree(s);
    }
    
    private static boolean isInjectionFree(String s) {
        String sup = s.toUpperCase();
        
        return !(sup.contains(";") && 
            (sup.contains("SELECT") || sup.contains("INSERT") || sup.contains("UPDATE") || 
            sup.contains("DELETE") || sup.contains("DROP") || sup.contains("CREATE") || sup.contains("ALTER") || 
            sup.contains("EXECUTE") || sup.contains("COMMIT") || sup.contains("ROLLBACK")));
    }
    
    protected static String fixTextInput(String input, boolean enableHtml) {
        String result = input;
        
        if (result == null) {
            return null;
        }
        
        if (!enableHtml) {
            result = result.replace("<", "&lt;");
            result = result.replace(">", "&gt;");
        }
        
        return result;
    }
}
