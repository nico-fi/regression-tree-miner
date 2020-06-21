package mapClient;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import utility.Keyboard;

/**
 * Classe per stabilire la connessione del client al server, al fine di avviare
 * l'apprendimento di un nuovo albero di regressione o recuperare un albero
 * precedentemente serializzato in un file.
 */
public class MainTest {

	/**
	 * Stabilisce la connessione al server. Successivamente invia e riceve messaggi
	 * dipendentemente dalle scelte effettuate dall'utente.
	 * 
	 * @param args Indirizzo e porta su cui il server è in ascolto.
	 */
	public static void main(String[] args) {
		ObjectInputStream in = null;
		ObjectOutputStream out = null;
		Socket socket = null;
		if(args.length < 2) {
			System.out.println("IP address and port number not provided");
			return;
		}
		try {
			InetAddress addr = InetAddress.getByName(args[0]);
			socket = new Socket(addr, Integer.parseInt(args[1]));
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
			String answer;
			int decision;
			do {
				System.out.println("[1]: Learn Regression Tree from data");
				System.out.println("[2]: Load Regression Tree from archive");
				decision = Keyboard.readInt();
			} while (decision != 1 && decision != 2);
			System.out.print("\nTable name: ");
			String tableName = Keyboard.readString();
			if (decision == 1) {
				System.out.println("\nStarting data acquisition phase!");
				out.writeObject(0);
				out.writeObject(tableName);
				answer = in.readObject().toString();
				if (!answer.equals("OK")) {
					System.out.println(answer);
					return;
				}
				System.out.println("\nStarting learning phase!");
				out.writeObject(1);
			} else {
				out.writeObject(2);
				out.writeObject(tableName);
			}
			answer = in.readObject().toString();
			if (!answer.equals("OK")) {
				System.out.println(answer);
				return;
			}
			char risp;
			do {
				out.writeObject(3);
				System.out.println("\nStarting prediction phase!");
				answer = in.readObject().toString();
				while (answer.equals("QUERY")) {
					answer = in.readObject().toString();
					System.out.print(answer);
					int path = Keyboard.readInt();
					out.writeObject(path);
					answer = in.readObject().toString();
				}
				if (answer.equals("OK")) {
					answer = in.readObject().toString();
					System.out.println("\nPredicted class:" + answer);
				} else
					System.out.println(answer);
					System.out.print("\nStart a new prediction? [y/n]: ");
					risp = Keyboard.readChar();
			} while (Character.toUpperCase(risp) == 'Y');
			out.writeObject(4);
		} catch (UnknownHostException e) {
			System.out.println(e);
		} catch (IOException | ClassNotFoundException e) {
			System.out.println(e);
		} finally {
			try {
				if (in != null)
					in.close();
				if (out != null)
					out.close();
				if (socket != null)
					socket.close();
			} catch (IOException e) {
				System.out.println(e);
			}
		}
	}

}
