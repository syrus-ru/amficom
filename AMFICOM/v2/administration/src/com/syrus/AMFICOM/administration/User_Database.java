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

public class User_Database extends StorableObject_Database {

	private User fromStorableObject(StorableObject storableObject) throws Exception {
		if (storableObject instanceof User)
			return (User)storableObject;
		else
			throw new Exception("User_Database.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	public void retrieve(StorableObject storableObject) throws Exception {
		User user = this.fromStorableObject(storableObject);
		this.retrieveUser(user);
		this.retrieveCategoryIds(user);
		this.retrieveGroupIds(user);
	}

	private void retrieveUser(User user) throws Exception {
		String user_id_str = user.getId().toString();
		String sql = "SELECT login, type, " + DatabaseDate.toQuerySubString("last_logged") + ", " + DatabaseDate.toQuerySubString("logged") + ", sessions FROM " + ObjectEntities.USER_ENTITY + " WHERE id = " + user_id_str;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("User_Database.retrieveUser | Trying: " + sql, Log.DEBUGLEVEL05);
			resultSet = statement.executeQuery(sql);
			if (resultSet.next())
				user.setAttributes(resultSet.getString("login"),
													 resultSet.getString("type"),
													 DatabaseDate.fromQuerySubString(resultSet, "last_logged"),
													 DatabaseDate.fromQuerySubString(resultSet, "logged"),
													 resultSet.getInt("sessions"));
			else
				throw new Exception("No such user: " + user_id_str);
		}
		catch (SQLException sqle) {
			String mesg = "User_Database.retrieveUser | Cannot retrieve user " + user_id_str;
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

	private void retrieveCategoryIds(User user) throws Exception {
		String user_id_str = user.getId().toString();
		
	}

	private void retrieveGroupIds(User user) throws Exception {
		String user_id_str = user.getId().toString();
		
	}

	public Object retrieveObject(StorableObject storableObject, int retrieve_kind, Object arg) throws Exception {
		User user = this.fromStorableObject(storableObject);
		switch (retrieve_kind) {
			default:
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws Exception {
		User user = this.fromStorableObject(storableObject);
		
	}

	public void update(StorableObject storableObject, int update_kind, Object obj) throws Exception {
		User user = this.fromStorableObject(storableObject);
		switch (update_kind) {
			default:
				return;
		}
	}
}