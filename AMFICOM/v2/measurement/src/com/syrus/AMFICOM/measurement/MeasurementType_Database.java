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

public class MeasurementType_Database extends StorableObject_Database  {
	public static final String MODE_IN = "IN";
	public static final String MODE_OUT = "OUT";

	private MeasurementType fromStorableObject(StorableObject storableObject) throws Exception {
		if (storableObject instanceof MeasurementType)
			return (MeasurementType)storableObject;
		else
			throw new Exception("MeasurementType_Database.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	public void retrieve(StorableObject storableObject) throws Exception {
		MeasurementType measurementType = this.fromStorableObject(storableObject);
		this.retrieveMeasurementType(measurementType);
		this.retrieveParameterTypes(measurementType);
	}

	private void retrieveMeasurementType(MeasurementType measurementType) throws Exception {
		String measurement_type_id_str = measurementType.getId().toString();
		String sql = "SELECT "
			+ DatabaseDate.toQuerySubString("created") + ", " 
			+ DatabaseDate.toQuerySubString("modified") + ", "
			+ "creator_id, "
			+ "modifier_id, "
			+ "codename, "
			+ "description"
			+ " FROM " + ObjectEntities.MEASUREMENTTYPE_ENTITY
			+ " WHERE id = " + measurement_type_id_str;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("MeasurementType_Database.retrieveMeasurementType | Trying: " + sql, Log.DEBUGLEVEL05);
			resultSet = statement.executeQuery(sql);
			if (resultSet.next())
				measurementType.setAttributes(DatabaseDate.fromQuerySubString(resultSet, "created"),
																			DatabaseDate.fromQuerySubString(resultSet, "modified"),
																			new Identifier(resultSet.getLong("creator_id")),
																			new Identifier(resultSet.getLong("modifier_id")),
																			resultSet.getString("codename"),
																			resultSet.getString("description"));
			else
				throw new Exception("No such measurement type: " + measurement_type_id_str);
		}
		catch (SQLException sqle) {
			String mesg = "MeasurementType_Database.retrieveMeasurementType | Cannot retrieve measurement type " + measurement_type_id_str;
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

	private void retrieveParameterTypes(MeasurementType measurementType) throws Exception {
		ArrayList in_par_typs = new ArrayList();
		ArrayList out_par_typs = new ArrayList();

		String measurement_type_id_str = measurementType.getId().toString();
		String sql = "SELECT "
			+ "parameter_type_id, "
			+ "parameter_mode"
			+ " FROM " + ObjectEntities.MNTTYPPARTYPLINK_ENTITY
			+ " WHERE measurement_type_id = " + measurement_type_id_str;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("MeasurementType_Database.retrieveParameterTypes | Trying: " + sql, Log.DEBUGLEVEL05);
			resultSet = statement.executeQuery(sql);
			String parameter_mode;
			long parameter_type_id_code;
			while (resultSet.next()) {
				parameter_mode = resultSet.getString("parameter_mode");
				parameter_type_id_code = resultSet.getLong("parameter_type_id");
				if (parameter_mode.equals(MODE_IN))
					in_par_typs.add(new Identifier(parameter_type_id_code));
				else
					if (parameter_mode.equals(MODE_OUT))
						out_par_typs.add(new Identifier(parameter_type_id_code));
					else
						Log.errorMessage("MeasurementType_Database.retrieveParameterTypes | ERROR: Unknown parameter mode for parameter_type_id " + parameter_type_id_code);
			}
		}
		catch (SQLException sqle) {
			String mesg = "MeasurementType_Database.retrieveParameterTypes | Cannot retrieve parameter types for measurement type " + measurement_type_id_str;
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
		out_par_typs.trimToSize();
		measurementType.setParameterTypes(in_par_typs, out_par_typs);
	}

	public Object retrieveObject(StorableObject storableObject, int retrieve_kind, Object arg) throws Exception {
		MeasurementType measurementType = this.fromStorableObject(storableObject);
		switch (retrieve_kind) {
			default:
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws Exception {
		MeasurementType measurementType = this.fromStorableObject(storableObject);
		try {
			this.insertMeasurementType(measurementType);
			this.insertParameterTypes(measurementType);
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

	private void insertMeasurementType(MeasurementType measurementType) throws Exception {
		String measurement_type_id_str = measurementType.getId().toString();
		String sql = "INSERT INTO " + ObjectEntities.MEASUREMENTTYPE_ENTITY
			+ " (id, created, modified, creator_id, modifier_id, codename, description)"
			+ " VALUES ("
			+ measurement_type_id_str + ", "
			+ DatabaseDate.toUpdateSubString(measurementType.getCreated()) + ", "
			+ DatabaseDate.toUpdateSubString(measurementType.getModified()) + ", "
			+ measurementType.getCreatorId().toString() + ", "
			+ measurementType.getModifierId().toString() + ", '"
			+ measurementType.getCodename() + "', '"
			+ measurementType.getDescription()
			+ "')";
		Statement statement = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("MeasurementType_Database.insertMeasurementType | Trying: " + sql, Log.DEBUGLEVEL05);
			statement.executeUpdate(sql);
		}
		catch (SQLException sqle) {
			String mesg = "MeasurementType_Database.insertMeasurementType | Cannot insert measurement type " + measurement_type_id_str;
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

	private void insertParameterTypes(MeasurementType measurementType) throws Exception {
		ArrayList in_par_typs = measurementType.getInParameterTypes();
		ArrayList out_par_typs = measurementType.getOutParameterTypes();
		long measurement_type_id_code = measurementType.getId().getCode();
		String sql = "INSERT INTO " + ObjectEntities.MNTTYPPARTYPLINK_ENTITY
			+ " (measurement_type_id, parameter_type_id, parameter_mode)"
			+ " VALUES (?, ?, ?)";
		PreparedStatement preparedStatement = null;
		long parameter_type_id_code = 0;
		String parameter_mode = null;
		try {
			preparedStatement = connection.prepareStatement(sql);
			for (Iterator iterator = in_par_typs.iterator(); iterator.hasNext();) {
				preparedStatement.setLong(1, measurement_type_id_code);
				parameter_type_id_code = ((Identifier)iterator.next()).getCode();
				preparedStatement.setLong(2, parameter_type_id_code);
				parameter_mode = MODE_IN;
				preparedStatement.setString(3, parameter_mode);
				Log.debugMessage("MeasurementType_Database.insertParameterTypes | Inserting parameter type " + parameter_type_id_code + " of parameter mode '" + parameter_mode + "' for measurement type " + measurement_type_id_code, Log.DEBUGLEVEL05);
				preparedStatement.executeUpdate();
			}
			for (Iterator iterator = out_par_typs.iterator(); iterator.hasNext();) {
				preparedStatement.setLong(1, measurement_type_id_code);
				parameter_type_id_code = ((Identifier)iterator.next()).getCode();
				preparedStatement.setLong(2, parameter_type_id_code);
				parameter_mode = MODE_OUT;
				preparedStatement.setString(3, parameter_mode);
				Log.debugMessage("MeasurementType_Database.insertParameterTypes | Inserting parameter type " + parameter_type_id_code + " of parameter mode '" + parameter_mode + "' for measurement type " + measurement_type_id_code, Log.DEBUGLEVEL05);
				preparedStatement.executeUpdate();
			}
		}
		catch (SQLException sqle) {
			String mesg = "MeasurementType_Database.insertParameterTypes | Cannot insert parameter type " + parameter_type_id_code + " of parameter mode '" + parameter_mode + "' for measurement type " + measurement_type_id_code;
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
		MeasurementType measurementType = this.fromStorableObject(storableObject);
		switch (update_kind) {
			default:
				return;
		}
	}

	public void delete(MeasurementType measurementType) {
		String measurement_type_id_str = measurementType.getId().toString();
		Statement statement = null;
		try {
			statement = connection.createStatement();
			statement.executeUpdate("DELETE FROM " + ObjectEntities.MNTTYPPARTYPLINK_ENTITY + " WHERE measurement_type_id = " + measurement_type_id_str);
			statement.executeUpdate("DELETE FROM " + ObjectEntities.MEASUREMENTTYPE_ENTITY + " WHERE id = " + measurement_type_id_str);
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

	public static MeasurementType retrieveForCodename(String codename) throws Exception {
		String sql = "SELECT "
			+ "id"
			+ " FROM " + ObjectEntities.MEASUREMENTTYPE_ENTITY
			+ " WHERE codename = '" + codename + "'";
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("MeasurementType_Database.retrieveForCodename | Trying: " + sql, Log.DEBUGLEVEL05);
			resultSet = statement.executeQuery(sql);
			if (resultSet.next())
				return new MeasurementType(new Identifier(resultSet.getLong("id")));
			else
				throw new Exception("No measurement type with codename: '" + codename + "'");
		}
		catch (SQLException sqle) {
			String mesg = "MeasurementType_Database.retrieveForCodename | Cannot retrieve measurement type with codename: '" + codename + "'";
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