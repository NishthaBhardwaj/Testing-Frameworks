package com.nishthasoft.junit.helper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

public class ArrayCompareTest {

    @Test
    public void testArraySort_RandomArray(){
        int[] numbers = {12,3,2,9};
        int[] expected = {2,3,9,12};
        Arrays.sort(numbers);
        assertArrayEquals(expected,numbers);

    }


    @Test()
    public void testArraySort_NullArray(){
        int[] numbers = null;
        int[] expected = {2,3,9,12};
        Assertions.assertThrows(NullPointerException.class,() ->{
            Arrays.sort(numbers);
        });


    }
}
