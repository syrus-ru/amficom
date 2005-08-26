/*
 * $Id: MeasurementSetupDatabase.java,v 1.105 2005/08/26 18:52:26 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import static com.syrus.AMFICOM.general.Identifier.VOID_IDENTIFIER;
import static com.syrus.AMFICOM.general.ObjectEntities.MSMELINK;
import static com.syrus.AMFICOM.general.ObjectEntities.MSMTLINK;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.105 $, $Date: 2005/08/26 18:52:26 $
 * @author $Author: arseniy $
 * @module measurement
 */

public final class MeasurementSetupDatabase extends StorableObjectDatabase<MeasurementSetup> {
	private static String columns;	
	private static String updateMultipleSQLValues;	

	@Override
	protected short getEntityCode() {		
		return ObjectEntities.MEASUREMENTSETUP_CODE;
	}

	@Override
	protected String getColumnsTmpl() {
		if (columns == null) {
			columns = MeasurementSetupWrapper.COLUMN_PARAMETER_SET_ID + COMMA
				+ MeasurementSetupWrapper.COLUMN_CRITERIA_SET_ID + COMMA
				+ MeasurementSetupWrapper.COLUMN_THRESHOLD_SET_ID + COMMA
				+ MeasurementSetupWrapper.COLUMN_ETALON_ID + COMMA
				+ StorableObjectWrapper.COLUMN_DESCRIPTION + COMMA
				+ MeasurementSetupWrapper.COLUMN_MEASUREMENT_DURAION;
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
				+ QUESTION + COMMA
				+ QUESTION;
		}
		return updateMultipleSQLValues;
	}	

