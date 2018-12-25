/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.svjis.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author berk
 */
public interface ICommand {
    public void run(HttpServletRequest request, HttpServletResponse response) throws Exception;
}
