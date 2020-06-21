package tree;

import java.util.ArrayList;
import java.util.List;
import data.Attribute;
import data.ContinuousAttribute;
import data.Data;

/**
 * Classe che estende SplitNode per modellare un nodo di split relativo ad un
 * attributo indipendente continuo.
 */
class ContinuousNode extends SplitNode {

	/**
	 * Costruttore di classe.
	 * 
	 * @param trainingSet       Oggetto di tipo Data contenente il training set
	 *                          completo.
	 * @param beginExampleIndex Estremo inferiore del sottoinsieme di training.
	 * @param endExampleIndex   Estremo superiore del sottoinsieme di training.
	 * @param attribute         Attributo indipendente continuo sul quale definire
	 *                          lo split.
	 */
	ContinuousNode(Data trainingSet, int beginExampleIndex, int endExampleIndex, ContinuousAttribute attribute) {
		super(trainingSet, beginExampleIndex, endExampleIndex, attribute);
	}

	/**
	 * Avvalora la lista mapSplit definita in SplitNode, in relazione ad attributo
	 * continuo e sottoinsieme di training correnti.
	 * 
	 * @param trainingSet       Oggetto di tipo Data contenente il training set
	 *                          completo.
	 * @param beginExampleIndex Estremo inferiore del sottoinsieme di training.
	 * @param endExampleIndex   Estremo superiore del sottoinsieme di training.
	 * @param attribute         Attributo indipendente sul quale si definisce lo
	 *                          split.
	 */
	void setSplitInfo(Data trainingSet, int beginExampleIndex, int endExampleIndex, Attribute attribute) {
		Double currentSplitValue = (Double) trainingSet.getExplanatoryValue(beginExampleIndex, attribute.getIndex());
		double bestInfoVariance = 0;
		List<SplitInfo> bestMapSplit = null;
		for (int i = beginExampleIndex + 1; i <= endExampleIndex; i++) {
			Double value = (Double) trainingSet.getExplanatoryValue(i, attribute.getIndex());
			if (value.doubleValue() != currentSplitValue.doubleValue()) {
				double candidateSplitVariance = new LeafNode(trainingSet, beginExampleIndex, i - 1).getVariance();
				candidateSplitVariance += new LeafNode(trainingSet, i, endExampleIndex).getVariance();
				if (bestMapSplit == null) {
					bestMapSplit = new ArrayList<SplitInfo>();
					bestMapSplit.add(new SplitInfo(currentSplitValue, beginExampleIndex, i - 1, 0, "<="));
					bestMapSplit.add(new SplitInfo(currentSplitValue, i, endExampleIndex, 1, ">"));
					bestInfoVariance = candidateSplitVariance;
				} else if (candidateSplitVariance < bestInfoVariance) {
					bestInfoVariance = candidateSplitVariance;
					bestMapSplit.set(0, new SplitInfo(currentSplitValue, beginExampleIndex, i - 1, 0, "<="));
					bestMapSplit.set(1, new SplitInfo(currentSplitValue, i, endExampleIndex, 1, ">"));
				}
				currentSplitValue = value;
			}
		}
		setMapSplit(bestMapSplit);
		if (getMapSplit() != null && (getSplitInfo(1).getBeginIndex() == getSplitInfo(1).getEndIndex()))
			getMapSplit().remove(1);
	}

	/**
	 * Confronta il valore in input con l’attributo splitValue degli SplitInfo
	 * collezionati in mapSplit e restituisce l'identificativo dello split con cui
	 * il test ha esito positivo.
	 * 
	 * @param value Valore continuo dell'attributo che si vuole testare.
	 * @return Identificativo del ramo di split in mapSplit, con cui il test ha
	 *         esito positivo.
	 */
	int testCondition(Object value) {
		if ((Double) value > (Double) getSplitInfo(0).getSplitValue())
			return 1;
		return 0;
	}

	/**
	 * Invoca il metodo della superclasse specializzandolo per attributi continui.
	 */
	public String toString() {
		return "CONTINUOUS " + super.toString();
	}

}
