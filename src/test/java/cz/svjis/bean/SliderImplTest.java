/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.svjis.bean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 *
 * @author berk
 */
public class SliderImplTest {
    
    @Test
    @DisplayName("Slider")
    public void testSlider() {
        SliderImpl sl = new SliderImpl();
        sl.setSliderWide(10);
        sl.setCurrentPage(1);
        sl.setNumOfItemsAtPage(10);
        sl.setTotalNumOfItems(20);
        assertEquals(6, sl.getItemList().size(), "Slider page 20/1");
        sl.setCurrentPage(2);
        assertEquals(6, sl.getItemList().size(), "Slider page 20/2");
        sl.setTotalNumOfItems(21);
        assertEquals(7, sl.getItemList().size(), "Slider page 21/2");
    }
}
