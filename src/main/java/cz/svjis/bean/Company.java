/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.svjis.bean;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author berk
 */
public class Company implements Serializable {
    private int id;
    private String name;
    private String address;
    private String city;
    private String postCode;
    private String phone;
    private String fax;
    private String eMail;
    private String registrationNo;
    private String vatRegistrationNo;
    private Date databaseCreationDate;
    private String internetDomain;
    private String pictureContentType;
    private String pictureFilename;
    private byte[] pictureData;
    private int unitCnt;
    private int boardCnt;
    private int userCnt;
    private int roleCnt;
    private int messageCnt;

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
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
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
     * @return the postCode
     */
    public String getPostCode() {
        return postCode;
    }

    /**
     * @param postCode the postCode to set
     */
    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    /**
     * @return the phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * @param phone the phone to set
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * @return the fax
     */
    public String getFax() {
        return fax;
    }

    /**
     * @param fax the fax to set
     */
    public void setFax(String fax) {
        this.fax = fax;
    }

    /**
     * @return the eMail
     */
    public String geteMail() {
        return eMail;
    }

    /**
     * @param eMail the eMail to set
     */
    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    /**
     * @return the registrationNo
     */
    public String getRegistrationNo() {
        return registrationNo;
    }

    /**
     * @param registrationNo the registrationNo to set
     */
    public void setRegistrationNo(String registrationNo) {
        this.registrationNo = registrationNo;
    }

    /**
     * @return the vatRegistrationNo
     */
    public String getVatRegistrationNo() {
        return vatRegistrationNo;
    }

    /**
     * @param vatRegistrationNo the vatRegistrationNo to set
     */
    public void setVatRegistrationNo(String vatRegistrationNo) {
        this.vatRegistrationNo = vatRegistrationNo;
    }

    /**
     * @return the databaseCreationDate
     */
    public Date getDatabaseCreationDate() {
        return databaseCreationDate;
    }

    /**
     * @param databaseCreationDate the databaseCreationDate to set
     */
    public void setDatabaseCreationDate(Date databaseCreationDate) {
        this.databaseCreationDate = databaseCreationDate;
    }

    /**
     * @return the internetDomain
     */
    public String getInternetDomain() {
        return internetDomain;
    }

    /**
     * @param internetDomain the internetDomain to set
     */
    public void setInternetDomain(String internetDomain) {
        this.internetDomain = internetDomain;
    }

    /**
     * @return the unitCnt
     */
    public int getUnitCnt() {
        return unitCnt;
    }

    /**
     * @param unitCnt the unitCnt to set
     */
    public void setUnitCnt(int unitCnt) {
        this.unitCnt = unitCnt;
    }

    /**
     * @return the userCnt
     */
    public int getUserCnt() {
        return userCnt;
    }

    /**
     * @param userCnt the userCnt to set
     */
    public void setUserCnt(int userCnt) {
        this.userCnt = userCnt;
    }

    /**
     * @return the roleCnt
     */
    public int getRoleCnt() {
        return roleCnt;
    }

    /**
     * @param roleCnt the roleCnt to set
     */
    public void setRoleCnt(int roleCnt) {
        this.roleCnt = roleCnt;
    }

    /**
     * @return the pictureContentType
     */
    public String getPictureContentType() {
        return pictureContentType;
    }

    /**
     * @param pictureContentType the pictureContentType to set
     */
    public void setPictureContentType(String pictureContentType) {
        this.pictureContentType = pictureContentType;
    }

    /**
     * @return the pictureFilename
     */
    public String getPictureFilename() {
        return pictureFilename;
    }

    /**
     * @param pictureFilename the pictureFilename to set
     */
    public void setPictureFilename(String pictureFilename) {
        this.pictureFilename = pictureFilename;
    }

    /**
     * @return the pictureData
     */
    public byte[] getPictureData() {
        return pictureData;
    }

    /**
     * @param pictureData the pictureData to set
     */
    public void setPictureData(byte[] pictureData) {
        this.pictureData = pictureData;
    }
 
    public String getPictureUrl(String realRootPath) {
        String result = null;
        if ((getPictureFilename() != null) && (!getPictureFilename().equals(""))) {
            TempFileProcessor tfp = new TempFileProcessor(getInternetDomain(), realRootPath);
            if (!tfp.isFileExists(getPictureFilename())) {
                try {
                    tfp.createFile(getPictureFilename(), getPictureData());
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(Company.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(Company.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            result = tfp.getUrlPath(getPictureFilename());
        }
        return result;
    }
    
    public void refreshPicture(String realRootPath) {
        if (getPictureFilename() != null) {
            TempFileProcessor tfp = new TempFileProcessor(getInternetDomain(), realRootPath);
            tfp.deleteFile(getPictureFilename());
        }
        getPictureUrl(realRootPath);
    }

    /**
     * @return the messageCnt
     */
    public int getMessageCnt() {
        return messageCnt;
    }

    /**
     * @param messageCnt the messageCnt to set
     */
    public void setMessageCnt(int messageCnt) {
        this.messageCnt = messageCnt;
    }

    /**
     * @return the boardCnt
     */
    public int getBoardCnt() {
        return boardCnt;
    }

    /**
     * @param boardCnt the boardCnt to set
     */
    public void setBoardCnt(int boardCnt) {
        this.boardCnt = boardCnt;
    }
}
