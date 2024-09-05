package ua.goit.timezone;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class TimezoneUtils {

    public static String getTimeZone(String timezone) {
        if (timezone != null && !timezone.isEmpty()) {
            int getUTC;
            if (timezone.startsWith("UTC ")) {
                getUTC = Integer.parseInt(timezone.substring(4));
                return ZonedDateTime.now(ZoneOffset.ofHours(getUTC)).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            } else if (timezone.startsWith("UTC-")) {
                getUTC = Integer.parseInt(timezone.substring(4));
                return ZonedDateTime.now(ZoneOffset.ofHours(-getUTC)).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            } else {
                return ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            }
        } else {
            return ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }
    }
}
