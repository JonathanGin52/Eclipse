package eclipse.gamecomponents;

import java.util.List;

public class EnemyWave {

    private List<Enemy> enemies;

    public EnemyWave(int enemyType, int size) {
        enemies = spawnEnemies(enemyType, size);
    }

    private List<Enemy> spawnEnemies(int enemyType, int size) {
        return null;
//        List<>
    }
}
