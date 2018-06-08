package eclipse.gamecomponents.path;

public class GenericLine implements VectorPath {
    double dx;
    double dy;

    // Create a randomized path
    public GenericLine(double fromX, double fromY, double toX, double toY) {
        dx = fromX - toX;
        dy = fromY - toY;
    }

    @Override
    public Vector getVector(double x, double y, long age) {
        return new Vector(dx, dy);
    }
}
