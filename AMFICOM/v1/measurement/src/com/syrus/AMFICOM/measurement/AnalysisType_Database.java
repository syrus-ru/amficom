package com.syrus.AMFICOM.measurement;

import java.util.ArrayList;
import java.util.Iterator;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObject_Database;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseDate;

public class AnalysisType_Database extends StorableObject_Database {

	public static final String	MODE_IN				= "IN";
	public static final String	MODE_CRITERION		= "CRI";
	public static final String	MODE_OUT			= "OUT";

	public static final String	COLUMN_CODENAME		= "codename";
	public static final String	COLUMN_DESCRIPTION	= "description";	

	private AnalysisType fromStorableObject(StorableObject storableObject) throws Exception {
		if (storableObject instanceof AnalysisType)
			return (AnalysisType) storableObject;
		else
			throw new Exception("AnalysisType_Database.fromStorableObject | Illegal Storable Object: "
					+ storableObject.getClass().getName());
	}

	public void retrieve(StorableObject storableObject) throws Exception {
		AnalysisType analysisType = this.fromStorableObject(storableObject);
		this.retrieveAnalysisType(analysisType);
		this.retrieveParameterTypes(analysisType);
	}

	private void retrieveAnalysisType(AnalysisType analysisType) throws Exception {
		String analysis_type_id_str = analysisType.getId().toSQLString();
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
			buffer.append(analysis_type_id_str);
			sql = buffer.toString();
		}
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("AnalysisType_Database.retrieveAnalysisType | Trying: " + sql, Log.DEBUGLEVEL05);
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
				throw new Exception("No such analysis type: " + analysis_type_id_str);
		} catch (SQLException sqle) {
			String mesg = "AnalysisType_Database.retrieveAnalysisType | Cannot retrieve analysis type "
					+ analysis_type_id_str;
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
		ArrayList in_par_typs = new ArrayList();
		ArrayList criteria_par_typs = new ArrayList();
		ArrayList out_par_typs = new ArrayList();
		
		String analysis_type_id_str = analysisType.getId().toSQLString();
		String sql;
		{
			StringBuffer buffer = new StringBuffer(SQL_SELECT);
			buffer.append("parameter_type_id");
			buffer.append(COMMA);
			buffer.append("parameter_mode");
			buffer.append(SQL_FROM);
			buffer.append(ObjectEntities.ANATYPPARTYPLINK_ENTITY);
			buffer.append(SQL_WHERE);
			buffer.append("analysis_type_id");
			buffer.append(EQUALS);
			buffer.append( analysis_type_id_str);
			sql = buffer.toString();
		}
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("AnalysisType_Database.retrieveParameterTypes | Trying: " + sql, Log.DEBUGLEVEL05);
			resultSet = statement.executeQuery(sql);
			String parameter_mode;
			String parameter_type_id_code;
			while (resultSet.next()) {
				/**
				 * @todo when change DB Identifier model ,change getString() to getLong()
				 */
				parameter_mode = resultSet.getString("parameter_mode");
				parameter_type_id_code = resultSet.getString("parameter_type_id");
				if (parameter_mode.equals(MODE_IN))
					in_par_typs.add(new Identifier(parameter_type_id_code));
				else if (parameter_mode.equals(MODE_CRITERION))
					criteria_par_typs.add(new Identifier(parameter_type_id_code));
				else if (parameter_mode.equals(MODE_OUT))
					out_par_typs.add(new Identifier(parameter_type_id_code));
				else
					Log
							.errorMessage("AnalysisType_Database.retrieveParameterTypes | ERROR: Unknown parameter mode for parameter_type_id "
									+ parameter_type_id_code);
			}
		} catch (SQLException sqle) {
			String mesg = "AnalysisType_Database.retrieveParameterTypes | Cannot retrieve parameter types for analysis type "
					+ analysis_type_id_str;
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
		in_par_typs.trimToSize();
		criteria_par_typs.trimToSize();
		out_par_typs.trimToSize();
		analysisType.setParameterTypes(in_par_typs, criteria_par_typs, out_par_typs);
	}

	public Object retrieveObject(StorableObject storableObject, int retrieve_kind, Object arg) throws Exception {
		AnalysisType analysisType = this.fromStorableObject(storableObject);
		switch (retrieve_kind) {
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
		String analysis_type_id_str = analysisType.getId().toSQLString();
		String sql = "INSERT INTO " + ObjectEntities.ANALYSISTYPE_ENTITY + " (" 
			+ COLUMN_ID + COMMA 
			+ COLUMN_CREATED + COMMA 
			+ COLUMN_MODIFIED + COMMA 
			+ COLUMN_CREATOR_ID + COMMA 
			+ COLUMN_MODIFIER_ID + COMMA
			+ COLUMN_CODENAME + COMMA 
			+ COLUMN_DESCRIPTION 
			+ ") VALUES ("
			+ analysis_type_id_str + COMMA
			+ DatabaseDate.toUpdateSubString(analysisType.getCreated()) + COMMA
			+ DatabaseDate.toUpdateSubString(analysisType.getModified()) + COMMA
			+ analysisType.getCreatorId().toString() + COMMA 
			+ analysisType.getModifierId().toString() + ", '"
			+ analysisType.getCodename() + "', '" 
			+ analysisType.getDescription() + "')";
		Statement statement = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("AnalysisType_Database.insertAnalysisType | Trying: " + sql, Log.DEBUGLEVEL05);
			statement.executeUpdate(sql);
		} catch (SQLException sqle) {
			String mesg = "AnalysisType_Database.insertAnalysisType | Cannot insert analysis type "
					+ analysis_type_id_str;
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
		ArrayList in_par_typs = analysisType.getInParameterTypes();
		ArrayList criteria_par_typs = analysisType.getCriteriaParameterTypes();
		ArrayList out_par_typs = analysisType.getOutParameterTypes();
		String analysis_type_id_code = analysisType.getId().getCode();
		String sql = "INSERT INTO " + ObjectEntities.ANATYPPARTYPLINK_ENTITY
				+ " (analysis_type_id, parameter_type_id, parameter_mode)" + " VALUES (?, ?, ?)";
		PreparedStatement preparedStatement = null;
		/**
		 * @todo when change DB Identifier model ,change String to long
		 */		
		String parameter_type_id_code = null;
		String parameter_mode = null;
		try {
			preparedStatement = connection.prepareStatement(sql);
			for (Iterator iterator = in_par_typs.iterator(); iterator.hasNext();) {
				preparedStatement.setString(1, analysis_type_id_code);
				parameter_type_id_code = ((Identifier) iterator.next()).getCode();
				/**
				 * @todo when change DB Identifier model ,change setString() to setLong()
				 */
				preparedStatement.setString(2, parameter_type_id_code);
				parameter_mode = MODE_IN;
				preparedStatement.setString(3, parameter_mode);
				Log.debugMessage("AnalysisType_Database.insertParameterTypes | Inserting parameter type "
						+ parameter_type_id_code + " of parameter mode '" + parameter_mode + "' for analysis type "
						+ analysis_type_id_code, Log.DEBUGLEVEL05);
				preparedStatement.executeUpdate();
			}
			for (Iterator iterator = criteria_par_typs.iterator(); iterator.hasNext();) {
				preparedStatement.setString(1, analysis_type_id_code);
				parameter_type_id_code = ((Identifier) iterator.next()).getCode();
				/**
				 * @todo when change DB Identifier model ,change setString() to setLong()
				 */
				preparedStatement.setString(2, parameter_type_id_code);
				parameter_mode = MODE_CRITERION;
				preparedStatement.setString(3, parameter_mode);
				Log.debugMessage("AnalysisType_Database.insertParameterTypes | Inserting parameter type "
						+ parameter_type_id_code + " of parameter mode '" + parameter_mode + "' for analysis type "
						+ analysis_type_id_code, Log.DEBUGLEVEL05);
				preparedStatement.executeUpdate();
			}
			for (Iterator iterator = out_par_typs.iterator(); iterator.hasNext();) {
				preparedStatement.setString(1, analysis_type_id_code);
				parameter_type_id_code = ((Identifier) iterator.next()).getCode();
				/**
				 * @todo when change DB Identifier model ,change setString() to setLong()
				 */
				preparedStatement.setString(2, parameter_type_id_code);
				parameter_mode = MODE_OUT;
				preparedStatement.setString(3, parameter_mode);
				Log.debugMessage("AnalysisType_Database.insertParameterTypes | Inserting parameter type "
						+ parameter_type_id_code + " of parameter mode '" + parameter_mode + "' for analysis type "
						+ analysis_type_id_code, Log.DEBUGLEVEL05);
				preparedStatement.executeUpdate();
			}
		} catch (SQLException sqle) {
			String mesg = "AnalysisType_Database.insertParameterTypes | Cannot insert parameter type "
					+ parameter_type_id_code + " of parameter mode '" + parameter_mode + "' for analysis type "
					+ analysis_type_id_code;
			throw new Exception(mesg, sqle);
		} finally {
			try {
				if (preparedStatement != null) preparedStatement.close();
				preparedStatement = null;
			} catch (SQLException sqle1) {
			}
		}
	}

	public void update(StorableObject storableObject, int update_kind, Object obj) throws Exception {
		AnalysisType analysisType = this.fromStorableObject(storableObject);
		switch (update_kind) {
			default:
				return;
		}
	}

	public void delete(AnalysisType analysisType) {
		String analysis_type_id_str = analysisType.getId().toString();
		Statement statement = null;
		try {
			statement = connection.createStatement();
			statement.executeUpdate("DELETE FROM " + ObjectEntities.ANATYPPARTYPLINK_ENTITY
					+ " WHERE analysis_type_id = " + analysis_type_id_str);
			statement.executeUpdate("DELETE FROM " + ObjectEntities.ANALYSISTYPE_ENTITY + " WHERE " + COLUMN_ID + " = "
					+ analysis_type_id_str);
			connection.commit();
		} catch (SQLException sqle1) {
			Log.errorException(sqle1);
		} finally {
			try {
				if (statement != null) statement.close();
				statement = null;
			} catch (SQLException _ex) {
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
			Log.debugMessage("AnalysisType_Database.retrieveForCodename | Trying: " + sql, Log.DEBUGLEVEL05);
			resultSet = statement.executeQuery(sql);
			if (resultSet.next())
				return new AnalysisType(new Identifier(resultSet.getString(COLUMN_ID)));
			else
				throw new Exception("No analysis type with codename: '" + codename + "'");
		} catch (SQLException sqle) {
			String mesg = "AnalysisType_Database.retrieveForCodename | Cannot retrieve analysis type with codename: '"
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