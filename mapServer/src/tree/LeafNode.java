package tree;

import data.Data;

/**
 * Classe che estende Node per modellare un nodo fogliare.
 */
class LeafNode extends Node {

	/**
	 * Valore dell'attributo di classe espresso nella foglia corrente.
	 */
	private Double predictedClassValue;

	/**
	 * Costruttore di classe. Invoca il costruttore della superclasse e avvalora
	 * l'attributo predictedClassValue, calcolando la media dei valori assunti
	 * dall'attributo di classe all'interno della partizione (ovvero il sottoinsieme
	 * di training compreso nell'intervallo specificato in input).
	 * 
	 * @param trainingSet       Oggetto di tipo Data contenente il training set
	 *                          completo.
	 * @param beginExampleIndex Estremo inferiore del sottoinsieme di training
	 *                          coperto dal nodo fogliare.
	 * @param endExampleIndex   Estremo superiore del sottoinsieme di training
	 *                          coperto dal nodo fogliare.
	 */
	LeafNode(Data trainingSet, int beginExampleIndex, int endExampleIndex) {
		super(trainingSet, beginExampleIndex, endExampleIndex);
		double mean = 0;
		for (int i = beginExampleIndex; i <= endExampleIndex; i++)
			mean += trainingSet.getClassValue(i);
		mean /= endExampleIndex - beginExampleIndex + 1;
		predictedClassValue = mean;
	}

	/**
	 * Restituisce il valore del membro predictedClassValue.
	 * 
	 * @return Valore dell'attributo di classe espresso nella foglia corrente.
	 */
	Double getPredictedClassValue() {
		return predictedClassValue;
	}

	/**
	 * Restituisce il numero di split aventi origine nel nodo fogliare, ossia zero.
	 * 
	 * @return Valore costante zero.
	 */
	int getNumberOfChildren() {
		return 0;
	}

	/**
	 * Invoca il metodo della superclasse, specificando il valore dell'attributo di
	 * classe espresso nella foglia.
	 */
	public String toString() {
		return "LEAF : Class=" + predictedClassValue + " Node: " + super.toString();
	}

}
