package shared.requests;

import shared.commands.CommandEnum;
import shared.elements.Route;
import shared.responses.Response;

public class CommandRequest extends Request {
    CommandEnum command;
    String[] primitiveArgs;
    Route route;

    public CommandRequest() { }

    public void setArgs(String[] args) { this.primitiveArgs = args; }

    public void setRoute(Route r) { this.route = r; }

    public CommandEnum getCommand() { return command; }

    public Route getRoute() { return route; }

    public String[] getArgs() { return primitiveArgs; }

    @Override
    public void processResponse(Response r) {}
}
