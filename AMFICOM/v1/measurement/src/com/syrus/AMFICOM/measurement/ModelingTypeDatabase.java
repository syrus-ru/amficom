/*
 * $Id: ModelingTypeDatabase.java,v 1.6 2005/01/21 14:39:19 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import com.syrus.AMFICOM.general.GeneralStorableObjectPool;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.ParameterType;
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
 * @version $Revision: 1.6 $, $Date: 2005/01/21 14:39:19 $
 * @author $Author: arseniy $
 * @module measurement_v1
 */

public class ModelingTypeDatabase extends StorableObjectDatabase {
	public static final String MODE_IN = "IN";
	public static final String MODE_OUT = "OUT";

	public static final String COLUMN_CODENAME = "codename";
	public static final String COLUMN_DESCRIPTION = "description";

	public static final String LINK_COLUMN_MODELING_TYPE_ID = "modeling_type_id";

	public static final int CHARACTER_NUMBER_OF_RECORDS = 1;

  public static final String PARAMETER_TYPE_ID = "parameter_type_id";
	public static final String PARAMETER_MODE = "parameter_mode";

	private static String columns;
	private static String updateMultiplySQLValues;

	private ModelingType fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof ModelingType)
			return (ModelingType) storableObject;
		throw new IllegalDataException("ModelingTypeDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	protected String getEnityName() {
		return ObjectEntities.MODELINGTYPE_ENTITY;
	}

	protected String getColumns(int mode) {
		if (columns == null) {
			columns = super.getColumns(mode) + COMMA
				+ COLUMN_CODENAME + COMMA
				+ COLUMN_DESCRIPTION;
		}

		return columns;
	}

	protected String getUpdateMultiplySQLValues(int mode) {
		if (updateMultiplySQLValues == null) {
			updateMultiplySQLValues = super.getUpdateMultiplySQLValues(mode) + COMMA
				+ QUESTION + COMMA
				+ QUESTION;
		}
		return updateMultiplySQLValues;
	}

	protected int setEntityForPreparedStatement(StorableObject storableObject, PreparedStatement preparedStatement, int mode)
			throws IllegalDataException, UpdateObjectException {
		ModelingType modelingType = this.fromStorableObject(storableObject);
		int i = super.setEntityForPreparedStatement(storableObject, preparedStatement, mode);
		try {
			DatabaseString.setString(preparedStatement, ++i, modelingType.getCodename(), SIZE_CODENAME_COLUMN); 
			DatabaseString.setString(preparedStatement, ++i, modelingType.getDescription(), SIZE_DESCRIPTION_COLUMN); 
		}
		catch (SQLException sqle) {
			throw new UpdateObjectException(this.getEnityName() + "Database.setEntityForPreparedStatement | Error " + sqle.getMessage(), sqle);
		}
		return i;
	}

	protected String getUpdateSingleSQLValues(StorableObject storableObject) throws IllegalDataException, UpdateObjectException {
		ModelingType modelingType = this.fromStorableObject(storableObject);
		String values = super.getUpdateSingleSQLValues(storableObject) + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(modelingType.getCodename(), SIZE_CODENAME_COLUMN) + APOSTOPHE + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(modelingType.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTOPHE;		
		return values;
	}

	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		ModelingType modelingType = this.fromStorableObject(storableObject);
		this.retrieveEntity(modelingType);
		this.retrieveParameterTypes(modelingType);
	}

	protected StorableObject updateEntityFromResultSet(StorableObject storableObject, ResultSet resultSet)
		throws IllegalDataException, RetrieveObjectException, SQLException {
		ModelingType modelingType = (storableObject == null) ?
				new ModelingType(DatabaseIdentifier.getIdentifier(resultSet, COLUMN_ID),
												 null,
												 null,
												 null,
												 null,
												 null) : 
					this.fromStorableObject(storableObject);
		modelingType.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
									 DatabaseDate.fromQuerySubString(resultSet, COLUMN_MODIFIED),
									 DatabaseIdentifier.getIdentifier(resultSet, COLUMN_CREATOR_ID),
									 DatabaseIdentifier.getIdentifier(resultSet, COLUMN_MODIFIER_ID),
									 DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_CODENAME)),
									 DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_DESCRIPTION)));
		return modelingType;
	}

	private void retrieveParameterTypes(ModelingType modelingType) throws RetrieveObjectException {	
		List inParTyps = new ArrayList();
		List outParTyps = new ArrayList();

		String modelingTypeIdStr = DatabaseIdentifier.toSQLString(modelingType.getId());
		String sql = SQL_SELECT
			+ LINK_COLUMN_PARAMETER_TYPE_ID + COMMA
			+ LINK_COLUMN_PARAMETER_MODE
			+ SQL_FROM + ObjectEntities.MODTYPPARTYPLINK_ENTITY
			+ SQL_WHERE + LINK_COLUMN_MODELING_TYPE_ID + EQUALS + modelingTypeIdStr;
		Statement statement = null;
		ResultSet resultSet = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage("ModelingTypeDatabase.retrieveParameterTypes | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);
			String parameterMode;			
			Identifier parameterTypeId;
			while (resultSet.next()) {
				parameterMode = resultSet.getString(LINK_COLUMN_PARAMETER_MODE);
				parameterTypeId = DatabaseIdentifier.getIdentifier(resultSet, LINK_COLUMN_PARAMETER_TYPE_ID);
				if (parameterMode.equals(MODE_IN))
					inParTyps.add((ParameterType) GeneralStorableObjectPool.getStorableObject(parameterTypeId, true));
				else
					if (parameterMode.equals(MODE_OUT))
						outParTyps.add((ParameterType) GeneralStorableObjectPool.getStorableObject(parameterTypeId, true));
					else
						Log.errorMessage("ModelingTypeDatabase.retrieveParameterTypes | ERROR: Unknown parameter mode '" + parameterMode + "' for parameterTypeId " + parameterTypeId);
			}
		}
		catch (SQLException sqle) {
			String mesg = "ModelingTypeDatabase.retrieveParameterTypes | Cannot retrieve parameter type ids for modeling type '" + modelingTypeIdStr + "' -- " + sqle.getMessage();
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
			}
			finally {
				DatabaseConnection.releaseConnection(connection);
			}
		}

		((ArrayList)inParTyps).trimToSize();
		((ArrayList)outParTyps).trimToSize();
		modelingType.setParameterTypes(inParTyps,
																	 outParTyps);
	}

	private void retrieveParameterTypesByOneQuery(List modelingTypes) throws RetrieveObjectException {
		if ((modelingTypes == null) || (modelingTypes.isEmpty()))
			return;

    StringBuffer sql = new StringBuffer(SQL_SELECT
				+ LINK_COLUMN_PARAMETER_TYPE_ID + COMMA
				+ LINK_COLUMN_PARAMETER_MODE + COMMA
				+ LINK_COLUMN_MODELING_TYPE_ID
				+ SQL_FROM + ObjectEntities.MODTYPPARTYPLINK_ENTITY
				+ SQL_WHERE + LINK_COLUMN_MODELING_TYPE_ID + SQL_IN + OPEN_BRACKET);

		int i = 1;
		for (Iterator it = modelingTypes.iterator(); it.hasNext(); i++) {
			ModelingType modelingType = (ModelingType)it.next();
			sql.append(DatabaseIdentifier.toSQLString(modelingType.getId()));
			if (it.hasNext()) {
				if (((i + 1) % MAXIMUM_EXPRESSION_NUMBER != 0))
					sql.append(COMMA);
				else {
					sql.append(CLOSE_BRACKET);
					sql.append(SQL_OR);
					sql.append(LINK_COLUMN_MODELING_TYPE_ID);
					sql.append(SQL_IN);
					sql.append(OPEN_BRACKET);
				}                   
			}
		}
		sql.append(CLOSE_BRACKET);

		Statement statement = null;
		ResultSet resultSet = null;
		Connection connection = DatabaseConnection.getConnection();

		try {
			statement = connection.createStatement();
			Log.debugMessage("ModelingTypeDatabase.retrieveParameterTypesByOneQuery | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql.toString());

			Map inParameterTypesMap = new HashMap();
			Map outParameterTypesMap = new HashMap();
			String parameterMode;
			Identifier parameterTypeId;
			Identifier modelingTypeId;
			List inParameterTypes;
			List outParameterTypes;
			while (resultSet.next()) {
				parameterMode = resultSet.getString(LINK_COLUMN_PARAMETER_MODE);
				parameterTypeId = DatabaseIdentifier.getIdentifier(resultSet, LINK_COLUMN_PARAMETER_TYPE_ID);
				modelingTypeId = DatabaseIdentifier.getIdentifier(resultSet, LINK_COLUMN_MODELING_TYPE_ID);

				if (parameterMode.equals(MODE_IN)) {
					inParameterTypes = (List)inParameterTypesMap.get(modelingTypeId);
					if (inParameterTypes == null) {
						inParameterTypes = new ArrayList();
						inParameterTypesMap.put(modelingTypeId, inParameterTypes);
					}
					inParameterTypes.add(GeneralStorableObjectPool.getStorableObject(parameterTypeId, true));
				}
				else
					if (parameterMode.equals(MODE_OUT)) {
						outParameterTypes = (List)outParameterTypesMap.get(modelingTypeId);
						if (outParameterTypes == null) {
							outParameterTypes = new ArrayList();
							outParameterTypesMap.put(modelingTypeId, outParameterTypes);
						}
						outParameterTypes.add(GeneralStorableObjectPool.getStorableObject(parameterTypeId, true));
					}
					else
						Log.errorMessage("ModelingTypeDatabase.retrieveParameterTypes | ERROR: Unknown parameter mode '" + parameterMode + "' for parameterTypeId '" + parameterTypeId + "' of modeling type '" + modelingTypeId + "'");
			}

			ModelingType modelingType;
			for (Iterator it = modelingTypes.iterator(); it.hasNext();) {
				modelingType = (ModelingType)it.next();
				modelingTypeId = modelingType.getId();
				inParameterTypes = (List)inParameterTypesMap.get(modelingTypeId);
				outParameterTypes = (List)outParameterTypesMap.get(modelingTypeId);

				modelingType.setParameterTypes(inParameterTypes, outParameterTypes);
			}

		}
		catch (SQLException sqle) {
			String mesg = "ModelingTypeDatabase.retrieveParameterTypes | Cannot retrieve parameter type ids for modeling types -- " + sqle.getMessage();
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
			}
			finally {
				DatabaseConnection.releaseConnection(connection);
			}
		}
	}

	public Object retrieveObject(StorableObject storableObject, int retrieveKind, Object arg) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
