package com.syrus.AMFICOM.administration;

import java.util.Date;
import java.util.ArrayList;
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

public class PermissionAttributes_Database extends StorableObject_Database {

	private PermissionAttributes fromStorableObject(StorableObject storableObject) throws Exception {
		if (storableObject instanceof PermissionAttributes)
			return (PermissionAttributes)storableObject;
		else
			throw new Exception("PermissionAttributes_Database.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	public void retrieve(StorableObject storableObject) throws Exception {
		PermissionAttributes permissionAttributes = this.fromStorableObject(storableObject);
		this.retrievePermissionAttributes(permissionAttributes);
		this.retrieveGroupIds(permissionAttributes);
		this.retrieveCategoryIds(permissionAttributes);
	}

	private void retrievePermissionAttributes(PermissionAttributes permissionAttributes) throws Exception {
		String pa_id_str = permissionAttributes.getId().toString();
		String sql = "SELECT "
			+ DatabaseDate.toQuerySubString("created") + ", " 
			+ DatabaseDate.toQuerySubString("modified") + ", "
			+ "creator_id, "
			+ "modifier_id, "
			+ "name, "
			+ "codename, "
			+ "rwx, "
			+ "deny_message"
			+ " FROM " + ObjectEntities.PERMATTR_ENTITY
			+ " WHERE id = " + pa_id_str;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("PermissionAttributes_Database.retrievePermissionAttributes | Trying: " + sql, Log.DEBUGLEVEL05);
			resultSet = statement.executeQuery(sql);
			if (resultSet.next())
				permissionAttributes.setAttributes(DatabaseDate.fromQuerySubString(resultSet, "created"),
																					 DatabaseDate.fromQuerySubString(resultSet, "modified"),
																					 new Identifier(resultSet.getLong("creator_id")),
																					 new Identifier(resultSet.getLong("modifier_id")),
																					 resultSet.getString("name"),
																					 resultSet.getString("codename"),
																					 resultSet.getString("rwx"),
																					 resultSet.getString("deny_message"));
			else
				throw new Exception("No such permission attributes: " + pa_id_str);
		}
		catch (SQLException sqle) {
			String mesg = "PermissionAttributes_Database.retrievePermissionAttributes | Cannot retrieve permission attributes " + pa_id_str;
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

	private void retrieveGroupIds(PermissionAttributes permissionAttributes) throws Exception {
		String pa_id_str = permissionAttributes.getId().toString();
		/*Dodelat'!!*/
	}

	private void retrieveCategoryIds(PermissionAttributes permissionAttributes) throws Exception {
		String pa_id_str = permissionAttributes.getId().toString();
		/*Dodelat'!!*/
	}

	public Object retrieveObject(StorableObject storableObject, int retrieve_kind, Object arg) throws Exception {
		PermissionAttributes permissionAttributes = this.fromStorableObject(storableObject);
		switch (retrieve_kind) {
			default:
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws Exception {
		PermissionAttributes permissionAttributes = this.fromStorableObject(storableObject);
		try {
			this.insertPermissionAttributes(permissionAttributes);
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

	private void insertPermissionAttributes(PermissionAttributes permissionAttributes) throws Exception {
		String pa_id_str = permissionAttributes.getId().toString();
		String sql = "INSERT INTO " + ObjectEntities.PERMATTR_ENTITY
			+ " (id, created, modified, creator_id, modifier_id, name, codename, rwx, deny_message)"
			+ " VALUES ("
			+ pa_id_str + ", "
			+ DatabaseDate.toUpdateSubString(permissionAttributes.getCreated()) + ", "
			+ DatabaseDate.toUpdateSubString(permissionAttributes.getModified()) + ", "
			+ permissionAttributes.getCreatorId().toString() + ", "
			+ permissionAttributes.getModifierId().toString() + ", '"
			+ permissionAttributes.getName() + "', '"
			+ permissionAttributes.getCodename() + "', '"
			+ permissionAttributes.getRWX() + "', '"
			+ permissionAttributes.getDenyMessage()
			+ "')";
		Statement statement = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("PermissionAttributes_Database.insertPermissionAttributes | Trying: " + sql, Log.DEBUGLEVEL05);
			statement.executeUpdate(sql);
		}
		catch (SQLException sqle) {
			String mesg = "PermissionAttributes_Database.insertPermissionAttributes | Cannot insert permission attributes " + pa_id_str;
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
		PermissionAttributes permissionAttributes = this.fromStorableObject(storableObject);
		switch (update_kind) {
			default:
				return;
		}
	}
}