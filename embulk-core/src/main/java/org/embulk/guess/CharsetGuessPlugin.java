package org.embulk.guess;

import com.ibm.icu.text.CharsetDetector;  // To be moved under a sub ClassLoader.
import com.ibm.icu.text.CharsetMatch;  // To be moved under a sub ClassLoader.
import org.embulk.config.ConfigDiff;
import org.embulk.config.ConfigSource;
import org.embulk.spi.Buffer;
import org.embulk.spi.Exec;
import org.embulk.spi.GuessPlugin;

public final class CharsetGuessPlugin implements GuessPlugin {
    @Override
    public ConfigDiff guess(final ConfigSource config, final Buffer sample) {
        final CharsetDetector detector = new CharsetDetector();
        detector.setText(sample.array());  // Unsafe to call |array()|.

        final CharsetMatch bestMatch = detector.detect();

        final ConfigDiff charset = Exec.newConfigDiff();
        if (bestMatch.getConfidence() < 50) {
            charset.set("charset", "UTF-8");
        } else {
            charset.set("charset", convertPredefined(bestMatch.getName()));
        }

        final ConfigDiff result = Exec.newConfigDiff();
        result.setNested("parser", charset);
        return result;
    }

    private static String convertPredefined(final String before) {
        switch (before) {
            // ISO-8859-1 means ASCII which is a subset of UTF-8 in most of cases
            // due to lack of sample data set.
            case "ISO-8859-1":
                return "UTF-8";

            // Shift_JIS is used almost only by Windows that uses "CP932" in fact.
            // And "CP932" called by Microsoft actually means "MS932" in Java.
            case "Shift_JIS":
                return "MS932";
        }
        return before;
    }
}
