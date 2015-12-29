package de.grundid.api.utils;

import com.google.common.base.CaseFormat;

import java.util.HashMap;
import java.util.Map;

public class Utils {

    private static final Map<String, String> WORD_CACHE = new HashMap<>();

    public static String camelCase(String str) {
        String result = WORD_CACHE.get(str);
        if (result == null) {
            result = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, str);
            WORD_CACHE.put(str, result);
        }
        return result;
    }

    public static double distFrom(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 6371000;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return earthRadius * c;
    }
}
