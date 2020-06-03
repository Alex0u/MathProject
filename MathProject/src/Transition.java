
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
	
	public Etat getEtatInit() {
		return this.etatInit;
	}
	
	public Etat getEtatFinal() {
		return this.etatFinal;
	}
	
	public void setEtatInit(Etat sommetInit) {
		this.etatInit = sommetInit;
	}
	
	public void setEtatFinal(Etat sommetFinal) {
		this.etatFinal = sommetFinal;
	}
	
	public String toString() {
		return "(" + this.etatInit.getNom() + ") --[" + this.symbole + "]-->(" + this.etatFinal.getNom() + ")"; 
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((etatFinal == null) ? 0 : etatFinal.hashCode());
		result = prime * result + ((etatInit == null) ? 0 : etatInit.hashCode());
		result = prime * result + ((symbole == null) ? 0 : symbole.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Transition other = (Transition) obj;
		if (etatFinal == null) {
			if (other.etatFinal != null)
				return false;
		} else if (!etatFinal.equals(other.etatFinal))
			return false;
		if (etatInit == null) {
			if (other.etatInit != null)
				return false;
		} else if (!etatInit.equals(other.etatInit))
			return false;
		if (symbole == null) {
			if (other.symbole != null)
				return false;
		} else if (!symbole.equals(other.symbole))
			return false;
		return true;
	}
}