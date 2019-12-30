/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.svjis.servlet.cmd;

import cz.svjis.bean.FaultReport;
import cz.svjis.bean.FaultReportDAO;
import cz.svjis.bean.FaultReportMenuCounters;
import cz.svjis.bean.SliderImpl;
import cz.svjis.servlet.CmdContext;
import cz.svjis.servlet.Command;
import cz.svjis.validator.Validator;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;

/**
 *
 * @author jarberan
 */
public class FaultReportingListCmd extends Command {
    
    public FaultReportingListCmd(CmdContext ctx) {
        super(ctx);
    }

    @Override
    public void execute() throws Exception {
        String parPage = getRequest().getParameter("page");
        String parPageNo = getRequest().getParameter("pageNo");
        String parSearch = Validator.fixTextInput(getRequest().getParameter("search"), false);
        
        if (!validateInput(parPageNo, parSearch)) {
            RequestDispatcher rd = getRequest().getRequestDispatcher("/InputValidationError.jsp");
            rd.forward(getRequest(), getResponse());
            return;
        }
        
        FaultReportDAO faultDao = new FaultReportDAO(getCnn());
        
        int pageNo = (parPageNo == null) ? 1 : Integer.valueOf(parPageNo);
        int pageSize = (getSetup().getProperty("faults.page.size") == null) ? 10 : Integer.valueOf(getSetup().getProperty("faults.page.size"));
        
        SliderImpl sl = new SliderImpl();
        sl.setSliderWide(10);
        sl.setCurrentPage(pageNo);
        sl.setNumOfItemsAtPage(pageSize);
        
        ArrayList<FaultReport> reportList = new ArrayList<FaultReport>();
        if (parPage.equals("faultReportingList")) {
            sl.setTotalNumOfItems(faultDao.getNumOfFaults(getCompany().getId(), 0));
            reportList = faultDao.getFaultList(getCompany().getId(), pageNo, pageSize, 0);
        }
        if (parPage.equals("faultReportingListCreatedByMe")) {
            sl.setTotalNumOfItems(faultDao.getNumOfFaultsByCreator(getCompany().getId(), getUser().getId()));
            reportList = faultDao.getFaultListByCreator(getCompany().getId(), pageNo, pageSize, getUser().getId());
        }
        if (parPage.equals("faultReportingListAssignedToMe")) {
            sl.setTotalNumOfItems(faultDao.getNumOfFaultsByResolver(getCompany().getId(), getUser().getId()));
            reportList = faultDao.getFaultListByResolver(getCompany().getId(), pageNo, pageSize, getUser().getId());
        }
        if (parPage.equals("faultReportingListClosed")) {
            sl.setTotalNumOfItems(faultDao.getNumOfFaults(getCompany().getId(), 1));
            reportList = faultDao.getFaultList(getCompany().getId(), pageNo, pageSize, 1);
        }
        if (parPage.equals("faultReportingListSearch")) {
            sl.setTotalNumOfItems(faultDao.getFaultListSizeFromSearch(getCompany().getId(), parSearch));
            reportList = faultDao.getFaultListFromSearch(getCompany().getId(), pageNo, pageSize, parSearch);
        }

        getRequest().setAttribute("slider", sl);
        getRequest().setAttribute("reportList", reportList);
        
        FaultReportMenuCounters counters = faultDao.getMenuCounters(getCompany().getId(), getUser().getId());
        getRequest().setAttribute("counters", counters);
        
        RequestDispatcher rd = getRequest().getRequestDispatcher("/Faults_reportList.jsp");
        rd.forward(getRequest(), getResponse());
    }
    
    private boolean validateInput(String pageNo, String parSearch) {
        boolean result = true;
        
        //-- pageNo can be null
        if ((pageNo != null) && !Validator.validateInteger(pageNo, 0, Validator.maxIntAllowed)) {
            result = false;
        }
        
        //-- parSearch can be null
        if ((parSearch != null) && !Validator.validateString(parSearch, 0, Validator.maxStringLenAllowed)) {
            result = false;
        }
        
        return result;
    }
}
