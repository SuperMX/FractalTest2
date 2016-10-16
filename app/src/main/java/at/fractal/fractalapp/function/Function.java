package at.fractal.fractalapp.function;

/**
 * A base class representing a one dimensional mathematical function
 */
public abstract class Function
{

    // region variables

    private double minValue;
    private double maxValue;

    // endregion

    // region constructors

    public Function (double minValue, double maxValue)
    {
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    // endregion

    // region public methods

    public double calculate(double x)
    {
        double y = calc(x);
        y = Math.max(minValue,y);
        y = Math.min(maxValue, y);
        return y;
    }

    protected abstract double calc(double x);

    // endregion
}
