package graphics;

import java.io.IOException;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import client.Client;
import client.ServerCommunicationException;

/**
 * Classe controller per gestire la scena relativa all'acquisizione del nome
 * della tabella contenente il training set.
 */
public class TableNameSceneController {

	/**
	 * Campo testuale per l'inserimento del nome della tabella.
	 */
	@FXML
	private TextField tableNameField;

	/**
	 * Tasto per l'inserimento del nome della tabella.
	 */
	@FXML
	private Button startButton;

	/**
	 * Abilita startButton a seguito della digitazione di un carattere nel campo
	 * tableNameField.
	 */
	@FXML
	private void enableStartButton() {
		startButton.setDisable(false);
	}

	/**
	 * Azione associata a startButton. Avvia l'acquisizione dell'albero di
	 * regressione e richiede il caricamento della scena successiva.
	 * 
	 * @throws IOException                  Se si verificano errori nel caricamento
	 *                                      della scena successiva.
	 * @throws ServerCommunicationException Se si verificano errori nella
	 *                                      comunicazione con il server.
	 */
	@FXML
	private void startButtonAction() throws IOException, ServerCommunicationException {
		Client.acquireTree(InitialSceneController.getDecision(), tableNameField.getText());
		((Stage) startButton.getScene().getWindow())
				.setScene(new Scene(FXMLLoader.load(getClass().getResource("PredictionScene.fxml"))));
	}

}