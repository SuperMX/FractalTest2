package at.fractal.fractalapp.gui;

import android.content.Context;
import android.graphics.Bitmap;
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
import java.util.Timer;

import at.fractal.fractalapp.data.Vector2D;
import at.fractal.fractalapp.fractal.Fractal;
import at.fractal.fractalapp.fractal.PathFractal;
import at.fractal.fractalapp.fractal.Snowflake;
import at.fractal.fractalapp.fractal.Squares;
import at.fractal.fractalapp.fractal.Triangle;
import at.fractal.fractalapp.fractal.Watch;

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
public class FractalView extends SurfaceView implements Runnable {



    private List<Fractal> fractalsToDraw = new ArrayList<Fractal>();
    private List<Fractal> fractals = new ArrayList<Fractal>();
    private Timer timer;
    private Vector2D zoomPoint = new Vector2D(0.5,0.5);
    private int counter = 0;

    //private Paint paint;

    private double cutOffLimitX = 1, cutOffLimitY = 1;



    // This is our thread
    Thread gameThread = null;

    // This is new. We need a SurfaceHolder
    // When we use Paint and Canvas in a thread
    // We will see it in action in the draw method soon.
    SurfaceHolder ourHolder;

    // A boolean which we will set and unset
    // when the game is running- or not.
    volatile boolean playing;

    // A Canvas and a Paint object
    Canvas canvas;
    Paint paint;

    // This variable tracks the game frame rate
    long fps;

    // This is used to help calculate the fps
    private long timeThisFrame;

    // Declare an object of type Bitmap
    Bitmap bitmapBob;

    // Bob starts off not moving
    boolean isMoving = false;

    // He can walk at 150 pixels per second
    float walkSpeedPerSecond = 150;

    // He starts 10 pixels from the left
    float bobXPosition = 10;

    // When the we initialize (call new()) on gameView
    // This special constructor method runs
    public FractalView(Context context) {
        // The next line of code asks the
        // SurfaceView class to set up our object.
        // How kind.
        super(context);

        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN){

                } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    int ex = (int) event.getX();
                    int ey = (int) event.getY();

                    double x = ((double) ex - (double) getWidth() / 2) * 2 / (double) FractalView.super.getWidth();
                    double y = ((double) ey - (double) getHeight() / 2) * 2 / (double) FractalView.super.getHeight();
                    zoomPoint = new Vector2D(x, y);
                }
                /*for (Fractal fractal : fractalsToDraw) {
                    fractal.getTurtle().zoom(zoomPoint, 1.30);
                }
                FractalView.super.invalidate();*/

                return true;
            }
        });

        // init Dimensions
        cutOffLimitX = (double)getWidth() / (double)getHeight();
        cutOffLimitY = (double)getHeight() / (double)getWidth();

        paint = new Paint();

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
        paint = new Paint();

        // Load Bob from his .png file
        //bitmapBob = BitmapFactory.decodeResource(this.getResources(), R.drawable.bob);

        // Set our boolean to true - game on!
        playing = true;

    }

    @Override
    public void run() {
        while (playing) {

            // Capture the current time in milliseconds in startFrameTime
            long startFrameTime = System.currentTimeMillis();


            long startUpdateTime = System.currentTimeMillis();
            // Update the frame
            update();

            System.out.println("updateTime: "+(System.currentTimeMillis()-startUpdateTime));

            long startDrawTime = System.currentTimeMillis();
            // Draw the frame
            draw();

            System.out.println("drawTime: "+(System.currentTimeMillis()-startDrawTime));
            // Calculate the fps this frame
            // We can then use the result to
            // time animations and more.
            timeThisFrame = System.currentTimeMillis() - startFrameTime;
            if (timeThisFrame > 0) {
                fps = 1000 / timeThisFrame;
            }

        }

    }

    // Everything that needs to be updated goes in here
    // In later projects we will have dozens (arrays) of objects.
    // We will also do other things like collision detection.
    public void update() {

        // If bob is moving (the player is touching the screen)
        // then move him to the right based on his target speed and the current fps.
        /*if (isMoving) {
            bobXPosition = bobXPosition + (walkSpeedPerSecond / fps);
        }*/
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

        for (Fractal fractal : fractalsToDraw) {
            fractal.getTurtle().zoom(zoomPoint, 1.02);
        }


        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Draw the newly updated scene
    public void draw() {

        // Make sure our drawing surface is valid or we crash
        if (ourHolder.getSurface().isValid()) {
            // Lock the canvas ready to draw
            canvas = ourHolder.lockCanvas();

            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.BLACK);
            canvas.drawPaint(paint);
            paint.setColor(Color.WHITE);
            //canvas.drawCircle(x / 2, y / 2, radius, DrawUtils.paint);

            //paint.setColor(Color.BLACK);
            //canvas.drawARGB(0, 0, 0, 0);

            //paint.setColor(Color.WHITE);

            DrawUtils.canvas = canvas;
            DrawUtils.paint = paint;

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


            // Draw the background color
            //canvas.drawColor(Color.argb(255, 26, 128, 182));

            // Choose the brush color for drawing
            paint.setColor(Color.argb(255, 249, 129, 0));

            // Make the text a bit bigger
            paint.setTextSize(45);

            // Display the current fps on the screen
            canvas.drawText("FPS:" + fps, 20, 40, paint);

            // Draw bob at bobXPosition, 200 pixels
            //canvas.drawBitmap(bitmapBob, bobXPosition, 200, paint);
            //for (int i = 0; i < 360; i++) {
            //    canvas.drawLine((float) (i * Math.sin(i)), (float) (i * Math.cos(i)), (int) Math.sin(i), (int) Math.cos(i), paint);
            //}

            // Draw everything to the screen
            ourHolder.unlockCanvasAndPost(canvas);
        }

    }

    // If SimpleGameEngine Activity is paused/stopped
    // shutdown our thread.
    public void pause() {
        playing = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            Log.e("Error:", "joining thread");
        }

    }

    // If SimpleGameEngine Activity is started then
    // start our thread.
    public void resume() {
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    // The SurfaceView class implements onTouchListener
    // So we can override this method and detect screen touches.
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {

        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {

            // Player has touched the screen
            case MotionEvent.ACTION_DOWN:

                // Set isMoving so Bob is moved in the update method
                isMoving = true;

                break;

            // Player has removed finger from screen
            case MotionEvent.ACTION_UP:

                // Set isMoving so Bob does not move
                isMoving = false;

                break;
        }
        return true;
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