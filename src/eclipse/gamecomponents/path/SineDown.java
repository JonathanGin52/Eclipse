package eclipse.gamecomponents.path;

public class SineDown implements VectorPath {
    public Vector getVector(double x, double y, long age) {
        return new Vector(5 * Math.sin(3 * age / 1e9),2);
    }
}
