package at.fractal.fractalapp.fractal;

import android.graphics.Canvas;

/**
 * This command turns the turtle to the left.
 */
public class TurnRight extends Command
{

    // region variables

    private double angle;

    // endregion

    // region constructors

    /**
     * @param angle the angle the turtle will turn each time in degrees.
     */
    public TurnRight(double angle)
    {
        this.angle = Math.toRadians(angle);
    }

    // endregion

    // region public methods

    @Override
    public TurtleInformation executeSpecific(Canvas c, TurtleInformation turtleInfo)
    {
        turtleInfo.getTransform().addRotation(-angle);
        return turtleInfo;
    }

    @Override
    public String toString()
    {
        return "-";
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TurnRight turnRight = (TurnRight) o;

        return Double.compare(turnRight.angle, angle) == 0;

    }

    @Override
    public int hashCode()
    {
        long temp = Double.doubleToLongBits(angle);
        return (int) (temp ^ (temp >>> 32));
    }

    // endregion
}
