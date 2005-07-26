/*
 * $Id: PhysicalLinkTypeDatabase.java,v 1.29 2005/07/26 11:41:05 arseniy Exp $
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
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;


/**
 * @version $Revision: 1.29 $, $Date: 2005/07/26 11:41:05 $
 * @author $Author: arseniy $
 * @module map_v1
 */
public final class PhysicalLinkTypeDatabase extends StorableObjectDatabase {
	private static String columns;
	
	private static String updateMultipleSQLValues;

	private PhysicalLinkType fromStorableObject(final StorableObject storableObject) throws IllegalDataException {
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
		if (columns == null) {
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
		if (updateMultipleSQLValues == null) {
			updateMultipleSQLValues = QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA				
				+ QUESTION;
		}
		return updateMultipleSQLValues;
	}
	
	
	@Override
	protected int setEntityForPreparedStatementTmpl(final StorableObject storableObject,
			final PreparedStatement preparedStatement,
			int startParameterNumber) throws IllegalDataException, SQLException {
		final PhysicalLinkType physicalLinkType = this.fromStorableObject(storableObject);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, physicalLinkType.getCodename(), SIZE_CODENAME_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, physicalLinkType.getName(), SIZE_NAME_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, physicalLinkType.getDescription(), SIZE_DESCRIPTION_COLUMN);
		preparedStatement.setInt(++startParameterNumber, physicalLinkType.getBindingDimension().getWidth());
		preparedStatement.setInt(++startParameterNumber, physicalLinkType.getBindingDimension().getHeight());		
		return startParameterNumber;
	}
	
	@Override
	protected String getUpdateSingleSQLValuesTmpl(final StorableObject storableObject) throws IllegalDataException {
		final PhysicalLinkType physicalLinkType = this.fromStorableObject(storableObject);
		final String values = APOSTROPHE + DatabaseString.toQuerySubString(physicalLinkType.getCodename(), SIZE_CODENAME_COLUMN) + APOSTROPHE + COMMA
			+ APOSTROPHE + DatabaseString.toQuerySubString(physicalLinkType.getName(), SIZE_NAME_COLUMN) + APOSTROPHE + COMMA
			+ APOSTROPHE + DatabaseString.toQuerySubString(physicalLinkType.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTROPHE + COMMA
			+ physicalLinkType.getBindingDimension().getWidth() + COMMA
			+ physicalLinkType.getBindingDimension().getHeight();
		return values;
	}
	
	@Override
	protected StorableObject updateEntityFromResultSet(final StorableObject storableObject, final ResultSet resultSet)
			throws IllegalDataException,
				SQLException {
		final PhysicalLinkType physicalLinkType = (storableObject == null)
				? new PhysicalLinkType(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID),
						null,
						StorableObjectVersion.ILLEGAL_VERSION,
						null,
						null,
						null,
						null,
						null)
					: this.fromStorableObject(storableObject);
		physicalLinkType.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
				new StorableObjectVersion(resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION)),
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_CODENAME)),
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_NAME)),
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_DESCRIPTION)),
				resultSet.getInt(PhysicalLinkTypeWrapper.COLUMN_DIMENSION_X),
				resultSet.getInt(PhysicalLinkTypeWrapper.COLUMN_DIMENSION_Y));
		return physicalLinkType;
	}

}
