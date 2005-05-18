/*
 * $Id: ShadowDatabase.java,v 1.2 2005/05/18 13:29:31 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.security;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.2 $, $Date: 2005/05/18 13:29:31 $
 * @author $Author: bass $
 * @module leserver_v1
 */
public final class ShadowDatabase {
	private static final String TABLE_NAME_SHADOW = "Shadow";
	private static final String COLUMN_USER_ID = "user_id";
	private static final String COLUMN_PASSWORD = "password";
	private static final int SIZE_COLUMN_PASSWORD = 64;

	private StringBuffer singleWhereClause(Identifier userId) {
		return new StringBuffer(COLUMN_USER_ID + StorableObjectDatabase.EQUALS + DatabaseIdentifier.toSQLString(userId));
	}

	private StringBuffer retrieveQuery(StringBuffer condition) {
		final StringBuffer sql = new StringBuffer(StorableObjectDatabase.SQL_SELECT
				+ COLUMN_USER_ID + StorableObjectDatabase.COMMA
				+ COLUMN_PASSWORD
				+ StorableObjectDatabase.SQL_FROM + TABLE_NAME_SHADOW);

		if (condition != null && condition.length() != 0) {
			sql.append(StorableObjectDatabase.SQL_WHERE);
			sql.append(condition);
		}

		return sql;
	}

	public String retrieve(Identifier userId) throws RetrieveObjectException, ObjectNotFoundException {
		final StringBuffer sql = this.retrieveQuery(this.singleWhereClause(userId));

		Statement statement = null;
		ResultSet resultSet = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage("ShadowDatabase.retrieve | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql.toString());
			if (resultSet.next())
				return DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_PASSWORD));
			throw new ObjectNotFoundException("Cannot find password for user '"+ userId + "'");
		}
		catch (SQLException sqle) {
			String mesg = "Cannot retrieve user password " + sqle.getMessage();
			throw new RetrieveObjectException(mesg, sqle);
		}
		finally {
			try {
				try {
					if (resultSet != null)
						resultSet.close();
				}
				finally {
					try {
						if (statement != null)
							statement.close();
					}
					finally {
						DatabaseConnection.releaseConnection(connection);
					}
				}
			}
			catch (SQLException sqle) {
				Log.errorException(sqle);
			}
		}
	}

	protected void updateOrInsert(Identifier userId, String password) throws UpdateObjectException {
		StringBuffer sql = null;
		try {
			String oldPassword = this.retrieve(userId);
			if (oldPassword != null && oldPassword.length() != 0)
				sql = this.updateQuery(userId, password);
			else
				sql = this.insertQuery(userId, password);
		}
		catch (RetrieveObjectException roe) {
			throw new UpdateObjectException(roe);
		}
		catch (ObjectNotFoundException onfe) {
			sql = this.insertQuery(userId, password);
		}

		Statement statement = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage("ShadowDatabase.update | Trying: " + sql, Log.DEBUGLEVEL09);
			statement.executeUpdate(sql.toString());
			connection.commit();
		}
		catch (SQLException sqle) {
			String mesg = "Cannot set password of user '" + userId + "' -- " + sqle.getMessage();
			throw new UpdateObjectException(mesg, sqle);
		}
		finally {
			try {
				if (statement != null)
					statement.close();
				statement = null;
			}
			catch (SQLException sqle1) {
				Log.errorException(sqle1);
			}
			finally {
				DatabaseConnection.releaseConnection(connection);
			}
		}
	}

	private StringBuffer insertQuery(Identifier userId, String password) {
		return new StringBuffer(StorableObjectDatabase.SQL_INSERT_INTO + TABLE_NAME_SHADOW
				+ StorableObjectDatabase.OPEN_BRACKET
				+ COLUMN_USER_ID + StorableObjectDatabase.COMMA
				+ COLUMN_PASSWORD
				+ StorableObjectDatabase.CLOSE_BRACKET + StorableObjectDatabase.SQL_VALUES + StorableObjectDatabase.OPEN_BRACKET
				+ DatabaseIdentifier.toSQLString(userId) + StorableObjectDatabase.COMMA
				+ DatabaseString.toQuerySubString(password, SIZE_COLUMN_PASSWORD)
				+ StorableObjectDatabase.CLOSE_BRACKET);
	}

	private StringBuffer updateQuery(Identifier userId, String password) {
		return new StringBuffer(StorableObjectDatabase.SQL_UPDATE + TABLE_NAME_SHADOW + StorableObjectDatabase.SQL_SET
				+ COLUMN_PASSWORD + StorableObjectDatabase.EQUALS + DatabaseString.toQuerySubString(password, SIZE_COLUMN_PASSWORD)
				+ StorableObjectDatabase.SQL_WHERE + this.singleWhereClause(userId));
	}
}
