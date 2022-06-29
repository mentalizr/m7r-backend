package org.mentalizr.backend.media.range;

import de.arthurpicht.utils.core.strings.Strings;
import org.mentalizr.backend.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class RangeParser {

    private static final Logger logger = LoggerFactory.getLogger(RangeParser.class);

    public static Ranges parse(HttpServletRequest request, long totalLength) throws RangeParserException {
        String rangeHeaderValue = request.getHeader("Range");
        return parseHeader(rangeHeaderValue, totalLength);
    }

    public static Ranges parseHeader(String rangeHeaderValue, long totalLength) throws RangeParserException {
        if (Strings.isNullOrEmpty(rangeHeaderValue)) return Ranges.getEmptyInstance();

        rangeHeaderValue = shortenRangeHeaderValue(rangeHeaderValue);

        RangesBuilder rangesBuilder = new RangesBuilder();
        if (rangeHeaderValue.contains(",")) {
            List<String> rangeHeaderValueList = StringUtils.split(rangeHeaderValue, ",");
            for (String rangeHeaderValueItem : rangeHeaderValueList) {
                Range range = parseRange(rangeHeaderValueItem, totalLength);
                rangesBuilder.addRange(range);
            }
        } else {
            Range range = parseRange(rangeHeaderValue, totalLength);
            rangesBuilder.addRange(range);
        }

        return rangesBuilder.build();
    }

    private static String shortenRangeHeaderValue(String rangeHeaderValue) throws RangeParserException {
        rangeHeaderValue = rangeHeaderValue.trim();
        if (!rangeHeaderValue.toLowerCase().startsWith("bytes"))
            throw new RangeParserException("Keyword 'bytes' missing in Range header value.");

        rangeHeaderValue = rangeHeaderValue.substring(5).trim();
        if (!rangeHeaderValue.startsWith("="))
            throw new RangeParserException("'=' sign missing after keyword 'bytes'.");

        return rangeHeaderValue.substring(1).trim();
    }

    private static Range parseRange(String rangeValue, long totalLength) throws RangeParserException {
        rangeValue = rangeValue.trim();

        if (!rangeValue.contains("-"))
            throw new RangeHeaderParserException(rangeValue, "'-' expected.");

        String[] boundaries = Strings.splitAtDelimiter(rangeValue, "-");

        if (Strings.isNullOrEmpty(boundaries[0]) && Strings.isNullOrEmpty(boundaries[1]))
            throw new RangeHeaderParserException(rangeValue);

        if (Strings.isSpecified(boundaries[0]) && Strings.isSpecified(boundaries[1])) {
            long begin = Long.parseLong(boundaries[0].trim());
            long end = Long.parseLong(boundaries[1].trim());
            if (begin > end || end >= totalLength) throw new RangeHeaderOutOfBoundsException(rangeValue);
            return new Range(begin, end);
        }

        if (Strings.isSpecified(boundaries[0]) && Strings.isNullOrEmpty(boundaries[1])) {
            long begin = Long.parseLong(boundaries[0].trim());
            if (begin >= totalLength) throw new RangeHeaderOutOfBoundsException(rangeValue);
            return new Range(begin, totalLength - 1);
        }

        if (Strings.isNullOrEmpty(boundaries[0]) && Strings.isSpecified(boundaries[1])) {
            long last = Long.parseLong(boundaries[1].trim());
            if (last > totalLength) throw new RangeHeaderOutOfBoundsException(rangeValue);
            long begin = totalLength - last;
            return new Range(begin, totalLength - 1);
        }

        throw new IllegalStateException();
    }

}
