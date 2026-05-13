package commands.meta;

import commands.*;
import handlers.OutputHandler;
import shared.commands.*;
import shared.elements.Route;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import shared.requests.CommandRequest;

import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.Stack;

/**
 * Class responsible for executing commands
 */
public class Invoker {
    private static final Logger logger = LogManager.getLogger("com.github.dustyace.lab6");

    static HashMap<CommandEnum, Command> commands = new HashMap<>();

    static Stack<HistoryEntry> commandHistory = new Stack<>();
    static Stack<Route[]> routeHistory = new Stack<>();
    static Stack<HistoryEntry> commandUndone = new Stack<>();
    static Stack<Route[]> routeUndone = new Stack<>();

    public static boolean historyWritable = true;

    static {
        commands.put(CommandEnum.SHOW, new ShowCommand());
        commands.put(CommandEnum.ADD, new AddCommand());
        commands.put(CommandEnum.ADD_IF_MIN, new AddMinCommand());
        commands.put(CommandEnum.CLEAR, new ClearCommand());
        commands.put(CommandEnum.ECHO, new EchoCommand());
        commands.put(CommandEnum.HISTORY, new HistoryCommand());
        commands.put(CommandEnum.INFO, new InfoCommand());
        commands.put(CommandEnum.MORE, new MoreCommand());
        commands.put(CommandEnum.PRINT_ASCENDING, new PrintAscendingCommand());
        commands.put(CommandEnum.PSZH, new PSZHCommand());
        commands.put(CommandEnum.UPDATE, new UpdateCommand());
        commands.put(CommandEnum.UNDO, new UndoCommand());
        commands.put(CommandEnum.REDO, new RedoCommand());
        commands.put(CommandEnum.REMOVE, new RemoveCommand());
        commands.put(CommandEnum.REMOVE_GREATER, new RemoveGreaterCommand());
        commands.put(CommandEnum.UNIQUE_DISTANCE, new UniqueDistanceCommand());
        commands.put(CommandEnum.FILTER_NAME, new FilterNameCommand());
        commands.put(CommandEnum.HELP, new HelpCommand());
        commands.put(CommandEnum.EXIT, new ExitCommand() );
    }


    public static void executeCommand(CommandRequest cr) {
        logger.info("Executing {}", cr);

        Command c = commands.get(cr.getCommand());
        if (c == null) {
            logger.error("Couldn't fulfill request, command {} not found", cr.getCommand());
            return;
        }

        CommandArgs args = new CommandArgs( cr.getArgs(), cr.getRoute());
        try {
            c.execute(  cr   );
        } catch (NullPointerException npe) {
            OutputHandler.message("Bad argument!");
            logger.error("Could not execute request, bad argument!");
            return;
        }

        addToCommandHistory(c, args);
        if ( !(c instanceof UndoCommand || c instanceof RedoCommand) ) {
            commandUndone.clear();
            routeUndone.clear();
        }

    }

    private static void addToCommandHistory(Command c, CommandArgs ca) {
        if ( !(c instanceof UndoCommand || c instanceof RedoCommand) && c instanceof Undoable) {
            commandHistory.push( new HistoryEntry(c, ca.args()));
        }
    }

    public static void addToRouteHistory(Route... r) {
        routeHistory.push(r);
    }

    public static void undo() {
        try {
            HistoryEntry h = commandHistory.pop();
            Command c = h.command();

            if (c instanceof Undoable) {
                commandUndone.push(h);
                routeUndone.push(routeHistory.pop());
                ((Undoable) c).undo(routeUndone.peek());
            }
            logger.trace("undone '{}'", c.getName());
            OutputHandler.message( "undone '%s'", c.getName() );
        } catch (EmptyStackException e) {
            logger.trace("undo called, nothing to undo");
            OutputHandler.message( "undo called, nothing to undo" );
        }
    }

    public static void redo() {
        try {
            HistoryEntry h = commandUndone.pop();
            Command c = h.command();

            if (c instanceof Undoable) {
                commandHistory.push(h);
                routeHistory.push(routeUndone.pop());
                ((Undoable) c).redo(routeHistory.peek());
            } else {
                //todo fix
                //c.execute(h.args());
            }
            logger.trace("redone '{}'", c.getName());
            OutputHandler.message( "redone '%s'", c.getName() );
        } catch (EmptyStackException e) {
            logger.trace("redo called, nothing to redo");
            OutputHandler.message("nothing to redo :(");
        }
    }

    public static Stack<HistoryEntry> getHistory() {
        return commandHistory;
    }

    /**
     * Returns all commands
     * @return the HashMap of command name-object pairs
     */
    public static HashMap<CommandEnum, Command> getCommands() {
        return commands;
    }
}