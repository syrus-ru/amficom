/*
 * $Id: ModelingDatabase.java,v 1.37 2005/04/01 08:43:32 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.measurement;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.37 $, $Date: 2005/04/01 08:43:32 $
 * @author $Author: bob $
 * @module module_name
 */

public class ModelingDatabase extends StorableObjectDatabase {

	private static String columns;

	private static String updateMultipleSQLValues;

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

	protected String getColumnsTmpl() {
		if (columns == null) {
			columns = StorableObjectWrapper.COLUMN_TYPE_ID + COMMA
				+ ModelingWrapper.COLUMN_MONITORED_ELEMENT_ID + COMMA
				+ ModelingWrapper.COLUMN_ARGUMENT_SET_ID + COMMA
				+ StorableObjectWrapper.COLUMN_NAME;
		}
		return columns;
	}

	protected String getUpdateMultipleSQLValuesTmpl() {
		if (updateMultipleSQLValues == null) {
			updateMultipleSQLValues = QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION;
		}
		return updateMultipleSQLValues;
	}

	protected int setEntityForPreparedStatementTmpl(StorableObject storableObject, PreparedStatement preparedStatement, int startParameterNumber)
			throws IllegalDataException, SQLException {
		Modeling modeling = this.fromStorableObject(storableObject);
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, modeling.getType().getId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, modeling.getMonitoredElementId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, modeling.getArgumentSet().getId());
		DatabaseString.setString(preparedStatement, ++startParameterNumber, modeling.getName(), SIZE_NAME_COLUMN);
		return startParameterNumber;
	}

  protected String getUpdateSingleSQLValuesTmpl(StorableObject storableObject) throws IllegalDataException {
    Modeling modeling = this.fromStorableObject(storableObject);
		String values = DatabaseIdentifier.toSQLString(modeling.getType().getId()) + COMMA
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
  	Modeling modeling = this.fromStorableObject(storableObject);
    switch (retrieveKind) {
			default:
				Log.errorMessage("Unknown retrieve kind: " + retrieveKind + " for " + this.getEnityName() + " '" +  modeling.getId() + "'; argument: " + arg);
				return null;
		}
	}

  public void insert(StorableObject storableObject) throws CreateObjectException , IllegalDataException {
		Modeling modeling = this.fromStorableObject(storableObject);
		this.insertEntity(modeling);
	}

  public void insert(java.util.Set storableObjects) throws IllegalDataException, CreateObjectException {
		this.insertEntities(storableObjects);
	}

}
