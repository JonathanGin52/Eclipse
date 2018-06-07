package eclipse.gamecomponents.fire;

import eclipse.gamecomponents.path.DownRight;
import eclipse.gamecomponents.path.VectorPath;

import java.util.ArrayList;
import java.util.List;

public class FireDownRight implements FirePattern {
    @Override
    public List<VectorPath> getProjectilePaths(long now) {
        List<VectorPath> projVectors = new ArrayList(1);
        projVectors.add(new DownRight());
        return projVectors;
    }
}