/*
 *       SliderImplTest.java
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 *
 * @author berk
 */
class SliderImplTest {
    
    @Test
    @DisplayName("Slider")
    void testSlider() {
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
