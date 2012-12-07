/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.svjis.bean;

import org.junit.Test;

/**
 *
 * @author berk
 */
public class SliderImplTest {
    
    @Test
    public void testSlider() {
        SliderImpl sl = new SliderImpl();
        sl.setSliderWide(10);
        sl.setCurrentPage(1);
        sl.setNumOfItemsAtPage(10);
        sl.setTotalNumOfItems(20);
        org.junit.Assert.assertEquals("Slider page 20/1", sl.getItemList().size(), 6);
        sl.setCurrentPage(2);
        org.junit.Assert.assertEquals("Slider page 20/2", sl.getItemList().size(), 6);
        sl.setTotalNumOfItems(21);
        org.junit.Assert.assertEquals("Slider page 21/2", sl.getItemList().size(), 7);
    }
}
