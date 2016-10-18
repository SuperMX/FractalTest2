package at.fractal.fractalapp.fractal;

import at.fractal.fractalapp.data.Vector2D;
import at.fractal.fractalapp.gui.FractalView;

/**
 * A super fancy fresh fractal that i discovered myself with a lot of randomness :).
 */


public class Watch extends Fractal
{
    private static String[] rules = new String[]{"F=L0.1,0.5,-0.03,0.5F+<1F--F-->1F++>1F++F-<1F","F=L0.1,0.5,0.03,0.1F+<3F--F-->3F++>3F++F-<3F","F=0.25F+<2F--F-->2F++>2F++F-<2F",
            "F=0.01F+<1F--F+>1F","F=0.01F->1F++F-<1F","F=0.01F+<1F--F-->1G+++fF","F=0.01F->1F++F++<1G---fF",
            "F=0.11F+<1F--F-->1G++>1F++F-<1F","G=L0,1,0.03,0.4GFG","G=L0,1,-0.03,0.6GGG", "f=fff"};

    public Watch(FractalView fractalPanel, Vector2D spawnPosition)
    {
        super(fractalPanel,rules,"C30F",new Vector2D(0,0.5),spawnPosition,1,60,3,0.1);
    }

}
