package client;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Classe che gestisce la comunicazione con il server.
 */
public class Client {

	/**
	 * Oggetto Socket per instaurare una connessione con il server.
	 */
	private static Socket socket;

	/**
	 * Oggetto per la gestione delle operazioni di lettura dal server.
	 */
	private static ObjectInputStream in;

	/**
	 * Oggetto per la gestione delle operazioni di scrittura verso il server.
	 */
	private static ObjectOutputStream out;

	/**
	 * Sovrascrive il costruttore di default avente visibilità pubblica.
	 */
	private Client() {
	}

	/**
	 * Instaura una connessione con il server.
	 * 
	 * @param host Indirizzo IP usato per stabilire la connessione.
	 * @param port Numero di porta sul quale stabilire la connessione.
	 * @throws IOException Se si verificano errori nel tentativo di stabilire una
	 *                     connessione con il server.
	 */
	public static void startConnection(String host, int port) throws IOException {
		InetAddress addr = InetAddress.getByName(host);
		socket = new Socket(addr, port);
		out = new ObjectOutputStream(socket.getOutputStream());
		in = new ObjectInputStream(socket.getInputStream());
	}

	/**
	 * Richiede al server l'acquisizione di un albero di regressione dal training
	 * set tableName, secondo la modalità specificata in decision.
	 * 
	 * @param decision  Valore che specifica la modalità di acquisizione dell'albero
	 *                  di regressione: 1 per apprendimento da database, 2 per
	 *                  caricamento da archivio.
	 * @param tableName Nome della tabella su database o file system contenente il
	 *                  training set.
	 * @throws ServerCommunicationException Se l'acquisizione dell'albero di
	 *                                      regressione fallisce.
	 */
	public static void acquireTree(int decision, String tableName) throws ServerCommunicationException {
		String answer;
		try {
			if (decision == 1) {
				out.writeObject(0);
				out.writeObject(tableName);
				answer = in.readObject().toString();
				if (!answer.equals("OK"))
					throw new ServerCommunicationException(answer);
				out.writeObject(1);
			} else {
				out.writeObject(2);
				out.writeObject(tableName);
			}
			answer = in.readObject().toString();
			if (!answer.equals("OK"))
				throw new ServerCommunicationException(answer);
		} catch (ClassNotFoundException | IOException e) {
			throw new ServerCommunicationException("Error in communication with server");
		}
	}

	/**
	 * Richiede al server di avviare una predizione dell'attributo di classe per uno
	 * specifico esempio.
	 * 
	 * @throws ServerCommunicationException Se si verificano errori nella
	 *                                      comunicazione con il server.
	 */
	public static void requestPrediction() throws ServerCommunicationException {
		try {
			out.writeObject(3);
		} catch (IOException e) {
			throw new ServerCommunicationException("Error in communication with the server");
		}
	}

	/**
	 * Ottiene la risposta fornita dal server a seguito dell'indicazione del valore
	 * di un attributo per l'esempio da predire.
	 * 
	 * @return Risposta fornita dal server. Indica le opzioni disponibili per la
	 *         scelta successiva o il valore predetto per l'attributo di classe.
	 * @throws ServerCommunicationException Se si verificano errori nella
	 *                                      comunicazione con il server.
	 */
	public static Object getResponse() throws ServerCommunicationException {
		Object response;
		try {
			String answer = in.readObject().toString();
			if (answer.equals("QUERY") || answer.equals("OK"))
				response = in.readObject();
			else
				throw new ServerCommunicationException(answer);
			return response;
		} catch (ClassNotFoundException | IOException e) {
			throw new ServerCommunicationException("Error in communication with the server");
		}
	}

	/**
	 * Comunica al server il valore di un attributo per l'esempio da predire.
	 * 
	 * @param choise Opzione scelta dall'utente per indicare il valore di un
	 *               attributo dell'esempio da predire.
	 * @throws ServerCommunicationException Se si verificano errori nella
	 *                                      comunicazione con il server.
	 */
	public static void SetResponse(int choise) throws ServerCommunicationException {
		try {
			out.writeObject(choise);
		} catch (IOException e) {
			throw new ServerCommunicationException("Error in communication with the server");
		}
	}

	/**
	 * Richiede al server l'interruzione della connessione.
	 * 
	 * @throws ServerCommunicationException Se si verificano errori nella
	 *                                      comunicazione con il server.
	 */
	public static void requestClosure() throws ServerCommunicationException {
		try {
			out.writeObject(4);
		} catch (IOException e) {
			throw new ServerCommunicationException("Error in communication with the server");
		}
	}

	/**
	 * Rilascia le risorse utilizzate per instaurare una connessione con il server.
	 * 
	 * @throws IOException Se si verificano errori nel rilascio delle risorse.
	 */
	public static void close() throws IOException {
		if (in != null)
			in.close();
		if (out != null)
			out.close();
		if (socket != null)
			socket.close();
	}

}
