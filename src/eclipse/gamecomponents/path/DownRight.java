package eclipse.gamecomponents.path;

public class DownRight implements VectorPath {
    public Vector getVector(double x, double y, long age) {
        return new Vector(450, 1200);
    }
}
