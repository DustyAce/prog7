import Exceptions.BadIdException;
import communication.CommunicationHandler;
import shared.commands.CommandEnum;
import shared.requests.CommandRequest;
import shared.requests.Request;
import userIO.ExecuteScript;
import userIO.InputHandler;

import java.io.File;
import java.util.NoSuchElementException;

class Main {
    public static void main(String[] args) {
        try {
        while (true) {
        try {
                Request req = InputHandler.requestInput();
                if (req == null) { System.out.println("No such command. Try 'help'"); continue; }

                if (req instanceof CommandRequest cr) {
                    if (cr.getCommand() == CommandEnum.EXIT) {System.out.println("k bye"); System.exit(0);}
                    if (cr.getCommand() == CommandEnum.EXECUTE_SCRIPT) {
                        ExecuteScript.execute( new File(cr.getArgs()[0]) );
                        continue;
                    }
                    //System.out.println(cr.getUsername());
                }
                CommunicationHandler.request(req);
        }
        catch (BadIdException e) { System.out.println("Bad argument"); }
        } } catch (NoSuchElementException e) {System.out.println("got it!");}
    }
}