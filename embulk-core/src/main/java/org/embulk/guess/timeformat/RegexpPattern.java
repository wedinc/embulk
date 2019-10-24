package org.embulk.guess.timeformat;

class RegexpPattern extends Pattern {
    RegexpPattern(final java.util.regex.Pattern regexp, final String format) {
        this.regexp = regexp;
        this.match = new SimpleMatch(format);
    }

    @Override
    Match match(final String text) {
        if (this.regexp.matcher(text).matches()) {
            return this.match;
        } else {
            return null;
        }
    }

    private final java.util.regex.Pattern regexp;
    private final Match match;
}
