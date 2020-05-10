/*
 *       Building.java
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
