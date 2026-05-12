package userIO;

import communication.CommunicationHandler;
import shared.commands.CommandEnum;
import shared.requests.CommandRequest;

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
                CommandRequest cr = InputHandler.commandInput();
                if (cr == null) { System.out.println("No such command. Try 'help'"); continue; }
                if (cr.getCommand() == CommandEnum.EXIT) {System.out.println("k bye"); System.exit(0);}
                if (cr.getCommand() == CommandEnum.EXECUTE_SCRIPT) {
                    ExecuteScript.execute( new File(cr.getArgs()[0]) );
                    continue;
                }
                CommunicationHandler.request(cr);
            }
        } catch (FileNotFoundException e) {
            System.out.println("No such file :(");
        } finally {
            InputHandler.sc = new Scanner(System.in);
            runningScripts.remove(f);
        }
    }
}
