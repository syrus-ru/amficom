/*
 * $Id: DomainDatabase.java,v 1.11 2005/02/11 07:50:02 bob Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������.
 */

package com.syrus.AMFICOM.administration;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.syrus.AMFICOM.general.CharacteristicDatabase;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.GeneralDatabaseContext;
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
import com.syrus.AMFICOM.general.corba.CharacteristicSort;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;


/**
 * @version $Revision: 1.11 $, $Date: 2005/02/11 07:50:02 $
 * @author $Author: bob $
 * @module administration_v1
 */

public class DomainDatabase extends StorableObjectDatabase {
	private static String columns;
	private static String updateMultiplySQLValues;
	
	private Domain fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof Domain)
			return (Domain) storableObject;
		throw new IllegalDataException("DomainDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}
	
	protected String getEnityName() {
		return ObjectEntities.DOMAIN_ENTITY;
	}
	
	protected String getColumns(int mode) {
		if (columns == null) {
			columns = super.getColumns(mode) + COMMA
			+ DomainMember.COLUMN_DOMAIN_ID + COMMA
			+ StorableObjectWrapper.COLUMN_NAME + COMMA
			+ StorableObjectWrapper.COLUMN_DESCRIPTION;
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
		Domain domain = this.fromStorableObject(storableObject);
		Identifier domainId = domain.getDomainId();
		String sql = super.getUpdateSingleSQLValues(storableObject) + COMMA
			+ DatabaseIdentifier.toSQLString(domainId) + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(domain.getName(), SIZE_NAME_COLUMN) + APOSTOPHE + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(domain.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTOPHE;
		return sql;
	}
	
	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)(GeneralDatabaseContext.getCharacteristicDatabase());
		Domain domain = this.fromStorableObject(storableObject);
		this.retrieveEntity(domain);
		domain.setCharacteristics0(characteristicDatabase.retrieveCharacteristics(domain.getId(), CharacteristicSort.CHARACTERISTIC_SORT_DOMAIN));
	}

	protected StorableObject updateEntityFromResultSet(
			StorableObject storableObject, ResultSet resultSet)
			throws IllegalDataException, RetrieveObjectException, SQLException {
		Domain domain = storableObject == null ? null : this.fromStorableObject(storableObject);
		if (domain == null) {
			domain = new Domain(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID),
											null,
											0L,
											null,
											null,
											null);			
		}
		Identifier domainId = DatabaseIdentifier.getIdentifier(resultSet, DomainMember.COLUMN_DOMAIN_ID);
		domain.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
							 DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
							 DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
							 DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
							 resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION),
							 domainId,
							 DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_NAME)),
							 DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_DESCRIPTION)));		
		
        return domain;
	}
	
	protected int setEntityForPreparedStatement(StorableObject storableObject,
			PreparedStatement preparedStatement, int mode) throws IllegalDataException,
			UpdateObjectException {
		Domain domain = this.fromStorableObject(storableObject);
		Identifier domainId = domain.getDomainId();
		int i;
		try {
			i = super.setEntityForPreparedStatement(storableObject, preparedStatement, mode);
			DatabaseIdentifier.setIdentifier(preparedStatement, ++i, domainId);
			DatabaseString.setString(preparedStatement, ++i, domain.getName(), SIZE_NAME_COLUMN);
			DatabaseString.setString(preparedStatement, ++i, domain.getDescription(), SIZE_DESCRIPTION_COLUMN);
		}catch (SQLException sqle) {
			throw new UpdateObjectException("DomainDatabase." +
					"setEntityForPreparedStatement | Error " + sqle.getMessage(), sqle);
		}
		return i;
	}
		
	
	public Object retrieveObject(StorableObject storableObject, int retrieveKind, Object arg) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
//		Domain domain = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws IllegalDataException, CreateObjectException {
		Domain domain = this.fromStorableObject(storableObject);
		super.insertEntity(domain);
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)(GeneralDatabaseContext.getCharacteristicDatabase());
		try {
			characteristicDatabase.updateCharacteristics(domain);
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

	public void update(StorableObject storableObject, Identifier modifierId, int updateKind) 
			throws IllegalDataException, VersionCollisionException, 
			UpdateObjectException {
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)(GeneralDatabaseContext.getCharacteristicDatabase());
		switch (updateKind) {
			case UPDATE_FORCE:
				super.checkAndUpdateEntity(storableObject, modifierId, true);
				characteristicDatabase.updateCharacteristics(storableObject);
				break;
			case UPDATE_CHECK:
			default:
				super.checkAndUpdateEntity(storableObject, modifierId, false);
				characteristicDatabase.updateCharacteristics(storableObject);
			break;
		}
	}

	public void update(List storableObjects, Identifier modifierId, int updateKind)
			throws IllegalDataException, VersionCollisionException, UpdateObjectException {
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)(GeneralDatabaseContext.getCharacteristicDatabase());
		switch (updateKind) {
			case UPDATE_FORCE:
				super.checkAndUpdateEntities(storableObjects, modifierId, true);
				characteristicDatabase.updateCharacteristics(storableObjects);
				break;
			case UPDATE_CHECK:
			default:
				super.checkAndUpdateEntities(storableObjects, modifierId, false);
				characteristicDatabase.updateCharacteristics(storableObjects);
			break;
		}
	}

	public List retrieveByIds(List ids, String condition) throws IllegalDataException, RetrieveObjectException {
		if ((ids == null) || (ids.isEmpty()))
			return this.retrieveByIdsOneQuery(null, condition);

		List retrivedDomains = this.retrieveByIdsOneQuery(ids, condition);

		if (retrivedDomains != null && !retrivedDomains.isEmpty()) {
			CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase) (GeneralDatabaseContext.getCharacteristicDatabase());
			Map characteristicMap = characteristicDatabase.retrieveCharacteristicsByOneQuery(retrivedDomains,
					CharacteristicSort.CHARACTERISTIC_SORT_DOMAIN);
			if (characteristicMap != null)
				for (Iterator iter = retrivedDomains.iterator(); iter.hasNext();) {
					Domain domain = (Domain) iter.next();
					List characteristics = (List) characteristicMap.get(domain.getId());
					domain.setCharacteristics0(characteristics);
				}
		}
		return retrivedDomains;

		// return retriveByIdsPreparedStatement(ids);
	}

// private List retrieveButIdsByDomain(List ids, Domain domain) throws
// RetrieveObjectException {
//		List list = null;
//
//		String condition = StorableObjectWrapper.COLUMN_ID + EQUALS + DatabaseIdentifier.toSQLString(domain.getId());
//
//		try {
//			list = retrieveButIds(ids, condition);
//		}
//		catch (IllegalDataException ide) {           
//			Log.debugMessage("DomainDatabase.retrieveButIdsByDomain | Error: " + ide.getMessage(), Log.DEBUGLEVEL09);
//		}
//
//		return list;
//	}

}
