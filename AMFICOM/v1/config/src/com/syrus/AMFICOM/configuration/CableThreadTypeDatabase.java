/*
 * $Id: CableThreadTypeDatabase.java,v 1.29 2005/04/01 07:57:28 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

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
 * @version $Revision: 1.29 $, $Date: 2005/04/01 07:57:28 $
 * @author $Author: bob $
 * @module config_v1
 */

public class CableThreadTypeDatabase extends StorableObjectDatabase {
	private static String columns;
	private static String updateMultipleSQLValues;
	
	protected String getEnityName() {
		return ObjectEntities.CABLETHREADTYPE_ENTITY;
	}
	
	protected String getUpdateMultipleSQLValuesTmpl() {
		if (updateMultipleSQLValues == null) {
			updateMultipleSQLValues = QUESTION + COMMA
					+ QUESTION + COMMA
					+ QUESTION + COMMA
					+ QUESTION + COMMA
					+ QUESTION;
		}
		return updateMultipleSQLValues;
	}

	protected String getColumnsTmpl() {
		if (columns == null) {
			columns = StorableObjectWrapper.COLUMN_CODENAME + COMMA
				+ StorableObjectWrapper.COLUMN_DESCRIPTION + COMMA
				+ StorableObjectWrapper.COLUMN_NAME + COMMA
				+ CableThreadTypeWrapper.COLUMN_COLOR + COMMA
				+ CableThreadTypeWrapper.COLUMN_LINK_TYPE_ID;
		}
		return columns;
	}
	
	protected String getUpdateSingleSQLValuesTmpl(StorableObject storableObject) throws IllegalDataException {
		CableThreadType cableThreadType = this.fromStorableObject(storableObject);
		String sql = APOSTOPHE + DatabaseString.toQuerySubString(cableThreadType.getCodename(), SIZE_CODENAME_COLUMN) + APOSTOPHE + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(cableThreadType.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTOPHE
            + APOSTOPHE + DatabaseString.toQuerySubString(cableThreadType.getName(), SIZE_NAME_COLUMN) + APOSTOPHE
			+ APOSTOPHE + cableThreadType.getColor() + APOSTOPHE + COMMA
			+ DatabaseIdentifier.toSQLString(cableThreadType.getLinkType().getId());
		return sql;
	}
	
	private CableThreadType fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof CableThreadType)
			return (CableThreadType)storableObject;
		throw new IllegalDataException("CableThreadTypeDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		CableThreadType cableThreadType = this.fromStorableObject(storableObject);
		super.retrieveEntity(cableThreadType);
	}
	
	protected int setEntityForPreparedStatementTmpl(StorableObject storableObject, PreparedStatement preparedStatement, int startParameterNumber)
			throws IllegalDataException, SQLException {
		CableThreadType cableThreadType = this.fromStorableObject(storableObject);
		preparedStatement.setString(++startParameterNumber, cableThreadType.getCodename());
		preparedStatement.setString(++startParameterNumber, cableThreadType.getDescription());
		preparedStatement.setString(++startParameterNumber, cableThreadType.getName());
		preparedStatement.setInt(++startParameterNumber, cableThreadType.getColor());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, cableThreadType.getLinkType().getId());
		return startParameterNumber;
	}
	
	protected StorableObject updateEntityFromResultSet(
			StorableObject storableObject, ResultSet resultSet)
			throws IllegalDataException, RetrieveObjectException, SQLException {
		CableThreadType cableThreadType = storableObject == null ? null : this.fromStorableObject(storableObject);
		if (cableThreadType == null){
			cableThreadType = new CableThreadType(DatabaseIdentifier.getIdentifier(resultSet,
				StorableObjectWrapper.COLUMN_ID), null, 0L, null, null, null, 0, null, null);			
		}
		LinkType linkType;
		CableLinkType cableLinkType;
		try {
			linkType = (LinkType) ConfigurationStorableObjectPool.getStorableObject(DatabaseIdentifier.getIdentifier(resultSet, CableThreadTypeWrapper.COLUMN_LINK_TYPE_ID), true);
			cableLinkType = (CableLinkType) ConfigurationStorableObjectPool.getStorableObject(DatabaseIdentifier.getIdentifier(resultSet, CableThreadTypeWrapper.COLUMN_CABLE_LINK_TYPE_ID), true);
		}
		catch (final ApplicationException ae) {
			throw new RetrieveObjectException(ae);
		}
        
		cableThreadType.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),									
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
				resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION),
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_CODENAME)),
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_DESCRIPTION)),
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_NAME)),
				resultSet.getInt(CableThreadTypeWrapper.COLUMN_COLOR),
				linkType,
				cableLinkType);

		
		return cableThreadType;
	}
	
	public Object retrieveObject(StorableObject storableObject, int retrieveKind, Object arg) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		CableThreadType cableThreadType = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				Log.errorMessage("Unknown retrieve kind: " + retrieveKind + " for " + this.getEnityName() + " '" +  cableThreadType.getId() + "'; argument: " + arg);
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws IllegalDataException, CreateObjectException {
		CableThreadType cableThreadType = this.fromStorableObject(storableObject);
		super.insertEntity(cableThreadType);		
	}

	public void insert(Set storableObjects) throws IllegalDataException,
			CreateObjectException {
		super.insertEntities(storableObjects);
	}

}