//		ModelingType modelingType = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws CreateObjectException , IllegalDataException {
		ModelingType modelingType = this.fromStorableObject(storableObject);
		this.insertEntity(modelingType);
		this.insertParameterTypes(modelingType);
	}

	public void insert(List storableObjects) throws IllegalDataException, CreateObjectException {
		this.insertEntities(storableObjects);
		for(Iterator it=storableObjects.iterator();it.hasNext();){
			ModelingType modelingType = this.fromStorableObject((StorableObject)it.next());
			insertParameterTypes(modelingType);
		}
	}

	private void insertParameterTypes(ModelingType modelingType) throws CreateObjectException {
		List inParTyps = modelingType.getInParameterTypes();
		List outParTyps = modelingType.getOutParameterTypes();

		Identifier modelingTypeId = modelingType.getId();
		String sql = SQL_INSERT_INTO
			+ ObjectEntities.EVATYPPARTYPLINK_ENTITY + OPEN_BRACKET
			+ LINK_COLUMN_MODELING_TYPE_ID + COMMA
			+ LINK_COLUMN_PARAMETER_TYPE_ID + COMMA
			+ LINK_COLUMN_PARAMETER_MODE
			+ CLOSE_BRACKET + SQL_VALUES + OPEN_BRACKET
			+ QUESTION + COMMA
			+ QUESTION + COMMA
			+ QUESTION
			+ CLOSE_BRACKET;
		PreparedStatement preparedStatement = null;
		Identifier parameterTypeId = null;
		String parameterMode = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			preparedStatement = connection.prepareStatement(sql);
			for (Iterator iterator = inParTyps.iterator(); iterator.hasNext();) {
				parameterTypeId = ((ParameterType) iterator.next()).getId();
				DatabaseIdentifier.setIdentifier(preparedStatement, 1, modelingTypeId);				
				DatabaseIdentifier.setIdentifier(preparedStatement, 2, parameterTypeId);
				parameterMode = MODE_IN;
				preparedStatement.setString(3, parameterMode);
				Log.debugMessage("ModelingTypeDatabase.insertParameterTypes | Inserting parameter type "
								+ parameterTypeId
								+ " of parameter mode '" + parameterMode
								+ "' for modeling type "
								+ modelingTypeId, Log.DEBUGLEVEL09);
				preparedStatement.executeUpdate();
			}
			for (Iterator iterator = outParTyps.iterator(); iterator.hasNext();) {
				parameterTypeId = ((ParameterType) iterator.next()).getId();
				DatabaseIdentifier.setIdentifier(preparedStatement, 1, modelingTypeId);				
				DatabaseIdentifier.setIdentifier(preparedStatement, 2, parameterTypeId);
				parameterMode = MODE_OUT;
				preparedStatement.setString(3, parameterMode);
				Log.debugMessage("ModelingTypeDatabase.insertParameterTypes | Inserting parameter type "
								+ parameterTypeId
								+ " of parameter mode '" + parameterMode
								+ "' for modeling type "
								+ modelingTypeId, Log.DEBUGLEVEL09);
				preparedStatement.executeUpdate();
			}
		}
		catch (SQLException sqle) {
			String mesg = "ModelingTypeDatabase.insertParameterTypes | Cannot insert parameter type '"
					+ parameterTypeId
					+ "' of parameter mode '"
					+ parameterMode
					+ "' for modeling type '"
					+ modelingTypeId + "' -- " + sqle.getMessage();
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
			finally {
				DatabaseConnection.releaseConnection(connection);
			}
		}
	}

	public void update(StorableObject storableObject, int updateKind, Object obj)
			throws IllegalDataException, VersionCollisionException, UpdateObjectException {
//		ModelingType modelingType = this.fromStorableObject(storableObject);
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

	public void update(List storableObjects, int updateKind, Object arg)
			throws IllegalDataException, VersionCollisionException, UpdateObjectException {
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

	public void delete(StorableObject storableObject) throws IllegalDataException {
		ModelingType modelingType = this.fromStorableObject(storableObject);
		String modelingTypeIdStr = DatabaseIdentifier.toSQLString(modelingType.getId());
		Statement statement = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			statement.executeUpdate(SQL_DELETE_FROM
					+ ObjectEntities.MODTYPPARTYPLINK_ENTITY
					+ SQL_WHERE + LINK_COLUMN_MODELING_TYPE_ID + EQUALS + modelingTypeIdStr);
			statement.executeUpdate(SQL_DELETE_FROM
					+ ObjectEntities.MODELINGTYPE_ENTITY
					+ SQL_WHERE + COLUMN_ID + EQUALS + modelingTypeIdStr);
			connection.commit();
		}
		catch (SQLException sqle1) {
			Log.errorException(sqle1);
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
			finally {
				DatabaseConnection.releaseConnection(connection);
			}
		}
	}

	public ModelingType retrieveForCodename(String codename) throws ObjectNotFoundException, RetrieveObjectException {
		List list = null;
		try {
			list = this.retrieveByIds( null , COLUMN_CODENAME + EQUALS + APOSTOPHE + DatabaseString.toQuerySubString(codename, SIZE_CODENAME_COLUMN) + APOSTOPHE);
		}
		catch (IllegalDataException ide) {				
			throw new RetrieveObjectException(ide);
		}

		if ((list == null) || (list.isEmpty()))
			throw new ObjectNotFoundException("No modeling type with codename: '" + codename + "'");

		return (ModelingType) list.get(0);
	}

	public List retrieveAll() throws RetrieveObjectException {
		try {
			return this.retrieveByIds(null, null);
		}
		catch (IllegalDataException ide) {
			throw new RetrieveObjectException(ide);
		}
	}

	public List retrieveByIds(List ids, String condition) throws IllegalDataException, RetrieveObjectException {
		List list = null; 
		if ((ids == null) || (ids.isEmpty()))
			list = this.retrieveByIdsOneQuery(null, condition);
		else
			list = this.retrieveByIdsOneQuery(ids, condition);

		retrieveParameterTypesByOneQuery(list);

		return list;	
	}

	private List retrieveButIdByArgumentSet(List ids, List argumentSetIds) throws RetrieveObjectException, IllegalDataException {
		if (argumentSetIds != null && !argumentSetIds.isEmpty()) {
			String condition = new String();
			StringBuffer argumentIds = new StringBuffer();

			int i = 1;
			for (Iterator it = argumentSetIds.iterator(); it.hasNext(); i++) {
				argumentIds.append(DatabaseIdentifier.toSQLString((Identifier) it.next()));
				if (it.hasNext()) {
					if (((i + 1) % MAXIMUM_EXPRESSION_NUMBER != 0))
						argumentIds.append(COMMA);
					else {
						argumentIds.append(CLOSE_BRACKET);
						argumentIds.append(SQL_OR);
						argumentIds.append(SetDatabase.LINK_COLUMN_SET_ID);
						argumentIds.append(SQL_IN);
						argumentIds.append(OPEN_BRACKET);
					}
				}
			}

			condition = PARAMETER_TYPE_ID + SQL_IN
						+ OPEN_BRACKET
						+ SQL_SELECT + SetDatabase.LINK_COLUMN_TYPE_ID + SQL_FROM + ObjectEntities.SETPARAMETER_ENTITY
						+ SQL_WHERE + SetDatabase.LINK_COLUMN_SET_ID + SQL_IN 
							+ OPEN_BRACKET
							+ argumentIds
							+ CLOSE_BRACKET
					+ CLOSE_BRACKET
					+ SQL_AND + PARAMETER_MODE + EQUALS + APOSTOPHE + MODE_IN + APOSTOPHE;
			return retrieveButIds(ids, condition);
		}
		return Collections.EMPTY_LIST;
	}

	public List retrieveByCondition(List ids, StorableObjectCondition condition)
			throws RetrieveObjectException, IllegalDataException {
		List list;
		if (condition instanceof LinkedIdsCondition) {
			LinkedIdsCondition linkedIdsCondition = (LinkedIdsCondition)condition;
			list = this.retrieveButIdByArgumentSet(ids, linkedIdsCondition.getLinkedIds());
		}
		else {
			Log.errorMessage("ModelingTypeDatabase.retrieveByCondition | Unknown condition class: " + condition);
			list = this.retrieveButIds(ids);
		}
		return list;
	}
}
