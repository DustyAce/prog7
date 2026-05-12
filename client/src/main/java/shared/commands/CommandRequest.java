package shared.commands;

import shared.Request;
import shared.elements.Route;

public class CommandRequest extends Request {
    CommandEnum command;
    String[] primitiveArgs;
    Route route;

    public CommandRequest(CommandEnum command) { this.command = command; }

    public void setArgs(String[] args) { this.primitiveArgs = args; }

    public void setRoute(Route r) { this.route = r; }

    public CommandEnum getCommand() { return command; }

    public Route getRoute() { return route; }

    public String[] getArgs() { return primitiveArgs; }
}
