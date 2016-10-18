package at.fractal.fractalapp.fractal;

import android.graphics.Canvas;
import android.graphics.Paint;

import at.fractal.fractalapp.data.Transform2D;
import at.fractal.fractalapp.data.Vector2D;
import at.fractal.fractalapp.gui.FractalView;

/**
 * This command moves the turtle on the screen and draws a line where the turtle has moved.
 */
public class ForwardDraw extends Command
{

    // region variables

    // the length of the line which is drawn or the distance the turtle moves.
    private double length;
    private FractalView fractalView;
    private double shrinkFactor;
    private double nextGenerationLength;
    private String name;

    private Canvas c;
    private Paint p;

    // endregion

    // region constructors

    /**
     * @param shrinkFactor determines how many ForwardDraw commands in the next generation are needed to replace one
     *                     ForwardDraw command in the current generation.
     * @param length the length of the line which is drawn.
     * @param fractalView the panel the lines are drawn to
     * @param nextGenerationLength when the length of the line which is drawn by this command
     *                             gets smaller than this value a new generation should be created.
     * @param name the name for this specific drawAndCutOff command (typically F or G).
     */
    public ForwardDraw(double shrinkFactor, double length, FractalView fractalView, double nextGenerationLength, String name)
    {
        this.shrinkFactor = shrinkFactor;
        this.length = length;
        this.fractalView = fractalView;
        this.nextGenerationLength = nextGenerationLength;
        this.name = name;
    }

    // endregion

    // region getters

    public String getName()
    {
        return name;
    }

    public boolean isAtMinimumLength()
    {
        if (length < nextGenerationLength)
            return true;
        else
            return false;
    }

    public boolean isNextGenerationNeeded()
    {
        if (length > nextGenerationLength)
            return true;
        else
            return false;
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
        FractalView.PAINT.setColor(Turtle.COLORS.get(turtleInfo.getColorNumber()));
        drawLine(c, transform.getPosition(), Command.newTurtleInformation.getTransform().getPosition(), fractalView.getWidth(), fractalView.getHeight());
        return Command.newTurtleInformation;
    }

    @Override
    public String toString()
    {
        return name;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ForwardDraw that = (ForwardDraw) o;

        if (!name.equals(that.name)) return false;
        if (Double.compare(that.length, length) != 0) return false;
        if (Double.compare(that.shrinkFactor, shrinkFactor) != 0) return false;
        return Double.compare(that.nextGenerationLength, nextGenerationLength) == 0;

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
        temp = Double.doubleToLongBits(nextGenerationLength);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    // endregion

    // region private methods

    /**
     * Inspired by the design of OpenGL, device coordinates are in the range of [-1,1] for the smaller
     * value of width and height. the interval of the other (bigger) dimension is proportionally larger than the smaller one.
     * This method converts those device coordinates to pixel coordinates and actually draws a line from the start to the end position.
     * @param c the canvas object used to drawAndCutOff to the screen.
     * @param startPosition the start position for the line in device coordinates.
     * @param endPosition the end position for the line in device coordinates.
     * @param width the total width of the panel the fractal is drawn to.
     * @param height the total height of the panel the fractal is drawn to.
     */
    private void drawLine(Canvas c, Vector2D startPosition, Vector2D endPosition, int width, int height)
    {
        int screenSize = Math.min(width, height);
        int x1 = (int) Math.round((startPosition.getX() * screenSize / 2) + width / 2);
        int y1 = (int) Math.round((startPosition.getY() * screenSize / 2) + height / 2);

        int x2 = (int) Math.round((endPosition.getX() * screenSize / 2) + width / 2);
        int y2 = (int) Math.round((endPosition.getY() * screenSize / 2) + height / 2);
        c.drawLine(x1, y1, x2, y2, FractalView.PAINT /* For Porting Test Purposes */);
    }

    // endregion
}
