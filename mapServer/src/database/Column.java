package database;

/**
 * Classe che modella una colonna di una tabella nel database relazionale.
 */
public class Column {

	/**
	 * Nome dell'attributo contenuto nella colonna.
	 */
	private String name;

	/**
	 * Tipo dell'attributo contenuto nella colonna.
	 */
	private String type;

	/**
	 * Costruttore di classe.
	 * 
	 * @param name Nome dell'attributo contenuto nella colonna.
	 * @param type Tipo dell'attributo contenuto nella colonna.
	 */
	Column(String name, String type) {
		this.name = name;
		this.type = type;
	}

	/**
	 * Restituisce il valore del membro name;
	 * 
	 * @return Nome dell'attributo contenuto nella colonna.
	 */
	public String getColumnName() {
		return name;
	}

	/**
	 * Verifica se la colonna contiene un attributo numerico.
	 * 
	 * @return Booleano che specifica se l'attributo della colonna è di tipo
	 *         numerico.
	 */
	public boolean isNumber() {
		return type.equals("number");
	}

	/**
	 * Restituisce nome e tipo dell'attributo relativo alla colonna corrente, in
	 * forma di stringa.
	 */
	public String toString() {
		return name + ":" + type;
	}

}
