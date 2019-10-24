package org.embulk.guess.timeformat;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class GuessPattern extends Pattern {
    @Override
    Match match(final String text) {
        final ArrayList<String> delimiters = new ArrayList<>();
        final ArrayList<GuessPart> parts = new ArrayList<>();
        final ArrayList<GuessOption> partOptions = new ArrayList<>();

        final Optional<DatePickup> datePickup = DatePickup.from(text);

        if (!datePickup.isPresent()) {
            return null;
        }

        final String dateDelim = datePickup.get().getDateDelim();

        switch (datePickup.get().getOrder()) {
            case YMD:
                parts.add(GuessPart.YEAR);
                partOptions.add(null);  // To be compacted.

                delimiters.add(dateDelim);
                parts.add(GuessPart.MONTH);
                partOptions.add(partHeadingOption(datePickup.get().getMonth()));

                delimiters.add(dateDelim);
                parts.add(GuessPart.DAY);
                partOptions.add(partHeadingOption(datePickup.get().getDay()));
                break;
            case MDY:
                parts.add(GuessPart.MONTH);
                partOptions.add(partHeadingOption(datePickup.get().getMonth()));

                delimiters.add(dateDelim);
                parts.add(GuessPart.DAY);
                partOptions.add(partHeadingOption(datePickup.get().getDay()));

                delimiters.add(dateDelim);
                parts.add(GuessPart.YEAR);
                partOptions.add(null);  // To be compacted.
                break;
            case DMY:
                parts.add(GuessPart.DAY);
                partOptions.add(partHeadingOption(datePickup.get().getDay()));

                delimiters.add(dateDelim);
                parts.add(GuessPart.MONTH);
                partOptions.add(partHeadingOption(datePickup.get().getMonth()));

                delimiters.add(dateDelim);
                parts.add(GuessPart.YEAR);
                partOptions.add(null);  // To be compacted.
                break;
            default:
                return null;
        }

        final Optional<RestTimePickup> restTimePickup = RestTimePickup.from(datePickup.get().getRest());

            /*
        if tm = (
              /^(?<date_time_delim>#{date_time_delims})#{TIME}(?<rest>.*?)?$/.match(rest) or
              /^(?<date_time_delim>#{date_time_delims})#{TIME_NODELIM}(?<rest>.*?)?$/.match(rest) or
              (date_delim == "" && /^#{TIME_NODELIM}(?<rest>.*?)?$/.match(rest))
            )
          date_time_delim = tm["date_time_delim"] rescue ""
          time_delim = tm["time_delim"] rescue ""

          delimiters << date_time_delim
          parts << :hour
          part_options << part_heading_option(tm["hour"])

          if tm["minute"]
            delimiters << time_delim
            parts << :minute
            part_options << part_heading_option(tm["minute"])

            if tm["second"]
              delimiters << time_delim
              parts << :second
              part_options << part_heading_option(tm["second"])

              if tm["frac"]
                delimiters << tm["frac_delim"]
                parts << :frac

                //// Here, reduce the integer "size" into Option
                part_options << tm["frac"].size
              end
            end
          end

          rest = tm["rest"]
        end

        if zm = /^#{ZONE}$/.match(rest)
          delimiters << (zm["zone_space"] || '')
          parts << :zone
          if zm["zone_off"]
            if zm["zone_off"].include?(':')
              part_options << :extended
            else
              part_options << :simple
            end
          else
            part_options << :abb
          end

          return GuessMatch.new(delimiters, parts, part_options)

        elsif rest =~ /^\s*$/
          return GuessMatch.new(delimiters, parts, part_options)

        else
          return nil
        end
*/
        return null;
    }

    private static GuessOption partHeadingOption(final String text) {
        if (text.charAt(0) == '0') {
            return GuessOption.ZERO;
        } else if (text.charAt(0) == ' ') {
            return GuessOption.BLANK;
        } else if (text.length() == 1) {
            return GuessOption.NONE;
        } else {
            return null;
        }
    }
}
