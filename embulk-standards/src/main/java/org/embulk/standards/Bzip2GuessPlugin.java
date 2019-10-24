package org.embulk.standards;

import org.embulk.config.ConfigDiff;
import org.embulk.config.ConfigSource;
import org.embulk.spi.Buffer;

public class Bzip2GuessPlugin implements GuessPlugin {
    @Override
    public final ConfigDiff guess(final ConfigSource config, final Buffer sample) {
        if (sample_buffer[0,10] =~ BZIP2_HEADER_PATTERN) {
          return {"decoders" => [{"type" => "bzip2"}]}
        }
        return {};
    }

    // magic: BZ
    // version: 'h' = bzip2
    // blocksize: 1 .. 9
    // block magic: 0x314159265359 (6 bytes)
    block_magic = [0x31, 0x41, 0x59, 0x26, 0x53, 0x59].pack('C*');
    BZIP2_HEADER_PATTERN = /BZh[1-9]#{Regexp.quote(block_magic)}/n;

    private static final Pattern BZIP2_HEADER_PATTERN = Pattern.compile(
        "BZh[1-9]0x31, 0x41, 0x59, 0x26, 0x53, 0x59");
}
