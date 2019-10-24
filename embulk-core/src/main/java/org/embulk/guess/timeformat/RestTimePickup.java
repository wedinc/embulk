package org.embulk.guess.timeformat;

import java.util.Optional;
import java.util.regex.Matcher;

import static org.embulk.guess.timeformat.RegexParts.DATE_TIME_DELIMS;
import static org.embulk.guess.timeformat.RegexParts.HOUR;
import static org.embulk.guess.timeformat.RegexParts.HOUR_NODELIM;
import static org.embulk.guess.timeformat.RegexParts.MINUTE;
import static org.embulk.guess.timeformat.RegexParts.MINUTE_NODELIM;
import static org.embulk.guess.timeformat.RegexParts.SECOND;
import static org.embulk.guess.timeformat.RegexParts.SECOND_NODELIM;
import static org.embulk.guess.timeformat.RegexParts.TIME;
import static org.embulk.guess.timeformat.RegexParts.TIME_DELIMS;
import static org.embulk.guess.timeformat.RegexParts.TIME_NODELIM;

class RestTimePickup {
    private RestTimePickup(
            final String hour,
            final String minute,
            final String second,
            final String rest,
            final String timeDelim) {
        this.hour = hour;
        this.minute = minute;
        this.second = second;
        this.rest = rest;
        this.timeDelim = timeDelim;
    }

    static Optional<RestTimePickup> from(final String text) {
        for (final RestTimeMatcher matcher : REST_TIME_MATCHERS) {
            final Optional<RestTimePickup> matched = matcher.match(text);
            if (matched.isPresent()) {
                return matched;
            }
        }
        return Optional.empty();
    }

    String getHour() {
        return this.hour;
    }

    String getTimeDelim() {
        return this.timeDelim;
    }

    String getMinute() {
        return this.minute;
    }

    String getSecond() {
        return this.second;
    }

    String getRest() {
        return this.rest;
    }

    private static class RestTimeMatcher {
        RestTimeMatcher(final java.util.regex.Pattern pattern, final boolean requireDateDelimEmpty) {
            this.pattern = pattern;
            this.requireDateDelimEmpty = requireDateDelimEmpty;
        }

        Optional<RestTimePickup> match(final String text) {
            final Matcher matcher = this.pattern.matcher(text);
            if (/*TODO: check requireDateDelim*/ matcher.matches()) {
                return Optional.of(new RestTimePickup(
                        matcher.group("hour"),
                        matcher.group("minute"),
                        matcher.group("second"),
                        matcher.group("rest"),
                        matcher.group("time_delim")));
            } else {
                return Optional.empty();
            }
        }

        private final java.util.regex.Pattern pattern;
        private final boolean requireDateDelimEmpty;
    }

    private static final java.util.regex.Pattern BOTH_DELIM = java.util.regex.Pattern.compile(String.format(
            "^(?<date_time_delim>%s)%s(?<rest>.*?)?$", DATE_TIME_DELIMS, TIME));

    private static final java.util.regex.Pattern TIME1_DELIM = java.util.regex.Pattern.compile(String.format(
            "^(?<date_time_delim>%s)%s(?<rest>.*?)?$", DATE_TIME_DELIMS, TIME_NODELIM));

    private static final java.util.regex.Pattern TIME2_DELIM = java.util.regex.Pattern.compile(String.format(
            "^%s(?<rest>.*?)?$", TIME_NODELIM));

    private static RestTimeMatcher[] REST_TIME_MATCHERS = {
        new RestTimeMatcher(BOTH_DELIM, false),
        new RestTimeMatcher(TIME1_DELIM, false),
        new RestTimeMatcher(TIME2_DELIM, true),
    };

    private final String hour;
    private final String minute;
    private final String second;
    private final String rest;

    private final String timeDelim;
}
