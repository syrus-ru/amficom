/*
 * $Id: AnalysisDatabase.java,v 1.61 2005/06/17 12:38:55 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.61 $, $Date: 2005/06/17 12:38:55 $
 * @author $Author: bass $
 * @module measurement_v1
 */

public final class AnalysisDatabase extends StorableObjectDatabase {

	private static String columns;

	private static String updateMultipleSQLValues;

	private Analysis fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof Analysis)
			return (Analysis) storableObject;
		throw new IllegalDataException("AnalysisDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		Analysis analysis = this.fromStorableObject(storableObject);
		this.retrieveEntity(analysis);
	}	

	protected short getEntityCode() {		
		return ObjectEntities.ANALYSIS_CODE;
	}	

	protected String getColumnsTmpl() {
		if (columns == null) {
			columns = StorableObjectWrapper.COLUMN_TYPE_ID + COMMA
					+ AnalysisWrapper.COLUMN_MONITORED_ELEMENT_ID + COMMA
					+ AnalysisWrapper.COLUMN_MEASUREMENT_ID + COMMA
					+ StorableObjectWrapper.COLUMN_NAME + COMMA
					+ AnalysisWrapper.COLUMN_CRITERIA_SET_ID;
		}
		return columns;
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

	protected String getUpdateSingleSQLValuesTmpl(StorableObject storableObject) throws IllegalDataException {
		Analysis analysis = this.fromStorableObject(storableObject);
		Measurement measurement = analysis.getMeasurement();
		String values = DatabaseIdentifier.toSQLString(analysis.getType().getId()) + COMMA
			+ DatabaseIdentifier.toSQLString(analysis.getMonitoredElementId()) + COMMA
			+ ((measurement != null) ? (DatabaseIdentifier.toSQLString(measurement.getId())) : "") + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(analysis.getName(), SIZE_NAME_COLUMN) + APOSTOPHE + COMMA
			+ DatabaseIdentifier.toSQLString(analysis.getCriteriaSet().getId());
		return values;
	}

	protected int setEntityForPreparedStatementTmpl(StorableObject storableObject, PreparedStatement preparedStatement, int startParameterNumber)
			throws IllegalDataException, SQLException {
		Analysis analysis = this.fromStorableObject(storableObject);
		Measurement measurement = analysis.getMeasurement();
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, analysis.getType().getId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, analysis.getMonitoredElementId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, (measurement != null) ? measurement.getId() : null);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, analysis.getName(), SIZE_NAME_COLUMN);
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, analysis.getCriteriaSet().getId());
		return startParameterNumber;
	}

	protected StorableObject updateEntityFromResultSet(StorableObject storableObject, ResultSet resultSet)
			throws IllegalDataException, RetrieveObjectException, SQLException {
		Analysis analysis = (storableObject == null) ?
				new Analysis(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID),
								null,
								0L,
								null,
								null,
								null,
								null,
								null) :
					this.fromStorableObject(storableObject);
		AnalysisType analysisType;
		Measurement measurement = null;
		ParameterSet criteriaSet;
		try {
			analysisType = (AnalysisType) StorableObjectPool.getStorableObject(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_TYPE_ID), true);
			Identifier measurementId = DatabaseIdentifier.getIdentifier(resultSet, AnalysisWrapper.COLUMN_MEASUREMENT_ID);
			if (measurementId != null)
				measurement = (Measurement) StorableObjectPool.getStorableObject(measurementId, true);
			criteriaSet = (ParameterSet) StorableObjectPool.getStorableObject(DatabaseIdentifier.getIdentifier(resultSet, AnalysisWrapper.COLUMN_CRITERIA_SET_ID), true);
		} catch (ApplicationException ae) {
			throw new RetrieveObjectException(ae);
		}
		analysis.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
							   DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
							   DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
							   DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
							   resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION),
							   analysisType,
							   DatabaseIdentifier.getIdentifier(resultSet, AnalysisWrapper.COLUMN_MONITORED_ELEMENT_ID),
							   measurement,
							   DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_NAME)),
							   criteriaSet);
		return analysis;
	}

	public Object retrieveObject(StorableObject storableObject, int retrieveKind, Object arg) throws IllegalDataException {
		Analysis analysis = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				Log.errorMessage("Unknown retrieve kind: " + retrieveKind + " for " + this.getEntityName() + " '" +  analysis.getId() + "'; argument: " + arg);
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws CreateObjectException , IllegalDataException {
		Analysis analysis = this.fromStorableObject(storableObject);
		super.insertEntity(analysis);
	}

	public void insert(java.util.Set storableObjects) throws IllegalDataException, CreateObjectException {
		super.insertEntities(storableObjects);
	}

}
