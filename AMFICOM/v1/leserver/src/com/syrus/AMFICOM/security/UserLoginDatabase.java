/*
 * $Id: UserLoginDatabase.java,v 1.17 2005/11/28 12:31:38 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.security;

import static com.syrus.AMFICOM.general.StorableObjectDatabase.APOSTROPHE;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.CLOSE_BRACKET;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.COMMA;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.EQUALS;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.OPEN_BRACKET;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.SQL_DELETE_FROM;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.SQL_FROM;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.SQL_INSERT_INTO;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.SQL_SELECT;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.SQL_SET;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.SQL_UPDATE;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.SQL_VALUES;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.SQL_WHERE;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.17 $, $Date: 2005/11/28 12:31:38 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module leserver
 */
public final class UserLoginDatabase {
	private static final String TABLE_NAME_USER_LOGIN = "UserLogin";
	private static final String COLUMN_SESSION_KEY = "session_key";
	private static final String COLUMN_USER_ID = "user_id";
	private static final String COLUMN_DOMAIN_ID = "domain_id";
	private static final String COLUMN_USER_IOR = "user_ior";
	private static final String COLUMN_LOGIN_DATE = "login_date";
	private static final String COLUMN_LAST_ACTIVITY_DATE = "last_activity_date";
	private static final int SIZE_COLUMN_SESSION_KEY = 128;
	private static final int SIZE_COLUMN_USER_IOR = 1000;

	private StringBuffer singleWhereClause(final SessionKey sessionKey) {
		return new StringBuffer(COLUMN_SESSION_KEY + EQUALS
				+ APOSTROPHE + DatabaseString.toQuerySubString(sessionKey.stringValue(), SIZE_COLUMN_SESSION_KEY) + APOSTROPHE);
	}

	private StringBuffer retrieveQuery(final StringBuffer condition) {
		final StringBuffer sql = new StringBuffer(SQL_SELECT
				+ COLUMN_SESSION_KEY + COMMA
				+ COLUMN_USER_ID + COMMA
				+ COLUMN_DOMAIN_ID + COMMA
				+ COLUMN_USER_IOR + COMMA
				+ DatabaseDate.toQuerySubString(COLUMN_LOGIN_DATE) + COMMA
				+ DatabaseDate.toQuerySubString(COLUMN_LAST_ACTIVITY_DATE)
				+ SQL_FROM + TABLE_NAME_USER_LOGIN);

		if (condition != null && condition.length() != 0) {
			sql.append(SQL_WHERE);
			sql.append(condition);
		}

		return sql;
	}

	public Set<UserLogin> retrieveAll() throws RetrieveObjectException {
		return this.retrieveByCondition(null);
	}

	protected UserLogin retrieve(final SessionKey sessionKey) throws RetrieveObjectException, ObjectNotFoundException {
		final Set<UserLogin> objects = this.retrieveByCondition(this.singleWhereClause(sessionKey));
		if (!objects.isEmpty()) {
			return objects.iterator().next();
		}
		throw new ObjectNotFoundException("User login not found");
	}

