/*
 *       InquiryLog.java
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
