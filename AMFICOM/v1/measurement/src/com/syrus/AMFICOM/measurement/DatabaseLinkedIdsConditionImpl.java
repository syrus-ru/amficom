/*
 * $Id: DatabaseLinkedIdsConditionImpl.java,v 1.2 2005/02/08 12:06:36 max Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.measurement;

import com.syrus.AMFICOM.general.AbstractDatabaseLinkedIdsCondition;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectWrapper;

/**
 * @version $Revision: 1.2 $, $Date: 2005/02/08 12:06:36 $
 * @author $Author: max $
 * @module measurement_v1
 */
public class DatabaseLinkedIdsConditionImpl extends AbstractDatabaseLinkedIdsCondition {

	private DatabaseLinkedIdsConditionImpl(LinkedIdsCondition condition) {
		super(condition);
	}

	protected String getColumnName(){
		String columnName = null;
		switch (super.condition.getEntityCode().shortValue()) {
			case ObjectEntities.MEASUREMENT_ENTITY_CODE:
				columnName = MeasurementWrapper.COLUMN_TEST_ID;
				break;
			case ObjectEntities.ANALYSISTYPE_ENTITY_CODE:
				columnName = StorableObjectWrapper.LINK_COLUMN_PARAMETER_TYPE_ID;
				break;
			case ObjectEntities.EVALUATIONTYPE_ENTITY_CODE:
				columnName = StorableObjectWrapper.LINK_COLUMN_PARAMETER_TYPE_ID;
				break;
			case ObjectEntities.MEASUREMENTTYPE_ENTITY_CODE:
				columnName = MeasurementTypeWrapper.LINK_COLUMN_MEASUREMENT_PORT_TYPE_ID;
				break;
			case ObjectEntities.MS_ENTITY_CODE:
				columnName = MeasurementSetupWrapper.LINK_COLUMN_ME_ID;
				columnName = MeasurementSetupWrapper.LINK_COLUMN_MEASUREMENT_SETUP_ID;
				break;
			case ObjectEntities.RESULT_ENTITY_CODE:
				columnName = ResultWrapper.COLUMN_ACTION_ID;
				break;
			default:
				throw new UnsupportedOperationException("Measurement.DatabaseLinkedIdsConditionImpl.getColumnName() | Unsupported entity type");
		}
		return columnName;	
	}
	
	public String getSQLQuery() {
		StringBuffer buffer = new StringBuffer();
		switch (super.condition.getEntityCode().shortValue()) {
		case ObjectEntities.ANALYSISTYPE_ENTITY_CODE:
			buffer.append(StorableObjectWrapper.COLUMN_ID);
			buffer.append(StorableObjectDatabase.SQL_IN);
			buffer.append(StorableObjectDatabase.OPEN_BRACKET);
			buffer.append(StorableObjectDatabase.SQL_SELECT);
			buffer.append(AnalysisTypeDatabase.LINK_COLUMN_ANALYSIS_TYPE_ID);
			buffer.append(StorableObjectDatabase.SQL_FROM);
			buffer.append(ObjectEntities.ANATYPPARTYPLINK_ENTITY);
			buffer.append(StorableObjectDatabase.SQL_WHERE);
			buffer.append(super.getSQLQuery());
			break;
		case ObjectEntities.EVALUATIONTYPE_ENTITY_CODE:
			buffer.append(StorableObjectWrapper.COLUMN_ID);
			buffer.append(StorableObjectDatabase.SQL_IN);
			buffer.append(StorableObjectDatabase.OPEN_BRACKET);
			buffer.append(StorableObjectDatabase.SQL_SELECT);
			buffer.append(EvaluationTypeDatabase.LINK_COLUMN_EVALUATION_TYPE_ID);
			buffer.append(StorableObjectDatabase.SQL_FROM);
			buffer.append(ObjectEntities.EVATYPPARTYPLINK_ENTITY);
			buffer.append(StorableObjectDatabase.SQL_WHERE);
			buffer.append(super.getSQLQuery());
			break;
		case ObjectEntities.MEASUREMENTTYPE_ENTITY_CODE:
			buffer.append(StorableObjectWrapper.COLUMN_ID);
			buffer.append(StorableObjectDatabase.SQL_IN);
			buffer.append(StorableObjectDatabase.OPEN_BRACKET);
			buffer.append(StorableObjectDatabase.SQL_SELECT);
			buffer.append(MeasurementTypeWrapper.LINK_COLUMN_MEASUREMENT_TYPE_ID);
			buffer.append(StorableObjectDatabase.SQL_FROM);
			buffer.append(ObjectEntities.m);
			buffer.append(StorableObjectDatabase.SQL_WHERE);
			buffer.append(super.getSQLQuery());
			break;
		case ObjectEntities.MS_ENTITY_CODE:
			columnName = MeasurementSetupWrapper.LINK_COLUMN_ME_ID;
			columnName = MeasurementSetupWrapper.LINK_COLUMN_MEASUREMENT_SETUP_ID;
			break;
		case ObjectEntities.RESULT_ENTITY_CODE:
			columnName = ResultWrapper.COLUMN_ACTION_ID;
			break;
		default:
			throw new ApplicationException("Measurement.DatabaseLinkedIdsConditionImpl.getColumnName() | Unsupported entity type");
	}
	}
	
}
