package com.syrus.AMFICOM.measurement;

import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseDate;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;

public class AnalysisDatabase extends StorableObjectDatabase {

	public static final String COLUMN_TYPE_ID = "type_id";
	public static final String COLUMN_MONITORED_ELEMENT_ID = "monitored_element_id";
	public static final String COLUMN_CRITERIA_SET_ID = "criteria_set_id";

	private Analysis fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof Analysis)
			return (Analysis) storableObject;
	throw new IllegalDataException("AnalysisDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		Analysis analysis = this.fromStorableObject(storableObject);
		this.retrieveAnalysis(analysis);
	}

	private void retrieveAnalysis(Analysis analysis) throws ObjectNotFoundException, RetrieveObjectException {
		String analysisIdStr = analysis.getId().toSQLString();
		String sql = SQL_SELECT
			+ DatabaseDate.toQuerySubString(COLUMN_CREATED) + COMMA 
			+ DatabaseDate.toQuerySubString(COLUMN_MODIFIED) + COMMA
			+ COLUMN_CREATOR_ID + COMMA
			+ COLUMN_MODIFIER_ID + COMMA
			+ COLUMN_TYPE_ID + COMMA
			+ COLUMN_MONITORED_ELEMENT_ID + COMMA
			+ COLUMN_CRITERIA_SET_ID
			+ SQL_FROM + ObjectEntities.ANALYSIS_ENTITY
			+ SQL_WHERE + COLUMN_ID + EQUALS + analysisIdStr;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("AnalysisDatabase.retrieve | Trying: " + sql, Log.DEBUGLEVEL05);
			resultSet = statement.executeQuery(sql);
			if (resultSet.next()) {
				/**
				 * @todo when change DB Identifier model ,change getString() to getLong()
				 */
				AnalysisType analysisType = new AnalysisType(new Identifier(resultSet.getString(COLUMN_TYPE_ID)));
				/**
				 * @todo when change DB Identifier model ,change getString() to getLong()
				 */
				Set criteriaSet = new Set(new Identifier(resultSet.getString(COLUMN_CRITERIA_SET_ID)));
				analysis.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
															 DatabaseDate.fromQuerySubString(resultSet, COLUMN_MODIFIED),
															/**
															 * @todo when change DB Identifier model ,change getString() to getLong()
															 */
															 new Identifier(resultSet.getString(COLUMN_CREATOR_ID)),
															/**
															 * @todo when change DB Identifier model ,change getString() to getLong()
															 */
															 new Identifier(resultSet.getString(COLUMN_MODIFIER_ID)),
															 analysisType,
															/**
															 * @todo when change DB Identifier model ,change getString() to getLong()
															 */
															 new Identifier(resultSet.getString(COLUMN_MONITORED_ELEMENT_ID)),
															 criteriaSet);
			}
			else
				throw new ObjectNotFoundException("No such analysis: " + analysisIdStr);
		}
		catch (SQLException sqle) {
			String mesg = "AnalysisDatabase.retrieve | Cannot retrieve analysis "
					+ analysisIdStr;
			throw new RetrieveObjectException(mesg, sqle);
		}
		finally {
			try {
				if (statement != null)
					statement.close();
				if (resultSet != null)
					resultSet.close();
				statement = null;
				resultSet = null;
			}
			catch (SQLException sqle1) {
				Log.errorException(sqle1);
			}
		}
	}

	public Object retrieveObject(StorableObject storableObject, int retrieveKind, Object arg) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
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
		}
		catch (CreateObjectException e) {
			try {
				connection.rollback();
			}
			catch (SQLException sqle) {
				Log.errorMessage("Exception in rolling back");
				Log.errorException(sqle);
			}
			throw e;
		}
		try {
			connection.commit();
		}
		catch (SQLException sqle) {
			Log.errorMessage("Exception in commiting");
			Log.errorException(sqle);
		}
	}

	private void insertAnalysis(Analysis analysis) throws CreateObjectException {
		String analysisIdStr = analysis.getId().toSQLString();
		String sql = SQL_INSERT_INTO
			+ ObjectEntities.ANALYSIS_ENTITY + OPEN_BRACKET
			+ COLUMN_ID + COMMA
			+ COLUMN_CREATED + COMMA
			+ COLUMN_MODIFIED + COMMA
			+ COLUMN_CREATOR_ID + COMMA
			+ COLUMN_MODIFIER_ID + COMMA
			+ COLUMN_TYPE_ID + COMMA
			+ COLUMN_MONITORED_ELEMENT_ID + COMMA
			+ COLUMN_CRITERIA_SET_ID
			+ CLOSE_BRACKET + SQL_VALUES + OPEN_BRACKET
			+ analysisIdStr + COMMA
			+ DatabaseDate.toUpdateSubString(analysis.getCreated()) + COMMA
			+ DatabaseDate.toUpdateSubString(analysis.getModified()) + COMMA
			+ analysis.getCreatorId().toSQLString() + COMMA
			+ analysis.getModifierId().toSQLString() + COMMA
			+ analysis.getType().getId().toSQLString() + COMMA
			+ analysis.getMonitoredElementId().toSQLString() + COMMA
			+ analysis.getCriteriaSet().getId().toSQLString()
			+ CLOSE_BRACKET;
		Statement statement = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("AnalysisDatabase.insert | Trying: " + sql, Log.DEBUGLEVEL05);
			statement.executeUpdate(sql);
		}
		catch (SQLException sqle) {
			String mesg = "AnalysisDatabase.insert | Cannot insert analysis " + analysisIdStr;
			throw new CreateObjectException(mesg, sqle);
		}
		finally {
			try {
				if (statement != null) statement.close();
				statement = null;
			}
			catch (SQLException sqle1) {
				Log.errorException(sqle1);
			}
		}
	}

	public void update(StorableObject storableObject, int updateKind, Object obj) throws IllegalDataException, UpdateObjectException {
		Analysis analysis = this.fromStorableObject(storableObject);
		switch (updateKind) {
			default:
				return;
		}
	}
}
