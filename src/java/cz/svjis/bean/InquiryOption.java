/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.svjis.bean;

/**
 *
 * @author berk
 */
public class InquiryOption {
    private int id;
    private int inquiryId;
    private String description;
    private int count;
    private double pct;
    
    public void clear() {
        id = 0;
        inquiryId = 0;
        description = "";
        count = 0;
        pct = 0;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the inquiryId
     */
    public int getInquiryId() {
        return inquiryId;
    }

    /**
     * @param inquiryId the inquiryId to set
     */
    public void setInquiryId(int inquiryId) {
        this.inquiryId = inquiryId;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the count
     */
    public int getCount() {
        return count;
    }

    /**
     * @param count the count to set
     */
    public void setCount(int count) {
        this.count = count;
    }

    /**
     * @return the pct
     */
    public double getPct() {
        return pct;
    }

    /**
     * @param pct the pct to set
     */
    public void setPct(double pct) {
        this.pct = pct;
    }
}
