/*
 * $Id: PhysicalLinkTypeDatabase.java,v 1.27 2005/07/17 05:20:44 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.map;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;


/**
 * @version $Revision: 1.27 $, $Date: 2005/07/17 05:20:44 $
 * @author $Author: arseniy $
 * @module map_v1
 */
public final class PhysicalLinkTypeDatabase extends StorableObjectDatabase {
	private static String columns;
	
	private static String updateMultipleSQLValues;

	private PhysicalLinkType fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof PhysicalLinkType)
			return (PhysicalLinkType) storableObject;
		throw new IllegalDataException("PhysicalLinkTypeDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}
	
	@Override
	protected short getEntityCode() {		
		return ObjectEntities.PHYSICALLINK_TYPE_CODE;
	}	
	
	@Override
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
	
	@Override
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
	
	
	@Override
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
	
	@Override
	protected String getUpdateSingleSQLValuesTmpl(StorableObject storableObject) throws IllegalDataException {
		PhysicalLinkType physicalLinkType = fromStorableObject(storableObject);
		String values = APOSTROPHE + DatabaseString.toQuerySubString(physicalLinkType.getCodename(), SIZE_CODENAME_COLUMN) + APOSTROPHE + COMMA
			+ APOSTROPHE + DatabaseString.toQuerySubString(physicalLinkType.getName(), SIZE_NAME_COLUMN) + APOSTROPHE + COMMA
			+ APOSTROPHE + DatabaseString.toQuerySubString(physicalLinkType.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTROPHE + COMMA
			+ physicalLinkType.getBindingDimension().getWidth() + COMMA
			+ physicalLinkType.getBindingDimension().getHeight();
		return values;
	}
	
	@Override
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

	
	@Override
	public Object retrieveObject(StorableObject storableObject, int retrieveKind, Object arg) throws IllegalDataException {
		final PhysicalLinkType physicalLinkType = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				Log.errorMessage("Unknown retrieve kind: " + retrieveKind + " for " + this.getEntityName() + " '" +  physicalLinkType.getId() + "'; argument: " + arg);
				return null;
		}
	}

}
