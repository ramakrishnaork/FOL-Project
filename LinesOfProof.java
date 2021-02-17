import java.util.HashSet;

public class LinesOfProof {
	
	private HashSet<ExpressionRep> main;
	private String operation;
	
	public LinesOfProof(HashSet<ExpressionRep> main, String operation){
		this.main = main;
		this.operation = operation;
	}
	
	public String toString(){
		
		int j = 20 - main.toString().length();
		String spaces = "";
		
		for(int i = 0; i < j; i++){
			spaces += " ";
		}
		
		return main + spaces + operation;
	}
	
	public HashSet<ExpressionRep> getMain(){
		return this.main;
	}

}
