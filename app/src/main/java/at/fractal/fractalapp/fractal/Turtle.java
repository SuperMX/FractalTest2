package at.fractal.fractalapp.fractal;

import android.graphics.Canvas;
import android.graphics.Color;

import java.util.*;
import java.util.List;

import at.fractal.fractalapp.data.Transform2D;
import at.fractal.fractalapp.data.Vector2D;
import at.fractal.fractalapp.gui.FractalView;

/**
 * This class is responsible for actually rendering the fractal to the display.
 * It uses a given LSystem and iterates over its commands using a turtle to follow some simple instructions.
 * Maybe i should have used a faster animal instead so that i get a better performance and no lag when rendering ;).
 */
public class Turtle
{

    // region variables

    private TurtleInformation startTurtleInfo;
    private double startAngle = Math.toRadians(90);
    private LSystem lSystem;
    private Fractal fractal;
    private FractalView fractalView;
    private TurtleInformation currentTurtleInfo;
    public static ArrayList<Integer> COLORS = new ArrayList<>();

    // endregion


    // region constructors

    // create a color palette where each colors fades into the next one in the list smoothly.
    // the last color of the list also fades into the second one.
    // The very first color (at index 0) is white.
    static
    {
        COLORS.add(Color.WHITE);
        int steps = 8;
        int stepSize = 256 / steps;
        int fullRed = 0x00FF0000;
        int fullGreen = 0x0000FF00;
        int fullBlue = 0x000000FF;
        int fullAlpha = 0xFF000000;
        int color = 0;

        for (int i = 0; i <= steps; i++)
        {
            int value = Math.min(255,i * stepSize);
            color = fullAlpha + fullRed + (value << 8) + 0;
            COLORS.add(color);
        }
        for (int i = 1; i <= steps; i++)
        {
            int value = Math.max(0,255 - i * stepSize);
            color = fullAlpha + (value << 16) + fullGreen + 0;
            COLORS.add(color);
        }
        for (int i = 0; i <= steps; i++)
        {
            int value = Math.min(255,i * stepSize);
            color = fullAlpha + 0 + fullGreen + value;
            COLORS.add(color);
        }
        for (int i = 1; i <= steps; i++)
        {
            int value = Math.max(0, 255 - i * stepSize);
            color = fullAlpha + 0 + (value << 8) + fullBlue;
            COLORS.add(color);
        }
        for (int i = 0; i <= steps; i++)
        {
            int value = Math.min(255,i * stepSize);
            color = fullAlpha + (value << 16)  + 0 + fullBlue;
            COLORS.add(color);
        }
        for (int i = 1; i < steps; i++)
        {
            int value = Math.max(0,255 - i * stepSize);
            color = fullAlpha + fullRed + 0 + value;
            COLORS.add(color);
        }
        //System.out.println("done");
    }

    /**
     * @param fractal The fractal for which this turtle is used to cutOff.
     * @param startPosition The position relative to the middle of the fractal where the turtle starts drawing.
     *                      The turtle always starts with its face up. (in device coordinates)
     * @param lSystem the lSystem which commands the turtle follows
     * @param fractalView the panel on which the fractal is drawn
     */
    public Turtle(Fractal fractal, Vector2D startPosition, LSystem lSystem, FractalView fractalView)
    {
        this.lSystem = lSystem;
        this.lSystem.setTurtle(this);
        this.fractal = fractal;
        this.fractalView = fractalView;
        startTurtleInfo = new TurtleInformation(new Transform2D(startPosition.clone(), startAngle),0);
        while(!lSystem.nextGeneration());
    }

    // endregion

    // region public methods

    /**
     * scales the fractal up by the specified zoomFactor.
     * @param point which part of the fractal should be zoomed into.
     * @param zoomFactor a scaling factor (must be at least 1)
     */
    public boolean zoom(Vector2D point, double zoomFactor)
    {
        Vector2D pos = Vector2D.add(startTurtleInfo.getTransform().getPosition(), fractal.getPosition());
        Vector2D distance = Vector2D.sub(point, pos);
        // move start position of the turtle so that the point which is focused while zooming in stays at the same position.
        Vector2D offset = Vector2D.scale(distance, -1 * (zoomFactor - 1));
        Transform2D startTransform = new Transform2D(Vector2D.add(startTurtleInfo.getTransform().getPosition(), offset),startTurtleInfo.getTransform().getRotation());
        startTurtleInfo = new TurtleInformation(startTransform,startTurtleInfo.getColorNumber());
        return lSystem.zoom(zoomFactor);
    }

