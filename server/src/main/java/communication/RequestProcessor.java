package communication;

import commands.meta.Invoker;
import handlers.CollectionHandler;
import handlers.DatabaseHandler;
import handlers.OutputHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import shared.elements.Route;
import shared.requests.*;
import shared.responses.BooleanResponse;
import shared.responses.CommandResponse;
import shared.responses.Response;

import java.io.IOException;
import java.net.DatagramPacket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RequestProcessor implements Runnable{
    private final Logger logger = LogManager.getLogger("com.github.dustyace.lab6");
    private final DatagramPacket dp;
    private final Request req;
    private static ExecutorService executorService = Executors.newFixedThreadPool(5);

    RequestProcessor(Request req, DatagramPacket dp) {
        this.req=req;
        this.dp=dp;
    }

    @Override
    public void run() {
        try {
            if (req instanceof CheckRouteExistsRequest) {
                processCheckRouteExistsRequest( (CheckRouteExistsRequest) req);
            } else if (req instanceof LoginRequest lr) {
                processLoginRequest(lr);
            } else {
                processCommandRequest( (CommandRequest) req);
            }
        } catch (IOException e) {
            logger.error("Something went wrong during processing a request!");
            logger.error(e.getMessage());
        }
    }

    private void processCommandRequest(CommandRequest req) throws IOException {
        System.out.println("hi");
        if (DatabaseHandler.checkUser(req.getUsername(), req.getPassword())) {
            Invoker.executeCommand(req);
        } else {
            OutputHandler.message("Bad credentials!");
        }

        ArrayList<Route> routes = OutputHandler.getRoutes();
        Response r = new CommandResponse(routes, OutputHandler.getMessage());
        sendResponse(r);
    }

    private void processLoginRequest(LoginRequest lr) throws IOException{
        BooleanResponse resp = new BooleanResponse();
        if (DatabaseHandler.checkUser(lr.getUsername(), lr.getPassword()) ) {
            resp.setStatus(true);
        } else if (lr instanceof RegisterRequest && DatabaseHandler.registerUser(lr.getUsername(), lr.getPassword())) {
            resp.setStatus(true);
        } else {resp.setStatus(false);}
        sendResponse( resp );
    }

    private void processCheckRouteExistsRequest(CheckRouteExistsRequest req) throws IOException {
        logger.info("'{}' looking for route with id {}", req.getUsername(), req.getId());

        BooleanResponse resp = new BooleanResponse();
        boolean isRouteIdValid = CollectionHandler.isIdValid(req.getId());
        boolean isRouteOwner = DatabaseHandler.checkOwnership(req.getId(), req.getUsername());
        resp.setStatus(isRouteIdValid && isRouteOwner);
        if (isRouteIdValid && isRouteOwner) { logger.info("Such route exists and is owned by requesting user"); }
        else { logger.info("Such route doesn't exist/is owned by another"); }

        sendResponse(resp);
    }

    private void sendResponse(Response r) {
        executorService.execute( new Responder(r, dp) );
    }

}
