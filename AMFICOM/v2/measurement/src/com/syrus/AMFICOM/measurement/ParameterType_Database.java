package com.syrus.AMFICOM.measurement;

import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObject_Database;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.util.Log;

public class ParameterType_Database extends StorableObject_Database  {

	private ParameterType fromStorableObject(StorableObject storableObject) throws Exception {
		if (storableObject instanceof ParameterType)
			return (ParameterType)storableObject;
		else
			throw new Exception("ParameterType_Database.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	public void retrieve(StorableObject storableObject) throws Exception {
		ParameterType parameterType = this.fromStorableObject(storableObject);

		String parameter_type_id_str = parameterType.getId().toString();
		String sql = "SELECT codename, name, description FROM " + ObjectEntities.PARAMETERTYPE_ENTITY + " WHERE id = " + parameter_type_id_str;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("ParameterType_Database.retrieve | Trying: " + sql, Log.DEBUGLEVEL05);
			resultSet = statement.executeQuery(sql);
			if (resultSet.next())
				parameterType.setAttributes(resultSet.getString("codename"),
																		resultSet.getString("name"),
																		resultSet.getString("description"));
			else
				throw new Exception("No such parameter type: " + parameter_type_id_str);
		}
		catch (SQLException sqle) {
			String mesg = "ParameterType_Database.retrieve | Cannot retrieve parameter type " + parameter_type_id_str;
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

	public Object retrieveObject(StorableObject storableObject, int retrieve_kind, Object arg) throws Exception {
		ParameterType parameterType = null;
		if (storableObject instanceof ParameterType)
			parameterType = (ParameterType)storableObject;
		else
			throw new Exception("ParameterType_Database.retrieveObject | Illegal Storable Object: " + storableObject.getClass().getName());

		switch (retrieve_kind) {
			default:
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws Exception {
		ParameterType parameterType = null;
		if (storableObject instanceof ParameterType)
			parameterType = (ParameterType)storableObject;
		else
			throw new Exception("ParameterType_Database.insert | Illegal Storable Object: " + storableObject.getClass().getName());

		String parameter_type_id_str = parameterType.getId().toString();
		String sql = "INSERT INTO " + ObjectEntities.PARAMETERTYPE_ENTITY + " (id, codename, name, description) VALUES (" + parameter_type_id_str + ", '" + parameterType.getCodename() + "', '" + parameterType.getName() + "', '" + parameterType.getDescription() + "')";
		Statement statement = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("ParameterType_Database.insert | Trying: " + sql, Log.DEBUGLEVEL05);
			statement.executeUpdate(sql);
			connection.commit();
		}
		catch (SQLException sqle) {
			String mesg = "ParameterType_Database.insert | Cannot insert parameter type " + parameter_type_id_str;
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

	public void update(StorableObject storableObject, int update_kind, Object obj) throws Exception {
		ParameterType parameterType = null;
		if (storableObject instanceof ParameterType)
			parameterType = (ParameterType)storableObject;
		else
			throw new Exception("ParameterType_Database.update | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	public static ParameterType retrieveForCodename(String codename) throws Exception {
		String sql = "SELECT id FROM " + ObjectEntities.PARAMETERTYPE_ENTITY + " WHERE codename = '" + codename + "'";
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("ParameterType_Database.retrieveForCodename | Trying: " + sql, Log.DEBUGLEVEL05);
			resultSet = statement.executeQuery(sql);
			if (resultSet.next())
				return new ParameterType(new Identifier(resultSet.getLong("id")));
			else
				throw new Exception("No such parameter type with codename: '" + codename + "'");
		}
		catch (SQLException sqle) {
			String mesg = "ParameterType_Database.retrieveForCodename | Cannot retrieve parameter type with codename: '" + codename + "'";
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