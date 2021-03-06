import java.io.*;
import java.util.ArrayList;

public class iengine 
{
	public static ArrayList<SolveMethod> solveMethods = new ArrayList<SolveMethod>();

	public static void main(String[] args) 
	{
		InitSolveMethods();
		
		if (args.length < 2) {
			System.out.println("Usage: iengine <method> <filename>");
			System.exit(1);
		}

		String methodName = args[0];
		String fileName = args[1];
		
		Problem inputProblem = readProblemFile(fileName);

		SolveMethod thisMethod = null;
		
		//determine which method the user wants to use to solve the puzzles
		for(SolveMethod method : solveMethods)
		{
			//do they want this one?
			if(method.name.compareTo(methodName) == 0)
			{
				//yes, use this method.
				thisMethod = method;
			}
		}
		
		//Has the method been implemented?
		if(thisMethod == null)
		{
			//No, give an error
			System.out.println("Search method identified by " + methodName + " not implemented. Methods are case sensitive.");
			System.exit(1);
		}

		String solution = thisMethod.Solve(inputProblem);

		System.out.println(solution);
	}

	public static void InitSolveMethods()
	{
		solveMethods.add(new TruthTable());
		solveMethods.add(new ForwardChaining());
		solveMethods.add(new BackwardChaining());
	}

	private static Problem readProblemFile(String fileName) 
	{
		try {
			// create file reading objects
			FileReader reader = new FileReader(fileName);
			BufferedReader buffReader = new BufferedReader(reader);

			//Problem object to be returned later.
			Problem problem = new Problem();

			String firstLine = buffReader.readLine();
			if(!(firstLine.compareTo("TELL") == 0))
			{
				System.out.println("The first line of the input file should be \"TELL\".");
				System.exit(1);
			}

			//Takes the second line of the input file, inputs it into a knowledge base, and adds said knowledge base to the problem.
			String secondLine = buffReader.readLine();
			KnowledgeBase KB = new KnowledgeBase();
			KB.AddKB(secondLine);
			problem.knowledgeBase = KB;

			String thirdLine = buffReader.readLine();
			if(!(thirdLine.compareTo("ASK") == 0))
			{
				System.out.println("The third line of the input file should be \"ASK\".");
				System.exit(1);
			}

			String fourthLine = buffReader.readLine();
			
			boolean askMatchesKnownSymbols = false;
			for(String s :problem.knowledgeBase.symbols)
			{
				if (s.compareTo(fourthLine)==0)
				{
					askMatchesKnownSymbols = true;
				}
			}

			if (!askMatchesKnownSymbols)
			{
				System.out.println("Symbol being queried is not contained in this knowledge base.");
				System.exit(1);
			}


			problem.query = fourthLine;



			buffReader.close();

			return problem;
		} catch (FileNotFoundException ex) {
			// The file didn't exist, show an error
			System.out.println("Error: File \"" + fileName + "\" not found.");
			System.out.println("Please check the path to the file.");
			System.exit(1);
		} catch (IOException ex) {
			// There was an IO error, show and error message
			System.out.println(
					"Error in reading \"" + fileName + "\". Try closing it and programs that may be accessing it.");
			System.out.println("If you're accessing this file over a network, try making a local copy.");
			System.exit(1);
		}
		return null;
	}
}
