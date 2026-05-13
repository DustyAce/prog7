package commands;

import commands.meta.Command;
import commands.meta.Invoker;
import handlers.OutputHandler;
import shared.requests.CommandRequest;

public class HistoryCommand implements Command {
    @Override
    public String desc() {
        return "show last 15 commands executed";
    }

    @Override
    public void execute(CommandRequest cr) {
        Invoker.getHistory().forEach( h -> { OutputHandler.message(h.command().getName()); });
    }

    @Override
    public String getName() {
        return "history";
    }
}