	@Override
	protected String getUpdateSingleSQLValuesTmpl(final MeasurementSetup storableObject) throws IllegalDataException {		
		final ParameterSet criteriaSet = storableObject.getCriteriaSet();
		final ParameterSet thresholdSet = storableObject.getThresholdSet();
		final ParameterSet etalon = storableObject.getEtalon();
		final String values = DatabaseIdentifier.toSQLString(storableObject.getParameterSet().getId()) + COMMA
			+ DatabaseIdentifier.toSQLString((criteriaSet != null) ? criteriaSet.getId() : VOID_IDENTIFIER) + COMMA
			+ DatabaseIdentifier.toSQLString((thresholdSet != null) ? thresholdSet.getId() : VOID_IDENTIFIER) + COMMA
			+ DatabaseIdentifier.toSQLString((etalon != null) ? etalon.getId() : VOID_IDENTIFIER) + COMMA
			+ APOSTROPHE + DatabaseString.toQuerySubString(storableObject.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTROPHE + COMMA
			+ Long.toString(storableObject.getMeasurementDuration());
		return values;
	}

	@Override
	protected int setEntityForPreparedStatementTmpl(final MeasurementSetup storableObject,
			final PreparedStatement preparedStatement,
			int startParameterNumber) throws IllegalDataException, SQLException {
		final ParameterSet criteriaSet = storableObject.getCriteriaSet();
		final ParameterSet thresholdSet = storableObject.getThresholdSet();
		final ParameterSet etalon = storableObject.getEtalon();
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, storableObject.getParameterSet().getId());
		DatabaseIdentifier.setIdentifier(preparedStatement,
				++startParameterNumber,
				(criteriaSet != null) ? criteriaSet.getId() : VOID_IDENTIFIER);
		DatabaseIdentifier.setIdentifier(preparedStatement,
				++startParameterNumber,
				(thresholdSet != null) ? thresholdSet.getId() : VOID_IDENTIFIER);
		DatabaseIdentifier.setIdentifier(preparedStatement,
				++startParameterNumber,
				(etalon != null) ? etalon.getId() : VOID_IDENTIFIER);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, storableObject.getDescription(), SIZE_DESCRIPTION_COLUMN);
		preparedStatement.setLong(++startParameterNumber, storableObject.getMeasurementDuration());
		return startParameterNumber;
	}

	@Override
	protected MeasurementSetup updateEntityFromResultSet(final MeasurementSetup storableObject, final ResultSet resultSet)
			throws IllegalDataException, RetrieveObjectException, SQLException {
		final MeasurementSetup measurementSetup = (storableObject == null)
				? new MeasurementSetup(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID),
						null,
						StorableObjectVersion.ILLEGAL_VERSION,
						null,
						null,
						null,
						null,
						null,
						0,
						null,
						null)
					: storableObject;	
		ParameterSet parameterSet;
		ParameterSet criteriaSet = null;
		ParameterSet thresholdSet = null;
		ParameterSet etalon = null;
		try {
			Identifier id = DatabaseIdentifier.getIdentifier(resultSet, MeasurementSetupWrapper.COLUMN_PARAMETER_SET_ID);
			parameterSet = (ParameterSet) StorableObjectPool.getStorableObject(id, true);
			id = DatabaseIdentifier.getIdentifier(resultSet, MeasurementSetupWrapper.COLUMN_CRITERIA_SET_ID);
			if (id != VOID_IDENTIFIER) {
				criteriaSet = StorableObjectPool.getStorableObject(id, true);
			}
			id = DatabaseIdentifier.getIdentifier(resultSet, MeasurementSetupWrapper.COLUMN_THRESHOLD_SET_ID);
			if (id != VOID_IDENTIFIER) {
				thresholdSet = StorableObjectPool.getStorableObject(id, true);
			}			
			id = DatabaseIdentifier.getIdentifier(resultSet, MeasurementSetupWrapper.COLUMN_ETALON_ID);
			if (id != VOID_IDENTIFIER) {
				etalon = StorableObjectPool.getStorableObject(id, true);
			}
		} catch (ApplicationException ae) {
			throw new RetrieveObjectException(ae);
		}

		final String description = DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_DESCRIPTION));
		measurementSetup.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
				new StorableObjectVersion(resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION)),
				parameterSet,
				criteriaSet,
				thresholdSet,
				etalon,
				(description != null) ? description : "",
				resultSet.getLong(MeasurementSetupWrapper.COLUMN_MEASUREMENT_DURAION));
		return measurementSetup;
	}

	@Override
	public void retrieve(final MeasurementSetup storableObject)
			throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		super.retrieveEntity(storableObject);
		this.retrieveMeasurementSetupMELinksByOneQuery(Collections.singleton(storableObject));
		this.retrieveMeasurementTypesByOneQuery(Collections.singleton(storableObject));
	}

	private void retrieveMeasurementSetupMELinksByOneQuery(final Set<MeasurementSetup> measurementSetups)
			throws RetrieveObjectException {
		if ((measurementSetups == null) || (measurementSetups.isEmpty())) {
			return;
		}

		final Map<Identifier, Set<Identifier>> meIdsMap = this.retrieveLinkedEntityIds(measurementSetups,
				MSMELINK,
				MeasurementSetupWrapper.LINK_COLUMN_MEASUREMENT_SETUP_ID,
				MeasurementSetupWrapper.LINK_COLUMN_MONITORED_ELEMENT_ID);

		for (final MeasurementSetup measurementSetup : measurementSetups) {
			final Identifier msId = measurementSetup.getId();
			final Set<Identifier> monitoredElementIds = meIdsMap.get(msId);

			measurementSetup.setMonitoredElementIds0(monitoredElementIds);
		}
	}

	private void retrieveMeasurementTypesByOneQuery(final Set<MeasurementSetup> measurementSetups) throws RetrieveObjectException {
		if ((measurementSetups == null) || (measurementSetups.isEmpty())) {
			return;
		}

		final Map<Identifier, EnumSet<MeasurementType>> mtMap = super.retrieveLinkedEnums(measurementSetups,
				MeasurementType.class,
				MSMTLINK,
				MeasurementSetupWrapper.LINK_COLUMN_MEASUREMENT_SETUP_ID,
				MeasurementSetupWrapper.LINK_COLUMN_MEASUREMENT_TYPE_CODE);
		for (final MeasurementSetup measurementSetup : measurementSetups) {
			final Identifier msId = measurementSetup.getId();
			final EnumSet<MeasurementType> measurementTypes = mtMap.get(msId);

			measurementSetup.setMeasurementTypes0(measurementTypes);
		}
	}

	@Override
	public void insert(final Set<MeasurementSetup> storableObjects) throws IllegalDataException, CreateObjectException {
		super.insertEntities(storableObjects);

		final Map<Identifier, Set<Identifier>> meIdsMap = createMonitoredElementIdsMap(storableObjects);
		super.insertLinkedEntityIds(meIdsMap,
				MSMELINK,
				MeasurementSetupWrapper.LINK_COLUMN_MEASUREMENT_SETUP_ID,
				MeasurementSetupWrapper.LINK_COLUMN_MONITORED_ELEMENT_ID);

		final Map<Identifier, EnumSet<MeasurementType>> mtMap = createMeasurementTypesMap(storableObjects);
		super.insertLinkedEnums(mtMap,
				MSMTLINK,
				MeasurementSetupWrapper.LINK_COLUMN_MEASUREMENT_SETUP_ID,
				MeasurementSetupWrapper.LINK_COLUMN_MEASUREMENT_TYPE_CODE);
	}

	@Override
	public void update(final Set<MeasurementSetup> storableObjects) throws UpdateObjectException {
		super.updateEntities(storableObjects);

		final Map<Identifier, Set<Identifier>> meIdsMap = createMonitoredElementIdsMap(storableObjects);
		super.updateLinkedEntityIds(meIdsMap,
				MSMELINK,
				MeasurementSetupWrapper.LINK_COLUMN_MEASUREMENT_SETUP_ID,
				MeasurementSetupWrapper.LINK_COLUMN_MONITORED_ELEMENT_ID);

		final Map<Identifier, EnumSet<MeasurementType>> mtMap = createMeasurementTypesMap(storableObjects);
		super.updateLinkedEnums(mtMap,
				MeasurementType.class,
				MSMTLINK,
				MeasurementSetupWrapper.LINK_COLUMN_MEASUREMENT_SETUP_ID,
				MeasurementSetupWrapper.LINK_COLUMN_MEASUREMENT_TYPE_CODE);
	}

	private static Map<Identifier, Set<Identifier>> createMonitoredElementIdsMap(final Set<MeasurementSetup> measurementSetups) {
		final Map<Identifier, Set<Identifier>> meIdsMap = new HashMap<Identifier, Set<Identifier>>();
		for (final MeasurementSetup measurementSetup : measurementSetups) {
			meIdsMap.put(measurementSetup.getId(), measurementSetup.getMonitoredElementIds());
		}
		return meIdsMap;
	}

	private static Map<Identifier, EnumSet<MeasurementType>> createMeasurementTypesMap(final Set<MeasurementSetup> measurementSetups) {
		final Map<Identifier, EnumSet<MeasurementType>> measurementTypesMap = new HashMap<Identifier, EnumSet<MeasurementType>>();
		for (final MeasurementSetup measurementSetup : measurementSetups) {
			measurementTypesMap.put(measurementSetup.getId(), measurementSetup.getMeasurementTypes());
		}
		return measurementTypesMap;
	}

	@Override
	protected Set<MeasurementSetup> retrieveByCondition(final String conditionQuery)
			throws RetrieveObjectException, IllegalDataException {
		final Set<MeasurementSetup> objects = super.retrieveByCondition(conditionQuery);
		this.retrieveMeasurementSetupMELinksByOneQuery(objects);
		this.retrieveMeasurementTypesByOneQuery(objects);
		return objects;
	}

}
