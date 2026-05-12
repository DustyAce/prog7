package commands;

import commands.meta.Command;
import commands.meta.CommandArgs;
import commands.meta.Invoker;
import handlers.CollectionHandler;
import handlers.OutputHandler;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class FilterNameCommand implements Command {
    @Override
    public String desc() {
        return "output all Routes w/ names containing {pattern}";
    }

    @Override
    public void execute(CommandArgs ca) {
        OutputHandler.setRoutes(
                CollectionHandler.getRoutes().stream()
                        .filter( r -> r.getName()
                                .contains(
                                        String.join( " ", ca.args() )
                        )).collect(Collectors.toCollection(ArrayList::new))
        );
    }

    @Override
    public String getName() {
        return "filter_contains_name";
    }
}
