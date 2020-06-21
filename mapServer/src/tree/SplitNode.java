package tree;

import java.util.List;
import java.util.ArrayList;
import java.io.Serializable;
import data.Data;
import data.Attribute;

/**
 * Classe che estende Node per modellare un nodo di split, continuo o discreto.
 */
abstract class SplitNode extends Node implements Comparable<SplitNode> {

	/**
	 * Classe che aggrega le informazioni riguardanti un nodo di split.
	 */
	class SplitInfo implements Serializable {

		/**
		 * Valore di attributo indipendente che definisce uno split.
		 */
		private Object splitValue;

		/**
		 * Indice iniziale del sottoinsieme di training relativo allo split corrente.
		 */
		private int beginIndex;

		/**
		 * Indice finale del sottoinsieme di training relativo allo split corrente.
		 */
		private int endIndex;

		/**
		 * Numero identificativo dello split.
		 */
		private int numberChild;

		/**
		 * Operatore matematico che definisce il test nel nodo corrente ("=" per valori
		 * discreti).
		 */
		private String comparator = "=";

		/**
		 * Costruttore di classe per split a valori discreti.
		 * 
		 * @param splitValue  Valore di attributo discreto che definisce uno split.
		 * @param beginIndex  Indice iniziale del sottoinsieme di training relativo allo
		 *                    split corrente.
		 * @param endIndex    Indice finale del sottoinsieme di training relativo allo
		 *                    split corrente.
		 * @param numberChild Numero identificativo dello split.
		 */
		SplitInfo(Object splitValue, int beginIndex, int endIndex, int numberChild) {
			this.splitValue = splitValue;
			this.beginIndex = beginIndex;
			this.endIndex = endIndex;
			this.numberChild = numberChild;
		}

		/**
		 * Costruttore di classe per split generici, da utilizzare per valori continui.
		 * 
		 * @param splitValue  Valore di attributo continuo che definisce uno split.
		 * @param beginIndex  Indice iniziale del sottoinsieme di training relativo allo
		 *                    split corrente.
		 * @param endIndex    Indice finale del sottoinsieme di training relativo allo
		 *                    split corrente.
		 * @param numberChild Numero identificativo dello split.
		 * @param comparator  Operatore matematico che definisce il test nel nodo
		 *                    corrente.
		 */
		SplitInfo(Object splitValue, int beginIndex, int endIndex, int numberChild, String comparator) {
			this.splitValue = splitValue;
			this.beginIndex = beginIndex;
			this.endIndex = endIndex;
			this.numberChild = numberChild;
			this.comparator = comparator;
		}

		/**
		 * Restituisce il valore del membro beginIndex.
		 * 
		 * @return Indice iniziale del sottoinsieme di training relativo allo split
		 *         corrente.
		 */
		int getBeginIndex() {
			return beginIndex;
		}

		/**
		 * Restituisce il valore del membro endIndex.
		 * 
		 * @return Indice finale del sottoinsieme di training relativo allo split
		 *         corrente.
		 */
		int getEndIndex() {
			return endIndex;
		}

		/**
		 * Restituisce il numero identificativo dello split.
		 * 
		 * @return Numero identificativo dello split.
		 */
		int getNumberChild() {
			return numberChild;
		}

		/**
		 * Restituisce il valore del membro splitValue.
		 * 
		 * @return Valore dello split.
		 */
		Object getSplitValue() {
			return splitValue;
		}

		/**
		 * Restituisce il valore del membro comparator.
		 * 
		 * @return Operatore matematico che definisce il test nel nodo corrente.
		 */
		String getComparator() {
			return comparator;
		}

		/**
		 * Restituisce le informazioni riguardanti lo split in forma di stringa.
		 */
		public String toString() {
			return "child " + numberChild + " split value" + comparator + splitValue + "[Examples:" + beginIndex + "-"
					+ endIndex + "]";
		}

	}

	/**
	 * Attributo indipendente sul quale è definito lo split.
	 */
	private Attribute attribute;

	/**
	 * Lista contenente gli split candidati. Ha dimensione pari ai possibili valori
	 * di test.
	 */
	private List<SplitInfo> mapSplit = new ArrayList<>();

	/**
	 * Valore dello SSE a seguito del partizionamento indotto dallo split corrente.
	 */
	private double splitVariance;

