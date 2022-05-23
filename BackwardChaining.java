import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class BackwardChaining extends SolveMethod
{
    //A table of symbols, where the count is the number of symbols in each clause's premise.
    HashMap<String, Integer> count = new HashMap<String, Integer>();
    
    //This is the queue of symbols to be evaluated.
    LinkedList<String> queue = new LinkedList<String>();
    
    //This is the table of symbols that lists which symbols are given as fact.
    HashMap<String, Boolean> given = new HashMap<String, Boolean>();

    //Using a linked list of strings to add symbols onto as they are proven true to maintain the order in which they were proven.
    LinkedList<String> provenInOppositeOrder = new LinkedList<String>();
    
    BackwardChaining()
    {
        name = "BC";
    }


    public String Solve(Problem inputProblem)
    {
        //Only the symbols given as facts are true at the start.
        InitialiseGiven(inputProblem);

        //The count for each statement is initially the number of symbols in each clause's premise
        InitialiseCount(inputProblem);

        //puts all symbols initially known to be true in the queue.
        InitialiseQueue(inputProblem);

        while(!queue.isEmpty())
        {
            String p = PopQueue();

            //checks if the solution has been found
            if ((count.get(p) == 0) && queue.isEmpty())
            {
                String solution = "Yes: ";
                for(String s : provenInOppositeOrder)
                {
                    solution += s + ", ";
                }
                solution += inputProblem.query;
                return solution;
            }

            ArrayList<String> Premises = GetPremises(p, inputProblem);

            for(String premise: Premises)
            {
                Boolean dupe = false;
                for(String s : queue)
                {
                    
                    if(s.compareTo(premise) == 0)
                    {
                        dupe = true;
                    }
                }
                if(!dupe)
                {
                    queue.add(premise);
                    provenInOppositeOrder.addFirst(premise);
                }
            }
/*
            //checks if the solution has been found
            if (count.get(p) == 0)
            {
                String solution = "Yes: ";
                for(String s : provenInOrder)
                {
                    solution += s + ", ";
                }
                solution += p;
                return solution;
            }*/
        }

        String solution = "No";
        return solution;
    }

    //returns the premises/bodies that must be true in order for a symbol to also be true.
    private ArrayList<String> GetPremises(String head, Problem prob)
    {
        ArrayList<String> result = new ArrayList<>();

        for (String sentence : prob.knowledgeBase.sentences)
        {            
            String[] hornBodyHead =  sentence.split("(=>)");
            if(hornBodyHead.length > 1)
            {
                String[] hornBodySymbols = hornBodyHead[0].split("(&)");
                if(head.compareTo(hornBodyHead[1]) == 0)
                {
                    for(String s : hornBodySymbols)
                    {
                        result.add(s);
                    }
                }
            }
        }

        return result;
    }


    //counts the number of unproven premises for each symbol. This is zero if it is a given fact, and -1 if it is neither implied, nor a given fact.
    private void InitialiseCount(Problem prob)
    {
        for (String sym : prob.knowledgeBase.symbols)
        {
            ArrayList<String> premises = GetPremises(sym, prob);
            int num = premises.size();            
            if((premises.size() == 0) && (!given.get(sym)))
            {
                num = -1;
            }
            this.count.put(sym, num);
        }
    }

    //puts all symbols initially known to be true in the queue.
    private void InitialiseQueue(Problem prob)
    {
        queue.add(prob.query);
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

    //pops the first symbol off the queue.
    private String PopQueue()
    {
        String popped = queue.getFirst();
        queue.removeFirst();
        return popped;
    }
}
