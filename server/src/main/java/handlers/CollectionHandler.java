package handlers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import shared.elements.Route;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * Class responsible for all changes in the collection
 */
public class CollectionHandler {
    private static final Logger logger = LogManager.getLogger("com.github.dustyace.lab6");
    static final Set<Route> routes = Collections.synchronizedSet(new HashSet<Route>());
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
        synchronized (routes) {
            for (long id: idsToRemove) {
                removed.add(removeById(id));
            }
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
    public static Route removeById(Long id) {
        Route r = find_by_id(id);
        if (r != null) {
            routes.remove(r);
            OutputHandler.message("Removed route %s", r);
            logger.trace( "Removed {}", r );
        } else { logger.trace("No route removed"); }
        return r;
    }

    public static void update(Route r) {
        Route old = find_by_id(r.getId());
        routes.remove(old);
        routes.add(r);
    }

    public static void add(Route... newRoutes) {
        synchronized (routes) {
            for (Route r: newRoutes) {
                routes.add(r);
                logger.trace("Route {} added", r);
                OutputHandler.message("Added %s", r);
            } //can't do stream api bc I want logging + output for each route
        }
    }

    public static void add(Route[] r, boolean updateId) {
        if (updateId) { add(r); }
        else { routes.addAll( Arrays.stream(r).toList() ); }
    }

    /**
     * Prints a set of all {@code Route.distance} values
     */
    public static void print_unique_distance() {
        HashSet<Long> unqDist;
        synchronized (routes) {
            unqDist = routes.stream()
                    .map(Route::getDistance)
                    .collect(Collectors.toCollection(HashSet::new));
        }
        for (Route r: routes) {
            unqDist.add(r.getDistance());
        }
        OutputHandler.message("Уникальные значения поля Distance:\n%s", unqDist);
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
        Route r;
        synchronized (routes) {
            r = routes.stream().filter(rt -> rt.getId()==id).findAny().orElse(null);
        }
        if (r==null) {
            OutputHandler.message("No Route with id '%s' found.", id);
            logger.trace("No Route with id '{}' found.", id);
        }
        return r;
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
    public static Set<Route> getRoutes() {
        return routes;
    }

    /**
     * Replace the collection with a new one.
     * @param r new collection
     */
    public static void setRoutes(HashSet<Route> r) {
        synchronized (routes) {
            routes.clear();
            routes.addAll(r);
        }
    }
}
