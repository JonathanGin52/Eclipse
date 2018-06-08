package eclipse.gamecomponents.path;

public class UpRight implements VectorPath {
    @Override
    public Vector getVector(double x, double y, long age) {
        return new Vector(450, -1200);
    }
}
