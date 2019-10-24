package org.embulk.guess.timeformat;

class RegexParts {
    private RegexParts() {
        // No instantiation.
    }

    static final String DATE_DELIMS = "[\\/\\-\\.]";
    static final String DATE_TIME_DELIMS = "(:? |_|T|\\. ?)";

    static final String YEAR = "[1-4][0-9]{3}";
    static final String MONTH = "10|11|12|[0 ]?[0-9]";
    static final String MONTH_NODELIM = "10|11|12|[0][0-9]";
    static final String DAY = "31|30|[1-2][0-9]|[0 ]?[1-9]";
    static final String DAY_NODELIM = "31|30|[1-2][0-9]|[0][1-9]";
    static final String HOUR = "20|21|22|23|24|1[0-9]|[0 ]?[0-9]";
    static final String HOUR_NODELIM = "20|21|22|23|24|1[0-9]|[0][0-9]";
    static final String MINUTE = "60|[1-5][0-9]|[0 ]?[0-9]";
    static final String SECOND = MINUTE;
    static final String MINUTE_NODELIM = "60|[1-5][0-9]|[0][0-9]";
    static final String SECOND_NODELIM = MINUTE_NODELIM;

    static final String MONTH_NAME_SHORT = "Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec";
    static final String MONTH_NAME_FULL =
            "January|February|March|April|May|June|July|August|September|October|November|December";

    static final String WEEKDAY_NAME_SHORT = "Sun|Mon|Tue|Wed|Thu|Fri|Sat";
    static final String WEEKDAY_NAME_FULL = "Sunday|Monday|Tuesday|Wednesday|Thursday|Friday|Saturday";

    static final String ZONE_OFF = "(?:Z|[\\-\\+]\\d\\d(?::?\\d\\d)?)";
    static final String ZONE_ABB = "[A-Z]{1,3}";

    static final String FRAC = "[0-9]{1,9}";
    static final String TIME_DELIMS = "[\\:\\-]";
    static final String FRAC_DELIMS = "[\\.\\,]";

    static final String TIME = String.format(
            "(?<hour>%s)(?:(?<time_delim>%s)(?<minute>%s)" +
            "(?:\\k<time_delim>(?<second>%s)(?:(?<frac_delim>%s)(?<frac>%s))?)?)?",
            HOUR, TIME_DELIMS, MINUTE, SECOND, FRAC_DELIMS, FRAC);

    static final String TIME_NODELIM = String.format(
            "(?<hour>%s)(?:(?<minute>%s)((?<second>%s)(?:(?<frac_delim>%s)(?<frac>%s))?)?)?",
            HOUR_NODELIM, MINUTE_NODELIM, SECOND_NODELIM, FRAC_DELIMS, FRAC);

    static final String ZONE = String.format(
            "(?<zone_space> )?(?<zone>(?<zone_off>%s)|(?<zone_abb>%s))", ZONE_OFF, ZONE_ABB);
}
