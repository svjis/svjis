/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
