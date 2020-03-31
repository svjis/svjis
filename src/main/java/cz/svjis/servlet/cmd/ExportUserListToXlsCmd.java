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
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

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
            getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            getResponse().setHeader("Content-Disposition", "attachment; filename=UserList_" + sdf.format(new Date()) + ".xlsx");
            outb = getResponse().getOutputStream();
            try (XSSFWorkbook workbook = new XSSFWorkbook()) {
                XSSFSheet sheet = workbook.createSheet("Users");
                
                LanguageDAO languageDao = new LanguageDAO(getCnn());
                Language lang = languageDao.getDictionary(getUser().getLanguageId());
                UserDAO userDao = new UserDAO(getCnn());
                ArrayList<User> userList = new ArrayList(userDao.getUserList(getUser().getCompanyId(), false, 0, true));
                
                int l = 0;
                int c = 0;
                
                //-- Header
                Row row = sheet.createRow(l++);
                Cell cell = row.createCell(c++);
                cell.setCellValue(lang.getText("User list"));
                l++;
                row = sheet.createRow(l++);
                c = 0;
                cell = row.createCell(c++);
                cell.setCellValue(lang.getText("Salutation"));
                cell = row.createCell(c++);
                cell.setCellValue(lang.getText("First name"));
                cell = row.createCell(c++);
                cell.setCellValue(lang.getText("Last name"));
                cell = row.createCell(c++);
                cell.setCellValue(lang.getText("Language"));
                cell = row.createCell(c++);
                cell.setCellValue(lang.getText("Address"));
                cell = row.createCell(c++);
                cell.setCellValue(lang.getText("City"));
                cell = row.createCell(c++);
                cell.setCellValue(lang.getText("Post code"));
                cell = row.createCell(c++);
                cell.setCellValue(lang.getText("Country"));
                cell = row.createCell(c++);
                cell.setCellValue(lang.getText("Fixed phone"));
                cell = row.createCell(c++);
                cell.setCellValue(lang.getText("Cell phone"));
                cell = row.createCell(c++);
                cell.setCellValue(lang.getText("E-Mail"));
                cell = row.createCell(c++);
                cell.setCellValue(lang.getText("Enabled"));
                cell = row.createCell(c++);
                cell.setCellValue(lang.getText("Last login"));
                
                for (User u: userList) {
                    row = sheet.createRow(l++);
                    c = 0;
                    
                    cell = row.createCell(c++);
                    cell.setCellValue(u.getSalutation());
                    cell = row.createCell(c++);
                    cell.setCellValue(u.getFirstName());
                    cell = row.createCell(c++);
                    cell.setCellValue(u.getLastName());
                    cell = row.createCell(c++);
                    cell.setCellValue(languageDao.getLanguage(u.getLanguageId()).getDescription());
                    cell = row.createCell(c++);
                    cell.setCellValue(u.getAddress());
                    cell = row.createCell(c++);
                    cell.setCellValue(u.getCity());
                    cell = row.createCell(c++);
                    cell.setCellValue(u.getPostCode());
                    cell = row.createCell(c++);
                    cell.setCellValue(u.getCountry());
                    cell = row.createCell(c++);
                    cell.setCellValue(u.getFixedPhone());
                    cell = row.createCell(c++);
                    cell.setCellValue(u.getCellPhone());
                    cell = row.createCell(c++);
                    cell.setCellValue(u.geteMail());
                    cell = row.createCell(c++);
                    cell.setCellValue((u.isEnabled()) ? lang.getText("yes") : lang.getText("no"));
                    if (u.getLastLogin() != null) {
                        cell = row.createCell(c++);
                        cell.setCellValue(sdf2.format(u.getLastLogin()));
                    } else {
                        c++;
                    }
                }
                workbook.write(outb);
            }
        } finally {
            if (outb != null) {
                outb.close();
            }
        }
    }
}
