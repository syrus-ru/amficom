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

public class Domain_Database extends StorableObject_Database {

	private Domain fromStorableObject(StorableObject storableObject) throws Exception {
		if (storableObject instanceof Domain)
			return (Domain)storableObject;
		else
			throw new Exception("Domain_Database.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	public void retrieve(StorableObject storableObject) throws Exception {
		Domain domain = this.fromStorableObject(storableObject);
		this.retrieveDomain(domain);
		this.retrieveDomainIds(domain);
	}

	private void retrieveDomain(Domain domain) throws Exception {
		String domain_id_str = domain.getId().toString();
		String sql = "SELECT "
			+ DatabaseDate.toQuerySubString("created") + ", " 
			+ DatabaseDate.toQuerySubString("modified") + ", "
			+ "creator_id, "
			+ "modifier_id, "
			+ "domain_id, "
			+ "name, "
			+ "description, "
			+ "owner_id, "
			+ "permission_attributes_id"
			+ " FROM " + ObjectEntities.DOMAIN_ENTITY
			+ " WHERE id = " + domain_id_str;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("Domain_Database.retrieveDomain | Trying: " + sql, Log.DEBUGLEVEL05);
			resultSet = statement.executeQuery(sql);
			if (resultSet.next()) {
				PermissionAttributes permission_attributes = new PermissionAttributes(new Identifier(resultSet.getLong("permission_attributes_id")));
				domain.setAttributes(DatabaseDate.fromQuerySubString(resultSet, "created"),
														 DatabaseDate.fromQuerySubString(resultSet, "modified"),
														 new Identifier(resultSet.getLong("creator_id")),
														 new Identifier(resultSet.getLong("modifier_id")),
														 new Identifier(resultSet.getLong("domain_id")),
														 resultSet.getString("name"),
														 resultSet.getString("description"),
														 new Identifier(resultSet.getLong("owner_id")),
														 permission_attributes);
			}
			else
				throw new Exception("No such domain: " + domain_id_str);
		}
		catch (SQLException sqle) {
			String mesg = "Domain_Database.retrieveDomain | Cannot retrieve domain " + domain_id_str;
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

	private void retrieveDomainIds(Domain domain) throws Exception {
		String domain_id_str = domain.getId().toString();
		/*Dodelat'!!*/
	}

	public Object retrieveObject(StorableObject storableObject, int retrieve_kind, Object arg) throws Exception {
		Domain domain = this.fromStorableObject(storableObject);
		switch (retrieve_kind) {
			default:
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws Exception {
		Domain domain = this.fromStorableObject(storableObject);
		try {
			this.insertDomain(domain);
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

	private void insertDomain(Domain domain) throws Exception {
		String domain_id_str = domain.getId().toString();
		String sql = "INSERT INTO " + ObjectEntities.DOMAIN_ENTITY
			+ " (id, created, modified, creator_id, modifier_id, name, description, owner_id, permission_attributes_id)"
			+ " VALUES ("
			+ domain_id_str + ", "
			+ DatabaseDate.toUpdateSubString(domain.getCreated()) + ", "
			+ DatabaseDate.toUpdateSubString(domain.getModified()) + ", "
			+ domain.getCreatorId().toString() + ", "
			+ domain.getModifierId().toString() + ", '"
			+ domain.getName() + "', '"
			+ domain.getDescription() + "', "
			+ domain.getOwnerId().toString() + ", "
			+ domain.getPermissionAttributes().getId().toString()
			+ ")";
		Statement statement = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("Domain_Database.insertDomain | Trying: " + sql, Log.DEBUGLEVEL05);
			statement.executeUpdate(sql);
		}
		catch (SQLException sqle) {
			String mesg = "Domain_Database.insertDomain | Cannot insert domain " + domain_id_str;
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
		Domain domain = this.fromStorableObject(storableObject);
		switch (update_kind) {
			default:
				return;
		}
	}
}