package shared.responses;

import shared.elements.Route;

import java.util.ArrayList;

public record CommandResponse(ArrayList<Route> routes, String output) implements Response{

}
