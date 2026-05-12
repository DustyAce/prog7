package commands;

import commands.meta.Command;
import commands.meta.CommandArgs;
import commands.meta.Invoker;

public class UndoCommand implements Command {
    @Override
    public String desc() {
        return "undo ";
    }

    @Override
    public void execute(CommandArgs ca) {
        Invoker.undo();
    }

    @Override
    public String getName() {
        return "undo";
    }
}
