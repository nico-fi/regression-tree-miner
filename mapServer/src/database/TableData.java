package database;

import java.util.List;
import java.util.Set;
import java.util.LinkedList;
import java.util.TreeSet;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Classe che modella l’insieme di transazioni collezionate in una tabella del
 * database.
 */
public class TableData {

	/**
	 * Attributo DbAccess per l'accesso alla base di dati.
	 */
	private DbAccess db;

	/**
	 * Costruttore di classe. Avvalora il membro db.
	 * 
	 * @param db Oggetto DbAccess per l'accesso alla base di dati.
	 */
	public TableData(DbAccess db) {
		this.db = db;
	}

	/**
	 * Ricava lo schema della tabella specificata in input ed esegue una
	 * interrogazione per estrarre le tuple distinte dalla tabella. Per ogni tupla
	 * del resultset si crea un oggetto Example, il cui riferimento è inserito nella
	 * lista restituita in output.
	 * 
	 * @param table Nome della tabella nel database.
	 * @return Lista di transazioni collezionate nella tabella.
	 * @throws SQLException      Se si verificano errori nell'esecuzione della
	 *                           query.
	 * @throws EmptySetException Se il resultset è vuoto.
	 */
	public List<Example> getTransazioni(String table) throws SQLException, EmptySetException {
		LinkedList<Example> transSet = new LinkedList<>();
		TableSchema tSchema = new TableSchema(db, table);
		String query = "SELECT ";
		for (int i = 0; i < tSchema.getNumberOfAttributes(); i++) {
			Column c = tSchema.getColumn(i);
			if (i > 0)
				query += ",";
			query += c.getColumnName();
		}
		if (tSchema.getNumberOfAttributes() == 0)
			throw new SQLException();
		query += (" FROM " + table);
		Statement statement = db.getConnection().createStatement();
		ResultSet rs = statement.executeQuery(query);
		boolean empty = true;
		while (rs.next()) {
			empty = false;
			Example currentTuple = new Example();
			for (int i = 0; i < tSchema.getNumberOfAttributes(); i++)
				if (tSchema.getColumn(i).isNumber())
					currentTuple.add(rs.getDouble(i + 1));
				else
					currentTuple.add(rs.getString(i + 1));
			transSet.add(currentTuple);
		}
		rs.close();
		statement.close();
		if (empty)
			throw new EmptySetException();
		return transSet;
	}

	/**
	 * Esegue una interrogazione SQL per estrarre i valori distinti e ordinati di
	 * column, restituendoli in forma di insieme.
	 * 
	 * @param table  Nome della tabella nel database.
	 * @param column Nome della colonna nella tabella.
	 * @return Insieme di valori distinti, ordinati in modo ascendente, che
	 *         l’attributo identificato da column assume nella tabella specificata.
	 * @throws SQLException Se si verificano errori nell'esecuzione della query.
	 */
	public Set<Object> getDistinctColumnValues(String table, Column column) throws SQLException {
		Set<Object> valuesSet = new TreeSet<>();
		String query = "SELECT DISTINCT " + column.getColumnName() + " FROM " + table + " ORDER BY " + column.getColumnName() + ";";
		Statement statement = db.getConnection().createStatement();
		ResultSet rs = statement.executeQuery(query);
		if (column.isNumber())
			while (rs.next())
				valuesSet.add(rs.getDouble(1));
		else
			while (rs.next())
				valuesSet.add(rs.getString(1));
		rs.close();
		statement.close();
		return valuesSet;
	}

}
