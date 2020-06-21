package data;

import java.util.List;
import java.util.TreeSet;
import java.util.LinkedList;
import java.util.ArrayList;
import java.sql.SQLException;
import database.*;

/**
 * Classe che modella il training set.
 */
public class Data {

	/**
	 * Lista degli oggetti Example che compongono il training set.
	 */
	private List<Example> data = new ArrayList<Example>();

	/**
	 * Cardinalità del training set, ossia dimensione della lista data.
	 */
	private int numberOfExamples;

	/**
	 * Lista di oggetti Attribute contenente gli attributi indipendenti.
	 */
	private List<Attribute> explanatorySet = new LinkedList<>();

	/**
	 * Oggetto che modella l'attributo di classe o target. L'attributo di classe è
	 * un attributo numerico.
	 */
	private ContinuousAttribute classAttribute;

	/**
	 * Costruttore di classe. Avvalora la lista explanatorySet con gli attributi
	 * indipendenti e il membro classAttribute con il target. Inserisce gli esempi
	 * di training contenuti nella tabella specificata in input all'interno della
	 * lista data.
	 * 
	 * @param tableName Nome della tabella contenente il training set.
	 * @throws TrainingDataException Se si verificano errori nell'acquisizione del
	 *                               training set: la connessione al database
	 *                               fallisce, la tabella è inesistente, ha meno di
	 *                               due colonne o ha zero tuple, l’attributo
	 *                               corrispondente all’ultima colonna non è
	 *                               numerico.
	 */
	public Data(String tableName) throws TrainingDataException {
		DbAccess db = new DbAccess();
		try {
			db.initConnection();
			TableData tData = new TableData(db);
			TableSchema tSchema = new TableSchema(db, tableName);
			if (tSchema.getNumberOfAttributes() == 0)
				throw new TrainingDataException("Table does not exist in the database");
			if (tSchema.getNumberOfAttributes() < 2)
				throw new TrainingDataException("Less than two columns in the table");
			Attribute currentAttribute;
			for (Column c : tSchema) {
				if (c.isNumber())
					currentAttribute = new ContinuousAttribute(c.getColumnName(), explanatorySet.size());
				else {
					TreeSet<String> values = new TreeSet<>();
					tData.getDistinctColumnValues(tableName, c).forEach(i -> values.add((String) i));
					currentAttribute = new DiscreteAttribute(c.getColumnName(), explanatorySet.size(), values);
				}
				if (explanatorySet.size() != tSchema.getNumberOfAttributes() - 1)
					explanatorySet.add(currentAttribute);
				else if (c.isNumber())
					classAttribute = (ContinuousAttribute) currentAttribute;
				else
					throw new TrainingDataException("Class attribute is not numeric");
			}
			data = tData.getTransazioni(tableName);
			numberOfExamples = data.size();
		} catch (DatabaseConnectionException e) {
			throw new TrainingDataException("Database connection failed");
		} catch (EmptySetException e) {
			throw new TrainingDataException("Empty table");
		} catch (SQLException e) {
			throw new TrainingDataException("Error in data acquisition");
		} finally {
			try {
				if (db.getConnection() != null)
					db.closeConnection();
			} catch (SQLException e) {
				throw new TrainingDataException("Unable to close database connection");
			}
		}
	}

	/**
	 * Restituisce il valore del membro numberOfExamples.
	 * 
	 * @return Cardinalità del training set.
	 */
	public int getNumberOfExamples() {
		return numberOfExamples;
	}

	/**
	 * Restituisce la dimensione della lista explanatorySet.
	 * 
	 * @return Numero degli attributi indipendenti.
	 */
	public int getNumberOfExplanatoryAttributes() {
		return explanatorySet.size();
	}

	/**
	 * Restituisce il valore dell'attributo di classe per l'esempio corrispondente
	 * al parametro di input.
	 * 
	 * @param exampleIndex Indice di data relativo ad uno specifico esempio.
	 * @return Valore dell'attributo di classe per l'esempio indicizzato in input.
	 */
	public Double getClassValue(int exampleIndex) {
		return (Double) data.get(exampleIndex).get(classAttribute.getIndex());
	}

	/**
	 * Restituisce il valore dell'attributo indipendente indicizzato da
	 * attributeIndex, relativo all'esempio exampleIndex.
	 * 
	 * @param exampleIndex   Indice di data relativo ad uno specifico esempio.
	 * @param attributeIndex Indice di uno specifico attributo indipendente.
	 * @return Valore dell'attributo indicizzato da attributeIndex, relativo
	 *         all'esempio exampleIndex.
	 */
	public Object getExplanatoryValue(int exampleIndex, int attributeIndex) {
		return data.get(exampleIndex).get(attributeIndex);
	}

	/**
	 * Restituisce l'attributo indipendente contenuto in explanatorySet e
	 * indicizzato in input.
	 * 
	 * @param index Indice di uno specifico attributo indipendente in
	 *              explanatorySet.
	 * @return Attributo indipendente di explanatorySet, indicizzato in input.
	 */
	public Attribute getExplanatoryAttribute(int index) {
		return explanatorySet.get(index);
	}

	/**
	 * Restituisce l'oggetto di tipo ContinuousAttribute, corrispondente
	 * all'attributo di classe.
	 * 
	 * @return Oggetto di tipo ContinuousAttribute associato al membro
	 *         classAttribute.
	 */
	ContinuousAttribute getClassAttribute() {
		return classAttribute;
	}

