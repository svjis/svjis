/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.svjis.bean;

/**
 *
 * @author berk
 */
public class Building {
    private int id;
    private int companyId;
    private String address;
    private String city;
    private String postCode;
    private String registrationNo;

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
     * @return the companyId
     */
    public int getCompanyId() {
        return companyId;
    }

    /**
     * @param companyId the companyId to set
     */
    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    /**
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return the city
     */
    public String getCity() {
        return city;
    }

    /**
     * @param city the city to set
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * @return the postingCode
     */
    public String getPostCode() {
        return postCode;
    }

    /**
     * @param postingCode the postingCode to set
     */
    public void setPostCode(String postingCode) {
        this.postCode = postingCode;
    }

    /**
     * @return the registrationId
     */
    public String getRegistrationNo() {
        return registrationNo;
    }

    /**
     * @param registrationId the registrationId to set
     */
    public void setRegistrationNo(String registrationNo) {
        this.registrationNo = registrationNo;
    }
    
    
}
