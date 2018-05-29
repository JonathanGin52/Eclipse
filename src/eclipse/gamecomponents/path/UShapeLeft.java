package eclipse.gamecomponents.path;

public class UShapeLeft implements VectorPath {
    public Vector getVector(double x, double y, long age) {
        return new Vector(-1, -(age - 0.5 * 1e9) / (0.5 * 1e9));
    }
}
