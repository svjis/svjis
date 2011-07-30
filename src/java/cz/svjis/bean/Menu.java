/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.svjis.bean;

import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author jaroslav_b
 */
public class Menu {

    private int activeSection;
    private ArrayList<MenuNode> navigationBar = null;
    private ArrayList<MenuNode> buffer = null;
    private ArrayList<MenuItem> menu = null;
    private ArrayList<MenuItem> allMenu = null;
    
    public Menu() {
    }
    
    public Menu(ArrayList<MenuNode> nodes) {
        this.buffer = nodes;
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
    public ArrayList<MenuNode> getNavigationBar() {
        return navigationBar;
    }

    /**
     * @return the menu
     */
    public ArrayList<MenuItem> getMenu() {
        return menu;
    }

    

    private MenuNode findSectionInBuffer(int section) {
        if (section == 0) return null;

        Iterator i = buffer.iterator();
        while (i.hasNext()) {
            MenuNode as = (MenuNode) i.next();
            if (as.getId() == section) {
                return as;
            }
        }

        return null;
    }

    private void buildMenu() {
        navigationBar = new ArrayList<MenuNode>();
        MenuNode as;
        int currSection = activeSection;
        while ((as = findSectionInBuffer(currSection)) != null) {
            navigationBar.add(0, as);
            currSection = as.getParentId();
        }

        menu = new ArrayList<MenuItem>();
        Iterator i = buffer.iterator();
        int navigationLevel = 0;
        while (i.hasNext()) {
            as = (MenuNode) i.next();
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
                menu.add(ami);
            }
        }
    }

    private ArrayList<MenuItem> buildSubMenu(int navigationLevel, int parent) {
        ArrayList<MenuItem> subMenu = new ArrayList<MenuItem>();
        Iterator i = buffer.iterator();
        while (i.hasNext()) {
            MenuNode as = (MenuNode) i.next();
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
        return writeSubMenu(menu);
    }

    private String writeSubMenu(ArrayList<MenuItem> menu) {
        String output = "";
        output = output + "<ul>" + "\n";
        Iterator i = menu.iterator();
        while (i.hasNext()) {
            MenuItem ami = (MenuItem) i.next();
            output = output + "<li>" + ami.getSection().getDescription();
            if (ami.getSubSections() != null)
                output = output + writeSubMenu(ami.getSubSections());
            output = output + "</li>" + "\n";
        }
        output = output + "</ul>" + "\n";
        return output;
    }
}
