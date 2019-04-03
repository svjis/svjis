/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.svjis.servlet.cmd;

import cz.svjis.bean.Language;
import cz.svjis.bean.LanguageDAO;
import cz.svjis.bean.UserDAO;
import cz.svjis.servlet.CmdContext;
import cz.svjis.servlet.Command;
import javax.servlet.RequestDispatcher;

/**
 *
 * @author jaroslav_b
 */
public class PersonalUserDetailSaveCmd extends Command {

    public PersonalUserDetailSaveCmd(CmdContext ctx) {
        super(ctx);
    }

    @Override
    public void execute() throws Exception {

        LanguageDAO languageDao = new LanguageDAO(getCnn());
        UserDAO userDao = new UserDAO(getCnn());

        getUser().setSalutation(getRequest().getParameter("salutation"));
        getUser().setFirstName(getRequest().getParameter("firstName"));
        getUser().setLastName(getRequest().getParameter("lastName"));
        getUser().setLanguageId(Integer.valueOf(getRequest().getParameter("language")));
        getUser().setAddress(getRequest().getParameter("address"));
        getUser().setCity(getRequest().getParameter("city"));
        getUser().setPostCode(getRequest().getParameter("postCode"));
        getUser().setCountry(getRequest().getParameter("country"));
        getUser().setFixedPhone(getRequest().getParameter("fixedPhone"));
        getUser().setCellPhone(getRequest().getParameter("cellPhone"));
        getUser().seteMail(getRequest().getParameter("eMail"));
        getUser().setShowInPhoneList(getRequest().getParameter("phoneList") != null);
        userDao.modifyUser(getUser());
        Language language = languageDao.getDictionary(getUser().getLanguageId());
        getRequest().getSession().setAttribute("language", language);
        String url = "Dispatcher?page=psUserDetail";
        getRequest().setAttribute("url", url);
        RequestDispatcher rd = getRequest().getRequestDispatcher("/_refresh.jsp");
        rd.forward(getRequest(), getResponse());
    }
}
