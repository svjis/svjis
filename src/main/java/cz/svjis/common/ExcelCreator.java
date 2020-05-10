/*
 *       ExcelCreator.java
 *
 *       This file is part of SVJIS project.
 *       https://github.com/svjis/svjis
 *
 *       SVJIS is free software; you can redistribute it and/or modify
 *       it under the terms of the GNU General Public License as published by
 *       the Free Software Foundation; either version 3 of the License, or
 *       (at your option) any later version. <http://www.gnu.org/licenses/>
 */

package cz.svjis.common;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author jarberan
 */
public class ExcelCreator {
    
    public static final String CONTENT_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private int line;
    private int maxCols;
    
    private CellStyle headerStyle;
    private CellStyle tableHeaderStyle;
    private CellStyle normalStyle;
    
    public void createWorkbook() {
        workbook = new XSSFWorkbook();
        Font font = workbook.createFont();
        font.setBold(true);
        
        headerStyle = workbook.createCellStyle();
        getHeaderStyle().setFont(font);
        
        tableHeaderStyle = workbook.createCellStyle();
        getTableHeaderStyle().setFont(font);
        getTableHeaderStyle().setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        getTableHeaderStyle().setFillPattern(FillPatternType.SOLID_FOREGROUND);
        
        normalStyle = workbook.createCellStyle();
    }
    
    public void createSheet(String name) {
        sheet = workbook.createSheet(name);
        line = 0;
        maxCols = 0;
    }
    
    public void writeWorkbook(OutputStream stream) throws IOException {
        for (int i = 0; i < maxCols; i++) {
            sheet.autoSizeColumn(i);
        }
        workbook.write(stream);
    }
    
    public void closeWorkbook() throws IOException {
        workbook.close();
    }
    
    public void addLine(List<String> columns, CellStyle style) {
        Row row = sheet.createRow(line++);
        int i = 0;
        for (String c: columns) {
            Cell cell = row.createCell(i++);
            cell.setCellStyle(style);
            cell.setCellValue(c);
            if (maxCols < i) {
                maxCols = i;
            }
        }
    }

    /**
     * @return the headerStyle
     */
    public CellStyle getHeaderStyle() {
        return headerStyle;
    }

    /**
     * @return the tableHeaderStyle
     */
    public CellStyle getTableHeaderStyle() {
        return tableHeaderStyle;
    }

    /**
     * @return the normalStyle
     */
    public CellStyle getNormalStyle() {
        return normalStyle;
    }
    
}
