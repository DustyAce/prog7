package commands;

import commands.meta.Command;
import commands.meta.CommandArgs;
import commands.meta.Invoker;

public class RedoCommand implements Command {
    @Override
    public String desc() {
        return "redo the last undone command";
    }

    @Override
    public void execute(CommandArgs ca) {
        Invoker.redo();
    }

    @Override
    public String getName() {
        return "redo";
    }
}
