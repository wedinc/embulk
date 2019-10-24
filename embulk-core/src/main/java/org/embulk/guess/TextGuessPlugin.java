package org.embulk.guess;

import java.util.ArrayList;
import org.embulk.config.ConfigDiff;
import org.embulk.config.ConfigSource;
import org.embulk.spi.Buffer;
import org.embulk.spi.Exec;
import org.embulk.spi.GuessPlugin;
import org.embulk.spi.util.LineDecoder;
import org.embulk.spi.util.ListFileInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class TextGuessPlugin implements GuessPlugin {
    @Override
    public final ConfigDiff guess(final ConfigSource config, final Buffer sample) {
        final ConfigSource parserConfig = config.getNestedOrGetEmpty("parser");
        if (parserConfig.getNestedOrGetEmpty("charset").isEmpty()) {
            return (new CharsetGuessPlugin()).guess(config, sample);
        }

        final LineDecoder.DecoderTask parserTask;
        try {
            parserTask = parserConfig.loadConfig(LineDecoder.DecoderTask.class);
        } catch (final Exception ex) {
            logger.warn(ex.getMessage(), ex);
            return Exec.newConfigDiff();
        }

        final ArrayList<Buffer> listBuffer = new ArrayList<>();
        listBuffer.add(sample);
        final ArrayList<ArrayList<Buffer>> listListBuffer = new ArrayList<>();
        listListBuffer.add(listBuffer);

        final LineDecoder decoder = new LineDecoder(new ListFileInput(listListBuffer), parserTask);
        final StringBuilder sampleText = new StringBuilder();
        while (decoder.nextFile()) {  // TODO: Confirm decoder contains only one, and stop looping.
            boolean first = true;
            while (true) {
                final String line = decoder.poll();
                if (line == null) {
                    break;
                }

                if (first) {
                    first = false;
                } else {
                    sampleText.append(parserTask.getNewline().getString());
                }
                sampleText.append(line);
            }
        }

        return this.guessText(config, sampleText.toString());
    }

    public abstract ConfigDiff guessText(final ConfigSource config, final String sampleText);

    private static final Logger logger = LoggerFactory.getLogger(TextGuessPlugin.class);
}
