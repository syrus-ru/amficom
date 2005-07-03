/*
 * $Id: TestDataType.java,v 1.1 2005/06/20 17:34:41 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

import static com.syrus.AMFICOM.general.StorableObjectDatabase.CLOSE_BRACKET;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.COMMA;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.OPEN_BRACKET;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.QUESTION;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.SQL_INSERT_INTO;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.SQL_VALUES;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.SIZE_CODENAME_COLUMN;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.SIZE_DESCRIPTION_COLUMN;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_CODENAME;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_DESCRIPTION;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_ID;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.syrus.AMFICOM.general.corba.DataType;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;
import com.syrus.util.database.DatabaseString;

import junit.framework.Test;
import junit.framework.TestCase;

public final class TestDataType extends TestCase {
	private static final String TABLE_DATATYPE = "datatype";

	private static final String DESCRIPTION_INTEGER = "Integer";
	private static final String DESCRIPTION_DOUBLE = "Double";
	private static final String DESCRIPTION_STRING = "String";
	private static final String DESCRIPTION_DATE = "Date";
	private static final String DESCRIPTION_LONG = "Long";
	private static final String DESCRIPTION_RAW = "Raw";
	private static final String DESCRIPTION_BOOLEAN = "Boolean";

	public TestDataType(final String name) {
		super(name);
	}

	public static Test suite() {
		SQLCommonTest commonTest = new SQLCommonTest();
		commonTest.addTestSuite(TestDataType.class);
		return commonTest.createTestSetup();
	}

	public void testCreate() throws SQLException {
		final String sql = SQL_INSERT_INTO + TABLE_DATATYPE + OPEN_BRACKET
				+ COLUMN_ID + COMMA
				+ COLUMN_CODENAME + COMMA
				+ COLUMN_DESCRIPTION
				+ CLOSE_BRACKET + SQL_VALUES + OPEN_BRACKET
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION
				+ CLOSE_BRACKET;
		final PreparedStatement preparedStatement = DatabaseConnection.getConnection().prepareStatement(sql);

		//-1. Integer
		preparedStatement.setInt(1, DataType._DATA_TYPE_INTEGER);
		DatabaseString.setString(preparedStatement, 2, DataTypeCodename.INTEGER.toString(), SIZE_CODENAME_COLUMN);
		DatabaseString.setString(preparedStatement, 3, DESCRIPTION_INTEGER, SIZE_DESCRIPTION_COLUMN);
		Log.debugMessage("Creating " + DESCRIPTION_INTEGER, Log.DEBUGLEVEL08);
		preparedStatement.executeUpdate();

		//-2. Double
		preparedStatement.setInt(1, DataType._DATA_TYPE_DOUBLE);
		DatabaseString.setString(preparedStatement, 2, DataTypeCodename.DOUBLE.toString(), SIZE_CODENAME_COLUMN);
		DatabaseString.setString(preparedStatement, 3, DESCRIPTION_DOUBLE, SIZE_DESCRIPTION_COLUMN);
		Log.debugMessage("Creating " + DESCRIPTION_DOUBLE, Log.DEBUGLEVEL08);
		preparedStatement.executeUpdate();

		//-3. String
		preparedStatement.setInt(1, DataType._DATA_TYPE_STRING);
		DatabaseString.setString(preparedStatement, 2, DataTypeCodename.STRING.toString(), SIZE_CODENAME_COLUMN);
		DatabaseString.setString(preparedStatement, 3, DESCRIPTION_STRING, SIZE_DESCRIPTION_COLUMN);
		Log.debugMessage("Creating " + DESCRIPTION_STRING, Log.DEBUGLEVEL08);
		preparedStatement.executeUpdate();

		//-4. Date
		preparedStatement.setInt(1, DataType._DATA_TYPE_DATE);
		DatabaseString.setString(preparedStatement, 2, DataTypeCodename.DATE.toString(), SIZE_CODENAME_COLUMN);
		DatabaseString.setString(preparedStatement, 3, DESCRIPTION_DATE, SIZE_DESCRIPTION_COLUMN);
		Log.debugMessage("Creating " + DESCRIPTION_DATE, Log.DEBUGLEVEL08);
		preparedStatement.executeUpdate();

		//-5. Long
		preparedStatement.setInt(1, DataType._DATA_TYPE_LONG);
		DatabaseString.setString(preparedStatement, 2, DataTypeCodename.LONG.toString(), SIZE_CODENAME_COLUMN);
		DatabaseString.setString(preparedStatement, 3, DESCRIPTION_LONG, SIZE_DESCRIPTION_COLUMN);
		Log.debugMessage("Creating " + DESCRIPTION_LONG, Log.DEBUGLEVEL08);
		preparedStatement.executeUpdate();

		//-6. Raw
		preparedStatement.setInt(1, DataType._DATA_TYPE_RAW);
		DatabaseString.setString(preparedStatement, 2, DataTypeCodename.RAW.toString(), SIZE_CODENAME_COLUMN);
		DatabaseString.setString(preparedStatement, 3, DESCRIPTION_RAW, SIZE_DESCRIPTION_COLUMN);
		Log.debugMessage("Creating " + DESCRIPTION_RAW, Log.DEBUGLEVEL08);
		preparedStatement.executeUpdate();

		//-7. Boolean
		preparedStatement.setInt(1, DataType._DATA_TYPE_BOOLEAN);
		DatabaseString.setString(preparedStatement, 2, DataTypeCodename.BOOLEAN.toString(), SIZE_CODENAME_COLUMN);
		DatabaseString.setString(preparedStatement, 3, DESCRIPTION_BOOLEAN, SIZE_DESCRIPTION_COLUMN);
		Log.debugMessage("Creating " + DESCRIPTION_BOOLEAN, Log.DEBUGLEVEL08);
		preparedStatement.executeUpdate();
	}
}
