package eclipse.gamecomponents.path;

public class Left implements VectorPath {
    public Vector getVector(double x, double y, long age) {
        return new Vector(-1, 0);
    }
}
