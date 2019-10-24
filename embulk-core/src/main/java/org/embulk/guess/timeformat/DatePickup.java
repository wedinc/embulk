package org.embulk.guess.timeformat;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.embulk.guess.timeformat.RegexParts.DATE_DELIMS;
import static org.embulk.guess.timeformat.RegexParts.DATE_TIME_DELIMS;
import static org.embulk.guess.timeformat.RegexParts.DAY;
import static org.embulk.guess.timeformat.RegexParts.DAY_NODELIM;
import static org.embulk.guess.timeformat.RegexParts.HOUR;
import static org.embulk.guess.timeformat.RegexParts.HOUR_NODELIM;
import static org.embulk.guess.timeformat.RegexParts.MINUTE;
import static org.embulk.guess.timeformat.RegexParts.MINUTE_NODELIM;
import static org.embulk.guess.timeformat.RegexParts.MONTH;
import static org.embulk.guess.timeformat.RegexParts.MONTH_NAME_FULL;
import static org.embulk.guess.timeformat.RegexParts.MONTH_NAME_SHORT;
import static org.embulk.guess.timeformat.RegexParts.MONTH_NODELIM;
import static org.embulk.guess.timeformat.RegexParts.SECOND;
import static org.embulk.guess.timeformat.RegexParts.SECOND_NODELIM;
import static org.embulk.guess.timeformat.RegexParts.WEEKDAY_NAME_SHORT;
import static org.embulk.guess.timeformat.RegexParts.WEEKDAY_NAME_FULL;
import static org.embulk.guess.timeformat.RegexParts.YEAR;
import static org.embulk.guess.timeformat.RegexParts.ZONE_OFF;
import static org.embulk.guess.timeformat.RegexParts.ZONE_ABB;

class DatePickup {
    private DatePickup(
            final DateOrder order,
            final String year,
            final String month,
            final String day,
            final String rest,
            final String dateDelim) {
        this.order = order;
        this.year = year;
        this.month = month;
        this.day = day;
        this.rest = rest;
        this.dateDelim = dateDelim;
    }

    static Optional<DatePickup> from(final String text) {
        for (final DateMatcher matcher : DATE_MATCHERS) {
            final Optional<DatePickup> matched = matcher.match(text);
            if (matched.isPresent()) {
                return matched;
            }
        }
        return Optional.empty();
    }

    DateOrder getOrder() {
        return this.order;
    }

    String getYear() {
        return this.year;
    }

    String getDateDelim() {
        return this.dateDelim;
    }

    String getMonth() {
        return this.month;
    }

    String getDay() {
        return this.day;
    }

    String getRest() {
        return this.rest;
    }

    private static class DateMatcher {
        DateMatcher(final Pattern pattern, final boolean useDateDelim, final DateOrder order) {
            this.pattern = pattern;
            this.useDateDelim = useDateDelim;
            this.order = order;
        }

        Optional<DatePickup> match(final String text) {
            final Matcher matcher = this.pattern.matcher(text);
            if (matcher.matches()) {
                return Optional.of(new DatePickup(
                        this.order,
                        matcher.group("year"),
                        matcher.group("month"),
                        matcher.group("day"),
                        matcher.group("rest"),
                        (this.useDateDelim ? matcher.group("date_delim") : "")));
            } else {
                return Optional.empty();
            }
        }

        private final Pattern pattern;
        private final boolean useDateDelim;
        private final DateOrder order;
    }

    // yyyy-MM-dd
    private static final Pattern YMD_DELIM = Pattern.compile(String.format(
            "^(?<year>%s)(?<date_delim>%s)(?<month>%s)\\k<date_delim>(?<day>%s)(?<rest>.*?)$",
            YEAR, DATE_DELIMS, MONTH, DAY));
    private static final Pattern YMD_NODELIM = Pattern.compile(String.format(
            "^(?<year>%s)(?<month>%s)(?<day>%s)(?<rest>.*?)$",
            YEAR, MONTH_NODELIM, DAY_NODELIM));

    // MM/dd/yyyy
    private static final Pattern MDY_DELIM = Pattern.compile(String.format(
            "^(?<month>%s)(?<date_delim>%s)(?<day>%s)\\k<date_delim>(?<year>%s)(?<rest>.*?)$",
            MONTH, DATE_DELIMS, DAY, YEAR));
    private static final Pattern MDY_NODELIM = Pattern.compile(String.format(
            "^(?<month>%s)(?<day>%s)(?<year>%s)(?<rest>.*?)$",
            MONTH_NODELIM, DAY_NODELIM, YEAR));

    // dd.MM.yyyy
    private static final Pattern DMY_DELIM = Pattern.compile(String.format(
            "^(?<day>%s)(?<date_delim>%s)(?<month>%s)\\k<date_delim>(?<year>%s)(?<rest>.*?)$",
            DAY, DATE_DELIMS, MONTH, YEAR));
    private static final Pattern DMY_NODELIM = Pattern.compile(String.format(
            "^(?<day>%s)(?<month>%s)(?<year>%s)(?<rest>.*?)$",
            DAY_NODELIM, MONTH_NODELIM, YEAR));

    private static DateMatcher[] DATE_MATCHERS = {
        new DateMatcher(YMD_DELIM, true, DateOrder.YMD),
        new DateMatcher(YMD_NODELIM, false, DateOrder.YMD),
        new DateMatcher(MDY_DELIM, true, DateOrder.MDY),
        new DateMatcher(MDY_NODELIM, false, DateOrder.MDY),
        new DateMatcher(DMY_DELIM, true, DateOrder.DMY),
        new DateMatcher(DMY_NODELIM, false, DateOrder.DMY)
    };

    private final DateOrder order;
    private final String year;
    private final String month;
    private final String day;
    private final String rest;

    private final String dateDelim;
}
