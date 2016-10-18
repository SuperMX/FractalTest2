package at.fractal.fractalapp.gui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import at.fractal.fractalapp.data.Vector2D;
import at.fractal.fractalapp.fractal.Fractal;
import at.fractal.fractalapp.fractal.PathFractal;
import at.fractal.fractalapp.fractal.Snowflake;
import at.fractal.fractalapp.fractal.Triangle;

/**
 * Created by Markus on 12.10.2016.
 */


// GameView class will go here

// Here is our implementation of GameView
// It is an inner class.
// Note how the final closing curly brace }
// is inside SimpleGameEngine

// Notice we implement runnable so we have
// A thread and can override the run method.
public class FractalView extends SurfaceView implements Runnable
{

    private List<Fractal> fractalsToDraw = new ArrayList<>();
    private List<Fractal> fractals = new ArrayList<>();
    private Vector2D zoomPoint = new Vector2D(0.5,0.5);
    private int counter = 0;
    private double cutOffLimitX = 1, cutOffLimitY = 1;

    // This is our thread
    private Thread gameThread = null;
    private SurfaceHolder ourHolder;
    volatile boolean playing;

    // A Canvas and a Paint object
    private Canvas canvas;
    public static Paint PAINT;
    private long fps;
    private final long maxFps = 30;
    private long timeToRender;
    private long startFrameTime;
    private long endFrameTime;

    public FractalView(Context context)
    {
        super(context);

        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //if (event.getAction() == MotionEvent.ACTION_DOWN){}
                if (event.getAction() == MotionEvent.ACTION_MOVE)
                {
                    int ex = (int) event.getX();
                    int ey = (int) event.getY();

                    int screensize = Math.min(getWidth(),getHeight());
                    double x = ((double) ex - (double) getWidth() / 2) * 2 / (double) screensize;
                    double y = ((double) ey - (double) getHeight() / 2) * 2 / (double) screensize;
                    zoomPoint = new Vector2D(x, y);
                }
                return true;
            }
        });

        calculateCutOffLimit();
        Vector2D spawnPosition = new Vector2D(0,0);

        //String[] rules = new String[]{"F=FF", "f=ff", "G=0.5FF-X-F-H-f-f>1G", "G=0.5FF-Z-F-H-f-f>1G", "H=0.5 +>1I-<1I-Z-F--fF", "H=0.5 +>1J-<1J-Z-F--fF", "I=0.2F+F->1I-<1F-Z--f", "I=0.8F+F->1J-<1J-F--f", "J=0.2 +F->1J-<1F-Z--fF", "J=0.8 +I->1I-<1F-F--fF", "Z=0.25F-<1Z++>1f-F", "Z=0.25F+<1Z-->1f+F", "Z=0.4F+<1Z--fZ-->1f-F", "Z=0.1FF", "X=F-Y++f-F", "Y=F+>2Y--f+<2F-<2Y--f->2"};
        //Fractal test = new Fractal(this, rules, "F-F-F-F", new Vector2D(-0.5, 0.5), spawnPosition, 1, 90, 2, 0.02);
        //fractals.add(test);

        Fractal triangle = new Triangle(this, spawnPosition);
        fractals.add(triangle);

        Fractal snowflake = new Snowflake(this, spawnPosition);
        fractals.add(snowflake);

        spawnPosition = new Vector2D(0,0);
        Fractal pathFractal = new PathFractal(this, spawnPosition);
        fractals.add(pathFractal);
        fractalsToDraw.add(fractals.get(0));

        // Initialize ourHolder and paint objects
        ourHolder = getHolder();
        PAINT = new Paint();
        // Set our boolean to true - game on!
        playing = true;

    }

    @Override
    public void run()
    {
        while (playing)
        {
            startFrameTime = System.currentTimeMillis();
            long startUpdateTime = System.currentTimeMillis();
            // Update the frame
            update();
            //System.out.println("updateTime: "+(System.currentTimeMillis()-startUpdateTime));
            long startDrawTime = System.currentTimeMillis();
            // Draw the frame
            draw();
            //System.out.println("drawTime: "+(System.currentTimeMillis()-startDrawTime));
            // Calculate the fps this frame
            // We can then use the result to
            // time animations and more.
            timeToRender = System.currentTimeMillis() - startFrameTime;
            if (timeToRender > 0)
            {
                fps = 1000 / timeToRender;
                if (fps > maxFps)
                {
                    long sleepTime = 1000 / maxFps - timeToRender;
                    try
                    {
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
            // time this frame took
            endFrameTime = System.currentTimeMillis() - startFrameTime;
            endFrameTime = Math.max(endFrameTime, 1);
            fps = 1000 / endFrameTime;
        }

    }

    private void calculateCutOffLimit()
    {
        if (getWidth() > getHeight())
        {
            cutOffLimitY = 1;
            cutOffLimitX = (double)getWidth() / (double)getHeight();
        }
        else
        {
            cutOffLimitX = 1;
            cutOffLimitY = (double)getHeight() / (double)getWidth();
        }
    }

    public void update()
    {
        calculateCutOffLimit();
        for (Fractal fractal : fractalsToDraw)
        {
            fractal.getTurtle().zoom(zoomPoint, 1.02);
        }
    }

    // Draw the newly updated scene
    public void draw()
    {
        // Make sure our drawing surface is valid or we crash
        if (ourHolder.getSurface().isValid()) {
            // Lock the canvas ready to draw
            canvas = ourHolder.lockCanvas();
            PAINT.setStyle(Paint.Style.FILL);
            PAINT.setColor(Color.BLACK);
            canvas.drawPaint(PAINT);
            PAINT.setColor(Color.WHITE);

            for (int i = fractalsToDraw.size() - 1; i >= 0; i--)
            {
                if (fractalsToDraw.get(i).getTurtle().drawAndCutOff(canvas))
                {
                    System.out.println("removed fractal " + fractalsToDraw.get(i));
                    fractalsToDraw.remove(i);
                    counter++;
                    if (counter == fractals.size())
                        counter = 0;
                    fractalsToDraw.add(fractals.get(counter));
                }
            }
            PAINT.setColor(Color.argb(255, 249, 129, 0));
            PAINT.setTextSize(45);
            canvas.drawText("FPS:" + fps, 20, 40, PAINT);
            ourHolder.unlockCanvasAndPost(canvas);
        }

    }

    public void pause()
    {
        playing = false;
        try
        {
            gameThread.join();
        }
        catch (InterruptedException e)
        {
            Log.e("Error:", "joining thread");
        }

    }

    public void resume()
    {
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    public double getCutOffLimitX()
    {
        return cutOffLimitX;
    }

    public double getCutOffLimitY()
    {
        return cutOffLimitY;
    }

}