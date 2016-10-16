package at.fractal.fractalapp.data;

/**
 * This class stores the position and rotation of an object in a 2d space.
 */
public class Transform2D
{

    // region variables

    private Vector2D position;
    private double rotation;

    // endregion

    // region constructors

    public Transform2D(Vector2D position, double rotation)
    {
        this.position = position;
        this.rotation = rotation;
    }

    // endregion

    // region getters

    public Vector2D getPosition()
    {
        return position;
    }

    public double getRotation()
    {
        return rotation;
    }

    // endregion

    // region public methods

    @Override
    public Transform2D clone()
    {
        return new Transform2D(position.clone(), rotation);
    }

    @Override
    public String toString()
    {
        return position.toString() + " rot : " + rotation;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Transform2D that = (Transform2D) o;

        if (Double.compare(that.rotation, rotation) != 0) return false;
        return !(position != null ? !position.equals(that.position) : that.position != null);

    }

    @Override
    public int hashCode()
    {
        int result;
        long temp;
        result = position != null ? position.hashCode() : 0;
        temp = Double.doubleToLongBits(rotation);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    // endregion
}
