package com.syrus.AMFICOM.general;

import java.sql.Connection;
import com.syrus.util.database.DatabaseConnection;

public abstract class StorableObjectDatabase {

	public static final String	COLUMN_CREATED					= "created";
	public static final String	COLUMN_CREATOR_ID				= "creator_id";
	public static final String	COLUMN_ID						= "id";
	public static final String	COLUMN_MODIFIED					= "modified";
	public static final String	COLUMN_MODIFIER_ID				= "modifier_id";

	public static final String	LINK_COLUMN_PARAMETER_TYPE_ID	= "parameter_type_id";
	public static final String	LINK_COLUMN_PARAMETER_MODE		= "parameter_mode";

	public static final String	APOSTOPHE				= "'";
	public static final String	CLOSE_BRACKET		= " ) ";
	public static final String	COMMA						= " , ";
	public static final String	EQUALS					= " = ";
	public static final String	OPEN_BRACKET		= " ( ";
	public static final String	QUESTION				= "?";

	public static final String	SQL_AND					= " AND ";
	public static final String	SQL_DELETE_FROM	= " DELETE FROM ";
	public static final String	SQL_FROM				= " FROM ";
	public static final String	SQL_INSERT_INTO	= " INSERT INTO ";
	public static final String	SQL_IN					= " IN ";
	public static final String	SQL_OR					= " OR ";
	public static final String	SQL_ORDER_BY		= " ORDER BY ";
	public static final String	SQL_SELECT			= " SELECT ";
	public static final String	SQL_SET					= " SET ";
	public static final String	SQL_UPDATE			= " UPDATE ";
	public static final String	SQL_VALUES			= " VALUES ";
	public static final String	SQL_WHERE				= " WHERE ";

	public static final String	SQL_FUNCTION_MAX	= " MAX ";

	protected static Connection	connection;

	public StorableObjectDatabase() {
		connection = DatabaseConnection.getConnection();
	}

	public abstract void insert(StorableObject storableObject) throws IllegalDataException, CreateObjectException;

	public abstract void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException;

	public abstract Object retrieveObject(StorableObject storableObject, int retrieve_kind, Object arg)
			throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException;

	public abstract void update(StorableObject storableObject, int update_kind, Object arg) throws IllegalDataException, UpdateObjectException;
}

