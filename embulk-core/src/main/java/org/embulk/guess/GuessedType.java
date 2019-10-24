package org.embulk.guess;

public final class GuessedType {
    private GuessedType(final String name, final String format) {
        this.name = name;
        this.format = format;
    }

    private GuessedType(final String name) {
        this(name, null);
    }

    public String getName() {
        return this.name;
    }

    public String getFormat() {
        return this.format;
    }

    public static final GuessedType BOOLEAN = new GuessedType("boolean");

    public static final GuessedType LONG = new GuessedType("long");

    public static final GuessedType DOUBLE = new GuessedType("double");

    public static final GuessedType STRING = new GuessedType("string");

    public static GuessedType createTimestampType(final String format) {
        return new GuessedType("timestamp", format);
    }

    public static final GuessedType JSON = new GuessedType("json");

    private final String name;
    private final String format;
}
