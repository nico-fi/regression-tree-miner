package tree;

import java.util.TreeSet;
import java.io.Serializable;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import data.*;
import server.UnknownValueException;

/**
 * Classe che modella l'intero albero di decisione come insieme di sottoalberi.
 */
public class RegressionTree implements Serializable {

	/**
	 * Radice del sottoalbero corrente.
	 */
	private Node root;

	/**
	 * Array di sottoalberi aventi radice nel nodo root.
	 */
	private RegressionTree childTree[];

	/**
	 * Costruttore di classe. Istanzia un sottoalbero.
	 */
	RegressionTree() {
	}

	/**
	 * Costruttore di classe. Istanzia un sottoalbero e avvia l'induzione
	 * dell'albero di decisione dagli esempi di training in input.
	 * 
	 * @param trainingSet Oggetto di tipo Data contenente il training set completo.
	 */
	public RegressionTree(Data trainingSet) {
		learnTree(trainingSet, 0, trainingSet.getNumberOfExamples() - 1, trainingSet.getNumberOfExamples() * 10 / 100);
	}

	/**
	 * Verifica se il sottoinsieme corrente può essere coperto da un nodo foglia,
	 * controllando che il numero di esempi compresi tra begin ed end sia minore o
	 * uguale del parametro in input o, alternativamente, se tutti gli esempi
	 * presentano uno stesso valore per l'attributo di classe.
	 * 
	 * @param trainingSet             Oggetto di tipo Data contenente il training
	 *                                set completo.
	 * @param begin                   Indice iniziale del sottoinsieme di training.
	 * @param end                     Indice finale del sottoinsieme di training.
	 * @param numberOfExamplesPerLeaf Numero massimo di esempi che una foglia deve
	 *                                contenere.
	 * @return Esito sulle condizioni richieste dai nodi fogliari.
	 */
	private boolean isLeaf(Data trainingSet, int begin, int end, int numberOfExamplesPerLeaf) {
		if (end - begin + 1 > numberOfExamplesPerLeaf)
			for (int i = begin; i < end; i++)
				if (!trainingSet.getClassValue(i).equals(trainingSet.getClassValue(i + 1)))
					return false;
		return true;
	}

	/**
	 * Istanzia uno SplitNode per ciascun attributo indipendente e seleziona il nodo
	 * di split con SSE minore tra quelli istanziati. Ordina il sottoinsieme di
	 * training corrente rispetto all’attributo corrispondente al nodo selezionato.
	 * 
	 * @param trainingSet Oggetto di tipo Data contenente il training set completo.
	 * @param begin       Indice iniziale del sottoinsieme di training.
	 * @param end         Indice finale del sottoinsieme di training.
	 * @return Miglior nodo di split per il sottoinsieme di training corrente.
	 */
	private SplitNode determineBestSplitNode(Data trainingSet, int begin, int end) {
		TreeSet<SplitNode> ts = new TreeSet<>();
		SplitNode currentNode;
		for (int i = 0; i < trainingSet.getNumberOfExplanatoryAttributes(); i++) {
			Attribute a = trainingSet.getExplanatoryAttribute(i);
			if (a instanceof DiscreteAttribute)
				currentNode = new DiscreteNode(trainingSet, begin, end, (DiscreteAttribute) a);
			else
				currentNode = new ContinuousNode(trainingSet, begin, end, (ContinuousAttribute) a);
			ts.add(currentNode);
		}
		trainingSet.sort(ts.first().getAttribute(), begin, end);
		return ts.first();
	}

	/**
	 * Genera un sottoalbero a partire dal sottoinsieme di training in input,
	 * istanziando un nodo fogliare o un nodo di split. In quest'ultimo caso
	 * determina il miglior nodo rispetto al sottoinsieme corrente, che fungerà da
	 * radice di un sottoalbero avente rami pari al numero di figli determinati
	 * dallo split. Per ciascun oggetto dell'array childTree, si invocherà
	 * ricorsivamente il metodo learnTree() al fin di eseguire l'apprendimento su un
	 * insieme ridotto rispetto all'insieme attuale. Se il nodo di split non genera
	 * figli diviene foglia.
	 * 
	 * @param trainingSet             Oggetto di tipo Data contenente il training
	 *                                set completo.
	 * @param begin                   Indice iniziale del sottoinsieme di training.
	 * @param end                     Indice finale del sottoinsieme di training.
	 * @param numberOfExamplesPerLeaf Numero massimo di esempi che una foglia deve
	 *                                contenere.
	 */
	void learnTree(Data trainingSet, int begin, int end, int numberOfExamplesPerLeaf) {
		if (isLeaf(trainingSet, begin, end, numberOfExamplesPerLeaf))
			root = new LeafNode(trainingSet, begin, end);
		else {
			root = determineBestSplitNode(trainingSet, begin, end);
			if (root.getNumberOfChildren() > 1) {
				childTree = new RegressionTree[root.getNumberOfChildren()];
				for (int i = 0; i < root.getNumberOfChildren(); i++) {
					childTree[i] = new RegressionTree();
					childTree[i].learnTree(trainingSet, ((SplitNode) root).getSplitInfo(i).getBeginIndex(),
							((SplitNode) root).getSplitInfo(i).getEndIndex(), numberOfExamplesPerLeaf);
				}
			} else
				root = new LeafNode(trainingSet, begin, end);
		}
	}

