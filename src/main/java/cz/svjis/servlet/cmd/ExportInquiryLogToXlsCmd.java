/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.svjis.servlet.cmd;

import cz.svjis.bean.Inquiry;
import cz.svjis.bean.InquiryDAO;
import cz.svjis.bean.InquiryLog;
import cz.svjis.bean.InquiryOption;
import cz.svjis.bean.Language;
import cz.svjis.bean.LanguageDAO;
import cz.svjis.common.ExcelCreator;
import cz.svjis.servlet.CmdContext;
import cz.svjis.servlet.Command;
import cz.svjis.validator.Validator;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author jarberan
 */
public class ExportInquiryLogToXlsCmd extends Command {
    
    public ExportInquiryLogToXlsCmd(CmdContext ctx) {
        super(ctx);
    }

    @Override
    public void execute() throws Exception {
        
        int parId = Validator.getInt(getRequest(), "id", 0, Validator.MAX_INT_ALLOWED, false);
        OutputStream outb = null;

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
            SimpleDateFormat sdf2 = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
            getResponse().setContentType(ExcelCreator.CONTENT_TYPE);
            getResponse().setHeader("Content-Disposition", "attachment; filename=Inquiry_" + sdf.format(new Date()) + ".xlsx");
            outb = getResponse().getOutputStream();

            LanguageDAO languageDao = new LanguageDAO(getCnn());
            Language lang = languageDao.getDictionary(getUser().getLanguageId());
            
            InquiryDAO inquiryDao = new InquiryDAO(getCnn());
            Inquiry inquiry = new Inquiry();
            ArrayList<InquiryLog> log = new ArrayList<>();
            if (parId != 0) {
                inquiry = inquiryDao.getInquiry(getUser(), parId);
                log = new ArrayList<>(inquiryDao.getInquiryLog(getUser(), parId));
            }

            ExcelCreator ec = new ExcelCreator();
            ec.createWorkbook();
            ec.createSheet(lang.getText("Inquiry"));
            
            ArrayList<String> line = new ArrayList<>();
            line.add(lang.getText("Inquiry"));
            ec.addLine(line, ec.getHeaderStyle());
            
            line = new ArrayList<>();
            line.add(inquiry.getDescription());
            ec.addLine(line, ec.getNormalStyle());
            
            line = new ArrayList<>();
            ec.addLine(line, ec.getNormalStyle());
            
            line = new ArrayList<>();
            line.add(lang.getText("Description"));
            line.add(lang.getText("Votes"));
            ec.addLine(line, ec.getTableHeaderStyle());
            
            for (InquiryOption io: inquiry.getOptionList()) {
                line = new ArrayList<>();
                line.add(io.getDescription());
                line.add(String.valueOf(io.getCount()));
                ec.addLine(line, ec.getNormalStyle());
            }
            
            line = new ArrayList<>();
            ec.addLine(line, ec.getNormalStyle());
            
            line = new ArrayList<>();
            line.add(lang.getText("Inquiry log"));
            ec.addLine(line, ec.getHeaderStyle());
            
            line = new ArrayList<>();
            ec.addLine(line, ec.getNormalStyle());
            
            line = new ArrayList<>();
            line.add(lang.getText("Time"));
            line.add(lang.getText("User"));
            line.add(lang.getText("Option"));
            ec.addLine(line, ec.getTableHeaderStyle());
            
            for (InquiryLog il: log) {
                line = new ArrayList<>();
                line.add(sdf2.format(il.getTime()));
                line.add(il.getUser());
                line.add(il.getOptionDescription());
                ec.addLine(line, ec.getNormalStyle());
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
