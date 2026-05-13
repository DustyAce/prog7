package handlers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import shared.elements.Route;

import java.util.Arrays;
import java.util.HashSet;


/**
 * Class responsible for all changes in the collection
 */
public class CollectionHandler {
    private static final Logger logger = LogManager.getLogger("com.github.dustyace.lab6");
    static HashSet<Route> routes = new HashSet<>();
    static java.time.LocalDateTime initTime = java.time.LocalDateTime.now();

    /**
     * Removes all shared.elements from the collection.
     */
    public static Route[] clear() {
        Route[] r = routes.toArray(new Route[0]);
        routes.clear();
        OutputHandler.message("Collection cleared.");
        logger.trace("Collecton cleared");
        return r;
    }

    public static Route[] clear(HashSet<Long> idsToRemove) {
        HashSet<Route> removed = new HashSet<>();
        for (long id: idsToRemove) {
            removed.add(remove_by_id(id));
        }
        Route[] r = removed.toArray(new Route[0]);
        OutputHandler.message("Collection cleared, removed %s routes.\n", r.length);
        logger.trace("Removed {} routes.", r.length);
        return r;
    }

    public static boolean isIdValid(Long id) {
        return routes.stream()
                .anyMatch( route -> route.getId().equals(id) );
    }

    /**
     * Removes a {@code Route} with the specified id from the collection.
     * @param id id of the object to be removed, if present
     */
    public static Route remove_by_id(Long id) {
        Route r = find_by_id(id);
        if (r != null) {
            routes.remove(r);
            OutputHandler.message("Removed route %s", r);
            logger.trace( "Removed {}", r );
        } else { logger.trace("No route removed"); }
        return r;
    }

    public static Route[] update_id(Long id, Route r_new_val) {
        Route r_old = find_by_id(id);
        if (r_old == null) {
            logger.trace("No route updated");
            return null;
        }
        Route rc = r_old.clone();
        routes.remove(r_old);
        r_old.update(r_new_val);
        routes.add(r_old);
        logger.trace( "Route {} updated", r_old );
        return new Route[]{rc, r_old.update(r_new_val)};
    }

    /**
     * Adds a new {@code Route} to the collection
     * if it's less than all other present shared.elements.
     * @param newRoute Route object to attempt to add.
     */
    public static boolean add_if_min(Route newRoute) {
        for (Route r: routes) {
            if (newRoute.compareTo(r) > 0) {
                logger.trace("Route {} wasn't added, not min", newRoute);
                OutputHandler.message("Route wasn't added, isn't min.");
                return false;
            }
        }
        add(newRoute);
        OutputHandler.message("Route added!");
        return true;
    }

    public static void add(Route... newRoutes) {
        for (Route r: newRoutes) {
            routes.add(r);
            logger.trace("Route {} added", r);
            OutputHandler.message("Added %s", r);
        } //can't use stream api cause maxid needs to change during iteration
    }

    public static void add(Route[] r, boolean updateId) {
        if (updateId) { add(r); }
        else { routes.addAll( Arrays.stream(r).toList() ); }
    }

    /**
     * Prints a set of all {@code Route.distance} values
     */
    public static void print_unique_distance() {
        HashSet<Long> unqDist = new HashSet<>();
        for (Route r: routes) {
            unqDist.add(r.getDistance());
        }
        OutputHandler.message("Уникальные значения поля Distance:\n%s", unqDist);
    }

    /**
     * Removes all Routes greater than specified Route.
     * @param newRoute Route to compare other objects with
     */
    public static Route[] remove_greater(Route newRoute) {
        HashSet<Route> toRemove = new HashSet<>();
        for (Route r: routes) {
            if (newRoute.compareTo(r) < 0) {
                toRemove.add(r);
            }
        }
        for (Route r: toRemove) {
            routes.remove(r);
            OutputHandler.message("Route '%s' removed.", r);
        }
        return toRemove.toArray(new Route[0]);
    }

    /**
     * Prints a more detailed String representation of Route object with specified id,
     * if such exists.
     * @param id id of Route to be printed
     */
    public static void more(long id) {
        Route r = find_by_id(id);
        if (r != null) {OutputHandler.message(r.more());}
    }

    /**
     * Find a route with a specified id, if such exists.
     * @param id id of Route to attempt to find
     * @return Route with specified id or {@code null}, if such doesn't exist
     */
    private static Route find_by_id(Long id) {
        for (Route r: routes) {
            if (r.getId().equals(id)) {
                return r;
            }
        }
        OutputHandler.message("No Route with id '%s' found.", id);
        logger.trace("No Route with id '{}' found.", id);
        return null;
    }

    /**
     * Prints info about the collection.
     */
    public static void info() {
        OutputHandler.message(" Collection type: %s\n" +
                          " Current size: %s\n" +
                          " Initialization time: %s\n" +
                          " Coolness: 100", routes.getClass(), routes.size(), initTime);
    }

    /**
     * @return the collection
     */
    public static HashSet<Route> getRoutes() {
        return routes;
    }

    /**
     * Replace the collection with a new one.
     * @param r new collection
     */
    public static void setRoutes(HashSet<Route> r) {
        routes = r;
    }
}
