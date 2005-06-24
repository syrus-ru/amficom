/*
 * $Id: MeasurementSetupDatabase.java,v 1.94 2005/06/24 13:54:35 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
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
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.94 $, $Date: 2005/06/24 13:54:35 $
 * @author $Author: arseniy $
 * @module measurement_v1
 */

public final class MeasurementSetupDatabase extends StorableObjectDatabase {

	public static final String  PARAMETER_TYPE_ID                   = "parameter_type_id";
	public static final int CHARACTER_NUMBER_OF_RECORDS = 1;	

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
	protected String getUpdateSingleSQLValuesTmpl(final StorableObject storableObject) throws IllegalDataException {		
		final MeasurementSetup measurementSetup = this.fromStorableObject(storableObject);
		final ParameterSet criteriaSet = measurementSetup.getCriteriaSet();
		final ParameterSet thresholdSet = measurementSetup.getThresholdSet();
		final ParameterSet etalon = measurementSetup.getEtalon();
		final String values = DatabaseIdentifier.toSQLString(measurementSetup.getParameterSet().getId()) + COMMA
			+ DatabaseIdentifier.toSQLString((criteriaSet != null) ? criteriaSet.getId() : null) + COMMA
			+ DatabaseIdentifier.toSQLString((thresholdSet != null) ? thresholdSet.getId() : null) + COMMA
			+ DatabaseIdentifier.toSQLString((etalon != null) ? etalon.getId() : null) + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(measurementSetup.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTOPHE + COMMA
			+ Long.toString(measurementSetup.getMeasurementDuration());
		return values;
	}

	@Override
	protected int setEntityForPreparedStatementTmpl(final StorableObject storableObject,
			final PreparedStatement preparedStatement,
			int startParameterNumber) throws IllegalDataException, SQLException {
		final MeasurementSetup measurementSetup = this.fromStorableObject(storableObject);
		final ParameterSet criteriaSet = measurementSetup.getCriteriaSet();
		final ParameterSet thresholdSet = measurementSetup.getThresholdSet();
		final ParameterSet etalon = measurementSetup.getEtalon();
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, measurementSetup.getParameterSet().getId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, (criteriaSet != null) ? criteriaSet.getId() : null);
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, (thresholdSet != null) ? thresholdSet.getId() : null);
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, (etalon != null) ? etalon.getId() : null);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, measurementSetup.getDescription(), SIZE_DESCRIPTION_COLUMN);
		preparedStatement.setLong(++startParameterNumber, measurementSetup.getMeasurementDuration());
		return startParameterNumber;
	}

	@Override
	protected StorableObject updateEntityFromResultSet(final StorableObject storableObject, final ResultSet resultSet)
			throws IllegalDataException, RetrieveObjectException, SQLException {
		final MeasurementSetup measurementSetup = (storableObject == null)
				? new MeasurementSetup(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID),
						null,
						0L,
						null,
						null,
						null,
						null,
						null,
						0,
						null,
						null) : this.fromStorableObject(storableObject);	
		ParameterSet parameterSet;
		ParameterSet criteriaSet;
		ParameterSet thresholdSet;
		ParameterSet etalon;
		Identifier id;
		try {
			parameterSet = (ParameterSet) StorableObjectPool.getStorableObject(DatabaseIdentifier.getIdentifier(resultSet,
					MeasurementSetupWrapper.COLUMN_PARAMETER_SET_ID), true);			
			id = DatabaseIdentifier.getIdentifier(resultSet, MeasurementSetupWrapper.COLUMN_CRITERIA_SET_ID);
			criteriaSet = (id != null) ? (ParameterSet) StorableObjectPool.getStorableObject(id, true) : null;
			id = DatabaseIdentifier.getIdentifier(resultSet, MeasurementSetupWrapper.COLUMN_THRESHOLD_SET_ID);
			thresholdSet = (id != null) ? (ParameterSet) StorableObjectPool.getStorableObject(id, true) : null;			
			id = DatabaseIdentifier.getIdentifier(resultSet, MeasurementSetupWrapper.COLUMN_ETALON_ID);
			etalon = (id != null) ? (ParameterSet) StorableObjectPool.getStorableObject(id, true) : null;
		} catch (ApplicationException ae) {
			throw new RetrieveObjectException(ae);
		}

		final String description = DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_DESCRIPTION));
		measurementSetup.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
									   DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
									   DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
									   DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
									   resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION),
									   parameterSet,
									   criteriaSet,
									   thresholdSet,
									   etalon,
									   (description != null) ? description : "",
									   resultSet.getLong(MeasurementSetupWrapper.COLUMN_MEASUREMENT_DURAION));
		return measurementSetup;
	}

	private MeasurementSetup fromStorableObject(final StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof MeasurementSetup)
			return (MeasurementSetup) storableObject;
		throw new IllegalDataException("MeasurementSetupDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	@Override
	public void retrieve(final StorableObject storableObject)
			throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		final MeasurementSetup measurementSetup = this.fromStorableObject(storableObject);
		this.retrieveEntity(measurementSetup);
		this.retrieveMeasurementSetupMELinksByOneQuery(Collections.singleton(measurementSetup));
		this.retrieveMeasurementTypeIdsByOneQuery(Collections.singleton(measurementSetup));
	}

	private void retrieveMeasurementSetupMELinksByOneQuery(final Set<MeasurementSetup> measurementSetups)
			throws RetrieveObjectException {
		if ((measurementSetups == null) || (measurementSetups.isEmpty()))
			return;

		final Map<Identifier, Set<Identifier>> meIdsMap = this.retrieveLinkedEntityIds(measurementSetups,
				ObjectEntities.MSMELINK,
				MeasurementSetupWrapper.LINK_COLUMN_MEASUREMENT_SETUP_ID,
				MeasurementSetupWrapper.LINK_COLUMN_MONITORED_ELEMENT_ID);

		for (final MeasurementSetup measurementSetup : measurementSetups) {
			final Identifier msId = measurementSetup.getId();
			final Set<Identifier> monitoredElementIds = meIdsMap.get(msId);

			measurementSetup.setMonitoredElementIds0(monitoredElementIds);
		}
	}

	private void retrieveMeasurementTypeIdsByOneQuery(final Set<MeasurementSetup> measurementSetups) throws RetrieveObjectException {
		if ((measurementSetups == null) || (measurementSetups.isEmpty()))
			return;

		final Map<Identifier, Set<Identifier>> mtIdsMap = this.retrieveLinkedEntityIds(measurementSetups,
				ObjectEntities.MSMTLINK,
				MeasurementSetupWrapper.LINK_COLUMN_MEASUREMENT_SETUP_ID,
				MeasurementSetupWrapper.LINK_COLUMN_MEASUREMENT_TYPE_ID);

		for (final MeasurementSetup measurementSetup : measurementSetups) {
			final Identifier msId = measurementSetup.getId();
			final Set<Identifier> measurementTypeIds = mtIdsMap.get(msId);

			measurementSetup.setMeasurementTypeIds0(measurementTypeIds);
		}
	}

	@Override
	public Object retrieveObject(final StorableObject storableObject, final int retrieveKind, final Object arg)
			throws IllegalDataException {
		final MeasurementSetup measurementSetup = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				Log.errorMessage("Unknown retrieve kind: " + retrieveKind + " for " + this.getEntityName() + " '" +  measurementSetup.getId() + "'; argument: " + arg);
				return null;
		}
	}

	@Override
	public void insert(final StorableObject storableObject) throws CreateObjectException, IllegalDataException {
		final MeasurementSetup measurementSetup = this.fromStorableObject(storableObject);
		super.insertEntity(measurementSetup);
		try {
			this.updateMeasurementSetupMELinks(Collections.singleton(measurementSetup));
			this.updateMeasurementTypeIds(Collections.singleton(measurementSetup));
		} catch (UpdateObjectException uoe) {
			throw new CreateObjectException(uoe);
		}
	}

	@Override
	public void insert(final Set storableObjects) throws IllegalDataException, CreateObjectException {
		super.insertEntities(storableObjects);
		try {
			this.updateMeasurementSetupMELinks(storableObjects);
			this.updateMeasurementTypeIds(storableObjects);
		} catch (UpdateObjectException uoe) {
			throw new CreateObjectException(uoe);
		}
	}

	@Override
	public void update(final StorableObject storableObject, final Identifier modifierId, final UpdateKind updateKind)
			throws VersionCollisionException, UpdateObjectException {
		super.update(storableObject, modifierId, updateKind);
		try {
			this.updateMeasurementSetupMELinks(Collections.singleton(this.fromStorableObject(storableObject)));
			this.updateMeasurementTypeIds(Collections.singleton(this.fromStorableObject(storableObject)));
		} catch (IllegalDataException ide) {
			Log.errorException(ide);
		}
	}

	@Override
	public void update(final Set storableObjects, final Identifier modifierId, final UpdateKind updateKind)
			throws VersionCollisionException, UpdateObjectException {
		super.update(storableObjects, modifierId, updateKind);
		try {
			this.updateMeasurementSetupMELinks(storableObjects);
			this.updateMeasurementTypeIds(storableObjects);
		} catch (IllegalDataException ide) {
			Log.errorException(ide);
		}
	}

	private void updateMeasurementSetupMELinks(final Set<MeasurementSetup> measurementSetups)
			throws IllegalDataException, UpdateObjectException {
		if (measurementSetups == null || measurementSetups.isEmpty())
			return;

		final Map<Identifier, Set<Identifier>> meIdsMap = new HashMap<Identifier, Set<Identifier>>();
		for (final MeasurementSetup measurementSetup : measurementSetups) {
			final Set<Identifier> meIds = measurementSetup.getMonitoredElementIds();
			meIdsMap.put(measurementSetup.getId(), meIds);
		}

		super.updateLinkedEntityIds(meIdsMap,
				ObjectEntities.MSMELINK,
				MeasurementSetupWrapper.LINK_COLUMN_MEASUREMENT_SETUP_ID,
				MeasurementSetupWrapper.LINK_COLUMN_MONITORED_ELEMENT_ID);
	}

	private void updateMeasurementTypeIds(final Set<MeasurementSetup> measurementSetups)
			throws IllegalDataException, UpdateObjectException {
		if (measurementSetups == null || measurementSetups.isEmpty())
			return;

		final Map<Identifier, Set<Identifier>> mtIdsMap = new HashMap<Identifier, Set<Identifier>>();
		for (final MeasurementSetup measurementSetup : measurementSetups) {
			final Set<Identifier> mtIds = measurementSetup.getMeasurementTypeIds();
			mtIdsMap.put(measurementSetup.getId(), mtIds);
		}

		super.updateLinkedEntityIds(mtIdsMap,
				ObjectEntities.MSMTLINK,
				MeasurementSetupWrapper.LINK_COLUMN_MEASUREMENT_SETUP_ID,
				MeasurementSetupWrapper.LINK_COLUMN_MEASUREMENT_TYPE_ID);
	}

	@Override
	protected Set retrieveByCondition(final String conditionQuery) throws RetrieveObjectException, IllegalDataException {
		final Set set = super.retrieveByCondition(conditionQuery);
		this.retrieveMeasurementSetupMELinksByOneQuery(set);
		this.retrieveMeasurementTypeIdsByOneQuery(set);
		return set;
	}

}
