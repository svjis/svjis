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
public class ExportBuildingUnitListToXlsCmd extends Command {

    public ExportBuildingUnitListToXlsCmd(CmdContext ctx) {
        super(ctx);
    }

    @Override
    public void execute() throws Exception {
        OutputStream outb = null;

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
            getResponse().setContentType("application/vnd.ms-excel");
            getResponse().setHeader("Content-Disposition", "attachment; filename=BuildingUnitList_" + sdf.format(new Date()) + ".xls");
            outb = getResponse().getOutputStream();
            WritableWorkbook w = Workbook.createWorkbook(outb);
            WritableSheet s = w.createSheet("RequestList", 0);

            LanguageDAO languageDao = new LanguageDAO(getCnn());
            Language lang = languageDao.getDictionary(getUser().getLanguageId());
            BuildingDAO buildingDao = new BuildingDAO(getCnn());
            ArrayList<BuildingUnit> buildingUnitList = new ArrayList(buildingDao.getBuildingUnitList(
                    buildingDao.getBuilding(getUser().getCompanyId()).getId(),
                    0));

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
            s.addCell(new Label(c, l, lang.getText("Building unit list"), bold));
            l += 2;
            s.setColumnView(c, 20);
            s.addCell(new Label(c++, l, lang.getText("Type"), boldGr));
            s.setColumnView(c, 14);
            s.addCell(new Label(c++, l, lang.getText("Registration Id."), boldGr));
            s.setColumnView(c, 20);
            s.addCell(new Label(c++, l, lang.getText("Description"), boldGr));
            s.setColumnView(c, 30);
            s.addCell(new Label(c++, l, lang.getText("Owner list"), boldGr));
            s.setColumnView(c, 14);
            s.addCell(new Label(c++, l, lang.getText("Numerator"), boldGr));
            s.setColumnView(c, 14);
            s.addCell(new Label(c++, l, lang.getText("Denominator"), boldGr));

            Iterator<BuildingUnit> i = buildingUnitList.iterator();
            while (i.hasNext()) {
                BuildingUnit u = i.next();
                l++;
                c = 0;

                s.addCell(new jxl.write.Label(c++, l, u.getBuildingUnitType(), std));
                s.addCell(new jxl.write.Label(c++, l, u.getRegistrationId(), std));
                s.addCell(new jxl.write.Label(c++, l, u.getDescription(), std));
                c++;
                s.addCell(new jxl.write.Number(c++, l, u.getNumerator(), num));
                s.addCell(new jxl.write.Number(c++, l, u.getDenominator(), num));

                ArrayList<User> userList = new ArrayList(buildingDao.getBuildingUnitHasUserList(u.getId()));
                Iterator<User> userIterator = userList.iterator();
                while (userIterator.hasNext()) {
                    User owner = userIterator.next();
                    l++;
                    c = 3;
                    s.addCell(new jxl.write.Label(c++, l,
                            String.valueOf(owner.getSalutation() + " " + owner.getFirstName() + " " + owner.getLastName()).trim(), std));
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
