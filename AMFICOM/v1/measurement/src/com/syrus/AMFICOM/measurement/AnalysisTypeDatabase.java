package com.syrus.AMFICOM.measurement;

import java.util.ArrayList;
import java.util.Iterator;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseDate;

public class AnalysisTypeDatabase extends StorableObjectDatabase {

	public static final String	MODE_IN							= "IN";
	public static final String	MODE_CRITERION					= "CRI";
	public static final String	MODE_OUT						= "OUT";

	public static final String	COLUMN_CODENAME					= "codename";
	public static final String	COLUMN_DESCRIPTION				= "description";	
	
	public static final String	LINK_COLUMN_ANALYSIS_TYPE_ID 	= "analysisTypeId";

	private AnalysisType fromStorableObject(StorableObject storableObject) throws Exception {
		if (storableObject instanceof AnalysisType)
			return (AnalysisType) storableObject;
		else
			throw new Exception("AnalysisTypeDatabase.fromStorableObject | Illegal Storable Object: "
					+ storableObject.getClass().getName());
	}

	public void retrieve(StorableObject storableObject) throws Exception {
		AnalysisType analysisType = this.fromStorableObject(storableObject);
		this.retrieveAnalysisType(analysisType);
		this.retrieveParameterTypes(analysisType);
	}

