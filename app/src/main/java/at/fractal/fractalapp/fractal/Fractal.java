package at.fractal.fractalapp.fractal;

import java.util.LinkedList;
import java.util.List;

import at.fractal.fractalapp.data.Vector2D;
import at.fractal.fractalapp.function.Function;
import at.fractal.fractalapp.function.LinearFunction;
import at.fractal.fractalapp.gui.FractalView;
import at.fractal.fractalapp.physic.GameObject;

/**
 * This class describes a Fractal and stores all necessary information and logic to create and zoom into the fractal.
 */
public class Fractal extends GameObject
{

    // region variables

    private LSystem lSystem;
    private Turtle turtle;
    private List<Command> axiom;
    private List<Command> successor;
    private List<Rule> rules;

    private Vector2D startPosition;
    private Vector2D spawnPosition;

    private double startLength;
    private double angle;
    private double shrinkFactor;
    private double nextGenerationLength;

    private FractalView fractalView;

    private Command moveCommand;
    private Command turnLeft;
    private Command turnRight;

    private double probability;
    private Function function;

    // endregion

    // region constructor

    /**
     * creates a new fractal with the specified properties.
     *
     * commands:
     * F to Z (except for L):  turtle moves forward and draws a line.
     * f: turtle moves forward without drawing a line.
     * +: turtle turns left
     * -: turtle turns right
     * Cxx: switch to color xx (x is a digit)
     * >xx: go xx colors to the right from the current (x is a digit)
     * <xx: go xx colors to the left from the current (x is a digit)
     *
     * replacement rules:
     * ([F-Z]|f)=.+ (in regex)
     * instead of simply using "=" as replacement you can also use:
     * =x.x (where x stands for any positive integer number): to specify the probability this replacement rule will be used for the specified command.
     * it is required that the total probabilities of all replacement rules for a specific command always equal 100% or 1!
     * =Lx.x,x.x,x.x,x.x (where x stands for any integer number): to define a linear function to change the probability of this replacement rule to be used over time.
     * the first x.x defines the lowest possible probability, the second x.x the highest possible probabilty,
     * the third x.x the k and the fourth x.x the d of the equation y=kx+d.
     * as the x of this equation is automatically increased over time and starts at 0, the d value also defines the start probability for this replacement.
     *
     *
     * @param fractalView the panel which this fractal is drawn on.
     * @param rulesString define the replacement rules of the used grammar.
     * @param axiomString defines the start axiom of the grammar
     * @param startPosition where the turtle begins to cutOff relative to the middle of the fractal in device coordinates
     * @param spawnPosition where the turtle spawns on the screen in device coordinates
     * @param startLength the distance the turtle moves in a ForwardMove or ForwardDraw command in the grammar's axiom.
     * @param angle the angle the turtle turns when executing a TurnLeft or TurnRight command. (in degrees)
     * @param shrinkFactor determines how many ForwardMove/Draw commands in the next generation are needed to replace one
     *                     ForwardMove/Draw command in the current generation.
     * @param nextGenerationLength when the distance the turtle moves when executing a Forward command
     *                             gets smaller than this the next generation of the fractal should be created. (in device coordinates)
     */
    public Fractal(FractalView fractalView, String[] rulesString, String axiomString, Vector2D startPosition, Vector2D spawnPosition, double startLength, double angle, double shrinkFactor, double nextGenerationLength)
    {
        super(spawnPosition, fractalView);
        rules = new LinkedList<>();
        List<ForwardDraw> drawCommands = new LinkedList<ForwardDraw>();
        moveCommand = new ForwardMove(shrinkFactor, startLength);
        turnLeft = new TurnLeft(angle);
        turnRight = new TurnRight(angle);
        this.startLength = startLength;
        this.angle = angle;
        this.shrinkFactor = shrinkFactor;
        this.nextGenerationLength = nextGenerationLength;
        this.startPosition = startPosition.clone();
        this.spawnPosition = spawnPosition.clone();
        this.fractalView = fractalView;

        for (String ruleString : rulesString)
        {
            successor = new LinkedList<>();
            createCommandsFromString(ruleString, successor, drawCommands);
            // the first "command" in this string always specifies which command is replaced by this rule
            Command predecessor = successor.remove(0);

            boolean predecessorExists = false;
            for (Rule rule : rules)
            {
                if (rule.getPredecessor().equals(predecessor))
                {
                    // there is already a replacement for this command for the next generation.
                    predecessorExists = true;
                    rule.addSuccessor(successor, probability, function);
                }
            }
            if(!predecessorExists)
            {
                // this is the first replacement for this command for the next generation.
                rules.add(new Rule(predecessor, successor, probability, function));
            }
            // reset values for the next rule
            function = null;
            probability = 0;
        }
        axiom = new LinkedList<>();
        createCommandsFromString(axiomString, axiom, drawCommands);
        lSystem = new LSystem(rules, axiom);
        turtle = new Turtle(this, startPosition, lSystem, fractalView);
    }

