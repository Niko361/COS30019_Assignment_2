import java.util.HashMap;
import java.util.LinkedList;

public class ForwardChaining extends SolveMethod
{
    //A table of symbols, where the count is the number of symbols in each clause's premise.
    HashMap<String, Integer> count = new HashMap<String, Integer>();
    
    //This is the queue of symbols to be evaluated.
    LinkedList<String> queue = new LinkedList<String>();
    
    //This is the table of symbols that have been inferred
    HashMap<String, Boolean> inferred = new HashMap<String, Boolean>();

    //Using a String to add symbols onto as they are infered to maintain the order in which they were infered.
    String inferredString = new String();
    
    ForwardChaining()
    {
        name = "FC";
    }


    public String Solve(Problem inputProblem)
    {
        //Inferred is initially set to false for all symbols in the knowledge base.
        InitialiseInferred(inputProblem);

        //The count for each statement is initially the number of symbols in each clause's premise
        InitialiseCount(inputProblem);

        //puts all symbols initially known to be true in the queue.
        InitialiseQueue(inputProblem);

        while(!queue.isEmpty())
        {
            String p = PopQueue();

            //checks if the solution has been found
            if (p.compareTo(inputProblem.query) == 0)
            {
                String solution = "Yes: " + inferredString + p;
                return solution;
            }

            if(!inferred.get(p))
            {
                inferred.replace(p, true);
                inferredString += p + ", ";

                for (String sentence : inputProblem.knowledgeBase.sentences) 
                {
                    String[] hornBodyHead =  sentence.split("(=>)");
                    if(hornBodyHead.length > 1)
                    {
                        String[] hornBodySymbols = hornBodyHead[0].split("(&)");
                        for(String sym : hornBodySymbols)
                        {
                            if (p.compareTo(sym) == 0)
                            {
                                count.replace(sentence, count.get(sentence) - 1);
                                if(count.get(sentence) == 0)
                                {
                                    queue.add(hornBodyHead[1]);
                                }
                            }
                        }
                    }
                }
            }
        }

        String solution = "No";
        return solution;
    }

    //counts the number of symbols in the body of each horn clause, and adds it to the count map for each horn-clause/sentence.
    private void InitialiseCount(Problem prob)
    {
        for (String sentence : prob.knowledgeBase.sentences)
        {
            int num = 0;
            
            String[] hornBodyHead =  sentence.split("(=>)");
            if(hornBodyHead.length > 1)
            {
                String[] hornBodySymbols = hornBodyHead[0].split("(&)");
                num = hornBodySymbols.length;
            }

            this.count.put(sentence, num);
        }
    }

    //puts all symbols initially known to be true in the queue.
    private void InitialiseQueue(Problem prob)
    {
        for (String sentence : prob.knowledgeBase.sentences)
        {
            String[] hornBodyHead =  sentence.split("(=>)");
            //the length will only be one if the sentence is a statement is a fact.
            if(hornBodyHead.length == 1)
            {
                queue.add(sentence);
            }
        }
    }

    //All symbols are initially not inferred.
    private void InitialiseInferred(Problem prob)
    {
        for (String s : prob.knowledgeBase.symbols)
        {
            inferred.put(s, false);
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
