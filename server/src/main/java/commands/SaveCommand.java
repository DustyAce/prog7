package commands;
import commands.meta.Command;
import handlers.CollectionHandler;
import handlers.FileHandler;
import shared.requests.CommandRequest;

public class SaveCommand implements Command {
    @Override
    public String desc() {
        return "saves collection to a file";
    }

    @Override
    public void execute(CommandRequest cr) {
        FileHandler.save(CollectionHandler.getRoutes());
    }

    @Override
    public String getName() {
        return "save";
    }
}
