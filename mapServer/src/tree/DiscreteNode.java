package tree;

import data.Data;
import data.Attribute;
import data.DiscreteAttribute;

/**
 * Classe che estende SplitNode per modellare un nodo di split relativo ad un
 * attributo indipendente discreto.
 */
class DiscreteNode extends SplitNode {

	/**
	 * Costruttore di classe.
	 * 
	 * @param trainingSet       Oggetto di tipo Data contenente il training set
	 *                          completo.
	 * @param beginExampleIndex Estremo inferiore del sottoinsieme di training.
	 * @param endExampleIndex   Estremo superiore del sottoinsieme di training.
	 * @param attribute         Attributo indipendente discreto sul quale definire
	 *                          lo split.
	 */
	DiscreteNode(Data trainingSet, int beginExampleIndex, int endExampleIndex, DiscreteAttribute attribute) {
		super(trainingSet, beginExampleIndex, endExampleIndex, attribute);
	}

	/**
	 * Avvalora la lista mapSplit definita in SplitNode, istanziando oggetti
	 * SplitInfo con ciascuno dei valori discreti assunti dall’attributo nel
	 * sottoinsieme di training corrente.
	 * 
	 * @param trainingSet       Oggetto di tipo Data contenente il training set
	 *                          completo.
	 * @param beginExampleIndex Estremo inferiore del sottoinsieme di training.
	 * @param endExampleIndex   Estremo superiore del sottoinsieme di training.
	 * @param attribute         Attributo indipendente sul quale si definisce lo
	 *                          split.
	 */
	void setSplitInfo(Data trainingSet, int beginExampleIndex, int endExampleIndex, Attribute attribute) {
		for (int begin = beginExampleIndex, end = beginExampleIndex + 1; end <= endExampleIndex; end++) {
			Object value = trainingSet.getExplanatoryValue(begin, attribute.getIndex());
			while (end <= endExampleIndex && value.equals(trainingSet.getExplanatoryValue(end, attribute.getIndex())))
				end++;
			getMapSplit().add(new SplitInfo(value, begin, end - 1, getNumberOfChildren()));
			begin = end;
		}
	}

	/**
	 * Confronta il valore in input con l’attributo splitValue di ciascun oggetto
	 * collezionato in mapSplit e restituisce l'identificativo dello split con cui
	 * il test ha esito positivo.
	 * 
	 * @param value Valore discreto dell'attributo che si vuole testare rispetto a
	 *              tutti gli split.
	 * @return Identificativo del ramo di split in mapSplit, con cui il test ha
	 *         esito positivo.
	 */
	int testCondition(Object value) {
		for (SplitInfo i : getMapSplit())
			if (value.equals(i.getSplitValue()))
				return i.getNumberChild();
		return 0;
	}

	/**
	 * Invoca il metodo della superclasse specializzandolo per attributi discreti.
	 */
	public String toString() {
		return "DISCRETE " + super.toString();
	}

}
