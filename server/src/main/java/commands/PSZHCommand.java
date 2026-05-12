package commands;

import commands.meta.Command;
import commands.meta.CommandArgs;
import handlers.CollectionHandler;
import handlers.OutputHandler;
import shared.elements.Route;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * ПСЖ
 */
public class PSZHCommand implements Command {
    @Override
    public String desc() {
        return "ПСЖ";
    }

    /**
     * ПСЖ
     */
    @Override
    public void execute(CommandArgs ca) {
        if (ca.args().length==0) {
        OutputHandler.message(
                """
                        ##########      ########    ##  ##  ##
                        ##########     #########    ##  ##  ##
                        ##      ##    ####           ## ## ##\s
                        ##      ##    ###             ###### \s
                        ##      ##    ###            ########\s
                        ##      ##    ####           ## ## ##\s
                        ##      ##     #########    ### ## ###
                        ##      ##       #######    ##  ##  ##"""); }
        OutputHandler.setRoutes(new ArrayList<>(CollectionHandler.getRoutes()));
    }

    @Override
    public String getName() {
        return "псж";
    }


}
