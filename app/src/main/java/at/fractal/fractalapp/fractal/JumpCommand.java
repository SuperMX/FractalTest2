package at.fractal.fractalapp.fractal;

import android.graphics.Canvas;

import at.fractal.fractalapp.data.Transform2D;
import at.fractal.fractalapp.data.Vector2D;

/**
 * This command "jumps" the turtle to a completely different position, rotation, color,..
 * It is used to cut off other commands of the turtle which take place outside of the screen so that all redundant data
 * gets deleted.
 */
public class JumpCommand extends Command
{
    // region variables

    private TurtleInformation delta;

    // endregion

    // region constructors

    /**
     * @param delta the values the turtle has after the command is executed.
     *              however, the position is passed as a relative vector referring to the position
     *              the turtle was at before the jump. all other values are absolutes.
     */
    public JumpCommand (TurtleInformation delta)
    {
        this.delta = delta;
    }

    // endregion

    // region public methods

    public void shrink() {}

    public void zoom(double zoomFactor)
    {
        delta.getTransform().getPosition().scale(zoomFactor);
    }

    public TurtleInformation execute(Canvas c, TurtleInformation turtleInfo)
    {
        Vector2D newPosition = Vector2D.add(turtleInfo.getTransform().getPosition(), delta.getTransform().getPosition());
        Command.newTurtleInformation.getTransform().setPosition(newPosition);
        Command.newTurtleInformation.getTransform().setRotation(delta.getTransform().getRotation());
        Command.newTurtleInformation.setColorNumber(delta.getColorNumber());
        return Command.newTurtleInformation;
        //return new TurtleInformation(new Transform2D(newPosition, delta.getTransform().getRotation()), delta.getColorNumber());
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JumpCommand that = (JumpCommand) o;

        return !(delta != null ? !delta.equals(that.delta) : that.delta != null);

    }

    @Override
    public int hashCode()
    {
        return delta != null ? delta.hashCode() : 0;
    }

    // endregion
}