	/**
	 * Restituisce in forma di stringa i valori di ciascun attributo per tutti gli
	 * esempi presenti nella lista data.
	 */
	public String toString() {
		String value = "Attributes:";
		for (Attribute a : explanatorySet)
			value += a.getName() + ",";
		value += classAttribute.getName() + "\n";
		for (int i = 0; i < numberOfExamples; i++) {
			for (Attribute a : explanatorySet)
				value += data.get(i).get(explanatorySet.indexOf(a)) + ",";
			value += data.get(i).get(explanatorySet.size()) + "\n";
		}
		return value;
	}

	/**
	 * Ordina il sottoinsieme di esempi del training set, compresi nell'intervallo
	 * indicato e rispetto all'attributo specificato in input.
	 * 
	 * @param attribute         Attributo rispetto al quale ordinare i valori del
	 *                          sottoinsieme del training set.
	 * @param beginExampleIndex Estremo inferiore che delimita il sottoinsieme del
	 *                          training set.
	 * @param endExampleIndex   Estremo superiore che delimita il sottoinsieme del
	 *                          training set.
	 */
	public void sort(Attribute attribute, int beginExampleIndex, int endExampleIndex) {
		quicksort(attribute, beginExampleIndex, endExampleIndex);
	}

	/**
	 * Algoritmo quicksort per l'ordinamento di un array di interi. Utilizza &le;
	 * come relazione di ordinamento totale.
	 * 
	 * @param attribute Attributo rispetto al quale ordinare i valori del
	 *                  sottoinsieme del training set.
	 * @param inf       Estremo inferiore che delimita il sottoinsieme del training
	 *                  set.
	 * @param sup       Estremo superiore che delimita il sottoinsieme del training
	 *                  set.
	 * @see sort
	 */
	private void quicksort(Attribute attribute, int inf, int sup) {
		if (sup >= inf) {
			int pos;
			if (attribute instanceof DiscreteAttribute)
				pos = partition((DiscreteAttribute) attribute, inf, sup);
			else
				pos = partition((ContinuousAttribute) attribute, inf, sup);
			if ((pos - inf) < (sup - pos + 1)) {
				quicksort(attribute, inf, pos - 1);
				quicksort(attribute, pos + 1, sup);
			} else {
				quicksort(attribute, pos + 1, sup);
				quicksort(attribute, inf, pos - 1);
			}
		}
	}

	/**
	 * Metodo ausiliario di quicksort, specifico per attributi discreti. Partiziona
	 * il vettore e restituisce il punto di separazione.
	 * 
	 * @param attribute Attributo discreto rispetto al quale ordinare i valori del
	 *                  sottoinsieme del training set.
	 * @param inf       Estremo inferiore che delimita il sottoinsieme del training
	 *                  set.
	 * @param sup       Estremo superiore che delimita il sottoinsieme del training
	 *                  set.
	 * @return Punto di separazione individuato nel vettore.
	 * @see quicksort
	 */
	private int partition(DiscreteAttribute attribute, int inf, int sup) {
		int i = inf;
		int j = sup;
		int med = (inf + sup) / 2;
		String x = (String) getExplanatoryValue(med, attribute.getIndex());
		swap(inf, med);
		while (true) {
			while (i <= sup && ((String) getExplanatoryValue(i, attribute.getIndex())).compareTo(x) <= 0)
				i++;
			while (((String) getExplanatoryValue(j, attribute.getIndex())).compareTo(x) > 0)
				j--;
			if (i < j)
				swap(i, j);
			else
				break;
		}
		swap(inf, j);
		return j;
	}

	/**
	 * Metodo ausiliario di quicksort, specifico per attributi continui. Partiziona
	 * il vettore e restituisce il punto di separazione.
	 * 
	 * @param attribute Attributo continuo rispetto al quale ordinare i valori del
	 *                  sottoinsieme del training set.
	 * @param inf       Estremo inferiore che delimita il sottoinsieme del training
	 *                  set.
	 * @param sup       Estremo superiore che delimita il sottoinsieme del training
	 *                  set.
	 * @return Punto di separazione individuato nel vettore.
	 * @see quicksort
	 */
	private int partition(ContinuousAttribute attribute, int inf, int sup) {
		int i = inf;
		int j = sup;
		int med = (inf + sup) / 2;
		Double x = (Double) getExplanatoryValue(med, attribute.getIndex());
		swap(inf, med);
		while (true) {
			while (i <= sup && ((Double) getExplanatoryValue(i, attribute.getIndex())).compareTo(x) <= 0)
				i++;
			while (((Double) getExplanatoryValue(j, attribute.getIndex())).compareTo(x) > 0)
				j--;
			if (i < j)
				swap(i, j);
			else
				break;
		}
		swap(inf, j);
		return j;
	}

	/**
	 * Scambia gli esempi nelle posizioni i e j del training set.
	 * 
	 * @param i Posizione dell'esempio del training set a cui assegnare l'indice j.
	 * @param j Posizione dell'esempio del training set a cui assegnare l'indice i.
	 */
	private void swap(int i, int j) {
		Example temp = data.get(i);
		data.set(i, data.get(j));
		data.set(j, temp);
	}

}
