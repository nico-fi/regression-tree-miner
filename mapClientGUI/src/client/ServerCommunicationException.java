package client;

/**
 * Classe eccezione per gestire errori nella comunicazione con il server.
 */
public class ServerCommunicationException extends Exception {

	/**
	 * Costruttore di classe.
	 * 
	 * @param message Messaggio d'errore comunicato all'utente.
	 */
	ServerCommunicationException(String message) {
		super(message);
	}
}
