package tree;

import java.io.Serializable;
import data.Data;

/**
 * Classe che modella un generico nodo, fogliare o intermedio, dell'albero di
 * decisione.
 */
abstract class Node implements Serializable {

	/**
	 * Contatore dei nodi generati nell'albero.
	 */
	private static int idNodeCount = 0;

	/**
	 * Identificativo numerico del nodo.
	 */
	private int idNode;

	/**
	 * Indice nel training set del primo esempio coperto dal nodo corrente.
	 */
	private int beginExampleIndex;

	/**
	 * Indice nel training set dell'ultimo esempio coperto dal nodo corrente.
	 */
	private int endExampleIndex;

	/**
	 * SSE calcolato rispetto all'attributo di classe, nel sottoinsieme di training
	 * coperto dal nodo.
	 * 
	 * @see "https://en.wikipedia.org/wiki/Residual_sum_of_squares"
	 */
	private double variance;

	/**
	 * Costruttore di classe. Avvalora gli attributi, incluso lo SSE calcolato
	 * rispetto all'attributo di classe nel sottoinsieme di training coperto dal
	 * nodo.
	 * 
	 * @param trainingSet       Oggetto di tipo Data contenente il training set
	 *                          completo.
	 * @param beginExampleIndex Indice nel training set del primo esempio coperto
	 *                          dal nodo corrente.
	 * @param endExampleIndex   Indice nel training set dell'ultimo esempio coperto
	 *                          dal nodo corrente.
	 */
	Node(Data trainingSet, int beginExampleIndex, int endExampleIndex) {
		idNode = idNodeCount++;
		this.beginExampleIndex = beginExampleIndex;
		this.endExampleIndex = endExampleIndex;
		double mean = 0;
		for (int i = beginExampleIndex; i <= endExampleIndex; i++)
			mean += trainingSet.getClassValue(i);
		mean /= endExampleIndex - beginExampleIndex + 1;
		variance = 0;
		for (int i = beginExampleIndex; i <= endExampleIndex; i++)
			variance += Math.pow(trainingSet.getClassValue(i) - mean, 2);
	}

	/**
	 * Restituisce il valore del membro idNode.
	 * 
	 * @return Identificativo numerico del nodo.
	 */
	int getIdNode() {
		return idNode;
	}

	/**
	 * Restituisce il valore del membro beginExampleIndex.
	 * 
	 * @return Indice nel training set del primo esempio coperto dal nodo corrente.
	 */
	int getBeginExampleIndex() {
		return beginExampleIndex;
	}

	/**
	 * Restituisce il valore del membro endExampleIndex.
	 * 
	 * @return Indice nel training set dell'ultimo esempio coperto dal nodo
	 *         corrente.
	 */
	int getEndExampleIndex() {
		return endExampleIndex;
	}

	/**
	 * Restituisce il valore del membro variance.
	 * 
	 * @return SSE calcolato rispetto all'attributo di classe, nel sottoinsieme di
	 *         training coperto dal nodo.
	 */
	double getVariance() {
		return variance;
	}

	/**
	 * Restituisce il numero di nodi figli del nodo corrente. L'implementazione
	 * differisce a seconda che si tratti di un nodo foglia o intermedio.
	 * 
	 * @return Numero di nodi figli del nodo corrente.
	 */
	abstract int getNumberOfChildren();

	/**
	 * Restituisce in forma di stringa i valori di beginExampleIndex,
	 * endExampleIndex e variance.
	 */
	public String toString() {
		return "[Examples:" + beginExampleIndex + "-" + endExampleIndex + "] variance:" + variance;
	}

}
