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
import com.syrus.AMFICOM.general.ObjectEntities;

public class Server_Database extends StorableObject_Database {

	private Server fromStorableObject(StorableObject storableObject) throws Exception {
		if (storableObject instanceof Server)
			return (Server)storableObject;
		else
			throw new Exception("Server_Database.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	public void retrieve(StorableObject storableObject) throws Exception {
		Server server = this.fromStorableObject(storableObject);
		this.retrieveServer(server);
		this.retrieveMCMs(server);
	}

	private void retrieveServer(Server server) throws Exception {
		String server_id_str = server.getId().toString();
		String sql = "SELECT name, description, location, contact, hostname, " + DatabaseDate.toQuerySubString("created") + ", " + DatabaseDate.toQuerySubString("modified") + ", sessions FROM " + ObjectEntities.SERVER_ENTITY + " WHERE id = " + server_id_str;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("Server_Database.retrieveServer | Trying: " + sql, Log.DEBUGLEVEL05);
			resultSet = statement.executeQuery(sql);
			if (resultSet.next())
				server.setAttributes(resultSet.getString("name"),
														 resultSet.getString("description"),
														 resultSet.getString("location"),
														 resultSet.getString("contact"),
														 resultSet.getString("hostname"),
														 DatabaseDate.fromQuerySubString(resultSet, "created"),
														 DatabaseDate.fromQuerySubString(resultSet, "modified"),
														 resultSet.getInt("sessions"));
			else
				throw new Exception("No such server: " + server_id_str);
		}
		catch (SQLException sqle) {
			String mesg = "Server_Database.retrieveServer | Cannot retrieve server " + server_id_str;
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

	protected void retrieveMCMs(Server server) throws Exception {
		String server_id_str = server.getId().toString();
		String sql = "SELECT id FROM " + ObjectEntities.MCM_ENTITY + " WHERE server_id = " + server_id_str;
		Statement statement = null;
		ResultSet resultSet = null;
		ArrayList arraylist = new ArrayList();
		try {
			statement = connection.createStatement();
			Log.debugMessage("Server_Database.retrieveMCMs | Trying: " + sql, Log.DEBUGLEVEL05);
			resultSet = statement.executeQuery(sql);
			while (resultSet.next())
				arraylist.add(new MCM(new Identifier(resultSet.getLong("id"))));
		}
		catch (SQLException sqle) {
			String mesg = "Server_Database.retrieveMCMs | Cannot retrieve mcms for server " + server_id_str;
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
		server.setMCMs(arraylist);
	}

	public Object retrieveObject(StorableObject storableObject, int retrieve_kind, Object arg) throws Exception {
		Server server = this.fromStorableObject(storableObject);

		switch (retrieve_kind) {
			default:
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws Exception {
		Server server = this.fromStorableObject(storableObject);
		this.insertServer(server);
	}

	public void insertServer(Server server) throws Exception {
		String server_id_str = server.getId().toString();
		String sql = "INSERT INTO " + ObjectEntities.SERVER_ENTITY + " (id, name, description, location, contact, hostname, created, modified, sessions) VALUES ("
				+ server_id_str + ", '" + server.getName() + "', '" + server.getDescription() + "', '" + server.getLocation() + "', '" + server.getContact() + "', '" + server.getHostname() + "', " + DatabaseDate.toUpdateSubString(server.getCreated()) + ", " + DatabaseDate.toUpdateSubString(server.getModified()) + ", " + Integer.toString(server.getSessions()) + ")";
		Statement statement = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("Server_Database.insertServer | Trying: " + sql, Log.DEBUGLEVEL05);
			statement.executeUpdate(sql);
			connection.commit();
		}
		catch (SQLException sqle) {
			String mesg = "Server_Database.insertServer | Cannot insert server " + server_id_str;
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
		Server server = this.fromStorableObject(storableObject);
		switch (update_kind) {
			default:
				return;
		}
	}
}