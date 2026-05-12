package commands;

import commands.meta.Command;
import commands.meta.CommandArgs;

public class ExitCommand implements Command {
    public String desc() {return "exit without saving";}

    public void execute(CommandArgs ca){
        System.exit(3);
    }

    @Override
    public String getName() {
        return "exit";
    }
}
