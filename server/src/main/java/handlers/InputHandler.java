package handlers;

import commands.ExitCommand;
import commands.meta.Command;
import shared.requests.CommandRequest;

import java.util.HashMap;
import java.util.Scanner;

public class InputHandler extends Thread {
    static Scanner sc = new Scanner(System.in);
    private static HashMap<String, Command> serverCommands = new HashMap<>();
    static {
        serverCommands.put("exit", new ExitCommand());
    }

    public void run() {
        while (sc.hasNext()) {
            String inp = sc.nextLine().trim().toLowerCase();
            Command c = serverCommands.get(inp);
            if (c == null) { System.out.println("No such command"); continue; }
            c.execute( new CommandRequest() );
        }
    }
}
