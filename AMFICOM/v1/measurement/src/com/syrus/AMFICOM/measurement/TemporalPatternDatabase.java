/*
 * $Id: TemporalPatternDatabase.java,v 1.9 2004/08/17 09:04:29 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import oracle.jdbc.driver.OracleResultSet;
import oracle.jdbc.driver.OraclePreparedStatement;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseDate;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.measurement.ora.CronStringArray;

/**
 * @version $Revision: 1.9 $, $Date: 2004/08/17 09:04:29 $
 * @author $Author: bob $
 * @module measurement_v1
 */

public class TemporalPatternDatabase extends StorableObjectDatabase {
	public static final String COLUMN_DESCRIPTION 	= "description";
	public static final String COLUMN_VALUE 		= "value";
		
	public static final int CHARACTER_NUMBER_OF_RECORDS = 1;

	private TemporalPattern fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof TemporalPattern)
			return (TemporalPattern)storableObject;
		throw new IllegalDataException("TemporalPatternDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		TemporalPattern temporalPattern = this.fromStorableObject(storableObject);
		this.retrieveTemporalPattern(temporalPattern);
	}

	private void retrieveTemporalPattern(TemporalPattern temporalPattern) throws ObjectNotFoundException, RetrieveObjectException {
		String tpIdStr = temporalPattern.getId().toSQLString();
		String sql = SQL_SELECT
			+ DatabaseDate.toQuerySubString(COLUMN_CREATED) + COMMA 
			+ DatabaseDate.toQuerySubString(COLUMN_MODIFIED) + COMMA
			+ COLUMN_CREATOR_ID + COMMA
			+ COLUMN_MODIFIER_ID + COMMA
			+ COLUMN_DESCRIPTION + COMMA
			+ COLUMN_VALUE
			+ SQL_FROM + ObjectEntities.TEMPORALPATTERN_ENTITY
			+ SQL_WHERE + COLUMN_ID + EQUALS + tpIdStr;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("TemporalPatternDatabase.retrieveTemporalPattern | Trying: " + sql, Log.DEBUGLEVEL05);
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
				throw new ObjectNotFoundException("No such temporal pattern: " + tpIdStr);
		}
		catch (SQLException sqle) {
			String mesg = "TemporalPatternDatabase.retrieveTemporalPattern | Cannot retrieve temporal pattern " + tpIdStr;
			throw new RetrieveObjectException(mesg, sqle);
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
			catch (SQLException sqle1) {
				Log.errorException(sqle1);
			}
		}
	}

	public Object retrieveObject(StorableObject storableObject, int retrieveKind, Object arg) throws ObjectNotFoundException, RetrieveObjectException, IllegalDataException {
		TemporalPattern temporalPattern = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws CreateObjectException , IllegalDataException {
		TemporalPattern temporalPattern = this.fromStorableObject(storableObject);
		try {
			this.insertTemporalPattern(temporalPattern);
		}
		catch (CreateObjectException e) {
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

	private void insertTemporalPattern(TemporalPattern temporalPattern) throws CreateObjectException {
		String tpIdCode = temporalPattern.getId().getCode();
		String sql = SQL_INSERT_INTO + ObjectEntities.TEMPORALPATTERN_ENTITY 
			+ OPEN_BRACKET
			+ COLUMN_ID + COMMA
			+ COLUMN_CREATED + COMMA
			+ COLUMN_MODIFIED + COMMA
			+ COLUMN_CREATOR_ID + COMMA
			+ COLUMN_MODIFIER_ID + COMMA
			+ COLUMN_DESCRIPTION + COMMA
			+ COLUMN_VALUE
			+ CLOSE_BRACKET
			+ SQL_VALUES
			+ OPEN_BRACKET
			+ QUESTION + COMMA
			+ QUESTION + COMMA
			+ QUESTION + COMMA
			+ QUESTION + COMMA
			+ QUESTION + COMMA
			+ QUESTION + COMMA
			+ QUESTION + CLOSE_BRACKET;
		PreparedStatement preparedStatement = null;
		try {			
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, tpIdCode);
			preparedStatement.setTimestamp(2, new Timestamp(temporalPattern.getCreated().getTime()));
			preparedStatement.setTimestamp(3, new Timestamp(temporalPattern.getModified().getTime()));
			preparedStatement.setString(4, temporalPattern.getCreatorId().getCode());
			preparedStatement.setString(5, temporalPattern.getModifierId().getCode());
			preparedStatement.setString(6, temporalPattern.getDescription());
			((OraclePreparedStatement)preparedStatement).setORAData(7, new CronStringArray(temporalPattern.getCronStrings()));
			Log.debugMessage("TemporalPatternDatabase.insertTemporalPattern | Inserting temporal pattern " + tpIdCode, Log.DEBUGLEVEL05);
			preparedStatement.executeUpdate();
		}
		catch (SQLException sqle) {
			String mesg = "TemporalPatternDatabase.insertTemporalPattern | Cannot insert temporal pattern " + tpIdCode;
			throw new CreateObjectException(mesg, sqle);
		}
		finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
				preparedStatement = null;
			}
			catch (SQLException sqle1) {
				Log.errorException(sqle1);
			}
		}
	}

	public void update(StorableObject storableObject, int updateKind, Object arg) throws IllegalDataException, UpdateObjectException {
		TemporalPattern temporalPattern = this.fromStorableObject(storableObject);
		switch (updateKind) {
			default:
				break;
		}
	}
	
	public static List retrieveAll() throws RetrieveObjectException {
		List temporalPatterns = new ArrayList(CHARACTER_NUMBER_OF_RECORDS);
		String sql = SQL_SELECT
				+ COLUMN_ID
				+ SQL_FROM + ObjectEntities.TEMPORALPATTERN_ENTITY;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("TemporalPatternDatabase.retrieveAll | Trying: " + sql, Log.DEBUGLEVEL05);
			resultSet = statement.executeQuery(sql);
			while (resultSet.next())
				temporalPatterns.add(new TemporalPattern(new Identifier(resultSet.getString(COLUMN_ID))));			
		}
		catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
		}
		catch (SQLException sqle) {
			String mesg = "TemporalPatternDatabase.retrieveAll | Cannot retrieve temporal pattern ";
			throw new RetrieveObjectException(mesg, sqle);
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
			catch (SQLException sqle1) {
				Log.errorException(sqle1);
			}
		}
		return temporalPatterns;
	}

	public static void delete(TemporalPattern temporalPattern) {
		String tpIdStr = temporalPattern.getId().toSQLString();
		Statement statement = null;
		try {
			statement = connection.createStatement();
			String sql = SQL_DELETE_FROM
						+ ObjectEntities.TEMPORALPATTERN_ENTITY
						+ SQL_WHERE
						+ COLUMN_ID + EQUALS
						+ tpIdStr;
			Log.debugMessage("TemporalPatternDatabase.delete | Trying: " + sql, Log.DEBUGLEVEL05);
			statement.executeUpdate(sql);
			connection.commit();
		}
		catch (SQLException sqle1) {
			Log.errorException(sqle1);
		}
		finally {
			try {
				if(statement != null)
					statement.close();
				statement = null;
			}
			catch(SQLException sqle1) {
				Log.errorException(sqle1);
			}
		}
	}

}
