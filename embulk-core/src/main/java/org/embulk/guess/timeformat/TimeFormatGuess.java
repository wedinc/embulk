package org.embulk.guess.timeformat;

import java.util.ArrayList;
import java.util.List;

/**
 * A utility class to guess a date-time format.
 */
public final class TimeFormatGuess {
    private TimeFormatGuess() {
        // No instantiation.
    }

    /**
     * Guesses a date-time format from examples objects representing timestamps.
     *
     * @param objects
     *
     * @return a String
     */
    public static String guess(final List<Object> objects) {
        final ArrayList<Match> matches = new ArrayList<>();
        for (final Object object : objects) {
            final String text = object.toString();
            if (!text.isEmpty()) {
                for (final Pattern pattern : PATTERNS) {
                    pattern.match(text);
                }
            }
        }

        /*
        for (final String text : texts) {
        }
      matches = texts.map do |text|
        PATTERNS.map {|pattern| pattern.match(text) }.compact
      end.flatten
        */

        if (matches.isEmpty()) {
            return null;
        } else if (matches.size() == 1) {
            return matches.get(0).getFormat();
        } else {
            /*
        match_groups = matches.group_by {|match| match.mergeable_group }.values
        best_match_group = match_groups.sort_by {|group| group.size }.last
        best_match = best_match_group.shift
        best_match_group.each {|m| best_match.merge!(m) }
        return best_match.format
            */
            return null;
        }
    }

    private static final Pattern[] PATTERNS = {
        new GuessPattern(),
        new Rfc2822Pattern(),
        StandardRegexpPatterns.APACHE_CLF,
        StandardRegexpPatterns.ANSI_C_ASCTIME
    };
}
