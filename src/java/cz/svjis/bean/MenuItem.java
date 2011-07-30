/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.svjis.bean;

import java.util.ArrayList;

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
    public ArrayList<MenuItem> getSubSections() {
        return subSections;
    }

    /**
     * @param subSections the subSections to set
     */
    public void setSubSections(ArrayList<MenuItem> subSections) {
        this.subSections = subSections;
    }

}
