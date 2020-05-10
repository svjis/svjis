/*
 *       BuildingUnit.java
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
public class BuildingUnit {
    private int id;
    private int buildingId;
    private int buildingUnitTypeId;
    private String buildingUnitType;
    private String registrationId;
    private String description;
    private int numerator;
    private int denominator;

    public BuildingUnit() {
        clean();
    }
    
    public void clean() {
        id = 0;
        buildingId = 0;
        buildingUnitTypeId = 0;
        buildingUnitType = "";
        registrationId = "";
        description = "";
        numerator = 0;
        denominator = 0;
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
     * @return the buildingId
     */
    public int getBuildingId() {
        return buildingId;
    }

    /**
     * @param buildingId the buildingId to set
     */
    public void setBuildingId(int buildingId) {
        this.buildingId = buildingId;
    }

    /**
     * @return the buildingUnitTypeId
     */
    public int getBuildingUnitTypeId() {
        return buildingUnitTypeId;
    }

    /**
     * @param buildingUnitTypeId the buildingUnitTypeId to set
     */
    public void setBuildingUnitTypeId(int buildingUnitTypeId) {
        this.buildingUnitTypeId = buildingUnitTypeId;
    }

    /**
     * @return the buildingUnitType
     */
    public String getBuildingUnitType() {
        return buildingUnitType;
    }

    /**
     * @param buildingUnitType the buildingUnitType to set
     */
    public void setBuildingUnitType(String buildingUnitType) {
        this.buildingUnitType = buildingUnitType;
    }

    /**
     * @return the registrationId
     */
    public String getRegistrationId() {
        return registrationId;
    }

    /**
     * @param registrationId the registrationId to set
     */
    public void setRegistrationId(String registrationId) {
        this.registrationId = registrationId;
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
     * @return the numerator
     */
    public int getNumerator() {
        return numerator;
    }

    /**
     * @param numerator the numerator to set
     */
    public void setNumerator(int numerator) {
        this.numerator = numerator;
    }

    /**
     * @return the denominator
     */
    public int getDenominator() {
        return denominator;
    }

    /**
     * @param denominator the denominator to set
     */
    public void setDenominator(int denominator) {
        this.denominator = denominator;
    }
}
