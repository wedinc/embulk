package org.embulk.guess.timeformat;

abstract class Pattern {
    abstract Match match(final String text);
}
