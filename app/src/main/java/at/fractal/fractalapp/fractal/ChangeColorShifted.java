package at.fractal.fractalapp.fractal;

import android.graphics.Canvas;

/**
 * This command changes the turtle's used color by going along the specified number of steps in the used color palette.
 */
public class ChangeColorShifted extends Command
{

    // region variables

    private int steps;

    // endregion

    // region constructors

    public ChangeColorShifted(int steps)
    {
        this.steps = steps;
    }

    // endregion

    // region public methods

    @Override
    public TurtleInformation executeSpecific(Canvas c, TurtleInformation turtleInfo)
    {
        int index = turtleInfo.getColorNumber();
        index += steps;
        if (index >= Turtle.COLORS.size())
        {
            index = index - Turtle.COLORS.size() + 1;
        }
        else if (index < 1)
        {
            index = index + Turtle.COLORS.size() - 1;
        }
        turtleInfo.setColorNumber(index);
        return turtleInfo;
    }

    @Override
    public String toString()
    {
        return ">" + steps;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ChangeColorShifted that = (ChangeColorShifted) o;

        return steps == that.steps;

    }

    @Override
    public int hashCode()
    {
        return steps;
    }

    // endregion
}
