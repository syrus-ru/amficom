/*
 * $Id: MeasurementPortDatabase.java,v 1.33 2005/01/28 10:23:01 arseniy Exp $
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

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.CharacteristicDatabase;
import com.syrus.AMFICOM.general.GeneralDatabaseContext;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.AMFICOM.general.corba.CharacteristicSort;
import com.syrus.AMFICOM.administration.Domain;
import com.syrus.AMFICOM.administration.DomainMember;
import com.syrus.AMFICOM.administration.DomainCondition;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;


/**
 * @version $Revision: 1.33 $, $Date: 2005/01/28 10:23:01 $
 * @author $Author: arseniy $
 * @module config_v1
 */
public class MeasurementPortDatabase extends StorableObjectDatabase {
	// table :: MeasurementPort

	public static final int CHARACTER_NUMBER_OF_RECORDS = 1;

	private static String columns;
	private static String updateMultiplySQLValues;

	protected String getEnityName() {
		return ObjectEntities.MEASUREMENTPORT_ENTITY;
	}

	protected String getColumns(int mode) {
		if (columns == null) {
			columns = super.getColumns(mode) + COMMA
				+ MeasurementPortWrapper.COLUMN_TYPE_ID + COMMA
				+ MeasurementPortWrapper.COLUMN_NAME + COMMA
				+ MeasurementPortWrapper.COLUMN_DESCRIPTION + COMMA
				+ MeasurementPortWrapper.COLUMN_KIS_ID + COMMA
				+ MeasurementPortWrapper.COLUMN_PORT_ID;
		}
		return columns; 
	}

	protected String getUpdateMultiplySQLValues(int mode) {
		if (updateMultiplySQLValues == null) {
			updateMultiplySQLValues = super.getUpdateMultiplySQLValues(mode) + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION;
		}
		return updateMultiplySQLValues;
	}

	protected String getUpdateSingleSQLValues(StorableObject storableObject)
			throws IllegalDataException, UpdateObjectException {
		MeasurementPort measurementPort = this.fromStorableObject(storableObject);
		Identifier typeId = measurementPort.getType().getId();
		Identifier kisId = measurementPort.getKISId();
		Identifier portId = measurementPort.getPortId();
		String sql = super.getUpdateSingleSQLValues(storableObject) + COMMA
			+ DatabaseIdentifier.toSQLString(typeId) + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(measurementPort.getName(), SIZE_NAME_COLUMN) + APOSTOPHE	+ COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(measurementPort.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTOPHE + COMMA
			+ DatabaseIdentifier.toSQLString(kisId)	+ COMMA
			+ DatabaseIdentifier.toSQLString(portId);
		return sql;
	}

	protected int setEntityForPreparedStatement(StorableObject storableObject, PreparedStatement preparedStatement, int mode)
			throws IllegalDataException, UpdateObjectException {
		MeasurementPort measurementPort = this.fromStorableObject(storableObject);
		Identifier typeId = measurementPort.getType().getId();
		Identifier kisId = measurementPort.getKISId();
		Identifier portId = measurementPort.getPortId();
		int i;
		try {
			i = super.setEntityForPreparedStatement(storableObject, preparedStatement, mode);
			DatabaseIdentifier.setIdentifier(preparedStatement, ++i, typeId);
			DatabaseString.setString(preparedStatement, ++i, measurementPort.getName(), SIZE_NAME_COLUMN);
			DatabaseString.setString(preparedStatement, ++i, measurementPort.getDescription(), SIZE_DESCRIPTION_COLUMN);
			DatabaseIdentifier.setIdentifier(preparedStatement, ++i, kisId);
			DatabaseIdentifier.setIdentifier(preparedStatement, ++i, portId);
		}
		catch (SQLException sqle) {
			throw new UpdateObjectException("MeasurmentPortDatabase." +
					"setEntityForPreparedStatement | Error " + sqle.getMessage(), sqle);
		}
		return i;
	}

  private MeasurementPort fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof MeasurementPort)
			return (MeasurementPort)storableObject;
		throw new IllegalDataException("MeasurementPortDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		MeasurementPort measurementPort = this.fromStorableObject(storableObject);
		super.retrieveEntity(measurementPort);
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)(GeneralDatabaseContext.getCharacteristicDatabase());
		measurementPort.setCharacteristics0(characteristicDatabase.retrieveCharacteristics(measurementPort.getId(), CharacteristicSort.CHARACTERISTIC_SORT_MEASUREMENTPORT));
	}

	public List retrieveByIds(List ids, String condition)
			throws IllegalDataException, RetrieveObjectException {
		List list = null;
		if ((ids == null) || (ids.isEmpty()))
			list = this.retrieveByIdsOneQuery(null, condition);
		else
			list = this.retrieveByIdsOneQuery(ids, condition);

    if (list != null) {
			CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase) (GeneralDatabaseContext.getCharacteristicDatabase());
			Map characteristicMap = characteristicDatabase.retrieveCharacteristicsByOneQuery(list,
					CharacteristicSort.CHARACTERISTIC_SORT_MEASUREMENTPORT);
			if (characteristicMap != null)
				for (Iterator iter = list.iterator(); iter.hasNext();) {
					MeasurementPort measurementPort = (MeasurementPort) iter.next();
					List characteristics = (List) characteristicMap.get(measurementPort.getId());
					measurementPort.setCharacteristics0(characteristics);
				}
		}

    return list;
	}

	protected StorableObject updateEntityFromResultSet(StorableObject storableObject, ResultSet resultSet)
			throws IllegalDataException, RetrieveObjectException, SQLException {
		MeasurementPort measurementPort = storableObject == null ? null : this.fromStorableObject(storableObject);
		if (measurementPort == null){
			measurementPort = new MeasurementPort(DatabaseIdentifier.getIdentifier(resultSet, COLUMN_ID),
															null,
															null,
															null,
															null,
															null,
															null);			
		}
		MeasurementPortType measurementPortType;
		try {
			Identifier measurementPortTypeId = DatabaseIdentifier.getIdentifier(resultSet, MeasurementPortWrapper.COLUMN_TYPE_ID);
			measurementPortType = (measurementPortTypeId != null) ? (MeasurementPortType)ConfigurationStorableObjectPool.getStorableObject(measurementPortTypeId, true) : null;
		}
		catch (ApplicationException ae) {
			throw new RetrieveObjectException(ae);
		}

		String name = DatabaseString.fromQuerySubString(resultSet.getString(MeasurementPortWrapper.COLUMN_NAME));

		String description = DatabaseString.fromQuerySubString(resultSet.getString(MeasurementPortWrapper.COLUMN_DESCRIPTION));
		measurementPort.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
											DatabaseDate.fromQuerySubString(resultSet, COLUMN_MODIFIED),								  
											DatabaseIdentifier.getIdentifier(resultSet, COLUMN_CREATOR_ID),
											DatabaseIdentifier.getIdentifier(resultSet, COLUMN_MODIFIER_ID),						  
											measurementPortType,
											(name != null) ? name : "",
											(description != null) ? description : "",
											DatabaseIdentifier.getIdentifier(resultSet, MeasurementPortWrapper.COLUMN_KIS_ID),
											DatabaseIdentifier.getIdentifier(resultSet, MeasurementPortWrapper.COLUMN_PORT_ID));
		return measurementPort;
	}

	public Object retrieveObject(StorableObject storableObject, int retrieveKind, Object arg) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
