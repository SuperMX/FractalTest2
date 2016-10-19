package at.fractal.fractalapp.function;

/**
 * A base class representing a one dimensional mathematical function
 */
public abstract class Function
{

    // region variables

    private double incrementStep = 1;
    private double x = 0;
    private double minValue = Integer.MIN_VALUE;
    private double maxValue = Integer.MAX_VALUE;

    // endregion

    // region constructors

    public Function () {}

    public Function (double incrementStep, double incrementStart)
    {
        this.incrementStep = incrementStep;
        this.x = incrementStart;
    }

    // endregion

    // region setters

    public void setIncrementStart(double incrementStart)
    {
        this.x = incrementStart;
    }

    public void setIncrementStep(double incrementStep) {this.incrementStep = incrementStep;}

    // endregion

    public void setMinMaxValue(double minValue, double maxValue)
    {
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    // region public methods

    public abstract double calculate(double x);

    public double limit(double value)
    {
        value = Math.max(minValue, value);
        value = Math.min(maxValue, value);
        return value;
    }

    public double calculateIncremented()
    {
        double y = calculate(x);
        x += incrementStep;
        return y;
    }

    // endregion
}
