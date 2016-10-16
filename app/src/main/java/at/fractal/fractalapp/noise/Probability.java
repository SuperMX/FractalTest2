package at.fractal.fractalapp.noise;

import java.util.List;

/**
 * Math concerning probability calculations.
 */
public class Probability
{

    /**
     * @param probabilities the probability for each element (index) in the list.
     *                      All probabilities summed up must equal 1.
     * @return the index of the chosen element
     */
    public static int selectOneAccordingToProbabilities(List<Double> probabilities)
    {
        double value = Math.random();
        double lowerBound = 0;
        double upperBound = 0;
        int counter = 0;
        for (Double probability : probabilities)
        {
            upperBound += probability;
            if (lowerBound <= value && value < upperBound)
                return counter;
            lowerBound = upperBound;
            counter++;
        }
        return -1;
    }
}
