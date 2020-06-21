package data;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

/**
 * Classe che estende Attribute. Rappresenta un attributo discreto.
 */
public class DiscreteAttribute extends Attribute implements Iterable<String> {

	/**
	 * Insieme ordinato di stringhe, contenente i valori discreti che l'attributo
	 * può assumere.
	 */
	private Set<String> values = new TreeSet<>();

	/**
	 * Costruttore di classe. Invoca il costruttore della superclasse e avvalora
	 * l'array values con i valori discreti in input.
	 * 
	 * @param name   Nome simbolico dell'attributo.
	 * @param index  Identificativo numerico dell'attributo.
	 * @param values Valori discreti che l'attributo può assumere.
	 */
	DiscreteAttribute(String name, int index, Set<String> values) {
		super(name, index);
		this.values = values;
	}

	/**
	 * Restituisce la cardinalità dell'insieme values.
	 * 
	 * @return Numero di valori discreti che l'attributo può assumere.
	 */
	int getNumberOfDistinctValues() {
		return values.size();
	}

	/**
	 * Restituisce un iteratore sugli elementi dell'insieme values.
	 * 
	 * @return Iteratore sugli elementi dell'insieme values.
	 */
	public Iterator<String> iterator() {
		return values.iterator();
	}

}
