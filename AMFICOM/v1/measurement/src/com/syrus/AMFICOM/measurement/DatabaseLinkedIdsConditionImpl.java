/*
 * $Id: DatabaseLinkedIdsConditionImpl.java,v 1.7 2005/03/10 11:45:11 arseniy Exp $
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
 * @version $Revision: 1.7 $, $Date: 2005/03/10 11:45:11 $
 * @author $Author: arseniy $
 * @module measurement_v1
 */
public class DatabaseLinkedIdsConditionImpl extends AbstractDatabaseLinkedIdsCondition {

	private DatabaseLinkedIdsConditionImpl(LinkedIdsCondition condition) {
		super(condition);
	}
	
	public String getSQLQuery() throws IllegalDataException {
		StringBuffer query = new StringBuffer();
		switch (super.condition.getEntityCode().shortValue()) {
		case ObjectEntities.ANALYSISTYPE_ENTITY_CODE:
			query.append(super.getLinkedQuery(AnalysisTypeWrapper.LINK_COLUMN_ANALYSIS_TYPE_ID,
					StorableObjectWrapper.LINK_COLUMN_PARAMETER_TYPE_ID,
					ObjectEntities.ANATYPPARTYPLINK_ENTITY));
			break;
		case ObjectEntities.EVALUATIONTYPE_ENTITY_CODE:
			query.append(getLinkedQuery(EvaluationTypeWrapper.LINK_COLUMN_EVALUATION_TYPE_ID, 
					StorableObjectWrapper.LINK_COLUMN_PARAMETER_TYPE_ID,
					ObjectEntities.EVATYPPARTYPLINK_ENTITY));
			break;
		case ObjectEntities.MEASUREMENTTYPE_ENTITY_CODE:
			switch (super.condition.getLinkedEntityCode()) {
				case ObjectEntities.MEASUREMENTPORTTYPE_ENTITY_CODE:
					query.append(super.getLinkedQuery(MeasurementTypeWrapper.LINK_COLUMN_MEASUREMENT_TYPE_ID,
							MeasurementTypeWrapper.LINK_COLUMN_MEASUREMENT_PORT_TYPE_ID,
							ObjectEntities.MNTTYMEASPORTTYPELINK_ENTITY));
					break;
			}
			break;
		case ObjectEntities.MS_ENTITY_CODE:
			query.append(getLinkedQuery(MeasurementSetupWrapper.LINK_COLUMN_MEASUREMENT_SETUP_ID, 
					MeasurementSetupWrapper.LINK_COLUMN_ME_ID, 
					ObjectEntities.MSMELINK_ENTITY));				
			break;
		case ObjectEntities.RESULT_ENTITY_CODE:
			query.append(super.getQuery(ResultWrapper.COLUMN_MEASUREMENT_ID));
			query.append(super.getQuery(ResultWrapper.COLUMN_ANALYSIS_ID));
			query.append(super.getQuery(ResultWrapper.COLUMN_EVALUATION_ID));
			query.append(super.getQuery(ResultWrapper.COLUMN_MODELING_ID));
			break;
		default:
			throw new IllegalDataException(
					"Measurement.DatabaseLinkedIdsConditionImpl.getColumnName() | Unsupported entity type");
		}
		return query.toString();
	}
	
}
