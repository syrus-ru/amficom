/*
 * $Id: SetDatabase.java,v 1.53 2005/02/03 08:36:47 bob Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������.
 */

package com.syrus.AMFICOM.measurement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import oracle.sql.BLOB;

import com.syrus.AMFICOM.administration.Domain;
import com.syrus.AMFICOM.administration.DomainMember;
import com.syrus.AMFICOM.administration.DomainCondition;
import com.syrus.AMFICOM.general.GeneralStorableObjectPool;
import com.syrus.AMFICOM.general.ParameterType;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.util.Log;
import com.syrus.util.database.ByteArrayDatabase;
import com.syrus.util.database.DatabaseConnection;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.53 $, $Date: 2005/02/03 08:36:47 $
 * @author $Author: bob $
 * @module measurement_v1
 */

public class SetDatabase extends StorableObjectDatabase {

	public static final int CHARACTER_NUMBER_OF_RECORDS = 1;

	private static String columns;
	private static String updateMultiplySQLValues;    

	protected String getEnityName() {
		return '"' + ObjectEntities.SET_ENTITY + '"';
	}

	protected String getColumns(int mode) {
		if (columns == null) {
			columns = super.getColumns(mode) + COMMA
				+ SetWrapper.COLUMN_SORT  + COMMA
				+ SetWrapper.COLUMN_DESCRIPTION;
		}
		return columns;
	}

	protected String getUpdateMultiplySQLValues(int mode) {
		if (updateMultiplySQLValues == null){
			updateMultiplySQLValues = super.getUpdateMultiplySQLValues(mode) + COMMA
				+ QUESTION  + COMMA
				+ QUESTION;
		}
		return updateMultiplySQLValues;
	}	

	protected String getUpdateSingleSQLValues(StorableObject storableObject)
			throws IllegalDataException, UpdateObjectException {
		Set set = this.fromStorableObject(storableObject);
		String values = super.getUpdateSingleSQLValues(storableObject) + COMMA
			+ Integer.toString(set.getSort().value()) + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(set.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTOPHE;
		return values;
	}	

	protected int setEntityForPreparedStatement(StorableObject storableObject, PreparedStatement preparedStatement, int mode)
			throws IllegalDataException, UpdateObjectException {
		Set set = this.fromStorableObject(storableObject);
		int i = super.setEntityForPreparedStatement(storableObject, preparedStatement, mode);
		try {
			preparedStatement.setInt(++i, set.getSort().value());
			DatabaseString.setString(preparedStatement, ++i, set.getDescription(), SIZE_DESCRIPTION_COLUMN);
		}
		catch (SQLException sqle) {
			throw new UpdateObjectException(getEnityName() + "Database.setEntityForPreparedStatement | Error " + sqle.getMessage(), sqle);
		}
		return i;
	}

	private Set fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof Set)
			return (Set)storableObject;
		throw new IllegalDataException("SetDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		Set set = this.fromStorableObject(storableObject);
		this.retrieveEntity(set);
		this.retrieveSetParameters(set);
		this.retrieveSetMELinksByOneQuery(Collections.singletonList(set));
	}

