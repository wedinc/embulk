package org.embulk.guess.timeformat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

class GuessMatch extends Match {
    GuessMatch(
            final List<String> delimiters,
            final List<GuessPart> parts,
            final List<GuessOption> partOptions) {
        this.delimiters = Collections.unmodifiableList(new ArrayList<>(delimiters));
        this.parts = Collections.unmodifiableList(new ArrayList<>(parts));
        this.partOptions = Collections.unmodifiableList(new ArrayList<>(partOptions));
    }

    @Override
    String getFormat() {
        final StringBuilder format = new StringBuilder();

        for (int i = 0; i < this.parts.size(); ++i) {
            if (i != 0) {
                format.append(this.delimiters.get(i - 1));
            }
            final GuessOption option = this.partOptions.get(i);

            switch (this.parts.get(i)) {
                case YEAR:
                    format.append("%Y");
                    break;

                case MONTH:
                    switch (option) {
                        case ZERO:
                            format.append("%m");
                            break;

                        case BLANK:
                            // format.append("%_m");  // not supported
                            format.append("%m");
                            break;

                        case NONE:
                            // format.append("%-m");  // not supported
                            format.append("%m");
                            break;

                        default:
                            format.append("%m");
                    }
                    break;

                case DAY:
                    switch (option) {
                        case ZERO:
                            format.append("%d");
                            break;

                        case BLANK:
                            format.append("%e");
                            break;

                        case NONE:
                            format.append("%d");  // not supported
                            break;

                        default:
                            format.append("%d");
                    }
                    break;

                case HOUR:
                    switch (option) {
                        case ZERO:
                            format.append("%H");
                            break;

                        case BLANK:
                            format.append("%k");
                            break;

                        case NONE:
                            format.append("%k");  // not supported
                            break;

                        default:
                            format.append("%H");
                    }
                    break;

                case MINUTE:
                    // heading options are not supported
                    format.append("%M");
                    break;

                case SECOND:
                    // heading options are not supported
                    format.append("%S");
                    break;

                case FRAC:
                    switch (option) {
                        case FRAC_3:
                            format.append("%L");
                            break;
                            /*
            #elsif option <= 6
            #  format << '%6N'
            #elsif option <= 6
            #  format << '%6N'
            #elsif option <= 9
            #  format << '%9N'
            #elsif option <= 12
            #  format << '%12N'
            #elsif option <= 15
            #  format << '%15N'
            #elsif option <= 18
            #  format << '%18N'
            #elsif option <= 21
            #  format << '%21N'
            #elsif option <= 24
            #  format << '%24N'
                            */
                        case FRAC_N:
                            format.append("%N");
                            break;
                        default:
                            format.append("%N");
                            break;
                    }
                    break;

                case ZONE:
                    switch (option) {
                        case EXTENDED:
                            format.append("%:z");
                            break;
                        default:
                            // :simple, :abb
                            // don't use %Z even with :abb: https://github.com/jruby/jruby/issues/3702
                            format.append("%z");
                            break;
                    }
                    break;
                default:
                    throw new RuntimeException("Unknown part: #{@parts[i]}");
            }
        }
        return format.toString();
    }

    @Override
    String getMergeableGroup() {
        // MDY is mergible with DMY
        final OptionalInt i = findPartialSequence(this.parts, DMY_SEQUENCE);

        if (i.isPresent()) {
            /*
          ps = @parts.dup
          ps[i, 3] = [:month, :day, :year]
          return [@delimiters, ps]
            */
        }

        //return [@delimiters, @parts];
        return null;
    }

    @Override
    void mergeFrom(final Match anotherInGroup) {
        if (!(anotherInGroup instanceof GuessMatch)) {
            throw new RuntimeException("Unexpected.");
        }

        // part_options = another_in_group.part_options
        final List<GuessOption> partOptions = ((GuessMatch) anotherInGroup).getPartOptions();

        /*
        @part_options.size.times do |i|
          @part_options[i] ||= part_options[i]
          if @part_options[i] == nil
            part_options[i]
          elsif part_options[i] == nil
            @part_options[i]
          else
            [@part_options[i], part_options[i]].sort.last
          end
        end

        # if DMY matches, MDY is likely false match of DMY.
        dmy = array_sequence_find(another_in_group.parts, [:day, :month, :year])
        mdy = array_sequence_find(@parts, [:month, :day, :year])
        if mdy && dmy
          @parts[mdy, 3] = [:day, :month, :year]
        end
        */
    }

    List<GuessPart> getParts() {
        return this.parts;
    }

    List<GuessOption> getPartOptions() {
        return this.partOptions;
    }

    private static OptionalInt findPartialSequence(final List<GuessPart> entire, final List<GuessPart> target) {
        for (int i = 0; i < (entire.size() - target.size() + 1); i++) {
            if (entire.subList(i, i + target.size()).equals(target)) {
                return OptionalInt.of(i);
            }
        }
        return OptionalInt.empty();
    }

    private static List<GuessPart> DMY_SEQUENCE = Arrays.asList(GuessPart.DAY, GuessPart.MONTH, GuessPart.YEAR);

    private final List<String> delimiters;
    private final List<GuessPart> parts;
    private final List<GuessOption> partOptions;
}
