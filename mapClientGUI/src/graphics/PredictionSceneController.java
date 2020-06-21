package graphics;

import java.util.Arrays;
import java.io.IOException;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import client.Client;
import client.ServerCommunicationException;


/**
 * Classe controller per gestire la scena relativa alla predizione
 * dell'attributo di classe per un esempio indicato dall'utente.
 */
public class PredictionSceneController {

	/**
	 * Valore predetto per l'attributo di classe relativo all'esempio indicato
	 * dall'utente.
	 */
	private static double predictedClassValue;

	/**
	 * Oggetto per la selezione di una delle opzioni disponibili per un attributo
	 * relativo all'esempio fornito dall'utente.
	 */
	@FXML
	private ComboBox<String> predictionBox;

	/**
	 * Tasto per l'inserimento dell'opzione scelta.
	 */
	@FXML
	private Button insertButton;

	/**
	 * Avvia la fase di predizione e inizializza la scena corrente.
	 * 
	 * @throws IOException                  Se si verificano errori nella
	 *                                      inizializzazione della scena.
	 * @throws ServerCommunicationException Se si verificano errori nella
	 *                                      comunicazione con il server.
	 */
	@FXML
	private void initialize() throws IOException, ServerCommunicationException {
		Client.requestPrediction();
		visualizeOptions();
	}

	/**
	 * Azione associata a insertButton. Comunica la scelta effettuata dall'utente e
	 * visualizza le nuove opzioni.
	 * 
	 * @throws IOException                  Se si verificano errori nel caricamento
	 *                                      della scena successiva.
	 * @throws ServerCommunicationException Se si verificano errori nella
	 *                                      comunicazione con il server.
	 */
	@FXML
	private void insertButtonAction() throws IOException, ServerCommunicationException {
		Client.SetResponse(Integer.parseInt(predictionBox.getValue().split(":")[0]));
		visualizeOptions();
	}

	/**
	 * Abilita insertButton a seguito della scelta di un'opzione da parte
	 * dell'utente.
	 */
	@FXML
	private void enableInsertButton() {
		insertButton.setDisable(false);
	}

	/**
	 * Visualizza in predictionBox le nuove opzioni a disposizione dell'utente,
	 * disabilitando insertButton. Qualora sia stata ottenuta la predizione del
	 * valore di classe, richiede il caricamento della scena successiva.
	 * 
	 * @throws IOException                  Se si verificano errori nel caricamento
	 *                                      della scena successiva.
	 * @throws ServerCommunicationException Se si verificano errori nella
	 *                                      comunicazione con il server.
	 */
	private void visualizeOptions() throws IOException, ServerCommunicationException {
		Platform.runLater(() -> insertButton.setDisable(true));
		Object response = Client.getResponse();
		if (response instanceof String) {
			String[] options = ((String) response).split("\n");
			predictionBox
					.setItems(FXCollections.observableArrayList(Arrays.copyOfRange(options, 1, options.length - 2)));
		} else {
			predictedClassValue = (double) response;
			((Stage) predictionBox.getScene().getWindow())
					.setScene(new Scene(FXMLLoader.load(getClass().getResource("FinalScene.fxml"))));
		}
	}

	/**
	 * Restituisce il valore dell'attributo predictedClassValue.
	 * 
	 * @return Valore predetto per l'attributo di classe relativo all'esempio
	 *         indicato dall'utente.
	 */
	static double getPredictedClassValue() {
		return predictedClassValue;
	}

}
