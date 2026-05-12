package commands.meta;

import shared.elements.Route;

import java.util.Arrays;
import java.util.stream.Collectors;

public record CommandArgs(String[] args, Route route) {

    @Override
    public String toString() {
        return String.format("CommandArgs{strings=%s, route=%s}",
                "[" +
                Arrays.stream(this.args)
                        .map(s -> String.format("\"%s\"", s))
                        .collect(Collectors.joining(", ")) + "]",
                this.args);
    }
}
