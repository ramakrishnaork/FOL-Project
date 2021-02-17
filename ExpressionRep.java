import java.util.ArrayList;


public interface ExpressionRep {
	
	public enum ExpressionType{ATOM, CONSTRUCTION};
	public ExpressionType getType();
	public String getName();
	public ArrayList<ExpressionRep> getTerms();
	public Symbols getSymbol();
	public int getNegation();
	public int getDepth();
	public int getSize();
	public ExpressionRep addTermFront(ExpressionRep term, Symbols symbol);
	public ExpressionRep addTermBack(ExpressionRep term, Symbols symbol);

}
