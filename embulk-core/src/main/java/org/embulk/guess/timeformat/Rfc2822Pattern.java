package org.embulk.guess.timeformat;

import java.util.regex.Matcher;

import static org.embulk.guess.timeformat.RegexParts.MONTH_NAME_SHORT;
import static org.embulk.guess.timeformat.RegexParts.WEEKDAY_NAME_SHORT;
import static org.embulk.guess.timeformat.RegexParts.ZONE_OFF;
import static org.embulk.guess.timeformat.RegexParts.ZONE_ABB;

final class Rfc2822Pattern extends Pattern {
    Rfc2822Pattern() {}

    @Override
    Match match(final String text) {
        final Matcher matcher = REGEX.matcher(text);

        if (matcher.matches()) {
            final StringBuilder format = new StringBuilder();
            if (!matcher.group("weekday").isEmpty()) {
                format.append("%a, ");
            }
            format.append("%d %b %Y");
            if (!matcher.group("time").isEmpty()) {
                format.append(" %H:%M");
            }
            if (!matcher.group("second").isEmpty()) {
                format.append(":%S");
            }
            if (!matcher.group("zone_off").isEmpty()) {
                if (!matcher.group("zone_off").contains(":")) {
                    format.append(" %:z");
                } else {
                    format.append(" %z");
                }
            } else if (!matcher.group("zone_abb").isEmpty()) {
                // don't use %Z: https://github.com/jruby/jruby/issues/3702
                if (!matcher.group("zone_abb").isEmpty()) {
                    format.append(" %z");
                }
            }
            return new SimpleMatch(format.toString());
        } else {
            return null;
        }
    }

    private static final java.util.regex.Pattern REGEX = java.util.regex.Pattern.compile(String.format(
            "^(?<weekday>%s, )?\\d\\d %s \\d\\d\\d\\d(?<time> \\d\\d:\\d\\d(?<second>:\\d\\d)? "
                    + "(?:(?<zone_off>%s)|(?<zone_abb>%s)))?$",
            WEEKDAY_NAME_SHORT, MONTH_NAME_SHORT, ZONE_OFF, ZONE_ABB));
}
