package commands;

import commands.meta.Command;
import commands.meta.CommandArgs;
import handlers.OutputHandler;

import java.util.Arrays;

public class EchoCommand implements Command {
    public String desc() {
        return "echo";
    }

    public void execute(CommandArgs ca) {
        OutputHandler.message( String.join(" ", ca.args())) ;
    }

    @Override
    public String getName() {
        return "echo";
    }
}
