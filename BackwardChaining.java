import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class BackwardChaining extends SolveMethod
{
    BackwardChaining()
    {
        name = "BC";
    }

    //This is the table of symbols that lists which symbols are given as fact.
    HashMap<String, Boolean> given = new HashMap<String, Boolean>();

    //Global variable used to store the successful path.
    LinkedList<String> solution = new LinkedList<String>();

    
    public String Solve(Problem inputProblem)
    {
        //Only the symbols given as facts are true at the start.
        InitialiseGiven(inputProblem);
        
        LinkedList<String> startingPath = new LinkedList<String>();
        startingPath.add(inputProblem.query);
        
        if(BC_AND_OR(inputProblem.knowledgeBase.sentences, startingPath))
        {
            String returnLine = "Yes: ";
            for(int i = 0; i < solution.size()-1; i++)
            {
                returnLine += solution.get(i) + ", ";
            }
            returnLine += solution.getLast();
            return returnLine;
        }
        else
        {
            return "No.";
        }
    }


    private Boolean BC_AND_OR(ArrayList<String> KB, LinkedList<String> path)
    {
        String currentSymbol = path.getFirst();
        solution.addFirst(currentSymbol);
        
        //checks if the solution has been found
        if (given.get(currentSymbol))
        {
            return true;
        }
        
        ArrayList<ArrayList<String>> premises = GetPremises(currentSymbol, KB);

        if(premises.isEmpty())
        {
            solution.clear();
            solution.addLast(path.getLast());
            return false;
        }


        for(ArrayList<String> premise: premises)
        {
            Boolean allTrue = true;
            for(String premiseSymbol: premise)
            {
                LinkedList<String> newpath = new LinkedList<String>();
                for(String s : path)
                {
                    newpath.add(s);
                }
                newpath.addFirst(premiseSymbol);
                
                if(!BC_AND_OR(KB, newpath))
                {
                    allTrue = false;
                }
            }
            return allTrue;
        }

        //Returns false if none of the paths lead to known given facts.
        return false;
    }
    
    //returns the premises/bodies that must be true in order for a symbol to also be true.
    private ArrayList<ArrayList<String>> GetPremises(String head, ArrayList<String> KB)
    {
        ArrayList<ArrayList<String>> results = new ArrayList<ArrayList<String>>();

        for (String sentence : KB)
        {            
            String[] hornBodyHead =  sentence.split("(=>)");
            if(hornBodyHead.length > 1)
            {
                String[] hornBodySymbols = hornBodyHead[0].split("(&)");
                if(head.compareTo(hornBodyHead[1]) == 0)
                {
                    ArrayList<String> result = new ArrayList<String>();
                    for(String s : hornBodySymbols)
                    {
                        result.add(s);
                    }
                    results.add(result);
                }
            }
        }
        return results;
    }

    //All symbols are initially not inferred.
    private void InitialiseGiven(Problem prob)
    {
        for (String s : prob.knowledgeBase.symbols)
        {
            given.put(s, false);
        }

        for (String sentence : prob.knowledgeBase.sentences)
        {
            String[] hornBodyHead =  sentence.split("(=>)");
            //the length will only be one if the sentence is a statement is a fact.
            if(hornBodyHead.length == 1)
            {
                given.replace(sentence, true);
            }
        }
    }
}