	private Set<UserLogin> retrieveByCondition(final StringBuffer condition) throws RetrieveObjectException {
		final Set<UserLogin> objects = new HashSet<UserLogin>();

		final StringBuffer sql = this.retrieveQuery(condition);

		Statement statement = null;
		ResultSet resultSet = null;
		Connection connection = null;
		try {
			connection = DatabaseConnection.getConnection();
			statement = connection.createStatement();
			Log.debugMessage("Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql.toString());
			while (resultSet.next()) {
				final UserLogin userLogin = new UserLogin(new SessionKey(DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_SESSION_KEY))),
						DatabaseIdentifier.getIdentifier(resultSet, COLUMN_USER_ID),
						DatabaseIdentifier.getIdentifier(resultSet, COLUMN_DOMAIN_ID),
						DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_USER_IOR)),
						DatabaseDate.fromQuerySubString(resultSet, COLUMN_LOGIN_DATE),
						DatabaseDate.fromQuerySubString(resultSet, COLUMN_LAST_ACTIVITY_DATE));
				objects.add(userLogin);
			}
		} catch (SQLException sqle) {
			final String mesg = "Cannot retrieve user login" + sqle.getMessage();
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

		return objects;
	}

	public void insert(final UserLogin userLogin) throws CreateObjectException {
		final StringBuffer sql = new StringBuffer(SQL_INSERT_INTO + TABLE_NAME_USER_LOGIN + OPEN_BRACKET
				+ COLUMN_SESSION_KEY + COMMA
				+ COLUMN_USER_ID + COMMA
				+ COLUMN_DOMAIN_ID + COMMA
				+ COLUMN_USER_IOR + COMMA
				+ COLUMN_LOGIN_DATE + COMMA
				+ COLUMN_LAST_ACTIVITY_DATE
				+ CLOSE_BRACKET + SQL_VALUES + OPEN_BRACKET
				+ APOSTROPHE + DatabaseString.toQuerySubString(userLogin.getSessionKey().stringValue(), SIZE_COLUMN_SESSION_KEY) + APOSTROPHE + COMMA
				+ DatabaseIdentifier.toSQLString(userLogin.getUserId()) + COMMA
				+ DatabaseIdentifier.toSQLString(userLogin.getDomainId()) + COMMA
				+ APOSTROPHE + DatabaseString.toQuerySubString(userLogin.getUserIOR(), SIZE_COLUMN_USER_IOR) + APOSTROPHE + COMMA
				+ DatabaseDate.toUpdateSubString(userLogin.getLoginDate()) + COMMA
				+ DatabaseDate.toUpdateSubString(userLogin.getLastActivityDate())
				+ CLOSE_BRACKET);

		Statement statement = null;
		Connection connection = null;
		try {
			connection = DatabaseConnection.getConnection();
			statement = connection.createStatement();
			Log.debugMessage("Trying: " + sql, Log.DEBUGLEVEL09);
			statement.executeUpdate(sql.toString());
			connection.commit();
		} catch (SQLException sqle) {
			final String mesg = "Cannot insert user login" + sqle.getMessage();
			throw new CreateObjectException(mesg, sqle);
		} finally {
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

	public void update(final UserLogin userLogin) throws UpdateObjectException {
		final StringBuffer sql = new StringBuffer(SQL_UPDATE + TABLE_NAME_USER_LOGIN + SQL_SET
				+ COLUMN_USER_ID + EQUALS + DatabaseIdentifier.toSQLString(userLogin.getUserId()) + COMMA
				+ COLUMN_DOMAIN_ID + EQUALS + DatabaseIdentifier.toSQLString(userLogin.getDomainId()) + COMMA
				+ COLUMN_USER_IOR + EQUALS + APOSTROPHE + DatabaseString.toQuerySubString(userLogin.getUserIOR(), SIZE_COLUMN_USER_IOR) + APOSTROPHE + COMMA
				+ COLUMN_LOGIN_DATE + EQUALS + DatabaseDate.toUpdateSubString(userLogin.getLoginDate()) + COMMA
				+ COLUMN_LAST_ACTIVITY_DATE + EQUALS + DatabaseDate.toUpdateSubString(userLogin.getLastActivityDate())
				+ SQL_WHERE + this.singleWhereClause(userLogin.getSessionKey()));

		Statement statement = null;
		Connection connection = null;
		try {
			connection = DatabaseConnection.getConnection();
			statement = connection.createStatement();
			Log.debugMessage("Trying: " + sql, Log.DEBUGLEVEL09);
			statement.executeUpdate(sql.toString());
			connection.commit();
		} catch (SQLException sqle) {
			final String mesg = "Cannot update user login" + sqle.getMessage();
			throw new UpdateObjectException(mesg, sqle);
		} finally {
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

	public void delete(final UserLogin userLogin) {
		final StringBuffer sql = new StringBuffer(SQL_DELETE_FROM + TABLE_NAME_USER_LOGIN
				+ SQL_WHERE + this.singleWhereClause(userLogin.getSessionKey()));

		Statement statement = null;
		Connection connection = null;
		try {
			connection = DatabaseConnection.getConnection();
			statement = connection.createStatement();
			Log.debugMessage("Trying: " + sql, Log.DEBUGLEVEL09);
			statement.executeUpdate(sql.toString());
			connection.commit();
		} catch (SQLException sqle) {
			Log.errorMessage(sqle);
		} finally {
			try {
				if (statement != null) {
					statement.close();
					statement = null;
				}
			} catch (SQLException sqle1) {
				Log.errorMessage(sqle1);
			} finally {
				if (connection != null) {
					DatabaseConnection.releaseConnection(connection);
					connection = null;
				}
			}
		}
	}

}
