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
	
	public static final String	COLUMN_CODENAME		= "codename";
	public static final String	COLUMN_DESCRIPTION	= "description";	
	public static final String	COLUMN_NAME			= "name";	

	private ParameterType fromStorableObject(StorableObject storableObject) throws Exception {
		if (storableObject instanceof ParameterType)
			return (ParameterType)storableObject;
		else
			throw new Exception("ParameterType_Database.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	public void retrieve(StorableObject storableObject) throws Exception {
		ParameterType parameterType = this.fromStorableObject(storableObject);

		String parameter_type_id_str = parameterType.getId().toSQLString();
		String sql = SQL_SELECT
			+ COLUMN_CODENAME
			+ COMMA
			+ COLUMN_NAME
			+ COMMA
			+ COLUMN_DESCRIPTION
			+ SQL_FROM
			+ ObjectEntities.PARAMETERTYPE_ENTITY
			+ SQL_WHERE
			+ COLUMN_ID
			+ EQUALS
			+ parameter_type_id_str;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("ParameterType_Database.retrieve | Trying: " + sql, Log.DEBUGLEVEL05);
			resultSet = statement.executeQuery(sql);
			if (resultSet.next())
				parameterType.setAttributes(resultSet.getString(COLUMN_CODENAME),
											resultSet.getString(COLUMN_NAME),
											resultSet.getString(COLUMN_DESCRIPTION));
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

		String parameter_type_id_str = parameterType.getId().toSQLString();
		String sql = SQL_INSERT_INTO
			+ ObjectEntities.PARAMETERTYPE_ENTITY
			+ OPEN_BRACKET
			+ COLUMN_ID
			+ COMMA
			+ COLUMN_CODENAME
			+ COMMA
			+ COLUMN_NAME
			+ COMMA
			+ COLUMN_DESCRIPTION
			+ CLOSE_BRACKET
			+ SQL_VALUES
			+ OPEN_BRACKET
			+ parameter_type_id_str
			+ COMMA
			+ APOSTOPHE
			+ parameterType.getCodename()
			+ APOSTOPHE
			+ COMMA
			+ APOSTOPHE
			+ parameterType.getName()
			+ APOSTOPHE
			+ COMMA
			+ APOSTOPHE
			+ parameterType.getDescription()
			+ APOSTOPHE
			+ CLOSE_BRACKET;
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
		String sql = SQL_SELECT
			+ COLUMN_ID
			+ SQL_FROM
			+ ObjectEntities.PARAMETERTYPE_ENTITY
			+ SQL_WHERE
			+ COLUMN_CODENAME
			+ EQUALS
			+ APOSTOPHE
			+ codename
			+ APOSTOPHE;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("ParameterType_Database.retrieveForCodename | Trying: " + sql, Log.DEBUGLEVEL05);
			resultSet = statement.executeQuery(sql);
			if (resultSet.next()){
				/**
				 * @todo when change DB Identifier model ,change getString() to getLong()
				 */
				return new ParameterType(new Identifier(resultSet.getString(COLUMN_ID)));
			}
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