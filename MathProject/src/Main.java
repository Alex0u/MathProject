import java.io.File;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		System.out.println("--------- D�but du programme ---------\n");
		
		boolean choice = choice("Charger un graphe en m�moire ?\n 1 : oui\n 0 : non\n");
		
	}
	
	public static boolean choice(String msg) {
		
		boolean choix = false;
		Scanner sc = new Scanner(System.in);
		System.out.println(msg);
		
		String userInput = sc.nextLine();
		
		boolean result = userInput.equals("0") | userInput.equals("1");
		
		while(!result) {
			System.out.println("Vous ne pouvez r�pondre que par 1 ou 0");
			userInput = sc.nextLine();
			result = userInput.equals("0") | userInput.equals("1");
		}
		if(userInput.equals("1")) {
			choix = true;
		} else {
			sc.close();
		}
		
		return choix;
	}
	
	public static String Automate(String message) {
		@SuppressWarnings("resource")
		Scanner sc = new Scanner(System.in);
		System.out.println(message);
		String file = sc.nextLine();
		String filePath = System.getProperty("user.dir")+"/"+file;
		File fileGraphe = new File(filePath);
		while(!fileGraphe.exists()) {
			System.out.println("Entrez un fichier pr�sent dans la racine du projet, exemple : \""+System.getProperty("user.dir")+"/fichier.txt");
			file = sc.nextLine();
			filePath = System.getProperty("user.dir")+"/"+file;
			fileGraphe = new File(filePath);
		}
		return file;
	}
	
}