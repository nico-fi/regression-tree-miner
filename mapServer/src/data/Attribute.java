package data;

import java.io.Serializable;

/**
 * Classe che modella un generico attributo, discreto o continuo.
 */
public abstract class Attribute implements Serializable {

	/**
	 * Nome simbolico dell'attributo.
	 */
	private String name;

	/**
	 * Identificativo numerico dell'attributo.
	 */
	private int index;

	/**
	 * Costruttore di classe.
	 * 
	 * @param name  Nome simbolico dell'attributo.
	 * @param index Identificativo numerico dell'attributo.
	 */
	Attribute(String name, int index) {
		this.name = name;
		this.index = index;
	}

	/**
	 * Restituisce il valore del membro name.
	 * 
	 * @return Nome simbolico dell'attributo.
	 */
	String getName() {
		return name;
	}

	/**
	 * Restituisce il valore nel membro index.
	 * 
	 * @return Identificativo numerico dell'attributo.
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * Restituisce il nome dell'attributo in forma di stringa.
	 */
	public String toString() {
		return name;
	}

}
