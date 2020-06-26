/*
 *       Menu.java
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
public class Menu {

    private int activeSection;
    private int defaultSection;
    private ArrayList<MenuNode> buffer = null;
    private ArrayList<MenuItem> menuContent = null;
    private ArrayList<MenuNode> activeMenuContent = null;
    
    public Menu() {
    }
    
    public Menu(List<MenuNode> nodes) {
        this.buffer = new ArrayList(nodes);
        buildMenu();
    }

    /**
     * @return the activeSection
     */
    public int getActiveSection() {
        return activeSection;
    }

    /**
     * @param activeSection the activeSection to set
     */
    public void setActiveSection(int activeSection) {
        this.activeSection = activeSection;
        buildMenu();
    }
    
    /**
     * @return the activeMenuContent
     */
    public List<MenuNode> getActiveMenuContent() {
        return activeMenuContent;
    }

    /**
     * @return the menu
     */
    public List<MenuItem> getMenu() {
        return menuContent;
    }

    public MenuNode findActiveSection(Language l) {
        MenuNode result = findSectionInBuffer(activeSection);
        if (result == null) {
            result = new MenuNode();
            result.setId(0);
            result.setDescription(l.getText("All articles"));
        }
        
        return result;
    }

    private MenuNode findSectionInBuffer(int section) {
        if (section == 0) return null;

        for (MenuNode as: buffer) {
            if (as.getId() == section) {
                return as;
            }
        }

        return null;
    }

    private void buildMenu() {
        activeMenuContent = new ArrayList<>();
        int currSection = activeSection;
        MenuNode mn;
        while ((mn = findSectionInBuffer(currSection)) != null) {
            activeMenuContent.add(0, mn);
            currSection = mn.getParentId();
        }

        menuContent = buildSubMenu(0, 0);
    }

    private ArrayList<MenuItem> buildSubMenu(int navigationLevel, int parent) {
        ArrayList<MenuItem> subMenu = new ArrayList<>();
        
        for (MenuNode as: buffer) {
            if (as.getParentId() == parent) {
                MenuItem ami = new MenuItem();
                ami.setSection(as);
                if (((activeMenuContent.size() >= navigationLevel + 1) &&
                        (activeMenuContent.get(navigationLevel).getId() == as.getId())) || 
                        (activeSection == -1)) {
                    ami.setSubSections(buildSubMenu(navigationLevel + 1, as.getId()));
                } else {
                    ami.setSubSections(null);
                }
                subMenu.add(ami);
            }
        }

        return subMenu;
    }

    public  String writeMenu() {
        return writeSubMenu(menuContent);
    }

    private String writeSubMenu(List<MenuItem> menu) {
        StringBuilder sb = new StringBuilder(); 
        sb.append("<ul>" + "\n");
        for (MenuItem ami: menu) {
            sb.append("<li>");
            sb.append(ami.getSection().getDescription());
            if (ami.getSubSections() != null)
                sb.append(writeSubMenu(ami.getSubSections()));
            sb.append("</li>" + "\n");
        }
        sb.append("</ul>" + "\n");
        return sb.toString();
    }

    /**
     * @return the defaultSection
     */
    public int getDefaultSection() {
        return defaultSection;
    }

    /**
     * @param defaultSection the defaultSection to set
     */
    public void setDefaultSection(int defaultSection) {
        this.defaultSection = defaultSection;
    }
}
