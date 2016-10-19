package at.fractal.fractalapp.fractal;

import android.graphics.Canvas;

/**
 * This command turns the turtle to the left.
 */
public class TurnLeft extends Command
{

    // region variables

    private double angle;

    // endregion

    // region constructors

    /**
     * @param angle the angle the turtle will turn each time in degrees.
     */
    public TurnLeft(double angle)
    {
        this.angle = Math.toRadians(angle);
    }

    // endregion

    // region public methods

    @Override
    public void execute(Canvas c, TurtleInformation turtleInfo)
    {
        turtleInfo.getTransform().addRotation(angle);
    }

    @Override
    public String toString()
    {
        return "+";
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TurnLeft turnLeft = (TurnLeft) o;

        return Double.compare(turnLeft.angle, angle) == 0;

    }

    @Override
    public int hashCode()
    {
        long temp = Double.doubleToLongBits(angle);
        return (int) (temp ^ (temp >>> 32));
    }

    // endregion
}
