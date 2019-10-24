package org.embulk.guess;

import org.embulk.config.ConfigDiff;
import org.embulk.config.ConfigSource;
import org.embulk.spi.Buffer;
import org.embulk.spi.Exec;
import org.embulk.spi.GuessPlugin;

public class NewlineGuessPlugin implements GuessPlugin {
    @Override
    public final ConfigDiff guess(final ConfigSource config, final Buffer sample) {
        if (config.getNestedOrGetEmpty("parser").getNestedOrGetEmpty("charset").isEmpty()) {
            return (new CharsetGuessPlugin()).guess(config, sample);
        }

        final byte[] array = sample.array();  // TODO: Unsafe to call Buffer#array().

        final int crCount = count(array, CR);
        final int lfCount = count(array, LF);
        final int crlfCount = count(array, CRLF);

        final ConfigDiff newlineConfig = Exec.newConfigDiff();
        if (crlfCount > crCount / 2 && crlfCount > lfCount / 2) {
            newlineConfig.set("newline", "CRLF");
        } else if (crCount > lfCount / 2) {
            newlineConfig.set("newline", "CR");
        } else {
            newlineConfig.set("newline", "LF");
        }

        final ConfigDiff parserConfig = Exec.newConfigDiff();
        parserConfig.setNested("parser", newlineConfig);
        return parserConfig;
    }

    static int countForTesting(final byte[] array, final byte[] target) {
        return count(array, target);
    }

    private static int count(final byte[] array, final byte[] target) {
        if (target.length == 0) {
            return 0;
        }

        int count = 0;
        outer: for (int i = 0; i < array.length - target.length + 1; i++) {
            for (int j = 0; j < target.length; j++) {
                if (array[i + j] != target[j]) {
                    continue outer;
                }
            }
            count++;
        }
        return count;
    }

    private static final byte[] CRLF = { (byte) '\r', (byte) '\n' };
    private static final byte[] CR = { (byte) '\r' };
    private static final byte[] LF = { (byte) '\n' };
}
