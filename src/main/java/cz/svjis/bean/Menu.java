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

import cz.svjis.servlet.Cmd;

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
        this.buffer = new ArrayList<>(nodes);
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

    /**
     * 
     * @param l language
     * @return active MenuNode
     */
    public MenuNode getActiveSection(Language l) {
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
    
    
    /**
     * 
     * @param menu
     * @param activeSection
     * @param isTopLevel
     * @return menu as string
     */
    public String writeSubMenu(List<MenuItem> menu, int activeSection, boolean isTopLevel) {
        StringBuilder output = new StringBuilder();
        if (!isTopLevel) {
            output.append("<ul>");
        }

        for (MenuItem ami: menu) {
            String active = "";
            
            if (ami.getSection().isHide())
                continue;
            
            if ((isTopLevel) && ((activeSection == ami.getSection().getId()) || (ami.getSubSections() != null))) {
                active = " id=\"nav-active\"";
            }
            output.append(String.format("<li %s><a href=\"Dispatcher?page=%s&section=%d\">%s</a>", active, Cmd.ARTICLE_LIST, ami.getSection().getId(), ami.getSection().getDescription()));
            if (ami.getSubSections() != null) {
                output.append(writeSubMenu(ami.getSubSections(), activeSection, false));
            }
            output.append("</li>");
        }

        if (!isTopLevel) {
            output.append("</ul>\n");
        }
        return output.toString();
    }

    
    /**
     * 
     * @param menu
     * @param level
     * @param selected
     * @return options as string
     */
    public String writeOptions(List<MenuItem> menu, int level, int selected) {
        String ident = "&nbsp;&nbsp;&nbsp;";
        StringBuilder output = new StringBuilder(); 

        for (MenuItem m: menu) {
            StringBuilder idn = new StringBuilder(); 
            for (int n = 0; n < level; n++) {
                idn.append(ident);
            }

            String sel;
            if (m.getSection().getId() == selected) {
                sel = "SELECTED";
            } else {
                sel = "";
            }

            output.append(String.format("<option value=\"%d\" %s>%s</option>%n", m.getSection().getId(), sel, idn + m.getSection().getDescription()));

            if ((m.getSubSections() != null) && (!m.getSubSections().isEmpty()))
                output.append(writeOptions(m.getSubSections(), level + 1, selected));
        }
        return output.toString();
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
