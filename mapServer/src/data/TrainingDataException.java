package data;

/**
 * Classe eccezione per gestire errori nell'acquisizione del Training set: la
 * connessione al database fallisce, la tabella è inesistente, ha meno di due
 * colonne o ha zero tuple, l’attributo corrispondente all’ultima colonna non è
 * numerico.
 */
public class TrainingDataException extends Exception {

	/**
	 * Costruttore di classe.
	 * 
	 * @param message Messaggio d'errore comunicato all'utente.
	 */
	TrainingDataException(String message) {
		super(message);
	}

}
