package at.fractal.fractalapp.fractal;

import android.graphics.Canvas;

public class SetStartTurtleInformation extends Command
{
    public TurtleInformation execute(Canvas c, TurtleInformation turtleInfo)
    {
        Command.newTurtleInformation = turtleInfo.clone();
        return Command.newTurtleInformation;
    }
}
