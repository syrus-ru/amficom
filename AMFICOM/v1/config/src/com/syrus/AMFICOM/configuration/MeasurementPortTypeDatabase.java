/*
 * $Id: MeasurementPortTypeDatabase.java,v 1.27 2005/02/03 08:37:00 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.CharacteristicDatabase;
import com.syrus.AMFICOM.general.GeneralDatabaseContext;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.AMFICOM.general.corba.CharacteristicSort;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.27 $, $Date: 2005/02/03 08:37:00 $
 * @author $Author: bob $
 * @module config_v1
 */

public class MeasurementPortTypeDatabase extends StorableObjectDatabase {
	public static final int CHARACTER_NUMBER_OF_RECORDS = 1;

	private static String columns;
	private static String updateMultiplySQLValues;

	private MeasurementPortType fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof MeasurementPortType)
			return (MeasurementPortType)storableObject;
		throw new IllegalDataException("MeasurementPortTypeDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	protected String getEnityName() {
		return ObjectEntities.MEASUREMENTPORTTYPE_ENTITY;
	}

	protected String getColumns(int mode) {
		if (columns == null) {
			columns = super.getColumns(mode) + COMMA
				+ MeasurementPortTypeWrapper.COLUMN_CODENAME + COMMA
				+ MeasurementPortTypeWrapper.COLUMN_DESCRIPTION + COMMA
				+ MeasurementPortTypeWrapper.COLUMN_NAME;
		}
		return columns;
	}

	protected String getUpdateMultiplySQLValues(int mode) {
		if (updateMultiplySQLValues == null) {
    	updateMultiplySQLValues = super.getUpdateMultiplySQLValues(mode) + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION;
    	}
		return updateMultiplySQLValues;
	}

	protected String getUpdateSingleSQLValues(StorableObject storableObject)
			throws IllegalDataException, UpdateObjectException {
		MeasurementPortType measurementPortType = this.fromStorableObject(storableObject);
		String sql = super.getUpdateSingleSQLValues(storableObject) + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(measurementPortType.getCodename(), SIZE_CODENAME_COLUMN) + APOSTOPHE + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(measurementPortType.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTOPHE + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(measurementPortType.getName(), SIZE_NAME_COLUMN) + APOSTOPHE;
		return sql;
	}

	protected int setEntityForPreparedStatement(StorableObject storableObject, PreparedStatement preparedStatement, int mode)
			throws IllegalDataException, UpdateObjectException {
		MeasurementPortType measurementPortType = this.fromStorableObject(storableObject);
		int i;
		try {
			i = super.setEntityForPreparedStatement(storableObject, preparedStatement, mode);
			DatabaseString.setString(preparedStatement, ++i, measurementPortType.getCodename(), SIZE_CODENAME_COLUMN);
			DatabaseString.setString(preparedStatement, ++i, measurementPortType.getDescription(), SIZE_DESCRIPTION_COLUMN);
			DatabaseString.setString(preparedStatement, ++i, measurementPortType.getName(), SIZE_NAME_COLUMN);
		}
		catch (SQLException sqle) {
			throw new UpdateObjectException("MeasurmentPortTypeDatabase." +
					"setEntityForPreparedStatement | Error " + sqle.getMessage(), sqle);
		}
		return i;
	}

	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		MeasurementPortType measurementPortType = this.fromStorableObject(storableObject);
		super.retrieveEntity(measurementPortType);
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)GeneralDatabaseContext.getCharacteristicDatabase();
		measurementPortType.setCharacteristics0(characteristicDatabase.retrieveCharacteristics(measurementPortType.getId(), CharacteristicSort.CHARACTERISTIC_SORT_MEASUREMENTPORTTYPE));
	}

	protected StorableObject updateEntityFromResultSet(StorableObject storableObject, ResultSet resultSet)
			throws IllegalDataException, RetrieveObjectException, SQLException {
		MeasurementPortType measurementPortType = storableObject == null ? null : this.fromStorableObject(storableObject);
		if (measurementPortType == null) {
			measurementPortType = new MeasurementPortType(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID),
																		null,
																		null,
																		null,
																		null);			
		}
		measurementPortType.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
														DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
														DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
														DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
														DatabaseString.fromQuerySubString(resultSet.getString(MeasurementPortTypeWrapper.COLUMN_CODENAME)),
														DatabaseString.fromQuerySubString(resultSet.getString(MeasurementPortTypeWrapper.COLUMN_DESCRIPTION)),
														DatabaseString.fromQuerySubString(resultSet.getString(MeasurementPortTypeWrapper.COLUMN_NAME)));
		return measurementPortType;
	}

	public Object retrieveObject(StorableObject storableObject, int retrieve_kind, Object arg) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
