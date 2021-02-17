import java.util.HashSet;

public class ConjunctiveNormalForm {
	
	private HashSet<HashSet<ExpressionRep>> cnf;

	public ConjunctiveNormalForm(HashSet<HashSet<ExpressionRep>> cnf) {
		this.cnf = cnf;
	}

	public PairOfCompLiterals findUnresolvedPair(HashSet<HashSet<ExpressionRep>> resolved) {
		for (HashSet<ExpressionRep> clause : cnf) {

			if (!resolved.contains(clause)) {

				for (ExpressionRep exp : clause) {

					for (HashSet<ExpressionRep> clause1 : cnf) {

						if (!clause1.equals(clause)) {
							for (ExpressionRep exp1 : clause1) {
								if (LogicOperations.checkNegation(exp, exp1)) {
									return new PairOfCompLiterals(clause, clause1);
								}
							}
						}
					}
				}

			}

		}
		return null;
	}

	public HashSet<HashSet<ExpressionRep>> getCNF() {
		return this.cnf;
	}
	
	public String toString() {

		String set = "{";

		for (HashSet<ExpressionRep> hs : cnf) {

			String clause = "{";
			for (ExpressionRep exp : hs) {
				clause += exp;
				clause += ", ";
			}

			clause = clause.substring(0, clause.length() - 2);
			clause += "}";
			set += clause;
			set += ", ";
		}

		if (set.length() != 0) {
			set = set.substring(0, set.length() - 2);
		}
		set += "}";
		return set;
	}
	
	public ConjunctiveNormalForm add(HashSet<ExpressionRep> c) {
		cnf.add(c);
		return this;
	}
	
	public boolean equals(Object obj) {
		if (!(obj instanceof ConjunctiveNormalForm))
			return false;
		if (obj == this)
			return true;

		ConjunctiveNormalForm cnf1 = (ConjunctiveNormalForm) obj;
		
		if (cnf1.getCNF().equals(cnf)) {
			return true;
		}

		return false;

	}
	
	public HashSet<ExpressionRep> getAtoms() {

		HashSet<ExpressionRep> atoms = new HashSet<>();
		for (HashSet<ExpressionRep> clause : cnf) {
			for (ExpressionRep atom : clause) {
				if (!LogicOperations.containsNegation(atom, atoms)) {
					atoms.add(new AtomicMethods(atom.getName()));
				}
			}
		}
		return atoms;

	}

}
