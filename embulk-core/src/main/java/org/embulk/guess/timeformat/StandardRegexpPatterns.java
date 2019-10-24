package org.embulk.guess.timeformat;

import static org.embulk.guess.timeformat.RegexParts.MONTH_NAME_SHORT;
import static org.embulk.guess.timeformat.RegexParts.WEEKDAY_NAME_SHORT;
import static org.embulk.guess.timeformat.RegexParts.ZONE_OFF;

final class StandardRegexpPatterns {
    private StandardRegexpPatterns() {
        // No instantiation.
    }

    static final Pattern APACHE_CLF = new RegexpPattern(
            java.util.regex.Pattern.compile(
                    String.format("^\\d\\d\\/%s\\/\\d\\d\\d\\d:\\d\\d:\\d\\d:\\d\\d %s?$", MONTH_NAME_SHORT, ZONE_OFF)),
            "%d/%b/%Y:%H:%M:%S %z");

    static final Pattern ANSI_C_ASCTIME = new RegexpPattern(
            java.util.regex.Pattern.compile(
                    String.format("^%s %s \\d\\d? \\d\\d:\\d\\d:\\d\\d \\d\\d\\d\\d$", WEEKDAY_NAME_SHORT, MONTH_NAME_SHORT)),
            "%a %b %e %H:%M:%S %Y");
}
