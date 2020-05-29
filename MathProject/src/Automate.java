import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
	private boolean isStandard = false;
	
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
					if(this.motVide && y+1 == totalSymboles) {
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
			System.out.println("L'automate n°" + this.id + " n'est pas déterministe car il possède plusieurs entrées");
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
	
	public boolean isStandard() {
		if(this.nbInit > 1) {
			this.isStandard = false;
			System.out.println("L'automate n°" + this.id + " n'est pas standard parce qu'il a plusieurs entrées.");
			return this.isStandard;
		} else {
			for(int i = 0; i < this.nbEtats; i++) {
				if(this.listEtats.get(i).isEntree()) {
					if (this.listEtats.get(i).getListEntrantes().size() != 0) {
						this.isStandard = false;
						System.out.println("L'automate n°" + this.id + " n'est pas standard parce qu'il a des transitions entrantes sur ses états initiaux.");
						return this.isStandard;
					}
				}
			}
		}
		System.out.println("L'automate n°" + this.id + " est standard.");
		this.isStandard = true;
		return this.isStandard;
	}

	public void standardiser() {
		if(isStandard) {
			System.out.println("Cet automate est déjà standard.");
		} else {
			Etat newEtatInitial = new Etat("i");
			newEtatInitial.setEntree(true);
			
			for(int i = 0; i < this.nbEtats; i++) {
				if(this.listEtats.get(i).isEntree()) {
					if(!newEtatInitial.isSortie() && this.listEtats.get(i).isSortie()) {
						newEtatInitial.setSortie(true);
					}
					for(int a = 0; a < this.listEtats.get(i).getListSortantes().size(); a++) {
						
						Transition tr = new Transition(this.listEtats.get(i).getListSortantes().get(a).getSymbole(), this.listEtats.get(i).getListSortantes().get(a).getEtatInit(), this.listEtats.get(i).getListSortantes().get(a).getEtatFinal());
						
						boolean alreadyIn = false;
						
						for(int c = 0; c < newEtatInitial.getListSortantes().size(); c++) {
							tr.setEtatInit(newEtatInitial);
							if(newEtatInitial.getListSortantes().get(c).equals(tr)) {
								alreadyIn = true;
							}
						}

						if(!alreadyIn) {
							newEtatInitial.getListSortantes().add(tr);
						}
					}
					this.listEtats.get(i).setEntree(false);
				}
			}
			this.listEtats.add(newEtatInitial);
			this.nbEtats += 1;
		}
		this.isStandard = true;
	}

	public void determiniser() {
		int totalSymboles = this.nbSymboles;
		int totalEtats = this.nbEtats;

		if(this.motVide) {
			totalSymboles += 1;
		}

		ArrayList<Etat> automateTemp = new ArrayList<Etat>();
		List<String[]> tempTabTransi = new ArrayList<String[]>(); // col = String[] - ligne = ArrayList()
		
		if(isDeterministe) {
			System.out.println("Cet automate est déjà déterministe");
		} else {
			String nom = "";
			Etat newEtatInit = new Etat(nom);
			
			for(int i = 0; i < this.listEtats.size(); i++) {
				if(this.listEtats.get(i).isEntree()) {
					if(nom.isEmpty()) {
						nom += this.listEtats.get(i).getNom();
						if(this.listEtats.get(i).isSortie() && !newEtatInit.isSortie()) {
							newEtatInit.setSortie(true);
						}
					} else {
						nom += "." + this.listEtats.get(i).getNom();
						if(this.listEtats.get(i).isSortie() && !newEtatInit.isSortie()) {
							newEtatInit.setSortie(true);
						}
					}
					newEtatInit.getComposition().add(this.listEtats.get(i));
				}
			}
			
			newEtatInit.setNom(nom);
			automateTemp.add(newEtatInit);
			newEtatInit.setEntree(true);
			this.nbInit = 1;
				
				for(int x = 0; x < automateTemp.size(); x++) {
					//initialisation de la ligne de la table de transition :
					tempTabTransi.add(new String[totalSymboles]);
					for(int temp = 0; temp < totalSymboles; temp++) {
						tempTabTransi.get(x)[temp] = "-";
					}
					
					for(int a = 0; a < automateTemp.get(x).getComposition().size(); a++) {
						for(int b = 0; b < automateTemp.get(x).getComposition().get(a).getListSortantes().size(); b++) {
							Transition tr = new Transition(automateTemp.get(x).getComposition().get(a).getListSortantes().get(b).getSymbole(), automateTemp.get(x), automateTemp.get(x).getComposition().get(a).getListSortantes().get(b).getEtatFinal());
							
							//if(!this.isTrAlreadyIn(automateTemp.get(x).getComposition().get(a).getListSortantes(), tr)) {
							if(!this.isTrAlreadyInV2(tempTabTransi, automateTemp, tr, x, this.getSymboleIndex(automateTemp.get(x).getComposition().get(a).getListSortantes().get(b).getSymbole()))) {
								if(tempTabTransi.get(x)[this.getSymboleIndex(automateTemp.get(x).getComposition().get(a).getListSortantes().get(b).getSymbole())].equalsIgnoreCase("-")) {
									tempTabTransi.get(x)[this.getSymboleIndex(automateTemp.get(x).getComposition().get(a).getListSortantes().get(b).getSymbole())] = automateTemp.get(x).getComposition().get(a).getListSortantes().get(b).getEtatFinal().getNom();
								} else {
									tempTabTransi.get(x)[this.getSymboleIndex(automateTemp.get(x).getComposition().get(a).getListSortantes().get(b).getSymbole())] += "." + automateTemp.get(x).getComposition().get(a).getListSortantes().get(b).getEtatFinal().getNom();
								}
							}
							//}
						}
					}
					
					for(int y = 0; y < totalSymboles; y++) {
						Etat etat = new Etat(tempTabTransi.get(x)[y]);
						String[] parts = tempTabTransi.get(x)[y].split("\\.");
						
						if(parts.length != 0) {
							for(int i = 0; i < parts.length; i++) {
								if (!parts[i].equalsIgnoreCase("-")) {
									etat.getComposition().add(getEtat(parts[i]));
									if(!etat.isSortie() && getEtat(parts[i]).isSortie()) {
										etat.setSortie(true);
									}
								}
							}
						} else if (!tempTabTransi.get(x)[y].equalsIgnoreCase("-")) {
							if(!etat.isSortie() && getEtat(tempTabTransi.get(x)[y]).isSortie()) {
								etat.setSortie(true);
							}
							etat.getComposition().add(getEtat(tempTabTransi.get(x)[y]));
						}
						
						if(!tempTabTransi.get(x)[y].equalsIgnoreCase("-") && !isEtatAlreadyIn(automateTemp, etat)) {
							automateTemp.add(etat);
						}
					}
				}
				
				for(int x = 0; x < automateTemp.size(); x++) {
					if(automateTemp.get(x).isEntree() && automateTemp.get(x).isSortie()) {
						System.out.print("|  E/S");
					} else if(automateTemp.get(x).isEntree()) {
						System.out.print("|    E");
					} else if(automateTemp.get(x).isSortie()){
						System.out.print("|    S");
					} else {
						System.out.print("|     ");
					}
					
					System.out.print("| " + automateTemp.get(x).getNom() + " ");
					System.out.print("| ");
					
					for(int y = 0; y < totalSymboles; y++) {
						System.out.print(tempTabTransi.get(x)[y] + " | ");
					}
					
					System.out.println();
				}
				
		}
		
		this.isDeterministe = true;
	}
	
	public boolean isEtatAlreadyIn(ArrayList<Etat> listEtat, Etat etat) {
		boolean test = false;
		for(int i = 0; i < listEtat.size(); i++) {
			if (etat.equals(listEtat.get(i))) {
				test = true;
				return test;
			} else {
				test = false;
			}
		}
		return test;
	}
	
	public boolean isTrAlreadyIn(ArrayList<Transition> listTr, Transition tr) {
		boolean test = false;
		for(int i = 0; i < listTr.size(); i++) {
			if (tr.equals(listTr.get(i))) {
				test = true;
			} else {
				test = false;
			}
		}
		return test;
	}

	public boolean isTrAlreadyInV2(List<String[]> tempTabTransi, ArrayList<Etat> listNewEtats,Transition tr, int x, int y) {
		boolean test = false;
		
		String[] parts = tempTabTransi.get(x)[y].split("\\.");
		String[] parts2 = listNewEtats.get(x).getNom().split("\\.");
		
		if (parts[0].equalsIgnoreCase("-")) {
			test = false;
			return test;
		}
		
		boolean test2 = true;
		ArrayList<Etat> listTest = new ArrayList<Etat>();
		
		for(int a = 0; a < parts.length; a++) {
			listTest.add(new Etat(parts[a]));
		}
		
		for(int i = 0; i < listTest.size(); i++) {
			if(!listTest.contains(tr.getEtatFinal())) {
				test2 = false;
				return test2;
			}
		}
		
		test = test2;
		/*if(parts2.length != 0 && parts.length != 0) {
			boolean test2 = false;
			
			ArrayList<Etat> listTest = new ArrayList<Etat>();
			ArrayList<Etat> listTest2 = new ArrayList<Etat>();

			for(int a = 0; a < parts.length; a++) {
				listTest.add(new Etat(parts[a]));
			}
			for(int i = 0; i < parts2.length; i++) {
				listTest2.add(new Etat(parts2[i]));
			}

			for(int i = 0; i < listTest.size(); i++) {
				if(listTest2.contains(listTest.get(i))) {
					test2 = true;
				}
			}
			test = test2;
		} else if(parts2.length != 0) {
			boolean test2 = true;
			
			ArrayList<Etat> listTest = new ArrayList<Etat>();

			for(int a = 0; a < parts2.length; a++) {
				listTest.add(new Etat(parts2[a]));
			}

			for(int i = 0; i < listTest.size(); i++) {
				if(!tempTabTransi.get(x)[y].equalsIgnoreCase("-")) {
					if(listTest.contains(new Etat (listNewEtats.get(x).getNom()))) {
						test2 = true;
					}
				}
			}
			test = test2;
		} else if(parts.length != 0) {
			boolean test2 = true;
			
			ArrayList<Etat> listTest = new ArrayList<Etat>();

			for(int a = 0; a < parts.length; a++) {
				listTest.add(new Etat(parts[a]));
			}

			for(int i = 0; i < listTest.size(); i++) {
				if(!tempTabTransi.get(x)[y].equalsIgnoreCase("-")) {
					if(listTest.contains(new Etat (tempTabTransi.get(x)[y]))) {
						test2 = true;
					}
				}
			}
			test = test2;
		} else if(parts.length != 0) {
			for(int i = 0; i < parts.length; i++) {
				if (!parts[i].equalsIgnoreCase("-")) {
					if(tr.getEtatFinal().getNom().equalsIgnoreCase(parts[i])) {
						test = true;
					}
				}
			}
		} else if (!tempTabTransi.get(x)[y].equalsIgnoreCase("-")) {
			if(tr.getEtatFinal().getNom().equalsIgnoreCase(tempTabTransi.get(x)[y])) {
				test = true;
			}
		}*/
		
		return test;
	}
	
	public int getSymboleIndex(String symbole) {
		if(symbole.equalsIgnoreCase("*")) {
			int i = this.nbSymboles;
			if(this.motVide) {
				i++;
			}
			return i;
		} else {
			for(int i = 0; i < this.alphabet.length; i++) {
				if(this.alphabet[i].equalsIgnoreCase(symbole)) {
					return i;
				}
			}
			return 0;
		}
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
					Etat etat = new Etat("P");
					etat.setSortie(false);
					etat.setEntree(false);
					this.listEtats.add(etat);
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
			return this.listEtats.get(this.nbEtats-1);
		}
		return this.listEtats.get(Integer.parseInt(nom));
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
							this.tabTransi[x][y] = etat.getListSortantes().get(i).getEtatFinal().getNom();
						} else {
							this.tabTransi[x][y] += "," + etat.getListSortantes().get(i).getEtatFinal().getNom();
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
			} else if(etat.isSortie()){
				st = "|         S|";
			} else {
				st = "|          |";
			}

			if(this.poubelle) {
				System.out.print(st + "     " + etat.getNom() +"     |");
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
