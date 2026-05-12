package commands;
import commands.meta.Command;
import commands.meta.CommandArgs;
import handlers.CollectionHandler;
import handlers.FileHandler;

public class SaveCommand implements Command {
    @Override
    public String desc() {
        return "saves collection to a file";
    }

    @Override
    public void execute(CommandArgs ca) {
        FileHandler.save(CollectionHandler.getRoutes());
    }

    @Override
    public String getName() {
        return "save";
    }
}
