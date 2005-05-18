/*
 * $Id: SiteNodeDatabase.java,v 1.23 2005/05/18 11:48:20 bass Exp $
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

import com.syrus.AMFICOM.general.ApplicationException;
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
 * @version $Revision: 1.23 $, $Date: 2005/05/18 11:48:20 $
 * @author $Author: bass $
 * @module map_v1
 */
public class SiteNodeDatabase extends CharacterizableDatabase {
	 private static String columns;
	
	private static String updateMultipleSQLValues;

	private SiteNode fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof SiteNode)
			return (SiteNode) storableObject;
		throw new IllegalDataException("SiteNodeDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	
	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		SiteNode siteNode = this.fromStorableObject(storableObject);
		super.retrieveEntity(siteNode);
	}	
	
	protected String getEnityName() {		
		return ObjectEntities.SITE_NODE_ENTITY;
	}	
	
	protected String getColumnsTmpl() {
		if (columns == null){
			columns = StorableObjectWrapper.COLUMN_NAME + COMMA
				+ StorableObjectWrapper.COLUMN_DESCRIPTION + COMMA
				+ SiteNodeWrapper.COLUMN_LONGITUDE + COMMA
				+ SiteNodeWrapper.COLUMN_LATIUDE + COMMA
				+ SiteNodeWrapper.COLUMN_IMAGE_ID + COMMA
				+ SiteNodeWrapper.COLUMN_SITE_NODE_TYPE_ID + COMMA
				+ SiteNodeWrapper.COLUMN_CITY + COMMA
				+ SiteNodeWrapper.COLUMN_STREET + COMMA
				+ SiteNodeWrapper.COLUMN_BUILDING;
		}
		return columns;
	}	
	
	protected String getUpdateMultipleSQLValuesTmpl() {
		if (updateMultipleSQLValues == null){
			updateMultipleSQLValues = QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION
				+ QUESTION
				+ QUESTION;
		}
		return updateMultipleSQLValues;
	}
	
	
	protected int setEntityForPreparedStatementTmpl(StorableObject storableObject, PreparedStatement preparedStatement, int startParameterNumber)
			throws IllegalDataException, SQLException {
		SiteNode siteNode = fromStorableObject(storableObject);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, siteNode.getName(), SIZE_NAME_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, siteNode.getDescription(), SIZE_DESCRIPTION_COLUMN);
		preparedStatement.setDouble(++startParameterNumber, siteNode.getLocation().getX());
		preparedStatement.setDouble(++startParameterNumber, siteNode.getLocation().getY());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, siteNode.getImageId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, siteNode.getType().getId());
		DatabaseString.setString(preparedStatement, ++startParameterNumber, siteNode.getCity(), MarkDatabase.SIZE_CITY_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, siteNode.getStreet(), MarkDatabase.SIZE_STREET_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, siteNode.getBuilding(), MarkDatabase.SIZE_BUILDING_COLUMN);
		return startParameterNumber;
	}
	
	protected String getUpdateSingleSQLValuesTmpl(StorableObject storableObject) throws IllegalDataException {
		SiteNode siteNode = fromStorableObject(storableObject);
		String values = APOSTOPHE + DatabaseString.toQuerySubString(siteNode.getName(), SIZE_NAME_COLUMN) + APOSTOPHE + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(siteNode.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTOPHE + COMMA
			+ siteNode.getLocation().getX() + COMMA
			+ siteNode.getLocation().getY() + COMMA
			+ DatabaseIdentifier.toSQLString(siteNode.getImageId()) + COMMA
			+ DatabaseIdentifier.toSQLString(siteNode.getType().getId()) + COMMA
			+ DatabaseString.toQuerySubString(siteNode.getCity(), MarkDatabase.SIZE_CITY_COLUMN) + COMMA
			+ DatabaseString.toQuerySubString(siteNode.getStreet(), MarkDatabase.SIZE_STREET_COLUMN) + COMMA
			+ DatabaseString.toQuerySubString(siteNode.getBuilding(), MarkDatabase.SIZE_BUILDING_COLUMN);
		return values;
	}
	
	protected StorableObject updateEntityFromResultSet(StorableObject storableObject, ResultSet resultSet)
	throws IllegalDataException, RetrieveObjectException, SQLException {
		SiteNode siteNode = (storableObject == null) ?
				new SiteNode(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID), null, 0L, null, null, null, null, 0.0, 0.0, null, null, null) :
					fromStorableObject(storableObject);
				
		SiteNodeType type;
		try {
			type = (SiteNodeType) MapStorableObjectPool.getStorableObject(DatabaseIdentifier.getIdentifier(resultSet, SiteNodeWrapper.COLUMN_SITE_NODE_TYPE_ID), true);
		} catch (ApplicationException ae) {
			String msg = this.getEnityName() + "Database.updateEntityFromResultSet | Error " + ae.getMessage();
			throw new RetrieveObjectException(msg, ae);
		} catch (SQLException sqle) {
			String msg = this.getEnityName() + "Database.updateEntityFromResultSet | Error " + sqle.getMessage();
			throw new RetrieveObjectException(msg, sqle);
		}
		siteNode.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
							   DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
							   DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
							   DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
							   resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION),
							   DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_NAME)),
							   DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_DESCRIPTION)),
							   resultSet.getDouble(SiteNodeWrapper.COLUMN_LONGITUDE),
							   resultSet.getDouble(SiteNodeWrapper.COLUMN_LATIUDE),
							   DatabaseIdentifier.getIdentifier(resultSet, SiteNodeWrapper.COLUMN_IMAGE_ID),
							   type,
							   DatabaseString.fromQuerySubString(resultSet.getString(SiteNodeWrapper.COLUMN_CITY)),
							   DatabaseString.fromQuerySubString(resultSet.getString(SiteNodeWrapper.COLUMN_STREET)),
							   DatabaseString.fromQuerySubString(resultSet.getString(SiteNodeWrapper.COLUMN_BUILDING)));		
		return siteNode;
	}

	
	public Object retrieveObject(StorableObject storableObject, int retrieveKind, Object arg) {
//		SiteNode siteNode = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws CreateObjectException , IllegalDataException {
		SiteNode siteNode = this.fromStorableObject(storableObject);
		super.insertEntity(siteNode);
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
