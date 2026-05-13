package shared.requests;

import shared.commands.CommandEnum;
import shared.elements.Route;
import shared.responses.Response;

public class CommandRequest extends Request {
    CommandEnum command;
    String[] primitiveArgs;
    Route route;
    String username;
    String password;

    public CommandRequest() { }

    public void setArgs(String[] args) { this.primitiveArgs = args; }

    public void setRoute(Route r) { this.route = r; }

    public CommandEnum getCommand() { return command; }

    public Route getRoute() { return route; }

    public String[] getArgs() { return primitiveArgs; }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public void processResponse(Response r) {}

    @Override
    public String toString() {
        return String.format("CommandRequest{%s, [%s], %s} by '%s'",
                command,
                String.join(",", primitiveArgs),
                route,
                username
                );
    }
}
