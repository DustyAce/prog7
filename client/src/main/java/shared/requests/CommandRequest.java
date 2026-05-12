package shared.requests;

import communication.LocalCollectionHandler;
import shared.commands.CommandEnum;
import shared.elements.Route;
import shared.responses.CommandResponse;
import shared.responses.Response;

import static communication.SerializationHandler.deserializeResponse;

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

    @Override
    public void processResponse(Response r) {
        CommandResponse resp = (CommandResponse) r;
        try {
            Route.isLoading = true;
            if (!resp.routes().isEmpty() && this.getCommand() != CommandEnum.PSZH)  {
                resp.routes().forEach(System.out::println);
            }

            if (!resp.output().isBlank())
            { System.out.println(resp.output().strip()); }

            Route.updateInstanceCounter( resp.routes().stream()
                    .mapToLong(Route::getId)
                    .max()
                    .orElse(-1) + 1);

            LocalCollectionHandler.updateExistingRoutes( resp.routes().stream()
                    .map(Route::getId)
                    .toList() );
        } finally {
            Route.isLoading = false;
        }
    }
}
