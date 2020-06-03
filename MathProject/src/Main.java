import java.io.File;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		System.out.println("--------- Début du programme ---------\n");
		
		boolean choice = choice("Charger un graphe en mémoire ?\n 1 : oui\n 0 : non\n");
		
		while(choice) {
			Automate automate = Automate("Veuillez indiquer le numéro du fichier contenant les informations de l'automate :\n Exemple pour le fichier 'L3New-MpI--13-21' vous devez entrer '21'");
			System.out.println("Vous avez chargé l'automate n°" + automate.getId() + " et voici sa table de transition : ");
			System.out.println();
			
			try {
				automate.setAutomate();
				automate.displayAutomate();
			} catch(Exception e) {
				System.out.println(e);
			}
			
			action(automate);
			
			choice = choice("Charger un graphe en mémoire ?\n 1 : oui\n 0 : non\n");
		}
	}
	
	public static boolean choice(String msg) {
		
		boolean choix = false;
		Scanner sc = new Scanner(System.in);
		System.out.println(msg);
		
		String userInput = sc.nextLine();
		
		boolean result = userInput.equals("0") | userInput.equals("1");
		
		while(!result) {
			System.out.println("Vous ne pouvez répondre que par 1 ou 0");
			userInput = sc.nextLine();
			result = userInput.equals("0") | userInput.equals("1");
		} if(userInput.equals("1")) {
			choix = true;
			
		} else {
			System.out.println("--------- Programme terminé ---------");
		}
		
		return choix;
	}
	
	public static Automate Automate(String message) {
		@SuppressWarnings("resource")
		Scanner sc = new Scanner(System.in);
		System.out.println(message);
		String file = sc.nextLine();
		String filePath = System.getProperty("user.dir")+"/L3New-MpI--13-"+file+".txt";
		File fileGraphe = new File(filePath);
		while(!fileGraphe.exists()) {
			System.out.println("Entrez un n° d'automate présent dans la racine du projet, exemple : \""+System.getProperty("user.dir")+"/L3New-MPI--13-21.txt");
			file = sc.nextLine();
			filePath = System.getProperty("user.dir")+"/L3New-MpI--13-"+file+".txt";
			fileGraphe = new File(filePath);
		}
		return new Automate(filePath, Integer.parseInt(file));
	}

	public static void action(Automate automate) {
		Scanner sc = new Scanner(System.in);
		System.out.println("Voulez vous effectuer une action sur ce graphe ? \n 1 : oui\n 0 : non\n");

		String userInput = sc.nextLine();

		boolean result = userInput.equals("0") | userInput.equals("1");

		while(!result) {
			System.out.println("Vous ne pouvez répondre que par 1 ou 0");
			userInput = sc.nextLine();
			result = userInput.equals("0") | userInput.equals("1");
		} if(userInput.equals("1")) {
			boolean continuer = true;
			while(continuer) {
				System.out.print("0 : Quitter cet automate ?\n");
				System.out.print("1 : Cet automate est-il asynchrone ?\n");
				System.out.print("2 : Cet automate est-il complet ?\n");
				System.out.print("3 : Cet automate est-il déterministe ?\n");
				System.out.print("4 : Cet automate est-il standard ?\n");
				System.out.print("5 : Reconnaître un mot avec cet automate ?\n");
				System.out.print("6 : Génerer un automate complémentaire ?\n");
				
				String userInput2 = sc.nextLine();

				switch(Integer.parseInt(userInput2)) {
				case 0:
					continuer = false;
					break;
				case 1:
					automate.isAsynchrone();
					break;
				case 2:
					if(automate.isComplet()) {
						break;
					}
					
					System.out.println("Voulez vous compléter cet automate ? \n 1 : oui\n 0 : non\n");

					String userInput3 = sc.nextLine();
					switch(Integer.parseInt(userInput3)) {
					case 1:
						if(automate.isDeterministe()) {
							automate.completion();
							automate.displayAutomate();
						} else {
							System.out.println("Vous devez déterminiser cet automate avant de le compléter");
						}
						break;
					case 0:
						break;
					}
					
					break;
				case 3:
					if(automate.isDeterministe()) {
						break;
					}

					System.out.println("Voulez vous déterminiser cet automate ? \n 1 : oui\n 0 : non\n");

					String userInput4 = sc.nextLine();
					switch(Integer.parseInt(userInput4)) {
					case 1:
						if(automate.isAsynchrone()) {
							System.out.println("Cet automate est asynchrone, voulez vous quand même le déterminiser ? \n 1 : oui\n 0 : non\n");

							userInput4 = sc.nextLine();
							switch(Integer.parseInt(userInput4)) {
							case 1:
								automate.determiniser_asynchrone();
								automate.displayAutomate();
								break;
							case 0:
								break;
							}
						} else {
							automate.determiniser();
							automate.displayAutomate();
						}
						break;
					case 0:
						break;
					}
					break;
				case 4:
					automate.isStandard();
					
					System.out.println("Voulez vous standardiser cet automate ? \n 1 : oui\n 0 : non\n");

					String userInput5 = sc.nextLine();
					switch(Integer.parseInt(userInput5)) {
					case 1:
						automate.standardiser();
						automate.displayAutomate();
						break;
					case 0:
						break;
					}
					break;
				case 5:					
					System.out.println("Voulez vous reconnaitre un mot avec cet automate ? \n 1 : oui\n 0 : non\n");

					String userInput6 = sc.nextLine();
					switch(Integer.parseInt(userInput6)) {
					case 1:
						automate.reconnaissance_deter_complet();
						break;
					case 0:
						break;
					}
					break;
				case 6:					
					automate.automate_complementaire();
					automate.displayAutomate();
					break;
				}
			}
		}
	}
}