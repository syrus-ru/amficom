package com.syrus.AMFICOM.configuration;

import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseDate;
import com.syrus.AMFICOM.general.*;

public class MonitoredElementDatabase extends StorableObjectDatabase {

	private MonitoredElement fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof MonitoredElement)
			return (MonitoredElement)storableObject;
		throw new IllegalDataException("MonitoredElement_Database.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		MonitoredElement monitoredElement = this.fromStorableObject(storableObject);
		this.retrieveMonitoredElement(monitoredElement);
	}

	private void retrieveMonitoredElement(MonitoredElement monitoredElement) throws ObjectNotFoundException, RetrieveObjectException {
		String meIdStr = monitoredElement.getId().toString();
		String sql = "SELECT "
			+ DatabaseDate.toQuerySubString("created") + ", "
			+ DatabaseDate.toQuerySubString("modified") + ", "
			+ "creator_id, "
			+ "modifier_id, "
			+ "domain_id, "
			+ "kis_id, "
			+ "local_address"
			+ " FROM " + ObjectEntities.ME_ENTITY
			+ " WHERE id = " + meIdStr;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("MonitoredElement_Database.retrieve | Trying: " + sql, Log.DEBUGLEVEL05);
			resultSet = statement.executeQuery(sql);
			if (resultSet.next())
				monitoredElement.setAttributes(DatabaseDate.fromQuerySubString(resultSet, "created"),
																			 DatabaseDate.fromQuerySubString(resultSet, "modified"),
																			 new Identifier(resultSet.getString("creator_id")),
																			 new Identifier(resultSet.getString("modifier_id")),
																			 new Identifier(resultSet.getString("domain_id")),
																			 new Identifier(resultSet.getString("kis_id")),
																			 resultSet.getString("local_address"));
			else
				throw new ObjectNotFoundException("No such monitored element: " + meIdStr);
		}
		catch (SQLException sqle) {
			String mesg = "MonitoredElement_Database.retrieve | Cannot retrieve monitored element " + meIdStr;
			throw new RetrieveObjectException(mesg, sqle);
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

	public Object retrieveObject(StorableObject storableObject, int retrieve_kind, Object arg) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException{
		MonitoredElement monitoredElement = this.fromStorableObject(storableObject);
		switch (retrieve_kind) {
			default:
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws IllegalDataException, CreateObjectException {
		MonitoredElement monitoredElement = this.fromStorableObject(storableObject);
		try {
			this.insertMonitoredElement(monitoredElement);
		}
		catch (CreateObjectException coe) {
			try {
				connection.rollback();
			}
			catch (SQLException sqle) {
				Log.errorMessage("Exception in rolling back");
				Log.errorException(sqle);
			}
			throw coe;
		}
		try {
			connection.commit();
		}
		catch (SQLException sqle) {
			Log.errorMessage("Exception in commiting");
			Log.errorException(sqle);
		}
	}

	private void insertMonitoredElement(MonitoredElement monitoredElement) throws CreateObjectException {
		String meIdStr = monitoredElement.getId().toString();
		String sql = "INSERT INTO " + ObjectEntities.ME_ENTITY
			+ " (id, created, modified, creator_id, modifier_id, domain_id, kis_id, local_address)"
			+ " VALUES ("
			+ meIdStr + ", "
			+ DatabaseDate.toUpdateSubString(monitoredElement.getCreated()) + ", "
			+ DatabaseDate.toUpdateSubString(monitoredElement.getModified()) + ", "
			+ monitoredElement.getCreatorId().toString() + ", "
			+ monitoredElement.getModifierId().toString() + ", "
			+ monitoredElement.getDomainId().toString() + ", "
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
			String mesg = "MonitoredElement_Database.insert | Cannot insert monitored element " + meIdStr;
			throw new CreateObjectException(mesg, sqle);
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

	public void update(StorableObject storableObject, int update_kind, Object obj) throws IllegalDataException, UpdateObjectException {
		MonitoredElement monitoredElement = this.fromStorableObject(storableObject);
		switch (update_kind) {
			default:
				return;
		}
	}
}