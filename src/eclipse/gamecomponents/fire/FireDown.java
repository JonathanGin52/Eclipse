package eclipse.gamecomponents.fire;

import eclipse.gamecomponents.path.Down;
import eclipse.gamecomponents.path.VectorPath;

import java.util.ArrayList;
import java.util.List;

public class FireDown implements FirePattern {

    public List<VectorPath> getProjectilePaths(long now) {
        List<VectorPath> projVectors = new ArrayList(1);
        projVectors.add(new Down());
        return projVectors;
    }
}
