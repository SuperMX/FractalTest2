package at.fractal.fractalapp.fractal;

import at.fractal.fractalapp.function.Function;

/**
 * Created by Sebastian on 19.10.2016.
 */

public class ZoomSpeedManager
{
    private long fps;
    // zoom factor per second
    private double zoomSpeed;
    private double zoomFactor;
    private double previousZoomFactor = 0;
    private Function zoomSpeedFunction;

    public ZoomSpeedManager(double zoomSpeed)
    {
        this.zoomSpeed = zoomSpeed;
    }

    public ZoomSpeedManager(Function zoomSpeedFunction)
    {
        this.zoomSpeedFunction = zoomSpeedFunction;
    }

    public void updateZoomSpeed(long currentFps)
    {
        if (zoomSpeedFunction != null)
        {
            long updateTime = 1000 / currentFps;
            zoomSpeedFunction.setIncrementStep(updateTime);
            zoomSpeed = zoomSpeedFunction.calculateIncremented();
            zoomSpeed = zoomSpeedFunction.limit(zoomSpeed);
            System.out.println(zoomSpeed);
        }
    }

    public double calculateZoomFactor(long currentFps)
    {
        if (currentFps <= 0)
            currentFps = 1;
        zoomFactor = Math.pow(zoomSpeed, 1d / currentFps);
        zoomFactor = Math.min(zoomFactor, 2);
        zoomFactor = Math.max(1, zoomFactor);
        if (previousZoomFactor != 0)
        {
            zoomFactor = Math.max(zoomFactor, previousZoomFactor * 0.7);
            zoomFactor = Math.min(zoomFactor, previousZoomFactor * 1.3);
        }
        previousZoomFactor = zoomFactor;
        return zoomFactor;
    }
}
