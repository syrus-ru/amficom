/*
 * $Id: MeasurementSetupDatabase.java,v 1.89 2005/05/26 14:15:57 arseniy Exp $
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
import java.util.Iterator;
import java.util.Map;

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
 * @version $Revision: 1.89 $, $Date: 2005/05/26 14:15:57 $
 * @author $Author: arseniy $
 * @module measurement_v1
 */

public final class MeasurementSetupDatabase extends StorableObjectDatabase {

	public static final String  PARAMETER_TYPE_ID                   = "parameter_type_id";
	public static final int CHARACTER_NUMBER_OF_RECORDS = 1;	

	private static String columns;	
	private static String updateMultipleSQLValues;	

	protected short getEntityCode() {		
		return ObjectEntities.MEASUREMENT_SETUP_ENTITY_CODE;
	}	

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

	protected String getUpdateMultipleSQLValuesTmpl() {
		if (updateMultipleSQLValues == null){			
			updateMultipleSQLValues = QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION;
		}
		
		return updateMultipleSQLValues;
	}	

	protected String getUpdateSingleSQLValuesTmpl(StorableObject storableObject) throws IllegalDataException {		
		MeasurementSetup measurementSetup = this.fromStorableObject(storableObject);
		Set criteriaSet = measurementSetup.getCriteriaSet();
		Set thresholdSet = measurementSetup.getThresholdSet();
		Set etalon = measurementSetup.getEtalon();
		String values = DatabaseIdentifier.toSQLString(measurementSetup.getParameterSet().getId()) + COMMA
			+ DatabaseIdentifier.toSQLString((criteriaSet != null) ? criteriaSet.getId() : null) + COMMA
			+ DatabaseIdentifier.toSQLString((thresholdSet != null) ? thresholdSet.getId() : null) + COMMA
			+ DatabaseIdentifier.toSQLString((etalon != null) ? etalon.getId() : null) + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(measurementSetup.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTOPHE + COMMA
			+ Long.toString(measurementSetup.getMeasurementDuration());
		return values;
	}

	protected int  setEntityForPreparedStatementTmpl(StorableObject storableObject, PreparedStatement preparedStatement, int startParameterNumber)
			throws IllegalDataException, SQLException {
		MeasurementSetup measurementSetup = this.fromStorableObject(storableObject);
		Set criteriaSet = measurementSetup.getCriteriaSet();
		Set thresholdSet = measurementSetup.getThresholdSet();
		Set etalon = measurementSetup.getEtalon();
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, measurementSetup.getParameterSet().getId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, (criteriaSet != null) ? criteriaSet.getId() : null);
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, (thresholdSet != null) ? thresholdSet.getId() : null);
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, (etalon != null) ? etalon.getId() : null);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, measurementSetup.getDescription(), SIZE_DESCRIPTION_COLUMN);
		preparedStatement.setLong(++startParameterNumber, measurementSetup.getMeasurementDuration());
		return startParameterNumber;
	}

	protected StorableObject updateEntityFromResultSet(StorableObject storableObject, ResultSet resultSet)
			throws IllegalDataException, RetrieveObjectException, SQLException {
		MeasurementSetup measurementSetup = (storableObject == null)
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
		Set parameterSet;
		Set criteriaSet;
		Set thresholdSet;
		Set etalon;
		Identifier id;
		try {
			parameterSet = (Set) StorableObjectPool.getStorableObject(DatabaseIdentifier.getIdentifier(resultSet,
					MeasurementSetupWrapper.COLUMN_PARAMETER_SET_ID), true);			
			id = DatabaseIdentifier.getIdentifier(resultSet, MeasurementSetupWrapper.COLUMN_CRITERIA_SET_ID);
			criteriaSet = (id != null) ? (Set) StorableObjectPool.getStorableObject(id, true) : null;
			id = DatabaseIdentifier.getIdentifier(resultSet, MeasurementSetupWrapper.COLUMN_THRESHOLD_SET_ID);
			thresholdSet = (id != null) ? (Set) StorableObjectPool.getStorableObject(id, true) : null;			
			id = DatabaseIdentifier.getIdentifier(resultSet, MeasurementSetupWrapper.COLUMN_ETALON_ID);
			etalon = (id != null) ? (Set) StorableObjectPool.getStorableObject(id, true) : null;
		}
		catch (ApplicationException ae) {
			throw new RetrieveObjectException(ae);
		}

		String description = DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_DESCRIPTION));
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

	private MeasurementSetup fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof MeasurementSetup)
			return (MeasurementSetup) storableObject;
		throw new IllegalDataException("MeasurementSetupDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		MeasurementSetup measurementSetup = this.fromStorableObject(storableObject);
		this.retrieveEntity(measurementSetup);
		this.retrieveMeasurementSetupMELinksByOneQuery(Collections.singleton(measurementSetup));
		this.retrieveMeasurementTypeIdsByOneQuery(Collections.singleton(measurementSetup));
	}

	private void retrieveMeasurementSetupMELinksByOneQuery(java.util.Set measurementSetups) throws RetrieveObjectException {
		if ((measurementSetups == null) || (measurementSetups.isEmpty()))
			return;

		Map meIdsMap = this.retrieveLinkedEntityIds(measurementSetups,
				ObjectEntities.MSMELINK_ENTITY,
				MeasurementSetupWrapper.LINK_COLUMN_MEASUREMENT_SETUP_ID,
				MeasurementSetupWrapper.LINK_COLUMN_MONITORED_ELEMENT_ID);

		MeasurementSetup measurementSetup;
		Identifier msId;
		java.util.Set monitoredElementIds;
		for (Iterator it = measurementSetups.iterator(); it.hasNext();) {
			measurementSetup = (MeasurementSetup) it.next();
			msId = measurementSetup.getId();
			monitoredElementIds = (java.util.Set) meIdsMap.get(msId);

			measurementSetup.setMonitoredElementIds0(monitoredElementIds);
		}
	}

	private void retrieveMeasurementTypeIdsByOneQuery(java.util.Set measurementSetups) throws RetrieveObjectException {
		if ((measurementSetups == null) || (measurementSetups.isEmpty()))
			return;

		Map mtIdsMap = this.retrieveLinkedEntityIds(measurementSetups,
				ObjectEntities.MSMTLINK_ENTITY,
				MeasurementSetupWrapper.LINK_COLUMN_MEASUREMENT_SETUP_ID,
				MeasurementSetupWrapper.LINK_COLUMN_MEASUREMENT_TYPE_ID);

		MeasurementSetup measurementSetup;
		Identifier msId;
		java.util.Set measurementTypeIds;
		for (Iterator it = measurementSetups.iterator(); it.hasNext();) {
			measurementSetup = (MeasurementSetup) it.next();
			msId = measurementSetup.getId();
			measurementTypeIds = (java.util.Set) mtIdsMap.get(msId);

			measurementSetup.setMeasurementTypeIds0(measurementTypeIds);
		}
	}

	public Object retrieveObject(StorableObject storableObject, int retrieveKind, Object arg) throws IllegalDataException {
		MeasurementSetup measurementSetup = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				Log.errorMessage("Unknown retrieve kind: " + retrieveKind + " for " + this.getEntityName() + " '" +  measurementSetup.getId() + "'; argument: " + arg);
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws CreateObjectException, IllegalDataException {
		MeasurementSetup measurementSetup = this.fromStorableObject(storableObject);
		super.insertEntity(measurementSetup);
		try {
			this.updateMeasurementSetupMELinks(Collections.singleton(measurementSetup));
			this.updateMeasurementTypeIds(Collections.singleton(measurementSetup));
		}
		catch (UpdateObjectException uoe) {
			throw new CreateObjectException(uoe);
		}
	}

	public void insert(java.util.Set storableObjects) throws IllegalDataException, CreateObjectException {
		super.insertEntities(storableObjects);
		try {
			this.updateMeasurementSetupMELinks(storableObjects);
			this.updateMeasurementTypeIds(storableObjects);
		}
		catch (UpdateObjectException uoe) {
			throw new CreateObjectException(uoe);
		}
	}

	public void update(StorableObject storableObject, Identifier modifierId, int updateKind)
			throws VersionCollisionException, UpdateObjectException {
		super.update(storableObject, modifierId, updateKind);
		try {
			this.updateMeasurementSetupMELinks(Collections.singleton(storableObject));
			this.updateMeasurementTypeIds(Collections.singleton(storableObject));
		}
		catch (IllegalDataException ide) {
			Log.errorException(ide);
		}
	}

	public void update(java.util.Set storableObjects, Identifier modifierId, int updateKind)
			throws VersionCollisionException, UpdateObjectException {
		super.update(storableObjects, modifierId, updateKind);
		try {
			this.updateMeasurementSetupMELinks(storableObjects);
			this.updateMeasurementTypeIds(storableObjects);
		}
		catch (IllegalDataException ide) {
			Log.errorException(ide);
		}
	}

	private void updateMeasurementSetupMELinks(java.util.Set measurementSetups) throws IllegalDataException, UpdateObjectException {
		if (measurementSetups == null || measurementSetups.isEmpty())
			return;

		Map meIdsMap = new HashMap();
		MeasurementSetup measurementSetup;
		java.util.Set meIds;
		for (Iterator it = measurementSetups.iterator(); it.hasNext();) {
			measurementSetup = this.fromStorableObject((StorableObject) it.next());
			meIds = measurementSetup.getMonitoredElementIds();
			meIdsMap.put(measurementSetup.getId(), meIds);
		}

		super.updateLinkedEntityIds(meIdsMap,
				ObjectEntities.MSMELINK_ENTITY,
				MeasurementSetupWrapper.LINK_COLUMN_MEASUREMENT_SETUP_ID,
				MeasurementSetupWrapper.LINK_COLUMN_MONITORED_ELEMENT_ID);
	}

	private void updateMeasurementTypeIds(java.util.Set measurementSetups) throws IllegalDataException, UpdateObjectException {
		if (measurementSetups == null || measurementSetups.isEmpty())
			return;

		Map mtIdsMap = new HashMap();
		MeasurementSetup measurementSetup;
		java.util.Set mtIds;
		for (Iterator it = measurementSetups.iterator(); it.hasNext();) {
			measurementSetup = this.fromStorableObject((StorableObject) it.next());
			mtIds = measurementSetup.getMeasurementTypeIds();
			mtIdsMap.put(measurementSetup.getId(), mtIds);
		}

		super.updateLinkedEntityIds(mtIdsMap,
				ObjectEntities.MSMTLINK_ENTITY,
				MeasurementSetupWrapper.LINK_COLUMN_MEASUREMENT_SETUP_ID,
				MeasurementSetupWrapper.LINK_COLUMN_MEASUREMENT_TYPE_ID);
	}

	protected java.util.Set retrieveByCondition(String conditionQuery) throws RetrieveObjectException, IllegalDataException {
		java.util.Set set = super.retrieveByCondition(conditionQuery);
		this.retrieveMeasurementSetupMELinksByOneQuery(set);
		this.retrieveMeasurementTypeIdsByOneQuery(set);
		return set;
	}

}
