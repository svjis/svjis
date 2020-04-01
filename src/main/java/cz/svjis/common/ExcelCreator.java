/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.svjis.common;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
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
    
    public static final String contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private int line;
    private int maxCols;
    
    public CellStyle headerStyle;
    public CellStyle tableHeaderStyle;
    public CellStyle normalStyle;
    
    public void createWorkbook() {
        workbook = new XSSFWorkbook();
        Font font = workbook.createFont();
        font.setBold(true);
        
        headerStyle = workbook.createCellStyle();
        headerStyle.setFont(font);
        
        tableHeaderStyle = workbook.createCellStyle();
        tableHeaderStyle.setFont(font);
        tableHeaderStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        tableHeaderStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        
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
    
    public void addLine(ArrayList<String> columns, CellStyle style) {
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
    
}
