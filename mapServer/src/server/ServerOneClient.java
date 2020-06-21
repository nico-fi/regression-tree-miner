package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import data.Data;
import data.TrainingDataException;
import tree.RegressionTree;

/**
 * Classe che estende Thread per gestire le richieste provenienti da un client.
 */
class ServerOneClient extends Thread {

	/**
	 * Oggetto Socket per instaurare una connessione con il client.
	 */
	private Socket socket;

	/**
	 * Oggetto per la gestione delle operazioni di lettura dal client.
	 */
	private ObjectInputStream in;

	/**
	 * Oggetto per la gestione delle operazioni di scrittura verso il client.
	 */
	private ObjectOutputStream out;

	/**
	 * Costruttore di classe. Inizializza gli attributi socket, in e out. Avvia il
	 * thread.
	 * 
	 * @param s Oggetto Socket per instaurare una connessione con il client.
	 * @throws IOException Se si verificano errori di input-output nella
	 *                     comunicazione con il client.
	 */
	ServerOneClient(Socket s) throws IOException {
		socket = s;
		in = new ObjectInputStream(socket.getInputStream());
		out = new ObjectOutputStream(socket.getOutputStream());
		start();
	}

	/**
	 * Ridefinisce il metodo run() della classe Thread al fine di gestire le
	 * richieste del client.
	 */
	public void run() {
		String tableName = null;
		Data trainingSet = null;
		RegressionTree tree = null;
		try {
			while (true) {
				int answer = (int) in.readObject();
				if (answer == 0) {
					tableName = (String) in.readObject();
					try {
						trainingSet = new Data(tableName);
					} catch (TrainingDataException e) {
						out.writeObject(e.toString());
						return;
					}
					out.writeObject("OK");
				} else if (answer == 1) {
					tree = new RegressionTree(trainingSet);
					try {
						tree.salva(tableName + ".dmp");
					} catch (IOException e) {
						out.writeObject(e.toString());
						return;
					}
					out.writeObject("OK");
				} else if (answer == 2) {
					tableName = (String) in.readObject();
					try {
						tree = RegressionTree.carica(tableName + ".dmp");
					} catch (ClassNotFoundException | IOException e) {
						out.writeObject(e.toString());
						return;
					}
					out.writeObject("OK");
				} else if (answer == 3)
					try {
						double prediction = tree.predictClass(in, out);
						out.writeObject("OK");
						out.writeObject(prediction);
					} catch (UnknownValueException e) {
						out.writeObject(e.toString());
					}
				else if (answer == 4)
					break;
			}
		} catch (IOException | ClassNotFoundException e) {
			System.out.println(e);
		} finally {
			try {
				in.close();
				out.close();
				socket.close();
			} catch (IOException e) {
				System.out.println(e);
			}
		}
	}

}
