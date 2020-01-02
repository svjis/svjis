/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.svjis.validator;

/**
 *
 * @author jarberan
 */
public class InputValidationException extends Exception {

    /**
     * Creates a new instance of <code>InputValidationException</code> without
     * detail message.
     */
    public InputValidationException() {
    }

    /**
     * Constructs an instance of <code>InputValidationException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public InputValidationException(String msg) {
        super(msg);
    }
}
