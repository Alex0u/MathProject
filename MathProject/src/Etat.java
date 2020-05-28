import java.util.ArrayList;

public class Sommet {
	private String nom;
	
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
	public ArrayList<Arc> getListEntrantes() {
		return listEntrantes;
	}
	public void setListEntrantes(ArrayList<Arc> listEntrantes) {
		this.listEntrantes = listEntrantes;
	}
	public ArrayList<Arc> getListSortantes() {
		return listSortantes;
	}
	public void setListSortantes(ArrayList<Arc> listSortantes) {
		this.listSortantes = listSortantes;
	}
	private boolean entree, sortie;
	private ArrayList<Arc> listEntrantes, listSortantes;
}
