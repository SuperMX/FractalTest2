package at.fractal.fractalapp.function;

/**
 * A linear mathematical one dimensional function.
 */
public class LinearFunction extends Function
{

    // region private variables

    private double d;
    private double k;

    // endregion

    // region constructors

    public LinearFunction(double minValue, double maxValue, double k, double d)
    {
        super(minValue,maxValue);
        this.d = d;
        this.k = k;
    }

    // endregion

    // region methods

    protected double calc(double x)
    {
        return k * x + d;
    }

    // endregion
}
