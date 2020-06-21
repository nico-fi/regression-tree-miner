package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe che realizza l'accesso alla base di dati.
 */
public class DbAccess {

	/**
	 * Driver per il database a cui si desidera accedere.
	 */
	private final String DRIVER_CLASS_NAME = "com.mysql.cj.jdbc.Driver";

	/**
	 * Sottoprotocollo per la connessione alla base di dati.
	 */
	private final String DBMS = "jdbc:mysql";

	/**
	 * Identificativo del server su cui risiede la base di dati.
	 */
	private String SERVER = "localhost";

	/**
	 * Nome della base di dati.
	 */
	private String DATABASE = "MapDB";

	/**
	 * Porta su cui il DBMS accetta le connessioni.
	 */
	private final String PORT = "3306";

	/**
	 * Nome utente per l’accesso alla base di dati.
	 */
	private String USER_ID = "MapUser";

	/**
	 * Password di autenticazione per l’utente USER_ID.
	 */
	private String PASSWORD = "map";

	/**
	 * Attributo per la gestione di una connessione;
	 */
	private Connection conn;

	/**
	 * Carica il driver per la connessione al database e inizializza il membro conn.
	 * 
	 * @throws DatabaseConnectionException Se la connessione al database fallisce.
	 */
	public void initConnection() throws DatabaseConnectionException {
		try {
			Class.forName(DRIVER_CLASS_NAME);
			String connectionString = DBMS + "://" + SERVER + ":" + PORT + "/" + DATABASE + "?user=" + USER_ID
					+ "&password=" + PASSWORD + "&serverTimezone=UTC";
			conn = DriverManager.getConnection(connectionString);
		} catch (ClassNotFoundException | SQLException e) {
			throw new DatabaseConnectionException(e.toString());
		}
	}

	/**
	 * Restituisce il valore del membro conn.
	 * 
	 * @return Attributo per la gestione della connessione.
	 */
	public Connection getConnection() {
		return conn;
	}

	/**
	 * Chiude la connessione riferita da conn.
	 * 
	 * @throws SQLException Se la chiusura della connessione fallisce.
	 */
	public void closeConnection() throws SQLException {
		conn.close();
	}

}
