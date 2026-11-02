// @author Oly
package se233.arkanoid.render;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public final class ParticleEffects {
    private static final Logger LOGGER = LoggerFactory.getLogger(ParticleEffects.class);
    private final List<Particle> particles = new ArrayList<>();
    private final Random random = new Random();
    private double flashX;
    private double flashY;
    private double flashRemaining;

    public void spawnBrickHit(double x, double y) {
        // Demo: a non-lethal hit produces a short flash and a small set of sparks.
        flashX = x;
        flashY = y;
        flashRemaining = 0.12;
        for (int i = 0; i < 5; i++) addParticle(x, y, Color.rgb(235, 248, 255), 0.22, 52.0);
        LOGGER.debug("Spawned brick hit sparks at {}, {}", x, y);
    }

    public void spawnBrickDestroyed(double x, double y) {
        // Demo: destruction uses a larger, colored particle burst to distinguish it from damage.
        Color[] palette = {Color.rgb(94, 226, 220), Color.rgb(226, 183, 91), Color.rgb(255, 117, 117)};
        for (int i = 0; i < 20; i++) addParticle(x, y, palette[i % palette.length], 0.55, 110.0);
        LOGGER.debug("Spawned brick destruction burst at {}, {}", x, y);
    }

    public void update(double dt) {
        flashRemaining = Math.max(0.0, flashRemaining - dt);
        Iterator<Particle> iterator = particles.iterator();
        while (iterator.hasNext()) {
            Particle particle = iterator.next();
            particle.x += particle.vx * dt;
            particle.y += particle.vy * dt;
            particle.vy += 90.0 * dt;
            particle.remaining -= dt;
            if (particle.remaining <= 0.0) iterator.remove();
        }
    }

    public void render(GraphicsContext gc) {
        if (flashRemaining > 0.0) {
            gc.setGlobalAlpha(flashRemaining / 0.12);
            gc.setFill(Color.WHITE);
            gc.fillRoundRect(flashX - 37, flashY - 16, 74, 32, 6, 6);
            gc.setGlobalAlpha(1.0);
        }
        for (Particle particle : particles) {
            gc.setGlobalAlpha(Math.max(0.0, particle.remaining / particle.lifetime));
            gc.setFill(particle.color);
            gc.fillOval(particle.x - particle.size / 2.0, particle.y - particle.size / 2.0,
                    particle.size, particle.size);
        }
        gc.setGlobalAlpha(1.0);
    }

    private void addParticle(double x, double y, Color color, double lifetime, double speed) {
        double angle = random.nextDouble() * Math.PI * 2.0;
        double velocity = speed * (0.45 + random.nextDouble() * 0.55);
        particles.add(new Particle(x, y, Math.cos(angle) * velocity, Math.sin(angle) * velocity,
                lifetime * (0.7 + random.nextDouble() * 0.6), color, 2.0 + random.nextDouble() * 3.0));
    }

    private static final class Particle {
        private double x;
        private double y;
        private final double vx;
        private double vy;
        private double remaining;
        private final double lifetime;
        private final Color color;
        private final double size;
        private Particle(double x, double y, double vx, double vy, double lifetime, Color color, double size) {
            this.x = x; this.y = y; this.vx = vx; this.vy = vy; this.remaining = lifetime;
            this.lifetime = lifetime; this.color = color; this.size = size;
        }
    }
}
