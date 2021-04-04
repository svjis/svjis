/*
 *       InputValidationException.java
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

/**
 *
 * @author jarberan
 */
public class InputValidationException extends Exception {

    private static final long serialVersionUID = -5489466695905327131L;

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
