package userIO;

import Exceptions.BadIdException;
import communication.CommunicationHandler;
import shared.commands.CommandEnum;
import shared.requests.*;
import shared.elements.Route;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Class that handles all the input
 */
public class InputHandler {
    public static Scanner sc = new Scanner(System.in);
    private final static HashSet<CommandEnum> routeCommands = new HashSet<>();
    static {
        routeCommands.add(CommandEnum.ADD);
        routeCommands.add(CommandEnum.ADD_IF_MIN);
        routeCommands.add(CommandEnum.REMOVE_GREATER);
        routeCommands.add(CommandEnum.UPDATE);
    }

    private final static HashMap<CommandEnum, Supplier<Request>> idfk = new HashMap<>();
    static {
        idfk.put(CommandEnum.LOGIN, LoginRequest::new);
        idfk.put(CommandEnum.REGISTER, RegisterRequest::new);
    }


    public static Request requestInput() {
        System.out.print(">> ");

        String[] inp = sc.nextLine().strip().split(" ");

        if (inp.length == 0) {return null;}
        final CommandEnum command;
        try {
            command = CommandEnum.valueOf(inp[0].toUpperCase());
        } catch (IllegalArgumentException iae) { return null; }

        if (idfk.containsKey(command)) {
            return idfk.get(command).get();
        } else {
            CommandRequest cr = new CommandRequest(command);
            cr.setArgs(Arrays.copyOfRange( inp, 1, inp.length ) );
            if (command==CommandEnum.UPDATE) { checkRouteIdExists(cr); }
            if ( routeCommands.contains(cr.getCommand()) ) { cr.setRoute( new Route() ); }
            if (command==CommandEnum.UPDATE) { try {
                cr.getRoute().setId( Long.parseLong(cr.getArgs()[0]) );
            } catch (NumberFormatException ignored) {} }
            return cr;
        }

    }

    private static void checkRouteIdExists(CommandRequest ret) {
        try {
            Long id = Long.parseLong(ret.getArgs()[0]);
            CommunicationHandler.request( new CheckRouteExistsRequest(id));
            if ( !CheckRouteExistsRequest.getResult() ) {
                System.out.println( "Route does not exist." ); throw new BadIdException("No route with given id");
            }
        } catch (NumberFormatException e) { throw new BadIdException("Invalid id, could not parse to Long"); }
    }
    /**
     * Gives the user a [y/n] prompt. N is selected by default.
     * @param question String printed before the prompt
     * @return {@code true} if user input was 'y' or 'Y', {@code false} otherwise
     */
    public static boolean ynPrompt(String question) {
        System.out.print(question + " (y/N)\n>>> ");
        return sc.nextLine().strip().equalsIgnoreCase("y");
    }

    public static String stringInput(String varName) {
        return Input(varName, "String", null, s -> s);
    }

    public static Integer intInput(String varName, InputValidator<Integer> v) {
        return Input(varName, "int", v, Integer::parseInt);
    }

    public static Long longInput(String varName, InputValidator<Long> v) {
        return Input(varName, "long", v, Long::parseLong);
    }

    private static <T extends Comparable> T Input(String varName, String varType, InputValidator<T> v, Function<String, T> parser) {
        if (v == null) { v = new InputValidator<>(); }
        String req = v.request(varType, varName);
        T n = null;
        boolean goodInput = false;
        while (!goodInput) {
            System.out.print(req);
            String inp = sc.nextLine();
            try {
                if (!inp.isBlank()) {n = parser.apply(inp);}
                goodInput = v.validate(n);
            } catch (NumberFormatException e) { System.out.println("Bad input :("); }
        }
        return n;
    }

    public static Float floatInput(String varName, InputValidator<Float> v) {
        return Input(varName, "float", v, Float::parseFloat);
    }
    public static Float floatInput(String varName) {
        return Input(varName, "float", null, Float::parseFloat);
    }
}
