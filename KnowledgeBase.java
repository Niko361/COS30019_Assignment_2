import java.util.ArrayList;

public class KnowledgeBase 
{
    ArrayList<String> sentences = new ArrayList<String>();
    ArrayList<String> symbols = new ArrayList<String>();


    private void AddSentence (String s)
    {
        sentences.add(s);
    }
    
    public void AddKB (String inputString)
    {
        // split the input string at semi-colons and strip spaces to get the propositional sentences.
        String[] sentenceStrings = inputString.replaceAll(" ", "").split(";");

        for(String s : sentenceStrings)
        {
            AddSentence(s);
        }

        DeriveSymbols();
    }


    //derives the individual symbols in the knowledge base.
    public void DeriveSymbols ()
    {
        for(String sentence: sentences)
        {
            //Strips ~ symbols, and splits on any logical connectives that can be found in either horn form, or general knowledge base.
            String[] tempsymbols = sentence.replace("~", "").split("(=>)|(&)|(\\()|(\\))|(<=>)|(\\|\\|)");
            for(String symbol : tempsymbols)
            {
                // Necessary for eliminating empty strings found between two or more brackets.
                if(symbol != "")
                {
                    boolean dupe = false;
                    for(String existingSymbol: symbols)
                    {
                        if(existingSymbol.equals(symbol))
                        {
                            dupe = true;
                        }
                    }
                    if(!dupe)
                    {
                        symbols.add(symbol);
                    }
                }
            }    
        }
    }
}

