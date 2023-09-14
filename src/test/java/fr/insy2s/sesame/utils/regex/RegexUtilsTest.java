package fr.insy2s.sesame.utils.regex;


import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RegexUtilsTest {

    @Test
    void testRegexAlphabeticName() {
        Pattern pattern = Pattern.compile(RegexUtils.REGEX_ALPHABETIC_NAME);
        assertTrue(pattern.matcher("Jean").matches());
        assertTrue(pattern.matcher("Jean-Pierre").matches());
        assertTrue(pattern.matcher("Jeén-Pi ero ö îaéàçè're").matches());
        assertFalse(pattern.matcher("Jean-Pierre1").matches());
        assertFalse(pattern.matcher("Jean-Pierre!").matches());
        assertFalse(pattern.matcher("Jean-Pierre?").matches());
        assertFalse(pattern.matcher("Jean-Pierre.").matches());
        assertFalse(pattern.matcher("Jean-Pierre,").matches());
        assertFalse(pattern.matcher("Jean-Pierre;").matches());
        assertFalse(pattern.matcher("Jean-Pierre:").matches());
        assertFalse(pattern.matcher("Jean-Pierre/").matches());
        assertFalse(pattern.matcher("Jean-Pierre*").matches());
        assertFalse(pattern.matcher("Jean-Pierre+").matches());
        assertFalse(pattern.matcher("Jean-Pierre=").matches());
        assertFalse(pattern.matcher("Jean-Pierre(").matches());
        assertFalse(pattern.matcher("Jean-Pierre[").matches());
        assertFalse(pattern.matcher("Jean-Pierre{").matches());
        assertFalse(pattern.matcher("Jean-Pierre<").matches());
        assertFalse(pattern.matcher("Jean-Pierre>").matches());
        assertFalse(pattern.matcher("Jean-Pierre%").matches());
        assertFalse(pattern.matcher("Jean-Pierre$").matches());
        assertFalse(pattern.matcher("Jean-Pierre£").matches());
        assertFalse(pattern.matcher("Jean-Pierre€").matches());

    }

    @Test
    void testRegexPhone() {
        Pattern pattern = Pattern.compile(RegexUtils.REGEX_PHONE);
        assertTrue(pattern.matcher("01 23 45 67 89").matches());
        assertTrue(pattern.matcher("+33 6 23 45 67 89").matches());
        assertFalse(pattern.matcher("01 23 45 67 8").matches());
        assertFalse(pattern.matcher("01 23 45 67 89 00").matches());
        assertFalse(pattern.matcher("0123456789").matches());
        assertFalse(pattern.matcher("01 23 45 67 89 0a").matches());
    }

    @Test
    void testRegexSiren() {
        Pattern pattern = Pattern.compile(RegexUtils.REGEX_SIREN);
        assertTrue(pattern.matcher("123456789").matches());
        assertFalse(pattern.matcher("12345678").matches());
        assertFalse(pattern.matcher("1234567890").matches());
        assertFalse(pattern.matcher("12345678a").matches());
    }

    @Test
    void testRegexZipCode() {
        Pattern pattern = Pattern.compile(RegexUtils.REGEX_ZIP_CODE);
        assertTrue(pattern.matcher("12345").matches());
        assertFalse(pattern.matcher("1234").matches());
        assertFalse(pattern.matcher("123456").matches());
        assertFalse(pattern.matcher("1234a").matches());
    }

}