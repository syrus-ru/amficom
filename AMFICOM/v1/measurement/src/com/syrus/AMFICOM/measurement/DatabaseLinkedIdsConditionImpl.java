/*
 * $Id: DatabaseLinkedIdsConditionImpl.java,v 1.8 2005/03/10 15:22:26 arseniy Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.measurement;

import com.syrus.AMFICOM.configuration.MonitoredElementWrapper;
import com.syrus.AMFICOM.general.AbstractDatabaseLinkedIdsCondition;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectWrapper;

/**
 * @version $Revision: 1.8 $, $Date: 2005/03/10 15:22:26 $
 * @author $Author: arseniy $
 * @module measurement_v1
 */
public class DatabaseLinkedIdsConditionImpl extends AbstractDatabaseLinkedIdsCondition {

	private DatabaseLinkedIdsConditionImpl(LinkedIdsCondition condition) {
		super(condition);
	}
	
	public String getSQLQuery() throws IllegalDataException {
		String query = null;
		switch (super.condition.getEntityCode().shortValue()) {
		case ObjectEntities.ANALYSISTYPE_ENTITY_CODE:
			query = super.getLinkedQuery(AnalysisTypeWrapper.LINK_COLUMN_ANALYSIS_TYPE_ID,
					StorableObjectWrapper.LINK_COLUMN_PARAMETER_TYPE_ID,
					ObjectEntities.ANATYPPARTYPLINK_ENTITY);
			break;
		case ObjectEntities.EVALUATIONTYPE_ENTITY_CODE:
			query = getLinkedQuery(EvaluationTypeWrapper.LINK_COLUMN_EVALUATION_TYPE_ID, 
					StorableObjectWrapper.LINK_COLUMN_PARAMETER_TYPE_ID,
					ObjectEntities.EVATYPPARTYPLINK_ENTITY);
			break;
		case ObjectEntities.MEASUREMENTTYPE_ENTITY_CODE:
			switch (super.condition.getLinkedEntityCode()) {
				case ObjectEntities.MEASUREMENTPORTTYPE_ENTITY_CODE:
					query = super.getLinkedQuery(MeasurementTypeWrapper.LINK_COLUMN_MEASUREMENT_TYPE_ID,
							MeasurementTypeWrapper.LINK_COLUMN_MEASUREMENT_PORT_TYPE_ID,
							ObjectEntities.MNTTYMEASPORTTYPELINK_ENTITY);
					break;
			}
			break;
		case ObjectEntities.MS_ENTITY_CODE:
			query = super.getLinkedQuery(MeasurementSetupWrapper.LINK_COLUMN_MEASUREMENT_SETUP_ID, 
					MeasurementSetupWrapper.LINK_COLUMN_ME_ID, 
					ObjectEntities.MSMELINK_ENTITY);
			break;
		case ObjectEntities.RESULT_ENTITY_CODE:
			StringBuffer stringBuffer = new StringBuffer();
			stringBuffer.append(super.getQuery(ResultWrapper.COLUMN_MEASUREMENT_ID));
			stringBuffer.append(super.getQuery(ResultWrapper.COLUMN_ANALYSIS_ID));
			stringBuffer.append(super.getQuery(ResultWrapper.COLUMN_EVALUATION_ID));
			stringBuffer.append(super.getQuery(ResultWrapper.COLUMN_MODELING_ID));
			query = stringBuffer.toString();
			break;
		case ObjectEntities.TEST_ENTITY_CODE:
			switch (super.condition.getLinkedEntityCode()) {
				case ObjectEntities.ME_ENTITY_CODE:
					query = super.getQuery(TestWrapper.COLUMN_MONITORED_ELEMENT_ID);
					break;
				case ObjectEntities.MEASUREMENTPORT_ENTITY_CODE:
					query = super.getLinkedQuery(TestWrapper.COLUMN_MONITORED_ELEMENT_ID,
							StorableObjectWrapper.COLUMN_ID,
							MonitoredElementWrapper.COLUMN_MEASUREMENT_PORT_ID,
							ObjectEntities.ME_ENTITY);
					break;
			}
			break;
		default:
			throw new IllegalDataException(
					"Measurement.DatabaseLinkedIdsConditionImpl.getColumnName() | Unsupported entity type");
		}
		return query;
	}
	
}
