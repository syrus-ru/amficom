/*
 * $Id: MeasurementTypeDatabase.java,v 1.35 2004/11/02 10:29:59 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.configuration.MeasurementPortType;
import com.syrus.AMFICOM.general.Identified;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.35 $, $Date: 2004/11/02 10:29:59 $
 * @author $Author: bob $
 * @module measurement_v1
 */

public class MeasurementTypeDatabase extends StorableObjectDatabase  {
	public static final String MODE_IN = "IN";
	public static final String MODE_OUT = "OUT";
	
	public static final String	COLUMN_CODENAME = "codename";
	public static final String	COLUMN_DESCRIPTION = "description";
	
	public static final String	LINK_COLUMN_MEASUREMENT_TYPE_ID = "measurement_type_id";
	public static final String	LINK_COLUMN_MEASUREMENT_PORT_TYPE_ID = "measurement_port_type_id";
	
	
	public static final int CHARACTER_NUMBER_OF_RECORDS = 1;
	
	private String updateColumns;
	private String updateMultiplySQLValues;

	private MeasurementType fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof MeasurementType)
			return (MeasurementType)storableObject;
		throw new IllegalDataException("MeasurementTypeDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}	
	
	protected String getEnityName() {
		return "MeasurementType";
	}
	
	
	protected String getTableName() {
		return ObjectEntities.MEASUREMENTTYPE_ENTITY;
	}
	
	
	protected String getUpdateColumns() {
		if (this.updateColumns == null){
			this.updateColumns = super.getUpdateColumns() + COMMA
				+ COLUMN_CODENAME + COMMA 
				+ COLUMN_DESCRIPTION;
		}
		return this.updateColumns;
	}	

	protected String getUpdateMultiplySQLValues() {
		if (this.updateMultiplySQLValues == null){
			this.updateMultiplySQLValues = super.getUpdateMultiplySQLValues() + COMMA
				+ QUESTION + COMMA 
				+ QUESTION;
		}
		return this.updateMultiplySQLValues;
	}	
	
	
	protected String getUpdateSingleSQLValues(StorableObject storableObject) throws IllegalDataException,
			UpdateObjectException {
		MeasurementType measurementType = fromStorableObject(storableObject);
		String sql = super.getUpdateSingleSQLValues(storableObject) + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(measurementType.getCodename()) + APOSTOPHE + COMMA 
			+ APOSTOPHE + DatabaseString.toQuerySubString(measurementType.getDescription()) + APOSTOPHE;
		return sql;
	}	

	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		MeasurementType measurementType = this.fromStorableObject(storableObject);
		this.retrieveEntity(measurementType);
		this.retrieveParameterTypes(measurementType);
		this.retrieveMeasurementPortTypes(measurementType);
	}

	protected String retrieveQuery(String condition){
		return super.retrieveQuery(condition) + COMMA
			+ COLUMN_CODENAME + COMMA
			+ COLUMN_DESCRIPTION
			+ SQL_FROM + ObjectEntities.MEASUREMENTTYPE_ENTITY
			+ ( ((condition == null) || (condition.length() == 0) ) ? "" : SQL_WHERE + condition);

	}
	
	
	protected StorableObject updateEntityFromResultSet(StorableObject storableObject, ResultSet resultSet)
		throws IllegalDataException, RetrieveObjectException, SQLException {
		MeasurementType measurementType = (storableObject == null) ? 
				new MeasurementType(new Identifier(resultSet.getString(COLUMN_ID)), null, null, null, 
									   null, null, null) : 
					fromStorableObject(storableObject);
		/**
		 * @todo when change DB Identifier model ,change getString() to getLong()
		 */
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
												 DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_CODENAME)),
												 DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_DESCRIPTION)));
		return measurementType;
	}

	private void retrieveParameterTypes(MeasurementType measurementType) throws RetrieveObjectException {
		List inParTyps = new ArrayList();
		List outParTyps = new ArrayList();

		String measurementTypeIdStr = measurementType.getId().toSQLString();
		String sql = SQL_SELECT
			+ LINK_COLUMN_PARAMETER_TYPE_ID + COMMA
			+ LINK_COLUMN_PARAMETER_MODE
			+ SQL_FROM + ObjectEntities.MNTTYPPARTYPLINK_ENTITY
			+ SQL_WHERE + LINK_COLUMN_MEASUREMENT_TYPE_ID + EQUALS + measurementTypeIdStr;
		Statement statement = null;
		ResultSet resultSet = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage("MeasurementTypeDatabase.retrieveParameterType | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);
			String parameterMode;
			/**
			 * @todo when change DB Identifier model ,change String to long
			 */
			String parameterTypeIdCode;
			while (resultSet.next()) {
				parameterMode = resultSet.getString(LINK_COLUMN_PARAMETER_MODE);
				/**
				 * @todo when change DB Identifier model ,change getString() to getLong()
				 */
				parameterTypeIdCode = resultSet.getString(LINK_COLUMN_PARAMETER_TYPE_ID);
				if (parameterMode.equals(MODE_IN))
					inParTyps.add((ParameterType) MeasurementStorableObjectPool.getStorableObject(new Identifier(parameterTypeIdCode), true));
				else
					if (parameterMode.equals(MODE_OUT))
						outParTyps.add((ParameterType) MeasurementStorableObjectPool.getStorableObject(new Identifier(parameterTypeIdCode), true));
					else
						Log.errorMessage("MeasurementTypeDatabase.retrieveParameterTypes | ERROR: Unknown parameter mode for parameterTypeId " + parameterTypeIdCode);
			}
		}
		catch (SQLException sqle) {
			String mesg = "MeasurementTypeDatabase.retrieveParameterTypes | Cannot retrieve parameter type ids for measurement type '" + measurementTypeIdStr + "' -- " + sqle.getMessage();
			throw new RetrieveObjectException(mesg, sqle);
		}
		catch (ApplicationException ae) {
			throw new RetrieveObjectException(ae);
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
			} finally{
				DatabaseConnection.closeConnection(connection);
			}
		}
		((ArrayList)inParTyps).trimToSize();
		((ArrayList)outParTyps).trimToSize();
		measurementType.setParameterTypes(inParTyps,
																			outParTyps);
	}
	
	private void retrieveMeasurementPortTypes(MeasurementType measurementType) throws RetrieveObjectException {
		List measurementPortTypes = new ArrayList();

		String measurementTypeIdStr = measurementType.getId().toSQLString();
		String sql = SQL_SELECT
			+ LINK_COLUMN_MEASUREMENT_PORT_TYPE_ID
			+ SQL_FROM + ObjectEntities.MNTTYMEASPORTTYPELINK_ENTITY
			+ SQL_WHERE + LINK_COLUMN_MEASUREMENT_TYPE_ID + EQUALS + measurementTypeIdStr;
		Statement statement = null;
		ResultSet resultSet = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage("MeasurementTypeDatabase.retrieveMeasurementPortTypes | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);
			String measurementPortTypeIdCode;
			while (resultSet.next()) {
				/**
				 * @todo when change DB Identifier model ,change getString() to getLong()
				 */
				measurementPortTypeIdCode = resultSet.getString(LINK_COLUMN_MEASUREMENT_PORT_TYPE_ID);
				measurementPortTypes.add((MeasurementPortType) ConfigurationStorableObjectPool.getStorableObject(new Identifier(measurementPortTypeIdCode), true));
			}
		}
		catch (SQLException sqle) {
			String mesg = "MeasurementTypeDatabase.retrieveMeasurementPortTypes | Cannot retrieve measurement port type ids for measurement type '" + measurementTypeIdStr + "' -- " + sqle.getMessage();
			throw new RetrieveObjectException(mesg, sqle);
		}
		catch (ApplicationException ae) {
			throw new RetrieveObjectException(ae);
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
			} finally{
				DatabaseConnection.closeConnection(connection);
			}
		}
		((ArrayList)measurementPortTypes).trimToSize();
		measurementType.setMeasurementPortTypes(measurementPortTypes);
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
		this.insertEntity(measurementType);
		this.insertParameterTypes(measurementType);
		this.insertMeasurementPortTypes(measurementType);
		
	}
	
	public void insert(List storableObjects) throws IllegalDataException, CreateObjectException {
		insertEntities(storableObjects);
		for(Iterator it=storableObjects.iterator();it.hasNext();){
			MeasurementType measurementType = fromStorableObject((StorableObject)it.next());
			this.insertParameterTypes(measurementType);
			this.insertMeasurementPortTypes(measurementType);
		}

	}

	private PreparedStatement insertParameterTypesPreparedStatement() throws SQLException{
		PreparedStatement preparedStatement = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
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
		preparedStatement = connection.prepareStatement(sql);
		} finally{
			DatabaseConnection.closeConnection(connection);
		}
		return preparedStatement;
	}
	
	private PreparedStatement insertMeasurementPortTypePreparedStatement() throws SQLException{
		PreparedStatement preparedStatement = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
		String sql = SQL_INSERT_INTO
		+ ObjectEntities.MNTTYMEASPORTTYPELINK_ENTITY + OPEN_BRACKET
		+ LINK_COLUMN_MEASUREMENT_TYPE_ID + COMMA
		+ LINK_COLUMN_MEASUREMENT_PORT_TYPE_ID
		+ CLOSE_BRACKET + SQL_VALUES + OPEN_BRACKET
		+ QUESTION + COMMA
		+ QUESTION
		+ CLOSE_BRACKET;
		preparedStatement = connection.prepareStatement(sql);
		} finally{
			DatabaseConnection.closeConnection(connection);
		}
		return preparedStatement;
	}
	
	private void updatePrepareStatementValues(PreparedStatement preparedStatement,MeasurementType measurementType) throws SQLException{
		List inParTyps = measurementType.getInParameterTypes();
		List outParTyps = measurementType.getOutParameterTypes();
		String measurementTypeIdCode = measurementType.getId().getCode();
		/**
		 * @todo when change DB Identifier model ,change String to long
		 */
		String parameterTypeIdCode = null;
		String parameterMode = null;
		
		for (Iterator iterator = inParTyps.iterator(); iterator.hasNext();) {
			/**
			 * @todo when change DB Identifier model ,change setString() to
			 *       setLong()
			 */
			preparedStatement.setString(1, measurementTypeIdCode);
			parameterTypeIdCode = ((ParameterType) iterator.next()).getId().getCode();
			/**
			 * @todo when change DB Identifier model ,change setString() to
			 *       setLong()
			 */
			preparedStatement.setString(2, parameterTypeIdCode);
			parameterMode = MODE_IN;
			preparedStatement.setString(3, parameterMode);
			Log.debugMessage("MeasurementTypeDatabase.insertParameterTypes | Inserting parameter type " + parameterTypeIdCode + " of parameter mode '" + parameterMode + "' for measurement type " + measurementTypeIdCode, Log.DEBUGLEVEL09);
			preparedStatement.executeUpdate();
		}
		for (Iterator iterator = outParTyps.iterator(); iterator.hasNext();) {
			/**
			 * @todo when change DB Identifier model ,change setString() to
			 *       setLong()
			 */
			preparedStatement.setString(1, measurementTypeIdCode);
			parameterTypeIdCode = ((ParameterType) iterator.next()).getId().getCode();
			/**
			 * @todo when change DB Identifier model ,change setString() to
			 *       setLong()
			 */
			preparedStatement.setString(2, parameterTypeIdCode);
			parameterMode = MODE_OUT;
			preparedStatement.setString(3, parameterMode);
			Log.debugMessage("MeasurementTypeDatabase.insertParameterTypes | Inserting parameter type '" + parameterTypeIdCode + "' of parameter mode '" + parameterMode + "' for measurement type '" + measurementTypeIdCode + "'", Log.DEBUGLEVEL09);
			preparedStatement.executeUpdate();
		}

	}

	private void updateMeasurementPortTypePrepareStatementValues(PreparedStatement preparedStatement,MeasurementType measurementType) throws SQLException{
		List measurementPortTypes = measurementType.getMeasurementPortTypes();
		String measurementTypeIdCode = measurementType.getId().getCode();
		/**
		 * @todo when change DB Identifier model ,change String to long
		 */
		String measurementPortTypeIdCode = null;
		
		for (Iterator iterator = measurementPortTypes.iterator(); iterator.hasNext();) {
			/**
			 * @todo when change DB Identifier model ,change setString() to
			 *       setLong()
			 */
			preparedStatement.setString(1, measurementTypeIdCode);
			measurementPortTypeIdCode = ((MeasurementPortType) iterator.next()).getId().getCode();
			/**
			 * @todo when change DB Identifier model ,change setString() to
			 *       setLong()
			 */
			preparedStatement.setString(2, measurementPortTypeIdCode);
			Log.debugMessage("MeasurementTypeDatabase.updateMeasurementPortTypePrepareStatementValues | Inserting measurement port type " + measurementPortTypeIdCode + "' for measurement type " + measurementTypeIdCode, Log.DEBUGLEVEL09);
			preparedStatement.executeUpdate();
		}

	}

	private void insertParameterTypes(MeasurementType measurementType) throws CreateObjectException {
		/**
		 * @todo when change DB Identifier model ,change String to long
		 */
		
		PreparedStatement preparedStatement = null;
		String measurementTypeIdCode = measurementType.getId().getCode();
		try {
			preparedStatement = insertParameterTypesPreparedStatement();
			updatePrepareStatementValues(preparedStatement, measurementType);
		}
		catch (SQLException sqle) {
			String mesg = "MeasurementTypeDatabase.insertParameterTypes | Cannot insert parameter type for measurement type '" + measurementTypeIdCode + "' -- " + sqle.getMessage();
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
	
	private void insertMeasurementPortTypes(MeasurementType measurementType) throws CreateObjectException {
		/**
		 * @todo when change DB Identifier model ,change String to long
		 */
		
		PreparedStatement preparedStatement = null;
		String measurementTypeIdCode = measurementType.getId().getCode();
		try {
			preparedStatement = insertMeasurementPortTypePreparedStatement();
			updateMeasurementPortTypePrepareStatementValues(preparedStatement, measurementType);
		}
		catch (SQLException sqle) {
			String mesg = "MeasurementTypeDatabase.insertMeasurementPortTypes | Cannot insert measurement port type for measurement type '" + measurementTypeIdCode + "' -- " + sqle.getMessage();
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

	public void update(StorableObject storableObject, int updateKind, Object obj) throws IllegalDataException, VersionCollisionException, UpdateObjectException {
		MeasurementType measurementType = this.fromStorableObject(storableObject);
		switch (updateKind) {
			case UPDATE_CHECK:
				super.checkAndUpdateEntity(storableObject, false);
				break;
			case UPDATE_FORCE:					
			default:
				super.checkAndUpdateEntity(storableObject, true);		
				return;
		}
	}
	
	public void update(List storableObjects, int updateKind, Object arg) throws IllegalDataException,
		VersionCollisionException, UpdateObjectException {		
		switch (updateKind) {
			case UPDATE_CHECK:
				super.checkAndUpdateEntities(storableObjects, false);
				break;
			case UPDATE_FORCE:					
			default:
				super.checkAndUpdateEntities(storableObjects, true);		
				return;
		}
	}

	public void delete(MeasurementType measurementType) {
		String measurementTypeIdStr = measurementType.getId().toSQLString();
		Statement statement = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			statement.executeUpdate(SQL_DELETE_FROM
									+ ObjectEntities.MNTTYMEASPORTTYPELINK_ENTITY
									+ SQL_WHERE + LINK_COLUMN_MEASUREMENT_TYPE_ID + EQUALS + measurementTypeIdStr);
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
			} finally{
				DatabaseConnection.closeConnection(connection);
			}
		}
	}

	public MeasurementType retrieveForCodename(String codename) throws ObjectNotFoundException , RetrieveObjectException {
		List list = null;
		
		try {
			list = retrieveByIds( null , COLUMN_CODENAME + EQUALS + APOSTOPHE + DatabaseString.toQuerySubString(codename) + APOSTOPHE);
		}  catch (IllegalDataException ide) {				
			throw new RetrieveObjectException(ide);
		}
		
		if ((list == null) || (list.isEmpty()))
				throw new ObjectNotFoundException("No analysis type with codename: '" + codename + "'");
		
		return (MeasurementType) list.get(0);
	}
	
	public List retrieveAll() throws RetrieveObjectException {
		try{
			return retrieveByIds(null, null);
		}catch(IllegalDataException ide){
			throw new RetrieveObjectException(ide);
		}
	}
	
	public List retrieveByIds(List ids, String condition) throws IllegalDataException, RetrieveObjectException {
		List list = null; 
		if ((ids == null) || (ids.isEmpty()))
			list = retrieveByIdsOneQuery(null, condition);
		else list = retrieveByIdsOneQuery(ids, condition);
		
		for(Iterator it=list.iterator();it.hasNext();){
			MeasurementType measurementType = (MeasurementType)it.next();
			retrieveParameterTypes(measurementType);
			retrieveMeasurementPortTypes(measurementType);
		}
		
		return list;
	}
	
	protected int setEntityForPreparedStatement(StorableObject storableObject, PreparedStatement preparedStatement)
		throws IllegalDataException, UpdateObjectException {
		MeasurementType measurementType = fromStorableObject(storableObject);
		int i = super.setEntityForPreparedStatement(storableObject, preparedStatement);
			try {				
				preparedStatement.setString(++i, measurementType.getCodename());
				preparedStatement.setString(++i, measurementType.getDescription());
			} catch (SQLException sqle) {
				throw new UpdateObjectException(getEnityName() + "Database.setEntityForPreparedStatement | Error " + sqle.getMessage(), sqle);
			}
		return i;
		}
	
	private List retrieveButIdsByMeasurementPortType(List ids, List measurementPortTypes) throws RetrieveObjectException {
		
		List list = null;
		
		if (measurementPortTypes != null && !measurementPortTypes.isEmpty()){
			StringBuffer buffer = new StringBuffer(COLUMN_ID + SQL_IN + OPEN_BRACKET
					+ SQL_SELECT + LINK_COLUMN_MEASUREMENT_TYPE_ID + ObjectEntities.MNTTYMEASPORTTYPELINK_ENTITY
					+ SQL_WHERE + LINK_COLUMN_MEASUREMENT_PORT_TYPE_ID + SQL_IN + OPEN_BRACKET);
			int i = 1;
			for (Iterator it = measurementPortTypes.iterator(); it.hasNext(); i++) {
				Object object = it.next();
				Identifier id = null;
				if (object instanceof Identifier)
					id = (Identifier) object;
				else if (object instanceof Identified)
					id = ((Identified) object).getId();
				else
					throw new RetrieveObjectException(
								getEnityName() + "Database.retrieveButIdsByMeasurementPortType | Object "
								+ object.getClass().getName()
												+ " isn't Identifier or Identified");
	
				if (id != null) {
					buffer.append(id.toSQLString());
					if (it.hasNext()){
						if (((i+1) % MAXIMUM_EXPRESSION_NUMBER != 0))
							buffer.append(COMMA);
						else {
							buffer.append(CLOSE_BRACKET);
							buffer.append(SQL_OR);
							buffer.append(LINK_COLUMN_MEASUREMENT_PORT_TYPE_ID);
							buffer.append(SQL_IN);
							buffer.append(OPEN_BRACKET);
						}					
					}
				}
			}
			buffer.append(CLOSE_BRACKET);
			buffer.append(CLOSE_BRACKET);	
			String condition = buffer.toString();
			try {
				Log.debugMessage(getEnityName() + "Database.retrieveButIdsByMeasurementPortType | Try with additional condition: " + condition, Log.DEBUGLEVEL09);
				list = retrieveButIds(ids, condition);
			}  catch (IllegalDataException ide) {			
				Log.debugMessage(getEnityName() + "Database.retrieveButIdsByMeasurementPortType | Error: " + ide.getMessage(), Log.DEBUGLEVEL09);
			}
		}
		else list = Collections.EMPTY_LIST;
		
		return list;
	}

	public List retrieveByCondition(List ids, StorableObjectCondition condition) throws IllegalDataException, RetrieveObjectException  {
			List list = null;
			if (condition instanceof LinkedIdsCondition){
				LinkedIdsCondition linkedIdsCondition = (LinkedIdsCondition)condition;
				list = this.retrieveButIdsByMeasurementPortType(ids, linkedIdsCondition.getMeasurementPortTypeIds());
			} else{
				Log.errorMessage(getEnityName() + "Database.retrieveByCondition | Unknown condition class: " + condition);
				list = this.retrieveButIds(ids);
			}
			return list;
	}
}
