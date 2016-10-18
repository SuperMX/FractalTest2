package at.fractal.fractalapp.fractal;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * This class stores the actual looks of a fractal. It stores a collection of commands which are interpreted by the turtle, defining
 * the fractal. It can also be seen as a grammar.
 */
public class LSystem
{

    // region variables

    private List<Rule> rules;
    private List<Command> current;
    private Turtle turtle;

    // endregion

    // region constructors

    public LSystem(List<Rule> rules, List<Command> axiom)
    {
        this.rules = new LinkedList<Rule>(rules);
        this.current = axiom;
    }

    // endregion

    // region getters

    public List<Rule> getRules()
    {
        return rules;
    }

    public List<Command> getCurrent()
    {
        return current;
    }

    // endregion

    // region setters

    public void setTurtle(Turtle turtle)
    {
        this.turtle = turtle;
    }

    // endregion

    // region public methods

    /**
     * adapt the parameters of the commands to compensate for the zoom into the fractal
     */
    public boolean zoom(double zoomFactor)
    {
        boolean nextGenerationCreated = false;
        for (Rule rule : rules)
        {
            Command command = rule.getPredecessor();
            command.zoom(zoomFactor);
            if (!nextGenerationCreated && command.isNextGenerationNeeded())
            {
                nextGenerationCreated = true;
            }
        }
        // the offsets in distance of all jump commands also need to be scaled proportionally
        for (Command command : current)
        {
            if (command.getClass().equals(JumpCommand.class))
            {
                JumpCommand jumpCommand = (JumpCommand)command;
                jumpCommand.zoom(zoomFactor);
            }
        }
        if (nextGenerationCreated)
        {
            current = turtle.cutOff(null);
            nextGeneration();
        }
        return nextGenerationCreated;
    }

    /**
     * creates the next generation of the grammar using the specified rules.
     * @return true if another generation can be created, false otherwise.
     */
    public boolean nextGeneration()
    {
        List<Command> next = new LinkedList<>();
        Iterator<Command> iterator = current.iterator();
        Command command;
        while(iterator.hasNext())
        {
            boolean commandFound = false;
            command = iterator.next();
            for (int j = 0; j < rules.size(); j++)
            {
                if (rules.get(j).getPredecessor().equals(command))
                {
                    next.addAll(rules.get(j).getSuccessor());
                    commandFound = true;
                    break;
                }
            }
            if (!commandFound)
                next.add(command);
        }
        current = next;
        boolean isAtMinimumLength = false;
        for (Rule rule : rules)
        {
            rule.updateProbabilities();
            command = rule.getPredecessor();
            command.shrink();
            if (command.isAtMinimumLength())
                isAtMinimumLength = true;
        }
        System.out.println(current.size() + "\tcommands this frame!");
        return isAtMinimumLength;
    }

    @Override
    public String toString()
    {
        String ret = "";
        for(Command c : current)
        {
            ret += c.toString();
        }
        return ret;
    }

    // endregion

}