package at.fractal.fractalapp.data;

/**
 * This class represents a 2D Vector and covers various Vector operations
 * such as addition, subtraction, multiplication with a scalar, creating a normal vector and more.
 */
public class Vector2D
{

    // region variables

    private double x;
    private double y;

    public static final Vector2D UP = new Vector2D(0,-1);
    public static final Vector2D DOWN = new Vector2D(0,1);
    public static final Vector2D LEFT = new Vector2D(0,-1);
    public static final Vector2D RIGHT = new Vector2D(0,1);

    private static Vector2D polarToCartesianVector = new Vector2D(0,0);

    // endregion

    // region constructors

    /**
     * Creates a new Vector2D with the specified direction and length.
     * @param x The x coordinate of the vector.
     * @param y The y coordinate of the vector.
     */
    public Vector2D(double x, double y)
    {
        this.x = x;
        this.y = y;
    }

    // endregion

    // region getters

    /**
     * @return The real length of the vector.
     */
    public double getMagnitude()
    {
        return Math.sqrt(x*x + y*y);
    }

    /**
     * @return The x coordinate of the vector.
     */
    public double getX()
    {
        return x;
    }

    /**
     * @return The y coordinate of the vector.
     */
    public double getY()
    {
        return y;
    }

    // endregion

    // region public methods

    /**
     * Changes the length (magnitude) of this vector to 1. The angle is not influenced and stays exactly the same.
     * @return The normalized vector.
     */
    public Vector2D normalize()
    {
        double factor = 1d / getMagnitude();
        this.x *= factor;
        this.y *= factor;
        return this;
    }

    /**
     * Scales this vector by the specified factor.
     * @param scalar the scaling factor
     */
    public void scale(double scalar)
    {
        x *= scalar;
        y *= scalar;
    }

    /**
     * adds a vector to this vector
     * @param v the vector to be added
     */
    public void add(Vector2D v)
    {
        x += v.x;
        y += v.y;
    }

    // endregion

    // region public static methods

    /**
     * @param v The vector for which the normal vector should be returned.
     * @return The normal vector of v.
     */
    public static Vector2D getNormalVector(Vector2D v)
    {
        return new Vector2D(-v.y, v.x);
    }

    /**
     * @param v The vector to be scaled.
     * @param scalar The factor which v should be scaled with.
     * @return The scaled vector.
     */
    public static Vector2D scale(Vector2D v, double scalar)
    {
        return new Vector2D(scalar * v.x, scalar * v.y);
    }

    /**
     * @param v1 The first vector.
     * @param v2 The second vector which is added to the first vector.
     * @return The result of the addition.
     */
    public static Vector2D add(Vector2D v1, Vector2D v2)
    {
        return new Vector2D(v1.x + v2.x, v1.y + v2.y);
    }

    /**
     * @param v1 The first vector.
     * @param v2 The second vector which is subtracted from the first vector.
     * @return The result of the subtraction.
     */
    public static Vector2D sub(Vector2D v1, Vector2D v2)
    {
        return new Vector2D(v1.x - v2.x, v1.y - v2.y);
    }

    /**
     * calculates the dot product of two vectors.
     * @param v1 the first vector.
     * @param v2 the second vector.
     * @return the dot product.
     */
    public static double dotProduct(Vector2D v1, Vector2D v2)
    {
        return v1.getX() * v2.getX() + v1.getY() + v2.getY();
    }

    /**
     * creates a vector out of the specified magnitude and phase
     * @param magnitude the length of the vector
     * @param angle the diretion of the vector
     * @return the created vector object
     */
    public static Vector2D polarToCartesian(double magnitude, double angle)
    {
        polarToCartesianVector.x = magnitude * Math.cos(angle);
        polarToCartesianVector.y = -magnitude * Math.sin(angle);
        return polarToCartesianVector;
    }

    @Override
    public Vector2D clone()
    {
        return new Vector2D(this.x,this.y);
    }


    @Override
    public String toString()
    {
        return "x: " + x + " y: " + y;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vector2D vector2D = (Vector2D) o;

        if (Double.compare(vector2D.x, x) != 0) return false;
        return Double.compare(vector2D.y, y) == 0;

    }

    @Override
    public int hashCode()
    {
        int result;
        long temp;
        temp = Double.doubleToLongBits(x);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(y);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

// endregion
}
