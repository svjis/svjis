/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.svjis.bean;

/**
 *
 * @author jarberan
 */
public class Boolean {
    
    private boolean value;

    public Boolean() {
        
    }
    
    public Boolean(boolean value) {
        this.value = value;
    }
    
    /**
     * @return the value
     */
    public boolean isValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(boolean value) {
        this.value = value;
    }
}
