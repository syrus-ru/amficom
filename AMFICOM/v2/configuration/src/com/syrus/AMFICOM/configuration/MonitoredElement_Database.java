package com.syrus.AMFICOM.configuration;

import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseDate;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObject_Database;
import com.syrus.AMFICOM.general.StorableObject_DatabaseContext;
import com.syrus.AMFICOM.general.ObjectEntities;

public class MonitoredElement_Database extends StorableObject_Database {

	private MonitoredElement fromStorableObject(StorableObject storableObject) throws Exception {
		if (storableObject instanceof MonitoredElement)
			return (MonitoredElement)storableObject;
		else
			throw new Exception("MonitoredElement_Database.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	public void retrieve(StorableObject storableObject) throws Exception {
		MonitoredElement monitoredElement = this.fromStorableObject(storableObject);
		this.retrieveMonitoredElement(monitoredElement);
	}

	private void retrieveMonitoredElement(MonitoredElement monitoredElement) throws Exception {
		String me_id_str = monitoredElement.getId().toString();
		String sql = "SELECT "
			+ DatabaseDate.toQuerySubString("created") + ", "
			+ DatabaseDate.toQuerySubString("modified") + ", "
			+ "creator_id, "
			+ "modifier_id, "
			+ "kis_id, "
			+ "local_address"
			+ " FROM " + ObjectEntities.ME_ENTITY
			+ " WHERE id = " + me_id_str;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("MonitoredElement_Database.retrieve | Trying: " + sql, Log.DEBUGLEVEL05);
			resultSet = statement.executeQuery(sql);
			if (resultSet.next())
				monitoredElement.setAttributes(DatabaseDate.fromQuerySubString(resultSet, "created"),
																			 DatabaseDate.fromQuerySubString(resultSet, "modified"),
																			 new Identifier(resultSet.getLong("creator_id")),
																			 new Identifier(resultSet.getLong("modifier_id")),
																			 new Identifier(resultSet.getLong("kis_id")),
																			 resultSet.getString("local_address"));
			else
				throw new Exception("No such monitored element: " + me_id_str);
		}
		catch (SQLException sqle) {
			String mesg = "MonitoredElement_Database.retrieve | Cannot retrieve monitored element " + me_id_str;
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
		MonitoredElement monitoredElement = this.fromStorableObject(storableObject);
		switch (retrieve_kind) {
			default:
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws Exception {
		MonitoredElement monitoredElement = this.fromStorableObject(storableObject);
		try {
			this.insertMonitoredElement(monitoredElement);
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

	private void insertMonitoredElement(MonitoredElement monitoredElement) throws Exception {
		String me_id_str = monitoredElement.getId().toString();
		String sql = "INSERT INTO " + ObjectEntities.ME_ENTITY
			+ " (id, created, modified, creator_id, modifier_id, kis_id, local_address)"
			+ " VALUES ("
			+ me_id_str + ", "
			+ DatabaseDate.toUpdateSubString(monitoredElement.getCreated()) + ", "
			+ DatabaseDate.toUpdateSubString(monitoredElement.getModified()) + ", "
			+ monitoredElement.getCreatorId().toString() + ", "
			+ monitoredElement.getModifierId().toString() + ", "
			+ monitoredElement.getKISId().toString() + ", '"
			+ monitoredElement.getLocalAddress()
			+ "')";
		Statement statement = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("MonitoredElement_Database.insert | Trying: " + sql, Log.DEBUGLEVEL05);
			statement.executeUpdate(sql);
		}
		catch (SQLException sqle) {
			String mesg = "MonitoredElement_Database.insert | Cannot insert monitored element " + me_id_str;
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
		MonitoredElement monitoredElement = this.fromStorableObject(storableObject);
		switch (update_kind) {
			default:
				return;
		}
	}
}