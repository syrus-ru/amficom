/*
 * $Id: MeasurementPortTypeDatabase.java,v 1.10 2005/11/30 14:55:26 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import static com.syrus.AMFICOM.general.TableNames.MNTPORTTYPMNTTYPLINK;
import static com.syrus.AMFICOM.measurement.MeasurementPortTypeWrapper.LINK_COLUMN_MEASUREMENT_PORT_TYPE_ID;
import static com.syrus.AMFICOM.measurement.MeasurementPortTypeWrapper.LINK_COLUMN_MEASUREMENT_TYPE_CODE;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.10 $, $Date: 2005/11/30 14:55:26 $
 * @author $Author: bass $
 * @author Tashoyan Arseniy Feliksovich
 * @module measurement
 */
public final class MeasurementPortTypeDatabase extends StorableObjectDatabase<MeasurementPortType> {
	private static String columns;
	private static String updateMultipleSQLValues;

	@Override
	protected short getEntityCode() {
		return ObjectEntities.MEASUREMENTPORT_TYPE_CODE;
	}

	@Override
	protected String getColumnsTmpl() {
		if (columns == null) {
			columns = StorableObjectWrapper.COLUMN_CODENAME + COMMA
				+ StorableObjectWrapper.COLUMN_DESCRIPTION + COMMA
				+ StorableObjectWrapper.COLUMN_NAME;
		}
		return columns;
	}

	@Override
	protected String getUpdateMultipleSQLValuesTmpl() {
		if (updateMultipleSQLValues == null) {
    	updateMultipleSQLValues = QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION;
    	}
		return updateMultipleSQLValues;
	}

	@Override
	protected String getUpdateSingleSQLValuesTmpl(final MeasurementPortType storableObject) throws IllegalDataException {
		final String sql = APOSTROPHE + DatabaseString.toQuerySubString(storableObject.getCodename(), SIZE_CODENAME_COLUMN) + APOSTROPHE + COMMA
			+ APOSTROPHE + DatabaseString.toQuerySubString(storableObject.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTROPHE + COMMA
			+ APOSTROPHE + DatabaseString.toQuerySubString(storableObject.getName(), SIZE_NAME_COLUMN) + APOSTROPHE;
		return sql;
	}

	@Override
	protected int setEntityForPreparedStatementTmpl(final MeasurementPortType storableObject,
			final PreparedStatement preparedStatement,
			int startParameterNumber) throws IllegalDataException, SQLException {
		DatabaseString.setString(preparedStatement, ++startParameterNumber, storableObject.getCodename(), SIZE_CODENAME_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, storableObject.getDescription(), SIZE_DESCRIPTION_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, storableObject.getName(), SIZE_NAME_COLUMN);
		return startParameterNumber;
	}

	@Override
	protected MeasurementPortType updateEntityFromResultSet(final MeasurementPortType storableObject, final ResultSet resultSet)
			throws IllegalDataException, SQLException {
		final MeasurementPortType measurementPortType = (storableObject == null)
				? new MeasurementPortType(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID),
						null,
						StorableObjectVersion.ILLEGAL_VERSION,
						null,
						null,
						null,
						null)
				: storableObject;
		measurementPortType.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
				new StorableObjectVersion(resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION)),
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_CODENAME)),
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_DESCRIPTION)),
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_NAME)));
		return measurementPortType;
	}

	private void retrieveMeasurementTypesByOneQuery(final Set<MeasurementPortType> measurementPortTypes) throws RetrieveObjectException {
		if (measurementPortTypes == null || measurementPortTypes.isEmpty()) {
			return;
		}

		final Map<Identifier, EnumSet<MeasurementType>> dbMeasurementTypesMap = super.retrieveLinkedEnums(measurementPortTypes,
				MeasurementType.class,
				MNTPORTTYPMNTTYPLINK,
				LINK_COLUMN_MEASUREMENT_PORT_TYPE_ID,
				LINK_COLUMN_MEASUREMENT_TYPE_CODE);
		for (final MeasurementPortType measurementPortType : measurementPortTypes) {
			final Identifier measurementPortTypeId = measurementPortType.getId();
			final EnumSet<MeasurementType> measurementTypes = dbMeasurementTypesMap.get(measurementPortTypeId);

			measurementPortType.setMeasurementTypes0(measurementTypes);
		}
	}

	@Override
	protected void insert(final Set<MeasurementPortType> storableObjects) throws CreateObjectException, IllegalDataException {
		super.insert(storableObjects);

		final Map<Identifier, EnumSet<MeasurementType>> measurementTypesMap = createMeasurementTypesMap(storableObjects);
		super.insertLinkedEnums(measurementTypesMap,
				MNTPORTTYPMNTTYPLINK,
				LINK_COLUMN_MEASUREMENT_PORT_TYPE_ID,
				LINK_COLUMN_MEASUREMENT_TYPE_CODE);
	}

	@Override
	protected void update(final Set<MeasurementPortType> storableObjects) throws UpdateObjectException {
		super.update(storableObjects);

		final Map<Identifier, EnumSet<MeasurementType>> measurementTypesMap = createMeasurementTypesMap(storableObjects);
		super.updateLinkedEnums(measurementTypesMap,
				MeasurementType.class,
				MNTPORTTYPMNTTYPLINK,
				LINK_COLUMN_MEASUREMENT_PORT_TYPE_ID,
				LINK_COLUMN_MEASUREMENT_TYPE_CODE);
	}

	private static Map<Identifier, EnumSet<MeasurementType>> createMeasurementTypesMap(final Set<MeasurementPortType> measurementPortTypes) {
		final Map<Identifier, EnumSet<MeasurementType>> measurementTypesMap = new HashMap<Identifier, EnumSet<MeasurementType>>();
		for (final MeasurementPortType measurementPortType : measurementPortTypes) {
			measurementTypesMap.put(measurementPortType.getId(), measurementPortType.getMeasurementTypes());
		}
		return measurementTypesMap;
	}

	@Override
	protected Set<MeasurementPortType> retrieveByCondition(final String conditionQuery)
			throws RetrieveObjectException,
				IllegalDataException {
		final Set<MeasurementPortType> objects = super.retrieveByCondition(conditionQuery);
		this.retrieveMeasurementTypesByOneQuery(objects);
		return objects;
	}

}
