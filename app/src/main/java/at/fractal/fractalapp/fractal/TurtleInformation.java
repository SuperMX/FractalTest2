package at.fractal.fractalapp.fractal;


import at.fractal.fractalapp.data.Transform2D;

/**
 * This class stores all data which describes the current state of the turtle used to drawAndCutOff a fractal.
 */
public class TurtleInformation
{

    // region variables

    private Transform2D transform;
    // which color the turtle currently draws in
    private int colorNumber;

    // endregion

    // region constructors

    public TurtleInformation (Transform2D transform, int colorNumber)
    {
        this.transform = transform;
        this.colorNumber = colorNumber;
    }

    // endregion

    // region getters

    public Transform2D getTransform()
    {
        return transform;
    }

    public int getColorNumber()
    {
        return colorNumber;
    }

    // endregion

    // region public methods

    @Override
    public TurtleInformation clone()
    {
        return new TurtleInformation(transform.clone(), colorNumber);
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TurtleInformation that = (TurtleInformation) o;

        if (colorNumber != that.colorNumber) return false;
        return !(transform != null ? !transform.equals(that.transform) : that.transform != null);

    }

    @Override
    public int hashCode()
    {
        int result = transform != null ? transform.hashCode() : 0;
        result = 31 * result + colorNumber;
        return result;
    }

    // endregion
}
