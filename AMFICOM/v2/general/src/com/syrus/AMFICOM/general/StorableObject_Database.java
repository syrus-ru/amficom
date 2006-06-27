package com.syrus.AMFICOM.general;

import java.sql.Connection;
import com.syrus.util.database.DatabaseConnection;

public abstract class StorableObject_Database {
	protected static Connection connection;

	public StorableObject_Database() {
		connection = DatabaseConnection.getConnection();
	}

	public abstract void retrieve(StorableObject storableObject) throws Exception;

	public abstract Object retrieveObject(StorableObject storableObject, int retrieve_kind, Object arg) throws Exception;

	public abstract void insert(StorableObject storableObject) throws Exception;

	public abstract void update(StorableObject storableObject, int update_kind, Object arg) throws Exception;
}