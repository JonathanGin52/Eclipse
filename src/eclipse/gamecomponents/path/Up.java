package eclipse.gamecomponents.path;

public class Up implements VectorPath {
    public Vector getVector(double x, double y, long age) {
        return new Vector(0, -1);
    }
}
