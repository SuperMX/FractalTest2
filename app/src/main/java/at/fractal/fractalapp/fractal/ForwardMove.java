package at.fractal.fractalapp.fractal;

import android.graphics.Canvas;

import at.fractal.fractalapp.data.Transform2D;
import at.fractal.fractalapp.data.Vector2D;

/**
 * This command moves the turtle on the screen without drawing a line where the turtle has moved.
 */
public class ForwardMove extends Command
{

    // region variables

    private double length;
    private double shrinkFactor;

    // endregion

    // region constructors

    /**
     *
     * @param shrinkFactor determines how many ForwardMove commands in the next generation are needed to replace one
     *                     ForwardMove command in the current generation.
     * @param length the distance the turtle moves when executing this command.
     */
    public ForwardMove(double shrinkFactor, double length)
    {
        this.shrinkFactor = shrinkFactor;
        this.length = length;
    }

    // endregion

    // region public methods

    public void shrink()
    {
        length /= shrinkFactor;
    }

    public void zoom(double zoomFactor)
    {
        length *= zoomFactor;
    }

    public TurtleInformation execute(Canvas c, TurtleInformation turtleInfo)
    {
        Transform2D transform = turtleInfo.getTransform();
        Vector2D delta = Vector2D.polarToCartesian(length, transform.getRotation());
        Command.newTurtleInformation.getTransform().getPosition().add(delta);
        return Command.newTurtleInformation;
    }

    @Override
    public String toString()
    {
        return "f";
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ForwardMove that = (ForwardMove) o;

        if (Double.compare(that.length, length) != 0) return false;
        return Double.compare(that.shrinkFactor, shrinkFactor) == 0;

    }

    @Override
    public int hashCode()
    {
        int result;
        long temp;
        temp = Double.doubleToLongBits(length);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(shrinkFactor);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    // endregion
}
