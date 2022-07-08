package org.mentalizr.backend.utils;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StringUtilsTest {

    @Test
    void splitOneElementWithoutDelimiter() {
        String string = "a";
        List<String> chunks = StringUtils.split(string, ",");

        assertEquals(1, chunks.size());
        assertEquals("a", chunks.get(0));
    }

    @Test
    void splitTwoElements() {
        String string = "a,b";
        List<String> chunks = StringUtils.split(string, ",");

        assertEquals(2, chunks.size());
        assertEquals("a", chunks.get(0));
        assertEquals("b", chunks.get(1));
    }

    @Test
    void splitThreeElements() {
        String string = "a,b,xx";
        List<String> chunks = StringUtils.split(string, ",");

        assertEquals(3, chunks.size());
        assertEquals("a", chunks.get(0));
        assertEquals("b", chunks.get(1));
        assertEquals("xx", chunks.get(2));
    }

    @Test
    void splitTwoElementsDelimiterAsLastCharacter() {
        String string = "a,b,";
        List<String> chunks = StringUtils.split(string, ",");

        assertEquals(2, chunks.size());
        assertEquals("a", chunks.get(0));
        assertEquals("b", chunks.get(1));
    }

    @Test
    void splitTwoElementsDelimiterAsFirstCharacter() {
        String string = ",a,b";
        List<String> chunks = StringUtils.split(string, ",");

        assertEquals(3, chunks.size());
        assertEquals("", chunks.get(0));
        assertEquals("a", chunks.get(1));
        assertEquals("b", chunks.get(2));
    }

    @Test
    void splitDelimiterOnly() {
        String string = ",";
        List<String> chunks = StringUtils.split(string, ",");

        assertEquals(1, chunks.size());
        assertEquals("", chunks.get(0));
    }



}