	/**
	 * Costruttore di classe. Invoca il costruttore della superclasse e ordina i
	 * valori dell'attributo in input per l'intervallo specificato, al fin di
	 * determinare i possibili split e avvalorare la lista mapSplit. Calcola lo SSE
	 * per l'attributo indicato, come somma degli SSE calcolati su ciascuno
	 * SplitInfo collezionato in mapSplit.
	 * 
	 * @param trainingSet       Oggetto di tipo Data contenente il training set
	 *                          completo.
	 * @param beginExampleIndex Estremo inferiore del sottoinsieme di training.
	 * @param endExampleIndex   Estremo superiore del sottoinsieme di training.
	 * @param attribute         Attributo indipendente sul quale definire lo split.
	 */
	SplitNode(Data trainingSet, int beginExampleIndex, int endExampleIndex, Attribute attribute) {
		super(trainingSet, beginExampleIndex, endExampleIndex);
		this.attribute = attribute;
		trainingSet.sort(attribute, beginExampleIndex, endExampleIndex);
		setSplitInfo(trainingSet, beginExampleIndex, endExampleIndex, attribute);
		splitVariance = 0;
		if (mapSplit != null)
			for (SplitInfo i : mapSplit)
				splitVariance += new LeafNode(trainingSet, i.getBeginIndex(), i.getEndIndex()).getVariance();
	}

	/**
	 * Genera le informazioni necessarie per ciascuno degli split candidati,
	 * memorizzandole in mapSplit.
	 * 
	 * @param trainingSet       Oggetto di tipo Data contenente il training set
	 *                          completo.
	 * @param beginExampelIndex Estremo inferiore del sottoinsieme di training.
	 * @param endExampleIndex   Estremo superiore del sottoinsieme di training.
	 * @param attribute         Attributo indipendente sul quale si definisce lo
	 *                          split.
	 */
	abstract void setSplitInfo(Data trainingSet, int beginExampelIndex, int endExampleIndex, Attribute attribute);

	/**
	 * Modella la condizione di test. Ad ogni valore di test corrisponde un ramo
	 * dello split.
	 * 
	 * @param value Valore dell'attributo che si vuole testare rispetto a tutti gli
	 *              split.
	 * @return Identificativo del ramo di split in mapSplit, con cui il test ha
	 *         esito positivo.
	 */
	abstract int testCondition(Object value);

	/**
	 * Restituisce l'oggetto corrispondente al membro attribute.
	 * 
	 * @return Attributo indipendente sul quale è definito lo split.
	 */
	Attribute getAttribute() {
		return attribute;
	}

	/**
	 * Restituisce l'attributo mapSplit.
	 * 
	 * @return Lista contenente gli split candidati. Ha dimensione pari ai possibili
	 *         valori di test.
	 */
	List<SplitInfo> getMapSplit() {
		return mapSplit;
	}

	/**
	 * Avvalora l'attributo mapSplit.
	 * 
	 * @param mapSplit Lista contenente gli split candidati.
	 */
	void setMapSplit(List<SplitInfo> mapSplit) {
		this.mapSplit = mapSplit;
	}

	/**
	 * Restituisce il valore del membro splitVariance.
	 * 
	 * @return Valore dello SSE a seguito del partizionamento indotto dallo split
	 *         corrente.
	 */
	double getVariance() {
		return splitVariance;
	}

	/**
	 * Restituisce il numero di rami aventi origine nel nodo corrente.
	 * 
	 * @return Numero di figli del nodo corrente.
	 */
	int getNumberOfChildren() {
		return mapSplit != null ? mapSplit.size() : 0;
	}

	/**
	 * Restituisce le informazioni del ramo di mapSplit indicizzato da child.
	 * 
	 * @param child Indice di un elemento di mapSplit.
	 * @return Ramo di mapSplit indicizzato in input.
	 */
	SplitInfo getSplitInfo(int child) {
		return mapSplit.get(child);
	}

	/**
	 * Restituisce attributo, operatore e valore di ciascun test in forma di
	 * stringa. Utile per la predizione di nuovi esempi.
	 * 
	 * @return Stringa contenente attributo, operatore e valore relativi a ciascun
	 *         test.
	 */
	String formulateQuery() {
		String query = "\n";
		for (SplitInfo i : mapSplit)
			query += mapSplit.indexOf(i) + ":\t" + attribute + "\t" + i.getComparator() + "\t" + i.getSplitValue() + "\n";
		return query;
	}

	/**
	 * Restituisce le informazioni relative a ciascun test in forma di stringa.
	 */
	public String toString() {
		String v = "SPLIT : attribute=" + attribute + " Node: " + super.toString() + " Split Variance: " + getVariance()
				+ "\n";
		for (SplitInfo i : mapSplit)
			v += "\t" + i + "\n";
		return v;
	}

	/**
	 * Confronta i valori di splitVariance dei due nodi, restituendo l'esito del
	 * confronto.
	 * 
	 * @param o Nodo di split da confrontare con il nodo corrente.
	 * @return Esito del confronto tra i valori di splitVariance dei nodi: 0 se
	 *         uguali, -1 per gain minore, 1 per gain maggiore.
	 */
	public int compareTo(SplitNode o) {
		return o.splitVariance == splitVariance ? 0 : (o.splitVariance < splitVariance ? 1 : -1);
	}

}
