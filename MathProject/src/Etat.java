import java.util.ArrayList;

public class Etat {
	private String nom;
	private boolean entree, sortie;
	private ArrayList<Transition> listEntrantes, listSortantes;
	private ArrayList<Etat> composedOf;
	
	public Etat(String nom) {
		this.listEntrantes = new ArrayList<Transition>();
		this.listSortantes = new ArrayList<Transition>();
		this.nom = nom;
		this.entree = false;
		this.sortie = false;
		this.composedOf = new ArrayList<Etat>();
	}
	
	public String getNom() {
		return nom;
	}
	
	public void setNom(String nom) {
		this.nom = nom;
	}
	
	public boolean isEntree() {
		return this.entree;
	}
	public void setEntree(boolean entree) {
		this.entree = entree;
	}
	public boolean isSortie() {
		return this.sortie;
	}
	public void setSortie(boolean sortie) {
		this.sortie = sortie;
	}
	public ArrayList<Transition> getListEntrantes() {
		return listEntrantes;
	}
	public void addListEntrantes(Transition tr) {
		this.listEntrantes.add(tr);
	}
	public ArrayList<Transition> getListSortantes() {
		return listSortantes;
	}
	public void addListSortantes(Transition tr) {
		this.listSortantes.add(tr);
	}

	public ArrayList<Etat> getComposition() {
		return this.composedOf;
	}
	
	@Override
	public String toString() {
		return "(" + this.nom + ")";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (entree ? 1231 : 1237);
		result = prime * result + ((listEntrantes == null) ? 0 : listEntrantes.hashCode());
		result = prime * result + ((listSortantes == null) ? 0 : listSortantes.hashCode());
		result = prime * result + ((nom == null) ? 0 : nom.hashCode());
		result = prime * result + (sortie ? 1231 : 1237);
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
		Etat other = (Etat) obj;
		
		StringBuffer sb = new StringBuffer(other.nom).reverse();
		String st = "" + sb;
		if(st.equalsIgnoreCase(nom)) {
			return true;
		} else if (nom == null) {
			if (other.nom != null)
				return false;
		} else if (!nom.equalsIgnoreCase(other.nom)) {
			return false;
		}
		
		return true;
	}
}