//		MeasurementPort measurementPort = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				return null;
		}
	}

	public void insert(StorableObject storableObject)
			throws IllegalDataException, CreateObjectException {
		MeasurementPort measurementPort = this.fromStorableObject(storableObject);
		super.insertEntity(measurementPort);		
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)(GeneralDatabaseContext.getCharacteristicDatabase());
		try {
			characteristicDatabase.updateCharacteristics(measurementPort);
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
		MeasurementPort measurementPort = this.fromStorableObject(storableObject);
		switch (updateKind) {
			case UPDATE_FORCE:
				super.checkAndUpdateEntity(measurementPort, true);
				break;
			case UPDATE_CHECK: 					
			default:
				super.checkAndUpdateEntity(measurementPort, false);
				break;
		}
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)(GeneralDatabaseContext.getCharacteristicDatabase());
		characteristicDatabase.updateCharacteristics(measurementPort);
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
			Log.debugMessage("MeasurementPortDatabase.retrieveAll | Trying: " + ide, Log.DEBUGLEVEL09);
			throw new RetrieveObjectException(ide);
		}
		return list;
	}

	private List retrieveButIdsByDomain(List ids, Domain domain) throws RetrieveObjectException {
		List list = null;

		String condition = MeasurementPortWrapper.COLUMN_KIS_ID + SQL_IN + OPEN_BRACKET
			+ SQL_SELECT + COLUMN_ID
			+ SQL_FROM + ObjectEntities.KIS_ENTITY
			+ SQL_WHERE + DomainMember.COLUMN_DOMAIN_ID + EQUALS + DatabaseIdentifier.toSQLString(domain.getId())
			+ CLOSE_BRACKET;

		try {
			list = retrieveButIds(ids, condition);
		}
		catch (IllegalDataException ide) {
			Log.debugMessage("MeasurementPortDatabase.retrieveButIdsByDomain | Error: " + ide.getMessage(), Log.DEBUGLEVEL09);
		}

		return list;
	}

	public List retrieveByCondition(List ids, StorableObjectCondition condition)
			throws RetrieveObjectException, IllegalDataException {
		List list;
		if (condition instanceof DomainCondition) {
			DomainCondition domainCondition = (DomainCondition)condition;
			list = this.retrieveButIdsByDomain(ids, domainCondition.getDomain());
		}
		else {
			Log.errorMessage("MeasurementPortDatabase.retrieveByCondition | Unknown condition class: " + condition.getClass().getName());
			list = this.retrieveButIds(ids);
		}
		return list;
	}
}