    /**
     * @param turtleInformation the data currently describing the turtle
     * @return true if the turtle is currently out of the frame, false otherwise.
     */
    private boolean isOutOfBounds(TurtleInformation turtleInformation)
    {
        Vector2D position = turtleInformation.getTransform().getPosition();

        if (position.getX() < -fractalView.getCutOffLimitX() || position.getX() > fractalView.getCutOffLimitX() || position.getY() < -fractalView.getCutOffLimitY() || position.getY() > fractalView.getCutOffLimitY())
            return true;
        else
            return false;
    }

    /**
     * cuts off all parts of the fractal which are outside of the screen to avoid memory and rendering overload.
     * @return true if the fractal got lost and is not visible anywhere on the screen anymore, false otherwise.
     */
    public List<Command> cutOff()
    {
        ForwardDraw.DRAWING_ENABLED = false;
        //fractal.updatePosition();
        currentTurtleInfo = startTurtleInfo.clone();
        currentTurtleInfo.getTransform().getPosition().add(fractal.getPosition());
        TurtleInformation currentTurtleInfoCopy = currentTurtleInfo.clone();

        List<Command> commands = lSystem.getCurrent();
        List<Command> newCommand = new LinkedList<>();
        TurtleInformation newTurtleInfo;
        TurtleInformation cutOffStartTurtleInfo = currentTurtleInfo.clone();
        boolean outOfBounds = false;
        boolean cutOffStartSet = false;
        if (isOutOfBounds(currentTurtleInfo))
            outOfBounds = true;

        for (Command command : commands)
        {
            command.execute(null, currentTurtleInfoCopy);
            newTurtleInfo = currentTurtleInfoCopy;
            if (isOutOfBounds(newTurtleInfo))
            {
                if (!outOfBounds)
                    outOfBounds = true;
                else
                {
                    if (!cutOffStartSet)
                    {
                        cutOffStartTurtleInfo = currentTurtleInfo.clone();
                        cutOffStartSet = true;
                    }
                }
            }
            else
            {
                if (outOfBounds)
                {
                    outOfBounds = false;
                    if (cutOffStartSet)
                    {
                        cutOffStartSet = false;
                        // don't add any commands which take place when the turtle is out of screen to the new lsystem.
                        // instead when the turtle comes back into the screen add one command which "jumps" the turtle to the last position it was at before reentering the screen again.
                        Vector2D difference = Vector2D.sub(currentTurtleInfo.getTransform().getPosition(), cutOffStartTurtleInfo.getTransform().getPosition());
                        TurtleInformation delta = new TurtleInformation(new Transform2D(difference, currentTurtleInfo.getTransform().getRotation()),currentTurtleInfo.getColorNumber());
                        newCommand.add(new JumpCommand(delta));
                    }
                }
            }
            currentTurtleInfo.copyDataFromOtherTurtleInformation(newTurtleInfo);
            if (!cutOffStartSet)
            {
                newCommand.add(command);
            }
        }
        if (newCommand.size() > 0 && newCommand.get(0).getClass().equals(JumpCommand.class))
        {
            // if the very first command the turtle executes is a jump command delete it and place the turtle's
            // start position where the jump command leads to instead. this avoids that the start position
            // drifts away exponentially when zooming into the fractal until it eventually grows so large that it overflows a double value.
            // YES - this happened ;).
            JumpCommand jumpCommand = (JumpCommand)newCommand.get(0);
            jumpCommand.execute(null, startTurtleInfo);

            newCommand.remove(0);
        }
        ForwardDraw.DRAWING_ENABLED = true;
        return newCommand;
    }

    public boolean draw(Canvas c)
    {
        //fractal.updatePosition();
        currentTurtleInfo = startTurtleInfo.clone();
        currentTurtleInfo.getTransform().getPosition().add(fractal.getPosition());

        List<Command> commands = lSystem.getCurrent();
        for (Command command : commands)
        {
            command.execute(c, currentTurtleInfo);
        }
        return !(commands.size() > 0);
    }

}
