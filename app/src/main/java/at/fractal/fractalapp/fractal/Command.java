package at.fractal.fractalapp.fractal;

import android.graphics.Canvas;

/**
 * This is the base class for all other commands which can be executed by the turtle.
 */
public abstract class Command
{

    // region public methods

    /**
     * adapt the parameters of the command to compensate for the zoom into the fractal
     * @param zoomFactor scales the size of the fractal by the specified value (>1!)
     */
    public void zoom(double zoomFactor){};

    /**
     * this method is only used by the ForwardDraw and ForwardMove classes.
     * it determines how many ForwardDraw/ForwardMove commands in the next generation are needed to replace one
     * ForwardDraw/ForwardMove command in the current generation.
     */
    public void shrink(){};

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

    /**
     * this method executes this command and returns the new turtleInformation data of the turtle
     * after executing the command.
     * @param g a graphics object used by the ForwardDraw class to drawAndCutOff lines to the screen.
     * @param turtleInfo describes the current state of the turtle before executing this command.
     * @return the state of the turtle after executing this command.
     */
    public abstract TurtleInformation execute(Canvas c, TurtleInformation turtleInfo);

    // endregion

}