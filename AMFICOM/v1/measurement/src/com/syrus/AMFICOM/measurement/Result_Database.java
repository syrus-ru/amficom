package com.syrus.AMFICOM.measurement;

import java.util.ArrayList;
import java.util.Iterator;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import oracle.sql.BLOB;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.ByteArrayDatabase;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObject_Database;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.measurement.corba.ResultSort;


public class Result_Database extends StorableObject_Database {
	
//	 measurement_id VARCHAR2(32) NOT NULL,
	public static final String COLUMN_MEASUREMENT_ID		=	"measurement_id";
//	 analysis_id VARCHAR2(32),
	public static final String COLUMN_ANALYSIS_ID			=	"analysis_id";
//	 evaluation_id VARCHAR2(32),
	public static final String COLUMN_EVALUATION_ID			=	"evaluation_id";
//	 sort NUMBER(2, 0) NOT NULL,
	public static final String COLUMN_SORT					=	"sort";
//	 alarm_level NUMBER(2, 0) NOT NULL,
	public static final String COLUMN_ALARM_LEVEL			=	"alarm_level";
	
	public static final String LINK_COLUMN_TYPE_ID			=	"type_id";
	public static final String LINK_COLUMN_RESULT_ID		=	"result_id";
	public static final String LINK_COLUMN_VALUE			=	"value";

