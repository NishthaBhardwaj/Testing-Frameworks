package com.nishthasoft.junit.helper;

public class StringHelper {

    public String truncateAInFirst2Positions(String str) {
        if(str.length()<=2){
            return str.replace("A"," ");
        }
        String first2Char = str.substring(0,2);
        String stringMinusFirst2Chras = str.substring(2);

        return first2Char.replace("A","") + stringMinusFirst2Chras;

    }
}
