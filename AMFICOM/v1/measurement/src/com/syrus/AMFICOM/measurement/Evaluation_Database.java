package com.syrus.AMFICOM.measurement;

import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseDate;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObject_Database;
import com.syrus.AMFICOM.general.ObjectEntities;

public class Evaluation_Database extends StorableObject_Database {

	public static final String	COLUMN_TYPE_ID				= "type_id";
	public static final String	COLUMN_MONITORED_ELEMENT_ID	= "monitored_element_id";
	public static final String	COLUMN_THRESHOLD_SET_ID		= "threshold_set_id";
	public static final String	COLUMN_ETHALON_ID			= "etalon_id";

	private Evaluation fromStorableObject(StorableObject storableObject)
			throws Exception {
		if (storableObject instanceof Evaluation)
			return (Evaluation) storableObject;
		else
			throw new Exception(
					"Evaluation_Database.fromStorableObject | Illegal Storable Object: "
							+ storableObject.getClass().getName());
	}

	public void retrieve(StorableObject storableObject) throws Exception {
		Evaluation evaluation = this.fromStorableObject(storableObject);
		this.retrieveEvaluation(evaluation);
	}

	private void retrieveEvaluation(Evaluation evaluation) throws Exception {
		String evaluation_id_str = evaluation.getId().toSQLString();
		String sql;
		{
			StringBuffer buffer = new StringBuffer(SQL_SELECT);
			buffer.append(DatabaseDate.toQuerySubString(COLUMN_CREATED));
			buffer.append(COMMA);
			buffer.append(DatabaseDate.toQuerySubString(COLUMN_MODIFIED));
			buffer.append(COMMA);
			buffer.append(COLUMN_CREATOR_ID);
			buffer.append(COMMA);
			buffer.append(COLUMN_MODIFIER_ID);
			buffer.append(COMMA);
			buffer.append(COLUMN_TYPE_ID);
			buffer.append(COMMA);
			buffer.append(COLUMN_MONITORED_ELEMENT_ID);
			buffer.append(COMMA);
			buffer.append(COLUMN_THRESHOLD_SET_ID);
			buffer.append(COMMA);
			buffer.append(COLUMN_ETHALON_ID);
			buffer.append(COMMA);
			buffer.append(SQL_FROM);
			buffer.append(ObjectEntities.EVALUATION_ENTITY);
			buffer.append(SQL_WHERE);
			buffer.append(COLUMN_ID);
			buffer.append(EQUALS);
			buffer.append(evaluation_id_str);
			sql = buffer.toString();
		}
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("Evaluation_Database.retrieve | Trying: " + sql,
					Log.DEBUGLEVEL05);
			resultSet = statement.executeQuery(sql);
			if (resultSet.next()) {
				/**
				 * @todo when change DB Identifier model ,change getString() to
				 *       getLong()
				 */
				Set threshold_set = new Set(new Identifier(resultSet
						.getString(COLUMN_THRESHOLD_SET_ID)));
				/**
				 * @todo when change DB Identifier model ,change getString() to
				 *       getLong()
				 */
				Set etalon = new Set(new Identifier(resultSet
						.getString(COLUMN_ETHALON_ID)));
				/**
				 * @todo when change DB Identifier model ,change getString() to
				 *       getLong()
				 */
				evaluation
						.setAttributes(
								DatabaseDate.fromQuerySubString(resultSet,
										COLUMN_CREATED),
								DatabaseDate.fromQuerySubString(resultSet,
										COLUMN_MODIFIED),
								new Identifier(resultSet
										.getString(COLUMN_CREATOR_ID)),
								new Identifier(resultSet
										.getString(COLUMN_MODIFIER_ID)),
								new Identifier(resultSet
										.getString(COLUMN_TYPE_ID)),
								new Identifier(resultSet
										.getString(COLUMN_MONITORED_ELEMENT_ID)),
								threshold_set, etalon);
			} else
				throw new Exception("No such evaluation: " + evaluation_id_str);
		} catch (SQLException sqle) {
			String mesg = "Evaluation_Database.retrieve | Cannot retrieve evaluation "
					+ evaluation_id_str;
			throw new Exception(mesg, sqle);
		} finally {
			try {
				if (statement != null)
					statement.close();
				if (resultSet != null)
					resultSet.close();
				statement = null;
				resultSet = null;
			} catch (SQLException sqle1) {
			}
		}
	}

	public Object retrieveObject(StorableObject storableObject,
			int retrieve_kind, Object arg) throws Exception {
		Evaluation evaluation = this.fromStorableObject(storableObject);
		switch (retrieve_kind) {
			default:
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws Exception {
		Evaluation evaluation = this.fromStorableObject(storableObject);
		try {
			this.insertEvaluation(evaluation);
		} catch (Exception e) {
			try {
				connection.rollback();
			} catch (SQLException sqle) {
				Log.errorMessage("Exception in rolling back");
				Log.errorException(sqle);
			}
			throw e;
		}
		try {
			connection.commit();
		} catch (SQLException sqle) {
			Log.errorMessage("Exception in commiting");
			Log.errorException(sqle);
		}
	}

	public void insertEvaluation(Evaluation evaluation) throws Exception {
		String evaluation_id_str = evaluation.getId().toSQLString();
		String sql;
		{
			StringBuffer buffer = new StringBuffer(SQL_INSERT_INTO);
			buffer.append(ObjectEntities.EVALUATION_ENTITY);
			buffer.append(OPEN_BRACKET);
			buffer.append(COLUMN_ID);
			buffer.append(COMMA);
			buffer.append(COLUMN_CREATED);
			buffer.append(COMMA);
			buffer.append(COLUMN_MODIFIED);
			buffer.append(COMMA);
			buffer.append(COLUMN_CREATOR_ID);
			buffer.append(COMMA);
			buffer.append(COLUMN_MODIFIER_ID);
			buffer.append(COMMA);
			buffer.append(COLUMN_TYPE_ID);
			buffer.append(COMMA);
			buffer.append(COLUMN_MONITORED_ELEMENT_ID);
			buffer.append(COMMA);
			buffer.append(COLUMN_THRESHOLD_SET_ID);
			buffer.append(COMMA);
			buffer.append(COLUMN_ETHALON_ID);
			buffer.append(CLOSE_BRACKET);
			buffer.append(SQL_VALUES);
			buffer.append(OPEN_BRACKET);
			buffer.append(evaluation_id_str);
			buffer.append(COMMA);
			buffer.append(DatabaseDate.toUpdateSubString(evaluation
					.getCreated()));
			buffer.append(COMMA);
			buffer.append(DatabaseDate.toUpdateSubString(evaluation
					.getModified()));
			buffer.append(COMMA);
			buffer.append(evaluation.getCreatorId().toSQLString());
			buffer.append(COMMA);
			buffer.append(evaluation.getModifierId().toSQLString());
			buffer.append(COMMA);
			buffer.append(evaluation.getTypeId().toSQLString());
			buffer.append(COMMA);
			buffer.append(evaluation.getMonitoredElementId().toSQLString());
			buffer.append(COMMA);
			buffer.append(evaluation.getThresholdSet().getId().toSQLString());
			buffer.append(COMMA);
			buffer.append(evaluation.getEtalon().getId().toSQLString());
			buffer.append(CLOSE_BRACKET);
			sql = buffer.toString();
		}
		Statement statement = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("Evaluation_Database.insert | Trying: " + sql,
					Log.DEBUGLEVEL05);
			statement.executeUpdate(sql);
		} catch (SQLException sqle) {
			String mesg = "Evaluation_Database.insert | Cannot insert evaluation "
					+ evaluation_id_str;
			throw new Exception(mesg, sqle);
		} finally {
			try {
				if (statement != null)
					statement.close();
				statement = null;
			} catch (SQLException sqle1) {
			}
		}
	}

	public void update(StorableObject storableObject, int update_kind,
			Object obj) throws Exception {
		Evaluation evaluation = this.fromStorableObject(storableObject);
		switch (update_kind) {
			default:
				return;
		}
	}
}