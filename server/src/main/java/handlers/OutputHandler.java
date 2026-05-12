package handlers;

import shared.elements.Route;

import java.util.ArrayList;

public class OutputHandler {
    private static String messageBuffer = "";
    private static ArrayList<Route> routeBuffer = new ArrayList<>();

    public static void message(String text) {
        messageBuffer = messageBuffer.concat( text ).concat("\n");
    }

    public static void message(Object o) {
        message( o.toString() );
    }

    public static void message(String text, Object... args) {
        message( String.format(text, args) );
    }



    public static String getMessage() {
        String ret = messageBuffer.toString();
        messageBuffer = "";
        return ret;
    }

    public static void setRoutes(ArrayList<Route> r) {
        routeBuffer.clear();
        routeBuffer.addAll(r);
    }

    public static ArrayList<Route> getRoutes() {
        ArrayList<Route> ret = routeBuffer;
        routeBuffer = new ArrayList<>();
        return ret;
    }
}
