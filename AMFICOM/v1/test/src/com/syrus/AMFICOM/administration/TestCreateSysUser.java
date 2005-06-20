/*
 * $Id: TestCreateSysUser.java,v 1.3 2005/06/20 17:38:10 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.administration;

import static com.syrus.AMFICOM.administration.SystemUserDatabase.SIZE_LOGIN_COLUMN;
import static com.syrus.AMFICOM.administration.SystemUserWrapper.COLUMN_LOGIN;
import static com.syrus.AMFICOM.administration.SystemUserWrapper.COLUMN_SORT;
import static com.syrus.AMFICOM.administration.SystemUserWrapper.SYS_LOGIN;
import static com.syrus.AMFICOM.administration.corba.SystemUser_TransferablePackage.SystemUserSort._USER_SORT_SYSADMIN;
import static com.syrus.AMFICOM.general.ObjectEntities.SYSTEMUSER;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.APOSTOPHE;
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

import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import junit.framework.Test;
import junit.framework.TestCase;

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
 * @version $Revision: 1.3 $, $Date: 2005/06/20 17:38:10 $
 * @author $Author: arseniy $
 * @module test
 */
public final class TestCreateSysUser extends TestCase {
	private static final String TABLE_SHADOW = "Shadow";
	private static final String COLUMN_USER_ID = "user_id";
	private static final String COLUMN_PASSWORD = "password";
	private static final String SYS_PASSWORD = SYS_LOGIN;

	public TestCreateSysUser(final String name) {
		super(name);
	}

	public static Test suite() {
		SQLCommonTest commonTest = new SQLCommonTest();
		commonTest.addTestSuite(TestCreateSysUser.class);
		return commonTest.createTestSetup();
	}

	public void testCreateSysUser() throws ApplicationException, SQLException {
		final Identifier sysUserId = IdentifierPool.getGeneratedIdentifier(ObjectEntities.SYSTEMUSER_CODE);
		final Date date = new Date(System.currentTimeMillis());
		final String name = "sys";
		final String description = "System administrator";
		String sql;
		final Statement statement = DatabaseConnection.getConnection().createStatement();

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
				+ APOSTOPHE + DatabaseString.toQuerySubString(SYS_LOGIN, SIZE_LOGIN_COLUMN) + APOSTOPHE + COMMA
				+ Integer.toString(_USER_SORT_SYSADMIN) + COMMA
				+ APOSTOPHE + DatabaseString.toQuerySubString(name, SIZE_NAME_COLUMN) + APOSTOPHE + COMMA 
				+ APOSTOPHE + DatabaseString.toQuerySubString(description, SIZE_DESCRIPTION_COLUMN) + APOSTOPHE
				+ StorableObjectDatabase.CLOSE_BRACKET;
		Log.debugMessage("Trying: " + sql, Log.DEBUGLEVEL09);
		statement.executeUpdate(sql);

		sql = SQL_INSERT_INTO + TABLE_SHADOW + OPEN_BRACKET
				+ COLUMN_USER_ID + COMMA
				+ COLUMN_PASSWORD
				+ CLOSE_BRACKET + SQL_VALUES + OPEN_BRACKET
				+ DatabaseIdentifier.toSQLString(sysUserId) + COMMA
				+ APOSTOPHE + DatabaseString.toQuerySubString(SYS_PASSWORD) + APOSTOPHE
				+ CLOSE_BRACKET;
		Log.debugMessage("Trying: " + sql, Log.DEBUGLEVEL09);
		statement.executeUpdate(sql);

		statement.close();
	}

}
