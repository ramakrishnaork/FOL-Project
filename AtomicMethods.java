import java.util.ArrayList;

public class AtomicMethods implements ExpressionRep {
	
	private String name;
	private int negation;
	
	public AtomicMethods (String name) {
		this.name = name;
		this.negation = 0;
	}
	
	public AtomicMethods (String name,int negation) {
		this.name = name;
		this.negation = negation;
	}

	@Override
	public ExpressionType getType() {
		return ExpressionType.ATOM;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public ArrayList<ExpressionRep> getTerms() {
		ArrayList<ExpressionRep> result = new ArrayList<>();
		result.add(this);
		return result;
	}

	@Override
	public Symbols getSymbol() {
		return null;
	}

	@Override
	public int getNegation() {
		return negation;
	}

	@Override
	public int getDepth() {
		return 0;
	}

	@Override
	public int getSize() {
		return 1;
	}

	@Override
	public ExpressionRep addTermFront(ExpressionRep term, Symbols symbol) {
		ArrayList<ExpressionRep> terms = new ArrayList<>();
		terms.add(term);
		terms.add(this);
		return new CombineExp(terms, symbol);
	}

	@Override
	public ExpressionRep addTermBack(ExpressionRep term, Symbols symbol) {
		ArrayList<ExpressionRep> terms = new ArrayList<>();
		terms.add(this);
		terms.add(term);
		return new CombineExp(terms, symbol);
	}
	
	public String toString() {

		String result = "";
		for (int i = 0; i < negation; i++) {
			result += "-";
		}
		return result + name;
	}
	
	public int hashCode() {
		return negation + name.hashCode();
	}
	
	public boolean equals(Object obj) {
		if (!(obj instanceof AtomicMethods))
			return false;
		if (obj == this)
			return true;

		AtomicMethods exp = (AtomicMethods) obj;
		if (exp.getNegation() == negation && exp.getName().equals(name)) {
			return true;
		}

		return false;

	}

}
