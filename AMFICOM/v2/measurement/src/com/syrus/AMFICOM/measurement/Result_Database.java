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
import com.syrus.AMFICOM.general.StorableObject_DatabaseContext;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.measurement.corba.ResultSort;


public class Result_Database extends StorableObject_Database {

	public void retrieve(StorableObject storableObject) throws Exception {
		Result result = null;
		if (storableObject instanceof Result)
			result = (Result)storableObject;
		else
			throw new Exception("Result_Database.retrieve | Illegal Storable Object: " + storableObject.getClass().getName());

		this.retrieveResult(result);
		this.retrieveResultParameters(result);
	}

	private void retrieveResult(Result result) throws Exception {
		String result_id_str = result.getId().toString();
		String sql = "SELECT measurement_id, analysis_id, evaluation_id, sort, alarm_level FROM " + ObjectEntities.RESULT_ENTITY + " WHERE id = " + result_id_str;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("Result_Database.retrieveResult | Trying: " + sql, Log.DEBUGLEVEL05);
			resultSet = statement.executeQuery(sql);
			if (resultSet.next()) {
				Measurement measurement = new Measurement(new Identifier(resultSet.getLong("measurement_id")));
				int result_sort = resultSet.getInt("sort");
				Action action = null;
				switch (result_sort) {
					case ResultSort._RESULT_SORT_MEASUREMENT:
						action = measurement;
						break;
					case ResultSort._RESULT_SORT_ANALYSIS:
						action = new Analysis(new Identifier(resultSet.getLong("analysis_id")));
						break;
					case ResultSort._RESULT_SORT_EVALUATION:
						action = new Evaluation(new Identifier(resultSet.getLong("evaluation_id")));
						break;
					default:
						Log.errorMessage("Unkown sort: " + result_sort + " of result " + result_id_str);
				}
				result.setAttributes(measurement,
														 action,
														 result_sort,
														 resultSet.getInt("alarm_level"));
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
		String result_id_str = result.getId().toString();
		String sql = "SELECT id, type_id, value FROM " + ObjectEntities.RESULTPARAMETER_ENTITY + " WHERE result_id = " + result_id_str;
		Statement statement = null;
		ResultSet resultSet = null;
		ArrayList arraylist = new ArrayList();
		try {
			statement = connection.createStatement();
			Log.debugMessage("Result_Database.retrieveResultParameters | Trying: " + sql, Log.DEBUGLEVEL05);
			resultSet = statement.executeQuery(sql);
			while (resultSet.next())
				arraylist.add(new SetParameter(new Identifier(resultSet.getLong("id")),
																			 new Identifier(resultSet.getLong("type_id")),
																			 ByteArrayDatabase.toByteArray((BLOB)resultSet.getBlob("value"))));
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
		Result result = null;
		if (storableObject instanceof Result)
			result = (Result)storableObject;
		else
			throw new Exception("Result_Database.retrieveObject | Illegal Storable Object: " + storableObject.getClass().getName());

		switch (retrieve_kind) {
			default:
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws Exception {
		Result result = null;
		if (storableObject instanceof Result)
			result = (Result)storableObject;
		else
			throw new Exception("Result_Database.insert | Illegal Storable Object: " + storableObject.getClass().getName());

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
		String result_id_str = result.getId().toString();
		String sql = "INSERT INTO " + ObjectEntities.RESULT_ENTITY + " (id, measurement_id, analysis_id, evaluation_id, sort, alarm_level) VALUES (" + result_id_str + ", " + result.getMeasurement().getId().toString() + ", ";
		int result_sort = result.getSort().value();
		switch (result_sort) {
			case ResultSort._RESULT_SORT_MEASUREMENT:
				sql = sql + "0, 0, ";
				break;
			case ResultSort._RESULT_SORT_ANALYSIS:
				sql = sql + result.getAction().getId().toString() + ", 0, ";
				break;
			case ResultSort._RESULT_SORT_EVALUATION:
				sql = sql + "0, " + result.getAction().getId().toString() + ", ";
				break;
		}
		sql = sql + Integer.toString(result_sort) + ", " + result.getAlarmLevel().value() + ")";
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
		long result_id_code = result.getId().getCode();
		SetParameter[] setParameters = result.getParameters();
		String sql = "INSERT INTO " + ObjectEntities.RESULTPARAMETER_ENTITY + " id, type_id, result_id, value) VALUES (?, ?, ?, ?)";
		PreparedStatement preparedStatement = null;
		int i = 0;
		try {
			preparedStatement = connection.prepareStatement(sql);
			for (i = 0; i < setParameters.length; i++) {
				preparedStatement.setLong(1, setParameters[i].getId().getCode());
				preparedStatement.setLong(2, setParameters[i].getTypeId().getCode());
				preparedStatement.setLong(3, result_id_code);
				preparedStatement.setBlob(4, BLOB.empty_lob());
				Log.debugMessage("Result_Database.insertResultParameters | Inserting parameter " + setParameters[i].getTypeId().toString() + " for result " + result_id_code, Log.DEBUGLEVEL05);
				preparedStatement.executeUpdate();
				ByteArrayDatabase badb = new ByteArrayDatabase(setParameters[i].getValue());
				badb.saveAsBlob(connection, ObjectEntities.RESULTPARAMETER_ENTITY, "value", "id = " + setParameters[i].getId().toString());
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
		Result result = null;
		if (storableObject instanceof Result)
			result = (Result)storableObject;
		else
			throw new Exception("Result_Database.update | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	private void delete(Result result) {
		String result_id_str = result.getId().toString();
		Statement statement = null;
		try {
			statement = connection.createStatement();
			statement.executeUpdate("DELETE FROM " + ObjectEntities.RESULTPARAMETER_ENTITY + " WHERE result_id = " + result_id_str);
			statement.executeUpdate("DELETE FROM " + ObjectEntities.RESULT_ENTITY + " WHERE id = " + result_id_str);
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