	private void retrieveAnalysisType(AnalysisType analysisType) throws Exception {
		String analysisTypeIdStr = analysisType.getId().toSQLString();
		String sql;
		{
			StringBuffer buffer = new StringBuffer();
			buffer.append(SQL_SELECT );
			buffer.append(DatabaseDate.toQuerySubString(COLUMN_CREATED));
			buffer.append(COMMA);
			buffer.append(DatabaseDate.toQuerySubString(COLUMN_MODIFIED));
			buffer.append(COMMA);
			buffer.append(COLUMN_CREATOR_ID);
			buffer.append(COMMA);
			buffer.append(COLUMN_MODIFIER_ID);
			buffer.append(COMMA);
			buffer.append(COLUMN_CODENAME);
			buffer.append(COMMA);
			buffer.append(COLUMN_DESCRIPTION);
			buffer.append(SQL_FROM);
			buffer.append(ObjectEntities.ANALYSISTYPE_ENTITY);
			buffer.append(SQL_WHERE);
			buffer.append(COLUMN_ID);
			buffer.append(EQUALS);
			buffer.append(analysisTypeIdStr);
			sql = buffer.toString();
		}
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("AnalysisTypeDatabase.retrieveAnalysisType | Trying: " + sql, Log.DEBUGLEVEL05);
			resultSet = statement.executeQuery(sql);
			if (resultSet.next())
				/**
				 * @todo when change DB Identifier model ,change getString() to getLong()
				 */
				analysisType.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED), DatabaseDate
						.fromQuerySubString(resultSet, COLUMN_MODIFIED), new Identifier(resultSet
						.getString(COLUMN_CREATOR_ID)), new Identifier(resultSet.getString(COLUMN_MODIFIER_ID)),
						resultSet.getString(COLUMN_CODENAME), resultSet.getString(COLUMN_DESCRIPTION));
			else
				throw new Exception("No such analysis type: " + analysisTypeIdStr);
		} catch (SQLException sqle) {
			String mesg = "AnalysisTypeDatabase.retrieveAnalysisType | Cannot retrieve analysis type "
					+ analysisTypeIdStr;
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

	private void retrieveParameterTypes(AnalysisType analysisType) throws Exception {	
		ArrayList inParTyps = new ArrayList();
		ArrayList criteriaParTyps = new ArrayList();
		ArrayList outParTyps = new ArrayList();
		
		String analysisTypeIdStr = analysisType.getId().toSQLString();
		String sql;
		{
			StringBuffer buffer = new StringBuffer(SQL_SELECT);
			buffer.append(LINK_COLUMN_PARAMETER_TYPE_ID);
			buffer.append(COMMA);
			buffer.append(LINK_COLUMN_PARAMETER_MODE);
			buffer.append(SQL_FROM);
			buffer.append(ObjectEntities.ANATYPPARTYPLINK_ENTITY);
			buffer.append(SQL_WHERE);
			buffer.append(LINK_COLUMN_ANALYSIS_TYPE_ID);
			buffer.append(EQUALS);
			buffer.append( analysisTypeIdStr);
			sql = buffer.toString();
		}
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("AnalysisTypeDatabase.retrieveParameterTypes | Trying: " + sql, Log.DEBUGLEVEL05);
			resultSet = statement.executeQuery(sql);
			String parameterMode;
			String parameterTypeIdCode;
			while (resultSet.next()) {
				/**
				 * @todo when change DB Identifier model ,change getString() to getLong()
				 */
				parameterMode = resultSet.getString(LINK_COLUMN_PARAMETER_MODE);
				parameterTypeIdCode = resultSet.getString(LINK_COLUMN_PARAMETER_TYPE_ID);
				if (parameterMode.equals(MODE_IN))
					inParTyps.add(new Identifier(parameterTypeIdCode));
				else if (parameterMode.equals(MODE_CRITERION))
					criteriaParTyps.add(new Identifier(parameterTypeIdCode));
				else if (parameterMode.equals(MODE_OUT))
					outParTyps.add(new Identifier(parameterTypeIdCode));
				else
					Log
							.errorMessage("AnalysisTypeDatabase.retrieveParameterTypes | ERROR: Unknown parameter mode for parameterTypeId "
									+ parameterTypeIdCode);
			}
		} catch (SQLException sqle) {
			String mesg = "AnalysisTypeDatabase.retrieveParameterTypes | Cannot retrieve parameter types for analysis type "
					+ analysisTypeIdStr;
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
		inParTyps.trimToSize();
		criteriaParTyps.trimToSize();
		outParTyps.trimToSize();
		analysisType.setParameterTypes(inParTyps, criteriaParTyps, outParTyps);
	}

	public Object retrieveObject(StorableObject storableObject, int retrieveKind, Object arg) throws Exception {
		AnalysisType analysisType = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws Exception {
		AnalysisType analysisType = this.fromStorableObject(storableObject);
		try {
			this.insertAnalysisType(analysisType);
			this.insertParameterTypes(analysisType);
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

	private void insertAnalysisType(AnalysisType analysisType) throws Exception {
		String analysisTypeIdStr = analysisType.getId().toSQLString();
		String sql = SQL_INSERT_INTO 
			+ ObjectEntities.ANALYSISTYPE_ENTITY + OPEN_BRACKET 
			+ COLUMN_ID + COMMA 
			+ COLUMN_CREATED + COMMA 
			+ COLUMN_MODIFIED + COMMA 
			+ COLUMN_CREATOR_ID + COMMA 
			+ COLUMN_MODIFIER_ID + COMMA
			+ COLUMN_CODENAME + COMMA 
			+ COLUMN_DESCRIPTION
			+ CLOSE_BRACKET + SQL_VALUES + OPEN_BRACKET			
			+ analysisTypeIdStr + COMMA
			+ DatabaseDate.toUpdateSubString(analysisType.getCreated()) + COMMA
			+ DatabaseDate.toUpdateSubString(analysisType.getModified()) + COMMA
			+ analysisType.getCreatorId().toString() + COMMA 
			+ analysisType.getModifierId().toString() + COMMA + APOSTOPHE
			+ analysisType.getCodename() + APOSTOPHE + COMMA + APOSTOPHE 
			+ analysisType.getDescription() + APOSTOPHE + CLOSE_BRACKET;
		Statement statement = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("AnalysisTypeDatabase.insertAnalysisType | Trying: " + sql, Log.DEBUGLEVEL05);
			statement.executeUpdate(sql);
		} catch (SQLException sqle) {
			String mesg = "AnalysisTypeDatabase.insertAnalysisType | Cannot insert analysis type "
					+ analysisTypeIdStr;
			throw new Exception(mesg, sqle);
		} finally {
			try {
				if (statement != null) statement.close();
				statement = null;
			} catch (SQLException sqle1) {
			}
		}
	}

	private void insertParameterTypes(AnalysisType analysisType) throws Exception {
		ArrayList inParTyps = analysisType.getInParameterTypes();
		ArrayList criteriaParTyps = analysisType.getCriteriaParameterTypes();
		ArrayList outParTyps = analysisType.getOutParameterTypes();
		String analysisTypeIdCode = analysisType.getId().getCode();
		String sql = "INSERT INTO " + ObjectEntities.ANATYPPARTYPLINK_ENTITY
				+ " (analysisTypeId, parameterTypeId, parameterMode)" + " VALUES (?, ?, ?)";
		PreparedStatement preparedStatement = null;
		/**
		 * @todo when change DB Identifier model ,change String to long
		 */		
		String parameterTypeIdCode = null;
		String parameterMode = null;
		try {
			preparedStatement = connection.prepareStatement(sql);
			for (Iterator iterator = inParTyps.iterator(); iterator.hasNext();) {
				preparedStatement.setString(1, analysisTypeIdCode);
				parameterTypeIdCode = ((Identifier) iterator.next()).getCode();
				/**
				 * @todo when change DB Identifier model ,change setString() to setLong()
				 */
				preparedStatement.setString(2, parameterTypeIdCode);
				parameterMode = MODE_IN;
				preparedStatement.setString(3, parameterMode);
				Log.debugMessage("AnalysisTypeDatabase.insertParameterTypes | Inserting parameter type "
						+ parameterTypeIdCode + " of parameter mode '" + parameterMode + "' for analysis type "
						+ analysisTypeIdCode, Log.DEBUGLEVEL05);
				preparedStatement.executeUpdate();
			}
			for (Iterator iterator = criteriaParTyps.iterator(); iterator.hasNext();) {
				preparedStatement.setString(1, analysisTypeIdCode);
				parameterTypeIdCode = ((Identifier) iterator.next()).getCode();
				/**
				 * @todo when change DB Identifier model ,change setString() to setLong()
				 */
				preparedStatement.setString(2, parameterTypeIdCode);
				parameterMode = MODE_CRITERION;
				preparedStatement.setString(3, parameterMode);
				Log.debugMessage("AnalysisTypeDatabase.insertParameterTypes | Inserting parameter type "
						+ parameterTypeIdCode + " of parameter mode '" + parameterMode + "' for analysis type "
						+ analysisTypeIdCode, Log.DEBUGLEVEL05);
				preparedStatement.executeUpdate();
			}
			for (Iterator iterator = outParTyps.iterator(); iterator.hasNext();) {
				preparedStatement.setString(1, analysisTypeIdCode);
				parameterTypeIdCode = ((Identifier) iterator.next()).getCode();
				/**
				 * @todo when change DB Identifier model ,change setString() to setLong()
				 */
				preparedStatement.setString(2, parameterTypeIdCode);
				parameterMode = MODE_OUT;
				preparedStatement.setString(3, parameterMode);
				Log.debugMessage("AnalysisTypeDatabase.insertParameterTypes | Inserting parameter type "
						+ parameterTypeIdCode + " of parameter mode '" + parameterMode + "' for analysis type "
						+ analysisTypeIdCode, Log.DEBUGLEVEL05);
				preparedStatement.executeUpdate();
			}
		} catch (SQLException sqle) {
			String mesg = "AnalysisTypeDatabase.insertParameterTypes | Cannot insert parameter type "
					+ parameterTypeIdCode + " of parameter mode '" + parameterMode + "' for analysis type "
					+ analysisTypeIdCode;
			throw new Exception(mesg, sqle);
		} finally {
			try {
				if (preparedStatement != null) preparedStatement.close();
				preparedStatement = null;
			} catch (SQLException sqle1) {
			}
		}
	}

	public void update(StorableObject storableObject, int updateKind, Object obj) throws Exception {
		AnalysisType analysisType = this.fromStorableObject(storableObject);
		switch (updateKind) {
			default:
				return;
		}
	}

	public void delete(AnalysisType analysisType) {
		String analysisTypeIdStr = analysisType.getId().toString();
		Statement statement = null;
		try {
			statement = connection.createStatement();
			statement.executeUpdate("DELETE FROM " + ObjectEntities.ANATYPPARTYPLINK_ENTITY
					+ " WHERE analysisTypeId = " + analysisTypeIdStr);
			statement.executeUpdate("DELETE FROM " + ObjectEntities.ANALYSISTYPE_ENTITY + " WHERE " + COLUMN_ID + " = "
					+ analysisTypeIdStr);
			connection.commit();
		} catch (SQLException sqle1) {
			Log.errorException(sqle1);
		} finally {
			try {
				if (statement != null) statement.close();
				statement = null;
			} catch (SQLException Ex) {
			}
		}
	}

	public static AnalysisType retrieveForCodename(String codename) throws Exception {
		String sql = SQL_SELECT + COLUMN_ID + SQL_FROM + ObjectEntities.ANALYSISTYPE_ENTITY + SQL_WHERE
				+ COLUMN_CODENAME + " = '" + codename + "'";
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("AnalysisTypeDatabase.retrieveForCodename | Trying: " + sql, Log.DEBUGLEVEL05);
			resultSet = statement.executeQuery(sql);
			if (resultSet.next())
				return new AnalysisType(new Identifier(resultSet.getString(COLUMN_ID)));
			else
				throw new Exception("No analysis type with codename: '" + codename + "'");
		} catch (SQLException sqle) {
			String mesg = "AnalysisTypeDatabase.retrieveForCodename | Cannot retrieve analysis type with codename: '"
					+ codename + "'";
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
}