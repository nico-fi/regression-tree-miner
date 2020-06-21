package graphics;

import java.io.IOException;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

/**
 * Classe controller per gestire la scena iniziale, in cui si specifica la
 * modalità di acquisizione dell'albero di regressione.
 */
public class InitialSceneController {

	/**
	 * Attributo che specifica la modalità di acquisizione dell'albero di
	 * regressione: 1 per apprendimento da database, 2 per caricamento da archivio.
	 */
	private static int decision;

	/**
	 * Tasto per l'apprendimento di un albero di regressione da database.
	 */
	@FXML
	private Button databaseButton;

	/**
	 * Tasto per il caricamento di un albero di regressione da file system.
	 */
	@FXML
	private Button archiveButton;

	/**
	 * Azione associata a databaseButton. Avvalora l'attributo decision e richiede
	 * il caricamento della scena successiva.
	 * 
	 * @throws IOException Se si verificano errori nel caricamento della scena
	 *                     successiva.
	 */
	@FXML
	private void databaseButtonAction() throws IOException {
		decision = 1;
		((Stage) databaseButton.getScene().getWindow())
				.setScene(new Scene(FXMLLoader.load(getClass().getResource("TableNameScene.fxml"))));
	}

	/**
	 * Azione associata a archiveButton. Avvalora l'attributo decision e richiede il
	 * caricamento della scena successiva.
	 * 
	 * @throws IOException Se si verificano errori nel caricamento della scena
	 *                     successiva.
	 */
	@FXML
	private void archiveButtonAction() throws IOException {
		decision = 2;
		((Stage) archiveButton.getScene().getWindow())
				.setScene(new Scene(FXMLLoader.load(getClass().getResource("TableNameScene.fxml"))));
	}

	/**
	 * Restituisce il valore dell'attributo decision.
	 * 
	 * @return Valore che specifica la modalità di acquisizione dell'albero di
	 *         regressione.
	 */
	static int getDecision() {
		return decision;
	}

}