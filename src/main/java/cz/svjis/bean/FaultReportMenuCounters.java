/*
 *       FaultReportMenuCounters.java
 *
 *       This file is part of SVJIS project.
 *       https://github.com/svjis/svjis
 *
 *       SVJIS is free software; you can redistribute it and/or modify
 *       it under the terms of the GNU General Public License as published by
 *       the Free Software Foundation; either version 3 of the License, or
 *       (at your option) any later version. <http://www.gnu.org/licenses/>
 */

package cz.svjis.bean;

/**
 *
 * @author jarberan
 */
public class FaultReportMenuCounters {
    
    private int allOpenCnt;
    private int allCreatedByMeCnt;
    private int allAssignedToMeCnt;
    private int allClosedCnt;

    /**
     * @return the allOpenCnt
     */
    public int getAllOpenCnt() {
        return allOpenCnt;
    }

    /**
     * @param allOpenCnt the allOpenCnt to set
     */
    public void setAllOpenCnt(int allOpenCnt) {
        this.allOpenCnt = allOpenCnt;
    }

    /**
     * @return the allCreatedByMeCnt
     */
    public int getAllCreatedByMeCnt() {
        return allCreatedByMeCnt;
    }

    /**
     * @param allCreatedByMeCnt the allCreatedByMeCnt to set
     */
    public void setAllCreatedByMeCnt(int allCreatedByMeCnt) {
        this.allCreatedByMeCnt = allCreatedByMeCnt;
    }

    /**
     * @return the allAssignedToMeCnt
     */
    public int getAllAssignedToMeCnt() {
        return allAssignedToMeCnt;
    }

    /**
     * @param allAssignedToMeCnt the allAssignedToMeCnt to set
     */
    public void setAllAssignedToMeCnt(int allAssignedToMeCnt) {
        this.allAssignedToMeCnt = allAssignedToMeCnt;
    }

    /**
     * @return the allClosedCnt
     */
    public int getAllClosedCnt() {
        return allClosedCnt;
    }

    /**
     * @param allClosedCnt the allClosedCnt to set
     */
    public void setAllClosedCnt(int allClosedCnt) {
        this.allClosedCnt = allClosedCnt;
    }
}
