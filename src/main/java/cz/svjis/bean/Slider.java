/*
 *       Slider.java
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
