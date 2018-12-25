/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.svjis.bean;

import java.util.Date;

/**
 *
 * @author jaroslav_b
 */
public class InquiryLog {
    private Date time;
    private String user;
    private String optionDescription;

    /**
     * @return the time
     */
    public Date getTime() {
        return time;
    }

    /**
     * @param time the time to set
     */
    public void setTime(Date time) {
        this.time = time;
    }

    /**
     * @return the user
     */
    public String getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * @return the optionDescription
     */
    public String getOptionDescription() {
        return optionDescription;
    }

    /**
     * @param optionDescription the optionDescription to set
     */
    public void setOptionDescription(String optionDescription) {
        this.optionDescription = optionDescription;
    }
    
}
