import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParseExpression {
	
	public static String readFile(String path) throws IOException {
		
		String expression = "";
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(path));
			expression = reader.readLine();
			reader.close();
		}
		
		catch (FileNotFoundException e) {
			System.err.println("File not found!");
			System.exit(1);
		}
		
		return expression;
	}
	
	public static ExpressionRep expressionParse (String expression, boolean conclusion, boolean negate) {
		
		ExpressionRep lastExp = null;
		Symbols lastSymbol = null;
		Matcher matcher = null;

		if (conclusion) {

			int i = 0;
			int j = 0;

			ExpressionRep result = null;

			while (i < expression.length()) {

				if (expression.charAt(i) == '}' || expression.charAt(i) == '{' || expression.charAt(i) == ' ') {
					i++;
				} else if (expression.charAt(i) == ':') {
					i++;

					while (expression.charAt(i) == ' ') {
						i++;
					}

					ExpressionRep conclusion1 = expressionParse(expression.substring(i, expression.length()), false,false);

					if (negate) {
						conclusion1 = LogicOperations.negate(conclusion1);
					}

					if (result == null) {
						return conclusion1;
					} else {
						return LogicOperations.combine(result, conclusion1, Symbols.AND);

					}
				} else {

					j = i;

					while (expression.charAt(j) != ',' && expression.charAt(j) != '}') {
						j++;
					}

						ExpressionRep newOne = expressionParse(expression.substring(i, j), false, false);

						if (result != null) {
							result = LogicOperations.combine(result, newOne, Symbols.AND);
						} else {
							result = newOne;
						}

					i = j;
					i++;
				}
			}
		}

		Pattern literals = Pattern.compile("[A-Z]+[0-9]*");
		Pattern terminals = Pattern.compile("&|\\||->|<->");

		int i = 0;

		while (i < expression.length()) {

			if (expression.charAt(i) == ' ') {
				i++;
			} else if (expression.charAt(i) == '-' && expression.charAt(i + 1) != '>') {
				i++;
			} else if (expression.charAt(i) == '(') {

				int j = i + 1;
				boolean foundEndBracket = false;

				int netBrackets = 1;

				while (!foundEndBracket) {

					if (expression.charAt(j) == '(') {

						netBrackets++;

					} else if (expression.charAt(j) == ')') {
						netBrackets--;
					}

					if (netBrackets == 0) {
						foundEndBracket = true;
					}

					j++;

				}

				ExpressionRep subExpression = ParseExpression.expressionParse(expression.substring(i + 1, j - 1),false, false);

				int k = 1;

				try {
					while (expression.charAt(i - k) == '-') {
						subExpression = LogicOperations.negate(subExpression);
						k++;
					}
				} catch (StringIndexOutOfBoundsException e) {
				}

				if (lastExp != null && subExpression != null && lastSymbol != null) {
					lastExp = LogicOperations.combine(lastExp, subExpression, lastSymbol);
				} else {
					lastExp = subExpression;
				}

				i = j;

			} else {

				int literalStart = expression.length();
				int literalEnd = expression.length();
				int terminalStart = expression.length();
				int terminalEnd = expression.length();

				try {
					matcher = literals.matcher(expression.substring(i));
					matcher.find();
					literalStart = matcher.start() + i;
					literalEnd = matcher.end() + i;
				} catch (IllegalStateException e) {
				}

				try {
					matcher = terminals.matcher(expression.substring(i));
					matcher.find();
					terminalStart = matcher.start() + i;
					terminalEnd = matcher.end() + i;
				} catch (IllegalStateException e) {
				}

				if (literalStart < terminalStart) {

					ExpressionRep newExpression = new AtomicMethods(expression.substring(literalStart, literalEnd));
					int j = 1;

					try {
						while (expression.charAt(literalStart - j) == '-') {
							newExpression = LogicOperations.negate(newExpression);
							j++;
						}
					} catch (StringIndexOutOfBoundsException e) {

					}

					if (lastSymbol != null) {
						lastExp = LogicOperations.combine(lastExp, newExpression, lastSymbol);
					} else {
						lastExp = newExpression;
					}

					i = literalEnd;

				} else {

					lastSymbol = Symbols.fromString(expression.substring(terminalStart, terminalEnd));
					i = terminalEnd;
				}

			}

		}

		return lastExp;
	}
	

}
