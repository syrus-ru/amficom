/*
 * $Id: SiteNodeTypeDatabase.java,v 1.18 2005/03/11 10:48:11 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.map;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

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
 * @version $Revision: 1.18 $, $Date: 2005/03/11 10:48:11 $
 * @author $Author: bob $
 * @module map_v1
 */
public class SiteNodeTypeDatabase extends CharacterizableDatabase {
	 private static String columns;
	
	private static String updateMultipleSQLValues;

	private SiteNodeType fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof SiteNodeType)
			return (SiteNodeType) storableObject;
		throw new IllegalDataException("SiteNodeTypeDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	
	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		SiteNodeType siteNodeType = this.fromStorableObject(storableObject);
		this.retrieveEntity(siteNodeType);
	}	
	
	protected String getEnityName() {		
		return ObjectEntities.SITE_NODE_TYPE_ENTITY;
	}	
	
	protected String getColumnsTmpl() {
		if (columns == null){
			columns = StorableObjectWrapper.COLUMN_CODENAME + COMMA
				+ StorableObjectWrapper.COLUMN_NAME + COMMA
				+ StorableObjectWrapper.COLUMN_DESCRIPTION + COMMA
				+ SiteNodeTypeWrapper.COLUMN_IMAGE_ID + COMMA
				+ SiteNodeTypeWrapper.COLUMN_TOPOLOGICAL;
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
		SiteNodeType siteNodeType = fromStorableObject(storableObject);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, siteNodeType.getCodename(), SIZE_CODENAME_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, siteNodeType.getName(), SIZE_NAME_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, siteNodeType.getDescription(), SIZE_DESCRIPTION_COLUMN);
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, siteNodeType.getImageId());
		preparedStatement.setInt(++startParameterNumber, siteNodeType.isTopological() ? 1 : 0);
		return startParameterNumber;
	}
	
	protected String getUpdateSingleSQLValuesTmpl(StorableObject storableObject) throws IllegalDataException {
		SiteNodeType siteNodeType = fromStorableObject(storableObject);
		String values = APOSTOPHE + DatabaseString.toQuerySubString(siteNodeType.getCodename(), SIZE_CODENAME_COLUMN) + APOSTOPHE + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(siteNodeType.getName(), SIZE_NAME_COLUMN) + APOSTOPHE + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(siteNodeType.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTOPHE + COMMA
			+ DatabaseIdentifier.toSQLString(siteNodeType.getImageId()) + COMMA
			+ (siteNodeType.isTopological() ? 1 : 0);
		return values;
	}
	
	protected StorableObject updateEntityFromResultSet(StorableObject storableObject, ResultSet resultSet)
	throws IllegalDataException, RetrieveObjectException, SQLException {
		SiteNodeType siteNodeType = (storableObject == null) ? 
				new SiteNodeType(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID), null, 0L, null, null, null, null, false) : 
					fromStorableObject(storableObject);
		siteNodeType.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
							   DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
							   DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
							   DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
							   resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION),
							   DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_CODENAME)),
							   DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_NAME)),
							   DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_DESCRIPTION)),
							   DatabaseIdentifier.getIdentifier(resultSet, SiteNodeTypeWrapper.COLUMN_IMAGE_ID),
							   resultSet.getInt(SiteNodeTypeWrapper.COLUMN_TOPOLOGICAL) == 1);		
		return siteNodeType;
	}

	
	public Object retrieveObject(StorableObject storableObject, int retrieveKind, Object arg) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
//		SiteNodeType siteNodeType = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws CreateObjectException , IllegalDataException {
		SiteNodeType siteNodeType = this.fromStorableObject(storableObject);
		super.insertEntity(siteNodeType);
	}
	
	
	public void insert(Collection storableObjects) throws IllegalDataException, CreateObjectException {
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
	
	
	public void update(Collection storableObjects, Identifier modifierId, int updateKind) throws VersionCollisionException, UpdateObjectException {
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
