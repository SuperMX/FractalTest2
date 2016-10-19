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

    public LinearFunction(double k, double d)
    {
        this.d = d;
        this.k = k;
    }

    public LinearFunction(double incrementStep, double incrementStart, double k, double d)
    {
        super(incrementStep, incrementStart);
        this.d = d;
        this.k = k;
    }

    // endregion

    // region methods

    @Override
    public double calculate(double x)
    {
        return k * x + d;
    }

    // endregion
}
