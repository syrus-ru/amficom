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

public class CharacteristicType_Database extends StorableObject_Database {

	private CharacteristicType fromStorableObject(StorableObject storableObject) throws Exception {
		if (storableObject instanceof CharacteristicType)
			return (CharacteristicType)storableObject;
		else
			throw new Exception("CharacteristicType_Database.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	public void retrieve(StorableObject storableObject) throws Exception {
		CharacteristicType characteristicType = this.fromStorableObject(storableObject);
		this.retrieveCharacteristicType(characteristicType);
	}

	private void retrieveCharacteristicType(CharacteristicType characteristicType) throws Exception {
		String ct_id_str = characteristicType.getId().toString();
		String sql = "SELECT "
			+ DatabaseDate.toQuerySubString("created") + ", "
			+ DatabaseDate.toQuerySubString("modified") + ", "
			+ "creator_id, "
			+ "modifier_id, "
			+ "codename, "
			+ "description, "
			+ "data_type, "
			+ "is_editable, "
			+ "is_visible"
			+ " FROM " + ObjectEntities.CHARACTERISTICTYPE_ENTITY
			+ " WHERE id = " + ct_id_str;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("CharacteristicType_Database.retrieve | Trying: " + sql, Log.DEBUGLEVEL05);
			resultSet = statement.executeQuery(sql);
			if (resultSet.next())
				characteristicType.setAttributes(DatabaseDate.fromQuerySubString(resultSet, "created"),
																				 DatabaseDate.fromQuerySubString(resultSet, "modified"),
																				 new Identifier(resultSet.getLong("creator_id")),
																				 new Identifier(resultSet.getLong("modifier_id")),
																				 resultSet.getString("codename"),
																				 resultSet.getString("description"),
																				 resultSet.getInt("data_type"),
																				 (resultSet.getInt("is_editable") == 0)?false:true,
																				 (resultSet.getInt("is_visible") == 0)?false:true);
			else
				throw new Exception("No such characteristic type: " + ct_id_str);
		}
		catch (SQLException sqle) {
			String mesg = "CharacteristicType_Database.retrieve | Cannot retrieve characteristic type " + ct_id_str;
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
		CharacteristicType characteristicType = this.fromStorableObject(storableObject);
		switch (retrieve_kind) {
			default:
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws Exception {
		CharacteristicType characteristicType = this.fromStorableObject(storableObject);
		try {
			this.insertCharacteristicType(characteristicType);
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

	private void insertCharacteristicType(CharacteristicType characteristicType) throws Exception {
		String ct_id_str = characteristicType.getId().toString();
		String sql = "INSERT INTO " + ObjectEntities.CHARACTERISTICTYPE_ENTITY
			+ " (id, created, modified, creator_id, modifier_id, codename, description, data_type, is_editable, is_visible)"
			+ " VALUES ("
			+ ct_id_str + ", "
			+ DatabaseDate.toUpdateSubString(characteristicType.getCreated()) + ", "
			+ DatabaseDate.toUpdateSubString(characteristicType.getModified()) + ", "
			+ characteristicType.getCreatorId().toString() + ", "
			+ characteristicType.getModifierId().toString() + ", '"
			+ characteristicType.getCodename() + "', '"
			+ characteristicType.getDescription() + "', "
			+ Integer.toString(characteristicType.getDataType().value()) + ", "
			+ (characteristicType.getIsEditable()?"1":"0") + ", "
			+ (characteristicType.getIsVisible()?"1":"0")
			+ "')";
		Statement statement = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("CharacteristicType_Database.insert | Trying: " + sql, Log.DEBUGLEVEL05);
			statement.executeUpdate(sql);
		}
		catch (SQLException sqle) {
			String mesg = "CharacteristicType_Database.insert | Cannot insert characteristic type " + ct_id_str;
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
		CharacteristicType characteristicType = this.fromStorableObject(storableObject);
		switch (update_kind) {
			default:
				return;
		}
	}
}