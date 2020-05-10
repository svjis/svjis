/*
 *       MenuItem.java
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

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jaroslav_b
 */
public class MenuItem {

    private MenuNode section;
    private ArrayList<MenuItem> subSections;

    /**
     * @return the section
     */
    public MenuNode getSection() {
        return section;
    }

    /**
     * @param section the section to set
     */
    public void setSection(MenuNode section) {
        this.section = section;
    }

    /**
     * @return the subSections
     */
    public List<MenuItem> getSubSections() {
        return subSections;
    }

    /**
     * @param subSections the subSections to set
     */
    public void setSubSections(List<MenuItem> subSections) {
        if (subSections != null) {
            this.subSections = new ArrayList(subSections);
        } else {
            this.subSections = null;
        }
    }

}
