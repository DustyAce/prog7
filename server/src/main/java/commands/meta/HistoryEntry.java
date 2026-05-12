package commands.meta;

public record HistoryEntry(Command command, String... args) { }