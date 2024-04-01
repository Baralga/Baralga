package org.remast.text;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Date;

import com.google.common.base.Strings;

/**
 * Smart time format based on the simple date format.
 * <h3>Examples</h3>
 * <ul>
 *  <li>12    -> 12:00</li>
 *  <li>12,5  -> 12:30</li>
 *  <li>12,50 -> 12:30</li>
 *  <li>12.30 -> 12:30</li>
 * </ul>
 * @author remast
 */
@SuppressWarnings("serial")
public class SmartTimeFormat extends TimeFormat {

    /**
     * {@inheritDoc}
     */
    public final Date parse(final String source, final ParsePosition pos) {
        String time = source;
        time = Strings.nullToEmpty(time).trim();

        if (time.length() == 0) {
            return super.parse(time, pos);
        }

        time = normalize(time);
        return super.parse(time, pos);
    }

    /**
     * Parses hours and minutes from the given time as String.
     * @param timeString the time to be parsed
     * @return the parsed time as array with hours and minutes
     * @throws ParseException on parse errors
     */
    public static int[] parseToHourAndMinutes(final String timeString) throws ParseException {
        String time = timeString;
        time = Strings.nullToEmpty(time).trim();

        if (time.length() == 0) {
            throw new ParseException("String is empty", 1);
        }

        time = normalize(time);

        String[] splitted = time.split(":");
        if (splitted.length != 2) {
            throw new ParseException("String '" + timeString + "' has an unsupported format", 1);
        } else {
            int[] result = new int[2];
            for (int i = 0; i < 2; i++) {
                result[i] = Integer.parseInt(splitted[i]);
            }
            return result;
        }
    }

    /**
     * Replaces ';' with ',', '.' with ':' and converts some fraction notations
     * into hh:mm (e.g., 12,5 into 12:30).
     * And some more.
     * @param timeString the String to normalize
     * @return the normalized String
     */
    private static String normalize(final String timeString) {
        final int MIN_LENGTH_FOR_HOUR_MIN_CONVERSION = 2;
        final int MIN_TIME_LENGTH_WITH_HOUR_ONLY = 5;

        // Corrected to reflect the refactoring intent more closely
        String time = Strings.nullToEmpty(timeString).trim();

        // Normalize the time string
        time = time.replace(",,", ":");
        time = time.replace("/", ":");
        time = time.replace(';', ',');
        time = time.replace('.', ':');

        // Handle fraction conversions, e.g., "12,5" -> "12:30"
        if (time.contains(",")) {
            String[] splittedTime = time.split(",");
            if (splittedTime.length >= 2) {
                String hh = splittedTime[0];
                String mm = splittedTime[1];
                if (mm.length() < MIN_LENGTH_FOR_HOUR_MIN_CONVERSION) {
                    mm += "0";
                }

                try {
                    int m = Integer.parseInt(mm);
                    float fm = m * 0.6f; // Convert from base100 to base60
                    m = Math.round(fm);
                    mm = String.format("%02d", m); // Ensures leading zero if needed
                    time = hh + ":" + mm;
                } catch (NumberFormatException e) {
                    // If conversion fails, do not apply the smart formatting.
                }
            }
        }

        // Default minutes for hour-only inputs, e.g., "11" -> "11:00"
        if (!time.contains(":")) {
            time += ":00";
        }

        // Prefix hours less than 10 with a zero, e.g., "9:30" -> "09:30"
        if (time.length() < MIN_TIME_LENGTH_WITH_HOUR_ONLY) {
            time = "0" + time;
        }
        return time;
    }


}
