package eclipse.gamecomponents.fire;

import eclipse.gamecomponents.path.VectorPath;

import java.util.List;

public interface FirePattern {
    // For each projectile to be fired at this moment, return a VectorPath associated with each one
    List<VectorPath> getProjectilePaths(long now);
}
