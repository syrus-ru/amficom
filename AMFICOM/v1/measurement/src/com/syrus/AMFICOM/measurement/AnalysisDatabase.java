package com.syrus.AMFICOM.measurement;

import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseDate;
import com.syrus.AMFICOM.general.*;

public class AnalysisDatabase extends StorableObjectDatabase {

	public static final String	COLUMN_TYPE_ID				= "type_id";
	public static final String	COLUMN_MONITORED_ELEMENT_ID	= "monitored_element_id";
	public static final String	COLUMN_CRITERIA_SET_ID		= "criteria_set_id";

	private Analysis fromStorableObject(StorableObject storableObject)
			throws IllegalDataException {
		if (storableObject instanceof Analysis)
			return (Analysis) storableObject;
	throw new IllegalDataException(
					"AnalysisDatabase.fromStorableObject | Illegal Storable Object: "
							+ storableObject.getClass().getName());
	}

	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		Analysis analysis = this.fromStorableObject(storableObject);
		this.retrieveAnalysis(analysis);
	}

	private void retrieveAnalysis(Analysis analysis) throws ObjectNotFoundException, RetrieveObjectException {
		String analysisIdStr = analysis.getId().toSQLString();
		String sql;
		{
			StringBuffer buffer = new StringBuffer();
			buffer.append(SQL_SELECT);
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
			buffer.append(COLUMN_CRITERIA_SET_ID);
			buffer.append(COMMA);
			buffer.append(SQL_FROM);
			buffer.append(ObjectEntities.ANALYSIS_ENTITY);
			buffer.append(SQL_WHERE);
			buffer.append(COLUMN_ID);
			buffer.append(EQUALS);
			buffer.append(analysisIdStr);
			sql = buffer.toString();
		}
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("AnalysisDatabase.retrieve | Trying: " + sql,
					Log.DEBUGLEVEL05);
			resultSet = statement.executeQuery(sql);
			if (resultSet.next()) {
				/**
				 * @todo when change DB Identifier model ,change getString() to getLong()
				 */
				Set criteriaSet = new Set(new Identifier(resultSet
						.getString(COLUMN_CRITERIA_SET_ID)));
				analysis.setAttributes(DatabaseDate.fromQuerySubString(
						resultSet, COLUMN_CREATED), DatabaseDate
						.fromQuerySubString(resultSet, COLUMN_MODIFIED),
						/**
						 * @todo when change DB Identifier model ,change getString() to getLong()
						 */
						new Identifier(resultSet.getString(COLUMN_CREATOR_ID)),
						/**
						 * @todo when change DB Identifier model ,change getString() to getLong()
						 */
						new Identifier(resultSet.getString(COLUMN_MODIFIER_ID)),
						/**
						 * @todo when change DB Identifier model ,change getString() to getLong()
						 */
						new Identifier(resultSet.getString(COLUMN_TYPE_ID)),
						/**
						 * @todo when change DB Identifier model ,change getString() to getLong()
						 */
						new Identifier(resultSet
								.getString(COLUMN_MONITORED_ELEMENT_ID)),
						criteriaSet);
			} else
				throw new ObjectNotFoundException("No such analysis: " + analysisIdStr);
		} catch (SQLException sqle) {
			String mesg = "AnalysisDatabase.retrieve | Cannot retrieve analysis "
					+ analysisIdStr;
			throw new RetrieveObjectException(mesg, sqle);
		} finally {
			try {
				if (statement != null) statement.close();
				if (resultSet != null) resultSet.close();
				statement = null;
				resultSet = null;
			} catch (SQLException sqle1) {
//				 nothing yet.
			}
		}
	}

	public Object retrieveObject(StorableObject storableObject,
			int retrieveKind, Object arg) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		Analysis analysis = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws CreateObjectException , IllegalDataException {
		Analysis analysis = this.fromStorableObject(storableObject);
		try {
			this.insertAnalysis(analysis);
		} catch (CreateObjectException e) {
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

	private void insertAnalysis(Analysis analysis) throws CreateObjectException {
		String analysisIdStr = analysis.getId().toSQLString();
		String sql;
		{
			StringBuffer buffer = new StringBuffer();
			buffer.append(SQL_INSERT_INTO);
			buffer.append(ObjectEntities.ANALYSIS_ENTITY);
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
			buffer.append(COLUMN_CRITERIA_SET_ID);
			buffer.append(CLOSE_BRACKET);
			buffer.append(SQL_VALUES);
			buffer.append(OPEN_BRACKET);
			buffer.append(analysisIdStr);
			buffer.append(COMMA);
			buffer.append(DatabaseDate.toUpdateSubString(analysis.getCreated()));
			buffer.append(COMMA);
			buffer.append(DatabaseDate.toUpdateSubString(analysis.getModified()));
			buffer.append(COMMA);
			buffer.append(analysis.getCreatorId().toSQLString());
			buffer.append(COMMA);
			buffer.append(analysis.getModifierId().toSQLString());
			buffer.append(COMMA);
			buffer.append(analysis.getTypeId().toSQLString());
			buffer.append(COMMA);
			buffer.append(analysis.getMonitoredElementId().toSQLString());
			buffer.append(COMMA);
			buffer.append(analysis.getCriteriaSet().getId().toSQLString());
			buffer.append(CLOSE_BRACKET);
			sql = buffer.toString();
		}
		Statement statement = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("AnalysisDatabase.insert | Trying: " + sql,
					Log.DEBUGLEVEL05);
			statement.executeUpdate(sql);
		} catch (SQLException sqle) {
			String mesg = "AnalysisDatabase.insert | Cannot insert analysis "
					+ analysisIdStr;
			throw new CreateObjectException(mesg, sqle);
		} finally {
			try {
				if (statement != null) statement.close();
				statement = null;
			} catch (SQLException sqle1) {
				// nothing yet.
			}
		}
	}

	public void update(StorableObject storableObject, int updateKind,
			Object obj) throws IllegalDataException, UpdateObjectException {
		Analysis analysis = this.fromStorableObject(storableObject);
		switch (updateKind) {
			default:
				return;
		}
	}
}