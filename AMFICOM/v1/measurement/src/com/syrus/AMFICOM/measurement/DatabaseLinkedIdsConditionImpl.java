/*
 * $Id: DatabaseLinkedIdsConditionImpl.java,v 1.18 2005/04/05 15:57:51 arseniy Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.measurement;

import com.syrus.AMFICOM.configuration.KISWrapper;
import com.syrus.AMFICOM.configuration.MeasurementPortWrapper;
import com.syrus.AMFICOM.configuration.MonitoredElementWrapper;
import com.syrus.AMFICOM.general.AbstractDatabaseLinkedIdsCondition;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectWrapper;

/**
 * @version $Revision: 1.18 $, $Date: 2005/04/05 15:57:51 $
 * @author $Author: arseniy $
 * @module measurement_v1
 */
public class DatabaseLinkedIdsConditionImpl extends AbstractDatabaseLinkedIdsCondition {

	private DatabaseLinkedIdsConditionImpl(LinkedIdsCondition condition) {
		super(condition);
	}
	
	public String getSQLQuery() throws IllegalDataException {
		String query = null;
		StringBuffer stringBuffer;
		switch (super.condition.getEntityCode().shortValue()) {
			case ObjectEntities.ANALYSISTYPE_ENTITY_CODE:
				switch (super.condition.getLinkedEntityCode()) {
					case ObjectEntities.PARAMETERTYPE_ENTITY_CODE:
						query = super.getLinkedQuery(AnalysisTypeWrapper.LINK_COLUMN_ANALYSIS_TYPE_ID,
								StorableObjectWrapper.LINK_COLUMN_PARAMETER_TYPE_ID,
								ObjectEntities.ANATYPPARTYPLINK_ENTITY);
						break;
					case ObjectEntities.MEASUREMENTTYPE_ENTITY_CODE:
						query = super.getLinkedQuery(AnalysisTypeWrapper.LINK_COLUMN_ANALYSIS_TYPE_ID,
								MeasurementTypeWrapper.LINK_COLUMN_MEASUREMENT_TYPE_ID,
								ObjectEntities.MNTTYPANATYPEVATYP_ENTITY);
						break;
				}
				break;
			case ObjectEntities.EVALUATIONTYPE_ENTITY_CODE:
				switch (super.condition.getLinkedEntityCode()) {
					case ObjectEntities.PARAMETERTYPE_ENTITY_CODE:
						query = getLinkedQuery(EvaluationTypeWrapper.LINK_COLUMN_EVALUATION_TYPE_ID, 
								StorableObjectWrapper.LINK_COLUMN_PARAMETER_TYPE_ID,
								ObjectEntities.EVATYPPARTYPLINK_ENTITY);
						break;
					case ObjectEntities.MEASUREMENTTYPE_ENTITY_CODE:
						query = super.getLinkedQuery(EvaluationTypeWrapper.LINK_COLUMN_EVALUATION_TYPE_ID,
								MeasurementTypeWrapper.LINK_COLUMN_MEASUREMENT_TYPE_ID,
								ObjectEntities.MNTTYPANATYPEVATYP_ENTITY);
						break;
				}
				break;
			case ObjectEntities.MEASUREMENTTYPE_ENTITY_CODE:
				switch (super.condition.getLinkedEntityCode()) {
					case ObjectEntities.MEASUREMENTPORTTYPE_ENTITY_CODE:
						query = super.getLinkedQuery(MeasurementTypeWrapper.LINK_COLUMN_MEASUREMENT_TYPE_ID,
								MeasurementTypeWrapper.LINK_COLUMN_MEASUREMENT_PORT_TYPE_ID,
								ObjectEntities.MNTTYPEMEASPORTTYPELINK_ENTITY);
						break;
				}
				break;
			case ObjectEntities.ANALYSIS_ENTITY_CODE:
				switch (super.condition.getLinkedEntityCode()) {
					case ObjectEntities.MEASUREMENT_ENTITY_CODE:
						query = super.getQuery(AnalysisWrapper.COLUMN_MEASUREMENT_ID);
						break;
				}
				break;
			case ObjectEntities.EVALUATION_ENTITY_CODE:
				switch (super.condition.getLinkedEntityCode()) {
					case ObjectEntities.MEASUREMENT_ENTITY_CODE:
						query = super.getQuery(EvaluationWrapper.COLUMN_MEASUREMENT_ID);
						break;
				}
				break;
			case ObjectEntities.MEASUREMENT_ENTITY_CODE:
				switch (super.condition.getLinkedEntityCode()) {
					case ObjectEntities.TEST_ENTITY_CODE:
						query = super.getQuery(MeasurementWrapper.COLUMN_TEST_ID);
						break;
				}
				break;
			case ObjectEntities.MS_ENTITY_CODE:
				switch (super.condition.getLinkedEntityCode()) {
					case ObjectEntities.ME_ENTITY_CODE:
						query = super.getLinkedQuery(MeasurementSetupWrapper.LINK_COLUMN_MEASUREMENT_SETUP_ID, 
									MeasurementSetupWrapper.LINK_COLUMN_MONITORED_ELEMENT_ID, 
									ObjectEntities.MSMELINK_ENTITY);
						break;
				}
				break;
			case ObjectEntities.RESULT_ENTITY_CODE:
				switch (super.condition.getLinkedEntityCode()) {
					case ObjectEntities.MEASUREMENT_ENTITY_CODE:
						stringBuffer = new StringBuffer();
						stringBuffer.append(super.getQuery(ResultWrapper.COLUMN_MEASUREMENT_ID));
						stringBuffer.append(StorableObjectDatabase.SQL_OR);
						stringBuffer.append(StorableObjectDatabase.OPEN_BRACKET);
						stringBuffer.append(super.getLinkedQuery(ResultWrapper.COLUMN_ANALYSIS_ID,
								StorableObjectWrapper.COLUMN_ID,
								AnalysisWrapper.COLUMN_MEASUREMENT_ID,
								ObjectEntities.ANALYSIS_ENTITY));
						stringBuffer.append(StorableObjectDatabase.CLOSE_BRACKET);
						stringBuffer.append(StorableObjectDatabase.SQL_OR);
						stringBuffer.append(StorableObjectDatabase.OPEN_BRACKET);
						stringBuffer.append(super.getLinkedQuery(ResultWrapper.COLUMN_EVALUATION_ID,
								StorableObjectWrapper.COLUMN_ID,
								AnalysisWrapper.COLUMN_MEASUREMENT_ID,
								ObjectEntities.EVALUATION_ENTITY));
						stringBuffer.append(StorableObjectDatabase.CLOSE_BRACKET);
						query = stringBuffer.toString();
						break;
					case ObjectEntities.ANALYSIS_ENTITY_CODE:
						query = super.getQuery(ResultWrapper.COLUMN_ANALYSIS_ID);
						break;
					case ObjectEntities.EVALUATION_ENTITY_CODE:
						query = super.getQuery(ResultWrapper.COLUMN_EVALUATION_ID);
					case ObjectEntities.MODELING_ENTITY_CODE:
						query = super.getQuery(ResultWrapper.COLUMN_MODELING_ID);
				}
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
					case ObjectEntities.MCM_ENTITY_CODE:
						stringBuffer = new StringBuffer();
						stringBuffer.append(TestWrapper.COLUMN_MONITORED_ELEMENT_ID);
						stringBuffer.append(StorableObjectDatabase.SQL_IN);
						stringBuffer.append(StorableObjectDatabase.OPEN_BRACKET);
						stringBuffer.append(StorableObjectDatabase.SQL_SELECT);
						stringBuffer.append(StorableObjectWrapper.COLUMN_ID);
						stringBuffer.append(StorableObjectDatabase.SQL_FROM);
						stringBuffer.append(ObjectEntities.ME_ENTITY);
						stringBuffer.append(StorableObjectDatabase.SQL_WHERE);
						stringBuffer.append(MonitoredElementWrapper.COLUMN_MEASUREMENT_PORT_ID);
						stringBuffer.append(StorableObjectDatabase.SQL_IN);
						stringBuffer.append(StorableObjectDatabase.OPEN_BRACKET);
						stringBuffer.append(StorableObjectDatabase.SQL_SELECT);
						stringBuffer.append(StorableObjectWrapper.COLUMN_ID);
						stringBuffer.append(StorableObjectDatabase.SQL_FROM);
						stringBuffer.append(ObjectEntities.MEASUREMENTPORT_ENTITY);
						stringBuffer.append(StorableObjectDatabase.SQL_WHERE);
						stringBuffer.append(super.getLinkedQuery(MeasurementPortWrapper.COLUMN_KIS_ID,
								StorableObjectWrapper.COLUMN_ID,
								KISWrapper.COLUMN_MCM_ID,
								ObjectEntities.KIS_ENTITY));
						stringBuffer.append(StorableObjectDatabase.CLOSE_BRACKET);
						stringBuffer.append(StorableObjectDatabase.CLOSE_BRACKET);
						query = stringBuffer.toString();
				}
				break;
			default:
				throw new IllegalDataException("Measurement.DatabaseLinkedIdsConditionImpl.getColumnName() | Unsupported entity type -- "
						+ super.condition.getEntityCode().shortValue());
		}
		return query;
	}
	
}
