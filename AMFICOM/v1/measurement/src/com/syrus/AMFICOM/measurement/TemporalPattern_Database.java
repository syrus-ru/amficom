package com.syrus.AMFICOM.measurement;

import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import oracle.jdbc.driver.OracleResultSet;
import oracle.jdbc.driver.OraclePreparedStatement;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseDate;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObject_Database;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.measurement.ora.CronStringArray;

public class TemporalPattern_Database extends StorableObject_Database {
	public static final String COLUMN_DESCRIPTION = "description";
	public static final String COLUMN_VALUE = "value";

	private TemporalPattern fromStorableObject(StorableObject storableObject) throws Exception {
		if (storableObject instanceof TemporalPattern)
			return (TemporalPattern)storableObject;
		else
			throw new Exception("TemporalPattern_Database.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	public void retrieve(StorableObject storableObject) throws Exception {
		TemporalPattern temporalPattern = this.fromStorableObject(storableObject);
		this.retrieveTemporalPattern(temporalPattern);
	}

	private void retrieveTemporalPattern(TemporalPattern temporalPattern) throws Exception {
		String tp_id_str = temporalPattern.getId().toString();
		String sql = "SELECT "
			+ DatabaseDate.toQuerySubString(COLUMN_CREATED) + ", " 
			+ DatabaseDate.toQuerySubString(COLUMN_MODIFIED) + ", "
			+ COLUMN_CREATOR_ID + ", "
			+ COLUMN_MODIFIER_ID + ", "
			+ COLUMN_DESCRIPTION + ", "
			+ COLUMN_VALUE
			+ " FROM " + ObjectEntities.TEMPORALPATTERN_ENTITY
			+ " WHERE " + COLUMN_ID + " = " + tp_id_str;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("TemporalPattern_Database.retrieveTemporalPattern | Trying: " + sql, Log.DEBUGLEVEL05);
			resultSet = statement.executeQuery(sql);
			if (resultSet.next()) {
				String[] cronStrings = ((CronStringArray)(((OracleResultSet)resultSet).getORAData(COLUMN_VALUE, CronStringArray.getORADataFactory()))).getArray();
				temporalPattern.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
																			DatabaseDate.fromQuerySubString(resultSet, COLUMN_MODIFIED),
																			new Identifier(resultSet.getString(COLUMN_CREATOR_ID)),
																			new Identifier(resultSet.getString(COLUMN_MODIFIER_ID)),
																			resultSet.getString(COLUMN_DESCRIPTION),
																			cronStrings);
			}
			else
				throw new Exception("No such temporal pattern: " + tp_id_str);
		}
		catch (SQLException sqle) {
			String mesg = "TemporalPattern_Database.retrieveTemporalPattern | Cannot retrieve temporal pattern " + tp_id_str;
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

	public Object retrieveObject(StorableObject storableObject, int retrieve_kind, Object arg) throws Exception {
		TemporalPattern temporalPattern = this.fromStorableObject(storableObject);
		switch (retrieve_kind) {
			default:
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws Exception {
		TemporalPattern temporalPattern = this.fromStorableObject(storableObject);
		try {
			this.insertTemporalPattern(test);
		}
		catch (Exception e) {
			try {
				connection.rollback();
			}
			catch (SQLException sqle) {
				Log.errorMessage("Exception in rolling back");
				Log.errorException(sqle);
			}
			throw e;
		}
		try {
			connection.commit();
		}
		catch (SQLException sqle) {
			Log.errorMessage("Exception in commiting");
			Log.errorException(sqle);
		}
	}

	private void insertTemporalPattern(TemporalPattern temporalPattern) throws Exception {
		String tp_id_str = temporalPattern.getId().toString();
		String sql = "INSERT INTO " + ObjectEntities.TEMPORALPATTERN_ENTITY + " ("
			+ COLUMN_ID + COMMA
			+ COLUMN_CREATED + COMMA
			+ COLUMN_MODIFIED + COMMA
			+ COLUMN_CREATOR_ID + COMMA
			+ COLUMN_MODIFIER_ID + COMMA
			+ COLUMN_DESCRIPTION + COMMA
			+ COLUMN_VALUE
			+ ") VALUES "
			+ "(?, ?, ?, ?, ?, ?, ?)";
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, tp_id_str);
			preparedStatement.setDate(2, new java.sql.Date(temporalPattern.getCreated().getTime()));
			preparedStatement.setDate(3, new java.sql.Date(temporalPattern.getModified().getTime()));
			preparedStatement.setString(4, tp_id_str);
		}
		catch (SQLException sqle) {
			String mesg = "TemporalPattern_Database.insertTest | Cannot insert temporal pattern " + tp_id_str;
			throw new Exception(mesg, sqle);
		}
		finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
				preparedStatement = null;
			}
			catch (SQLException sqle1) {}
		}
	}
}
