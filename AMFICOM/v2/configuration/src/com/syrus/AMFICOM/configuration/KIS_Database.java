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

public class KIS_Database extends StorableObject_Database {

	public void retrieve(StorableObject storableObject) throws Exception {
		KIS kis = null;
		if (storableObject instanceof KIS)
			kis = (KIS)storableObject;
		else
			throw new Exception("KIS_Database.retrieve | Illegal Storable Object: " + storableObject.getClass().getName());

		String kis_id_str = kis.getId().toString();
		String sql = "SELECT mcm_id, name, description FROM " + ObjectEntities.KIS_ENTITY + " WHERE id = " + kis_id_str;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("KIS_Database.retrieve | Trying: " + sql, Log.DEBUGLEVEL05);
			resultSet = statement.executeQuery(sql);
			if (resultSet.next())
				kis.setAttributes(new Identifier(resultSet.getLong("mcm_id")),
													resultSet.getString("name"),
													resultSet.getString("description"));
			else
				throw new Exception("No such kis: " + kis_id_str);
		}
		catch (SQLException sqle) {
			String mesg = "KIS_Database.retrieve | Cannot retrieve kis " + kis_id_str;
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

	public void insert(StorableObject storableObject) throws Exception {
		KIS kis = null;
		if (storableObject instanceof KIS)
			kis = (KIS)storableObject;
		else
			throw new Exception("KIS_Database.insert | Illegal Storable Object: " + storableObject.getClass().getName());

		this.insertKIS(kis);
	}

	public void insertKIS(KIS kis) throws Exception {
		String kis_id_str = kis.getId().toString();
		String sql = "INSERT INTO " + ObjectEntities.KIS_ENTITY + " (id, mcm_id, name, description) VALUES (" + kis_id_str + ", " + kis.getMCMId().toString() + ", '" + kis.getName() + "', '" + kis.getDescription() + "')";
		Statement statement = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("KIS_Database.insertKIS | Trying: " + sql, Log.DEBUGLEVEL05);
			statement.executeUpdate(sql);
			connection.commit();
		}
		catch (SQLException sqle) {
			String mesg = "KIS_Database.insertKIS | Cannot insert kis " + kis_id_str;
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

	protected ArrayList retrieveMonitoredElements(KIS kis) throws Exception {
		String kis_id_str = kis.getId().toString();
		String sql = "SELECT id FROM " + ObjectEntities.ME_ENTITY + " WHERE kis_id = " + kis_id_str;
		Statement statement = null;
		ResultSet resultSet = null;
		ArrayList arraylist = new ArrayList();
		try {
			statement = connection.createStatement();
			Log.debugMessage("KIS_Database.retrieveMonitoredElements | Trying: " + sql, Log.DEBUGLEVEL05);
			resultSet = statement.executeQuery(sql);
			while (resultSet.next())
				arraylist.add(new MonitoredElement(new Identifier(resultSet.getLong("id"))));
		}
		catch (SQLException sqle) {
			String mesg = "KIS_Database.retrieveMonitoredElements | Cannot retrieve monitored elements for kis " + kis_id_str;
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

	public Object retrieveObject(StorableObject storableObject, int retrieve_kind, Object arg) throws Exception {
		KIS kis = null;
		if (storableObject instanceof KIS)
			kis = (KIS)storableObject;
		else
			throw new Exception("KIS_Database.retrieveObject | Illegal Storable Object: " + storableObject.getClass().getName());

		switch (retrieve_kind) {
			default:
				return null;
		}
	}

	public void update(StorableObject storableObject, int update_kind, Object obj) throws Exception {
		KIS kis = null;
		if (storableObject instanceof KIS)
			kis = (KIS)storableObject;
		else
			throw new Exception("KIS_Database.update | Illegal Storable Object: " + storableObject.getClass().getName());
	}
}