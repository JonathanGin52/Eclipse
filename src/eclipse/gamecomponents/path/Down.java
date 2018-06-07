package eclipse.gamecomponents.path;

public class Down implements VectorPath {
    @Override
    public Vector getVector(double x, double y, long age) {
        return new Vector(0, 1);
    }
}
