/*
 * $Id: DatabaseLinkedIdsConditionImpl.java,v 1.3 2005/02/08 13:56:53 max Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.measurement;

import com.syrus.AMFICOM.general.AbstractDatabaseLinkedIdsCondition;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectWrapper;

/**
 * @version $Revision: 1.3 $, $Date: 2005/02/08 13:56:53 $
 * @author $Author: max $
 * @module measurement_v1
 */
public class DatabaseLinkedIdsConditionImpl extends AbstractDatabaseLinkedIdsCondition {

	private DatabaseLinkedIdsConditionImpl(LinkedIdsCondition condition) {
		super(condition);
	}

	protected String getColumnName(short entityCode){
		String columnName = null;
		short code = super.condition.getEntityCode().shortValue();
		switch (code) {
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
				switch(entityCode) {
					case ObjectEntities.ME_ENTITY_CODE: 
						columnName = MeasurementSetupWrapper.LINK_COLUMN_ME_ID;
					break;
//					case ObjectEntities.PARAMETERTYPE_ENTITY_CODE:
//						columnName = StorableObjectWrapper.LINK_COLUMN_PARAMETER_TYPE_ID;
//					break;
					default:
						throw new UnsupportedOperationException("Measurement.DatabaseLinkedIdsConditionImpl.getColumnName() | Unsupported entity type");
						
				}
				break;
			case ObjectEntities.RESULT_ENTITY_CODE:
				switch(entityCode) {
				case ObjectEntities.MEASUREMENT_ENTITY_CODE:
					columnName = ResultWrapper.COLUMN_MEASUREMENT_ID;
					break;					
				case ObjectEntities.ANALYSIS_ENTITY_CODE:
					columnName = ResultWrapper.COLUMN_ANALYSIS_ID;
					break;
				case ObjectEntities.EVALUATION_ENTITY_CODE:
					columnName = ResultWrapper.COLUMN_EVALUATION_ID;
					break;
				case ObjectEntities.MODELING_ENTITY_CODE:
					columnName = ResultWrapper.COLUMN_MODELING_ID;
					break;
				default:
					throw new UnsupportedOperationException("Measurement.DatabaseLinkedIdsConditionImpl.getColumnName() | Unsupported entity type");
				}
			default:
				throw new UnsupportedOperationException("Measurement.DatabaseLinkedIdsConditionImpl.getColumnName() | Unsupported entity " + ObjectEntities.codeToString(code));
		}
		return columnName;	
	}
	
	public String getSQLQuery() throws IllegalDataException {
		String query;
		switch (super.condition.getEntityCode().shortValue()) {
		case ObjectEntities.ANALYSISTYPE_ENTITY_CODE:
			query = super.getLinkedQuery(
					AnalysisTypeDatabase.LINK_COLUMN_ANALYSIS_TYPE_ID,
					ObjectEntities.ANATYPPARTYPLINK_ENTITY);
			break;
		case ObjectEntities.EVALUATIONTYPE_ENTITY_CODE:
			query = super.getLinkedQuery(
					EvaluationTypeDatabase.LINK_COLUMN_EVALUATION_TYPE_ID,
					ObjectEntities.EVATYPPARTYPLINK_ENTITY);
			break;
		case ObjectEntities.MEASUREMENTTYPE_ENTITY_CODE:
			query = super.getLinkedQuery(
					MeasurementTypeWrapper.LINK_COLUMN_MEASUREMENT_TYPE_ID,
					ObjectEntities.MNTTYPPARTYPLINK_ENTITY);
			break;
		case ObjectEntities.MS_ENTITY_CODE:
			query = super.getLinkedQuery(
					MeasurementSetupWrapper.LINK_COLUMN_MEASUREMENT_SETUP_ID,
					ObjectEntities.MSMELINK_ENTITY);
			break;
		case ObjectEntities.RESULT_ENTITY_CODE:
			query = super.getSQLQuery();
			break;
		default:
			throw new IllegalDataException(
					"Measurement.DatabaseLinkedIdsConditionImpl.getColumnName() | Unsupported entity type");
		}
		return query;
	}
	
}
