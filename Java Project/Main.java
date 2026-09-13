import javax.swing.*;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            Star sun = new Star("Sun", 1000000, new Location(0, 0), 10, new Velocity(0, 0));

            List<Body> bodies = new ArrayList<>();
            bodies.add(sun);
            bodies.add(makePlanet("Mercury", 1.660, 138.7, 4, sun, Color.GRAY));
            bodies.add(makePlanet("Venus", 24.47, 272.3, 5, sun, Color.ORANGE));
            bodies.add(makePlanet("Earth", 30.03, 300, 5.4, sun, Color.BLUE));
            bodies.add(makePlanet("Mars", 3.227, 452.4, 4.4, sun, Color.RED));
            bodies.add(makePlanet("Jupiter", 954.6, 520.4, 12, sun, new Color(210, 180, 140))); // tan
            bodies.add(makePlanet("Saturn", 285.8, 957.4, 10, sun, new Color(230, 210, 150)));  // pale gold
            bodies.add(makePlanet("Uranus", 43.66, 1921, 8, sun, new Color(150, 220, 220)));   // pale teal
            bodies.add(makePlanet("Neptune", 51.51, 3002, 8, sun, new Color(70, 100, 220)));   // deep blue

            // Generics demo: pull out just the Planets from the mixed Body list,
            // with no casting needed and full type safety.
            List<Planet> planetsOnly = Bodyutils.filterByType(bodies, Planet.class);
            System.out.println("Planet count: " + planetsOnly.size());
            for (Planet p : planetsOnly) {
                System.out.println(" - " + p.name);
            }

            JFrame frame = new JFrame("Solar System Simulation");
            SimulationPanel panel = new SimulationPanel(bodies);
            frame.add(panel);
            frame.pack();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

        } catch (InvalidBodyException e) {
            System.err.println("Failed to create solar system: " + e.getMessage());
        }
    }

    // helper to build a planet with a correct circular orbit speed.
    // The planet is drawn cyan until hit by a meteor, then reveals trueColor.
    static Planet makePlanet(String name, double mass, double distance, double radius, Star sun, Color trueColor)
            throws InvalidBodyException {
        double speed = Physics.circularOrbitSpeed(sun.mass, distance);
        Planet p = new Planet(name, mass, new Location(distance, 0), radius, new Velocity(0, speed));
        p.trueColor = trueColor; // color is left as the default cyan
        return p;
    }
}