/*
 *       FaultReportingListCmd.java
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

import cz.svjis.bean.FaultReport;
import cz.svjis.bean.FaultReportDAO;
import cz.svjis.bean.FaultReportMenuCounters;
import cz.svjis.bean.Language;
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
        
        String parPage = Validator.getString(getRequest(), "page", 0, Validator.MAX_STRING_LEN_ALLOWED, false, false);
        int parPageNo = Validator.getInt(getRequest(), "pageNo", 0, Validator.MAX_INT_ALLOWED, true);
        String parSearch = Validator.getString(getRequest(), "search", 0, Validator.MAX_STRING_LEN_ALLOWED, true, false);
        
        FaultReportDAO faultDao = new FaultReportDAO(getCnn());
        
        int pageNo = (parPageNo == 0) ? 1 : parPageNo;
        int pageSize = getSetup().getFaultPageSize();
        
        SliderImpl sl = new SliderImpl();
        sl.setPageId(parPage);
        sl.setSliderWide(10);
        sl.setCurrentPage(pageNo);
        sl.setNumOfItemsAtPage(pageSize);
        
        ArrayList<FaultReport> reportList = new ArrayList<>();
        if (parPage.equals("faultReportingList")) {
            sl.setTotalNumOfItems(faultDao.getNumOfFaults(getCompany().getId(), 0));
            reportList = new ArrayList<>(faultDao.getFaultList(getCompany().getId(), pageNo, pageSize, 0));
        }
        if (parPage.equals("faultReportingListCreatedByMe")) {
            sl.setTotalNumOfItems(faultDao.getNumOfFaultsByCreator(getCompany().getId(), getUser().getId()));
            reportList = new ArrayList<>(faultDao.getFaultListByCreator(getCompany().getId(), pageNo, pageSize, getUser().getId()));
        }
        if (parPage.equals("faultReportingListAssignedToMe")) {
            sl.setTotalNumOfItems(faultDao.getNumOfFaultsByResolver(getCompany().getId(), getUser().getId()));
            reportList = new ArrayList<>(faultDao.getFaultListByResolver(getCompany().getId(), pageNo, pageSize, getUser().getId()));
        }
        if (parPage.equals("faultReportingListClosed")) {
            sl.setTotalNumOfItems(faultDao.getNumOfFaults(getCompany().getId(), 1));
            reportList = new ArrayList<>(faultDao.getFaultList(getCompany().getId(), pageNo, pageSize, 1));
        }
        if (parPage.equals("faultReportingListSearch")) {
            if (parSearch.length() < 3) {
                Language lang = (Language) this.getRequest().getSession().getAttribute("language");
                getRequest().setAttribute("messageHeader", lang.getText("Search"));
                getRequest().setAttribute("message", lang.getText("Text to be searched should has 3 chars at least."));
                RequestDispatcher rd = getRequest().getRequestDispatcher("/WEB-INF/jsp/_message.jsp");
                rd.forward(getRequest(), getResponse());
                return;
            }

            sl.setTotalNumOfItems(faultDao.getFaultListSizeFromSearch(getCompany().getId(), parSearch));

            if (sl.getTotalNumOfItems() == 0) {
                Language lang = (Language) this.getRequest().getSession().getAttribute("language");
                getRequest().setAttribute("messageHeader", lang.getText("Search"));
                getRequest().setAttribute("message", lang.getText("Nothing found."));
                RequestDispatcher rd = getRequest().getRequestDispatcher("/WEB-INF/jsp/_message.jsp");
                rd.forward(getRequest(), getResponse());
                return;
            }

            reportList = new ArrayList<>(faultDao.getFaultListFromSearch(getCompany().getId(), pageNo, pageSize, parSearch));
        }

        getRequest().setAttribute("searchKey", parSearch);
        getRequest().setAttribute("slider", sl);
        getRequest().setAttribute("reportList", reportList);
        
        FaultReportMenuCounters counters = faultDao.getMenuCounters(getCompany().getId(), getUser().getId());
        getRequest().setAttribute("counters", counters);
        
        RequestDispatcher rd = getRequest().getRequestDispatcher("/WEB-INF/jsp/Faults_reportList.jsp");
        rd.forward(getRequest(), getResponse());
    }
}
