package shared;

import shared.elements.Route;

import java.util.ArrayList;

public record Response(ArrayList<Route> routes, String output) {
}
