package commands;

import commands.meta.Command;
import handlers.CollectionHandler;
import handlers.OutputHandler;
import shared.requests.CommandRequest;

import java.util.ArrayList;

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
    public void execute(CommandRequest cr) {
        if (cr.getArgs().length==0) {
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
