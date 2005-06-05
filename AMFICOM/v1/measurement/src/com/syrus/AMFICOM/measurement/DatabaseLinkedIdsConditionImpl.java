/*
 * $Id: DatabaseLinkedIdsConditionImpl.java,v 1.25 2005/06/05 18:31:27 arseniy Exp $
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
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectWrapper;

/**
 * @version $Revision: 1.25 $, $Date: 2005/06/05 18:31:27 $
 * @author $Author: arseniy $
 * @module measurement_v1
 */
final class DatabaseLinkedIdsConditionImpl extends AbstractDatabaseLinkedIdsCondition {

	private DatabaseLinkedIdsConditionImpl(LinkedIdsCondition condition) {
		super(condition);
	}
	
	public String getSQLQuery() throws IllegalObjectEntityException {
		StringBuffer stringBuffer;
		switch (super.condition.getEntityCode().shortValue()) {
			case ObjectEntities.ANALYSISTYPE_ENTITY_CODE:
				switch (super.condition.getLinkedEntityCode()) {
					case ObjectEntities.PARAMETERTYPE_ENTITY_CODE:
						return super.getLinkedQuery(AnalysisTypeWrapper.LINK_COLUMN_ANALYSIS_TYPE_ID,
								StorableObjectWrapper.LINK_COLUMN_PARAMETER_TYPE_ID,
								ObjectEntities.ANATYPPARTYPLINK_ENTITY);
					case ObjectEntities.MEASUREMENTTYPE_ENTITY_CODE:
						return super.getLinkedQuery(AnalysisTypeWrapper.LINK_COLUMN_ANALYSIS_TYPE_ID,
								MeasurementTypeWrapper.LINK_COLUMN_MEASUREMENT_TYPE_ID,
								ObjectEntities.MNTTYPANATYPEVATYP_ENTITY);
					default:
						throw new IllegalObjectEntityException("Unsupported linked entity type -- "
								+ super.condition.getLinkedEntityCode()
								+ " for entity type " + super.condition.getEntityCode(),
								IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
				}
			case ObjectEntities.EVALUATIONTYPE_ENTITY_CODE:
				switch (super.condition.getLinkedEntityCode()) {
					case ObjectEntities.PARAMETERTYPE_ENTITY_CODE:
						return getLinkedQuery(EvaluationTypeWrapper.LINK_COLUMN_EVALUATION_TYPE_ID,
								StorableObjectWrapper.LINK_COLUMN_PARAMETER_TYPE_ID,
								ObjectEntities.EVATYPPARTYPLINK_ENTITY);
					case ObjectEntities.MEASUREMENTTYPE_ENTITY_CODE:
						return super.getLinkedQuery(EvaluationTypeWrapper.LINK_COLUMN_EVALUATION_TYPE_ID,
								MeasurementTypeWrapper.LINK_COLUMN_MEASUREMENT_TYPE_ID,
								ObjectEntities.MNTTYPANATYPEVATYP_ENTITY);
					default:
						throw new IllegalObjectEntityException("Unsupported linked entity type -- "
								+ super.condition.getLinkedEntityCode()
								+ " for entity type " + super.condition.getEntityCode(),
								IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
				}
			case ObjectEntities.MEASUREMENTTYPE_ENTITY_CODE:
				switch (super.condition.getLinkedEntityCode()) {
					case ObjectEntities.MEASUREMENTPORTTYPE_ENTITY_CODE:
						return super.getLinkedQuery(MeasurementTypeWrapper.LINK_COLUMN_MEASUREMENT_TYPE_ID,
								MeasurementTypeWrapper.LINK_COLUMN_MEASUREMENT_PORT_TYPE_ID,
								ObjectEntities.MNTTYPEMEASPORTTYPELINK_ENTITY);
					default:
						throw new IllegalObjectEntityException("Unsupported linked entity type -- "
								+ super.condition.getLinkedEntityCode()
								+ " for entity type " + super.condition.getEntityCode(),
								IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
				}
			case ObjectEntities.ANALYSIS_ENTITY_CODE:
				switch (super.condition.getLinkedEntityCode()) {
					case ObjectEntities.MEASUREMENT_ENTITY_CODE:
						return super.getQuery(AnalysisWrapper.COLUMN_MEASUREMENT_ID);
					default:
						throw new IllegalObjectEntityException("Unsupported linked entity type -- "
								+ super.condition.getLinkedEntityCode()
								+ " for entity type " + super.condition.getEntityCode(),
								IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
				}
			case ObjectEntities.EVALUATION_ENTITY_CODE:
				switch (super.condition.getLinkedEntityCode()) {
					case ObjectEntities.MEASUREMENT_ENTITY_CODE:
						return super.getQuery(EvaluationWrapper.COLUMN_MEASUREMENT_ID);
					default:
						throw new IllegalObjectEntityException("Unsupported linked entity type -- "
								+ super.condition.getLinkedEntityCode()
								+ " for entity type " + super.condition.getEntityCode(),
								IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
				}
			case ObjectEntities.MEASUREMENT_ENTITY_CODE:
				switch (super.condition.getLinkedEntityCode()) {
					case ObjectEntities.TEST_ENTITY_CODE:
						return super.getQuery(MeasurementWrapper.COLUMN_TEST_ID);
					default:
						throw new IllegalObjectEntityException("Unsupported linked entity type -- "
								+ super.condition.getLinkedEntityCode()
								+ " for entity type " + super.condition.getEntityCode(),
								IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
				}
			case ObjectEntities.MEASUREMENTSETUP_ENTITY_CODE:
				switch (super.condition.getLinkedEntityCode()) {
					case ObjectEntities.MONITORED_ELEMENT_ENTITY_CODE:
						return super.getLinkedQuery(MeasurementSetupWrapper.LINK_COLUMN_MEASUREMENT_SETUP_ID,
									MeasurementSetupWrapper.LINK_COLUMN_MONITORED_ELEMENT_ID,
									ObjectEntities.MSMELINK_ENTITY);
					case ObjectEntities.MEASUREMENTTYPE_ENTITY_CODE:
						return super.getLinkedQuery(MeasurementSetupWrapper.LINK_COLUMN_MEASUREMENT_SETUP_ID,
							MeasurementSetupWrapper.LINK_COLUMN_MEASUREMENT_TYPE_ID,
							ObjectEntities.MSMTLINK_ENTITY);
					default:
						throw new IllegalObjectEntityException("Unsupported linked entity type -- "
								+ super.condition.getLinkedEntityCode()
								+ " for entity type " + super.condition.getEntityCode(),
								IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
				}
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
						return stringBuffer.toString();
					case ObjectEntities.ANALYSIS_ENTITY_CODE:
						return super.getQuery(ResultWrapper.COLUMN_ANALYSIS_ID);
					case ObjectEntities.EVALUATION_ENTITY_CODE:
						return super.getQuery(ResultWrapper.COLUMN_EVALUATION_ID);
					case ObjectEntities.MODELING_ENTITY_CODE:
						return super.getQuery(ResultWrapper.COLUMN_MODELING_ID);
					default:
						throw new IllegalObjectEntityException("Unsupported linked entity type -- "
								+ super.condition.getLinkedEntityCode()
								+ " for entity type " + super.condition.getEntityCode(),
								IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
				}
			case ObjectEntities.TEST_ENTITY_CODE:
				switch (super.condition.getLinkedEntityCode()) {
					case ObjectEntities.MONITORED_ELEMENT_ENTITY_CODE:
						return super.getQuery(TestWrapper.COLUMN_MONITORED_ELEMENT_ID);
					case ObjectEntities.MEASUREMENTPORT_ENTITY_CODE:
						return super.getLinkedQuery(TestWrapper.COLUMN_MONITORED_ELEMENT_ID,
								StorableObjectWrapper.COLUMN_ID,
								MonitoredElementWrapper.COLUMN_MEASUREMENT_PORT_ID,
								ObjectEntities.MONITORED_ELEMENT_ENTITY);
					case ObjectEntities.MCM_ENTITY_CODE:
						stringBuffer = new StringBuffer();
						stringBuffer.append(TestWrapper.COLUMN_MONITORED_ELEMENT_ID);
						stringBuffer.append(StorableObjectDatabase.SQL_IN);
						stringBuffer.append(StorableObjectDatabase.OPEN_BRACKET);
						stringBuffer.append(StorableObjectDatabase.SQL_SELECT);
						stringBuffer.append(StorableObjectWrapper.COLUMN_ID);
						stringBuffer.append(StorableObjectDatabase.SQL_FROM);
						stringBuffer.append(ObjectEntities.MONITORED_ELEMENT_ENTITY);
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
						return stringBuffer.toString();
					case ObjectEntities.TEST_ENTITY_CODE:
						return super.getQuery(TestWrapper.COLUMN_GROUP_TEST_ID);
					default:
						throw new IllegalObjectEntityException("Unsupported linked entity type -- "
								+ super.condition.getLinkedEntityCode()
								+ " for entity type " + super.condition.getEntityCode(),
								IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
				}
			default:
				throw new IllegalObjectEntityException("Unsupported entity type -- "
						+ super.condition.getEntityCode(), IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
		}
	}
	
}
