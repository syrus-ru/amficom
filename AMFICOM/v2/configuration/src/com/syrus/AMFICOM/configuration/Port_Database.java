package com.syrus.AMFICOM.configuration;

import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseDate;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObject_Database;
import com.syrus.AMFICOM.general.ObjectEntities;

public class Port_Database extends StorableObject_Database {

	private Port fromStorableObject(StorableObject storableObject) throws Exception {
		if (storableObject instanceof Port)
			return (Port)storableObject;
		else
			throw new Exception("Port_Database.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	public void retrieve(StorableObject storableObject) throws Exception {
		Port port = this.fromStorableObject(storableObject);
		this.retrievePort(port);
		this.retrievePortCharacteristics(port);
	}

	private void retrievePort(Port port) throws Exception {
		String port_id_str = port.getId().toString();
		String sql = "SELECT "
			+ DatabaseDate.toQuerySubString("created") + ", "
			+ DatabaseDate.toQuerySubString("modified") + ", "
			+ "creator_id, "
			+ "modifier_id, "
			+ "type_id, "
			+ "name, "
			+ "description, "
			+ "equipment_id"
			+ " FROM " + ObjectEntities.PORT_ENTITY
			+ " WHERE id = " + port_id_str;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("Port_Database.retrieve | Trying: " + sql, Log.DEBUGLEVEL05);
			resultSet = statement.executeQuery(sql);
			if (resultSet.next())
				port.setAttributes(DatabaseDate.fromQuerySubString(resultSet, "created"),
													 DatabaseDate.fromQuerySubString(resultSet, "modified"),
													 new Identifier(resultSet.getLong("creator_id")),
													 new Identifier(resultSet.getLong("modifier_id")),
													 new Identifier(resultSet.getLong("type_id")),
													 resultSet.getString("name"),
													 resultSet.getString("description"),
													 new Identifier(resultSet.getLong("equipment_id")));
			else
				throw new Exception("No such port: " + port_id_str);
		}
		catch (SQLException sqle) {
			String mesg = "Port_Database.retrieve | Cannot retrieve port " + port_id_str;
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

	private void retrievePortCharacteristics(Port port) throws Exception {
		String port_id_str = port.getId().toString();
		String sql = "SELECT "
			+ "id"
			+ " FROM " + ObjectEntities.CHARACTERISTIC_ENTITY
			+ " WHERE port_id = " + port_id_str;
	}

	public Object retrieveObject(StorableObject storableObject, int retrieve_kind, Object arg) throws Exception {
		Port port = this.fromStorableObject(storableObject);
		switch (retrieve_kind) {
			default:
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws Exception {
		Port port = this.fromStorableObject(storableObject);
		try {
			this.insertPort(port);
			this.insertPortCharacteristics(port);
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

	private void insertPort(Port port) throws Exception {
		String port_id_str = port.getId().toString();
		String sql = "INSERT INTO " + ObjectEntities.PORT_ENTITY
			+ " (id, created, modified, creator_id, modifier_id, type_id, name, description, equipment_id)"
			+ " VALUES ("
			+ port_id_str + ", "
			+ DatabaseDate.toUpdateSubString(port.getCreated()) + ", "
			+ DatabaseDate.toUpdateSubString(port.getModified()) + ", "
			+ port.getCreatorId().toString() + ", "
			+ port.getModifierId().toString() + ", "
			+ port.getTypeId().toString() + ", '"
			+ port.getName() + "', '"
			+ port.getDescription() + "', "
			+ port.getEquipmentId().toString()
			+ ")";
		Statement statement = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("Port_Database.insert | Trying: " + sql, Log.DEBUGLEVEL05);
			statement.executeUpdate(sql);
		}
		catch (SQLException sqle) {
			String mesg = "Port_Database.insert | Cannot insert port " + port_id_str;
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

	private void insertPortCharacteristics(Port port) throws Exception {
		String port_id_str = port.getId().toString();
	}

	public void update(StorableObject storableObject, int update_kind, Object obj) throws Exception {
		Port port = this.fromStorableObject(storableObject);
		switch (update_kind) {
			default:
				return;
		}
	}
}