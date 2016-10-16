package at.fractal.fractalapp.fractal;

import at.fractal.fractalapp.data.Vector2D;
import at.fractal.fractalapp.gui.FractalView;

/**
 * A fractal consisting of infinitely many islands.
 */
public class Islands extends Fractal
{

    private static String[] rules = new String[]{"F=0.2F+f-<4FF+F+FF+Ff+>4FF-f+>4FF-F-FF-Ff-<4FFF",
            "F=L0.05,0.25,0.03,0.05F+f-<7FF+F+FF+Ff+>7FF-f+>7FF-F-FF-Ff-<7FFF",
            "F=L0.05,0.25,-0.03,0.25F+f-<1FF+F+FF+Ff+>1FF-f+>1FF-F-FF-Ff-<1FFF",
            "F=0.2F+f->4FF+F+FF+Ff+<4FF-f+<4FF-F-FF-Ff->4FFF",
            "F=L0.05,0.25,-0.02,0.25F+f->7FF+F+FF+Ff+<7FF-f+<7FF-F-FF-Ff->7FFF",
            "F=L0.05,0.25,0.02,0.05F+f->1FF+F+FF+Ff+<1FF-f+<1FF-F-FF-Ff->1FFF","f=ffffff"};

    public Islands(FractalView fractalPanel, Vector2D spawnPosition)
    {
        super(fractalPanel,rules,"C20F-F-F-F",new Vector2D(-0.5,0.5),spawnPosition,1,90,6,0.015);
    }
}
