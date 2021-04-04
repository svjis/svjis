/*
 *       ExportBuildingUnitListToXlsCmd.java
 *
 *       This file is part of SVJIS project.
 *       https://github.com/svjis/svjis
 *
 *       SVJIS is free software; you can redistribute it and/or modify
 *       it under the terms of the GNU General Public License as published by
 *       the Free Software Foundation; either version 3 of the License, or
 *       (at your option) any later version. <http://www.gnu.org/licenses/>
 */

package cz.svjis.servlet.cmd;

import cz.svjis.bean.BuildingDAO;
import cz.svjis.bean.BuildingUnit;
import cz.svjis.bean.Language;
import cz.svjis.bean.LanguageDAO;
import cz.svjis.bean.User;
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
public class ExportBuildingUnitListToXlsCmd extends Command {

    public ExportBuildingUnitListToXlsCmd(CmdContext ctx) {
        super(ctx);
    }

    @Override
    public void execute() throws Exception {
        OutputStream outb = null;

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
            getResponse().setContentType(ExcelCreator.CONTENT_TYPE);
            getResponse().setHeader("Content-Disposition", "attachment; filename=BuildingUnitList_" + sdf.format(new Date()) + ".xlsx");
            outb = getResponse().getOutputStream();

            LanguageDAO languageDao = new LanguageDAO(getCnn());
            Language lang = languageDao.getDictionary(getUser().getLanguageId());
            BuildingDAO buildingDao = new BuildingDAO(getCnn());
            ArrayList<BuildingUnit> buildingUnitList = new ArrayList<>(buildingDao.getBuildingUnitList(
                    buildingDao.getBuilding(getUser().getCompanyId()).getId(),
                    0));

            ExcelCreator ec = new ExcelCreator();
            ec.createWorkbook();
            ec.createSheet(lang.getText("Building unit list"));
            ArrayList<String> line = new ArrayList<>();
            line.add(lang.getText("Building unit list"));
            ec.addLine(line, ec.getHeaderStyle());
            line = new ArrayList<>();
            ec.addLine(line, ec.getNormalStyle());
            line = new ArrayList<>();
            line.add(lang.getText("Type"));
            line.add(lang.getText("Registration Id."));
            line.add(lang.getText("Description"));
            line.add(lang.getText("Owner list"));
            line.add(lang.getText("Numerator"));
            line.add(lang.getText("Denominator"));
            ec.addLine(line, ec.getTableHeaderStyle());
            
            for (BuildingUnit u: buildingUnitList) {
                line = new ArrayList<>();
                line.add(u.getBuildingUnitType());
                line.add(u.getRegistrationId());
                line.add(u.getDescription());
                line.add("");
                line.add(String.valueOf(u.getNumerator()));
                line.add(String.valueOf(u.getDenominator()));
                ec.addLine(line, ec.getNormalStyle());
                
                ArrayList<User> userList = new ArrayList<>(buildingDao.getBuildingUnitHasUserList(u.getId()));
                for (User owner: userList) {
                    line = new ArrayList<>();
                    line.add("");
                    line.add("");
                    line.add("");
                    line.add(String.valueOf(owner.getSalutation() + " " + owner.getFirstName() + " " + owner.getLastName()).trim());
                    ec.addLine(line, ec.getNormalStyle());
                }
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
