package com.syrus.AMFICOM.measurement;

import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseDate;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.ObjectEntities;

public class EvaluationDatabase extends StorableObjectDatabase {

	public static final String	COLUMN_TYPE_ID				= "typeId";
	public static final String	COLUMN_MONITORED_ELEMENT_ID	= "monitoredElementId";
	public static final String	COLUMN_THRESHOLD_SET_ID		= "thresholdSetId";
	public static final String	COLUMN_ETHALON_ID			= "etalonId";

	private Evaluation fromStorableObject(StorableObject storableObject)
			throws Exception {
		if (storableObject instanceof Evaluation)
			return (Evaluation) storableObject;
		else
			throw new Exception(
					"EvaluationDatabase.fromStorableObject | Illegal Storable Object: "
							+ storableObject.getClass().getName());
	}

	public void retrieve(StorableObject storableObject) throws Exception {
		Evaluation evaluation = this.fromStorableObject(storableObject);
		this.retrieveEvaluation(evaluation);
	}

	private void retrieveEvaluation(Evaluation evaluation) throws Exception {
		String evaluationIdStr = evaluation.getId().toSQLString();
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
			buffer.append(evaluationIdStr);
			sql = buffer.toString();
		}
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("EvaluationDatabase.retrieve | Trying: " + sql,
					Log.DEBUGLEVEL05);
			resultSet = statement.executeQuery(sql);
			if (resultSet.next()) {
				/**
				 * @todo when change DB Identifier model ,change getString() to
				 *       getLong()
				 */
				Set thresholdSet = new Set(new Identifier(resultSet
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
								thresholdSet, etalon);
			} else
				throw new Exception("No such evaluation: " + evaluationIdStr);
		} catch (SQLException sqle) {
			String mesg = "EvaluationDatabase.retrieve | Cannot retrieve evaluation "
					+ evaluationIdStr;
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
			int retrieveKind, Object arg) throws Exception {
		Evaluation evaluation = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
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
		String evaluationIdStr = evaluation.getId().toSQLString();
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
			buffer.append(evaluationIdStr);
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
			Log.debugMessage("EvaluationDatabase.insert | Trying: " + sql,
					Log.DEBUGLEVEL05);
			statement.executeUpdate(sql);
		} catch (SQLException sqle) {
			String mesg = "EvaluationDatabase.insert | Cannot insert evaluation "
					+ evaluationIdStr;
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

	public void update(StorableObject storableObject, int updateKind,
			Object obj) throws Exception {
		Evaluation evaluation = this.fromStorableObject(storableObject);
		switch (updateKind) {
			default:
				return;
		}
	}
}