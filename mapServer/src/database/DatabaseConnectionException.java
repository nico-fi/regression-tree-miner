package database;

/**
 * Classe eccezione per gestire il fallimento nella connessione al database.
 */
public class DatabaseConnectionException extends Exception {

	/**
	 * Costruttore di classe.
	 * 
	 * @param message Messaggio d'errore comunicato all'utente.
	 */
	DatabaseConnectionException(String message) {
		super(message);
	}

}
