package server;

/**
 * Classe eccezione per gestire l'acquisizione di un valore mancante o fuori
 * range per un attributo relativo ad un nuovo esempio da classificare.
 */
public class UnknownValueException extends Exception {

	/**
	 * Costruttore di classe.
	 * 
	 * @param message Messaggio d'errore comunicato all'utente.
	 */
	public UnknownValueException(String message) {
		super(message);
	}

}
