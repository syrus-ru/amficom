/*
 * $Id: MeasurementTypeDatabase.java,v 1.10 2004/07/27 15:52:26 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseDate;

/**
 * @version $Revision: 1.10 $, $Date: 2004/07/27 15:52:26 $
 * @author $Author: arseniy $
 * @module measurement_v1
 */

public class MeasurementTypeDatabase extends StorableObjectDatabase  {
	public static final String MODE_IN = "IN";
	public static final String MODE_OUT = "OUT";
	
	public static final String	COLUMN_CODENAME = "codename";
	public static final String	COLUMN_DESCRIPTION = "description";
	
	public static final String	LINK_COLUMN_MEASUREMENT_TYPE_ID = "measurement_type_id";
	
	public static final int CHARACTER_NUMBER_OF_RECORDS = 1;

	private MeasurementType fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof MeasurementType)
			return (MeasurementType)storableObject;
		throw new IllegalDataException("MeasurementTypeDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		MeasurementType measurementType = this.fromStorableObject(storableObject);
		this.retrieveMeasurementType(measurementType);
		this.retrieveParameterTypes(measurementType);
	}

	private void retrieveMeasurementType(MeasurementType measurementType) throws ObjectNotFoundException, RetrieveObjectException {
		String measurementTypeIdStr = measurementType.getId().toSQLString();
		String sql = SQL_SELECT
			+ DatabaseDate.toQuerySubString(COLUMN_CREATED) + COMMA 
			+ DatabaseDate.toQuerySubString(COLUMN_MODIFIED) + COMMA
			+ COLUMN_CREATOR_ID + COMMA
			+ COLUMN_MODIFIER_ID + COMMA
			+ COLUMN_CODENAME + COMMA
			+ COLUMN_DESCRIPTION
			+ SQL_FROM + ObjectEntities.MEASUREMENTTYPE_ENTITY
			+ SQL_WHERE + COLUMN_ID + EQUALS + measurementTypeIdStr;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("MeasurementTypeDatabase.retrieveMeasurementType | Trying: " + sql, Log.DEBUGLEVEL05);
			resultSet = statement.executeQuery(sql);
			if (resultSet.next())
				measurementType.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
																			DatabaseDate.fromQuerySubString(resultSet, COLUMN_MODIFIED),
																			/**
																			 * @todo when change DB Identifier model ,change getString() to
																			 *       getLong()
																			 */																			
																			new Identifier(resultSet.getString(COLUMN_CREATOR_ID)),
																			/**
																			 * @todo when change DB Identifier model ,change getString() to
																			 *       getLong()
																			 */
																			new Identifier(resultSet.getString(COLUMN_MODIFIER_ID)),
																			resultSet.getString(COLUMN_CODENAME),
																			resultSet.getString(COLUMN_DESCRIPTION));
			else
				throw new ObjectNotFoundException("No such measurement type: " + measurementTypeIdStr);
		}
		catch (SQLException sqle) {
			String mesg = "MeasurementTypeDatabase.retrieveMeasurementType | Cannot retrieve measurement type " + measurementTypeIdStr;
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

	private void retrieveParameterTypes(MeasurementType measurementType) throws RetrieveObjectException {
		ArrayList inParTyps = new ArrayList();
		ArrayList outParTyps = new ArrayList();

		String measurementTypeIdStr = measurementType.getId().toSQLString();
		String sql = SQL_SELECT
			+ LINK_COLUMN_PARAMETER_TYPE_ID + COMMA
			+ LINK_COLUMN_PARAMETER_MODE
			+ SQL_FROM + ObjectEntities.MNTTYPPARTYPLINK_ENTITY
			+ SQL_WHERE + LINK_COLUMN_MEASUREMENT_TYPE_ID + EQUALS + measurementTypeIdStr;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("MeasurementTypeDatabase.retrieveParameterTypes | Trying: " + sql, Log.DEBUGLEVEL05);
			resultSet = statement.executeQuery(sql);
			String parameterMode;
			/**
			 * @todo when change DB Identifier model ,change String to long
			 */
			String parameterTypeIdCode;
			while (resultSet.next()) {
				parameterMode = resultSet.getString(LINK_COLUMN_PARAMETER_MODE);
				/**
				 * @todo when change DB Identifier model ,change getString() to
				 *       getLong()
				 */
				parameterTypeIdCode = resultSet.getString(LINK_COLUMN_PARAMETER_TYPE_ID);
				if (parameterMode.equals(MODE_IN))
					inParTyps.add(new Identifier(parameterTypeIdCode));
				else
					if (parameterMode.equals(MODE_OUT))
						outParTyps.add(new Identifier(parameterTypeIdCode));
					else
						Log.errorMessage("MeasurementTypeDatabase.retrieveParameterTypes | ERROR: Unknown parameter mode for parameterTypeId " + parameterTypeIdCode);
			}
		}
		catch (SQLException sqle) {
			String mesg = "MeasurementTypeDatabase.retrieveParameterTypes | Cannot retrieve parameter types for measurement type " + measurementTypeIdStr;
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
		inParTyps.trimToSize();
		outParTyps.trimToSize();
		measurementType.setParameterTypes(inParTyps,
																			outParTyps);
	}

	public Object retrieveObject(StorableObject storableObject, int retrieveKind, Object arg) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		MeasurementType measurementType = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws IllegalDataException, CreateObjectException {
		MeasurementType measurementType = this.fromStorableObject(storableObject);
		try {
			this.insertMeasurementType(measurementType);
			this.insertParameterTypes(measurementType);
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

	private void insertMeasurementType(MeasurementType measurementType) throws CreateObjectException {
		String measurementTypeIdStr = measurementType.getId().toSQLString();
		String sql = SQL_INSERT_INTO
			+ ObjectEntities.MEASUREMENTTYPE_ENTITY + OPEN_BRACKET
			+ COLUMN_ID + COMMA
			+ COLUMN_CREATED + COMMA
			+ COLUMN_MODIFIED + COMMA
			+ COLUMN_CREATOR_ID + COMMA
			+ COLUMN_MODIFIER_ID + COMMA
			+ COLUMN_CODENAME + COMMA
			+ COLUMN_DESCRIPTION
			+ CLOSE_BRACKET + SQL_VALUES + OPEN_BRACKET
			+ measurementTypeIdStr + COMMA
			+ DatabaseDate.toUpdateSubString(measurementType.getCreated()) + COMMA
			+ DatabaseDate.toUpdateSubString(measurementType.getModified()) + COMMA
			+ measurementType.getCreatorId().toString() + COMMA
			+ measurementType.getModifierId().toString() + COMMA
			+ APOSTOPHE + measurementType.getCodename() + APOSTOPHE + COMMA
			+ APOSTOPHE + measurementType.getDescription() + APOSTOPHE
			+ CLOSE_BRACKET;
		Statement statement = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("MeasurementTypeDatabase.insertMeasurementType | Trying: " + sql, Log.DEBUGLEVEL05);
			statement.executeUpdate(sql);
		}
		catch (SQLException sqle) {
			String mesg = "MeasurementTypeDatabase.insertMeasurementType | Cannot insert measurement type " + measurementTypeIdStr;
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

	private void insertParameterTypes(MeasurementType measurementType) throws CreateObjectException {
		List inParTyps = measurementType.getInParameterTypes();
		List outParTyps = measurementType.getOutParameterTypes();
		/**
		 * @todo when change DB Identifier model ,change String to long
		 */
		String measurementTypeIdCode = measurementType.getId().getCode();
		String sql = SQL_INSERT_INTO
			+ ObjectEntities.MNTTYPPARTYPLINK_ENTITY + OPEN_BRACKET
			+ LINK_COLUMN_MEASUREMENT_TYPE_ID + COMMA
			+ LINK_COLUMN_PARAMETER_TYPE_ID + COMMA
			+ LINK_COLUMN_PARAMETER_MODE
			+ CLOSE_BRACKET + SQL_VALUES + OPEN_BRACKET
			+ QUESTION + COMMA
			+ QUESTION + COMMA
			+ QUESTION
			+ CLOSE_BRACKET;
		PreparedStatement preparedStatement = null;
		/**
		 * @todo when change DB Identifier model ,change String to long
		 */
		String parameterTypeIdCode = null;
		String parameterMode = null;
		try {
			preparedStatement = connection.prepareStatement(sql);
			for (Iterator iterator = inParTyps.iterator(); iterator.hasNext();) {
				/**
				 * @todo when change DB Identifier model ,change setString() to
				 *       setLong()
				 */
				preparedStatement.setString(1, measurementTypeIdCode);
				parameterTypeIdCode = ((Identifier)iterator.next()).getCode();
				/**
				 * @todo when change DB Identifier model ,change setString() to
				 *       setLong()
				 */
				preparedStatement.setString(2, parameterTypeIdCode);
				parameterMode = MODE_IN;
				preparedStatement.setString(3, parameterMode);
				Log.debugMessage("MeasurementTypeDatabase.insertParameterTypes | Inserting parameter type " + parameterTypeIdCode + " of parameter mode '" + parameterMode + "' for measurement type " + measurementTypeIdCode, Log.DEBUGLEVEL05);
				preparedStatement.executeUpdate();
			}
			for (Iterator iterator = outParTyps.iterator(); iterator.hasNext();) {
				/**
				 * @todo when change DB Identifier model ,change setString() to
				 *       setLong()
				 */
				preparedStatement.setString(1, measurementTypeIdCode);
				parameterTypeIdCode = ((Identifier)iterator.next()).getCode();
				/**
				 * @todo when change DB Identifier model ,change setString() to
				 *       setLong()
				 */
				preparedStatement.setString(2, parameterTypeIdCode);
				parameterMode = MODE_OUT;
				preparedStatement.setString(3, parameterMode);
				Log.debugMessage("MeasurementTypeDatabase.insertParameterTypes | Inserting parameter type " + parameterTypeIdCode + " of parameter mode '" + parameterMode + "' for measurement type " + measurementTypeIdCode, Log.DEBUGLEVEL05);
				preparedStatement.executeUpdate();
			}
		}
		catch (SQLException sqle) {
			String mesg = "MeasurementTypeDatabase.insertParameterTypes | Cannot insert parameter type " + parameterTypeIdCode + " of parameter mode '" + parameterMode + "' for measurement type " + measurementTypeIdCode;
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

	public void update(StorableObject storableObject, int updateKind, Object obj) throws IllegalDataException, UpdateObjectException {
		MeasurementType measurementType = this.fromStorableObject(storableObject);
		switch (updateKind) {
			default:
				return;
		}
	}

	public void delete(MeasurementType measurementType) {
		String measurementTypeIdStr = measurementType.getId().toSQLString();
		Statement statement = null;
		try {
			statement = connection.createStatement();
			statement.executeUpdate(SQL_DELETE_FROM
					+ ObjectEntities.MNTTYPPARTYPLINK_ENTITY
					+ SQL_WHERE + LINK_COLUMN_MEASUREMENT_TYPE_ID + EQUALS + measurementTypeIdStr);
			statement.executeUpdate(SQL_DELETE_FROM
					+ ObjectEntities.MEASUREMENTTYPE_ENTITY 
					+ SQL_WHERE + COLUMN_ID + EQUALS + measurementTypeIdStr);
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

	public static MeasurementType retrieveForCodename(String codename) throws ObjectNotFoundException , RetrieveObjectException {
		String sql = SQL_SELECT
			+ COLUMN_ID
			+ SQL_FROM + ObjectEntities.MEASUREMENTTYPE_ENTITY
			+ SQL_WHERE + COLUMN_CODENAME + EQUALS + APOSTOPHE + codename + APOSTOPHE;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("MeasurementTypeDatabase.retrieveForCodename | Trying: " + sql, Log.DEBUGLEVEL05);
			resultSet = statement.executeQuery(sql);
			if (resultSet.next()){
				/**
				 * @todo when change DB Identifier model ,change getString() to
				 *       getLong()
				 */
				return new MeasurementType(new Identifier(resultSet.getString(COLUMN_ID)));
			}
			throw new ObjectNotFoundException("No measurement type with codename: '" + codename + "'");
		}
		catch (SQLException sqle) {
			String mesg = "MeasurementTypeDatabase.retrieveForCodename | Cannot retrieve measurement type with codename: '" + codename + "'";
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
	
	protected static List retrieveAll() throws RetrieveObjectException {
		List measurementTypes = new ArrayList(CHARACTER_NUMBER_OF_RECORDS);
		String sql = SQL_SELECT
				+ COLUMN_ID
				+ SQL_FROM + ObjectEntities.MEASUREMENTTYPE_ENTITY;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("MeasurementTypeDatabase.retrieveAll | Trying: " + sql, Log.DEBUGLEVEL05);
			resultSet = statement.executeQuery(sql);
			while (resultSet.next())
				measurementTypes.add(new MeasurementType(new Identifier(resultSet.getString(COLUMN_ID))));			
		}
		catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
		}
		catch (SQLException sqle) {
			String mesg = "MeasurementTypeDatabase.retrieveAll | Cannot retrieve measurement type";
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
		return measurementTypes;
	}
}
