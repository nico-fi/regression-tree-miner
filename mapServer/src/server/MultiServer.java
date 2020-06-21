package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Classe che modella un server con multithreading.
 */
public class MultiServer {

	/**
	 * Numero di porta su cui il server è in ascolto.
	 */
	private int PORT;

	/**
	 * Costruttore di classe. Inizializza la porta e invoca il metodo run().
	 * 
	 * @param port Numero di porta su cui il server è in ascolto.
	 * @throws IOException Se si verificano errori di input-output nella
	 *                     comunicazione con il client.
	 */
	public MultiServer(int port) throws IOException {
		PORT = port;
		run();
	}

	/**
	 * Istanzia un oggetto ServerSocket che pone in attesa di richiesta di
	 * connessione da parte di un client. Per ogni nuova richiesta di connessione
	 * viene istanziato un oggetto della classe ServerOneClient.
	 * 
	 * @throws IOException Se si verificano errori di input-output nella
	 *                     comunicazione con il client.
	 */
	private void run() throws IOException {
		ServerSocket s = new ServerSocket(PORT);
		try {
			while (true) {
				Socket socket = s.accept();
				try {
					new ServerOneClient(socket);
				} catch (IOException e) {
					socket.close();
				}
			}
		} finally {
			s.close();
		}
	}

}
