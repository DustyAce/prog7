package commands;

import commands.meta.Command;
import shared.requests.CommandRequest;

public class ExitCommand implements Command {
    public String desc() {return "exit without saving";}

    public void execute(CommandRequest cr){
        System.exit(3);
    }

    @Override
    public String getName() {
        return "exit";
    }
}
