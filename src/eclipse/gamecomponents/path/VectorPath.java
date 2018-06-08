package eclipse.gamecomponents.path;

public interface VectorPath {
    // Get a vector that tells how an object should move based on these parameters: location and age
    Vector getVector(double x, double y, long age);
}

