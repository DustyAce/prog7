package commands;

import commands.meta.Command;
import commands.meta.Invoker;
import shared.requests.CommandRequest;

public class UndoCommand implements Command {
    @Override
    public String desc() {
        return "undo ";
    }

    @Override
    public void execute(CommandRequest cr) {
        Invoker.undo();
    }

    @Override
    public String getName() {
        return "undo";
    }
}
