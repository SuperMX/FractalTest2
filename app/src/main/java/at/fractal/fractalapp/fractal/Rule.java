package at.fractal.fractalapp.fractal;

import java.util.ArrayList;
import java.util.List;

import at.fractal.fractalapp.function.Function;
import at.fractal.fractalapp.noise.Probability;

/**
 * This class stores one or several replacing rules for one command of a grammar.
 */
public class Rule
{

    // region variables

    private Command predecessor;
    private List<List<Command>> successors = new ArrayList<>();
    private List<Double> probabilities = new ArrayList<>();
    private List<Function> functions = new ArrayList<>();
    private int counter = 0;

    // endregion

    // region getters

    public Command getPredecessor()
    {
        return predecessor;
    }

    /**
     * @return one successor calculated based on the specified probabilities of the replacing rules.
     */
    public List<Command> getSuccessor()
    {
        int index = Probability.selectOneAccordingToProbabilities(probabilities);
        return successors.get(index);
    }

    // endregion

    // region constructors

    /**
     * @param predecessor
     * @param successor
     * @param probability the probability that this specific replacement will be used
     * @param function a function changing the probability over time (can be null if constant probability is desired.)
     */
    public Rule(Command predecessor, List<Command> successor, double probability, Function function)
    {
        this.predecessor = predecessor;
        addSuccessor(successor,probability, function);
    }

    // endregion

    // region public methods

    /**
     * adds another successor to this command.
     * @param probability the probability that this specific replacement will be used
     * @param function a function changing the probability over time (can be null if constant probability is desired.)
     */
    public void addSuccessor(List<Command> successor, double probability, Function function)
    {
        successors.add(successor);
        probabilities.add(probability);
        functions.add(function);
    }

    /**
     * changes the probabilities of the replacements being chosen based on the specified functions.
     */
    public void updateProbabilities()
    {
        for(int i = 0; i < functions.size(); i++)
        {
            if (functions.get(i) != null)
            {
                probabilities.remove(i);
                probabilities.add(i,functions.get(i).calculate(counter));
            }
        }
        counter++;
    }

    // endregion

}
