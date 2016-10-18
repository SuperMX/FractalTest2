package at.fractal.fractalapp.fractal;

import android.graphics.Canvas;

/**
 * This command changes the turtle's used color to the specified color in the selection.
 */
public class ChangeColorDirectly extends Command
{

    // region variable

    private int colorNumber;

    // endregion

    // region constructors

    public ChangeColorDirectly(int colorNumber)
    {
        this.colorNumber = colorNumber;
    }

    // endregion

    // region public methods

    public TurtleInformation execute(Canvas c, TurtleInformation turtleInformation)
    {
        Command.newTurtleInformation.setColorNumber(colorNumber);
        return Command.newTurtleInformation;
        //return new TurtleInformation(turtleInformation.getTransform(), colorNumber);
    }

    @Override
    public String toString()
    {
        return "C" + colorNumber;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ChangeColorDirectly that = (ChangeColorDirectly) o;

        return colorNumber == that.colorNumber;

    }

    @Override
    public int hashCode()
    {
        return colorNumber;
    }

    // endregion
}
