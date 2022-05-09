import java.util.LinkedList;

public class KnowledgeBase 
{
    LinkedList<Sentence> sentences = new LinkedList<Sentence>();


    public void AddSentence (Sentence s)
    {
        sentences.add(s);
    }
}