	/**
	 * Stampa a video le informazioni dell'intero albero inserendo un'opportuna
	 * intestazione.
	 */
	void printTree() {
		System.out.println("********* TREE **********\n");
		System.out.println(toString());
		System.out.println("*************************\n");
	}

	/**
	 * Concatena in una stringa le informazioni del nodo root e dell'array
	 * childTree. Qualora root sia un nodo di split vengono concatenate anche le
	 * informazioni dei rami.
	 */
	public String toString() {
		String tree = root.toString() + "\n";
		if (root instanceof SplitNode)
			for (int i = 0; i < childTree.length; i++)
				tree += childTree[i];
		return tree;
	}

	/**
	 * Scandisce ciascun ramo dell'albero, concatenando le informazioni dei nodi di
	 * split fino al nodo foglia. Visualizza le regole ottenute aggiungendo
	 * un'opportuna intestazione.
	 * 
	 * @see printRules(String current)
	 */
	void printRules() {
		System.out.println("********* RULES *********\n");
		if (root instanceof SplitNode)
			for (int i = 0; i < childTree.length; i++)
				childTree[i].printRules(
						((SplitNode) root).getAttribute() + ((SplitNode) root).getSplitInfo(i).getComparator()
								+ ((SplitNode) root).getSplitInfo(i).getSplitValue());
		else
			System.out.println("Class=" + ((LeafNode) root).getPredictedClassValue());
		System.out.println("*************************\n");
	}

	/**
	 * Supporta il metodo printRules(), concatenando alle informazioni del nodo
	 * precedente quelle relative alla radice del sottoalbero corrente. Se si tratta
	 * di un nodo di split il metodo viene invocato ricorsivamente, altrimenti, in
	 * caso di nodo fogliare, viene visualizzata l'intera informazione generata.
	 * 
	 * @param current Informazioni del nodo di split relativo al sottoalbero di
	 *                livello superiore.
	 * @see printRules()
	 */
	private void printRules(String current) {
		if (root instanceof SplitNode)
			for (int i = 0; i < childTree.length; i++)
				childTree[i].printRules(current + " AND " + ((SplitNode) root).getAttribute()
						+ ((SplitNode) root).getSplitInfo(i).getComparator()
						+ ((SplitNode) root).getSplitInfo(i).getSplitValue());
		else
			System.out.println(current + " ==> Class=" + ((LeafNode) root).getPredictedClassValue());
	}

	/**
	 * Comunica al client le informazioni di ciascuno split dell'albero e per il
	 * corrispondente attributo acquisisce il valore dell'esempio da
	 * predire. Se la radice corrente è un nodo foglia, viene restituita la
	 * predizione per l’attributo di classe, altrimenti il metodo è invocato
	 * ricorsivamente sul nodo figlio individuato dal valore acquisito.
	 * 
	 * @param in  Oggetto per la gestione delle operazioni di lettura dal client.
	 * @param out Oggetto per la gestione delle operazioni di scrittura verso il
	 *            client.
	 * @return Valore predetto per l'attributo di classe dell'esempio acquisito.
	 * @throws UnknownValueException  Se il valore acquisito non consente
	 *                                di selezionare un ramo valido del nodo di
	 *                                split.
	 * @throws IOException            Se si verificano errori di input-output.
	 * @throws ClassNotFoundException Se il tipo del valore letto non è noto.
	 */
	public Double predictClass(ObjectInputStream in, ObjectOutputStream out)
			throws UnknownValueException, IOException, ClassNotFoundException {
		if (root instanceof LeafNode)
			return ((LeafNode) root).getPredictedClassValue();
		else {
			out.writeObject("QUERY");
			out.writeObject(((SplitNode) root).formulateQuery() + "\nInsert value: ");
			int risp = (int) in.readObject();
			if (risp < 0 || risp >= root.getNumberOfChildren())
				throw new UnknownValueException(
						"The answer should be an integer between 0 and " + (root.getNumberOfChildren() - 1));
			else
				return childTree[risp].predictClass(in, out);
		}
	}

	/**
	 * Serializza l'albero di regressione all'interno del file specificato in input.
	 * 
	 * @param nomeFile Nome del file in cui salvare l'albero.
	 * @throws IOException Se si verificano errori nella creazione del file
	 *                     contenente l'albero di regressione.
	 */
	public void salva(String nomeFile) throws IOException {
		ObjectOutputStream out = null;
		try {
			out = new ObjectOutputStream(new FileOutputStream(nomeFile));
			out.writeObject(this);
		} finally {
			if (out != null)
				out.close();
		}
	}

	/**
	 * Carica l'albero di regressione conservato nel file specificato in input.
	 * 
	 * @param nomeFile Nome del file in cui è salvato l'albero.
	 * @return Albero di regressione contenuto nel file.
	 * @throws IOException            Se si verificano errori nel caricamento del
	 *                                file contenente l'albero di regressione.
	 * @throws ClassNotFoundException Se la classe dell'oggetto serializzato non
	 *                                viene trovata.
	 */
	public static RegressionTree carica(String nomeFile) throws IOException, ClassNotFoundException {
		ObjectInputStream in = null;
		try {
			in = new ObjectInputStream(new FileInputStream(nomeFile));
			RegressionTree tree = (RegressionTree) in.readObject();
			return tree;
		} finally {
			if (in != null)
				in.close();
		}
	}

}
