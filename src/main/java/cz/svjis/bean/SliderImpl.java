/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.svjis.bean;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author berk
 */
public class SliderImpl implements Slider {
    private String pageId;
    private int currentPage;
    private int numOfItemsAtPage;
    private long totalNumOfItems;
    private int totalNumOfPages;
    private int sliderWide;
    private List<SliderItem> itemList;
    
    public SliderImpl() {
        numOfItemsAtPage = 10;
        totalNumOfItems = 0;
        itemList = new ArrayList<>();
    }
    
    private void update() {
        if (numOfItemsAtPage == 0) {
            totalNumOfPages = 0;
            itemList = new ArrayList<>();
            return;
        }
        totalNumOfPages = (int)(totalNumOfItems / numOfItemsAtPage);
        if ((totalNumOfItems % numOfItemsAtPage) != 0) {
            totalNumOfPages += 1;
        }
        
        int sliderLastPage = ((currentPage + sliderWide / 2) < totalNumOfPages) ? (currentPage + sliderWide / 2) : totalNumOfPages;
        int sliderFirstPage = ((sliderLastPage - sliderWide + 1) > 1) ? (sliderLastPage - sliderWide + 1) : 1;
        sliderLastPage = ((sliderFirstPage + sliderWide - 1) < totalNumOfPages) ? (sliderFirstPage + sliderWide - 1) : totalNumOfPages;
        
        itemList.clear();
        
        SliderItem si;
        si = new SliderItem();
        si.setPage(1);
        si.setLabel("&lt;&lt;");
        si.setCurrent(false);
        itemList.add(si);
        
        si = new SliderItem();
        si.setPage((currentPage > 1) ? currentPage - 1 : 1);
        si.setLabel("&lt;");
        si.setCurrent(false);
        itemList.add(si);
            
        for (int i = 0; i < totalNumOfPages; i++) {
            int page = i + 1;
            if ((page < sliderFirstPage) || (page > sliderLastPage)) {
                continue;
            }
            si = new SliderItem();
            si.setPage(page);
            si.setLabel(String.valueOf(page));
            si.setCurrent((page) == currentPage);
            itemList.add(si);
        }
        
        si = new SliderItem();
        si.setPage((currentPage < totalNumOfPages) ? currentPage + 1 : totalNumOfPages);
        si.setLabel("&gt;");
        si.setCurrent(false);
        itemList.add(si);
        
        si = new SliderItem();
        si.setPage(totalNumOfPages);
        si.setLabel("&gt;&gt;");
        si.setCurrent(false);
        itemList.add(si);
    }
    
        /**
     * @return the currentPage
     */
    public int getCurrentPage() {
        return currentPage;
    }

    /**
     * @param currentPage the currentPage to set
     */
    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
        update();
    }

    /**
     * @return the numOfItemsAtPage
     */
    public int getNumOfItemsAtPage() {
        return numOfItemsAtPage;
    }

    /**
     * @param numOfItemsAtPage the numOfItemsAtPage to set
     */
    public void setNumOfItemsAtPage(int numOfItemsAtPage) {
        this.numOfItemsAtPage = numOfItemsAtPage;
        update();
    }

    /**
     * @return the totalNumOfItems
     */
    public long getTotalNumOfItems() {
        return totalNumOfItems;
    }

    /**
     * @param totalNumOfItems the totalNumOfItems to set
     */
    public void setTotalNumOfItems(long totalNumOfItems) {
        this.totalNumOfItems = totalNumOfItems;
        update();
    }

    /**
     * @return the totalNumOfPages
     */
    public int getTotalNumOfPages() {
        return totalNumOfPages;
    }

    /**
     * @return the itemList
     */
    public List<SliderItem> getItemList() {
        return itemList;
    }

    /**
     * @return the sliderWide
     */
    public int getSliderWide() {
        return sliderWide;
    }

    /**
     * @param sliderWide the sliderWide to set
     */
    public void setSliderWide(int sliderWide) {
        this.sliderWide = sliderWide;
    }

    /**
     * @return the pageId
     */
    public String getPageId() {
        return pageId;
    }

    /**
     * @param pageId the pageId to set
     */
    public void setPageId(String pageId) {
        this.pageId = pageId;
    }
}
