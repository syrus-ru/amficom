package com.syrus.AMFICOM.measurement;

import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObject_Database;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseDate;

public class ParameterTypeDatabase extends StorableObject_Database  {
	
	public static final String	COLUMN_CODENAME		= "codename";
	public static final String	COLUMN_DESCRIPTION	= "description";	
	public static final String	COLUMN_NAME			= "name";	

	private ParameterType fromStorableObject(StorableObject storableObject) throws Exception {
		if (storableObject instanceof ParameterType)
			return (ParameterType)storableObject;
		else
			throw new Exception("ParameterTypeDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	public void retrieve(StorableObject storableObject) throws Exception {
		ParameterType parameterType = this.fromStorableObject(storableObject);

		String parameterTypeIdStr = parameterType.getId().toSQLString();
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
			+ parameterTypeIdStr;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("ParameterTypeDatabase.retrieve | Trying: " + sql, Log.DEBUGLEVEL05);
			resultSet = statement.executeQuery(sql);
			if (resultSet.next()){
				parameterType.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
											DatabaseDate.fromQuerySubString(resultSet, COLUMN_MODIFIED),
											/**
											 * @todo when change DB Identifier model ,change getString() to
											 *       getLong()
											 */																			
											new Identifier(resultSet.getString(COLUMN_CREATOR_ID)),
											/**
											 * @todo when change DB Identifier model ,change getString() to
											 *       getLong()
											 */
											new Identifier(resultSet.getString(COLUMN_MODIFIER_ID)),
											resultSet.getString(COLUMN_CODENAME),
											resultSet.getString(COLUMN_DESCRIPTION),
											resultSet.getString(COLUMN_NAME));
			}
			else
				throw new Exception("No such parameter type: " + parameterTypeIdStr);
		}
		catch (SQLException sqle) {
			String mesg = "ParameterTypeDatabase.retrieve | Cannot retrieve parameter type " + parameterTypeIdStr;
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

	public Object retrieveObject(StorableObject storableObject, int retrieveKind, Object arg) throws Exception {
		ParameterType parameterType = null;
		if (storableObject instanceof ParameterType)
			parameterType = (ParameterType)storableObject;
		else
			throw new Exception("ParameterTypeDatabase.retrieveObject | Illegal Storable Object: " + storableObject.getClass().getName());

		switch (retrieveKind) {
			default:
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws Exception {
		ParameterType parameterType = null;
		if (storableObject instanceof ParameterType)
			parameterType = (ParameterType)storableObject;
		else
			throw new Exception("ParameterTypeDatabase.insert | Illegal Storable Object: " + storableObject.getClass().getName());

		String parameterTypeIdStr = parameterType.getId().toSQLString();
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
			+ parameterTypeIdStr
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
			Log.debugMessage("ParameterTypeDatabase.insert | Trying: " + sql, Log.DEBUGLEVEL05);
			statement.executeUpdate(sql);
			connection.commit();
		}
		catch (SQLException sqle) {
			String mesg = "ParameterTypeDatabase.insert | Cannot insert parameter type " + parameterTypeIdStr;
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

	public void update(StorableObject storableObject, int updateKind, Object obj) throws Exception {
		ParameterType parameterType = null;
		if (storableObject instanceof ParameterType)
			parameterType = (ParameterType)storableObject;
		else
			throw new Exception("ParameterTypeDatabase.update | Illegal Storable Object: " + storableObject.getClass().getName());
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
			Log.debugMessage("ParameterTypeDatabase.retrieveForCodename | Trying: " + sql, Log.DEBUGLEVEL05);
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
			String mesg = "ParameterTypeDatabase.retrieveForCodename | Cannot retrieve parameter type with codename: '" + codename + "'";
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