	protected StorableObject updateEntityFromResultSet(StorableObject storableObject, ResultSet resultSet)
			throws IllegalDataException, RetrieveObjectException, SQLException {
		Set set = (storableObject == null) ?
				new Set(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID), null, 0, null, null, null) :			
				this.fromStorableObject(storableObject);
		String description = DatabaseString.fromQuerySubString(resultSet.getString(SetWrapper.COLUMN_DESCRIPTION));
		set.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
						  DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
						  DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
						  DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
						  resultSet.getInt(SetWrapper.COLUMN_SORT),
						  (description != null) ? description : "");
		return set;
	}

	private void retrieveSetParameters(Set set) throws RetrieveObjectException {
		List parameters = new ArrayList();

		String setIdStr = DatabaseIdentifier.toSQLString(set.getId());
		String sql = SQL_SELECT
			+ StorableObjectWrapper.COLUMN_ID + COMMA			
			+ SetWrapper.LINK_COLUMN_TYPE_ID + COMMA
			+ SetWrapper.LINK_COLUMN_PARAMETER_VALUE
			+ SQL_FROM
			+ ObjectEntities.SETPARAMETER_ENTITY
			+ SQL_WHERE
			+ SetWrapper.LINK_COLUMN_SET_ID +EQUALS
			+ setIdStr;
		Statement statement = null;
		ResultSet resultSet = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage("SetDatabase.retrieveSetParameters | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);
			SetParameter parameter;
			ParameterType parameterType;
			while (resultSet.next()) {
				try {
					parameterType = (ParameterType)MeasurementStorableObjectPool.getStorableObject(DatabaseIdentifier.getIdentifier(resultSet, SetWrapper.LINK_COLUMN_TYPE_ID), true);
				}
				catch (ApplicationException ae) {
					throw new RetrieveObjectException(ae);
				}
				parameter = new SetParameter(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID),
											 parameterType,
											 ByteArrayDatabase.toByteArray(resultSet.getBlob(SetWrapper.LINK_COLUMN_PARAMETER_VALUE)));
				parameters.add(parameter);
			}
		}
		catch (SQLException sqle) {
			String mesg = "SetDatabase.retrieveSetParameters | Cannot retrieve parameters for set '" + setIdStr + "' -- " + sqle.getMessage();
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
			finally {
				DatabaseConnection.releaseConnection(connection);
			}
		}
		set.setParameters((SetParameter[])parameters.toArray(new SetParameter[parameters.size()]));
	}

	private void retrieveSetParametersByOneQuery(List sets) throws RetrieveObjectException {
        if ((sets == null) || (sets.isEmpty()))
			return;

		StringBuffer sql = new StringBuffer(SQL_SELECT + StorableObjectWrapper.COLUMN_ID + COMMA
				+ SetWrapper.LINK_COLUMN_TYPE_ID + COMMA + SetWrapper.LINK_COLUMN_PARAMETER_VALUE + COMMA
				+ SetWrapper.LINK_COLUMN_SET_ID + SQL_FROM + ObjectEntities.SETPARAMETER_ENTITY
				+ SQL_WHERE + SetWrapper.LINK_COLUMN_SET_ID + SQL_IN + OPEN_BRACKET);
		int i = 1;
		for (Iterator it = sets.iterator(); it.hasNext(); i++) {
			Set set = (Set) it.next();
			sql.append(DatabaseIdentifier.toSQLString(set.getId()));
			if (it.hasNext()) {
				if (((i + 1) % MAXIMUM_EXPRESSION_NUMBER != 0))
					sql.append(COMMA);
				else {
					sql.append(CLOSE_BRACKET);
					sql.append(SQL_OR);
					sql.append(SetWrapper.LINK_COLUMN_SET_ID);
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
			Log.debugMessage("SetDatabase.retrieveSetParametersByOneQuery | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql.toString());

			Map setParametersMap = new HashMap();
			Identifier setId;
			ParameterType parameterType;
			SetParameter parameter;
			List setParameters;
			while (resultSet.next()) {
				try {
					parameterType = (ParameterType) GeneralStorableObjectPool.getStorableObject(DatabaseIdentifier.getIdentifier(resultSet, SetWrapper.LINK_COLUMN_TYPE_ID), true);
				}
				catch (ApplicationException ae) {
					throw new RetrieveObjectException(ae);
				}
				parameter = new SetParameter(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID),
														parameterType,
														ByteArrayDatabase.toByteArray(resultSet.getBlob(SetWrapper.LINK_COLUMN_PARAMETER_VALUE)));
				setId = DatabaseIdentifier.getIdentifier(resultSet, SetWrapper.LINK_COLUMN_SET_ID);
				setParameters = (List) setParametersMap.get(setId);
				if (setParameters == null) {
					setParameters = new ArrayList();
					setParametersMap.put(setId, setParameters);
				}
				setParameters.add(parameter);
			}

			Set set;
			for (Iterator it = sets.iterator(); it.hasNext();) {
				set = (Set)it.next();
				setId = set.getId();
				setParameters = (List) setParametersMap.get(setId);

				if (setParameters != null)
					set.setParameters((SetParameter[]) setParameters.toArray(new SetParameter[setParameters.size()]));
			}
		}
		catch (SQLException sqle) {
			String mesg = "SetDatabase.retrieveSetParametersByOneQuery | Cannot retrieve parameters for result -- "
					+ sqle.getMessage();
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
			finally {
				DatabaseConnection.releaseConnection(connection);
			}
		}
	}

	private void retrieveSetMELinksByOneQuery(List sets) throws RetrieveObjectException {
		if ((sets == null) || (sets.isEmpty()))
			return;

		StringBuffer sql = new StringBuffer(SQL_SELECT + SetWrapper.LINK_COLUMN_ME_ID + COMMA
				+ SetWrapper.LINK_COLUMN_SET_ID + SQL_FROM + ObjectEntities.SETMELINK_ENTITY
				+ SQL_WHERE + SetWrapper.LINK_COLUMN_SET_ID + SQL_IN + OPEN_BRACKET);
		int i = 1;
		for (Iterator it = sets.iterator(); it.hasNext(); i++) {
			Set set = (Set) it.next();
			sql.append(DatabaseIdentifier.toSQLString(set.getId()));
			if (it.hasNext()) {
				if (((i + 1) % MAXIMUM_EXPRESSION_NUMBER != 0))
					sql.append(COMMA);
				else {
					sql.append(CLOSE_BRACKET);
					sql.append(SQL_OR);
					sql.append(SetWrapper.LINK_COLUMN_SET_ID);
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
			Log.debugMessage("SetDatabase.retrieveSetMELinksByOneQuery | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql.toString());

			Map meLinkMap = new HashMap();
			Identifier setId;
			List meIds;
			while (resultSet.next()) {
				setId = DatabaseIdentifier.getIdentifier(resultSet, SetWrapper.LINK_COLUMN_SET_ID);
				meIds = (List) meLinkMap.get(setId);
				if (meIds == null) {
					meIds = new ArrayList();
					meLinkMap.put(setId, meIds);
				}
				meIds.add(DatabaseIdentifier.getIdentifier(resultSet, SetWrapper.LINK_COLUMN_ME_ID));

				Set set;
				for (Iterator it = sets.iterator(); it.hasNext();) {
					set = (Set) it.next();
					setId = set.getId();
					meIds = (List) meLinkMap.get(setId);

					set.setMonitoredElementIds0(meIds);
				}
			}
		}
		catch (SQLException sqle) {
			String mesg = "SetDatabase.retrieveSetMELinksByOneQuery | Cannot retrieve parameters for result -- " + sqle.getMessage();
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
			finally {
				DatabaseConnection.releaseConnection(connection);
			}
		}
	}

	public Object retrieveObject(StorableObject storableObject, int retrieveKind, Object arg) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		Set set = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws IllegalDataException, CreateObjectException {
		Set set = this.fromStorableObject(storableObject);
		try {
			this.insertEntity(set);
			this.insertSetParameters(set);
			this.insertSetMELinks(set);
		}
		catch (CreateObjectException coe) {
			this.delete(set);
			throw coe;
		}
	}
	
	public void insert(List storableObjects) throws IllegalDataException, CreateObjectException {
		this.insertEntities(storableObjects);
		for (Iterator it = storableObjects.iterator(); it.hasNext();) {
			Set set = (Set) it.next();
			this.insertSetParameters(set);
			this.insertSetMELinks(set);			
		}

	}

	private void insertSetParameters(Set set) throws CreateObjectException {
		String setIdStr = set.getId().toString();
		SetParameter[] setParameters = set.getParameters();
		String sql = SQL_INSERT_INTO 
			+ ObjectEntities.SETPARAMETER_ENTITY
			+ OPEN_BRACKET
			+ StorableObjectWrapper.COLUMN_ID  + COMMA
			+ SetWrapper.LINK_COLUMN_TYPE_ID + COMMA
			+ SetWrapper.LINK_COLUMN_SET_ID + COMMA
			+ SetWrapper.LINK_COLUMN_PARAMETER_VALUE + CLOSE_BRACKET
			+ SQL_VALUES 
			+ OPEN_BRACKET
			+ QUESTION + COMMA
			+ QUESTION + COMMA
			+ QUESTION + COMMA
			+ QUESTION + CLOSE_BRACKET;
		PreparedStatement preparedStatement = null;
		int i = 0;
		Identifier parameterId = null;
		Identifier parameterTypeId = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			preparedStatement = connection.prepareStatement(sql);
			for (i = 0; i < setParameters.length; i++) {
				parameterId = setParameters[i].getId();
				parameterTypeId = setParameters[i].getType().getId();
				DatabaseIdentifier.setIdentifier(preparedStatement, 1, parameterId);
				DatabaseIdentifier.setIdentifier(preparedStatement, 2, parameterTypeId);
				DatabaseIdentifier.setIdentifier(preparedStatement, 3, set.getId());
				preparedStatement.setBlob(4, BLOB.empty_lob());
				Log.debugMessage("SetDatabase.insertSetParameters | Inserting parameter " + parameterTypeId.toString() + " for set " + setIdStr, Log.DEBUGLEVEL09);
				preparedStatement.executeUpdate();
				ByteArrayDatabase.saveAsBlob(setParameters[i].getValue(),
											 connection,
											 ObjectEntities.SETPARAMETER_ENTITY,
											 SetWrapper.LINK_COLUMN_PARAMETER_VALUE,
											 StorableObjectWrapper.COLUMN_ID + EQUALS + DatabaseIdentifier.toSQLString(parameterId));
			}
			connection.commit();
		}
		catch (SQLException sqle) {
			String mesg = "SetDatabase.insertSetParameters | Cannot insert parameter '" + parameterId.toString() + "' of type '" + parameterTypeId.toString() + "' for set '" + setIdStr + "' -- " + sqle.getMessage();
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
			} finally{
				DatabaseConnection.releaseConnection(connection);
			}
		}
	}

	private void insertSetMELinks(Set set) throws CreateObjectException {
		Identifier setId = set.getId();
		List meIds = set.getMonitoredElementIds();
		String sql = SQL_INSERT_INTO 
			+ ObjectEntities.SETMELINK_ENTITY
			+ OPEN_BRACKET
			+ SetWrapper.LINK_COLUMN_SET_ID + COMMA 
			+ SetWrapper.LINK_COLUMN_ME_ID 
			+ CLOSE_BRACKET
			+ SQL_VALUES + OPEN_BRACKET
			+ QUESTION + COMMA
			+ QUESTION 
			+ CLOSE_BRACKET;
		PreparedStatement preparedStatement = null;
		Identifier meId = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			preparedStatement = connection.prepareStatement(sql);
			for (Iterator iterator = meIds.iterator(); iterator.hasNext();) {
				meId = ((Identifier)iterator.next());
				DatabaseIdentifier.setIdentifier(preparedStatement, 1, setId);
				DatabaseIdentifier.setIdentifier(preparedStatement, 2, meId);
				Log.debugMessage("SetDatabase.insertSetMELinks | Inserting link for set " + setId + " and monitored element " + meId, Log.DEBUGLEVEL09);
				preparedStatement.executeUpdate();
			}
			connection.commit();
		}
		catch (SQLException sqle) {
			String mesg = "SetDatabase.insertSetMELinks | Cannot insert link for monitored element '" + meId + "' and set '" + setId + "' -- " + sqle.getMessage();
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
			}  finally{
				DatabaseConnection.releaseConnection(connection);
			}
		}
	}

	public void update(StorableObject storableObject, int updateKind, Object obj) throws IllegalDataException, VersionCollisionException, UpdateObjectException {
		Set set = this.fromStorableObject(storableObject);
		switch (updateKind) {
			case Set.UPDATE_ATTACH_ME:
				this.createMEAttachment(set, (Identifier)obj);
				this.setModified(set);
				break;
			case Set.UPDATE_DETACH_ME:
				this.deleteMEAttachment(set, (Identifier)obj);
				this.setModified(set);
				break;
			case UPDATE_CHECK:
				super.checkAndUpdateEntity(storableObject, false);
				break;
			case UPDATE_FORCE:					
			default:
				super.checkAndUpdateEntity(storableObject, true);		
				return;
		}
	}

	public void update(List storableObjects, int updateKind, Object obj) throws IllegalDataException,
			VersionCollisionException, UpdateObjectException {		
		switch (updateKind) {
			case Set.UPDATE_ATTACH_ME:
				for (Iterator it = storableObjects.iterator(); it.hasNext();) {
					Set set = (Set) it.next();
					this.createMEAttachment(set, (Identifier)obj);
					this.setModified(set);
				}
				break;
			case Set.UPDATE_DETACH_ME:
				for (Iterator it = storableObjects.iterator(); it.hasNext();) {
					Set set = (Set) it.next();
					this.deleteMEAttachment(set, (Identifier)obj);
					this.setModified(set);
				}
				break;
			case UPDATE_CHECK:
				super.checkAndUpdateEntities(storableObjects, false);
				break;
			case UPDATE_FORCE:					
			default:
				super.checkAndUpdateEntities(storableObjects, true);		
				return;
		}
	}

	private void createMEAttachment(Set set, Identifier monitoredElementId) throws UpdateObjectException {
		String setIdStr = DatabaseIdentifier.toSQLString(set.getId());
		String meIdStr = DatabaseIdentifier.toSQLString(monitoredElementId);
		String sql = SQL_INSERT_INTO 
			+ ObjectEntities.SETMELINK_ENTITY
			+ OPEN_BRACKET
			+ SetWrapper.LINK_COLUMN_SET_ID + COMMA
			+ SetWrapper.LINK_COLUMN_ME_ID
			+ CLOSE_BRACKET
			+ SQL_VALUES
			+ OPEN_BRACKET			
			+ setIdStr + COMMA
			+ meIdStr
			+ CLOSE_BRACKET;
		Statement statement = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage("Set.createMEAttachment | Trying: " + sql, Log.DEBUGLEVEL09);
			statement.executeUpdate(sql);
			connection.commit();
		}
		catch (SQLException sqle) {
			String mesg = "Set.createMEAttachment | Cannot attach set '" + setIdStr + "' to monitored element '" + meIdStr + "' -- " + sqle.getMessage();
			throw new UpdateObjectException(mesg, sqle);
		}
		finally {
			try {
				if (statement != null)
					statement.close();
				statement = null;
			}
			catch (SQLException sqle1) {
				Log.errorException(sqle1);
			} finally{
				DatabaseConnection.releaseConnection(connection);
			}
		}
	}

	private void deleteMEAttachment(Set set, Identifier monitoredElementId) throws UpdateObjectException {
		String setIdStr = DatabaseIdentifier.toSQLString(set.getId());
		String meIdStr = DatabaseIdentifier.toSQLString(monitoredElementId);
		String sql = SQL_DELETE_FROM 
					+ ObjectEntities.SETMELINK_ENTITY
					+ SQL_WHERE 
					+ SetWrapper.LINK_COLUMN_SET_ID + EQUALS
					+ setIdStr
					+ SQL_AND
					+ SetWrapper.LINK_COLUMN_ME_ID + EQUALS
					+ meIdStr;
		Statement statement = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage("SetDatabase.deleteMEAttachment | Trying: " + sql, Log.DEBUGLEVEL09);
			statement.executeUpdate(sql);
			connection.commit();
		}
		catch (SQLException sqle) {
			String mesg = "SetDatabase.deleteMEAttachment | Cannot detach set '" + setIdStr + "' from monitored element '" + meIdStr + "' -- " + sqle.getMessage();
			throw new UpdateObjectException(mesg, sqle);
		}
		finally {
			try {
				if (statement != null)
					statement.close();
				statement = null;
			}
			catch (SQLException sqle1) {
				Log.errorException(sqle1);
			} finally{
				DatabaseConnection.releaseConnection(connection);
			}
		}
	}

	private void setModified(Set set) throws UpdateObjectException {
		String setIdStr = DatabaseIdentifier.toSQLString(set.getId());
		String sql = SQL_UPDATE
					+ ObjectEntities.SET_ENTITY
					+ SQL_SET
					+ StorableObjectWrapper.COLUMN_MODIFIED + EQUALS
					+ DatabaseDate.toUpdateSubString(set.getModified()) + COMMA
					+ StorableObjectWrapper.COLUMN_MODIFIER_ID + EQUALS + DatabaseIdentifier.toSQLString(set.getModifierId())
					+ SQL_WHERE + EQUALS + setIdStr;
		Statement statement = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage("SetDatabase.setModified | Trying: " + sql, Log.DEBUGLEVEL09);
			statement.executeUpdate(sql);
			connection.commit();
		}
		catch (SQLException sqle) {
			String mesg = "SetDatabase.setModified | Cannot set modified for set '" + setIdStr + "' -- " + sqle.getMessage();
			throw new UpdateObjectException(mesg, sqle);
		}
		finally {
			try {
				if (statement != null)
					statement.close();
				statement = null;
			}
			catch (SQLException sqle1) {
				Log.errorException(sqle1);
			} finally{
				DatabaseConnection.releaseConnection(connection);
			}
		}
	}

	public void delete(StorableObject storableObject) throws IllegalDataException {
		Set set = this.fromStorableObject(storableObject);
		String setIdStr = DatabaseIdentifier.toSQLString(set.getId());
		Statement statement = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			statement.executeUpdate(SQL_DELETE_FROM
									+ ObjectEntities.SETMELINK_ENTITY
									+ SQL_WHERE
									+ SetWrapper.LINK_COLUMN_SET_ID + EQUALS
									+ setIdStr);
			statement.executeUpdate(SQL_DELETE_FROM 
									+ ObjectEntities.SETPARAMETER_ENTITY
									+ SQL_WHERE
									+ SetWrapper.LINK_COLUMN_SET_ID + EQUALS
									+ setIdStr);									
			statement.executeUpdate(SQL_DELETE_FROM 
									+ ObjectEntities.SET_ENTITY
									+ SQL_WHERE
									+ StorableObjectWrapper.COLUMN_ID + EQUALS
									+ setIdStr);
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
			finally {
				DatabaseConnection.releaseConnection(connection);
			}
		}
	}

	public List retrieveAll() throws RetrieveObjectException {
		try{
			return this.retrieveByIds(null, null);
		}catch(IllegalDataException ide){
			throw new RetrieveObjectException(ide);
		}
	}

	public List retrieveByIds(List ids, String condition) throws IllegalDataException, RetrieveObjectException {
		List list = null; 
		if ((ids == null) || (ids.isEmpty()))
			list = this.retrieveByIdsOneQuery(null, condition);
		else
			list = this.retrieveByIdsOneQuery(ids, condition);
		
		retrieveSetParametersByOneQuery(list);
		retrieveSetMELinksByOneQuery(list);

		return list;
	}

	private List retrieveButIdsByDomain(List ids, Domain domain) throws RetrieveObjectException {
		List list = null;

		String condition = StorableObjectWrapper.COLUMN_ID + SQL_IN + OPEN_BRACKET + SQL_SELECT
				+ SetWrapper.LINK_COLUMN_SET_ID + SQL_FROM + ObjectEntities.SETMELINK_ENTITY
				+ SQL_WHERE + SetWrapper.LINK_COLUMN_ME_ID + SQL_IN + OPEN_BRACKET + SQL_SELECT
				+ StorableObjectWrapper.COLUMN_ID + SQL_FROM + ObjectEntities.ME_ENTITY + SQL_WHERE
				+ DomainMember.COLUMN_DOMAIN_ID + EQUALS
				+ DatabaseIdentifier.toSQLString(domain.getId()) + CLOSE_BRACKET
				+ CLOSE_BRACKET;
		try {
			list = retrieveButIds(ids, condition);
		}
		catch (IllegalDataException ide) {
			Log.debugMessage("MeasurementDatabase.retrieveButIdsByDomain | Error: " + ide.getMessage(), Log.DEBUGLEVEL09);
		}

		return list;
	}

	public List retrieveByCondition(List ids, StorableObjectCondition condition)
			throws RetrieveObjectException, IllegalDataException {
		List list = null;
		if (condition instanceof DomainCondition) {
			DomainCondition domainCondition = (DomainCondition)condition;
			list = this.retrieveButIdsByDomain(ids, domainCondition.getDomain());
		}
		else {
			Log.errorMessage("MeasurementDatabase.retrieveByCondition | Unknown condition class: " + condition);
			list = this.retrieveButIds(ids);
		}
		return list;
	}
}
