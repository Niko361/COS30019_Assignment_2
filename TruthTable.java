import java.util.ArrayList;
import java.util.HashMap;

public class TruthTable extends SolveMethod
{
    TruthTable()
    {
        name = "TT";   
    }

    private int modelsQueryTrue = 0;


    public String Solve(Problem inputProblem)
    {
        if(TTCheckAll(inputProblem.knowledgeBase, inputProblem.query, inputProblem.knowledgeBase.symbols, null))
        {
            return "Yes: " + modelsQueryTrue;
        }
        else
        {
            return "No";
        }
    }

    private Boolean TTCheckAll(KnowledgeBase KB, String alpha, ArrayList<String> symbols, HashMap<String, Boolean> model)
    {
    
        if(symbols.isEmpty())
        {
            if(PLTrue(KB, model))
            {
                if(PLTrue(alpha, model))
                {
                    modelsQueryTrue++;
                    return true;
                }
                else
                {
                    return false;
                }
            }
            else
            {
                return true;
            }
        }
        String p = symbols.get(0);
        
        //creates a new ArrayList of Strings, and populates it with all symbols except p
        ArrayList<String> rest = new ArrayList<>();
        for(int i = 1; i < symbols.size(); i++)
        {
            rest.add(symbols.get(i));
        }
        
        HashMap<String, Boolean> modelPTrue = DeepCopyModelAndSet(model, p, true);
        HashMap<String, Boolean> modelPFalse = DeepCopyModelAndSet(model, p, false);

        return TTCheckAll(KB, alpha, rest, modelPTrue) && TTCheckAll(KB, alpha, rest, modelPFalse);
    }

    //copies a Hashmap of strings and booleans that is used for the model, and sets the specified symbol to true or false depending on what it is told.
    private HashMap<String, Boolean> DeepCopyModelAndSet(HashMap<String, Boolean> input, String key, Boolean value)
    {
        HashMap<String, Boolean> copy = new HashMap<String, Boolean>();

        if(input != null)
        {
            for(String str: input.keySet())
            {
                copy.put(str, input.get(str));
            }
        }

        copy.put(key, value);

        return copy;

    }

    //needs to be implemented
    private Boolean PLTrue(KnowledgeBase KB, HashMap<String, Boolean> model)
    {
        //result starts off being assumed to be true, until it either proven false, or returned.
        Boolean result = true;
        for(String sentence: KB.sentences)
        {
            //checks whether an implication in the knowledge base is untrue. It is only untrue if the body of the impication is false and the head is true.
            String[] hornBodyHead =  sentence.split("(=>)");
            if(hornBodyHead.length > 1)
            {
                String[] hornBodySymbols = hornBodyHead[0].split("(&)");
                Boolean hornBodySymbolsAllTrue = true;
                for(String sym: hornBodySymbols)
                {
                    if(!model.get(sym))
                    {
                        hornBodySymbolsAllTrue = false;
                    }
                }

                if(hornBodySymbolsAllTrue && !model.get(hornBodyHead[1]))
                {
                    result = false;
                }
            }
            //if it is a given fact
            else if (hornBodyHead.length == 1)
            {
                if(!(model.get(sentence)))
                {
                    result = false;
                }
            }

        }
        return result;
    }

    private Boolean PLTrue(String alpha, HashMap<String, Boolean> model)
    {
        return model.get(alpha);
    }
}
