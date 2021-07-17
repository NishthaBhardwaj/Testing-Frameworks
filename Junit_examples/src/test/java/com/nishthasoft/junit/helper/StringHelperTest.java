package com.nishthasoft.junit.helper;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StringHelperTest {
    StringHelper str = new StringHelper();
    @Test
    public void testTruncateAInFirst2Positions(){
        String actual = str.truncateAInFirst2Positions("AAWE");
        String expected = "WE";
        assertEquals(expected,actual);

    }
    @Test
    public void testTruncateAInFirst2Positions_2(){
        String actual = str.truncateAInFirst2Positions("ACD");
        String expected = "CD";
        assertEquals(expected,actual);

    }

}