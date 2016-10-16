package at.fractal.fractalapp.fractal;

import at.fractal.fractalapp.data.Vector2D;
import at.fractal.fractalapp.gui.FractalView;

/**
 * Sierpinski carpet with some additional randomness
 */
public class Carpet extends Fractal
{
    private static String[] rules = new String[]{"F=0.30F+<1F-F-F->1G+>1F+F+F-<1F","F=0.5F+<2F-F-F->2G+>2F+F+F-<2F","F=0.2F+<4F-F-F->4G+>4F+F+F-<4F", "G=0.9GFG", "G=0.1GGG"};

    //private static String[] rules = new String[]{"F=0.9F+F-F-F-G+F+F+F-F","F=0.05F+F-F-F-G--fF","F=0.05F-F+F+F+G++fF","G=GFG","f=fff"};

    public Carpet(FractalView fractalPanel, Vector2D spawnPosition)
    {
        super(fractalPanel,rules,"C30F",new Vector2D(0,0.5),spawnPosition,1,90,3,0.025);
    }
}
