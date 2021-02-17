import java.util.HashSet;

public class PairOfCompLiterals {
	
	private HashSet<ExpressionRep> c1;
	private HashSet<ExpressionRep> c2;
	
	
	
	public PairOfCompLiterals(HashSet<ExpressionRep> c1, HashSet<ExpressionRep> c2){
		
		this.c1 = c1;
		this.c2 = c2;

	}

	public HashSet<ExpressionRep> getFirst(){
		return this.c1;
	}

	public HashSet<ExpressionRep> getSecond(){
		return this.c2;
	}
	
	public String toString(){
		return c1.toString() + ", " + c2.toString();
	}
	

}
