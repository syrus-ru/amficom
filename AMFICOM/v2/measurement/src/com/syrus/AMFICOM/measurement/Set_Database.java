package com.syrus.AMFICOM.measurement;

import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import oracle.sql.BLOB;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.ByteArrayDatabase;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObject_Database;
import com.syrus.AMFICOM.general.ObjectEntities;

public class Set_Database extends StorableObject_Database {

	private Set fromStorableObject(StorableObject storableObject) throws Exception {
		if (storableObject instanceof Set)
			return (Set)storableObject;
		else
			throw new Exception("Set_Database.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	public void retrieve(StorableObject storableObject) throws Exception {
		Set set = this.fromStorableObject(storableObject);
		this.retrieveSet(set);
		this.retrieveSetParameters(set);
		this.retrieveSetMELinks(set);
	}

	private void retrieveSet(Set set) throws Exception {
		String set_id_str = set.getId().toString();
		String sql = "SELECT "
			+ DatabaseDate.toQuerySubString("created") + ", " 
			+ DatabaseDate.toQuerySubString("modified") + ", "
			+ "creator_id, "
			+ "modifier_id, "
			+ "sort, "
			+ "description"
			+ " FROM " + ObjectEntities.SET_ENTITY
			+ " WHERE id = " + set_id_str;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("Set_Database.retrieveSet | Trying: " + sql, Log.DEBUGLEVEL05);
			resultSet = statement.executeQuery(sql);
			if (resultSet.next()) {
				String description = resultSet.getString("description");
				set.setAttributes(DatabaseDate.fromQuerySubString(resultSet, "created"),
													DatabaseDate.fromQuerySubString(resultSet, "modified"),
													new Identifier(resultSet.getLong("creator_id")),
													new Identifier(resultSet.getLong("modifier_id")),
													resultSet.getInt("sort"),
													(description != null)?description:"");
			}
			else
				throw new Exception("No such set: " + set_id_str);
		}
		catch (SQLException sqle) {
			String mesg = "Set_Database.retrieveSet | Cannot retrieve set " + set_id_str;
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

	private void retrieveSetParameters(Set set) throws Exception {
		String set_id_str = set.getId().toString();
		String sql = "SELECT "
			+ "id, "
			+ "type_id, "
			+ "value"
			+ " FROM " + ObjectEntities.SETPARAMETER_ENTITY
			+ " WHERE set_id = " + set_id_str;
		Statement statement = null;
		ResultSet resultSet = null;
		ArrayList arraylist = new ArrayList();
		try {
			statement = connection.createStatement();
			Log.debugMessage("Set_Database.retrieveSetParameters | Trying: " + sql, Log.DEBUGLEVEL05);
			resultSet = statement.executeQuery(sql);
			while (resultSet.next())
				arraylist.add(new SetParameter(new Identifier(resultSet.getLong("id")),
																			 new Identifier(resultSet.getLong("type_id")),
																			 ByteArrayDatabase.toByteArray((BLOB)resultSet.getBlob("value"))));
		}
		catch (SQLException sqle) {
			String mesg = "Set_Database.retrieveSetParameters | Cannot retrieve parameters for set " + set_id_str;
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
		set.setParameters((SetParameter[])arraylist.toArray(new SetParameter[arraylist.size()]));
	}

	private void retrieveSetMELinks(Set set) throws Exception {
		String set_id_str = set.getId().toString();
		String sql = "SELECT "
			+ "monitored_element_id"
			+ " FROM " + ObjectEntities.SETMELINK_ENTITY
			+ " WHERE set_id = " + set_id_str;
		Statement statement = null;
		ResultSet resultSet = null;
		ArrayList arraylist = new ArrayList();
		try {
			statement = connection.createStatement();
			Log.debugMessage("Set_Database.retrieveSetMELinks | Trying: " + sql, Log.DEBUGLEVEL05);
			resultSet = statement.executeQuery(sql);
			while (resultSet.next())
				arraylist.add(new Identifier(resultSet.getLong("monitored_element_id")));
		}
		catch (SQLException sqle) {
			String mesg = "Set_Database.retrieveSetMELinks | Cannot retrieve monitored element ids for set " + set_id_str;
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
		set.setMonitoredElementIds(arraylist);
	}

	public Object retrieveObject(StorableObject storableObject, int retrieve_kind, Object arg) throws Exception {
		Set set = this.fromStorableObject(storableObject);
		switch (retrieve_kind) {
			default:
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws Exception {
		Set set = this.fromStorableObject(storableObject);
		try {
			this.insertSet(set);
			this.insertSetParameters(set);
			this.insertSetMELinks(set);
		}
		catch (Exception e) {
			this.delete(set);
			throw e;
		}
	}

	private void insertSet(Set set) throws Exception {
		String set_id_str = set.getId().toString();
		String sql = "INSERT INTO " + ObjectEntities.SET_ENTITY
			+ " (id, created, modified, creator_id, modifier_id, sort, description)"
			+ " VALUES ("
			+ set_id_str + ", "
			+ DatabaseDate.toUpdateSubString(set.getCreated()) + ", "
			+ DatabaseDate.toUpdateSubString(set.getModified()) + ", "
			+ set.getCreatorId().toString() + ", "
			+ set.getModifierId().toString() + ", "
			+ Integer.toString(set.getSort().value()) + ", '"
			+ set.getDescription()
			+ "')";
		Statement statement = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("Set_Database.insertSet | Trying: " + sql, Log.DEBUGLEVEL05);
			statement.executeUpdate(sql);
			connection.commit();
		}
		catch (SQLException sqle) {
			String mesg = "Set_Database.insertSet | Cannot insert set " + set_id_str;
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

	private void insertSetParameters(Set set) throws Exception {
		String set_id_str = set.getId().toString();
		SetParameter[] setParameters = set.getParameters();
		String sql = "INSERT INTO " + ObjectEntities.SETPARAMETER_ENTITY
			+ " (id, type_id, set_id, value)"
			+ " VALUES (?, ?, ?, ?)";
		PreparedStatement preparedStatement = null;
		int i = 0;
		try {
			preparedStatement = connection.prepareStatement(sql);
			for (i = 0; i < setParameters.length; i++) {
				preparedStatement.setLong(1, setParameters[i].getId().getCode());
				preparedStatement.setLong(2, setParameters[i].getTypeId().getCode());
				preparedStatement.setLong(3, set.getId().getCode());
				preparedStatement.setBlob(4, BLOB.empty_lob());
				Log.debugMessage("Set_Database.insertSetParameters | Inserting parameter " + setParameters[i].getTypeId().toString() + " for set " + set_id_str, Log.DEBUGLEVEL05);
				preparedStatement.executeUpdate();
				ByteArrayDatabase badb = new ByteArrayDatabase(setParameters[i].getValue());
				badb.saveAsBlob(connection, ObjectEntities.SETPARAMETER_ENTITY, "value", "id = " + setParameters[i].getId().toString());
			}
			connection.commit();
		}
		catch (SQLException sqle) {
			String mesg = "Set_Database.insertSetParameters | Cannot insert parameter " + setParameters[i].getId().toString() + " of type " + setParameters[i].getTypeId().toString() + " for set " + set_id_str;
			throw new Exception(mesg, sqle);
		}
		finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
				preparedStatement = null;
			}
			catch (SQLException sqle1) {}
		}
	}

	private void insertSetMELinks(Set set) throws Exception {
		long set_id_code = set.getId().getCode();
		ArrayList me_ids = set.getMonitoredElementIds();
		String sql = "INSERT INTO " + ObjectEntities.SETMELINK_ENTITY
			+ " (set_id, monitored_element_id)"
			+ " VALUES (?, ?)";
		PreparedStatement preparedStatement = null;
		long me_id_code = 0;
		try {
			preparedStatement = connection.prepareStatement(sql);
			for (Iterator iterator = me_ids.iterator(); iterator.hasNext();) {
				preparedStatement.setLong(1, set_id_code);
				me_id_code = ((Identifier)iterator.next()).getCode();
				preparedStatement.setLong(2, me_id_code);
				Log.debugMessage("Set_Database.insertSetMELinks | Inserting link for set " + set_id_code + " and monitored element " + me_id_code, Log.DEBUGLEVEL05);
				preparedStatement.executeUpdate();
			}
			connection.commit();
		}
		catch (SQLException sqle) {
			String mesg = "Set_Database.insertSetMELinks | Cannot insert link for monitored element " + me_id_code + " and set " + set_id_code;
			throw new Exception(mesg, sqle);
		}
		finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
				preparedStatement = null;
			}
			catch (SQLException sqle1) {}
		}
	}

	public void update(StorableObject storableObject, int update_kind, Object obj) throws Exception {
		Set set = this.fromStorableObject(storableObject);
		switch (update_kind) {
			case Set.UPDATE_ATTACH_ME:
				this.createMEAttachment(set, (Identifier)obj);
				this.setModified(set);
				break;
			case Set.UPDATE_DETACH_ME:
				this.deleteMEAttachment(set, (Identifier)obj);
				this.setModified(set);
				break;
		}
	}

	private void createMEAttachment(Set set, Identifier monitored_element_id) throws Exception {
		String set_id_str = set.getId().toString();
		String me_id_str = monitored_element_id.toString();
		String sql = "INSERT INTO " + ObjectEntities.SETMELINK_ENTITY
			+ " (set_id, monitored_element_id)"
			+ " VALUES ("
			+ set_id_str + ", "
			+ me_id_str
			+ ")";
		Statement statement = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("Set.createMEAttachment | Trying: " + sql, Log.DEBUGLEVEL05);
			statement.executeUpdate(sql);
			connection.commit();
		}
		catch (SQLException sqle) {
			String mesg = "Set.createMEAttachment | Cannot attach set " + set_id_str + " to monitored element " + me_id_str;
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

	private void deleteMEAttachment(Set set, Identifier monitored_element_id) throws Exception {
		String set_id_str = set.getId().toString();
		String me_id_str = monitored_element_id.toString();
		String sql = "DELETE FROM " + ObjectEntities.SETMELINK_ENTITY
			+ " WHERE set_id = " + set_id_str
				+ " AND monitored_element_id = " + me_id_str;
		Statement statement = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("Set_Database.deleteMEAttachment | Trying: " + sql, Log.DEBUGLEVEL05);
			statement.executeUpdate(sql);
			connection.commit();
		}
		catch (SQLException sqle) {
			String mesg = "Set_Database.deleteMEAttachment | Cannot detach set " + set_id_str + " from monitored element " + me_id_str;
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

	private void setModified(Set set) throws Exception {
		String set_id_str = set.getId().toString();
		String sql = "UPDATE " + ObjectEntities.SET_ENTITY
			+ " SET "
			+ "modified = " + DatabaseDate.toUpdateSubString(set.getModified()) + ", "
			+ "modifier_id = " + set.getModifierId().toString()
			+ " WHERE id = " + set_id_str;
		Statement statement = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("Set_Database.setModified | Trying: " + sql, Log.DEBUGLEVEL05);
			statement.executeUpdate(sql);
			connection.commit();
		}
		catch (SQLException sqle) {
			String mesg = "Set_Database.setModified | Cannot set modified for set " + set_id_str;
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

	private void delete(Set set) {
		String set_id_str = set.getId().toString();
		Statement statement = null;
		try {
			statement = connection.createStatement();
			statement.executeUpdate("DELETE FROM " + ObjectEntities.SETMELINK_ENTITY + " WHERE set_id = " + set_id_str);
			statement.executeUpdate("DELETE FROM " + ObjectEntities.SETPARAMETER_ENTITY + " WHERE set_id = " + set_id_str);
			statement.executeUpdate("DELETE FROM " + ObjectEntities.SET_ENTITY + " WHERE id = " + set_id_str);
			connection.commit();
		}
		catch (SQLException sqle1) {
			Log.errorException(sqle1);
		}
		finally {
			try {
				if(statement != null)
					statement.close();
				statement = null;
			}
			catch(SQLException _ex) { }
		}
	}
}