/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.svjis.servlet.cmd;

import cz.svjis.servlet.ICommand;
import java.sql.Connection;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author jaroslav_b
 */
public class LostPasswordCmd implements ICommand {

    @Override
    public void run(HttpServletRequest request, HttpServletResponse response, Connection cnn) throws Exception {
        RequestDispatcher rd = request.getRequestDispatcher("/LostPassword_form.jsp");
        rd.forward(request, response);
        return;
    }
}
