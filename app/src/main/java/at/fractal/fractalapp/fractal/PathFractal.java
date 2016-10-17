package at.fractal.fractalapp.fractal;

import at.fractal.fractalapp.data.Vector2D;
import at.fractal.fractalapp.gui.FractalView;

/**
 * Created by Sebastian on 17.10.2016.
 */

public class PathFractal extends Fractal
{
    private static String[] rules = new String[]{"F=FF", "f=ff", "G=0.5FF-X-F-H-f-f>1G", "G=0.5FF-Z-F-H-f-f>1G", "H=0.5+>1I-<1I-Z-F--fF", "H=0.5+>1J-<1J-Z-F--fF", "I=0.2F+F->1I-<1F-Z--f", "I=0.8F+F->1J-<1J-F--f", "J=0.2+F->1J-<1F-Z--fF", "J=0.8+I->1I-<1F-F--fF", "Z=0.25F-<1Z++>1f-F", "Z=0.25F+<1Z-->1f+F", "Z=0.4F+<1Z--fZ-->1f-F", "Z=0.1FF", "X=F-Y++f-F", "Y=F+>2Y--f+<2F-<2Y--f->2"};

    // "H=-I+J+F+F++fF", "I=F-F+I+J+F++f", "J=F-I+J+F+F++f", "G=L0,1,-0.02,1FF+F+F+H+f+fG", "G=L0,1,0.02,0FF-F-F-K-f-fG"
    // left snail: "G=L0,1,+0.02,0FF+Z+F+M+f+fG", "M=0.5-N+N+Z+F++fF", "M=0.5-O+O+Z+F++fF", "N=0.2F-F+N+F+Z++f", "N=0.8F-F+O+O+F++f", "O=0.2-F+O+F+Z++fF", "O=0.8-N+N+F+F++fF
    public PathFractal(FractalView fractalView, Vector2D spawnPosition)
    {
        super(fractalView,rules,"C30G",new Vector2D(0,0.5),spawnPosition,1,90,2,0.030);
    }
}
