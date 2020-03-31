/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.svjis.servlet.cmd;

import cz.svjis.bean.BuildingDAO;
import cz.svjis.bean.BuildingUnit;
import cz.svjis.bean.Language;
import cz.svjis.bean.LanguageDAO;
import cz.svjis.bean.User;
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
public class ExportBuildingUnitListToXlsCmd extends Command {

    public ExportBuildingUnitListToXlsCmd(CmdContext ctx) {
        super(ctx);
    }

    @Override
    public void execute() throws Exception {
        OutputStream outb = null;

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
            getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            getResponse().setHeader("Content-Disposition", "attachment; filename=BuildingUnitList_" + sdf.format(new Date()) + ".xlsx");
            outb = getResponse().getOutputStream();
            try (XSSFWorkbook workbook = new XSSFWorkbook()) {
                XSSFSheet sheet = workbook.createSheet("Building units");

                LanguageDAO languageDao = new LanguageDAO(getCnn());
                Language lang = languageDao.getDictionary(getUser().getLanguageId());
                BuildingDAO buildingDao = new BuildingDAO(getCnn());
                ArrayList<BuildingUnit> buildingUnitList = new ArrayList(buildingDao.getBuildingUnitList(
                        buildingDao.getBuilding(getUser().getCompanyId()).getId(),
                        0));

                int l = 0;
                int c = 0;

                //-- Header
                Row row = sheet.createRow(l++);
                Cell cell = row.createCell(c++);
                cell.setCellValue(lang.getText("Building unit list"));
                l++;
                row = sheet.createRow(l++);
                c = 0;
                cell = row.createCell(c++);
                cell.setCellValue(lang.getText("Type"));
                cell = row.createCell(c++);
                cell.setCellValue(lang.getText("Registration Id."));
                cell = row.createCell(c++);
                cell.setCellValue(lang.getText("Description"));
                cell = row.createCell(c++);
                cell.setCellValue(lang.getText("Owner list"));
                cell = row.createCell(c++);
                cell.setCellValue(lang.getText("Numerator"));
                cell = row.createCell(c++);
                cell.setCellValue(lang.getText("Denominator"));

                for (BuildingUnit u: buildingUnitList) {
                    row = sheet.createRow(l++);
                    c = 0;

                    cell = row.createCell(c++);
                    cell.setCellValue(u.getBuildingUnitType());
                    cell = row.createCell(c++);
                    cell.setCellValue(u.getRegistrationId());
                    cell = row.createCell(c++);
                    cell.setCellValue(u.getDescription());
                    c++;
                    cell = row.createCell(c++);
                    cell.setCellValue(u.getNumerator());
                    cell = row.createCell(c++);
                    cell.setCellValue(u.getDenominator());

                    ArrayList<User> userList = new ArrayList(buildingDao.getBuildingUnitHasUserList(u.getId()));
                    for (User owner: userList) {
                        row = sheet.createRow(l++);
                        c = 3;
                        cell = row.createCell(c++);
                        cell.setCellValue(String.valueOf(owner.getSalutation() + " " + owner.getFirstName() + " " + owner.getLastName()).trim());
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
