package commands;

import commands.meta.Command;
import handlers.OutputHandler;
import shared.requests.CommandRequest;

public class EchoCommand implements Command {
    public String desc() {
        return "echo";
    }

    public void execute(CommandRequest cr) {
        OutputHandler.message( String.join(" ", cr.getArgs())) ;
    }

    @Override
    public String getName() {
        return "echo";
    }
}
