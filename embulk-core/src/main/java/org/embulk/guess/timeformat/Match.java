package org.embulk.guess.timeformat;

abstract class Match {
    abstract String getFormat();

    abstract String getMergeableGroup();

    abstract void mergeFrom(final Match anotherInGroup);
}
