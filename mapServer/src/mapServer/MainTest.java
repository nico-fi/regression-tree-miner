package mapServer;

import java.io.IOException;
import server.MultiServer;

/**
 * Classe per l'avvio di un server che utilizza il multithreading.
 */
public class MainTest {

	/**
	 * Avvia un server che utilizza il multithreading.
	 * 
	 * @param args null
	 */
	public static void main(String[] args) {
		System.out.println("Server started");
		try {
			new MultiServer(8080);
		} catch (IOException e) {
			System.err.println(e.toString());
		}
	}

}