    // endregion

    // region getters

    public Turtle getTurtle()
    {
        return turtle;
    }

    // endregion

    // region private methods

    /**
     * returns the first integer number occurring in the specified string
     * where the integer number starts at the specified offset index
     */
    private String getIntNumber(String commands, int offset)
    {
        String number = "";
        String part = commands.substring(offset);
        while(part.matches("[0-9].*"))
        {
            number += part.charAt(0);
            part = part.substring(1);
        }
        return number;
    }

    /**
     * returns the first double number occurring in the specified string
     * where the double number starts at the specified offset index
     */
    private String getDoubleNumber(String commands, int offset)
    {
        String number = "";
        String part = commands.substring(offset);
        if (part.matches("[0-9].*")|| part.matches("\\..*") || part.matches("-.*") || part.matches("\\+.*"))
        {
            number += part.charAt(0);
            part = part.substring(1);
        }
        while(part.matches("[0-9].*")|| part.matches("\\..*"))
        {
            number += part.charAt(0);
            part = part.substring(1);
        }
        return number;
    }

    /**
     * adds all commands specified by the commands string to the destination list.
     * @param drawCommands this is necessary to avoid creating the same ForwardDraw object multiple times.
     *                     when a new ForwardDraw object is created by this method it is automatically added to this list.
     */
    private void createCommandsFromString(String commands, List<Command> destination, List<ForwardDraw> drawCommands)
    {
        for (int i = 0; i < commands.length(); i++)
        {
            String command = Character.toString(commands.charAt(i));
            if (command.equals("="))
            {
                String nextChar = Character.toString(commands.charAt(i + 1));
                if (nextChar.equals("L"))
                {
                    // command has the form ?=Lx.x,x.x,x.x,x.x.???
                    i+=2;
                    String number = getDoubleNumber(commands, i);
                    double min = Double.parseDouble(number);
                    i += number.length() + 1;
                    number = getDoubleNumber(commands, i);
                    double max = Double.parseDouble(number);
                    i += number.length() + 1;
                    number = getDoubleNumber(commands, i);
                    double k = Double.parseDouble(number);
                    i += number.length() + 1;
                    number = getDoubleNumber(commands, i);
                    double d = Double.parseDouble(number);
                    probability = d;
                    i += number.length() - 1;
                    function = new LinearFunction(k,d);
                    function.setMinMaxValue(min, max);
                }
                else if (!nextChar.matches("[0-9]"))
                {
                    // command has the form ?=???
                    probability = 1;
                    continue;
                }
                else
                {
                    // command has the form ?=x.x???
                    i++;
                    String number = getDoubleNumber(commands, i);
                    probability = Double.parseDouble(number);
                    i += number.length() - 1;
                }
            }
            else if (command.equals("C"))
            {
                // command has the form ???Cxx???
                i++;
                String number = getIntNumber(commands, i);
                destination.add(new ChangeColorDirectly(Integer.parseInt(number)));
                i += number.length() - 1;
            }
            else if (command.equals(">"))
            {
                // command has the form ???>xx???
                i++;
                String number = getIntNumber(commands, i);
                destination.add(new ChangeColorShifted(-1*Integer.parseInt(number)));
                i += number.length() - 1;
            }
            else if (command.equals("<"))
            {
                // command has the form ???<xx???
                i++;
                String number = getIntNumber(commands, i);
                destination.add(new ChangeColorShifted(Integer.parseInt(number)));
                i += number.length() - 1;
            }
            else if (command.matches("[F-Z]"))
            {
                boolean commandExists = false;
                for (ForwardDraw drawCommand : drawCommands)
                {
                    if (drawCommand.getName().equals(command))
                    {
                        commandExists = true;
                        destination.add(drawCommand);
                    }
                }
                if (!commandExists)
                {
                    Command drawCommand = new ForwardDraw(shrinkFactor, startLength, fractalView, nextGenerationLength, command);
                    drawCommands.add((ForwardDraw)drawCommand);
                    destination.add(drawCommand);
                }
            }
            else if (command.equals("+"))
            {
                destination.add(turnLeft);
            }
            else if (command.equals("-"))
            {
                destination.add(turnRight);
            }
            else if (command.equals("f"))
            {
                destination.add(moveCommand);
            }
        }
    }

    // endregion

}
