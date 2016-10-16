package at.fractal.fractalapp.physic;

import java.util.Random;

import at.fractal.fractalapp.data.Vector2D;
import at.fractal.fractalapp.gui.FractalView;
import at.fractal.fractalapp.noise.PerlinNoise;

/**
 * Any object which has a position and can move around and experience accelerations.
 */
public class GameObject
{

    // region variables

    private Vector2D position;
    private Vector2D velocity = new Vector2D(0,0);
    private Vector2D acceleration = new Vector2D(0,0);
    private double offsetX;
    private double offsetY;
    private double stepSize = 0.02;
    private long counter = 0;
    private FractalView fractalView;

    // endregion

    // region constructors

    public GameObject(Vector2D spawnPosition, FractalView fractalView)
    {
        this.position = spawnPosition;
        this.fractalView = fractalView;
        offsetX = randomInt(0,10000);
        offsetY = randomInt(1000000,1010000);
    }

    // endregion

    // region getters

    public Vector2D getPosition()
    {
        return position.clone();
    }

    // endregion

    // region public methods

    public void updatePosition()
    {
        double positionX = PerlinNoise.noise1D(offsetX + counter * stepSize) * fractalView.getCutOffLimitX() / 0.5;
        double positionY = PerlinNoise.noise1D(offsetY + counter * stepSize) * fractalView.getCutOffLimitY() / 0.5;
        position = new Vector2D(positionX,positionY);
        counter++;
    }

    public void updateAcceleration()
    {
        position = Vector2D.add(position, velocity);
        velocity = Vector2D.add(velocity, acceleration);
        double accelerationX = PerlinNoise.noise1D(offsetX + counter * stepSize) / 300;
        double accelerationY = PerlinNoise.noise1D(offsetY + counter * stepSize) / 300;
        acceleration = new Vector2D(accelerationX,accelerationY);
    }

    // endregion

    // region private methods

    private int randomInt(int min, int max)
    {
        Random random = new Random();
        return min + random.nextInt(max - min);
    }

    // endregion
}
