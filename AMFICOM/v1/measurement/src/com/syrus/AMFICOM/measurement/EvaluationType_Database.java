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

public class EvaluationType_Database extends StorableObject_Database {
	public static final String MODE_IN = "IN";
	public static final String MODE_THRESHOLD = "THS";
	public static final String MODE_ETALON = "ETA";
	public static final String MODE_OUT = "OUT";

	private EvaluationType fromStorableObject(StorableObject storableObject) throws Exception {
		if (storableObject instanceof EvaluationType)
			return (EvaluationType)storableObject;
		else
			throw new Exception("EvaluationType_Database.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	public void retrieve(StorableObject storableObject) throws Exception {
		EvaluationType evaluationType = this.fromStorableObject(storableObject);
		this.retrieveEvaluationType(evaluationType);
		this.retrieveParameterTypes(evaluationType);
	}

	private void retrieveEvaluationType(EvaluationType evaluationType) throws Exception {
		String evaluation_type_id_str = evaluationType.getId().toString();
		String sql = "SELECT "
			+ DatabaseDate.toQuerySubString("created") + ", " 
			+ DatabaseDate.toQuerySubString("modified") + ", "
			+ "creator_id, "
			+ "modifier_id, "
			+ "codename, "
			+ "description"
			+ " FROM " + ObjectEntities.EVALUATIONTYPE_ENTITY
			+ " WHERE id = " + evaluation_type_id_str;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("EvaluationType_Database.retrieveEvaluationType | Trying: " + sql, Log.DEBUGLEVEL05);
			resultSet = statement.executeQuery(sql);
			if (resultSet.next())
				evaluationType.setAttributes(DatabaseDate.fromQuerySubString(resultSet, "created"),
																		 DatabaseDate.fromQuerySubString(resultSet, "modified"),
																		 new Identifier(resultSet.getLong("creator_id")),
																		 new Identifier(resultSet.getLong("modifier_id")),
																		 resultSet.getString("codename"),
																		 resultSet.getString("description"));
			else
				throw new Exception("No such evaluation type: " + evaluation_type_id_str);
		}
		catch (SQLException sqle) {
			String mesg = "EvaluationType_Database.retrieveEvaluationType | Cannot retrieve evaluation type " + evaluation_type_id_str;
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

	private void retrieveParameterTypes(EvaluationType evaluationType) throws Exception {
		ArrayList in_par_typs = new ArrayList();
		ArrayList threshold_par_typs = new ArrayList();
		ArrayList etalon_par_typs = new ArrayList();
		ArrayList out_par_typs = new ArrayList();

		String evaluation_type_id_str = evaluationType.getId().toString();
		String sql = "SELECT "
			+ "parameter_type_id, "
			+ "parameter_mode"
			+ " FROM " + ObjectEntities.EVATYPPARTYPLINK_ENTITY
			+ " WHERE evaluation_type_id = " + evaluation_type_id_str;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("EvaluationType_Database.retrieveParameterTypes | Trying: " + sql, Log.DEBUGLEVEL05);
			resultSet = statement.executeQuery(sql);
			String parameter_mode;
			long parameter_type_id_code;
			while (resultSet.next()) {
				parameter_mode = resultSet.getString("parameter_mode");
				parameter_type_id_code = resultSet.getLong("parameter_type_id");
				if (parameter_mode.equals(MODE_IN))
					in_par_typs.add(new Identifier(parameter_type_id_code));
				else
					if (parameter_mode.equals(MODE_THRESHOLD))
						threshold_par_typs.add(new Identifier(parameter_type_id_code));
					else
						if (parameter_mode.equals(MODE_ETALON))
							etalon_par_typs.add(new Identifier(parameter_type_id_code));
						else
							if (parameter_mode.equals(MODE_OUT))
								out_par_typs.add(new Identifier(parameter_type_id_code));
							else
								Log.errorMessage("EvaluationType_Database.retrieveParameterTypes | ERROR: Unknown parameter mode for parameter_type_id " + parameter_type_id_code);
			}
		}
		catch (SQLException sqle) {
			String mesg = "EvaluationType_Database.retrieveParameterTypes | Cannot retrieve parameter types for evaluation type " + evaluation_type_id_str;
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
		in_par_typs.trimToSize();
		threshold_par_typs.trimToSize();
		etalon_par_typs.trimToSize();
		out_par_typs.trimToSize();
		evaluationType.setParameterTypes(in_par_typs, threshold_par_typs, etalon_par_typs, out_par_typs);
	}

	public Object retrieveObject(StorableObject storableObject, int retrieve_kind, Object arg) throws Exception {
		EvaluationType evaluationType = this.fromStorableObject(storableObject);
		switch (retrieve_kind) {
			default:
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws Exception {
		EvaluationType evaluationType = this.fromStorableObject(storableObject);
		try {
			this.insertEvaluationType(evaluationType);
			this.insertParameterTypes(evaluationType);
		}
		catch (Exception e) {
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

	private void insertEvaluationType(EvaluationType evaluationType) throws Exception {
		String evaluation_type_id_str = evaluationType.getId().toString();
		String sql = "INSERT INTO " + ObjectEntities.EVALUATIONTYPE_ENTITY
			+ " (id, codename, description)"
			+ " VALUES ("
			+ evaluation_type_id_str + ", "
			+ DatabaseDate.toUpdateSubString(evaluationType.getCreated()) + ", "
			+ DatabaseDate.toUpdateSubString(evaluationType.getModified()) + ", "
			+ evaluationType.getCreatorId().toString() + ", "
			+ evaluationType.getModifierId().toString() + ", '"
			+ evaluationType.getCodename() + "', '"
			+ evaluationType.getDescription()
			+ "')";
		Statement statement = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("EvaluationType_Database.insertEvaluationType | Trying: " + sql, Log.DEBUGLEVEL05);
			statement.executeUpdate(sql);
		}
		catch (SQLException sqle) {
			String mesg = "EvaluationType_Database.insertEvaluationType | Cannot insert evaluation type " + evaluation_type_id_str;
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

	private void insertParameterTypes(EvaluationType evaluationType) throws Exception {
		ArrayList in_par_typs = evaluationType.getInParameterTypes();
		ArrayList threshold_par_typs = evaluationType.getThresholdParameterTypes();
		ArrayList etalon_par_typs = evaluationType.getEtalonParameterTypes();
		ArrayList out_par_typs = evaluationType.getOutParameterTypes();
		long evaluation_type_id_code = evaluationType.getId().getCode();
		String sql = "INSERT INTO " + ObjectEntities.EVATYPPARTYPLINK_ENTITY
			+ " (evaluation_type_id, parameter_type_id, parameter_mode)"
			+ " VALUES (?, ?, ?)";
		PreparedStatement preparedStatement = null;
		long parameter_type_id_code = 0;
		String parameter_mode = null;
		try {
			preparedStatement = connection.prepareStatement(sql);
			for (Iterator iterator = in_par_typs.iterator(); iterator.hasNext();) {
				preparedStatement.setLong(1, evaluation_type_id_code);
				parameter_type_id_code = ((Identifier)iterator.next()).getCode();
				preparedStatement.setLong(2, parameter_type_id_code);
				parameter_mode = MODE_IN;
				preparedStatement.setString(3, parameter_mode);
				Log.debugMessage("EvaluationType_Database.insertParameterTypes | Inserting parameter type " + parameter_type_id_code + " of parameter mode '" + parameter_mode + "' for evaluation type " + evaluation_type_id_code, Log.DEBUGLEVEL05);
				preparedStatement.executeUpdate();
			}
			for (Iterator iterator = threshold_par_typs.iterator(); iterator.hasNext();) {
				preparedStatement.setLong(1, evaluation_type_id_code);
				parameter_type_id_code = ((Identifier)iterator.next()).getCode();
				preparedStatement.setLong(2, parameter_type_id_code);
				parameter_mode = MODE_THRESHOLD;
				preparedStatement.setString(3, parameter_mode);
				Log.debugMessage("EvaluationType_Database.insertParameterTypes | Inserting parameter type " + parameter_type_id_code + " of parameter mode '" + parameter_mode + "' for evaluation type " + evaluation_type_id_code, Log.DEBUGLEVEL05);
				preparedStatement.executeUpdate();
			}
			for (Iterator iterator = etalon_par_typs.iterator(); iterator.hasNext();) {
				preparedStatement.setLong(1, evaluation_type_id_code);
				parameter_type_id_code = ((Identifier)iterator.next()).getCode();
				preparedStatement.setLong(2, parameter_type_id_code);
				parameter_mode = MODE_ETALON;
				preparedStatement.setString(3, parameter_mode);
				Log.debugMessage("EvaluationType_Database.insertParameterTypes | Inserting parameter type " + parameter_type_id_code + " of parameter mode '" + parameter_mode + "' for evaluation type " + evaluation_type_id_code, Log.DEBUGLEVEL05);
				preparedStatement.executeUpdate();
			}
			for (Iterator iterator = out_par_typs.iterator(); iterator.hasNext();) {
				preparedStatement.setLong(1, evaluation_type_id_code);
				parameter_type_id_code = ((Identifier)iterator.next()).getCode();
				preparedStatement.setLong(2, parameter_type_id_code);
				parameter_mode = MODE_OUT;
				preparedStatement.setString(3, parameter_mode);
				Log.debugMessage("EvaluationType_Database.insertParameterTypes | Inserting parameter type " + parameter_type_id_code + " of parameter mode '" + parameter_mode + "' for evaluation type " + evaluation_type_id_code, Log.DEBUGLEVEL05);
				preparedStatement.executeUpdate();
			}
		}
		catch (SQLException sqle) {
			String mesg = "EvaluationType_Database.insertParameterTypes | Cannot insert parameter type " + parameter_type_id_code + " of parameter mode '" + parameter_mode + "' for evaluation type " + evaluation_type_id_code;
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
		EvaluationType evaluationType = this.fromStorableObject(storableObject);
		switch (update_kind) {
			default:
				return;
		}
	}

	public void delete(EvaluationType evaluationType) {
		String evaluation_type_id_str = evaluationType.getId().toString();
		Statement statement = null;
		try {
			statement = connection.createStatement();
			statement.executeUpdate("DELETE FROM " + ObjectEntities.EVATYPPARTYPLINK_ENTITY + " WHERE evaluation_type_id = " + evaluation_type_id_str);
			statement.executeUpdate("DELETE FROM " + ObjectEntities.EVALUATIONTYPE_ENTITY + " WHERE id = " + evaluation_type_id_str);
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

	public static EvaluationType retrieveForCodename(String codename) throws Exception {
		String sql = "SELECT "
			+ "id"
			+ " FROM " + ObjectEntities.EVALUATIONTYPE_ENTITY
			+ " WHERE codename = '" + codename + "'";
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("EvaluationType_Database.retrieveForCodename | Trying: " + sql, Log.DEBUGLEVEL05);
			resultSet = statement.executeQuery(sql);
			if (resultSet.next())
				return new EvaluationType(new Identifier(resultSet.getLong("id")));
			else
				throw new Exception("No evaluation type with codename: '" + codename + "'");
		}
		catch (SQLException sqle) {
			String mesg = "EvaluationType_Database.retrieveForCodename | Cannot retrieve evaluation type with codename: '" + codename + "'";
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
}