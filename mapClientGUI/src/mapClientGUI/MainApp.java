package mapClientGUI;

import java.io.IOException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.fxml.FXMLLoader;
import client.Client;

/**
 * Classe che stabilisce una connessione del client al server, al fine di
 * avviare l'apprendimento di un nuovo albero di regressione o caricare un
 * albero archiviato su file system. Tutte le operazioni sono svolte con il
 * supporto di un'interfaccia grafica. Eventuali eccezioni sono comunicate
 * all'utente mediante finestre di errore.
 */
public class MainApp extends Application {

	/**
	 * Instaura la connessione con il server e avvia l'interfaccia grafica.
	 * 
	 * @param args null
	 */
	public static void main(String[] args) {
		try {
			Client.startConnection("localhost", 8080);
			launch();
		} catch (IOException e) {
			showErrorDialog(e);
		} finally {
			try {
				Client.close();
			} catch (IOException e) {
				showErrorDialog(e);
			}
		}
	}

	/**
	 * Inizializza l'interfaccia, caricando la scena iniziale all'interno dello
	 * stage.
	 */
	public void start(Stage primaryStage) {
		Thread.currentThread().setUncaughtExceptionHandler((thread, exception) -> {
			primaryStage.close();
			showErrorDialog(exception);
		});
		try {
			primaryStage.setTitle("Data Mining");
			primaryStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/graphics/InitialScene.fxml"))));
			primaryStage.show();
		} catch (Exception e) {
			showErrorDialog(e);
		}
	}

	/**
	 * Mostra una finestra di errore, indicando la causa radice che ha generato
	 * l'anomalia.
	 * 
	 * @param exception Anomalia che impedisce la corretta esecuzione del programma.
	 */
	private static void showErrorDialog(Throwable exception) {
		Platform.runLater(() -> {
			Throwable e = exception;
			while (e.getCause() != null)
				e = e.getCause();
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error Dialog");
			alert.setHeaderText("An error has occurred!");
			alert.setContentText(e.getMessage());
			alert.showAndWait();
		});
	}

}
