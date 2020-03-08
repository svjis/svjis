/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
    private ArrayList<MenuNode> navigationBar = null;
    private ArrayList<MenuNode> buffer = null;
    private ArrayList<MenuItem> menuContent = null;    
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
     * @return the navigationBar
     */
    public List<MenuNode> getNavigationBar() {
        return navigationBar;
    }

    /**
     * @return the menu
     */
    public List<MenuItem> getMenu() {
        return menuContent;
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
        navigationBar = new ArrayList<>();
        int currSection = activeSection;
        MenuNode mn;
        while ((mn = findSectionInBuffer(currSection)) != null) {
            navigationBar.add(0, mn);
            currSection = mn.getParentId();
        }

        menuContent = new ArrayList<>();
        int navigationLevel = 0;
        for (MenuNode as: buffer) {
            if (as.getParentId() == 0) {
                MenuItem ami = new MenuItem();
                ami.setSection(as);
                if (((navigationBar.size() >= navigationLevel + 1) &&
                        (navigationBar.get(navigationLevel).getId() == as.getId())) || 
                        (activeSection == -1)) {
                    ami.setSubSections(buildSubMenu(navigationLevel + 1, as.getId()));
                } else {
                    ami.setSubSections(null);
                }
                menuContent.add(ami);
            }
        }
    }

    private ArrayList<MenuItem> buildSubMenu(int navigationLevel, int parent) {
        ArrayList<MenuItem> subMenu = new ArrayList<>();
        
        for (MenuNode as: buffer) {
            if ((as.getParentId() != 0) && (as.getParentId() == parent)) {
                MenuItem ami = new MenuItem();
                ami.setSection(as);
                if (((navigationBar.size() >= navigationLevel + 1) &&
                        (navigationBar.get(navigationLevel).getId() == as.getId())) || 
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
        String output = "";
        output = output + "<ul>" + "\n";

        for (MenuItem ami: menu) {
            output = output + "<li>" + ami.getSection().getDescription();
            if (ami.getSubSections() != null)
                output = output + writeSubMenu(ami.getSubSections());
            output = output + "</li>" + "\n";
        }
        output = output + "</ul>" + "\n";
        return output;
    }
}
