/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.svjis.servlet.cmd;

import cz.svjis.bean.Language;
import cz.svjis.bean.LanguageDAO;
import cz.svjis.bean.User;
import cz.svjis.bean.UserDAO;
import cz.svjis.common.ExcelCreator;
import cz.svjis.servlet.CmdContext;
import cz.svjis.servlet.Command;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author jaroslav_b
 */
public class ExportUserListToXlsCmd extends Command {

    public ExportUserListToXlsCmd(CmdContext ctx) {
        super(ctx);
    }

    @Override
    public void execute() throws Exception {
        OutputStream outb = null;

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
            SimpleDateFormat sdf2 = new SimpleDateFormat("dd.MM.yyyy HH:mm");
            getResponse().setContentType(ExcelCreator.contentType);
            getResponse().setHeader("Content-Disposition", "attachment; filename=UserList_" + sdf.format(new Date()) + ".xlsx");
            outb = getResponse().getOutputStream();

            LanguageDAO languageDao = new LanguageDAO(getCnn());
            Language lang = languageDao.getDictionary(getUser().getLanguageId());
            UserDAO userDao = new UserDAO(getCnn());
            ArrayList<User> userList = new ArrayList(userDao.getUserList(getUser().getCompanyId(), false, 0, true));

            ExcelCreator ec = new ExcelCreator();
            ec.createWorkbook();
            ec.createSheet(lang.getText("User list"));
            ArrayList<String> line = new ArrayList<>();
            line.add(lang.getText("User list"));
            ec.addLine(line, ec.headerStyle);
            line = new ArrayList<>();
            ec.addLine(line, ec.normalStyle);
            line = new ArrayList<>();
            line.add(lang.getText("Salutation"));
            line.add(lang.getText("First name"));
            line.add(lang.getText("Last name"));
            line.add(lang.getText("Language"));
            line.add(lang.getText("Address"));
            line.add(lang.getText("City"));
            line.add(lang.getText("Post code"));
            line.add(lang.getText("Country"));
            line.add(lang.getText("Fixed phone"));
            line.add(lang.getText("Cell phone"));
            line.add(lang.getText("E-Mail"));
            line.add(lang.getText("Enabled"));
            line.add(lang.getText("Last login"));
            ec.addLine(line, ec.tableHeaderStyle);
                
            for (User u: userList) {
                line = new ArrayList<>();
                line.add(u.getSalutation());
                line.add(u.getFirstName());
                line.add(u.getLastName());
                line.add(languageDao.getLanguage(u.getLanguageId()).getDescription());
                line.add(u.getAddress());
                line.add(u.getCity());
                line.add(u.getPostCode());
                line.add(u.getCountry());
                line.add(u.getFixedPhone());
                line.add(u.getCellPhone());
                line.add(u.geteMail());
                line.add((u.isEnabled()) ? lang.getText("yes") : lang.getText("no"));
                if (u.getLastLogin() != null) {
                    line.add(sdf2.format(u.getLastLogin()));
                }
                ec.addLine(line, ec.normalStyle);
            }
            
            ec.writeWorkbook(outb);
            ec.closeWorkbook();
        } finally {
            if (outb != null) {
                outb.close();
            }
        }
    }
}
