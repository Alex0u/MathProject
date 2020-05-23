
public class Arc {
	private String symbole;
	private Sommet sommetInit, sommetFinal;
	
	public Arc(String symbole, Sommet sommetInit, Sommet sommetFinal) {
		this.symbole = symbole;
		this.sommetInit = sommetInit;
		this.sommetFinal = sommetFinal;
	}
	
	public String getSymbole() {
		return this.symbole;
	}
	
	public void setSymbole(String symbole) {
		this.symbole = symbole;
	}
	
	public Sommet getSommetInit() {
		return this.sommetInit;
	}
	
	public Sommet getSommetFinal() {
		return this.sommetFinal;
	}
	
	public void setSommetInit(Sommet sommetInit) {
		this.sommetInit = sommetInit;
	}
	
	public void setSommetFinal(Sommet sommetFinal) {
		this.sommetFinal = sommetFinal;
	}
	
	public String toString() {
		return "(" + this.sommetInit.getNom() + ") --[" + this.symbole + "]-->(" + this.sommetFinal.getNom() + ")"; 
	}
}
