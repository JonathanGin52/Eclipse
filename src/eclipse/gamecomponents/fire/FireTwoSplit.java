package eclipse.gamecomponents.fire;

import eclipse.gamecomponents.path.DownLeft;
import eclipse.gamecomponents.path.DownRight;
import eclipse.gamecomponents.path.VectorPath;

import java.util.ArrayList;
import java.util.List;

public class FireTwoSplit implements FirePattern {
    @Override
    public List<VectorPath> getProjectilePaths(long now) {
        List<VectorPath> projVectors = new ArrayList<>(3);
        projVectors.add(new DownLeft());
        projVectors.add(new DownRight());
        return projVectors;
    }
}