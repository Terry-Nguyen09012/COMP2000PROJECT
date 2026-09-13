import java.awt.Color;

public class Planet extends Body {
    Color color = Color.CYAN;      // what's drawn — starts cyan for every planet
    Color trueColor = Color.CYAN;  // revealed once a meteor hits it

    Planet(String name, double mass, Location l, double radius, Velocity v) throws InvalidBodyException {
        super(name, mass, l, radius, v);
    }

    void collide() {
        System.out.println(name + " is Revitalised!");
        color = trueColor;
    }

    void stable() {
        System.out.print(name + " stable ");
    }
}