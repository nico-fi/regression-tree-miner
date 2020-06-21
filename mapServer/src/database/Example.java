package database;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Classe che modella una transazione letta da una tabella del database.
 */
public class Example implements Comparable<Example>, Iterable<Object> {

	/**
	 * Lista di oggetti che compongono la transazione letta.
	 */
	private List<Object> example = new ArrayList<>();

	/**
	 * Aggiunge un oggetto alla transazione corrente.
	 * 
	 * @param o Oggetto da aggiungere alla transazione corrente.
	 */
	void add(Object o) {
		example.add(o);
	}

	/**
	 * Restituisce l'elemento di example specificato dall'indice in input.
	 * 
	 * @param i Indice dell'elemento di example richiesto.
	 * @return Elemento indicizzato in input della transazione corrente.
	 */
	public Object get(int i) {
		return example.get(i);
	}

	/**
	 * Confronta la transazione corrente con la transazione in input, restituendo
	 * l'esito del confronto.
	 * 
	 * @param ex Transazione da confrontare con la transazione corrente.
	 * @return Esito del confronto tra le due transazioni.
	 */
	public int compareTo(Example ex) {
		int i = 0;
		for (Object o : ex.example) {
			if (!o.equals(this.example.get(i)))
				return ((Comparable) o).compareTo(example.get(i));
			i++;
		}
		return 0;
	}

	/**
	 * Restituisce la transazione corrente in forma di stringa.
	 */
	public String toString() {
		String str = "";
		for (Object o : example)
			str += o.toString() + " ";
		return str;
	}

	/**
	 * Restituisce un riferimento nullo.
	 * 
	 * @return Riferimento nullo.
	 */
	public Iterator<Object> iterator() {
		return null;
	}

}