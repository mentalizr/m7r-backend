package org.mentalizr.backend.utils;

import de.arthurpicht.utils.core.assertion.AssertMethodPrecondition;
import de.arthurpicht.utils.core.strings.Strings;

import java.util.ArrayList;
import java.util.List;

public class StringUtils {

    /**
     * Splits specified string in a list of chunks applying the specified delimiter. The delimiter will not
     * be part of any chunk. Repeating delimiters will lead to empty chunks. A string starting with a delimiter will
     * lead to an empty first chunk. A trailing delimiter will be ignored.
     *
     * @param string string to be split
     * @param delimiter separation between to chunks
     * @return list of splitted chunks
     */
    public static List<String> split(String string, String delimiter) {
        AssertMethodPrecondition.parameterNotNullAndNotEmpty("string", string);
        AssertMethodPrecondition.parameterNotNullAndNotEmpty("delimiter", delimiter);

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

}
