// A Boomerang is only fired by the player

package eclipse.gamecomponents;

import eclipse.gamecomponents.path.Up;
import eclipse.gamecomponents.path.Vector;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.List;

public class Boomerang extends Projectile {

    private final static long FRAME_RATE = 100000000L;
    private static Image[] images;

    static {
        images = new Image[4];
        for (int i = 0; i < 4; i++) {
            images[i] = new Image(IMAGE_DIR + "/boomerang/boomerang" + i + ".png");
        }
    }

    private Player player;
    private GameObject target;
    private List<GameObject> gameObjects;
    private List<Enemy> enemies;
    private Enemy lastSeen;
    private int bounces;
    private boolean targeting = false;
    private boolean remove = false; // Should the boomerang be removed on contact with the player?
    private long lastUpdate = startTime;
    private int animationFrame = 0;

    public Boomerang(double xPos, double yPos, int speed, boolean enemyProj, Player player, List<GameObject> gameObjects, int bounces) {
        super(xPos - 15, yPos - 20, 30, 30, speed, images[0], new Up(), enemyProj);
        this.player = player;
        this.gameObjects = gameObjects; // copy reference

        enemies = new ArrayList();
        for (GameObject o : gameObjects) {
            if (o instanceof Enemy) {
                enemies.add((Enemy) o);
            }
        }

        this.bounces = bounces;
    }

    @Override
    public void update(long now) {
        // Find enemies
        enemies = new ArrayList<>();
        for (GameObject o : gameObjects) {
            if (o instanceof Enemy) {
                enemies.add((Enemy) o);
            }
        }

        if (!targeting) { // Act like a normal forward shooting projectile
            // If time's up, start going to the player
            if (now - startTime > 5000000000L / speed) {
                setPlayerTarget();
            }

            super.update(now);
        } else {
            if (target instanceof Enemy) {
                if (!((Enemy) target).isAlive()) {
                    target = findClosestEnemy();
                }
            }

            if (target == null) {
                setPlayerTarget();
            }

            setVector((xPos, yPos, age) -> new Vector(target.xPos - xPos, target.yPos - yPos));

            super.update(now);
        }

        if (now - lastUpdate > FRAME_RATE) {
            lastUpdate = now;
            animationFrame++;
            if (animationFrame == 4) animationFrame = 0;
        }

        setSprite();
        SPRITE.setRotate(0);
    }

    @Override
    public int getDamage() {
        return 1;
    }

    // What happens when a Boomerang hits an enemy
    public void setHitEnemy(Enemy hit) {
        if (lastSeen == hit) return;

        lastSeen = hit;

        targeting = true;
        bounces--;

        if (bounces < 0) {
            setPlayerTarget();
        } else { // Find a new target
            target = findClosestEnemy();
        }
    }

    public boolean getRemove() {
        return remove;
    }

    private void setPlayerTarget() {
        targeting = true;
        remove = true;
        target = player;
    }

    // Find the enemy closest to the boomerang
    private Enemy findClosestEnemy() {
        Enemy closest = null;
        double minDistance = 250 * 250; // Targeting radius

        for (Enemy e : enemies) {
            if (lastSeen == e) continue;

            double distance = Math.pow(xPos - e.xPos, 2) + Math.pow(yPos - e.yPos, 2);

            if (distance < minDistance) {
                minDistance = distance;
                closest = e;
            }
        }

        return closest;
    }

    private void setSprite() {
        SPRITE.setImage(images[animationFrame]);
    }
}
