import java.util.ArrayList;
import java.util.HashSet;

public class ResolutionProof {
	
	private ArrayList<LinesOfProof> lines;
	private Satisfiability sat;
	
	public ResolutionProof() {
		lines = new ArrayList<>();
	}
	
	public ResolutionProof(ArrayList<LinesOfProof> lines, Satisfiability sat){
		this.lines = lines;
		this.sat = sat;
	}

	public void addLine(LinesOfProof linesOfProof) {
		lines.add(linesOfProof);
		
	}

	public int getLine(HashSet<ExpressionRep> c1) {
		int i = 1;
		
		for(LinesOfProof line : lines){
			if(line.getMain().equals(c1)){
				return i;
			}
			i++;
		}
		return -1;
	}

	public void setResult(Satisfiability sat) {
		this.sat = sat;
	}
	
	public Satisfiability getResult(Satisfiability sat) {
		return this.sat;
	}

	public Satisfiability getResult() {
		return this.sat;
	}
	
	public String toString(){
		
		String returnString = "Resoultion Proof Starts: ";
		
		int lineNo = 1;
		
		for(LinesOfProof line : lines){
			returnString =returnString + ("\n" + lineNo + ": " + line);
			lineNo++;
		}
		
		if(sat == Satisfiability.SATISFIABLE){
			returnString =returnString + "\nNo more clauses to resolve";
			returnString =returnString + "\nNegation is satisfiable";
		}
		else if(sat == Satisfiability.UNSATISFIABLE) {
			returnString =returnString + "\nWe have empty clause";
			returnString =returnString + "\nNegation is unsatisfiable";
		}
		
		returnString = returnString + "\nProof Conclusion";
		
		return returnString;
	}

}
