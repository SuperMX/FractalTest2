package at.fractal.fractalapp.fractal;

import at.fractal.fractalapp.data.Vector2D;
import at.fractal.fractalapp.gui.FractalView;

/**
 * Sierpinski triangle
 */
public class Triangle extends Fractal
{
    private static String[] rules = new String[]{"F=L0,1,0.01,0F-<2G+F+G->2F", "F=L0,1,-0.01,1F->2G+F+G-<2F","G=GG"};

    public Triangle(FractalView fractalPanel, Vector2D spawnPosition)
    {
        super(fractalPanel,rules,"C40F-G-G",new Vector2D(-0.5,0.7),spawnPosition,1.4,120,2,0.2);
    }
}
