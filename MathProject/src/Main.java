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
			
			choice("Charger un graphe en mémoire ?\n 1 : oui\n 0 : non\n");
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
			sc.close();
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
				
				String userInput2 = sc.nextLine();

				switch(Integer.parseInt(userInput2)) {
				case 0:
					System.out.println("0");
					continuer = false;
					break;
				case 1:
					System.out.println("1");
					automate.isAsynchrone();
					break;
				case 2:
					System.out.println("2");
					automate.isComplet();
					break;
				case 3:
					System.out.println("3");
					automate.isDeterministe();
					break;
				}
			}
		} else {
			sc.close();
		}
	}
}