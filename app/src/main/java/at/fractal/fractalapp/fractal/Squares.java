package at.fractal.fractalapp.fractal;


import at.fractal.fractalapp.data.Vector2D;
import at.fractal.fractalapp.gui.FractalView;

/**
 * Just a fractal based on some squares. (simply simple)
 */
public class Squares extends Fractal
{
    private static String[] rules = new String[]{"F=FF-F-F-F-fF","f=fff"};

    public Squares(FractalView fractalView, Vector2D spawnPosition)
    {
        super(fractalView,rules,"F-F-F-F",new Vector2D(-0.7,0.7),spawnPosition,1.4,90,3,0.1);
    }

    @Override
    public String toString()
    {
        return "squares";
    }
}
