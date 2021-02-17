import java.io.IOException;

public class Main {
	
	public static String fileName = "/Users/ramak/Desktop/test.txt";
	public static String input;
	public static ExpressionRep parsedExpression;
	public static final String unsatisfiable = "UNSATISFIED";
	public static final String satisfiable = "SATISFIED";
	
	public static void main(String[] args) {

		try {
			input = ParseExpression.readFile(fileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Input KB:\n" + input);

		prepareCNF();
		performResolution();
		
	}
	public static void prepareCNF() {
		parsedExpression = ParseExpression.expressionParse(input, true, false);
		convertToCNF(parsedExpression);		 
	}


	public static ConjunctiveNormalForm convertToCNF(ExpressionRep expression) {

		ConjunctiveNormalForm cnf = LogicOperations.genConjunctiveNormalForm(expression);
		System.out.println("CNF:\n" + cnf);

		return cnf;
	}

	public static String performResolution() {

		System.out.println("Negating conclusion");
		parsedExpression = ParseExpression.expressionParse(input, true, true);	
		ConjunctiveNormalForm cnf = convertToCNF(parsedExpression);
		//System.out.println("Starting resolution");
		String result = resolutionProof(cnf);

		if (result == unsatisfiable) {
			System.out.println("Initial expression is unsatisfiable");
			System.out.println("Conclusion does not entail KB");
		} else {
			System.out.println("Initial expression is satisfiable");
			System.out.println("Conclusion entails KB");
		}

		return result;
	}
	public static String resolutionProof(ConjunctiveNormalForm cnf) {

		ResolutionProof proof = LogicOperations.resolutionProof(cnf);
		System.out.println(proof);
		if (proof.getResult() == Satisfiability.SATISFIABLE) {
			return unsatisfiable;
		} else {
			return satisfiable;
		}

	}

}
