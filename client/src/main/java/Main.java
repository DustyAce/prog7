import Exceptions.BadIdException;
import communication.CommunicationHandler;
import shared.commands.CommandEnum;
import shared.commands.CommandRequest;
import userIO.ExecuteScript;
import userIO.InputHandler;

import java.io.File;
import java.util.NoSuchElementException;

class Main {
    public static void main(String[] args) {
        try {
        while (true) {
        try {
                CommandRequest cr = InputHandler.commandInput();
                if (cr == null) { System.out.println("No such command. Try 'help'"); continue; }
                if (cr.getCommand() == CommandEnum.EXIT) {System.out.println("k bye"); System.exit(0);}
                if (cr.getCommand() == CommandEnum.EXECUTE_SCRIPT) {
                    ExecuteScript.execute( new File(cr.getArgs()[0]) );
                    continue;
                }
                CommunicationHandler.request(cr);
        }
        catch (BadIdException e) { System.out.println("Bad argument"); }
        } } catch (NoSuchElementException e) {System.out.println("got it!");}
    }
}