//		MeasurementPortType measurementPortType = this.fromStorableObject(storableObject);
		switch (retrieve_kind) {
			default:
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws IllegalDataException, CreateObjectException {
		MeasurementPortType measurementPortType = this.fromStorableObject(storableObject);
		super.insertEntity(measurementPortType);	
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)(GeneralDatabaseContext.getCharacteristicDatabase());
		try {
			characteristicDatabase.updateCharacteristics(measurementPortType);
		}
		catch (UpdateObjectException e) {
			throw new CreateObjectException(e);
		}
	}

	public void insert(List storableObjects) throws IllegalDataException, CreateObjectException {
		super.insertEntities(storableObjects);
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)(GeneralDatabaseContext.getCharacteristicDatabase());
		try {
			characteristicDatabase.updateCharacteristics(storableObjects);
		}
		catch (UpdateObjectException e) {
			throw new CreateObjectException(e);
		}
	}

	public void update(StorableObject storableObject, int updateKind, Object obj)
			throws IllegalDataException, VersionCollisionException, UpdateObjectException {
		MeasurementPortType measurementPortType = this.fromStorableObject(storableObject);
		switch (updateKind) {
			case UPDATE_FORCE:
				super.checkAndUpdateEntity(measurementPortType, true);
				break;
			case UPDATE_CHECK:
			default:
				super.checkAndUpdateEntity(measurementPortType, false);
				break;
		}
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)(GeneralDatabaseContext.getCharacteristicDatabase());
		characteristicDatabase.updateCharacteristics(measurementPortType);
	}

	public void update(List storableObjects, int updateKind, Object arg)
			throws IllegalDataException, VersionCollisionException, UpdateObjectException {
		switch (updateKind) {
			case UPDATE_FORCE:
				super.checkAndUpdateEntities(storableObjects, true);
				break;
			case UPDATE_CHECK:
			default:
				super.checkAndUpdateEntities(storableObjects, false);
				break;
		}
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)(GeneralDatabaseContext.getCharacteristicDatabase());
		characteristicDatabase.updateCharacteristics(storableObjects);
	}

	public List retrieveAll() throws RetrieveObjectException {
		List list = null;
		try {
			list = this.retrieveByIds(null, null);
		}
		catch (IllegalDataException ide) {           
			Log.debugMessage("MeasurementPortTypeDatabase.retrieveAll | Trying: " + ide, Log.DEBUGLEVEL09);
			throw new RetrieveObjectException(ide);
		}
		return list;
	}

	public List retrieveByIds(List ids, String condition) throws IllegalDataException, RetrieveObjectException {
		List list = null;
		if ((ids == null) || (ids.isEmpty()))
			list = this.retrieveByIdsOneQuery(null, condition);
		else
			list = this.retrieveByIdsOneQuery(ids, condition);

    if (list != null) {
			CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)(GeneralDatabaseContext.getCharacteristicDatabase());
			Map characteristicMap = characteristicDatabase.retrieveCharacteristicsByOneQuery(list,
					CharacteristicSort.CHARACTERISTIC_SORT_MEASUREMENTPORTTYPE);
			if (characteristicMap != null)
				for (Iterator iter = list.iterator(); iter.hasNext();) {
					MeasurementPortType measurementPortType = (MeasurementPortType) iter.next();
					List characteristics = (List) characteristicMap.get(measurementPortType.getId());
					measurementPortType.setCharacteristics0(characteristics);
				}
		}
		return list;
	}

	public List retrieveByCondition(List ids, StorableObjectCondition condition)
			throws RetrieveObjectException, IllegalDataException {
		return this.retrieveButIds(ids);
	}
}
