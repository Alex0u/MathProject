
public class Transition {
	private String symbole;
	private Etat etatInit, etatFinal;
	
	public Transition(String symbole, Etat etatInit, Etat etatFinal) {
		this.symbole = symbole;
		this.etatInit = etatInit;
		this.etatFinal = etatFinal;
	}
	
	public String getSymbole() {
		return this.symbole;
	}
	
	public void setSymbole(String symbole) {
		this.symbole = symbole;
	}
	
	public Etat getSommetInit() {
		return this.etatInit;
	}
	
	public Etat getSommetFinal() {
		return this.etatFinal;
	}
	
	public void setSommetInit(Etat sommetInit) {
		this.etatInit = sommetInit;
	}
	
	public void setSommetFinal(Etat sommetFinal) {
		this.etatFinal = sommetFinal;
	}
	
	public String toString() {
		return "(" + this.etatInit.getNom() + ") --[" + this.symbole + "]-->(" + this.etatFinal.getNom() + ")"; 
	}
}