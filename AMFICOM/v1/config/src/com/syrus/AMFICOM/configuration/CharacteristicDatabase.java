package com.syrus.AMFICOM.configuration;

import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseDate;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.configuration.corba.CharacteristicSort;

public class CharacteristicDatabase extends StorableObjectDatabase {

	private Characteristic fromStorableObject(StorableObject storableObject) throws Exception {
		if (storableObject instanceof Characteristic)
			return (Characteristic)storableObject;
		throw new Exception("Characteristic_Database.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	public void retrieve(StorableObject storableObject) throws Exception {
		Characteristic characteristic = this.fromStorableObject(storableObject);
		this.retrieveCharacteristic(characteristic);
	}

	private void retrieveCharacteristic(Characteristic characteristic) throws Exception {
		String cIdStr = characteristic.getId().toString();
		String sql = "SELECT "
			+ DatabaseDate.toQuerySubString("created") + ", "
			+ DatabaseDate.toQuerySubString("modified") + ", "
			+ "creator_id, "
			+ "modifier_id, "
			+ "type_id, "
			+ "name, "
			+ "description, "
			+ "sort, "
			+ "value, "
			+ "equipment_id, "
			+ "port_id, "
			+ "cable_port_id, "
			+ "measurement_port_id, "
			+ "monitoring_port_id, "
			+ "link_id, "
			+ "cable_link_id, "
			+ " FROM " + ObjectEntities.CHARACTERISTIC_ENTITY
			+ " WHERE id = " + cIdStr;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("Characteristic_Database.retrieve | Trying: " + sql, Log.DEBUGLEVEL05);
			resultSet = statement.executeQuery(sql);
			if (resultSet.next()) {
				int sort = resultSet.getInt("sort");
				Identifier characterized_id;
				switch (sort) {
					case CharacteristicSort._CHARACTERISTIC_SORT_EQUIPMENT:
						characterized_id = new Identifier(resultSet.getLong("equipment_id"));
						break;
					case CharacteristicSort._CHARACTERISTIC_SORT_PORT:
						characterized_id = new Identifier(resultSet.getLong("port_id"));
						break;
					case CharacteristicSort._CHARACTERISTIC_SORT_CABLEPORT:
						characterized_id = new Identifier(resultSet.getLong("cable_port_id"));
						break;
					case CharacteristicSort._CHARACTERISTIC_SORT_MEASUREMENTPORT:
						characterized_id = new Identifier(resultSet.getLong("measurement_port_id"));
						break;
					case CharacteristicSort._CHARACTERISTIC_SORT_MONITORINGPORT:
						characterized_id = new Identifier(resultSet.getLong("monitoring_port_id"));
						break;
					case CharacteristicSort._CHARACTERISTIC_SORT_LINK:
						characterized_id = new Identifier(resultSet.getLong("link_id"));
						break;
					case CharacteristicSort._CHARACTERISTIC_SORT_CABLELINK:
						characterized_id = new Identifier(resultSet.getLong("cable_link_id"));
						break;
					default:
						characterized_id = null;
						Log.errorMessage("Unknown sort: " + sort + " for characteristic: " + c_id_str);
				}
				characteristic.setAttributes(DatabaseDate.fromQuerySubString(resultSet, "created"),
																		 DatabaseDate.fromQuerySubString(resultSet, "modified"),
																		 new Identifier(resultSet.getLong("creator_id")),
																		 new Identifier(resultSet.getLong("modifier_id")),
																		 new Identifier(resultSet.getLong("type_id")),
																		 resultSet.getString("name"),
																		 resultSet.getString("description"),
																		 sort,
																		 resultSet.getString("value"),
																		 characterized_id);
			}
			else
				throw new Exception("No such characteristic: " + cIdStr);
		}
		catch (SQLException sqle) {
			String mesg = "Characteristic_Database.retrieve | Cannot retrieve characteristic " + cIdStr;
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
		Characteristic characteristic = this.fromStorableObject(storableObject);
		switch (retrieve_kind) {
			default:
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws Exception {
		Characteristic characteristic = this.fromStorableObject(storableObject);
		try {
			this.insertCharacteristic(characteristic);
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

	private void insertCharacteristic(Characteristic characteristic) throws Exception {
		String cIdStr = characteristic.getId().toString();
		int sort = characteristic.getSort().value();
		String sql = "INSERT INTO " + ObjectEntities.CHARACTERISTIC_ENTITY
			+ " (id, created, modified, creator_id, modifier_id, type_id, sort, name, description, value, equipment_id, port_id, cable_port_id, measurement_port_id, monitoring_port_id, link_id, cable_link_id)"
			+ " VALUES ("
			+ cIdStr + ", "
			+ DatabaseDate.toUpdateSubString(characteristic.getCreated()) + ", "
			+ DatabaseDate.toUpdateSubString(characteristic.getModified()) + ", "
			+ characteristic.getCreatorId().toString() + ", "
			+ characteristic.getModifierId().toString() + ", "
			+ characteristic.getTypeId().toString() + ", "
			+ Integer.toString(sort) + ", '"
			+ characteristic.getName() + "', '"
			+ characteristic.getDescription() + "', '"
			+ characteristic.getValue() + "', ";
		String characterizedIdStr = characteristic.getCharacterizedId().toString();
		switch (sort) {
			case CharacteristicSort._CHARACTERISTIC_SORT_EQUIPMENT:
				sql += characterizedIdStr + ", 0, 0, 0, 0, 0, 0";
				break;
			case CharacteristicSort._CHARACTERISTIC_SORT_PORT:
				sql += "0, " + characterizedIdStr + ", 0, 0, 0, 0, 0";
				break;
			case CharacteristicSort._CHARACTERISTIC_SORT_CABLEPORT:
				sql += "0, 0, " + characterizedIdStr + ", 0, 0, 0, 0";
				break;
			case CharacteristicSort._CHARACTERISTIC_SORT_MEASUREMENTPORT:
				sql += "0, 0, 0, " + characterizedIdStr + ", 0, 0, 0";
				break;
			case CharacteristicSort._CHARACTERISTIC_SORT_MONITORINGPORT:
				sql += "0, 0, 0, 0, " + characterizedIdStr + ", 0, 0";
				break;
			case CharacteristicSort._CHARACTERISTIC_SORT_LINK:
				sql += "0, 0, 0, 0, 0, " + characterizedIdStr + ", 0";
				break;
			case CharacteristicSort._CHARACTERISTIC_SORT_CABLELINK:
				sql += "0, 0, 0, 0, 0, 0, " + characterizedIdStr;
				break;
		}
		sql += ")";
		Statement statement = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("Characteristic_Database.insert | Trying: " + sql, Log.DEBUGLEVEL05);
			statement.executeUpdate(sql);
		}
		catch (SQLException sqle) {
			String mesg = "Characteristic_Database.insert | Cannot insert characteristic " + cIdStr;
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
		Characteristic characteristic = this.fromStorableObject(storableObject);
		switch (update_kind) {
			default:
				return;
		}
	}
}