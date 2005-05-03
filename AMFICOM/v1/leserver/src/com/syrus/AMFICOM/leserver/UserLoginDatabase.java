/*
 * $Id: UserLoginDatabase.java,v 1.3 2005/05/03 14:36:40 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.leserver;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.SessionKey;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.3 $, $Date: 2005/05/03 14:36:40 $
 * @author $Author: arseniy $
 * @module leserver_v1
 */
final class UserLoginDatabase {
	private static final String TABLE_NAME_USER_LOGIN = "UserLogin";
	private static final String COLUMN_SESSION_KEY = "session_key";
	private static final String COLUMN_USER_ID = "user_id";
	private static final String COLUMN_DOMAIN_ID = "domain_id";
	private static final String COLUMN_LOGIN_DATE = "login_date";
	private static final String COLUMN_LAST_ACTIVITY_DATE = "last_activity_date";
	private static final int SIZE_COLUMN_SESSION_KEY = 128;

	private StringBuffer singleWhereClause(final SessionKey sessionKey) {
		return new StringBuffer(COLUMN_SESSION_KEY + StorableObjectDatabase.EQUALS
				+ StorableObjectDatabase.APOSTOPHE
				+ DatabaseString.toQuerySubString(sessionKey.toString(), SIZE_COLUMN_SESSION_KEY)
				+ StorableObjectDatabase.APOSTOPHE);
	}

	private StringBuffer retrieveQuery(final StringBuffer condition) {
		StringBuffer sql = new StringBuffer(StorableObjectDatabase.SQL_SELECT
				+ COLUMN_SESSION_KEY + StorableObjectDatabase.COMMA
				+ COLUMN_USER_ID + StorableObjectDatabase.COMMA
				+ COLUMN_DOMAIN_ID + StorableObjectDatabase.COMMA
				+ DatabaseDate.toQuerySubString(COLUMN_LOGIN_DATE) + StorableObjectDatabase.COMMA
				+ DatabaseDate.toQuerySubString(COLUMN_LAST_ACTIVITY_DATE)
				+ StorableObjectDatabase.SQL_FROM + TABLE_NAME_USER_LOGIN);

		if (condition != null && condition.length() != 0) {
			sql.append(StorableObjectDatabase.SQL_WHERE);
			sql.append(condition);
		}

		return sql;
	}

	protected UserLogin retrieve(final SessionKey sessionKey) throws RetrieveObjectException, ObjectNotFoundException {
		StringBuffer sql = this.retrieveQuery(this.singleWhereClause(sessionKey));

		Statement statement = null;
		ResultSet resultSet = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage("UserLoginDatabase.retrieve | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql.toString());
			if (resultSet.next())
				return new UserLogin(sessionKey,
						DatabaseIdentifier.getIdentifier(resultSet, COLUMN_USER_ID),
						DatabaseIdentifier.getIdentifier(resultSet, COLUMN_DOMAIN_ID),
						DatabaseDate.fromQuerySubString(resultSet, COLUMN_LOGIN_DATE),
						DatabaseDate.fromQuerySubString(resultSet, COLUMN_LAST_ACTIVITY_DATE));
			throw new ObjectNotFoundException("User login not found");
		}
		catch (SQLException sqle) {
			String mesg = "Cannot retrieve user login" + sqle.getMessage();
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

	protected void insert(final UserLogin userLogin) throws CreateObjectException {
		final StringBuffer sql = new StringBuffer(StorableObjectDatabase.SQL_INSERT_INTO + TABLE_NAME_USER_LOGIN
				+ StorableObjectDatabase.OPEN_BRACKET
				+ COLUMN_SESSION_KEY + StorableObjectDatabase.COMMA
				+ COLUMN_USER_ID + StorableObjectDatabase.COMMA
				+ COLUMN_DOMAIN_ID + StorableObjectDatabase.COMMA
				+ COLUMN_LOGIN_DATE + StorableObjectDatabase.COMMA
				+ COLUMN_LAST_ACTIVITY_DATE
				+ StorableObjectDatabase.CLOSE_BRACKET + StorableObjectDatabase.SQL_VALUES + StorableObjectDatabase.OPEN_BRACKET
				+ StorableObjectDatabase.APOSTOPHE + DatabaseString.toQuerySubString(userLogin.getSessionKey().toString(), SIZE_COLUMN_SESSION_KEY) + StorableObjectDatabase.APOSTOPHE + StorableObjectDatabase.COMMA
				+ DatabaseIdentifier.toSQLString(userLogin.getUserId()) + StorableObjectDatabase.COMMA
				+ DatabaseIdentifier.toSQLString(userLogin.getDomainId()) + StorableObjectDatabase.COMMA
				+ DatabaseDate.toUpdateSubString(userLogin.getLoginDate()) + StorableObjectDatabase.COMMA
				+ DatabaseDate.toUpdateSubString(userLogin.getLastActivityDate())
				+ StorableObjectDatabase.CLOSE_BRACKET);

		Statement statement = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage("UserLoginDatabase.insert | Trying: " + sql, Log.DEBUGLEVEL09);
			statement.executeUpdate(sql.toString());
			connection.commit();
		}
		catch (SQLException sqle) {
			String mesg = "Cannot insert user login" + sqle.getMessage();
			throw new CreateObjectException(mesg, sqle);
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

	protected void update(final UserLogin userLogin) throws UpdateObjectException {
		final StringBuffer sql = new StringBuffer(StorableObjectDatabase.SQL_UPDATE + TABLE_NAME_USER_LOGIN + StorableObjectDatabase.SQL_SET
				+ COLUMN_USER_ID + StorableObjectDatabase.EQUALS + DatabaseIdentifier.toSQLString(userLogin.getUserId())
				+ StorableObjectDatabase.COMMA
				+ COLUMN_DOMAIN_ID + StorableObjectDatabase.EQUALS + DatabaseIdentifier.toSQLString(userLogin.getDomainId())
				+ StorableObjectDatabase.COMMA
				+ COLUMN_LOGIN_DATE + StorableObjectDatabase.EQUALS + DatabaseDate.toUpdateSubString(userLogin.getLoginDate())
				+ StorableObjectDatabase.COMMA
				+ COLUMN_LAST_ACTIVITY_DATE + StorableObjectDatabase.EQUALS + DatabaseDate.toUpdateSubString(userLogin.getLastActivityDate())
				+ StorableObjectDatabase.SQL_WHERE + this.singleWhereClause(userLogin.getSessionKey()));

		Statement statement = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage("UserLoginDatabase.update | Trying: " + sql, Log.DEBUGLEVEL09);
			statement.executeUpdate(sql.toString());
			connection.commit();
		}
		catch (SQLException sqle) {
			String mesg = "Cannot update user login" + sqle.getMessage();
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

	protected void delete(final UserLogin userLogin) {
		final StringBuffer sql = new StringBuffer(StorableObjectDatabase.SQL_DELETE_FROM + TABLE_NAME_USER_LOGIN
				+ StorableObjectDatabase.SQL_WHERE + this.singleWhereClause(userLogin.getSessionKey()));

		Statement statement = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage("UserLoginDatabase.delete | Trying: " + sql, Log.DEBUGLEVEL09);
			statement.executeUpdate(sql.toString());
			connection.commit();
		}
		catch (SQLException sqle) {
			Log.errorException(sqle);
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

}
