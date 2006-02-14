/*
 * $Id: TestCreateSysUser.java,v 1.8 2006/02/14 14:05:09 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.administration;

import static com.syrus.AMFICOM.administration.SystemUserWrapper.COLUMN_LOGIN;
import static com.syrus.AMFICOM.administration.SystemUserWrapper.COLUMN_SORT;
import static com.syrus.AMFICOM.administration.SystemUserWrapper.SYS_LOGIN;
import static com.syrus.AMFICOM.administration.corba.IdlSystemUserPackage.SystemUserSort._USER_SORT_SYSADMIN;
import static com.syrus.AMFICOM.general.ObjectEntities.SYSTEMUSER;
import static com.syrus.AMFICOM.general.Identifier.VOID_IDENTIFIER;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.APOSTROPHE;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.CLOSE_BRACKET;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.COMMA;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.OPEN_BRACKET;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.SIZE_DESCRIPTION_COLUMN;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.SIZE_NAME_COLUMN;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.SQL_INSERT_INTO;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.SQL_VALUES;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_CREATED;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_CREATOR_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_DESCRIPTION;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_MODIFIED;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_MODIFIER_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_NAME;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_VERSION;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import junit.framework.Test;
import junit.framework.TestCase;

import com.syrus.AMFICOM.administration.SystemUser;
import com.syrus.AMFICOM.administration.SystemUserDatabase;
import com.syrus.AMFICOM.administration.corba.IdlSystemUserPackage.SystemUserSort;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.SQLCommonTest;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.8 $, $Date: 2006/02/14 14:05:09 $
 * @author $Author: arseniy $
 * @module test
 */
public final class TestCreateSysUser extends TestCase {
	private static final String SYS_PASSWORD = SYS_LOGIN;

	public TestCreateSysUser(final String name) {
		super(name);
	}

	public static Test suite() {
		final SQLCommonTest commonTest = new SQLCommonTest();
		commonTest.addTestSuite(TestCreateSysUser.class);
		return commonTest.createTestSetup();
	}

	public void testCreateSysUser() throws ApplicationException, SQLException {
		final Identifier sysUserId = IdentifierPool.getGeneratedIdentifier(ObjectEntities.SYSTEMUSER_CODE);
		final Date date = new Date(System.currentTimeMillis());
		final String name = "sys";
		final String description = "System administrator";

		Connection connection = null;
		Statement statement = null;
		try {
			connection = DatabaseConnection.getConnection();
			statement = connection.createStatement();
			String sql;

			sql = SQL_INSERT_INTO + SYSTEMUSER + OPEN_BRACKET
					+ COLUMN_ID + COMMA
					+ COLUMN_CREATED + COMMA
					+ COLUMN_MODIFIED + COMMA
					+ COLUMN_CREATOR_ID + COMMA
					+ COLUMN_MODIFIER_ID + COMMA
					+ COLUMN_VERSION + COMMA
					+ COLUMN_LOGIN + COMMA
					+ COLUMN_SORT + COMMA
					+ COLUMN_NAME + COMMA
					+ COLUMN_DESCRIPTION
					+ CLOSE_BRACKET + SQL_VALUES + OPEN_BRACKET
					+ DatabaseIdentifier.toSQLString(sysUserId) + COMMA
					+ DatabaseDate.toUpdateSubString(date) + COMMA
					+ DatabaseDate.toUpdateSubString(date) + COMMA
					+ DatabaseIdentifier.toSQLString(sysUserId) + COMMA
					+ DatabaseIdentifier.toSQLString(sysUserId) + COMMA
					+ Long.toString(0) + COMMA
					+ APOSTROPHE + DatabaseString.toQuerySubString(SYS_LOGIN, SIZE_LOGIN_COLUMN) + APOSTROPHE + COMMA
					+ Integer.toString(_USER_SORT_SYSADMIN) + COMMA
					+ APOSTROPHE + DatabaseString.toQuerySubString(name, SIZE_NAME_COLUMN) + APOSTROPHE + COMMA
					+ APOSTROPHE + DatabaseString.toQuerySubString(description, SIZE_DESCRIPTION_COLUMN) + APOSTROPHE
					+ StorableObjectDatabase.CLOSE_BRACKET;
			Log.debugMessage("Trying: " + sql, Log.DEBUGLEVEL09);
			statement.executeUpdate(sql);

			sql = SQL_INSERT_INTO + TABLE_SHADOW + OPEN_BRACKET
					+ COLUMN_USER_ID + COMMA
					+ COLUMN_PASSWORD
					+ CLOSE_BRACKET + SQL_VALUES + OPEN_BRACKET
					+ DatabaseIdentifier.toSQLString(sysUserId) + COMMA
					+ APOSTROPHE + DatabaseString.toQuerySubString(SYS_PASSWORD) + APOSTROPHE
					+ CLOSE_BRACKET;
			Log.debugMessage("Trying: " + sql, Log.DEBUGLEVEL09);
			statement.executeUpdate(sql);

			statement.close();
		} finally {
			if (statement != null) {
				try {
					statement.close();
					statement = null;
				}
				finally {
					DatabaseConnection.releaseConnection(connection);
					connection = null;
				}
			}
		}
	}

}
