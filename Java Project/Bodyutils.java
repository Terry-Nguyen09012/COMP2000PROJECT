import java.util.ArrayList;
import java.util.List;

public class Bodyutils {

    // Generic method: works for any subtype of Body (Planet, Star, Moon, Meteor...)
    // without needing a separate method per type.
    static <T extends Body> List<T> filterByType(List<Body> bodies, Class<T> type) {
        List<T> result = new ArrayList<>();
        for (Body b : bodies) {
            if (type.isInstance(b)) {
                result.add(type.cast(b));
            }
        }
        return result;
    }
}