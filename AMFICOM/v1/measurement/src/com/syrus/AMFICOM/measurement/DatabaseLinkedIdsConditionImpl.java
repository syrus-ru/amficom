/*
 * $Id: DatabaseLinkedIdsConditionImpl.java,v 1.31 2005/08/08 11:31:45 arseniy Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.measurement;

import static com.syrus.AMFICOM.general.ObjectEntities.ANALYSIS_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.ANALYSIS_TYPE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.EVALUATION_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.EVALUATION_TYPE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MCM_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MEASUREMENTPORT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MEASUREMENTPORT_TYPE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MEASUREMENTSETUP_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MEASUREMENT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MEASUREMENT_TYPE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MODELING_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MONITOREDELEMENT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.PARAMETER_TYPE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.RESULT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.TEST_CODE;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.CLOSE_BRACKET;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.OPEN_BRACKET;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.SQL_FROM;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.SQL_IN;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.SQL_OR;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.SQL_SELECT;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.SQL_WHERE;

import com.syrus.AMFICOM.configuration.KISWrapper;
import com.syrus.AMFICOM.configuration.MeasurementPortWrapper;
import com.syrus.AMFICOM.configuration.MonitoredElementWrapper;
import com.syrus.AMFICOM.general.AbstractDatabaseLinkedIdsCondition;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectWrapper;

/**
 * @version $Revision: 1.31 $, $Date: 2005/08/08 11:31:45 $
 * @author $Author: arseniy $
 * @module measurement
 */
final class DatabaseLinkedIdsConditionImpl extends AbstractDatabaseLinkedIdsCondition {

	@SuppressWarnings("unused")
	private DatabaseLinkedIdsConditionImpl(final LinkedIdsCondition condition) {
		super(condition);
	}
	
