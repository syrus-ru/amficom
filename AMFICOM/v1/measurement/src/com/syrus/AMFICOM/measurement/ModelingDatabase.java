/*
 * $Id: ModelingDatabase.java,v 1.25 2005/02/11 11:55:22 bob Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������.
 */
package com.syrus.AMFICOM.measurement;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.syrus.AMFICOM.administration.Domain;
import com.syrus.AMFICOM.administration.DomainMember;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.25 $, $Date: 2005/02/11 11:55:22 $
 * @author $Author: bob $
 * @module module_name
 */

public class ModelingDatabase extends StorableObjectDatabase {

	private static String columns;

	private static String updateMultiplySQLValues;

	private Modeling fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof Modeling)
			return (Modeling) storableObject;
		throw new IllegalDataException("ModelingDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		Modeling modeling = this.fromStorableObject(storableObject);
		this.retrieveEntity(modeling);
	}

	protected String getEnityName() {
		return ObjectEntities.MODELING_ENTITY;
	}

	protected String getColumns(int mode) {
		if (columns == null) {
			columns = super.getColumns(mode) + COMMA
				+ StorableObjectWrapper.COLUMN_TYPE_ID + COMMA
				+ ModelingWrapper.COLUMN_MONITORED_ELEMENT_ID + COMMA
				+ ModelingWrapper.COLUMN_ARGUMENT_SET_ID + COMMA
				+ StorableObjectWrapper.COLUMN_NAME;
		}
		return columns;
	}

	protected String getUpdateMultiplySQLValues(int mode) {
		if (updateMultiplySQLValues == null) {
			updateMultiplySQLValues = super.getUpdateMultiplySQLValues(mode) + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION;
		}
		return updateMultiplySQLValues;
	}

	protected int setEntityForPreparedStatement(StorableObject storableObject, PreparedStatement preparedStatement, int mode)
			throws IllegalDataException, UpdateObjectException {
		Modeling modeling = this.fromStorableObject(storableObject);
		int i = super.setEntityForPreparedStatement(storableObject, preparedStatement, mode);
		try {
			DatabaseIdentifier.setIdentifier(preparedStatement, ++i, modeling.getType().getId());
			DatabaseIdentifier.setIdentifier(preparedStatement, ++i, modeling.getMonitoredElementId());
			DatabaseIdentifier.setIdentifier(preparedStatement, ++i, modeling.getArgumentSet().getId());
			DatabaseString.setString(preparedStatement, ++i, modeling.getName(), SIZE_NAME_COLUMN);
		}
		catch (SQLException sqle) {
			throw new UpdateObjectException(getEnityName() + "Database.setEntityForPreparedStatement | Error " + sqle.getMessage(), sqle);
		}
		return i;
	}

  protected String getUpdateSingleSQLValues(StorableObject storableObject)
			throws IllegalDataException, UpdateObjectException {
    Modeling modeling = this.fromStorableObject(storableObject);
		String values = super.getUpdateSingleSQLValues(storableObject) + COMMA
				+ DatabaseIdentifier.toSQLString(modeling.getType().getId()) + COMMA
				+ DatabaseIdentifier.toSQLString(modeling.getMonitoredElementId()) + COMMA
				+ DatabaseIdentifier.toSQLString(modeling.getArgumentSet().getId()) + COMMA
				+ APOSTOPHE + DatabaseString.toQuerySubString(modeling.getName(), SIZE_NAME_COLUMN) + APOSTOPHE;
		return values;
	}

  protected StorableObject updateEntityFromResultSet(StorableObject storableObject, ResultSet resultSet)
			throws IllegalDataException, RetrieveObjectException, SQLException {
		Modeling modeling = (storableObject == null) ? 
                new Modeling(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID), null, 0L, null, null, null, null) : 
                    this.fromStorableObject(storableObject);
		ModelingType modelingType;
		Set argumentSet;
		try {
			modelingType = (ModelingType)MeasurementStorableObjectPool.getStorableObject(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_TYPE_ID), true);
			argumentSet = (Set)MeasurementStorableObjectPool.getStorableObject(DatabaseIdentifier.getIdentifier(resultSet, ModelingWrapper.COLUMN_ARGUMENT_SET_ID), true);
		}
		catch (ApplicationException ae) {
			throw new RetrieveObjectException(ae);
		}

    modeling.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
													DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
													DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
													DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
													resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION),
													modelingType,
													DatabaseIdentifier.getIdentifier(resultSet, ModelingWrapper.COLUMN_MONITORED_ELEMENT_ID),
													DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_NAME)),
													argumentSet);
    return modeling;
	}

  public Object retrieveObject(StorableObject storableObject, int retrieveKind, Object arg) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
//  	Modeling modeling = this.fromStorableObject(storableObject);
    switch (retrieveKind) {
			default:
				return null;
		}
	}

  public void insert(StorableObject storableObject) throws CreateObjectException , IllegalDataException {
		Modeling modeling = this.fromStorableObject(storableObject);
		this.insertEntity(modeling);
	}

  public void insert(List storableObjects) throws IllegalDataException, CreateObjectException {
		this.insertEntities(storableObjects);
	}

  public void update(StorableObject storableObject, Identifier modifierId, int updateKind) throws IllegalDataException, VersionCollisionException, UpdateObjectException {
//		Modeling modeling = this.fromStorableObject(storableObject);
		switch (updateKind) {
			case UPDATE_CHECK:
				super.checkAndUpdateEntity(storableObject, modifierId, false);
				break;
			case UPDATE_FORCE:
			default:
				super.checkAndUpdateEntity(storableObject, modifierId, true);
				return;
		}
	}

  public void update(List storableObjects, Identifier modifierId, int updateKind)
			throws IllegalDataException, VersionCollisionException, UpdateObjectException {
    switch (updateKind) {
			case UPDATE_CHECK:
				super.checkAndUpdateEntities(storableObjects, modifierId, false);
				break;
			case UPDATE_FORCE:
			default:
				super.checkAndUpdateEntities(storableObjects, modifierId, true);
				return;
		}
	}

  public List retrieveByIds(List ids, String conditions) throws IllegalDataException, RetrieveObjectException {
		if ((ids == null) || (ids.isEmpty()))
			return this.retrieveByIdsOneQuery(null, conditions);
		return this.retrieveByIdsOneQuery(ids, conditions);  
		//return retriveByIdsPreparedStatement(ids, conditions);
	}

	public List retrieveButIdsByDomain(List ids, Domain domain) throws RetrieveObjectException {
		List list = null;

    String condition = ModelingWrapper.COLUMN_MONITORED_ELEMENT_ID + SQL_IN
				+ OPEN_BRACKET
					+ SQL_SELECT
					+ StorableObjectWrapper.COLUMN_ID
					+ SQL_FROM + ObjectEntities.ME_ENTITY
					+ SQL_WHERE + DomainMember.COLUMN_DOMAIN_ID + EQUALS + DatabaseIdentifier.toSQLString(domain.getId())
				+ CLOSE_BRACKET;

    try {
			list = retrieveButIds(ids, condition);
		}
		catch (IllegalDataException ide) {
			Log.debugMessage("EvaluationDatabase.retrieveButIdsByDomain | Error: " + ide.getMessage(), Log.DEBUGLEVEL09);
		}

		return list;
	}
}
