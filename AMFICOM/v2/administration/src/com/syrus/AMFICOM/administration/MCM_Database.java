package com.syrus.AMFICOM.administration;

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

public class MCM_Database extends StorableObject_Database {

	public void retrieve(StorableObject storableObject) throws Exception {
		MCM mcm = null;
		if (storableObject instanceof MCM)
			mcm = (MCM)storableObject;
		else
			throw new Exception("MCM_Database.retrieve | Illegal Storable Object: " + storableObject.getClass().getName());

		this.retrieveMCM(mcm);
		this.retrieveKISs(mcm);
	}

	private void retrieveMCM(MCM mcm) throws Exception {
		String mcm_id_str = mcm.getId().toString();
		String sql = "SELECT server_id, hostname, name, description FROM " + ObjectEntities.MCM_ENTITY + " WHERE id = " + mcm_id_str;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("MCM_Database.retrieveMCM | Trying: " + sql, Log.DEBUGLEVEL05);
			resultSet = statement.executeQuery(sql);
			if (resultSet.next())
				mcm.setAttributes(new Identifier(resultSet.getLong("server_id")),
													resultSet.getString("hostname"),
													resultSet.getString("name"),
													resultSet.getString("description"));
			else
				throw new Exception("No such mcm: " + mcm_id_str);
		}
		catch (SQLException sqle) {
			String mesg = "MCM_Database.retrieveMCM | Cannot retrieve mcm " + mcm_id_str;
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

	private void retrieveKISs(MCM mcm) throws Exception {
		String mcm_id_str = mcm.getId().toString();
		String sql = "SELECT id FROM " + ObjectEntities.KIS_ENTITY + " WHERE mcm_id = " + mcm_id_str;
		Statement statement = null;
		ResultSet resultSet = null;
		ArrayList arraylist = new ArrayList();
		try {
			statement = connection.createStatement();
			Log.debugMessage("MCM_Database.retrieveKISs | Trying: " + sql, Log.DEBUGLEVEL05);
			resultSet = statement.executeQuery(sql);
			while (resultSet.next())
				arraylist.add(new KIS(new Identifier(resultSet.getLong("id"))));
		}
		catch (SQLException sqle) {
			String mesg = "MCM_Database.retrieveKISs | Cannot retrieve kiss for mcm " + mcm_id_str;
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
		mcm.setKISs(arraylist);
	}

	public Object retrieveObject(StorableObject storableObject, int retrieve_kind, Object arg) throws Exception {
		MCM mcm = null;
		if (storableObject instanceof MCM)
			mcm = (MCM)storableObject;
		else
			throw new Exception("MCM_Database.retrieveObject | Illegal Storable Object: " + storableObject.getClass().getName());

		switch (retrieve_kind) {
			case MCM.RETRIEVE_TESTS_ORDER_BY_START_TIME:
				return this.retrieveTestsOrderByStartTime(mcm, (TestStatus)arg);
			default:
				return null;
		}
	}

	private ArrayList retrieveTestsOrderByStartTime(MCM mcm, TestStatus test_status) throws Exception {
		String mcm_id_str = mcm.getId().toString();
		String sql = "SELECT id FROM " + ObjectEntities.TEST_ENTITY + "	WHERE status = " + Integer.toString(test_status.value()) + " AND monitored_element_id IN (SELECT id FROM " + ObjectEntities.ME_ENTITY + " WHERE kis_id IN (SELECT id FROM " + ObjectEntities.KIS_ENTITY + " WHERE mcm_id = " + mcm_id_str + ")) ORDER BY start_time ASC";
		Statement statement = null;
		ResultSet resultSet = null;
		ArrayList arraylist = new ArrayList();
		try {
			statement = connection.createStatement();
			Log.debugMessage("MCM_Database.retrieveTestsOrderByStartTime | Trying: " + sql, Log.DEBUGLEVEL05);
			resultSet = statement.executeQuery(sql);
			while (resultSet.next())
				arraylist.add(new Test(new Identifier(resultSet.getLong("id"))));
		}
		catch (SQLException sqle) {
			String mesg = "MCM_Database.retrieveTestsOrderByStartTime | Cannot retrieve tests of status " + Integer.toString(test_status.value()) + " for mcm " + mcm_id_str;
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
		return arraylist;
	}

	public void insert(StorableObject storableObject) throws Exception {
		MCM mcm = null;
		if (storableObject instanceof MCM)
			mcm = (MCM)storableObject;
		else
			throw new Exception("MCM_Database.insert | Illegal Storable Object: " + storableObject.getClass().getName());

		this.insertMCM(mcm);
	}

	public void insertMCM(MCM mcm) throws Exception {
		String mcm_id_str = mcm.getId().toString();
		String sql = "INSERT INTO " + ObjectEntities.MCM_ENTITY + " (id, server_id, hostname, name, description) VALUES (" + mcm_id_str + ", " + mcm.getServerId().toString() + ", '" + mcm.getHostname() + "', '" + mcm.getName() + "', '" + mcm.getDescription() + "')";
		Statement statement = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("MCM_Database.insertMCM | Trying: " + sql, Log.DEBUGLEVEL05);
			statement.executeUpdate(sql);
			connection.commit();
		}
		catch (SQLException sqle) {
			String mesg = "MCM_Database.insertMCM | Cannot insert mcm " + mcm_id_str;
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

	public void update(StorableObject storableObject, int update_kind, Object arg) throws Exception {
		MCM mcm = null;
		if (storableObject instanceof MCM)
			mcm = (MCM)storableObject;
		else
			throw new Exception("MCM_Database.update | Illegal Storable Object: " + storableObject.getClass().getName());
	}
}