package commands;

import commands.meta.Command;
import commands.meta.Invoker;
import handlers.OutputHandler;
import shared.requests.CommandRequest;

import java.util.stream.Collectors;

public class HelpCommand implements Command {
    static String text;

    public String desc() {
        return "outputs a list of all commands";
    }

    public void execute(CommandRequest cr) {
        if (text == null) {
            text = Invoker.getCommands().entrySet().stream().map(
                entry -> String.format("%-16s - %s", entry.getKey(), entry.getValue().desc())
            ).sorted()
            .collect(Collectors.joining("\n"));}
        OutputHandler.message(text);
    }

    @Override
    public String getName() {
        return "help";
    }
}
