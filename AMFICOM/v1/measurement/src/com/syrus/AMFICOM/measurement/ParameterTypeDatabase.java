/*
 * $Id: ParameterTypeDatabase.java,v 1.10 2004/07/27 15:52:26 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import java.util.List;
import java.util.ArrayList;
import java.sql.Statement;
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

public class ParameterTypeDatabase extends StorableObjectDatabase  {
	
	public static final String COLUMN_CODENAME = "codename";
	public static final String COLUMN_DESCRIPTION = "description";	
	public static final String COLUMN_NAME = "name";	
	
	public static final int CHARACTER_NUMBER_OF_RECORDS = 15;

	private ParameterType fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof ParameterType)
			return (ParameterType)storableObject;
		throw new IllegalDataException("ParameterTypeDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		ParameterType parameterType = this.fromStorableObject(storableObject);
		this.retrieveParameterType(parameterType);
	}

	private void retrieveParameterType(ParameterType parameterType) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		String parameterTypeIdStr = parameterType.getId().toSQLString();
		String sql = SQL_SELECT
			+ DatabaseDate.toQuerySubString(COLUMN_CREATED) + COMMA 
			+ DatabaseDate.toQuerySubString(COLUMN_MODIFIED) + COMMA
			+ COLUMN_CREATOR_ID + COMMA
			+ COLUMN_MODIFIER_ID + COMMA
			+ COLUMN_CODENAME + COMMA
			+ COLUMN_DESCRIPTION + COMMA
			+ COLUMN_NAME
			+ SQL_FROM + ObjectEntities.PARAMETERTYPE_ENTITY
			+ SQL_WHERE + COLUMN_ID + EQUALS + parameterTypeIdStr;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("ParameterTypeDatabase.retrieve | Trying: " + sql, Log.DEBUGLEVEL05);
			resultSet = statement.executeQuery(sql);
			if (resultSet.next()){
				parameterType.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
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
																		resultSet.getString(COLUMN_DESCRIPTION),
																		resultSet.getString(COLUMN_NAME));
			}
			else
				throw new ObjectNotFoundException("No such parameter type: " + parameterTypeIdStr);
		}
		catch (SQLException sqle) {
			String mesg = "ParameterTypeDatabase.retrieve | Cannot retrieve parameter type " + parameterTypeIdStr;
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

	public Object retrieveObject(StorableObject storableObject, int retrieveKind, Object arg) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		ParameterType parameterType = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws IllegalDataException, CreateObjectException {
		ParameterType parameterType = this.fromStorableObject(storableObject);
		try {
			this.insertParameterType(parameterType);
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

	private void insertParameterType(ParameterType parameterType) throws IllegalDataException, CreateObjectException {
		String parameterTypeIdStr = parameterType.getId().toSQLString();
		String sql = SQL_INSERT_INTO
			+ ObjectEntities.PARAMETERTYPE_ENTITY + OPEN_BRACKET
			+ COLUMN_ID + COMMA
			+ COLUMN_CREATED + COMMA
			+ COLUMN_MODIFIED + COMMA
			+ COLUMN_CREATOR_ID + COMMA
			+ COLUMN_MODIFIER_ID + COMMA
			+ COLUMN_CODENAME + COMMA
			+ COLUMN_DESCRIPTION + COMMA
			+ COLUMN_NAME
			+ CLOSE_BRACKET + SQL_VALUES + OPEN_BRACKET
			+ parameterTypeIdStr + COMMA
			+ DatabaseDate.toUpdateSubString(parameterType.getCreated()) + COMMA
			+ DatabaseDate.toUpdateSubString(parameterType.getModified()) + COMMA
			+ parameterType.getCreatorId().toString() + COMMA
			+ parameterType.getModifierId().toString() + COMMA
			+ APOSTOPHE + parameterType.getCodename() + APOSTOPHE + COMMA
			+ APOSTOPHE + parameterType.getDescription() + APOSTOPHE + COMMA
			+ APOSTOPHE + parameterType.getName() + APOSTOPHE
			+ CLOSE_BRACKET;
		Statement statement = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("ParameterTypeDatabase.insert | Trying: " + sql, Log.DEBUGLEVEL05);
			statement.executeUpdate(sql);
		}
		catch (SQLException sqle) {
			String mesg = "ParameterTypeDatabase.insert | Cannot insert parameter type " + parameterTypeIdStr;
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
		ParameterType parameterType = this.fromStorableObject(storableObject);
		switch (updateKind) {
			default:
				return;
		}
	}

	public static ParameterType retrieveForCodename(String codename) throws ObjectNotFoundException , RetrieveObjectException {
		String sql = SQL_SELECT
			+ COLUMN_ID
			+ SQL_FROM + ObjectEntities.PARAMETERTYPE_ENTITY
			+ SQL_WHERE + COLUMN_CODENAME + EQUALS + APOSTOPHE + codename + APOSTOPHE;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("ParameterTypeDatabase.retrieveForCodename | Trying: " + sql, Log.DEBUGLEVEL05);
			resultSet = statement.executeQuery(sql);
			if (resultSet.next()){
				/**
				 * @todo when change DB Identifier model ,change getString() to getLong()
				 */
				return new ParameterType(new Identifier(resultSet.getString(COLUMN_ID)));
			}
			throw new ObjectNotFoundException("No such parameter type with codename: '" + codename + "'");
		}
		catch (SQLException sqle) {
			String mesg = "ParameterTypeDatabase.retrieveForCodename | Cannot retrieve parameter type with codename: '" + codename + "'";
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
		List parameterTypes = new ArrayList(CHARACTER_NUMBER_OF_RECORDS);
		String sql = SQL_SELECT
			+ COLUMN_ID
			+ SQL_FROM + ObjectEntities.PARAMETERTYPE_ENTITY;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("ParameterTypeDatabase.retrieveAll | Trying: " + sql, Log.DEBUGLEVEL05);
			resultSet = statement.executeQuery(sql);
			while (resultSet.next()){
				/**
				 * @todo when change DB Identifier model ,change getString() to getLong()
				 */
				parameterTypes.add(new ParameterType(new Identifier(resultSet.getString(COLUMN_ID))));
			}
		}
		catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
		}
		catch (SQLException sqle) {
			String mesg = "ParameterTypeDatabase.retrieveAll | Cannot retrieve parameter type";
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
		return parameterTypes;
	}
}
