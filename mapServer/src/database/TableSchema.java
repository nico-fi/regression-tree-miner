package database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Classe che modella lo schema di una tabella nel database relazionale.
 */
public class TableSchema implements Iterable<Column> {

	/**
	 * Lista contenente le colonne che compongono la tabella.
	 */
	private List<Column> tableSchema = new ArrayList<>();

	/**
	 * Costruttore di classe. Modella la tabella del database specificata in input.
	 * 
	 * @param db        Oggetto DbAccess per l'accesso al database.
	 * @param tableName Nome della tabella da modellare.
	 * @throws SQLException Se si verificano errori nel modellare la tabella.
	 */
	public TableSchema(DbAccess db, String tableName) throws SQLException {
		HashMap<String, String> mapSQL_JAVATypes = new HashMap<>();
		mapSQL_JAVATypes.put("CHAR", "string");
		mapSQL_JAVATypes.put("VARCHAR", "string");
		mapSQL_JAVATypes.put("LONGVARCHAR", "string");
		mapSQL_JAVATypes.put("BIT", "string");
		mapSQL_JAVATypes.put("SHORT", "number");
		mapSQL_JAVATypes.put("INT", "number");
		mapSQL_JAVATypes.put("LONG", "number");
		mapSQL_JAVATypes.put("FLOAT", "number");
		mapSQL_JAVATypes.put("DOUBLE", "number");
		Connection con = db.getConnection();
		DatabaseMetaData meta = con.getMetaData();
		ResultSet res = meta.getColumns(null, null, tableName, null);
		while (res.next())
			if (mapSQL_JAVATypes.containsKey(res.getString("TYPE_NAME")))
				tableSchema.add(
						new Column(res.getString("COLUMN_NAME"), mapSQL_JAVATypes.get(res.getString("TYPE_NAME"))));
		res.close();
	}

	/**
	 * Restituisce la dimensione della lista tableSchema.
	 * 
	 * @return Numero di colonne presenti nella tabella.
	 */
	public int getNumberOfAttributes() {
		return tableSchema.size();
	}

	/**
	 * Restituisce l'elemento di tableSchema corrispondente all'indice specificato
	 * in input.
	 * 
	 * @param index Indice della colonna richiesta.
	 * @return Colonna della tabella corrispondente all'indice specificato in input.
	 */
	Column getColumn(int index) {
		return tableSchema.get(index);
	}

	/**
	 * Restituisce un iteratore sugli elementi della lista tableSchema.
	 * 
	 * @return Iteratore sulle colonne della tabella.
	 */
	public Iterator<Column> iterator() {
		return tableSchema.iterator();
	}

}
