package org.mentalizr.backend.utils;

import de.arthurpicht.utils.core.strings.Strings;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static de.arthurpicht.utils.core.assertion.MethodPreconditions.assertArgumentNotNullAndNotEmpty;

public class StringUtils {

    /**
     * Splits specified string in a list of chunks applying the specified delimiter. The delimiter will not
     * be part of any chunk. Repeating delimiters will lead to empty chunks. A string starting with a delimiter will
     * lead to an empty first chunk. A trailing delimiter will be ignored.
     *
     * @param string string to be split
     * @param delimiter separation between to chunks
     * @return list of split chunks
     */
    public static List<String> split(String string, String delimiter) {
        assertArgumentNotNullAndNotEmpty("string", string);
        assertArgumentNotNullAndNotEmpty("delimiter", delimiter);

        List<String> chunks = new ArrayList<>();

        if (!string.contains(delimiter)) {
            chunks.add(string);
            return chunks;
        }

        while (string.contains(delimiter)) {
            String[] splitStrings = Strings.splitAtDelimiter(string, delimiter);
            chunks.add(splitStrings[0]);
            string = splitStrings[1];
        }
        if (Strings.isSpecified(string)) chunks.add(string);

        return chunks;
    }

    public static InputStream asInputStream(String string, Charset charset) {
        return new ByteArrayInputStream(string.getBytes(charset));
    }

}
