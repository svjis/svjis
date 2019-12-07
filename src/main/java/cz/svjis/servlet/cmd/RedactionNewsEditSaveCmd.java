/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.svjis.servlet.cmd;

import cz.svjis.bean.MiniNews;
import cz.svjis.bean.MiniNewsDAO;
import cz.svjis.servlet.CmdContext;
import cz.svjis.servlet.Command;
import cz.svjis.validator.Validator;
import java.text.SimpleDateFormat;
import javax.servlet.RequestDispatcher;

/**
 *
 * @author jaroslav_b
 */
public class RedactionNewsEditSaveCmd extends Command {

    public RedactionNewsEditSaveCmd(CmdContext ctx) {
        super(ctx);
    }

    @Override
    public void execute() throws Exception {
        
        String parId = getRequest().getParameter("id");
        String parTime = Validator.fixTextInput(getRequest().getParameter("time"), false);
        String parLangId = getRequest().getParameter("language");
        String parBody = Validator.fixTextInput(getRequest().getParameter("body"), true);
        
        if (!validateInput(parId, parTime, parLangId, parBody)) {
            RequestDispatcher rd = getRequest().getRequestDispatcher("/InputValidationError.jsp");
            rd.forward(getRequest(), getResponse());
            return;
        }
        
        MiniNewsDAO newsDao = new MiniNewsDAO(getCnn());
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");

        int id = Integer.parseInt(parId);
        MiniNews n = null;
        if (id == 0) {
            n = new MiniNews();
            n.setId(id);
            n.setCreatedById(getUser().getId());
            n.setCompanyId(getUser().getCompanyId());
        } else {
            n = newsDao.getMiniNews(getUser(), id);
        }
        n.setTime(sdf.parse(parTime));
        n.setLanguageId(Integer.parseInt(parLangId));
        n.setPublished(getRequest().getParameter("publish") != null);
        n.setBody(parBody);
        if (id == 0) {
            n.setId(newsDao.insertMiniNews(n));
        } else {
            newsDao.modifyMiniNews(n);
        }
        String url = "Dispatcher?page=redactionNewsEdit&id=" + n.getId();
        getRequest().setAttribute("url", url);
        RequestDispatcher rd = getRequest().getRequestDispatcher("/_refresh.jsp");
        rd.forward(getRequest(), getResponse());
    }
    
    private boolean validateInput(String parId, String parTime, String parLangId, String parBody) {
        boolean result = true;
        
        if (!Validator.validateInteger(parId, 0, Validator.maxIntAllowed)) {
            result = false;
        }
        
        if (!Validator.validateString(parTime, 0, 30)) {
            result = false;
        }
        
        if (!Validator.validateInteger(parLangId, 0, Validator.maxIntAllowed)) {
            result = false;
        }
        
        if (!Validator.validateString(parBody, 0, 100000)) {
            result = false;
        }

        return result;
    }
}
