// A Boomerang is only fired by the player

package eclipse.gamecomponents;

import eclipse.gamecomponents.path.Up;
import eclipse.gamecomponents.path.Vector;
import eclipse.gamecomponents.path.VectorPath;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.List;

public class Boomerang extends Projectile {

    private final static Image image = new Image(IMAGE_DIR + "boomerang.png");
    Player player;
    GameObject target;
    List<Enemy> enemies;
    List<Enemy> seen = new ArrayList();
    int bounces;
    boolean targeting = false;
    boolean remove = false; // Should the boomerang be removed on contact with the player?

    public Boomerang(double xPos, double yPos, int speed, boolean enemyProj, Player player, List<Enemy> enemies, int bounces) {
        super(xPos, yPos, 40, 40, speed, image, new Up(), enemyProj);
        this.player = player;
        this.enemies = enemies;
        this.bounces = bounces;
    }

    @Override
    public void update(long now) {
        if (!targeting) { // Act like a normal forward shooting projectile
            // If time's up, start going to the player
            if (now - startTime > 500000000L) {
                setPlayerTarget();
            }

            super.update(now);
        } else {
            if (target == null) {
                setPlayerTarget();
            }

            if (target instanceof Enemy) {
                if (((Enemy) target).isAlive() == false) {
                    System.out.println("BIGGIE");
                    target = findClosestEnemy();
                }
            }

            setVector(new VectorPath() {
                @Override
                public Vector getVector(double xPos, double yPos, long age) {
                    return new Vector(target.xPos - xPos, target.yPos - yPos);
                }
            });

            super.update(now);
        }

        double age = (double) (now - startTime) / 1000000000;
        SPRITE.setRotate(45);
    }


    // What happens when a Boomerang hits an enemy
    public void setHitEnemy(Enemy hit) {
        for (Enemy previous : seen) {
            if (previous == hit) return;
        }

        seen.add(hit);

        targeting = true;
        bounces--;

        if (bounces < 0) {
            setPlayerTarget();
        } else { // Find a new target
            target = findClosestEnemy();
        }
    }

    private void setPlayerTarget() {
        targeting = true;
        remove = true;
        target = player;
    }

    // Find the enemy closest to the boomerang
    private Enemy findClosestEnemy() {
        Enemy closest = null;
        double minDistance = 200 * 200; // Targeting radius

        for (Enemy e : enemies) {
            if (seen.contains(e)) continue;

            double distance = Math.pow(xPos - e.xPos, 2) + Math.pow(yPos - e.yPos, 2);

            if (distance < minDistance) {
                minDistance = distance;
                closest = e;
            }
        }

        if (closest != null) {
            System.out.println(target);
            Thread.dumpStack();
            target = closest;
        }

        return closest;
    }

    public boolean getRemove() {
        return remove;
    }
}