	private Result fromStorableObject(StorableObject storableObject) throws Exception {
		if (storableObject instanceof Result)
			return (Result)storableObject;
		else
			throw new Exception("Result_Database.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	public void retrieve(StorableObject storableObject) throws Exception {
		Result result = this.fromStorableObject(storableObject);
		this.retrieveResult(result);
		this.retrieveResultParameters(result);
	}

	private void retrieveResult(Result result) throws Exception {
		String result_id_str = result.getId().toSQLString();
		String sql = SQL_SELECT
			+ DatabaseDate.toQuerySubString(COLUMN_CREATED) + COMMA 
			+ DatabaseDate.toQuerySubString(COLUMN_MODIFIED) + COMMA
			+ COLUMN_CREATOR_ID + COMMA
			+ COLUMN_MODIFIER_ID + COMMA
			+ COLUMN_MEASUREMENT_ID + COMMA
			+ COLUMN_ANALYSIS_ID + COMMA
			+ COLUMN_EVALUATION_ID + COMMA
			+ COLUMN_SORT + COMMA
			+ COLUMN_ALARM_LEVEL
			+ SQL_FROM + ObjectEntities.RESULT_ENTITY
			+ SQL_WHERE 
			+ COLUMN_ID + EQUALS
			+ result_id_str;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("Result_Database.retrieveResult | Trying: " + sql, Log.DEBUGLEVEL05);
			resultSet = statement.executeQuery(sql);
			if (resultSet.next()) {
				/**
				 * @todo when change DB Identifier model ,change getString() to getLong()
				 */
				Measurement measurement = new Measurement(new Identifier(resultSet.getString(COLUMN_MEASUREMENT_ID)));
				int result_sort = resultSet.getInt(COLUMN_SORT);
				Action action = null;
				switch (result_sort) {
					case ResultSort._RESULT_SORT_MEASUREMENT:
						action = measurement;
						break;
					case ResultSort._RESULT_SORT_ANALYSIS:
						/**
						 * @todo when change DB Identifier model ,change getString() to getLong()
						 */
						action = new Analysis(new Identifier(resultSet.getString(COLUMN_ANALYSIS_ID)));
						break;
					case ResultSort._RESULT_SORT_EVALUATION:
						/**
						 * @todo when change DB Identifier model ,change getString() to getLong()
						 */
						action = new Evaluation(new Identifier(resultSet.getString(COLUMN_EVALUATION_ID)));
						break;
					default:
						Log.errorMessage("Unkown sort: " + result_sort + " of result " + result_id_str);
				}
				result.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
														 DatabaseDate.fromQuerySubString(resultSet, COLUMN_MODIFIED),
														 /**
														  * @todo when change DB Identifier model ,change getString() to getLong()
														  */
														 new Identifier(resultSet.getString(COLUMN_CREATOR_ID)),
														 /**
														  * @todo when change DB Identifier model ,change getString() to getLong()
														  */
														 new Identifier(resultSet.getString(COLUMN_MODIFIER_ID)),
														 measurement,
														 action,
														 result_sort,
														 resultSet.getInt(COLUMN_ALARM_LEVEL));
			}
			else
				throw new Exception("No such result: " + result_id_str);
		}
		catch (SQLException sqle) {
			String mesg = "Result_Database.retrieveResult | Cannot retrieve result " + result_id_str;
			throw new Exception(mesg, sqle);
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
			catch (SQLException sqle1) {}
		}
	}

	private void retrieveResultParameters(Result result) throws Exception {
		String result_id_str = result.getId().toSQLString();
		String sql = SQL_SELECT
			+ COLUMN_ID + COMMA
			+ LINK_COLUMN_TYPE_ID + COMMA
			+ LINK_COLUMN_VALUE
			+ SQL_FROM
			+ ObjectEntities.RESULTPARAMETER_ENTITY
			+ SQL_WHERE
			+ LINK_COLUMN_RESULT_ID + EQUALS
			+ result_id_str;
		Statement statement = null;
		ResultSet resultSet = null;
		ArrayList arraylist = new ArrayList();
		try {
			statement = connection.createStatement();
			Log.debugMessage("Result_Database.retrieveResultParameters | Trying: " + sql, Log.DEBUGLEVEL05);
			resultSet = statement.executeQuery(sql);
			while (resultSet.next())
							/**
							 * @todo when change DB Identifier model ,change getString() to getLong()
							 */
				arraylist.add(new SetParameter(	new Identifier(resultSet.getString(COLUMN_ID)),
							/**
							 * @todo when change DB Identifier model ,change getString() to getLong()
							 */
												new Identifier(resultSet.getString(LINK_COLUMN_TYPE_ID)),
												ByteArrayDatabase.toByteArray((BLOB)resultSet.getBlob(LINK_COLUMN_VALUE))));
		}
		catch (SQLException sqle) {
			String mesg = "Result_Database.retrieveResultParameters | Cannot retrieve parameters for result " + result_id_str;
			throw new Exception(mesg, sqle);
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
			catch (SQLException sqle1) {}
		}
		result.setParameters((SetParameter[])arraylist.toArray(new SetParameter[arraylist.size()]));
	}

	public Object retrieveObject(StorableObject storableObject, int retrieve_kind, Object arg) throws Exception {
		Result result = this.fromStorableObject(storableObject);
		switch (retrieve_kind) {
			default:
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws Exception {
		Result result = this.fromStorableObject(storableObject);
		try {
			this.insertResult(result);
			this.insertResultParameters(result);
		}
		catch (Exception e) {
			this.delete(result);
			throw e;
		}
	}

	private void insertResult(Result result) throws Exception {
		String result_id_str = result.getId().toSQLString();
		String sql;
		{			
			StringBuffer buffer = new StringBuffer(SQL_INSERT_INTO);
			buffer.append(ObjectEntities.RESULT_ENTITY);
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
			buffer.append(COLUMN_MEASUREMENT_ID);
			buffer.append(COMMA);
			buffer.append(COLUMN_ANALYSIS_ID);
			buffer.append(COMMA);
			buffer.append(COLUMN_EVALUATION_ID);
			buffer.append(COMMA);
			buffer.append(COLUMN_SORT);
			buffer.append(COMMA);
			buffer.append(COLUMN_ALARM_LEVEL);
			buffer.append(CLOSE_BRACKET);
			buffer.append(SQL_VALUES);
			buffer.append(OPEN_BRACKET);			
			buffer.append(result_id_str);
			buffer.append(COMMA);
			buffer.append(DatabaseDate.toUpdateSubString(result.getCreated()));
			buffer.append(COMMA);
			buffer.append(DatabaseDate.toUpdateSubString(result.getModified()));
			buffer.append(COMMA);
			buffer.append(result.getCreatorId().toSQLString());
			buffer.append(COMMA);
			buffer.append(result.getModifierId().toSQLString());
			buffer.append(COMMA);
			buffer.append(result.getMeasurement().getId().toSQLString());
			buffer.append(COMMA);
		int result_sort = result.getSort().value();
		switch (result_sort) {
			case ResultSort._RESULT_SORT_MEASUREMENT:				
				buffer.append(Identifier.getNullSQLString());
				buffer.append(COMMA);				
				buffer.append(Identifier.getNullSQLString());
				buffer.append(COMMA);
				break;
			case ResultSort._RESULT_SORT_ANALYSIS:
				buffer.append(result.getAction().getId().toSQLString());
				buffer.append(COMMA);				
				buffer.append(Identifier.getNullSQLString());
				buffer.append(COMMA);
				break;
			case ResultSort._RESULT_SORT_EVALUATION:				
				buffer.append(Identifier.getNullSQLString());
				buffer.append(COMMA);
				buffer.append(result.getAction().getId().toSQLString());
				buffer.append(COMMA);
				break;
		}
			buffer.append(Integer.toString(result_sort));
			buffer.append(COMMA);
			buffer.append(Integer.toString(result.getAlarmLevel().value()));
			buffer.append(CLOSE_BRACKET);
			sql = buffer.toString();
		}
		Statement statement = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("Result_Database.insertResult | Trying: " + sql, Log.DEBUGLEVEL05);
			statement.executeUpdate(sql);
			connection.commit();
		}
		catch (SQLException sqle) {
			String mesg = "Result_Database.insertResult | Cannot insert result " + result_id_str;
			throw new Exception(mesg, sqle);
		}
		finally {
			try {
				if (statement != null)
					statement.close();
				statement = null;
			}
			catch (SQLException sqle1) {}
		}
	}

	private void insertResultParameters(Result result) throws Exception {
		/**
		 * @todo when change DB Identifier model ,change String to long
		 */
		String result_id_code = result.getId().getCode();
		SetParameter[] setParameters = result.getParameters();
		String sql = SQL_INSERT_INTO 
			+ ObjectEntities.RESULTPARAMETER_ENTITY
			+ OPEN_BRACKET
			+ COLUMN_ID + COMMA
			+ LINK_COLUMN_TYPE_ID + COMMA
			+ LINK_COLUMN_RESULT_ID + COMMA
			+ LINK_COLUMN_VALUE + COMMA
			+ CLOSE_BRACKET
			+ SQL_VALUES
			+ OPEN_BRACKET
			+ QUESTION + COMMA
			+ QUESTION + COMMA
			+ QUESTION + COMMA
			+ QUESTION 
			+ CLOSE_BRACKET;
		PreparedStatement preparedStatement = null;
		int i = 0;
		try {
			preparedStatement = connection.prepareStatement(sql);
			for (i = 0; i < setParameters.length; i++) {
				/**
				 * @todo when change DB Identifier model ,change setString() to setLong()
				 */
				preparedStatement.setString(1, setParameters[i].getId().getCode());
				/**
				 * @todo when change DB Identifier model ,change setString() to setLong()
				 */
				preparedStatement.setString(2, setParameters[i].getTypeId().getCode());
				/**
				 * @todo when change DB Identifier model ,change setString() to setLong()
				 */
				preparedStatement.setString(3, result_id_code);
				preparedStatement.setBlob(4, BLOB.empty_lob());
				Log.debugMessage("Result_Database.insertResultParameters | Inserting parameter " + setParameters[i].getTypeId().toString() + " for result " + result_id_code, Log.DEBUGLEVEL05);
				preparedStatement.executeUpdate();
				ByteArrayDatabase badb = new ByteArrayDatabase(setParameters[i].getValue());
				badb.saveAsBlob(connection, 
								ObjectEntities.RESULTPARAMETER_ENTITY, 
								LINK_COLUMN_VALUE, COLUMN_ID + EQUALS + setParameters[i].getId().toSQLString());
			}
			connection.commit();
		}
		catch (SQLException sqle) {
			String mesg = "Result_Database.insertResultParameters | Cannot insert parameter " + setParameters[i].getId().toString() + " of type " + setParameters[i].getTypeId().toString() + " for result " + result_id_code;
			throw new Exception(mesg, sqle);
		}
		finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
				preparedStatement = null;
			}
			catch (SQLException sqle1) {}
		}
	}

	public void update(StorableObject storableObject, int update_kind, Object obj) throws Exception {
		Result result = this.fromStorableObject(storableObject);
		switch (update_kind) {
			default:
				return;
		}
	}

	private void delete(Result result) {
		String result_id_str = result.getId().toSQLString();
		Statement statement = null;
		try {
			statement = connection.createStatement();
			statement.executeUpdate(SQL_DELETE_FROM 
									+ ObjectEntities.RESULTPARAMETER_ENTITY
									+ SQL_WHERE
									+ LINK_COLUMN_RESULT_ID
									+ EQUALS
									+ result_id_str);
			statement.executeUpdate(SQL_DELETE_FROM 
									+ ObjectEntities.RESULT_ENTITY
									+ SQL_WHERE
									+ COLUMN_ID
									+ EQUALS
									+ result_id_str);
			connection.commit();
		}
		catch (SQLException sqle1) {
			Log.errorException(sqle1);
		}
		finally {
			try {
				if(statement != null)
					statement.close();
				statement = null;
			}
			catch(SQLException _ex) { }
		}
	}
}