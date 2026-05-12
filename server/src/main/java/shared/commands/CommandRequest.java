package shared.commands;

import shared.Request;
import shared.elements.Route;

import java.io.Serializable;
import java.util.Arrays;

public class CommandRequest extends Request {
    CommandEnum command;
    String[] primitiveArgs;
    Route route;

    public CommandRequest() {}
    public CommandRequest(CommandEnum command) { this.command = command; }

    public void setArgs(String[] args) { this.primitiveArgs = args; }

    public void setRoute(Route r) { this.route = r; }

    public CommandEnum getCommand() { return command; }

    public Route getRoute() { return route; }

    public String[] getArgs() { return primitiveArgs; }

    @Override
    public String toString() {
        return String.format("REQUEST{ Command: %s ; args: %s ; Route: %s }", command, Arrays.toString(primitiveArgs), route);
    }
}
