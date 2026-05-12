package communication;

import java.util.HashSet;
import java.util.List;

public class LocalCollectionHandler {
    static HashSet<Long> existingRoutes = new HashSet<>();

    public static void updateExistingRoutes(List<Long> al) {
        existingRoutes.clear();
        existingRoutes.addAll(al);
    }

    public static boolean routeExists(Long id) {
        return existingRoutes.contains(id);
    }
}
