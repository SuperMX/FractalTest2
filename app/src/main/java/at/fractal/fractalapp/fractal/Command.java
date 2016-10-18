package at.fractal.fractalapp.fractal;

import android.graphics.Canvas;

import at.fractal.fractalapp.data.Transform2D;
import at.fractal.fractalapp.data.Vector2D;

/**
 * This is the base class for all other commands which can be executed by the turtle.
 */
public abstract class Command
{

    private static boolean turtleInfoBuffer0Active = true;
    private static TurtleInformation turtleInfoBuffer0 = new TurtleInformation(new Transform2D(new Vector2D(0,0),0),0);
    private static TurtleInformation turtleInfoBuffer1 = new TurtleInformation(new Transform2D(new Vector2D(0,0),0),0);

    // region public methods

    /**
     * adapt the parameters of the command to compensate for the zoom into the fractal
     * @param zoomFactor scales the size of the fractal by the specified value (>1!)
     */
    public void zoom(double zoomFactor){}

    /**
     * this method is only used by the ForwardDraw and ForwardMove classes.
     * it determines how many ForwardDraw/ForwardMove commands in the next generation are needed to replace one
     * ForwardDraw/ForwardMove command in the current generation.
     */
    public void shrink(){}

    /**
     * this method is only used by the ForwardDraw and ForwardMove classes to determine when
     * to stop creating new generations when a fractal is first created.
     * @return true if no further generations need to be created, false otherwise.
     */
    public boolean isAtMinimumLength() { return false; }

    /**
     * this method is only used by the ForwardDraw and ForwardMove classes to determine if the
     * next generation of the fractal is needed.
     * @return true if a new generation should be created, false otherwise.
     */
    public boolean isNextGenerationNeeded() { return false; }

    public TurtleInformation execute(Canvas c)
    {
        turtleInfoBuffer0 = executeSpecific(c, turtleInfoBuffer0);
        return turtleInfoBuffer0;
    }

    public TurtleInformation finish(Canvas c)
    {
        turtleInfoBuffer1 = executeSpecific(c, turtleInfoBuffer1);
        return turtleInfoBuffer1;
    }

    public static void initializeTurtleInfoBuffers(TurtleInformation turtleInfo)
    {
        turtleInfoBuffer0 = turtleInfo.clone();
        turtleInfoBuffer1 = turtleInfo.clone();
    }

    /**
     * this method executes this command and returns the new turtleInformation data of the turtle
     * after executing the command.
     * @param c a canvas object used by the ForwardDraw class to draw lines to the screen.
     * @param turtleInfo describes the current state of the turtle before executing this command.
     * @return the state of the turtle after executing this command.
     */
    public abstract TurtleInformation executeSpecific(Canvas c, TurtleInformation turtleInfo);

    // endregion

}
