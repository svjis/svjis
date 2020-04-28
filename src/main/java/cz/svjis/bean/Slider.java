/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.svjis.bean;

import java.util.List;

/**
 *
 * @author berk
 */
public interface Slider {
    
    public String getPageId();
    public void setPageId(String currentPage);
    
    public int getCurrentPage();
    public void setCurrentPage(int currentPage);
        
    public int getNumOfItemsAtPage();
    public void setNumOfItemsAtPage(int numOfItemsAtPage);
    
    public int getSliderWide();
    public void setSliderWide(int sliderWide);
    
    public long getTotalNumOfItems();
    public void setTotalNumOfItems(long totalNumOfItems);
    
    public int getTotalNumOfPages();
    
    public List<SliderItem> getItemList();
}
