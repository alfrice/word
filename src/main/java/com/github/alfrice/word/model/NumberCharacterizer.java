package com.github.alfrice.word.model;

import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by alice.martin
 * Developer: alice.martin
 * Date: 12/3/18
 * Time: 5:50 AM
 * Description: characterizes a string number entry, removing commas, validating that is is parseable, Identifying negatives and floating points and breaking down the abs number into sets of three (from the rear)
 */
@Getter
@Slf4j
@ToString
public class NumberCharacterizer implements Serializable {

    public static final String s = "I cant count that high. Try something smaller.";
    public static final String NUM_FORMAT_ERR_MSG = "Not a parseable number.";

    String[] chunks = null;
    String digits = "";
    boolean negative = false;
    boolean floatingPoint = false;
    private String errorMessage;
    private boolean parseable = true;
    private boolean outOfBounds = false;

    public NumberCharacterizer(String stringNumber) {
        characterize(stringNumber);
    }

    /**
     * characterizes a number as to type (Negative, Floating Point, etc) and maps values into chunks for processing
     *
     * @param strNum a string that may containt several characters
     */
    private void characterize(String strNum) {

        negative = strNum.startsWith("-") ? true : negative;
        String cleanedNumber = clean(strNum);
        if (parseable) {
            floatingPoint = cleanedNumber.contains(".") ? true : false;
            if (floatingPoint) {
                String[] parts = cleanedNumber.split("\\.");
                if (parts.length == 2) {
                    chunks = chunk(parts[0]);
                    digits = parts[1];
                }
                assert (parts.length == 2);
            } else {
                chunks = chunk(cleanedNumber);
            }
        }

    }

    /**
     * Breaks down a string number (non floating, non negative) into a sets of three characters.
     * Ensures mod of 3 is at the front
     *
     * @param intString A string that is an integer. It is expected to be only in a parseable number format
     * @return An array of strings of length no longer than three. Mods are at the front.
     */
    private String[] chunk(String intString) {

        int len = intString.length();
        int firstChunkSize = len % 3;
        int chunkSize = 3;
        int sets = (int) Math.floor(len / 3);
        sets = firstChunkSize > 0 ? ++sets : sets;
        String[] chunks = new String[sets];
        int count = -1;
        if (firstChunkSize > 0) {
            chunks[++count] = intString.substring(0, firstChunkSize);
        }

        for (int i = firstChunkSize; i < len; i += chunkSize) {
            chunks[++count] = intString.substring(i, i + 3);
        }
        if (chunks.length>30){
            parseable=false;
            errorMessage= "I cant count that high. Try something smaller.";
            outOfBounds=true;
            chunks=null;
        }
        return chunks;
    }

    /**
     * Takes a string and validates for number format, removes commas and other non usable data
     *
     * @param stringNumber
     * @return the number if parseable or {@link NumberCharacterizer#errorMessage}
     */
    String clean(String stringNumber) {
        stringNumber = stringNumber.replaceAll(",", "");

        if (StringUtils.isEmpty(stringNumber) || NumberUtils.isCreatable(stringNumber)) {
            if (stringNumber.toLowerCase().contains("e")) {
                return new BigDecimal(stringNumber).toPlainString().replaceAll("-", "");
            }
            return stringNumber.replaceAll("-", "");
        } else {

            errorMessage = String.format( "%s:  '%s'. \nThe problem may be these character(s): '%s'", NUM_FORMAT_ERR_MSG, stringNumber, stringNumber.replaceAll("[0-9]", ""));
            log.error(errorMessage);
            parseable = false;
            return errorMessage;
        }


    }


}
