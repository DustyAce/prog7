package commands;

import commands.meta.Command;
import commands.meta.Invoker;
import shared.requests.CommandRequest;

public class RedoCommand implements Command {
    @Override
    public String desc() {
        return "redo the last undone command";
    }

    @Override
    public void execute(CommandRequest cr) {
        Invoker.redo();
    }

    @Override
    public String getName() {
        return "redo";
    }
}
