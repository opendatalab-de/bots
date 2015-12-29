package de.grundid.api.utils;

import org.springframework.util.StringUtils;

public class GrabUtils {

    private static final char NO_BRACK_SPACE = 160;

    public static String trimAll(String s) {
        if (s == null) {
            return s;
        }
        while (s.contains("" + NO_BRACK_SPACE)) {
            s = s.replace(NO_BRACK_SPACE, ' ');
        }
        return StringUtils.trimWhitespace(s);
    }
}
