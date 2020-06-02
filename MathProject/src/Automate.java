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
	private boolean isTabTransiInitiated = false;

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
						System.out.println(this.listEtats.get(x) + "--[*]-->" );
					} else {
						System.out.println(this.listEtats.get(x) + "--[" + this.alphabet[y] + "]-->" );
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

						Transition tr = new Transition(this.listEtats.get(i).getListSortantes().get(a).getSymbole(), newEtatInitial, this.listEtats.get(i).getListSortantes().get(a).getEtatFinal());

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
			this.isStandard = true;
			this.nbInit = 1;
		}
		
		String[][] newTabTransi = new String[this.nbEtats][this.nbSymboles];
		
		for(int x = 0; x < this.nbEtats; x++) {
			for(int y = 0; y < this.nbSymboles; y++) {
				newTabTransi[x][y] = "-";
			}
		}
		
		for(int x = 0; x < this.nbEtats - 1; x++) {
			for(int y = 0; y < this.nbSymboles; y++) {
				newTabTransi[x][y] = this.tabTransi[x][y];
			}
		}
		
		this.tabTransi = newTabTransi;
		
		for(int y = 0; y < this.nbSymboles; y++) {
			for(int i = 0; i < this.listEtats.get(this.nbEtats-1).getListSortantes().size(); i++) {
				if(this.listEtats.get(this.nbEtats-1).getListSortantes().get(i).getSymbole().equalsIgnoreCase(this.alphabet[y]) || (y == this.nbSymboles-1) && this.listEtats.get(this.nbEtats-1).getListSortantes().get(i).getSymbole().equalsIgnoreCase("*")) {
					if(this.tabTransi[this.nbEtats-1][y].equalsIgnoreCase("-")) {
						this.tabTransi[this.nbEtats-1][y] = this.listEtats.get(this.nbEtats-1).getListSortantes().get(i).getEtatFinal().getNom();
					} else {
						this.tabTransi[this.nbEtats-1][y] += "," + this.listEtats.get(this.nbEtats-1).getListSortantes().get(i).getEtatFinal().getNom();
					}
				}
			}
		}
		
		this.isStandard = true;
	}

	public void determiniser() {
		int totalSymboles = this.nbSymboles;

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

						if(!this.isTrAlreadyInV2(tempTabTransi, automateTemp, tr, x, this.getSymboleIndex(automateTemp.get(x).getComposition().get(a).getListSortantes().get(b).getSymbole()))) {
							if(tempTabTransi.get(x)[this.getSymboleIndex(automateTemp.get(x).getComposition().get(a).getListSortantes().get(b).getSymbole())].equalsIgnoreCase("-")) {
								tempTabTransi.get(x)[this.getSymboleIndex(automateTemp.get(x).getComposition().get(a).getListSortantes().get(b).getSymbole())] = automateTemp.get(x).getComposition().get(a).getListSortantes().get(b).getEtatFinal().getNom();
							} else {
								tempTabTransi.get(x)[this.getSymboleIndex(automateTemp.get(x).getComposition().get(a).getListSortantes().get(b).getSymbole())] += "." + automateTemp.get(x).getComposition().get(a).getListSortantes().get(b).getEtatFinal().getNom();
							}
						}
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
		}
		
		this.nbEtats = automateTemp.size();
		this.tabTransi = new String[automateTemp.size()][totalSymboles];
		this.setListEtats(automateTemp);

		for(int x = 0; x < tempTabTransi.size(); x++) {
			for(int y = 0; y < totalSymboles; y++) {
				this.tabTransi[x][y] = tempTabTransi.get(x)[y];
				
				if(!tempTabTransi.get(x)[y].equalsIgnoreCase("-")) {
					this.listEtats.get(x).getListSortantes().add(new Transition(this.alphabet[y], this.listEtats.get(x), automateTemp.get(automateTemp.indexOf(new Etat(tempTabTransi.get(x)[y])))));
					this.listEtats.get(x).getListEntrantes().add(new Transition(this.alphabet[y], automateTemp.get(automateTemp.indexOf(new Etat(tempTabTransi.get(x)[y]))), this.listEtats.get(x)));
				}
			}
		}

		this.isDeterministe = true;
	}

	public void determiniser_asynchrone() {
		
		ArrayList<Etat> automateTemp = new ArrayList<Etat>();
		List<String[]> tempTabTransi = new ArrayList<String[]>(); // col = String[] - ligne = ArrayList()

		if(isDeterministe) {
			System.out.println("Cet automate est déjà déterministe");
		} else {

			int totalSymboles = this.nbSymboles;

			if(this.motVide) {
				totalSymboles += 1;
				
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
				
				for(int i = 0; i < newEtatInit.getComposition().size(); i++) {
					for(int y = 0; y < newEtatInit.getComposition().get(i).getListSortantes().size(); y++) {
						if(newEtatInit.getComposition().get(i).getListSortantes().get(y).getSymbole().equalsIgnoreCase("*")) {
							newEtatInit.getComposition().add(newEtatInit.getComposition().get(i).getListSortantes().get(y).getEtatFinal());
							
							if(nom.isEmpty()) {
								nom += newEtatInit.getComposition().get(i).getListSortantes().get(y).getEtatFinal().getNom();
							} else {
								nom += "." + newEtatInit.getComposition().get(i).getListSortantes().get(y).getEtatFinal().getNom();
							}
							
							if(!newEtatInit.isSortie() && newEtatInit.getComposition().get(i).getListSortantes().get(y).getEtatFinal().isSortie()) {
								newEtatInit.setSortie(true);
							}
						}							
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

							if(!this.isTrAlreadyInV2(tempTabTransi, automateTemp, tr, x, this.getSymboleIndex(automateTemp.get(x).getComposition().get(a).getListSortantes().get(b).getSymbole())) && !automateTemp.get(x).getComposition().get(a).getListSortantes().get(b).getSymbole().equalsIgnoreCase("*")) {
								if(tempTabTransi.get(x)[this.getSymboleIndex(automateTemp.get(x).getComposition().get(a).getListSortantes().get(b).getSymbole())].equalsIgnoreCase("-")) {
									tempTabTransi.get(x)[this.getSymboleIndex(automateTemp.get(x).getComposition().get(a).getListSortantes().get(b).getSymbole())] = automateTemp.get(x).getComposition().get(a).getListSortantes().get(b).getEtatFinal().getNom();
								} else {
									tempTabTransi.get(x)[this.getSymboleIndex(automateTemp.get(x).getComposition().get(a).getListSortantes().get(b).getSymbole())] += "." + automateTemp.get(x).getComposition().get(a).getListSortantes().get(b).getEtatFinal().getNom();
								}
							}
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
				
				totalSymboles -= 1;
				this.nbEtats = automateTemp.size();
				this.tabTransi = new String[automateTemp.size()][totalSymboles];
				this.setListEtats(automateTemp);

				for(int x = 0; x < tempTabTransi.size(); x++) {
					for(int y = 0; y < totalSymboles; y++) {
						this.tabTransi[x][y] = tempTabTransi.get(x)[y];
						
						if(!tempTabTransi.get(x)[y].equalsIgnoreCase("-")) {
							this.listEtats.get(x).getListSortantes().add(new Transition(this.alphabet[y], this.listEtats.get(x), automateTemp.get(automateTemp.indexOf(new Etat(tempTabTransi.get(x)[y])))));
							this.listEtats.get(x).getListEntrantes().add(new Transition(this.alphabet[y], automateTemp.get(automateTemp.indexOf(new Etat(tempTabTransi.get(x)[y]))), this.listEtats.get(x)));
						}
					}
				}
				
				this.isDeterministe = true;
				this.motVide = false;
			} else {
				System.out.println("Cet automate n'est pas asynchrone.");
			}
		}
	}
	
	public void completion() {
		if(this.isComplet) {
			System.out.println("Cet automate est déjà complet");
		} else {
			if(!this.isDeterministe) {
				System.out.println("Vous devez déterminiser cet automate avant de le compléter");
			} else {
				this.poubelle = true;
				this.nbEtats += 1;
				this.listEtats.add(new Etat("P"));
				
				String[][] newTabTransi = new String[this.nbEtats][this.nbSymboles];
				
				for(int x = 0; x < this.nbEtats; x++) {
					for(int y = 0; y < this.nbSymboles; y++) {
						newTabTransi[x][y] = "-";
					}
				}
				
				for(int x = 0; x < this.nbEtats - 1; x++) {
					for(int y = 0; y < this.nbSymboles; y++) {
						newTabTransi[x][y] = this.tabTransi[x][y];
					}
				}
				
				this.tabTransi = newTabTransi;
				
				for(int x = 0; x < this.nbEtats; x++) {
					for(int y = 0; y < this.nbSymboles; y++) {
						if(this.tabTransi[x][y].equalsIgnoreCase("-")) {
							this.tabTransi[x][y] = "P";
							this.listEtats.get(x).addListEntrantes(new Transition(this.alphabet[y], this.listEtats.get(x), this.listEtats.get(this.nbEtats-1)));
							this.listEtats.get(x).addListSortantes(new Transition(this.alphabet[y], this.listEtats.get(this.nbEtats-1), this.listEtats.get(x)));
						}
					}
				}
				this.isComplet = true;
			}
		}
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
		
		return test;
	}

	public int getSymboleIndex(String symbole) {
		if(symbole.equalsIgnoreCase("*")) {
			return this.nbSymboles;
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

	public int getNbFinaux() {
		return nbFinaux;
	}

	public int getNbTransition() {
		return nbTransition;
	}
	
	public boolean isPoubelle() {
		return poubelle;
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
					this.listEtats.get(this.nbEtats-1).setNom("P");
					Etat etat = this.listEtats.get(this.nbEtats-1);
					etat.setSortie(false);
					etat.setEntree(false);
				} else if (parts[0].equalsIgnoreCase("P")) {
					this.poubelle = true;
					this.listEtats.get(this.nbEtats-1).setNom("P");
					Etat etat = this.listEtats.get(this.nbEtats-1);
					etat.setSortie(false);
					etat.setEntree(false);
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
					if(etat.getListSortantes().get(i).getSymbole().equalsIgnoreCase(this.alphabet[y]) || (y == totalSymboles-1) && etat.getListSortantes().get(i).getSymbole().equalsIgnoreCase("*")) {
						if(this.tabTransi[x][y].equalsIgnoreCase("-")) {
							this.tabTransi[x][y] = etat.getListSortantes().get(i).getEtatFinal().getNom();
						} else {
							this.tabTransi[x][y] += "," + etat.getListSortantes().get(i).getEtatFinal().getNom();
						}
					}
				}
			}
		}
		this.isTabTransiInitiated = true;
	}

	public void displayAutomate() {
		if(!isTabTransiInitiated) {
			createTabTransi();
		}
		
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

			if(etat.getNom().equalsIgnoreCase("P")) {
				System.out.print(st + "     " + etat.getNom() +"     |");
			} else if(etat.getNom().length() == 1) {
				System.out.print(st + "     " + etat.getNom() +"     |");
			} else if(etat.getNom().length() == 2) {
				System.out.print(st + "    " + etat.getNom() +"     |");
			} else if(etat.getNom().length() == 3) {
				System.out.print(st + "    " + etat.getNom() +"    |");
			} else if(etat.getNom().length() == 4) {
				System.out.print(st + "    " + etat.getNom() +"   |");
			} else if(etat.getNom().length() == 5) {
				System.out.print(st + "   " + etat.getNom() +"   |");
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
				} else if (this.tabTransi[x][y].length() == 8) {
					System.out.print("" + this.tabTransi[x][y] +"|");
				}
			}
			System.out.println();
			cpt++;
		}
		System.out.println();
	}
}