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

public class Analysis_Database extends StorableObject_Database {

	public static final String	COLUMN_TYPE_ID				= "type_id";
	public static final String	COLUMN_MONITORED_ELEMENT_ID	= "monitored_element_id";
	public static final String	COLUMN_CRITERIA_SET_ID		= "criteria_set_id";

	private Analysis fromStorableObject(StorableObject storableObject)
			throws Exception {
		if (storableObject instanceof Analysis)
			return (Analysis) storableObject;
		else
			throw new Exception(
					"Analysis_Database.fromStorableObject | Illegal Storable Object: "
							+ storableObject.getClass().getName());
	}

	public void retrieve(StorableObject storableObject) throws Exception {
		Analysis analysis = this.fromStorableObject(storableObject);
		this.retrieveAnalysis(analysis);
	}

	private void retrieveAnalysis(Analysis analysis) throws Exception {
		String analysis_id_str = analysis.getId().toSQLString();
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
			buffer.append(analysis_id_str);
			sql = buffer.toString();
		}
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("Analysis_Database.retrieve | Trying: " + sql,
					Log.DEBUGLEVEL05);
			resultSet = statement.executeQuery(sql);
			if (resultSet.next()) {
				Set criteria_set = new Set(new Identifier(resultSet
						.getLong(COLUMN_CRITERIA_SET_ID)));
				analysis.setAttributes(DatabaseDate.fromQuerySubString(
						resultSet, COLUMN_CREATED), DatabaseDate
						.fromQuerySubString(resultSet, COLUMN_MODIFIED),
						new Identifier(resultSet.getLong(COLUMN_CREATOR_ID)),
						new Identifier(resultSet.getLong(COLUMN_MODIFIER_ID)),
						new Identifier(resultSet.getLong(COLUMN_TYPE_ID)),
						new Identifier(resultSet
								.getLong(COLUMN_MONITORED_ELEMENT_ID)),
						criteria_set);
			} else
				throw new Exception("No such analysis: " + analysis_id_str);
		} catch (SQLException sqle) {
			String mesg = "Analysis_Database.retrieve | Cannot retrieve analysis "
					+ analysis_id_str;
			throw new Exception(mesg, sqle);
		} finally {
			try {
				if (statement != null) statement.close();
				if (resultSet != null) resultSet.close();
				statement = null;
				resultSet = null;
			} catch (SQLException sqle1) {
			}
		}
	}

	public Object retrieveObject(StorableObject storableObject,
			int retrieve_kind, Object arg) throws Exception {
		Analysis analysis = this.fromStorableObject(storableObject);
		switch (retrieve_kind) {
			default:
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws Exception {
		Analysis analysis = this.fromStorableObject(storableObject);
		try {
			this.insertAnalysis(analysis);
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

	private void insertAnalysis(Analysis analysis) throws Exception {
		String analysis_id_str = analysis.getId().toSQLString();
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
			buffer.append(analysis_id_str);
			buffer.append(COMMA);
			buffer.append(DatabaseDate.toUpdateSubString(analysis.getCreated()));
			buffer.append(COMMA);
			buffer.append(DatabaseDate.toUpdateSubString(analysis.getModified()));
			buffer.append(COMMA);
			buffer.append(analysis.getCreatorId().toString());
			buffer.append(COMMA);
			buffer.append(analysis.getModifierId().toString());
			buffer.append(COMMA);
			buffer.append(analysis.getTypeId().toString());
			buffer.append(COMMA);
			buffer.append(analysis.getMonitoredElementId().toString());
			buffer.append(COMMA);
			buffer.append(analysis.getCriteriaSet().getId().toString());
			buffer.append(CLOSE_BRACKET);
			sql = buffer.toString();
		}
		Statement statement = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("Analysis_Database.insert | Trying: " + sql,
					Log.DEBUGLEVEL05);
			statement.executeUpdate(sql);
		} catch (SQLException sqle) {
			String mesg = "Analysis_Database.insert | Cannot insert analysis "
					+ analysis_id_str;
			throw new Exception(mesg, sqle);
		} finally {
			try {
				if (statement != null) statement.close();
				statement = null;
			} catch (SQLException sqle1) {
			}
		}
	}

	public void update(StorableObject storableObject, int update_kind,
			Object obj) throws Exception {
		Analysis analysis = this.fromStorableObject(storableObject);
		switch (update_kind) {
			default:
				return;
		}
	}
}