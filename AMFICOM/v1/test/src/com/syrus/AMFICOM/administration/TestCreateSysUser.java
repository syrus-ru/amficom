/*
 * $Id: TestCreateSysUser.java,v 1.2 2005/06/20 15:13:53 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.administration;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.syrus.AMFICOM.administration.corba.SystemUser_TransferablePackage.SystemUserSort;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CommonTest;
import com.syrus.AMFICOM.general.DatabaseCommonTest;
import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.DatabaseIdentifierGeneratorServer;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.util.Application;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.2 $, $Date: 2005/06/20 15:13:53 $
 * @author $Author: arseniy $
 * @module test
 */
public final class TestCreateSysUser extends TestCase {
	private static final String TABLE_SHADOW = "Shadow";
	private static final String COLUMN_USER_ID = "user_id";
	private static final String COLUMN_PASSWORD = "password";
	private static final String SYS_PASSWORD = SystemUserWrapper.SYS_LOGIN;

	public TestCreateSysUser(final String name) {
		super(name);
	}

	public static Test suite() {
		TestSuite testSuite = new TestSuite(TestCreateSysUser.class);
		TestSetup testSetup = new TestSetup(testSuite) {
			public void setUp() {
				oneTimeSetUp();
			}
			public void tearDown() {
				oneTimeTearDown();
			}
		};
		return testSetup;
	}

	static void oneTimeSetUp() {
		Application.init(CommonTest.APPLICATION_NAME);
		DatabaseCommonTest.establishDatabaseConnection();
		IdentifierPool.init(new DatabaseIdentifierGeneratorServer(), 1);
	}

	static void oneTimeTearDown() {
		DatabaseConnection.closeConnection();
	}

	public void testCreateSysUser() throws ApplicationException, SQLException {
		final Identifier sysUserId = IdentifierPool.getGeneratedIdentifier(ObjectEntities.SYSTEMUSER_CODE);
		final Date date = new Date(System.currentTimeMillis());
		final String name = "sys";
		final String description = "System administrator";
		String sql;
		final Statement statement = DatabaseConnection.getConnection().createStatement();

		sql = StorableObjectDatabase.SQL_INSERT_INTO + ObjectEntities.SYSTEMUSER + StorableObjectDatabase.OPEN_BRACKET
				+ StorableObjectWrapper.COLUMN_ID + StorableObjectDatabase.COMMA
				+ StorableObjectWrapper.COLUMN_CREATED + StorableObjectDatabase.COMMA
				+ StorableObjectWrapper.COLUMN_MODIFIED + StorableObjectDatabase.COMMA
				+ StorableObjectWrapper.COLUMN_CREATOR_ID + StorableObjectDatabase.COMMA
				+ StorableObjectWrapper.COLUMN_MODIFIER_ID + StorableObjectDatabase.COMMA
				+ StorableObjectWrapper.COLUMN_VERSION + StorableObjectDatabase.COMMA
				+ SystemUserWrapper.COLUMN_LOGIN + StorableObjectDatabase.COMMA
				+ SystemUserWrapper.COLUMN_SORT + StorableObjectDatabase.COMMA
				+ StorableObjectWrapper.COLUMN_NAME + StorableObjectDatabase.COMMA
				+ StorableObjectWrapper.COLUMN_DESCRIPTION
				+ StorableObjectDatabase.CLOSE_BRACKET + StorableObjectDatabase.SQL_VALUES + StorableObjectDatabase.OPEN_BRACKET
				+ DatabaseIdentifier.toSQLString(sysUserId) + StorableObjectDatabase.COMMA
				+ DatabaseDate.toUpdateSubString(date) + StorableObjectDatabase.COMMA
				+ DatabaseDate.toUpdateSubString(date) + StorableObjectDatabase.COMMA
				+ DatabaseIdentifier.toSQLString(sysUserId) + StorableObjectDatabase.COMMA
				+ DatabaseIdentifier.toSQLString(sysUserId) + StorableObjectDatabase.COMMA
				+ Long.toString(0) + StorableObjectDatabase.COMMA
				+ StorableObjectDatabase.APOSTOPHE
					+ DatabaseString.toQuerySubString(SystemUserWrapper.SYS_LOGIN, SystemUserDatabase.SIZE_LOGIN_COLUMN)
						+ StorableObjectDatabase.APOSTOPHE + StorableObjectDatabase.COMMA
				+ Integer.toString(SystemUserSort._USER_SORT_SYSADMIN) + StorableObjectDatabase.COMMA
				+ StorableObjectDatabase.APOSTOPHE
					+ DatabaseString.toQuerySubString(name, StorableObjectDatabase.SIZE_NAME_COLUMN)
						+ StorableObjectDatabase.APOSTOPHE + StorableObjectDatabase.COMMA 
				+ StorableObjectDatabase.APOSTOPHE
						+ DatabaseString.toQuerySubString(description, StorableObjectDatabase.SIZE_NAME_COLUMN)
							+ StorableObjectDatabase.APOSTOPHE
				+ StorableObjectDatabase.CLOSE_BRACKET;
		Log.debugMessage("Trying: " + sql, Log.DEBUGLEVEL09);
		statement.executeUpdate(sql);

		sql = StorableObjectDatabase.SQL_INSERT_INTO + TABLE_SHADOW + StorableObjectDatabase.OPEN_BRACKET
				+ COLUMN_USER_ID + StorableObjectDatabase.COMMA
				+ COLUMN_PASSWORD
				+ StorableObjectDatabase.CLOSE_BRACKET + StorableObjectDatabase.SQL_VALUES + StorableObjectDatabase.OPEN_BRACKET
				+ DatabaseIdentifier.toSQLString(sysUserId) + StorableObjectDatabase.COMMA
				+ StorableObjectDatabase.APOSTOPHE + DatabaseString.toQuerySubString(SYS_PASSWORD) + StorableObjectDatabase.APOSTOPHE
				+ StorableObjectDatabase.CLOSE_BRACKET;
		Log.debugMessage("Trying: " + sql, Log.DEBUGLEVEL09);
		statement.executeUpdate(sql);

		statement.close();
	}

}
