import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class SimulationPanel extends JPanel {
    private List<Body> bodies;
    private double scale = 0.2;   // pixels per distance-unit — shrink if planets go off screen
    private int substeps = 100;   // physics updates per animation frame
    private double dt = 3000;     // time per SUBSTEP (not per frame)

    public SimulationPanel(List<Body> bodies) {
        this.bodies = bodies;
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(900, 900));

        Timer timer = new Timer(16, e -> { // ~60 fps
            step();
            repaint();
        });
        timer.start();

        // Click to drop a meteor at that spot in simulation space.
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int cx = getWidth() / 2;
                int cy = getHeight() / 2;
                double simX = (e.getX() - cx) / scale;
                double simY = (e.getY() - cy) / scale;

                try {
                    Meteor meteor = new Meteor(
                            "Meteor",
                            0.5,                       // small mass — barely perturbs planets
                            new Location(simX, simY),
                            2,                         // pixel radius for drawing/collision
                            new Velocity(0, 0)         // starts still, gravity takes over
                    );
                    bodies.add(meteor);
                } catch (InvalidBodyException ex) {
                    System.err.println("Could not create meteor: " + ex.getMessage());
                }
            }
        });
    }

    private void step() {
        for (int s = 0; s < substeps; s++) {
            List<Velocity> accelerations = new ArrayList<>();
            for (Body b : bodies) {
                double ax = 0, ay = 0;
                for (Body other : bodies) {
                    if (other == b) continue;
                    try {
                        Velocity a = Physics.calculateGravity(b, other);
                        ax += a.vx;
                        ay += a.vy;
                    } catch (CollisionException e) {
                        System.out.println("Collision detected: " + e.getMessage());
                    }
                }
                accelerations.add(new Velocity(ax, ay));
            }
            for (int i = 0; i < bodies.size(); i++) {
                Physics.updateBody(bodies.get(i), accelerations.get(i), dt);
            }
        }
        checkMeteorCollisions();
    }

    // Checks every meteor against every planet; on overlap, the planet
    // changes color (via collide()) and the meteor is removed.
    private void checkMeteorCollisions() {
        List<Body> toRemove = new ArrayList<>();

        for (Body b : bodies) {
            if (!(b instanceof Meteor)) continue;
            Meteor meteor = (Meteor) b;

            for (Body other : bodies) {
                if (!(other instanceof Planet)) continue;
                Planet planet = (Planet) other;

                double dx = meteor.l.x - planet.l.x;
                double dy = meteor.l.y - planet.l.y;
                double distance = Math.sqrt(dx * dx + dy * dy);

                // radii are in pixel units (used directly for drawing),
                // so convert the collision threshold back into simulation units
                double collisionDistance = (meteor.radius + planet.radius) / scale;

                if (distance < collisionDistance) {
                    planet.collide();
                    toRemove.add(meteor);
                }
            }
        }

        bodies.removeAll(toRemove);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int cx = getWidth() / 2;
        int cy = getHeight() / 2;

        for (Body b : bodies) {
            int screenX = cx + (int) (b.l.x * scale);
            int screenY = cy + (int) (b.l.y * scale);
            int r = Math.max(2, (int) b.radius);

            if (b instanceof Star) {
                g.setColor(Color.YELLOW);
            } else if (b instanceof Planet) {
                g.setColor(((Planet) b).color);
            } else if (b instanceof Meteor) {
                g.setColor(Color.LIGHT_GRAY);
            } else {
                g.setColor(Color.WHITE);
            }

            g.fillOval(screenX - r, screenY - r, r * 2, r * 2);
        }
    }
}