	public String getSQLQuery() throws IllegalObjectEntityException {
		StringBuffer stringBuffer;
		switch (super.condition.getEntityCode().shortValue()) {
			case ANALYSIS_TYPE_CODE:
				switch (super.condition.getLinkedEntityCode()) {
					case PARAMETER_TYPE_CODE:
						return super.getLinkedQuery(AnalysisTypeWrapper.LINK_COLUMN_ANALYSIS_TYPE_ID,
								StorableObjectWrapper.LINK_COLUMN_PARAMETER_TYPE_ID,
								ObjectEntities.ANATYPPARTYPLINK);
					case MEASUREMENT_TYPE_CODE:
						return super.getLinkedQuery(AnalysisTypeWrapper.LINK_COLUMN_ANALYSIS_TYPE_ID,
								MeasurementTypeWrapper.LINK_COLUMN_MEASUREMENT_TYPE_ID,
								ObjectEntities.MNTTYPANATYPEVATYP);
					default:
						throw super.newExceptionLinkedEntityIllegal();
				}
			case EVALUATION_TYPE_CODE:
				switch (super.condition.getLinkedEntityCode()) {
					case PARAMETER_TYPE_CODE:
						return getLinkedQuery(EvaluationTypeWrapper.LINK_COLUMN_EVALUATION_TYPE_ID,
								StorableObjectWrapper.LINK_COLUMN_PARAMETER_TYPE_ID,
								ObjectEntities.EVATYPPARTYPLINK);
					case MEASUREMENT_TYPE_CODE:
						return super.getLinkedQuery(EvaluationTypeWrapper.LINK_COLUMN_EVALUATION_TYPE_ID,
								MeasurementTypeWrapper.LINK_COLUMN_MEASUREMENT_TYPE_ID,
								ObjectEntities.MNTTYPANATYPEVATYP);
					default:
						throw super.newExceptionLinkedEntityIllegal();
				}
			case MEASUREMENT_TYPE_CODE:
				switch (super.condition.getLinkedEntityCode()) {
					case MEASUREMENTPORT_TYPE_CODE:
						return super.getLinkedQuery(MeasurementTypeWrapper.LINK_COLUMN_MEASUREMENT_TYPE_ID,
								MeasurementTypeWrapper.LINK_COLUMN_MEASUREMENT_PORT_TYPE_ID,
								ObjectEntities.MNTTYPEMEASPORTTYPELINK);
					default:
						throw super.newExceptionLinkedEntityIllegal();
				}
			case ANALYSIS_CODE:
				switch (super.condition.getLinkedEntityCode()) {
					case MEASUREMENT_CODE:
						return super.getQuery(AnalysisWrapper.COLUMN_MEASUREMENT_ID);
					default:
						throw super.newExceptionLinkedEntityIllegal();
				}
			case EVALUATION_CODE:
				switch (super.condition.getLinkedEntityCode()) {
					case MEASUREMENT_CODE:
						return super.getQuery(EvaluationWrapper.COLUMN_MEASUREMENT_ID);
					default:
						throw super.newExceptionLinkedEntityIllegal();
				}
			case MEASUREMENT_CODE:
				switch (super.condition.getLinkedEntityCode()) {
					case TEST_CODE:
						return super.getQuery(MeasurementWrapper.COLUMN_TEST_ID);
					default:
						throw super.newExceptionLinkedEntityIllegal();
				}
			case MEASUREMENTSETUP_CODE:
				switch (super.condition.getLinkedEntityCode()) {
					case MONITOREDELEMENT_CODE:
						return super.getLinkedQuery(MeasurementSetupWrapper.LINK_COLUMN_MEASUREMENT_SETUP_ID,
									MeasurementSetupWrapper.LINK_COLUMN_MONITORED_ELEMENT_ID,
									ObjectEntities.MSMELINK);
					case MEASUREMENT_TYPE_CODE:
						return super.getLinkedQuery(MeasurementSetupWrapper.LINK_COLUMN_MEASUREMENT_SETUP_ID,
							MeasurementSetupWrapper.LINK_COLUMN_MEASUREMENT_TYPE_ID,
							ObjectEntities.MSMTLINK);
					default:
						throw super.newExceptionLinkedEntityIllegal();
				}
			case RESULT_CODE:
				switch (super.condition.getLinkedEntityCode()) {
					case MEASUREMENT_CODE:
						stringBuffer = new StringBuffer();
						stringBuffer.append(super.getQuery(ResultWrapper.COLUMN_MEASUREMENT_ID));
						stringBuffer.append(SQL_OR);
						stringBuffer.append(OPEN_BRACKET);
						stringBuffer.append(super.getLinkedQuery(ResultWrapper.COLUMN_ANALYSIS_ID,
								StorableObjectWrapper.COLUMN_ID,
								AnalysisWrapper.COLUMN_MEASUREMENT_ID,
								ObjectEntities.ANALYSIS));
						stringBuffer.append(CLOSE_BRACKET);
						stringBuffer.append(SQL_OR);
						stringBuffer.append(OPEN_BRACKET);
						stringBuffer.append(super.getLinkedQuery(ResultWrapper.COLUMN_EVALUATION_ID,
								StorableObjectWrapper.COLUMN_ID,
								AnalysisWrapper.COLUMN_MEASUREMENT_ID,
								ObjectEntities.EVALUATION));
						stringBuffer.append(CLOSE_BRACKET);
						return stringBuffer.toString();
					case ANALYSIS_CODE:
						return super.getQuery(ResultWrapper.COLUMN_ANALYSIS_ID);
					case EVALUATION_CODE:
						return super.getQuery(ResultWrapper.COLUMN_EVALUATION_ID);
					case MODELING_CODE:
						return super.getQuery(ResultWrapper.COLUMN_MODELING_ID);
					default:
						throw super.newExceptionLinkedEntityIllegal();
				}
			case TEST_CODE:
				switch (super.condition.getLinkedEntityCode()) {
					case MONITOREDELEMENT_CODE:
						return super.getQuery(TestWrapper.COLUMN_MONITORED_ELEMENT_ID);
					case MEASUREMENTPORT_CODE:
						return super.getLinkedQuery(TestWrapper.COLUMN_MONITORED_ELEMENT_ID,
								StorableObjectWrapper.COLUMN_ID,
								MonitoredElementWrapper.COLUMN_MEASUREMENT_PORT_ID,
								ObjectEntities.MONITOREDELEMENT);
					case MCM_CODE:
						stringBuffer = new StringBuffer();
						stringBuffer.append(TestWrapper.COLUMN_MONITORED_ELEMENT_ID);
						stringBuffer.append(SQL_IN);
						stringBuffer.append(OPEN_BRACKET);
						stringBuffer.append(SQL_SELECT);
						stringBuffer.append(StorableObjectWrapper.COLUMN_ID);
						stringBuffer.append(SQL_FROM);
						stringBuffer.append(ObjectEntities.MONITOREDELEMENT);
						stringBuffer.append(SQL_WHERE);
						stringBuffer.append(MonitoredElementWrapper.COLUMN_MEASUREMENT_PORT_ID);
						stringBuffer.append(SQL_IN);
						stringBuffer.append(OPEN_BRACKET);
						stringBuffer.append(SQL_SELECT);
						stringBuffer.append(StorableObjectWrapper.COLUMN_ID);
						stringBuffer.append(SQL_FROM);
						stringBuffer.append(ObjectEntities.MEASUREMENTPORT);
						stringBuffer.append(SQL_WHERE);
						stringBuffer.append(super.getLinkedQuery(MeasurementPortWrapper.COLUMN_KIS_ID,
								StorableObjectWrapper.COLUMN_ID,
								KISWrapper.COLUMN_MCM_ID,
								ObjectEntities.KIS));
						stringBuffer.append(CLOSE_BRACKET);
						stringBuffer.append(CLOSE_BRACKET);
						return stringBuffer.toString();
					case MEASUREMENTSETUP_CODE:
						return super.getLinkedQuery(TestWrapper.LINK_COLUMN_TEST_ID, 
								TestWrapper.LINK_COLUMN_MEASUREMENT_SETUP_ID, 
								ObjectEntities.MSTESTLINK);
					case TEST_CODE:
						return super.getQuery(TestWrapper.COLUMN_GROUP_TEST_ID);
					default:
						throw super.newExceptionLinkedEntityIllegal();
				}
			default:
				throw super.newExceptionEntityIllegal();
		}
	}
	
}
