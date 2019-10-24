package org.embulk.guess;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TestNewlineGuessPlugin {
    @Test
    public void testSimple() {
        assertEquals(2, NewlineGuessPlugin.countForTesting(ARRAY1, CRLF));
        assertEquals(3, NewlineGuessPlugin.countForTesting(ARRAY1, CR));
        assertEquals(3, NewlineGuessPlugin.countForTesting(ARRAY1, LF));
    }

    private static final byte[] ARRAY1 = {
        (byte) 'a', (byte) '\r', (byte) '\r', (byte) '\n', (byte) '\r', (byte) '\n', (byte) '\n', (byte) 'a'
    };

    private static final byte[] CRLF = { (byte) '\r', (byte) '\n' };
    private static final byte[] CR = { (byte) '\r' };
    private static final byte[] LF = { (byte) '\n' };
}
