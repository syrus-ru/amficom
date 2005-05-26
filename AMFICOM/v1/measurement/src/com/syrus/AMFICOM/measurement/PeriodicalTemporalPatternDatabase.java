/*-
 * $Id: PeriodicalTemporalPatternDatabase.java,v 1.4 2005/05/26 08:33:32 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */


package com.syrus.AMFICOM.measurement;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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

/**
 * @version $Revision: 1.4 $, $Date: 2005/05/26 08:33:32 $
 * @author $Author: bass $
 * @module measurement_v1
 */

public final class PeriodicalTemporalPatternDatabase extends StorableObjectDatabase {

	private static String columns;

	private static String updateMultipleSQLValues;

	private PeriodicalTemporalPattern fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof PeriodicalTemporalPattern)
			return (PeriodicalTemporalPattern) storableObject;
		throw new IllegalDataException("PeriodicalTemporalPatternDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		PeriodicalTemporalPattern periodicalTemporalPattern = this.fromStorableObject(storableObject);
		this.retrieveEntity(periodicalTemporalPattern);
	}	

	protected short getEntityCode() {		
		return ObjectEntities.PERIODICAL_TEMPORALPATTERN_ENTITY_CODE;
	}	

	protected String getColumnsTmpl() {
		if (columns == null) {
			columns = PeriodicalTemporalPatternWrapper.COLUMN_PERIOD;
		}
		return columns;
	}

	protected String getUpdateMultipleSQLValuesTmpl() {
		if (updateMultipleSQLValues == null) {
			updateMultipleSQLValues = QUESTION;
		}
		return updateMultipleSQLValues;
	}

	protected String getUpdateSingleSQLValuesTmpl(StorableObject storableObject) throws IllegalDataException {
		PeriodicalTemporalPattern periodicalTemporalPattern = this.fromStorableObject(storableObject);
		String values = "" + periodicalTemporalPattern.getPeriod();
		return values;
	}

	protected int setEntityForPreparedStatementTmpl(StorableObject storableObject, PreparedStatement preparedStatement, int startParameterNumber)
			throws IllegalDataException, SQLException {		
		PeriodicalTemporalPattern periodicalTemporalPattern = this.fromStorableObject(storableObject);
		preparedStatement.setLong(++startParameterNumber, periodicalTemporalPattern.getPeriod());
		return startParameterNumber;
	}

	protected StorableObject updateEntityFromResultSet(StorableObject storableObject, ResultSet resultSet)
			throws IllegalDataException, SQLException {
		PeriodicalTemporalPattern periodicalTemporalPattern = (storableObject == null) ?
				new PeriodicalTemporalPattern(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID),
								null,
								0L,
								0) :
					this.fromStorableObject(storableObject);
		periodicalTemporalPattern.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
							   DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
							   DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
							   DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
							   resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION),
							   resultSet.getLong(PeriodicalTemporalPatternWrapper.COLUMN_PERIOD));
		return periodicalTemporalPattern;
	}

	public Object retrieveObject(StorableObject storableObject, int retrieveKind, Object arg) throws IllegalDataException {
		PeriodicalTemporalPattern periodicalTemporalPattern = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				Log.errorMessage("Unknown retrieve kind: " + retrieveKind + " for " + this.getEntityName() + " '" +  periodicalTemporalPattern.getId() + "'; argument: " + arg);
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws CreateObjectException , IllegalDataException {
		PeriodicalTemporalPattern periodicalTemporalPattern = this.fromStorableObject(storableObject);
		this.insertEntity(periodicalTemporalPattern);
	}

	public void insert(java.util.Set storableObjects) throws IllegalDataException, CreateObjectException {
		this.insertEntities(storableObjects);
	}

}
