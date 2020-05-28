import java.util.ArrayList;

public class Etat {
	private String nom;
	private boolean entree, sortie;
	private ArrayList<Transition> listEntrantes, listSortantes;
	
	public Etat(String nom) {
		this.listEntrantes = new ArrayList<Transition>();
		this.listSortantes = new ArrayList<Transition>();
		this.nom = nom;
	}
	
	public String getNom() {
		return nom;
	}
	
	public void setNom(String nom) {
		this.nom = nom;
	}
	
	public boolean isEntree() {
		return entree;
	}
	public void setEntree(boolean entree) {
		this.entree = entree;
	}
	public boolean isSortie() {
		return sortie;
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
}
