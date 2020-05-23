import java.util.ArrayList;

public class Automate {
	private int nbSommets;
	private ArrayList<Sommet> listSommets;
	private String fichier;
	
	public Automate(String cheminFichier) {
		this.listSommets = new ArrayList<Sommet>();
		this.nbSommets = 0;
		this.fichier = cheminFichier;
	}
	
	public int getNbEtats() {
		return nbSommets;
	}
	
	public void setNbEtats(int nbEtats) {
		this.nbSommets = nbEtats;
	}
	
	public ArrayList<Sommet> getListEtats() {
		return listSommets;
	}
	
	public void setListEtats(ArrayList<Sommet> listEtats) {
		this.listSommets = listEtats;
	}
}
