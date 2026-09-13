public class Body {
    String name;
    double mass;
    Location l;
    double radius;
    Velocity v;

    Body(String name, double mass, Location l, double radius, Velocity v) throws InvalidBodyException {
        if (mass <= 0) {
            throw new InvalidBodyException(name + " has invalid mass: " + mass + " (must be > 0)");
        }
        if (radius <= 0) {
            throw new InvalidBodyException(name + " has invalid radius: " + radius + " (must be > 0)");
        }
        this.name = name;
        this.mass = mass;
        this.l = l;
        this.radius = radius;
        this.v = v;
    }
}
