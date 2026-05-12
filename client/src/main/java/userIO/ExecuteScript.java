package userIO;

import Exceptions.BadIdException;
import communication.CommunicationHandler;
import shared.commands.CommandEnum;
import shared.requests.CommandRequest;
import shared.requests.Request;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;

public class ExecuteScript {
    private static final HashSet<File> runningScripts = new HashSet<>();

    public static void execute(File f) {
        if (runningScripts.contains(f)) {
            System.out.println("Recursion! Returning...");
            return;
        }
        runningScripts.add(f);

        try {
            InputHandler.sc = new Scanner(f);
            while (InputHandler.sc.hasNext()) {
                try {
                    Request req = InputHandler.requestInput();
                    if (req == null) { System.out.println("No such command. Try 'help'"); continue; }

                    if (req instanceof CommandRequest cr) {
                        if (cr.getCommand() == CommandEnum.EXIT) {System.out.println("k bye"); System.exit(0);}
                        if (cr.getCommand() == CommandEnum.EXECUTE_SCRIPT) {
                            ExecuteScript.execute( new File(cr.getArgs()[0]) );
                            continue;
                        }
                    }

                    CommunicationHandler.request(req);
                }
                catch (BadIdException e) { System.out.println("Bad argument"); }
            }
        } catch (FileNotFoundException e) {
            System.out.println("No such file :(");
        } finally {
            InputHandler.sc = new Scanner(System.in);
            runningScripts.remove(f);
        }
    }
}
