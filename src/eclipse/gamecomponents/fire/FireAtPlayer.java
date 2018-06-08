package eclipse.gamecomponents.fire;

import eclipse.gamecomponents.Enemy;
import eclipse.gamecomponents.Player;
import eclipse.gamecomponents.path.GenericLine;
import eclipse.gamecomponents.path.VectorPath;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FireAtPlayer implements FirePattern {
    final static private Random random = new Random();

    private Player player;
    private Enemy enemy;
    private double margin;

    // Enemy isn't known yet
    public FireAtPlayer(Player player) {
        this.player = player;
    }

    public FireAtPlayer(Player player, Enemy enemy, double margin) {
        this.player = player;
        this.enemy = enemy;
        this.margin = margin;
    }

    @Override
    public List<VectorPath> getProjectilePaths(long now) {
        double theta = Math.atan2(enemy.getY() + enemy.getHeight() - player.getMidpointY(), enemy.getMidpointX() - player.getMidpointX());
        double newTheta = theta + 2 * margin * (random.nextDouble() - 0.5) * Math.PI / 180;

        double x = Math.cos(newTheta);
        double y = Math.sin(newTheta);

        List<VectorPath> projVectors = new ArrayList(1);
        projVectors.add(new GenericLine(0, 0, x, y));
        return projVectors;
    }

    public Player getPlayer() {
        return player;
    }

    public double getMargin() {
        return margin;
    }
}
