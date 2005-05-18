/*
 * $Id: PhysicalLinkTypeDatabase.java,v 1.22 2005/05/18 11:48:20 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.map;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

import com.syrus.AMFICOM.general.CharacterizableDatabase;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;


/**
 * @version $Revision: 1.22 $, $Date: 2005/05/18 11:48:20 $
 * @author $Author: bass $
 * @module map_v1
 */
public class PhysicalLinkTypeDatabase extends CharacterizableDatabase {
	private static String columns;
	
	private static String updateMultipleSQLValues;

	private PhysicalLinkType fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof PhysicalLinkType)
			return (PhysicalLinkType) storableObject;
		throw new IllegalDataException("PhysicalLinkTypeDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	
	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		PhysicalLinkType physicalLinkType = this.fromStorableObject(storableObject);
		this.retrieveEntity(physicalLinkType);
	}	
	
	protected String getEnityName() {		
		return ObjectEntities.PHYSICAL_LINK_TYPE_ENTITY;
	}	
	
	protected String getColumnsTmpl() {
		if (columns == null){
			columns = StorableObjectWrapper.COLUMN_CODENAME + COMMA
				+ StorableObjectWrapper.COLUMN_NAME + COMMA
				+ StorableObjectWrapper.COLUMN_DESCRIPTION + COMMA
				+ PhysicalLinkTypeWrapper.COLUMN_DIMENSION_X + COMMA
				+ PhysicalLinkTypeWrapper.COLUMN_DIMENSION_Y;
		}
		return columns;
	}	
	
	protected String getUpdateMultipleSQLValuesTmpl() {
		if (updateMultipleSQLValues == null){
			updateMultipleSQLValues = QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA				
				+ QUESTION;
		}
		return updateMultipleSQLValues;
	}
	
	
	protected int setEntityForPreparedStatementTmpl(StorableObject storableObject, PreparedStatement preparedStatement, int startParameterNumber)
			throws IllegalDataException, SQLException {
		PhysicalLinkType physicalLinkType = fromStorableObject(storableObject);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, physicalLinkType.getCodename(), SIZE_CODENAME_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, physicalLinkType.getName(), SIZE_NAME_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, physicalLinkType.getDescription(), SIZE_DESCRIPTION_COLUMN);
		preparedStatement.setInt(++startParameterNumber, physicalLinkType.getBindingDimension().getWidth());
		preparedStatement.setInt(++startParameterNumber, physicalLinkType.getBindingDimension().getHeight());		
		return startParameterNumber;
	}
	
	protected String getUpdateSingleSQLValuesTmpl(StorableObject storableObject) throws IllegalDataException {
		PhysicalLinkType physicalLinkType = fromStorableObject(storableObject);
		String values = APOSTOPHE + DatabaseString.toQuerySubString(physicalLinkType.getCodename(), SIZE_CODENAME_COLUMN) + APOSTOPHE + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(physicalLinkType.getName(), SIZE_NAME_COLUMN) + APOSTOPHE + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(physicalLinkType.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTOPHE + COMMA
			+ physicalLinkType.getBindingDimension().getWidth() + COMMA
			+ physicalLinkType.getBindingDimension().getHeight();
		return values;
	}
	
	protected StorableObject updateEntityFromResultSet(StorableObject storableObject, ResultSet resultSet)
	throws IllegalDataException, SQLException {
		PhysicalLinkType physicalLinkType = (storableObject == null) ?
				new PhysicalLinkType(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID), null, 0L, null, null, null, null, null) :
					fromStorableObject(storableObject);
		physicalLinkType.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
							   DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
							   DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
							   DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
							   resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION),
							   DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_CODENAME)),
							   DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_NAME)),
							   DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_DESCRIPTION)),
							   resultSet.getInt(PhysicalLinkTypeWrapper.COLUMN_DIMENSION_X),
							   resultSet.getInt(PhysicalLinkTypeWrapper.COLUMN_DIMENSION_Y));		
		return physicalLinkType;
	}

	
	public Object retrieveObject(StorableObject storableObject, int retrieveKind, Object arg) {
//		PhysicalLinkType physicalLinkType = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws CreateObjectException , IllegalDataException {
		PhysicalLinkType physicalLinkType = this.fromStorableObject(storableObject);
		super.insertEntity(physicalLinkType);
	}
	
	
	public void insert(Set storableObjects) throws IllegalDataException, CreateObjectException {
		super.insertEntities(storableObjects);
	}

	public void update(StorableObject storableObject, Identifier modifierId, int updateKind) throws VersionCollisionException, UpdateObjectException {
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
	
	
	public void update(Set storableObjects, Identifier modifierId, int updateKind) throws VersionCollisionException, UpdateObjectException {
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

}
