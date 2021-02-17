import java.util.ArrayList;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogicOperations {

	public static ExpressionRep combine(ExpressionRep exp1, ExpressionRep exp2, Symbols symbol) {
		ArrayList<ExpressionRep> terms = new ArrayList<>();

		if (symbol.equals(Symbols.IMPLICATION) || symbol.equals(Symbols.EQUIVALENCE)) {

			terms.add(exp1);
			terms.add(exp2);

		} else if (exp1.getType() == ExpressionRep.ExpressionType.ATOM && exp2.getType() == ExpressionRep.ExpressionType.ATOM) {

			terms.add(exp1);
			terms.add(exp2);

		} else if (exp1.getType() == ExpressionRep.ExpressionType.ATOM) {

			if (exp2.getSymbol() == symbol && exp2.getNegation() == 0) {
				return exp2.addTermFront(exp1, symbol);

			} else {
				terms.add(exp1);
				terms.add(exp2);
			}

		} else if (exp2.getType() == ExpressionRep.ExpressionType.ATOM) {

			if (exp1.getSymbol() == symbol && exp1.getNegation() == 0) {
				return exp1.addTermBack(exp2, symbol);

			} else {
				terms.add(exp1);
				terms.add(exp2);
			}

		} else if (exp1.getSymbol() == symbol && exp1.getNegation() == 0) {
			return exp1.addTermBack(exp2, symbol);
		} else {
			
			terms.add(exp1);
			terms.add(exp2);
			
		}

		return new CombineExp(terms, symbol);
	}

	public static ExpressionRep negate(ExpressionRep expression) {
		int negs = expression.getNegation();

		switch (expression.getType()) {
		case ATOM:
			return new AtomicMethods(expression.getName(), negs + 1);
		case CONSTRUCTION:
			return new CombineExp(expression.getTerms(), expression.getSymbol(), negs + 1);
		default:
			return expression;
		}
	}
	
	public static ArrayList<ExpressionRep> negate(ArrayList<ExpressionRep> list) {

		ArrayList<ExpressionRep> negated = new ArrayList<>();

		for (ExpressionRep exp : list) {

			negated.add(negate(exp));

		}

		return negated;

	}
	
	public static ExpressionRep eliminateDoubleNegation (ExpressionRep expression) {
		
		int negs = expression.getNegation();
		
		if (negs % 2 == 0) {

			switch (expression.getType()) {
			case ATOM:
				return new AtomicMethods(expression.getName(), negs - 2);
			case CONSTRUCTION:
				return new CombineExp(expression.getTerms(), expression.getSymbol(), negs - 2);
			default:
				return null;
			}

		}
		
		return expression;
	}
	
	public static ExpressionRep deMorgans (ExpressionRep expression) {
		
		switch (expression.getSymbol()) {
		case AND:
			return new CombineExp(negate(expression.getTerms()), Symbols.OR);
		case OR:
			return new CombineExp(negate(expression.getTerms()), Symbols.AND);
		default:
			return expression;
		}
		
	}
	
	public static ExpressionRep eliminateImplication (ExpressionRep expression) {
		ArrayList<ExpressionRep> terms = expression.getTerms();

		ExpressionRep left = terms.get(0);
		ExpressionRep right = terms.get(1);

		ArrayList<ExpressionRep> newTerms = new ArrayList<>();

		newTerms.add(negate(left));
		newTerms.add(right);

		return new CombineExp(newTerms, Symbols.OR, expression.getNegation());
	}
	
	public static ExpressionRep eliminateDoubleImplication (ExpressionRep expression) {
		ArrayList<ExpressionRep> terms = expression.getTerms();
		ArrayList<ExpressionRep> newTerms = new ArrayList<>();

		ExpressionRep left = terms.get(0);
		ExpressionRep right = terms.get(1);

		ArrayList<ExpressionRep> lhs = new ArrayList<>();
		ArrayList<ExpressionRep> rhs = new ArrayList<>();

		lhs.add(left);
		lhs.add(right);

		rhs.add(right);
		rhs.add(left);

		newTerms.add(new CombineExp(lhs, Symbols.IMPLICATION));
		newTerms.add(new CombineExp(rhs, Symbols.IMPLICATION));
		
		return new CombineExp(newTerms, Symbols.AND, expression.getNegation());
	}
	
	public static boolean checkNegation (ExpressionRep exp1, ExpressionRep exp2) {
		
		if (exp1.toString().equals(Symbols.NOT.toString() + exp2.toString())
				|| exp2.toString().equals(Symbols.NOT.toString() + exp1.toString())) {
			return true;
		}

		return false;
	}
	
	public static ConjunctiveNormalForm genConjunctiveNormalForm (ExpressionRep expression) {
		expression = convert(expression);
		return findClauses(expression.toString());
	}

	private static ConjunctiveNormalForm findClauses(String expression) {
		HashSet<HashSet<ExpressionRep>> cnf = new HashSet<>();
		HashSet<ExpressionRep> currentClause = new HashSet<>();

		Pattern literals = Pattern.compile("[A-Z]+[0-9]*");
		Pattern terminals = Pattern.compile("&|\\||->|<->");
		Matcher matcher = null;

		int i = 0;

		while (i < expression.length()) {

			int literalStart = expression.length();
			int literalEnd = expression.length();
			int terminalStart = expression.length();

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
			} catch (IllegalStateException e) {
			}

			if (literalStart < terminalStart) {

				i = literalStart;
				ExpressionRep newTerm = new AtomicMethods(expression.substring(literalStart, literalEnd));

				try {
					if (expression.charAt(literalStart - 1) == '-') {
						newTerm = LogicOperations.negate(newTerm);
					}
				} catch (StringIndexOutOfBoundsException e) {

				}

				currentClause.add(newTerm);
				i = literalEnd;

			} else if (literalStart > terminalStart) {

				i = terminalStart;

				if (expression.charAt(i) == '|') {
					i++;
				} else if (expression.charAt(i) == '&') {
					cnf.add(currentClause);
					currentClause = new HashSet<>();
					i++;
				}

			} else {
				i++;
			}
		}

		cnf.add(currentClause);

		return new ConjunctiveNormalForm(cnf);
	}

	private static ExpressionRep convert(ExpressionRep expression) {
		if (expression.getNegation() % 2 == 0 && expression.getNegation() > 1) {
			expression = eliminateDoubleNegation(expression);
		}

		if (expression.getType() == ExpressionRep.ExpressionType.ATOM) {
			return expression;
		}

		if (expression.getSymbol() == Symbols.IMPLICATION) {
			return convert(eliminateImplication(expression));
		}

		if (expression.getSymbol() == Symbols.EQUIVALENCE) {
			return convert(eliminateDoubleImplication(expression));
		}

		if (expression.getNegation() == 1) {
			return convert(deMorgans(expression));
		}

		if (expression.getSymbol() == Symbols.AND) {

			ArrayList<ExpressionRep> terms = expression.getTerms();
			ExpressionRep result = null;
			for (ExpressionRep term : terms) {

				try {
					result = combine(result, convert(term), Symbols.AND);
				} catch (NullPointerException e) {
					result = convert(term);
				}
			}

			return result;
		}

		if (expression.getSymbol() == Symbols.OR) {

			ArrayList<ExpressionRep> terms = expression.getTerms();
			ExpressionRep result = null;

			for (ExpressionRep term : terms) {

				try {
					result = combine(result, convert(term), Symbols.OR);
				} catch (NullPointerException e) {
					result = convert(term);
				}
			}

			ArrayList<ExpressionRep> newTerms = result.getTerms();

			for (int i = 0; i < result.getSize() - 1; i = i++) {

				ArrayList<ExpressionRep> finalTerms = new ArrayList<>();

				ExpressionRep expa = newTerms.get(0);
				ExpressionRep expb = newTerms.get(1);

				ArrayList<ExpressionRep> a = expa.getTerms();
				ArrayList<ExpressionRep> b = expb.getTerms();

				for (ExpressionRep ai : a) {
					for (ExpressionRep bi : b) {

						ExpressionRep ci = combine(ai, bi, Symbols.OR);
						finalTerms.add(ci);

					}
				}

				newTerms.remove(expa);
				newTerms.remove(expb);

				ExpressionRep nextResult = new CombineExp(finalTerms, Symbols.AND);

				newTerms.add(0, nextResult);

			}

			return result;

		}

		return null;
	}
	
	@SuppressWarnings("unchecked")
	public static ResolutionProof resolutionProof(ConjunctiveNormalForm cnf) {

		boolean box = false;
		boolean same = false;

		HashSet<HashSet<ExpressionRep>> resolved = new HashSet<>();
		ResolutionProof proof = new ResolutionProof();

		for (HashSet<ExpressionRep> clause : cnf.getCNF()) {
			proof.addLine(new LinesOfProof(clause, ""));
		}

		while (!box && !same) {

			PairOfCompLiterals pair = cnf.findUnresolvedPair(resolved);

			if (pair != null) {

				String resolvents = "";

				resolved.add((HashSet<ExpressionRep>) pair.getFirst().clone());
				resolved.add((HashSet<ExpressionRep>) pair.getSecond().clone());

				HashSet<ExpressionRep> c1 = (HashSet<ExpressionRep>) pair.getFirst().clone();
				HashSet<ExpressionRep> c2 = (HashSet<ExpressionRep>) pair.getSecond().clone();

				ExpressionRep a = null;
				ExpressionRep b = null;

				for (ExpressionRep exp : c1) {

					for (ExpressionRep exp1 : c2) {

						if (LogicOperations.checkNegation(exp, exp1)) {

							resolvents = "Res " + proof.getLine(c1) + ", " + proof.getLine(c2);

							a = exp;
							b = exp1;

							break;
						}
					}

				}

				c1.remove(a);
				c2.remove(b);

				HashSet<ExpressionRep> c = new HashSet<>();
				c.addAll(c1);
				c.addAll(c2);

				if (!cnf.getCNF().contains(c)) {
					proof.addLine(new LinesOfProof(c, resolvents));
					cnf.add(c);
				}

				if (c.isEmpty()) {
					box = true;
					proof.setResult(Satisfiability.UNSATISFIABLE);

				}

			} else {
				same = true;
				proof.setResult(Satisfiability.SATISFIABLE);
			}

		}

		return proof;

	}
	
	public static boolean containsNegation(ExpressionRep exp, Iterable<ExpressionRep> list) {

		for (ExpressionRep exp1 : list) {
			if (checkNegation(exp, exp1)) {
				return true;
			}
		}
		return false;
	}
	
}
