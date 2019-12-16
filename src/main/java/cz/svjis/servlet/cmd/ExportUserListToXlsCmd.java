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
import cz.svjis.servlet.CmdContext;
import cz.svjis.servlet.Command;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import jxl.Workbook;
import jxl.format.Colour;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.NumberFormats;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

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
            getResponse().setContentType("application/vnd.ms-excel");
            getResponse().setHeader("Content-Disposition", "attachment; filename=UserList_" + sdf.format(new Date()) + ".xls");
            outb = getResponse().getOutputStream();
            WritableWorkbook w = Workbook.createWorkbook(outb);
            WritableSheet s = w.createSheet("RequestList", 0);

            LanguageDAO languageDao = new LanguageDAO(getCnn());
            Language lang = languageDao.getDictionary(getUser().getLanguageId());
            UserDAO userDao = new UserDAO(getCnn());
            ArrayList<User> userList = userDao.getUserList(getUser().getCompanyId(), false, 0, true);

            WritableCellFormat bold = new WritableCellFormat(new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD));
            WritableCellFormat boldGr = new WritableCellFormat(new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD));
            boldGr.setBackground(Colour.GREY_25_PERCENT);

            WritableCellFormat std = new WritableCellFormat(new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD));
            std.setShrinkToFit(true);
            std.setWrap(true);
            std.setVerticalAlignment(VerticalAlignment.TOP);

            WritableCellFormat num = new WritableCellFormat(NumberFormats.INTEGER);
            num.setVerticalAlignment(VerticalAlignment.TOP);
            WritableCellFormat dateTimeFormat = new WritableCellFormat(new jxl.write.DateFormat("dd.MM.yyyy HH:mm"));
            dateTimeFormat.setVerticalAlignment(VerticalAlignment.TOP);

            int l = 0;
            int c = 0;

            //-- Header
            s.addCell(new Label(c, l, lang.getText("User list"), bold));
            l += 2;
            s.setColumnView(c, 14);
            s.addCell(new Label(c++, l, lang.getText("Salutation"), boldGr));
            s.setColumnView(c, 14);
            s.addCell(new Label(c++, l, lang.getText("First name"), boldGr));
            s.setColumnView(c, 14);
            s.addCell(new Label(c++, l, lang.getText("Last name"), boldGr));
            s.setColumnView(c, 14);
            s.addCell(new Label(c++, l, lang.getText("Language"), boldGr));
            s.setColumnView(c, 30);
            s.addCell(new Label(c++, l, lang.getText("Address"), boldGr));
            s.setColumnView(c, 14);
            s.addCell(new Label(c++, l, lang.getText("City"), boldGr));
            s.setColumnView(c, 14);
            s.addCell(new Label(c++, l, lang.getText("Post code"), boldGr));
            s.setColumnView(c, 14);
            s.addCell(new Label(c++, l, lang.getText("Country"), boldGr));
            s.setColumnView(c, 20);
            s.addCell(new Label(c++, l, lang.getText("Fixed phone"), boldGr));
            s.setColumnView(c, 20);
            s.addCell(new Label(c++, l, lang.getText("Cell phone"), boldGr));
            s.setColumnView(c, 30);
            s.addCell(new Label(c++, l, lang.getText("E-Mail"), boldGr));
            s.setColumnView(c, 14);
            s.addCell(new Label(c++, l, lang.getText("Enabled"), boldGr));
            s.setColumnView(c, 20);
            s.addCell(new Label(c++, l, lang.getText("Last login"), boldGr));

            Iterator<User> i = userList.iterator();
            while (i.hasNext()) {
                User u = i.next();
                l++;
                c = 0;

                s.addCell(new jxl.write.Label(c++, l, u.getSalutation(), std));
                s.addCell(new jxl.write.Label(c++, l, u.getFirstName(), std));
                s.addCell(new jxl.write.Label(c++, l, u.getLastName(), std));
                //s.addCell(new jxl.write.Number(c++, l, u.getLanguageId(), num));
                s.addCell(new jxl.write.Label(c++, l, languageDao.getLanguage(u.getLanguageId()).getDescription(), std));
                s.addCell(new jxl.write.Label(c++, l, u.getAddress(), std));
                s.addCell(new jxl.write.Label(c++, l, u.getCity(), std));
                s.addCell(new jxl.write.Label(c++, l, u.getPostCode(), std));
                s.addCell(new jxl.write.Label(c++, l, u.getCountry(), std));
                s.addCell(new jxl.write.Label(c++, l, u.getFixedPhone(), std));
                s.addCell(new jxl.write.Label(c++, l, u.getCellPhone(), std));
                s.addCell(new jxl.write.Label(c++, l, u.geteMail(), std));
                s.addCell(new jxl.write.Label(c++, l, (u.isEnabled()) ? lang.getText("yes") : lang.getText("no"), std));
                if (u.getLastLogin() != null) {
                    s.addCell(new jxl.write.DateTime(c++, l, u.getLastLogin(), dateTimeFormat));
                } else {
                    c++;
                }
            }

            w.write();
            w.close();
        } finally {
            if (outb != null) {
                outb.close();
            }
        }
    }
}
