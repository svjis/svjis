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
import cz.svjis.validator.Validator;
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

        String parSalutation = Validator.fixTextInput(getRequest().getParameter("salutation"), false);
        String parFirstName = Validator.fixTextInput(getRequest().getParameter("firstName"), false);
        String parLastName = Validator.fixTextInput(getRequest().getParameter("lastName"), false);
        String parLangId = getRequest().getParameter("language");
        String parAddress = Validator.fixTextInput(getRequest().getParameter("address"), false);
        String parCity = Validator.fixTextInput(getRequest().getParameter("city"), false);
        String parPostCode = Validator.fixTextInput(getRequest().getParameter("postCode"), false);
        String parCountry = Validator.fixTextInput(getRequest().getParameter("country"), false);
        String parFixedPhone = Validator.fixTextInput(getRequest().getParameter("fixedPhone"), false);
        String parCellPhone = Validator.fixTextInput(getRequest().getParameter("cellPhone"), false);
        String parEMail = Validator.fixTextInput(getRequest().getParameter("eMail"), false);
        
        if (!validateInput(parSalutation, parFirstName, parLastName, parLangId, parAddress, parCity, parPostCode, parCountry, parFixedPhone, parCellPhone, parEMail)) {
            RequestDispatcher rd = getRequest().getRequestDispatcher("/InputValidationError.jsp");
            rd.forward(getRequest(), getResponse());
            return;
        }
        
        getUser().setSalutation(parSalutation);
        getUser().setFirstName(parFirstName);
        getUser().setLastName(parLastName);
        getUser().setLanguageId(Integer.valueOf(parLangId));
        getUser().setAddress(parAddress);
        getUser().setCity(parCity);
        getUser().setPostCode(parPostCode);
        getUser().setCountry(parCountry);
        getUser().setFixedPhone(parFixedPhone);
        getUser().setCellPhone(parCellPhone);
        getUser().seteMail(parEMail);
        getUser().setShowInPhoneList(getRequest().getParameter("phoneList") != null);
        userDao.modifyUser(getUser());
        Language language = languageDao.getDictionary(getUser().getLanguageId());
        getRequest().getSession().setAttribute("language", language);
        String url = "Dispatcher?page=psUserDetail";
        getRequest().setAttribute("url", url);
        RequestDispatcher rd = getRequest().getRequestDispatcher("/_refresh.jsp");
        rd.forward(getRequest(), getResponse());
    }
    
    private boolean validateInput(String parSalutation, String parFirstName, String parLastName, String parLangId, String parAddress, String parCity, String parPostCode, String parCountry, String parFixedPhone, String parCellPhone, String parEMail) {
        boolean result = true;
        
        if (!Validator.validateString(parSalutation, 0, 30)) {
            result = false;
        }
        
        if (!Validator.validateString(parFirstName, 0, 30)) {
            result = false;
        }
        
        if (!Validator.validateString(parLastName, 0, 30)) {
            result = false;
        }
        
        if ((parLangId != null) && !Validator.validateInteger(parLangId, 0, Validator.maxIntAllowed)) {
            result = false;
        }
        
        if (!Validator.validateString(parAddress, 0, 50)) {
            result = false;
        }
        
        if (!Validator.validateString(parCity, 0, 50)) {
            result = false;
        }
        
        if (!Validator.validateString(parPostCode, 0, 10)) {
            result = false;
        }
        
        if (!Validator.validateString(parCountry, 0, 50)) {
            result = false;
        }
        
        if (!Validator.validateString(parFixedPhone, 0, 30)) {
            result = false;
        }
        
        if (!Validator.validateString(parCellPhone, 0, 30)) {
            result = false;
        }
        
        if (!Validator.validateString(parEMail, 0, 50)) {
            result = false;
        }
        
        return result;
    }
}
