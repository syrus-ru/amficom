package com.syrus.AMFICOM.general;

import java.sql.Connection;
import com.syrus.util.database.DatabaseConnection;

public abstract class StorableObject_Database {
	public static final String COLUMN_ID = "id";
	public static final String COLUMN_CREATED = "created";
	public static final String COLUMN_MODIFIED = "modified";
	public static final String COLUMN_CREATOR_ID = "creator_id";
	public static final String COLUMN_MODIFIER_ID = "modifier_id";
	public static final String COMMA = " , ";

	protected static Connection connection;

	public StorableObject_Database() {
		connection = DatabaseConnection.getConnection();
	}

	public abstract void retrieve(StorableObject storableObject) throws Exception;

	public abstract Object retrieveObject(StorableObject storableObject, int retrieve_kind, Object arg) throws Exception;

	public abstract void insert(StorableObject storableObject) throws Exception;

	public abstract void update(StorableObject storableObject, int update_kind, Object arg) throws Exception;
}
