/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.svjis.common;

/**
 *
 * @author jarberan
 */
public class Input {
    
    public static String fixTextInput(String input, boolean enableHtml) {
        String result = input;
        
        if (result == null) {
            return null;
        }
        
        if (!enableHtml) {
            result = result.replaceAll("<", "&lt;");
            result = result.replaceAll(">", "&gt;");
        }
        
        return result;
    }
}
