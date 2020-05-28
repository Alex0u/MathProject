import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Automate {
	private int id;
	private int nbEtats;
	private ArrayList<Etat> listEtats;
	private String fichier;
	private int nbSymboles;
	private int nbInit;
	private int nbFinaux;
	private int nbTransition;
	private String[] alphabet = {"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"};
	private String[][] tabTransi;
	private boolean motVide = false;
	private boolean poubelle = false;
	private boolean isComplet = false;
	private boolean isDeterministe = true;

	public Automate(String cheminFichier, int number) {
		this.listEtats = new ArrayList<Etat>();
		this.nbEtats = 0;
		this.nbSymboles = 0;
		this.fichier = cheminFichier;
		this.nbInit = 0;
		this.nbFinaux = 0;
		this.nbTransition = 0;
		this.id = number;
	}

	public int getId() {
		return this.id;
	}
	
	public boolean isAsynchrone() {
		if(this.motVide) {
			System.out.println("L'automate n°" + this.id + " est asynchrone. C'est parce qu'il possède une ou plusieurs transition(s) contenant le mot vide :");
			
			for(int i = 0; i < this.listEtats.size(); i++) {
				for(int z = 0; z < this.listEtats.get(i).getListSortantes().size(); z++) {
					if(this.listEtats.get(i).getListSortantes().get(z).getSymbole().equalsIgnoreCase("*")) {
						System.out.println(this.listEtats.get(i).getListSortantes().get(z).toString());
					}
				}
			}
			System.out.println();
		} else {
			System.out.println("L'automate n°" + this.id + " n'est pas asynchrone.");
			System.out.println();
		}
		return this.motVide;
	}
	
	public boolean isComplet() {
		boolean test = true;
		int totalSymboles = this.nbSymboles;

		if(this.motVide) {
			totalSymboles += 1;
		}
		
		for(int x = 0; x < this.nbEtats; x++) {
			for(int y = 0; y < totalSymboles; y++) {
				if(this.tabTransi[x][y].equalsIgnoreCase("-")) {
					test = false;
					if(this.motVide && y == totalSymboles) {
						System.out.println("(" + x + ")--[*]-->" );
					} else {
						System.out.println("(" + x + ")--[" + this.alphabet[y] + "]-->" );
					}
				}
			}
		}
		this.isComplet = test;
		
		if(this.isComplet) {
			System.out.println("L'automate n°" + this.id + " est complet.");
		} else {
			System.out.println("L'automate n°" + this.id + " n'est pas complet car il lui manque les transitions précédentes.");
		}
		
		return this.isComplet;
	}
	
	public boolean isDeterministe() {
		if(this.nbInit > 1) {
			this.isDeterministe = false;
			System.out.println("L'automate n°" + this.id + " n'est déterministe car il possède plusieurs entrées");
			return this.isDeterministe;
		} else {
			int totalSymboles = this.nbSymboles;

			if(this.motVide) {
				totalSymboles += 1;
			}
			
			for(int x = 0; x < this.nbEtats; x++) {
				
				for(int y = 0; y < totalSymboles; y++) {
					String st = this.tabTransi[x][y];
					String tab[] = st.split(",");
					if(tab.length > 1) {
						this.isDeterministe = false;
						System.out.println("L'automate n°" + this.id + " n'est pas déterministe car il possède des transitions avec le même symbole");
						return this.isDeterministe;
					}
				}
			}
		}
		System.out.println("L'automate n°" + this.id + " est déterministe");
		return this.isDeterministe;
	}
	
	public int getNbEtats() {
		return nbEtats;
	}

	public void setNbEtats(int nbEtats) {
		this.nbEtats = nbEtats;
	}

	public ArrayList<Etat> getListEtats() {
		return listEtats;
	}

	public void setListEtats(ArrayList<Etat> listEtats) {
		this.listEtats = listEtats;
	}

	public void setMotVide(boolean b) {
		this.motVide = b; 
	}

	public void setPoubelle(boolean b) {
		this.poubelle = b; 
	}

	public void setAutomate() throws IOException {
		File file = new File(fichier);
		BufferedReader br = new BufferedReader(new FileReader(file));
		int cpt = 0;
		String st;

		while((st = br.readLine()) != null) {
			switch (cpt) {
			case 0:
				this.nbSymboles = Integer.parseInt(st);
				break;
			case 1:
				this.nbEtats = Integer.parseInt(st);
				createEtats(nbEtats);
				break;
			case 2:
				String[] parts = st.split(" ");
				for(int i = 0; i < parts.length; i++) {
					if(i == 0) {
						this.nbInit = Integer.parseInt(parts[i]);
					} else {
						getEtat(parts[i]).setEntree(true);
					}
				}
				break;
			case 3:
				parts = st.split(" ");
				for(int i = 0; i < parts.length; i++) {
					if(i == 0) {
						this.nbFinaux = Integer.parseInt(parts[i]);
					} else {
						getEtat(parts[i]).setSortie(true);
					}
				}
				break;
			case 4:
				this.nbTransition = Integer.parseInt(st);
				break;
			default:
				parts = st.split(" ");

				if(parts[1].equalsIgnoreCase("*")) {
					this.motVide = true;
				}
				if(parts[2].equalsIgnoreCase("P")) {
					this.poubelle = true;
					this.listEtats.remove(this.nbEtats-1);
					this.listEtats.add(new Etat("P"));
				} else if (parts[0].equalsIgnoreCase("P")) {
					this.poubelle = true;
					this.listEtats.remove(this.nbEtats-1);
					this.listEtats.add(new Etat("P"));
				}

				Etat etatInit = getEtat(parts[0]);
				Etat etatFinal = getEtat(parts[2]);

				Transition tr = new Transition(parts[1], etatInit , etatFinal);
				etatInit.addListSortantes(tr);
				etatFinal.addListEntrantes(tr);
				break;
			}
			cpt++;
		}
	}

	public void createEtats(int nbEtats) {
		for(int i = 0; i < nbEtats; i++) {
			this.listEtats.add(new Etat(Integer.toString(i)));
		}
	}

	public Etat getEtat(String nom) {
		if(nom.equalsIgnoreCase("P")) {
			return listEtats.get(nbEtats-1);
		}
		return listEtats.get(Integer.parseInt(nom));
	}

	public void initTabTransi() {
		int totalSymboles = this.nbSymboles;
		int totalEtats = this.nbEtats;

		if(this.motVide) {
			totalSymboles += 1;
		}

		this.tabTransi = new String[totalEtats][totalSymboles];

		for(int x = 0; x < totalEtats; x++) {
			for(int y = 0; y < totalSymboles; y++) {
				this.tabTransi[x][y] = "-";
			}
		}
	}

	public void createTabTransi() {
		initTabTransi();

		int totalSymboles = this.nbSymboles;
		int totalEtats = this.nbEtats;

		if(this.motVide) {
			totalSymboles += 1;
		}

		for(int x = 0; x < totalEtats; x++) {
			Etat etat;
			if(x == totalEtats) {
				etat = getEtat("P");
			} else {
				etat = getEtat("" + x);
			}

			for(int y = 0; y < totalSymboles; y++) {
				for(int i = 0; i < etat.getListSortantes().size(); i++) {
					if(etat.getListSortantes().get(i).getSymbole().equalsIgnoreCase(this.alphabet[y]) || (this.alphabet[y] != "a" && this.alphabet[y] != "b" && this.alphabet[y] != "c") && etat.getListSortantes().get(i).getSymbole().equalsIgnoreCase("*")) {
						if(this.tabTransi[x][y].equalsIgnoreCase("-")) {
							this.tabTransi[x][y] = etat.getListSortantes().get(i).getSommetFinal().getNom();
						} else {
							this.tabTransi[x][y] += "," + etat.getListSortantes().get(i).getSommetFinal().getNom();
						}
					}
				}
			}
		}
	}

	public void displayAutomate() {
		createTabTransi();
		int cpt = 0;
		System.out.print("|          |           |");
		for(int i = 0; i < this.nbSymboles; i++) {
			System.out.print("     " + this.alphabet[i] +"     |");
		}

		int totalSymboles = this.nbSymboles;

		if(this.motVide) {
			System.out.print("     *     |");
			totalSymboles += 1;
		}

		System.out.println("");

		for(int x = 0; x < this.nbEtats; x++) {
			Etat etat = getEtat(""+cpt);
			String st = "";

			if(etat.isEntree() && etat.isSortie()) {
				st = "|       E/S|";
			} else if(etat.isEntree()) {
				st = "|         E|";
			} else {
				st = "|         S|";
			}
			
			if(this.poubelle) {
				System.out.print(st + "     " + etat.getNom() +"     |");
			} else if(Integer.parseInt(etat.getNom()) >= 10 ) {
				System.out.print(st + "    " + etat.getNom() +"     |");
			} else {
				System.out.print(st + "     " + etat.getNom() +"     |");
			}
			for(int y = 0; y < totalSymboles; y++) {
				if (this.tabTransi[x][y].length() == 1) {
					System.out.print("     " + this.tabTransi[x][y] + "     |");
				} else if (this.tabTransi[x][y].length() == 2) {
					System.out.print("     " + this.tabTransi[x][y] + "    |");
				} else if (this.tabTransi[x][y].length() == 3) {
					System.out.print("    " + this.tabTransi[x][y] + "    |");
				} else if (this.tabTransi[x][y].length() == 4) {
					System.out.print("    " + this.tabTransi[x][y] + "   |");
				} else if (this.tabTransi[x][y].length() == 5) {
					System.out.print("   " + this.tabTransi[x][y] +"   |");
				} else if (this.tabTransi[x][y].length() == 6) {
					System.out.print("   " + this.tabTransi[x][y] +"   |");
				} else if (this.tabTransi[x][y].length() == 7) {
					System.out.print(" " + this.tabTransi[x][y] +"  |");
				} else if (this.tabTransi[x][y].length() == 8) {
					System.out.print(" " + this.tabTransi[x][y] +" |");
				}
			}
			System.out.println();
			cpt++;
		}
		System.out.println();
	}
}
