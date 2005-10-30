/*
 * $Id: ShadowDatabase.java,v 1.11 2005/10/30 14:49:11 bass Exp $
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
 * @version $Revision: 1.11 $, $Date: 2005/10/30 14:49:11 $
 * @author $Author: bass $
 * @author Tashoyan Arseniy Feliksovich
 * @module leserver
 */
public final class ShadowDatabase {
	private static final String TABLE_NAME_SHADOW = "Shadow";
	private static final String COLUMN_USER_ID = "user_id";
	private static final String COLUMN_PASSWORD = "password";
	private static final int SIZE_COLUMN_PASSWORD = 64;

	private StringBuffer singleWhereClause(final Identifier userId) {
		return new StringBuffer(COLUMN_USER_ID + StorableObjectDatabase.EQUALS + DatabaseIdentifier.toSQLString(userId));
	}

	private StringBuffer retrieveQuery(final StringBuffer condition) {
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

	public String retrieve(final Identifier userId) throws RetrieveObjectException, ObjectNotFoundException {
		final StringBuffer sql = this.retrieveQuery(this.singleWhereClause(userId));

		Statement statement = null;
		ResultSet resultSet = null;
		Connection connection = null;
		try {
			connection = DatabaseConnection.getConnection();
			statement = connection.createStatement();
			Log.debugMessage("Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql.toString());
			if (resultSet.next()) {
				return DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_PASSWORD));
			}
			throw new ObjectNotFoundException("Cannot find password for user '" + userId + "'");
		} catch (SQLException sqle) {
			final String mesg = "Cannot retrieve user password " + sqle.getMessage();
			throw new RetrieveObjectException(mesg, sqle);
		} finally {
			try {
				try {
					if (resultSet != null) {
						resultSet.close();
						resultSet = null;
					}
				} finally {
					try {
						if (statement != null) {
							statement.close();
							statement = null;
						}
					} finally {
						if (connection != null) {
							DatabaseConnection.releaseConnection(connection);
							connection = null;
						}
					}
				}
			} catch (SQLException sqle) {
				Log.errorMessage(sqle);
			}
		}
	}

	public void updateOrInsert(final Identifier userId, final String password) throws UpdateObjectException {
		StringBuffer sql = null;
		try {
			final String oldPassword = this.retrieve(userId);
			if (oldPassword != null && oldPassword.length() != 0) {
				sql = this.updateQuery(userId, password);
			} else {
				sql = this.insertQuery(userId, password);
			}
		} catch (RetrieveObjectException roe) {
			throw new UpdateObjectException(roe);
		} catch (ObjectNotFoundException onfe) {
			sql = this.insertQuery(userId, password);
		}

		Statement statement = null;
		Connection connection = null;
		try {
			connection = DatabaseConnection.getConnection();
			statement = connection.createStatement();
			Log.debugMessage("Trying: " + sql, Log.DEBUGLEVEL09);
			statement.executeUpdate(sql.toString());
			connection.commit();
		}
		catch (SQLException sqle) {
			final String mesg = "Cannot set password of user '" + userId + "' -- " + sqle.getMessage();
			throw new UpdateObjectException(mesg, sqle);
		}
		finally {
			try {
				try {
					if (statement != null) {
						statement.close();
						statement = null;
					}
				} finally {
					if (connection != null) {
						DatabaseConnection.releaseConnection(connection);
						connection = null;
					}
				}
			} catch (SQLException sqle1) {
				Log.errorMessage(sqle1);
			}
		}
	}

	private StringBuffer insertQuery(final Identifier userId, final String password) {
		return new StringBuffer(StorableObjectDatabase.SQL_INSERT_INTO + TABLE_NAME_SHADOW
				+ StorableObjectDatabase.OPEN_BRACKET
				+ COLUMN_USER_ID + StorableObjectDatabase.COMMA
				+ COLUMN_PASSWORD
				+ StorableObjectDatabase.CLOSE_BRACKET + StorableObjectDatabase.SQL_VALUES + StorableObjectDatabase.OPEN_BRACKET
				+ DatabaseIdentifier.toSQLString(userId) + StorableObjectDatabase.COMMA
				+ StorableObjectDatabase.APOSTROPHE + DatabaseString.toQuerySubString(password, SIZE_COLUMN_PASSWORD) + StorableObjectDatabase.APOSTROPHE
				+ StorableObjectDatabase.CLOSE_BRACKET);
	}

	private StringBuffer updateQuery(final Identifier userId, final String password) {
		return new StringBuffer(StorableObjectDatabase.SQL_UPDATE + TABLE_NAME_SHADOW + StorableObjectDatabase.SQL_SET
				+ COLUMN_PASSWORD + StorableObjectDatabase.EQUALS
					+ StorableObjectDatabase.APOSTROPHE + DatabaseString.toQuerySubString(password, SIZE_COLUMN_PASSWORD) + StorableObjectDatabase.APOSTROPHE
				+ StorableObjectDatabase.SQL_WHERE + this.singleWhereClause(userId));
	}
}
