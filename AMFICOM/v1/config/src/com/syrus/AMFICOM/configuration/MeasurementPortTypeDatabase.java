/*
 * $Id: MeasurementPortTypeDatabase.java,v 1.3 2004/08/22 18:49:19 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.util.List;
import java.util.ArrayList;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseDate;

/**
 * @version $Revision: 1.3 $, $Date: 2004/08/22 18:49:19 $
 * @author $Author: arseniy $
 * @module configuration_v1
 */

public class MeasurementPortTypeDatabase extends StorableObjectDatabase {
	public static final String COLUMN_CODENAME				= "codename";
	public static final String COLUMN_DESCRIPTION			= "description";

	public static final int CHARACTER_NUMBER_OF_RECORDS = 1;
	
	private MeasurementPortType fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof MeasurementPortType)
			return (MeasurementPortType)storableObject;
		throw new IllegalDataException("MeasurementPortTypeDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		MeasurementPortType measurementPortType = this.fromStorableObject(storableObject);
		this.retrieveMeasurementPortType(measurementPortType);
	}

	private void retrieveMeasurementPortType(MeasurementPortType measurementPortType) throws ObjectNotFoundException, RetrieveObjectException {
		String ptIdStr = measurementPortType.getId().toSQLString();
		String sql = SQL_SELECT
			+ DatabaseDate.toQuerySubString(COLUMN_CREATED) + COMMA 
			+ DatabaseDate.toQuerySubString(COLUMN_MODIFIED) + COMMA
			+ COLUMN_CREATOR_ID + COMMA
			+ COLUMN_MODIFIER_ID + COMMA
			+ COLUMN_CODENAME + COMMA
			+ COLUMN_DESCRIPTION
			+ SQL_FROM + ObjectEntities.MEASUREMENTPORTTYPE_ENTITY
			+ SQL_WHERE	+ COLUMN_ID + EQUALS + ptIdStr;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("MeasurementPortTypeDatabase.retrieve | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);
			if (resultSet.next())
				measurementPortType.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
																		DatabaseDate.fromQuerySubString(resultSet, COLUMN_MODIFIED),
																		/**
																			* @todo when change DB Identifier model ,change getString() to getLong()
																			*/
																		new Identifier(resultSet.getString(COLUMN_CREATOR_ID)),
																		/**
																			* @todo when change DB Identifier model ,change getString() to getLong()
																			*/
																		new Identifier(resultSet.getString(COLUMN_MODIFIER_ID)),
																		resultSet.getString(COLUMN_CODENAME),
																		resultSet.getString(COLUMN_DESCRIPTION));
			else
				throw new ObjectNotFoundException("No such measurement port type: " + ptIdStr);
		}
		catch (SQLException sqle) {
			String mesg = "MeasurementPortTypeDatabase.retrieve | Cannot retrieve measurement port type " + ptIdStr;
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

	public Object retrieveObject(StorableObject storableObject, int retrieve_kind, Object arg) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		MeasurementPortType measurementPortType = this.fromStorableObject(storableObject);
		switch (retrieve_kind) {
			default:
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws IllegalDataException, CreateObjectException {
		MeasurementPortType measurementPortType = this.fromStorableObject(storableObject);
		try {
			this.insertMeasurementPortType(measurementPortType);
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

	private void insertMeasurementPortType(MeasurementPortType measurementPortType) throws CreateObjectException {
		String ptIdStr = measurementPortType.getId().toSQLString();
		String sql = SQL_INSERT_INTO
			+ ObjectEntities.MEASUREMENTPORTTYPE_ENTITY
			+ OPEN_BRACKET
			+ COLUMN_ID + COMMA
			+ COLUMN_CREATED + COMMA
			+ COLUMN_MODIFIED + COMMA
			+ COLUMN_CREATOR_ID + COMMA
			+ COLUMN_MODIFIER_ID + COMMA
			+ COLUMN_CODENAME + COMMA
			+ COLUMN_DESCRIPTION
			+ CLOSE_BRACKET
			+ SQL_VALUES
			+ OPEN_BRACKET
			+ ptIdStr + COMMA
			+ DatabaseDate.toUpdateSubString(measurementPortType.getCreated()) + COMMA
			+ DatabaseDate.toUpdateSubString(measurementPortType.getModified()) + COMMA
			+ measurementPortType.getCreatorId().toSQLString() + COMMA
			+ measurementPortType.getModifierId().toSQLString() + COMMA
			+ APOSTOPHE + measurementPortType.getCodename() + APOSTOPHE + COMMA
			+ APOSTOPHE + measurementPortType.getDescription() + APOSTOPHE
			+ CLOSE_BRACKET;
		Statement statement = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("MeasurementPortTypeDatabase.insert | Trying: " + sql, Log.DEBUGLEVEL09);
			statement.executeUpdate(sql);
		}
		catch (SQLException sqle) {
			String mesg = "MeasurementPortTypeDatabase.insert | Cannot insert measurement port type " + ptIdStr;
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
		}
	}

	public void update(StorableObject storableObject, int updateKind, Object obj) throws IllegalDataException, UpdateObjectException {
		MeasurementPortType measurementPortType = this.fromStorableObject(storableObject);
		switch (updateKind) {
			default:
				return;
		}
	}

	public static List retrieveAll() throws RetrieveObjectException {
		List measurementPortTypes = new ArrayList(CHARACTER_NUMBER_OF_RECORDS);
		String sql = SQL_SELECT
				+ COLUMN_ID
				+ SQL_FROM + ObjectEntities.MEASUREMENTPORTTYPE_ENTITY;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("MeasurementPortTypeDatabase.retrieveAll | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);
			while (resultSet.next())
				measurementPortTypes.add(new MeasurementPortType(new Identifier(resultSet.getString(COLUMN_ID))));			
		}
		catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
		}
		catch (SQLException sqle) {
			String mesg = "MeasurementPortTypeDatabase.retrieveAll | Cannot retrieve measurement port type";
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
		return measurementPortTypes;
	}
	
	public static void delete(MeasurementPortType measurementPortType) {
		String mtIdStr = measurementPortType.getId().toSQLString();
		Statement statement = null;
		try {
			statement = connection.createStatement();
			String sql = SQL_DELETE_FROM
						+ ObjectEntities.MEASUREMENTPORTTYPE_ENTITY
						+ SQL_WHERE
						+ COLUMN_ID + EQUALS
						+ mtIdStr;
			Log.debugMessage("MeasurementPortTypeDatabase.delete | Trying: " + sql, Log.DEBUGLEVEL09);
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
