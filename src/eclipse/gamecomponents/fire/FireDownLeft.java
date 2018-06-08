package eclipse.gamecomponents.fire;

import eclipse.gamecomponents.path.DownLeft;
import eclipse.gamecomponents.path.VectorPath;

import java.util.ArrayList;
import java.util.List;

public class FireDownLeft implements FirePattern {
    @Override
    public List<VectorPath> getProjectilePaths(long now) {
        List<VectorPath> projVectors = new ArrayList<>(1);
        projVectors.add(new DownLeft());
        return projVectors;
    }
}