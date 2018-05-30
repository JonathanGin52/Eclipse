package eclipse.gamecomponents.fire;

import eclipse.gamecomponents.path.VectorPath;

import java.util.List;

public interface FirePattern {
    // Return a vector path with path of projectile if a projectile is to be fired at time now
    // Returns a list because there may be multiple projectiles
    List<VectorPath> getProjectilePaths(long now);
}
