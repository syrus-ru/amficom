/*
 * $Id: AnalysisDatabase.java,v 1.37 2005/02/03 14:57:22 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.syrus.AMFICOM.administration.Domain;
import com.syrus.AMFICOM.administration.DomainMember;
import com.syrus.AMFICOM.administration.DomainCondition;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseDate;

/**
 * @version $Revision: 1.37 $, $Date: 2005/02/03 14:57:22 $
 * @author $Author: arseniy $
 * @module measurement_v1
 */

public class AnalysisDatabase extends StorableObjectDatabase {

	private static String columns;

	private static String updateMultiplySQLValues;

	private Analysis fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof Analysis)
			return (Analysis) storableObject;
		throw new IllegalDataException("AnalysisDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		Analysis analysis = this.fromStorableObject(storableObject);
		this.retrieveEntity(analysis);
	}	

	protected String getEnityName() {		
		return ObjectEntities.ANALYSIS_ENTITY;
	}	

	protected String getColumns(int mode) {
		if (columns == null) {
			columns = super.getColumns(mode) + COMMA
				+ StorableObjectWrapper.COLUMN_TYPE_ID + COMMA
				+ AnalysisWrapper.COLUMN_MONITORED_ELEMENT_ID + COMMA
				+ AnalysisWrapper.COLUMN_MEASUREMENT_ID + COMMA
				+ AnalysisWrapper.COLUMN_CRITERIA_SET_ID;
		}
		return columns;
	}

	protected String getUpdateMultiplySQLValues(int mode) {
		if (updateMultiplySQLValues == null) {
			updateMultiplySQLValues = super.getUpdateMultiplySQLValues(mode) + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION;
		}
		return updateMultiplySQLValues;
	}

	protected String getUpdateSingleSQLValues(StorableObject storableObject)
			throws IllegalDataException, UpdateObjectException {
		Analysis analysis = this.fromStorableObject(storableObject);
		Measurement measurement = analysis.getMeasurement();
		String values = super.getUpdateSingleSQLValues(storableObject) + COMMA
			+ DatabaseIdentifier.toSQLString(analysis.getType().getId()) + COMMA
			+ DatabaseIdentifier.toSQLString(analysis.getMonitoredElementId()) + COMMA
			+ ((measurement != null) ? (DatabaseIdentifier.toSQLString(measurement.getId())) : "") + COMMA
			+ DatabaseIdentifier.toSQLString(analysis.getCriteriaSet().getId());
		return values;
	}

	protected int setEntityForPreparedStatement(StorableObject storableObject, PreparedStatement preparedStatement, int mode)
			throws IllegalDataException, UpdateObjectException {
		Analysis analysis = this.fromStorableObject(storableObject);
		Measurement measurement = analysis.getMeasurement();
		int i = super.setEntityForPreparedStatement(storableObject, preparedStatement, mode);
		try {
			DatabaseIdentifier.setIdentifier(preparedStatement, ++i, analysis.getType().getId()); 
			DatabaseIdentifier.setIdentifier(preparedStatement, ++i, analysis.getMonitoredElementId());
			DatabaseIdentifier.setIdentifier(preparedStatement, ++i, (measurement != null) ? measurement.getId() : null);
			DatabaseIdentifier.setIdentifier(preparedStatement, ++i, analysis.getCriteriaSet().getId());
		}
		catch (SQLException sqle) {
			throw new UpdateObjectException(getEnityName() + "Database.setEntityForPreparedStatement | Error " + sqle.getMessage(), sqle);
		}
		return i;
	}

	protected StorableObject updateEntityFromResultSet(StorableObject storableObject, ResultSet resultSet)
			throws IllegalDataException, RetrieveObjectException, SQLException {
		Analysis analysis = (storableObject == null) ? 
				new Analysis(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID),
								null,
								null,
								null,
								null,
								null) : 
					this.fromStorableObject(storableObject);
		AnalysisType analysisType;
		Measurement measurement = null;
		Set criteriaSet;
		try {
			analysisType = (AnalysisType) MeasurementStorableObjectPool.getStorableObject(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_TYPE_ID), true);
			Identifier measurementId = DatabaseIdentifier.getIdentifier(resultSet, AnalysisWrapper.COLUMN_MEASUREMENT_ID);
			if (measurementId != null)
				measurement = (Measurement) MeasurementStorableObjectPool.getStorableObject(measurementId, true);
			criteriaSet = (Set) MeasurementStorableObjectPool.getStorableObject(DatabaseIdentifier.getIdentifier(resultSet, AnalysisWrapper.COLUMN_CRITERIA_SET_ID), true);
		}
		catch (ApplicationException ae) {
			throw new RetrieveObjectException(ae);
		}
		analysis.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
							   DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
							   DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
							   DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
							   analysisType,
							   DatabaseIdentifier.getIdentifier(resultSet, AnalysisWrapper.COLUMN_MONITORED_ELEMENT_ID),
							   measurement,
							   criteriaSet);
		return analysis;
	}

	public Object retrieveObject(StorableObject storableObject, int retrieveKind, Object arg) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		Analysis analysis = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws CreateObjectException , IllegalDataException {
		Analysis analysis = this.fromStorableObject(storableObject);
		this.insertEntity(analysis);
	}

	public void insert(List storableObjects) throws IllegalDataException, CreateObjectException {
		this.insertEntities(storableObjects);
	}

	public void update(StorableObject storableObject, int updateKind, Object obj) throws IllegalDataException, VersionCollisionException, UpdateObjectException {
		Analysis analysis = this.fromStorableObject(storableObject);
		switch (updateKind) {
			case UPDATE_CHECK:
				super.checkAndUpdateEntity(storableObject, false);
				break;
			case UPDATE_FORCE:					
			default:
				super.checkAndUpdateEntity(storableObject, true);		
				return;
		}
	}

	public void update(List storableObjects, int updateKind, Object arg)
			throws IllegalDataException, VersionCollisionException, UpdateObjectException {
		switch (updateKind) {
			case UPDATE_CHECK:
				super.checkAndUpdateEntities(storableObjects, false);
				break;
			case UPDATE_FORCE:					
			default:
				super.checkAndUpdateEntities(storableObjects, true);		
				return;
		}
	}

	public List retrieveByIds(List ids, String conditions) throws IllegalDataException, RetrieveObjectException {
		if ((ids == null) || (ids.isEmpty()))
			return this.retrieveByIdsOneQuery(null, conditions);
		return this.retrieveByIdsOneQuery(ids, conditions);	
		//return retriveByIdsPreparedStatement(ids, conditions);
	}

	private List retrieveButIdsByDomain(List ids, Domain domain) throws RetrieveObjectException {
		List list = null;
		
		String condition = AnalysisWrapper.COLUMN_MONITORED_ELEMENT_ID + SQL_IN + OPEN_BRACKET
				+ SQL_SELECT + StorableObjectWrapper.COLUMN_ID + SQL_FROM + ObjectEntities.ME_ENTITY + SQL_WHERE
				+ DomainMember.COLUMN_DOMAIN_ID + EQUALS + DatabaseIdentifier.toSQLString(domain.getId())
			+ CLOSE_BRACKET;

		try {
			list = retrieveButIds(ids, condition);
		}
		catch (IllegalDataException ide) {
			Log.debugMessage("AnalysisDatabase.retrieveButIdsByDomain | Error: " + ide.getMessage(), Log.DEBUGLEVEL09);
		}

		return list;
	}

	public List retrieveByCondition(List ids, StorableObjectCondition condition)
			throws RetrieveObjectException, IllegalDataException {
		List list = null;
		if (condition instanceof DomainCondition) {
			DomainCondition domainCondition = (DomainCondition)condition;
			list = this.retrieveButIdsByDomain(ids, domainCondition.getDomain());
		}
		else {
			Log.errorMessage("AnalysisDatabase.retrieveByCondition | Unknown condition class: " + condition);
			list = this.retrieveButIds(ids);
		}
		return list;
	}
}
