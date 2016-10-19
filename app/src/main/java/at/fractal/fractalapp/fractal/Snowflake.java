package at.fractal.fractalapp.fractal;

import at.fractal.fractalapp.data.Vector2D;
import at.fractal.fractalapp.gui.FractalView;

/**
 * A randomized version of the Koch Snowflake fractal.
 */
public class Snowflake extends Fractal
{

    private static String[] rules = new String[]{"F=L0,1,-0.05,1F-<1F++F->1F","F=L0,1,0.05,0F+<2F--F+>2F","f=fff"};

    public Snowflake(FractalView fractalPanel, Vector2D spawnPosition)
    {
        super(fractalPanel,rules,"F--F--F",new Vector2D(-0.5,0.7),spawnPosition,1.4,60,3,0.04);
    }

    @Override
    public String toString()
    {
        return "snowflake";
    }
}


