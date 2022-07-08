package org.mentalizr.backend.media;

import org.junit.jupiter.api.Test;
import org.mentalizr.backend.media.range.*;

import static org.junit.jupiter.api.Assertions.*;

class RangeParserTest {

    @Test
    public void singleRangeWithBeginAndEnd() throws RangeParserException {
        String rangeHeaderValue = "bytes=1-2";
        Ranges ranges = RangeParser.parseHeader(rangeHeaderValue, 10);

        assertTrue(ranges.hasSingleRange());

        Range range = ranges.getRanges().get(0);

        assertEquals(1, range.getBegin());
        assertEquals(2, range.getEnd());
    }

    @Test
    public void singleRangeWithBeginAndEndOneByte() throws RangeParserException {
        String rangeHeaderValue = "bytes=2-2";
        Ranges ranges = RangeParser.parseHeader(rangeHeaderValue, 10);

        assertTrue(ranges.hasSingleRange());

        Range range = ranges.getRanges().get(0);

        assertEquals(2, range.getBegin());
        assertEquals(2, range.getEnd());
    }

    @Test
    public void capitalKeywordBytes() throws RangeParserException {
        String rangeHeaderValue = "BYTES=1-2";
        Ranges ranges = RangeParser.parseHeader(rangeHeaderValue, 10);

        assertTrue(ranges.hasSingleRange());

        Range range = ranges.getRanges().get(0);

        assertEquals(1, range.getBegin());
        assertEquals(2, range.getEnd());
    }

    @Test
    public void additionalSpacingInPrefix() throws RangeParserException {
        String rangeHeaderValue = "bytes = 1-2";
        Ranges ranges = RangeParser.parseHeader(rangeHeaderValue, 10);

        assertTrue(ranges.hasSingleRange());

        Range range = ranges.getRanges().get(0);

        assertEquals(1, range.getBegin());
        assertEquals(2, range.getEnd());
    }

    @Test
    public void additionalSpacingAlsoInValues() throws RangeParserException {
        String rangeHeaderValue = "bytes = 1 - 2";
        Ranges ranges = RangeParser.parseHeader(rangeHeaderValue, 10);

        assertTrue(ranges.hasSingleRange());

        Range range = ranges.getRanges().get(0);

        assertEquals(1, range.getBegin());
        assertEquals(2, range.getEnd());
    }

    @Test
    public void singleRangeWithBeginOny() throws RangeParserException {
        String rangeHeaderValue = "bytes=5-";
        Ranges ranges = RangeParser.parseHeader(rangeHeaderValue, 10);

        assertTrue(ranges.hasSingleRange());

        Range range = ranges.getRanges().get(0);

        assertEquals(5, range.getBegin());
        assertEquals(9, range.getEnd());
    }

    @Test
    public void singleRangeGetLast() throws RangeParserException {
        String rangeHeaderValue = "bytes=-6";
        Ranges ranges = RangeParser.parseHeader(rangeHeaderValue, 10);

        assertTrue(ranges.hasSingleRange());

        Range range = ranges.getRanges().get(0);

        assertEquals(4, range.getBegin());
        assertEquals(9, range.getEnd());
    }

    @Test
    public void singleRangeWithBeginAndEndIllegalRangeDefinition_neg() throws RangeParserException {
        String rangeHeaderValue = "bytes=3-2";

        RangeParserException rangeParserException =
                assertThrows(RangeParserException.class, () -> RangeParser.parseHeader(rangeHeaderValue, 10));

        assertTrue(rangeParserException instanceof RangeHeaderOutOfBoundsException);
    }

    @Test
    public void singleRangeWithBeginAndEndIllegalRangeDefinition2_neg() throws RangeParserException {
        String rangeHeaderValue = "bytes=3-10";

        RangeParserException rangeParserException =
                assertThrows(RangeParserException.class, () -> RangeParser.parseHeader(rangeHeaderValue, 10));

        assertTrue(rangeParserException instanceof RangeHeaderOutOfBoundsException);
    }

    @Test
    public void twoRangeWithBeginAndEnd() throws RangeParserException {
        String rangeHeaderValue = "bytes=1-2,3-4";
        Ranges ranges = RangeParser.parseHeader(rangeHeaderValue, 10);

        assertFalse(ranges.hasSingleRange());
        assertEquals(2, ranges.getNrOfRanges());

        Range range = ranges.getRanges().get(0);
        assertEquals(1, range.getBegin());
        assertEquals(2, range.getEnd());

        range = ranges.getRanges().get(1);
        assertEquals(3, range.getBegin());
        assertEquals(4, range.getEnd());
    }

    @Test
    public void emptyRangesByNull() throws RangeParserException {
        Ranges ranges = RangeParser.parseHeader(null, 10);
        assertFalse(ranges.hasRange());
        assertFalse(ranges.hasSingleRange());
    }

    @Test
    public void emptyRangesByEmptyHeaderValue() throws RangeParserException {
        Ranges ranges = RangeParser.parseHeader("", 10);
        assertFalse(ranges.hasRange());
    }

}