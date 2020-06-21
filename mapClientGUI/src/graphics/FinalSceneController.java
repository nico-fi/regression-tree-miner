package graphics;

import java.io.IOException;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import client.Client;
import client.ServerCommunicationException;

/**
 * Classe controller per gestire la scena finale, in cui si visualizza la
 * predizione dell'attributo di classe, consentendo all'utente di eseguire una
 * nuova predizione o terminare il programma.
 */
public class FinalSceneController {

	/**
	 * Label contenente il valore predetto per l'attributo di classe.
	 */
	@FXML
	private Label classValueLabel;

	/**
	 * Tasto per avviare una nuova predizione dell'attributo di classe.
	 */
	@FXML
	private Button newPredictionButton;

	/**
	 * Tasto per richiedere la terminazione del programma.
	 */
	@FXML
	private Button exitButton;

	/**
	 * Inizializza la scenza corrente, visualizzando la predizione dell'attributo di
	 * classe per l'esempio immesso dall'utente.
	 */
	@FXML
	private void initialize() {
		classValueLabel.setText(String.valueOf(PredictionSceneController.getPredictedClassValue()));
	}

	/**
	 * Azione associata a newPredictionButton. Richiede il caricamento della scena
	 * relativa alla predizione dell'attributo di classe.
	 * 
	 * @throws IOException Se si verificano errori nel caricamento della scena
	 *                     successiva.
	 */
	@FXML
	private void newPredictionButtonAction() throws IOException {
		((Stage) newPredictionButton.getScene().getWindow())
				.setScene(new Scene(FXMLLoader.load(getClass().getResource("PredictionScene.fxml"))));
	}

	/**
	 * Richiede l'interruzione della connessione con il server e la terminazione del
	 * programma.
	 * 
	 * @throws ServerCommunicationException Se si verificano errori nella
	 *                                      comunicazione con il server.
	 */
	@FXML
	private void exitButtonAction() throws ServerCommunicationException {
		Client.requestClosure();
		((Stage) exitButton.getScene().getWindow()).close();
	}

}
