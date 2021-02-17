import java.util.ArrayList;

public class CombineExp implements ExpressionRep {
	
	
	private ArrayList<ExpressionRep> terms;
	private Symbols symbol;
	private int negation;
	
	public CombineExp(ArrayList<ExpressionRep> terms, Symbols symbol) {
		this.terms = terms;
		this.symbol = symbol;
		this.negation = 0;
	}
	
	public CombineExp(ArrayList<ExpressionRep> terms, Symbols symbol,int negation) {
		this.terms = terms;
		this.symbol = symbol;
		this.negation = negation;
	}

	@Override
	public ExpressionType getType() {
		return ExpressionType.CONSTRUCTION;
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public ArrayList<ExpressionRep> getTerms() {
		return terms;
	}

	@Override
	public Symbols getSymbol() {
		return symbol;
	}

	@Override
	public int getNegation() {
		return negation;
	}

	@Override
	public int getDepth() {
		
		int depthLevel = 0;

		for (ExpressionRep term : terms) {
			if (term.getDepth() > depthLevel) {
				depthLevel = term.getDepth();
			}
		}
		return 1 + depthLevel;
	}

	@Override
	public int getSize() {
		return terms.size();
	}

	@Override
	public ExpressionRep addTermFront(ExpressionRep term, Symbols symbol) {
		int i = 0;
		
		ArrayList<ExpressionRep> newTerms = new ArrayList<>();
		newTerms.addAll(terms);

		try {
			if (term.getSymbol() == symbol) {

				for (ExpressionRep exp : term.getTerms()) {
					newTerms.add(i, exp);
					i++;
				}
			}

			else {
				newTerms.add(0, term);
			}
		} catch (NullPointerException e) {
			newTerms.add(0, term);
		}

		return new CombineExp(newTerms, this.symbol);
	}

	@Override
	public ExpressionRep addTermBack(ExpressionRep term, Symbols symbol) {
		
		ArrayList<ExpressionRep> newTerms = new ArrayList<>();
		newTerms.addAll(terms);

		try {
			
			if (term.getSymbol() == symbol && term.getNegation() == 0) {

				for (ExpressionRep exp : term.getTerms()) {
					newTerms.add(exp);
				}
			}

			else {
				newTerms.add(term);
			}
		} catch (NullPointerException e) {
			newTerms.add(term);
		}

		return new CombineExp(newTerms, this.symbol);
	}
	
	public int hashCode() {
		return negation + terms.hashCode() + symbol.hashCode();
	}
	
	public String toString() {

		String result = "";

		for (int i = 0; i < negation; i++) {
			result += "-";
		}

		result += "(" + terms.get(0);

		for (int i = 1; i < terms.size(); i++) {
			result += (" " + symbol.toString() + " " + terms.get(i).toString());
		}

		return result + ")";

	}

}
