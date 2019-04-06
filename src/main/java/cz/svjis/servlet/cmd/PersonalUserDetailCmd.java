/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.svjis.servlet.cmd;

import cz.svjis.bean.Language;
import cz.svjis.bean.LanguageDAO;
import cz.svjis.servlet.CmdContext;
import cz.svjis.servlet.Command;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;

/**
 *
 * @author jaroslav_b
 */
public class PersonalUserDetailCmd extends Command {

    public PersonalUserDetailCmd(CmdContext ctx) {
        super(ctx);
    }

    @Override
    public void execute() throws Exception {
        LanguageDAO languageDao = new LanguageDAO(getCnn());
        
        ArrayList<Language> languageList = languageDao.getLanguageList();
        getRequest().setAttribute("languageList", languageList);
        RequestDispatcher rd = getRequest().getRequestDispatcher("/PersonalSettings_userDetail.jsp");
        rd.forward(getRequest(), getResponse());
    }
}
