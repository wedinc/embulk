package org.embulk.guess.timeformat;

class SimpleMatch extends Match {
    SimpleMatch(final String format) {
        this.format = format;
    }

    @Override
    String getFormat() {
        return this.format;
    }

    @Override
    String getMergeableGroup() {
        return this.format;
    }

    @Override
    void mergeFrom(final Match anotherInGroup) {
    }

    private final String format;
}
