/*
 * $Id: DatabaseLinkedIdsConditionImpl.java,v 1.41.2.12 2006/06/08 14:28:34 arseniy Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.measurement;

import static com.syrus.AMFICOM.administration.DomainMember.COLUMN_DOMAIN_ID;
import static com.syrus.AMFICOM.general.ObjectEntities.ACTIONPARAMETERTYPEBINDING_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.ACTIONPARAMETER_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.ACTIONTEMPLATE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.ANALYSIS;
import static com.syrus.AMFICOM.general.ObjectEntities.ANALYSISRESULTPARAMETER_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.ANALYSIS_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.ANALYSIS_TYPE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.DOMAIN_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.KIS;
import static com.syrus.AMFICOM.general.ObjectEntities.KIS_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MCM_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MEASUREMENT;
import static com.syrus.AMFICOM.general.ObjectEntities.MEASUREMENTPORT;
import static com.syrus.AMFICOM.general.ObjectEntities.MEASUREMENTPORT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MEASUREMENTPORT_TYPE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MEASUREMENTRESULTPARAMETER_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MEASUREMENTSETUP_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MEASUREMENT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MEASUREMENT_TYPE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MODELINGRESULTPARAMETER_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MODELING_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MODELING_TYPE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MONITOREDELEMENT;
import static com.syrus.AMFICOM.general.ObjectEntities.MONITOREDELEMENT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.PARAMETER_TYPE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.PORT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.TEST_CODE;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.CLOSE_BRACKET;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.OPEN_BRACKET;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.SQL_FROM;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.SQL_IN;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.SQL_SELECT;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.SQL_WHERE;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_TYPE_ID;
import static com.syrus.AMFICOM.general.TableNames.ACTMPL_ME_LINK;
import static com.syrus.AMFICOM.general.TableNames.MS_ME_LINK;
import static com.syrus.AMFICOM.general.TableNames.TEST_MS_LINK;
import static com.syrus.AMFICOM.measurement.ActionParameterTypeBindingWrapper.COLUMN_MEASUREMENT_PORT_TYPE_ID;
import static com.syrus.AMFICOM.measurement.ActionParameterTypeBindingWrapper.COLUMN_PARAMETER_TYPE_ID;
import static com.syrus.AMFICOM.measurement.ActionParameterWrapper.COLUMN_BINDING_ID;
import static com.syrus.AMFICOM.measurement.ActionTemplateWrapper.LINK_COLUMN_ACTION_TEMPLATE_ID;
import static com.syrus.AMFICOM.measurement.ActionTemplateWrapper.LINK_COLUMN_MONITORED_ELEMENT_ID;
import static com.syrus.AMFICOM.measurement.ActionTypeWrapper.ActionTypeKindWrapper.COLUMN_ANALYSIS_TYPE_ID;
import static com.syrus.AMFICOM.measurement.ActionTypeWrapper.ActionTypeKindWrapper.COLUMN_MEASUREMENT_TYPE_ID;
import static com.syrus.AMFICOM.measurement.ActionTypeWrapper.ActionTypeKindWrapper.COLUMN_MODELING_TYPE_ID;
import static com.syrus.AMFICOM.measurement.ActionWrapper.COLUMN_ACTION_TEMPLATE_ID;
import static com.syrus.AMFICOM.measurement.ActionWrapper.COLUMN_MONITORED_ELEMENT_ID;
import static com.syrus.AMFICOM.measurement.AnalysisResultParameterWrapper.COLUMN_ANALYSIS_ID;
import static com.syrus.AMFICOM.measurement.KISWrapper.COLUMN_MCM_ID;
import static com.syrus.AMFICOM.measurement.MeasurementPortWrapper.COLUMN_KIS_ID;
import static com.syrus.AMFICOM.measurement.MeasurementPortWrapper.COLUMN_PORT_ID;
import static com.syrus.AMFICOM.measurement.MeasurementResultParameterWrapper.COLUMN_MEASUREMENT_ID;
import static com.syrus.AMFICOM.measurement.MeasurementWrapper.COLUMN_TEST_ID;
import static com.syrus.AMFICOM.measurement.ModelingResultParameterWrapper.COLUMN_MODELING_ID;
import static com.syrus.AMFICOM.measurement.MonitoredElementWrapper.COLUMN_MEASUREMENT_PORT_ID;
import static com.syrus.AMFICOM.measurement.TestWrapper.COLUMN_GROUP_TEST_ID;
import static com.syrus.AMFICOM.measurement.TestWrapper.LINK_COLUMN_MEASUREMENT_SETUP_ID;
import static com.syrus.AMFICOM.measurement.TestWrapper.LINK_COLUMN_TEST_ID;

import com.syrus.AMFICOM.general.AbstractDatabaseLinkedIdsCondition;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.LinkedIdsCondition;

/**
 * @version $Revision: 1.41.2.12 $, $Date: 2006/06/08 14:28:34 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
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
			case ACTIONPARAMETERTYPEBINDING_CODE:
				switch (super.condition.getLinkedEntityCode()) {
					case PARAMETER_TYPE_CODE:
						return super.getQuery(COLUMN_PARAMETER_TYPE_ID);
					case MEASUREMENT_TYPE_CODE:
						return super.getQuery(COLUMN_MEASUREMENT_TYPE_ID);
					case ANALYSIS_TYPE_CODE:
						return super.getQuery(COLUMN_ANALYSIS_TYPE_ID);
					case MODELING_TYPE_CODE:
						return super.getQuery(COLUMN_MODELING_TYPE_ID);
					case MEASUREMENTPORT_TYPE_CODE:
						return super.getQuery(COLUMN_MEASUREMENT_PORT_TYPE_ID);
					default:
						throw super.newExceptionLinkedEntityIllegal();
				}
			case ACTIONPARAMETER_CODE:
				switch (super.condition.getLinkedEntityCode()) {
					case ACTIONPARAMETERTYPEBINDING_CODE:
						return super.getQuery(COLUMN_BINDING_ID);
					default:
						throw super.newExceptionLinkedEntityIllegal();
				}
			case MEASUREMENT_CODE:
				switch (super.condition.getLinkedEntityCode()) {
					case TEST_CODE:
						return super.getQuery(COLUMN_TEST_ID);
					case MONITOREDELEMENT_CODE:
						return super.getQuery(COLUMN_MONITORED_ELEMENT_ID);
					case ACTIONTEMPLATE_CODE:
						return super.getQuery(COLUMN_ACTION_TEMPLATE_ID);
					default:
						throw super.newExceptionLinkedEntityIllegal();
				}
			case ANALYSIS_CODE:
				switch (super.condition.getLinkedEntityCode()) {
					case MEASUREMENT_CODE:
						return super.getQuery(AnalysisWrapper.COLUMN_MEASUREMENT_ID);
					case MONITOREDELEMENT_CODE:
						return super.getQuery(COLUMN_MONITORED_ELEMENT_ID);
					case ACTIONTEMPLATE_CODE:
						return super.getQuery(COLUMN_ACTION_TEMPLATE_ID);
					default:
						throw super.newExceptionLinkedEntityIllegal();
				}
			case MODELING_CODE:
				switch (super.condition.getLinkedEntityCode()) {
					case MONITOREDELEMENT_CODE:
						return super.getQuery(COLUMN_MONITORED_ELEMENT_ID);
					case ACTIONTEMPLATE_CODE:
						return super.getQuery(COLUMN_ACTION_TEMPLATE_ID);
					default:
						throw super.newExceptionLinkedEntityIllegal();
				}
			case ACTIONTEMPLATE_CODE:
				switch (super.condition.getLinkedEntityCode()) {
					case MEASUREMENT_TYPE_CODE:
						return super.getQuery(COLUMN_MEASUREMENT_TYPE_ID);
					case ANALYSIS_TYPE_CODE:
						return super.getQuery(COLUMN_ANALYSIS_TYPE_ID);
					case MODELING_TYPE_CODE:
						return super.getQuery(COLUMN_MODELING_TYPE_ID);
					case MEASUREMENTPORT_TYPE_CODE:
						return super.getQuery(ActionTemplateWrapper.COLUMN_MEASUREMENT_PORT_TYPE_ID);
					case MONITOREDELEMENT_CODE:
						return super.getLinkedQuery(LINK_COLUMN_ACTION_TEMPLATE_ID,
								LINK_COLUMN_MONITORED_ELEMENT_ID,
								ACTMPL_ME_LINK);
					default:
						throw super.newExceptionLinkedEntityIllegal();
				}
			case MEASUREMENTSETUP_CODE:
				switch (super.condition.getLinkedEntityCode()) {
					case MEASUREMENTPORT_TYPE_CODE:
						return super.getQuery(MeasurementSetupWrapper.COLUMN_MEASUREMENT_PORT_TYPE_ID);
					case MONITOREDELEMENT_CODE:
						return super.getLinkedQuery(MeasurementSetupWrapper.LINK_COLUMN_MEASUREMENT_SETUP_ID,
								MeasurementSetupWrapper.LINK_COLUMN_MONITORED_ELEMENT_ID,
								MS_ME_LINK);
					default:
						throw super.newExceptionLinkedEntityIllegal();
				}
			case MEASUREMENTRESULTPARAMETER_CODE:
				switch (super.condition.getLinkedEntityCode()) {
					case PARAMETER_TYPE_CODE:
						return super.getQuery(COLUMN_TYPE_ID);
					case MEASUREMENT_CODE:
						return super.getQuery(COLUMN_MEASUREMENT_ID);
					case TEST_CODE:
						return super.getLinkedQuery(COLUMN_MEASUREMENT_ID, COLUMN_ID, COLUMN_TEST_ID, MEASUREMENT);
					default:
						throw super.newExceptionLinkedEntityIllegal();
				}
			case ANALYSISRESULTPARAMETER_CODE:
				switch (super.condition.getLinkedEntityCode()) {
					case PARAMETER_TYPE_CODE:
						return super.getQuery(COLUMN_TYPE_ID);
					case ANALYSIS_CODE:
						return super.getQuery(COLUMN_ANALYSIS_ID);
					case MEASUREMENT_CODE:
						return super.getLinkedQuery(COLUMN_ANALYSIS_ID,
								COLUMN_ID,
								AnalysisWrapper.COLUMN_MEASUREMENT_ID,
								ANALYSIS);
					case TEST_CODE:
						stringBuffer = new StringBuffer();
						stringBuffer.append(COLUMN_ANALYSIS_ID);
						stringBuffer.append(SQL_IN);
						stringBuffer.append(OPEN_BRACKET);
						stringBuffer.append(SQL_SELECT);
						stringBuffer.append(COLUMN_ID);
						stringBuffer.append(SQL_FROM);
						stringBuffer.append(ANALYSIS);
						stringBuffer.append(SQL_WHERE);
						stringBuffer.append(super.getLinkedQuery(AnalysisWrapper.COLUMN_MEASUREMENT_ID,
								COLUMN_ID,
								COLUMN_TEST_ID,
								MEASUREMENT));
						stringBuffer.append(CLOSE_BRACKET);
						return stringBuffer.toString();
					default:
						throw super.newExceptionLinkedEntityIllegal();
				}
			case MODELINGRESULTPARAMETER_CODE:
				switch (super.condition.getLinkedEntityCode()) {
					case PARAMETER_TYPE_CODE:
						return super.getQuery(COLUMN_TYPE_ID);
					case MODELING_CODE:
						return super.getQuery(COLUMN_MODELING_ID);
					default:
						throw super.newExceptionLinkedEntityIllegal();
				}
			case TEST_CODE:
				switch (super.condition.getLinkedEntityCode()) {
					case MONITOREDELEMENT_CODE:
						return super.getQuery(TestWrapper.COLUMN_MONITORED_ELEMENT_ID);
					case MEASUREMENTPORT_CODE:
						return super.getLinkedQuery(TestWrapper.COLUMN_MONITORED_ELEMENT_ID,
								COLUMN_ID,
								COLUMN_MEASUREMENT_PORT_ID,
								MONITOREDELEMENT);
					case MCM_CODE:
						stringBuffer = new StringBuffer();
						stringBuffer.append(TestWrapper.COLUMN_MONITORED_ELEMENT_ID);
						stringBuffer.append(SQL_IN);
						stringBuffer.append(OPEN_BRACKET);
						stringBuffer.append(SQL_SELECT);
						stringBuffer.append(COLUMN_ID);
						stringBuffer.append(SQL_FROM);
						stringBuffer.append(MONITOREDELEMENT);
						stringBuffer.append(SQL_WHERE);
						stringBuffer.append(COLUMN_MEASUREMENT_PORT_ID);
						stringBuffer.append(SQL_IN);
						stringBuffer.append(OPEN_BRACKET);
						stringBuffer.append(SQL_SELECT);
						stringBuffer.append(COLUMN_ID);
						stringBuffer.append(SQL_FROM);
						stringBuffer.append(MEASUREMENTPORT);
						stringBuffer.append(SQL_WHERE);
						stringBuffer.append(super.getLinkedQuery(COLUMN_KIS_ID, COLUMN_ID, COLUMN_MCM_ID, KIS));
						stringBuffer.append(CLOSE_BRACKET);
						stringBuffer.append(CLOSE_BRACKET);
						return stringBuffer.toString();
					case MEASUREMENTSETUP_CODE:
						return super.getLinkedQuery(LINK_COLUMN_TEST_ID, LINK_COLUMN_MEASUREMENT_SETUP_ID, TEST_MS_LINK);
					case TEST_CODE:
						return super.getQuery(COLUMN_GROUP_TEST_ID);
					default:
						throw super.newExceptionLinkedEntityIllegal();
				}
			case MEASUREMENTPORT_CODE:
				switch (super.condition.getLinkedEntityCode()) {
					case MEASUREMENTPORT_TYPE_CODE:
						return super.getQuery(COLUMN_TYPE_ID);
					case PORT_CODE:
						return super.getQuery(COLUMN_PORT_ID);
					case KIS_CODE:
						return super.getQuery(COLUMN_KIS_ID);
					case MCM_CODE:
						return super.getLinkedQuery(COLUMN_KIS_ID, COLUMN_ID, COLUMN_MCM_ID, KIS);
					case DOMAIN_CODE:
						return super.getLinkedQuery(COLUMN_KIS_ID, COLUMN_ID, COLUMN_DOMAIN_ID, KIS);
					default:
						throw super.newExceptionLinkedEntityIllegal();
				}
			case KIS_CODE:
				switch (super.condition.getLinkedEntityCode()) {
					case MCM_CODE:
						return super.getQuery(COLUMN_MCM_ID);
					case DOMAIN_CODE:
						return super.getQuery(COLUMN_DOMAIN_ID);
					case MEASUREMENTPORT_CODE:
						return super.getLinkedQuery(COLUMN_KIS_ID, COLUMN_ID, MEASUREMENTPORT);
					default:
						throw super.newExceptionLinkedEntityIllegal();
				}
			case MONITOREDELEMENT_CODE:
				switch (super.condition.getLinkedEntityCode()) {
					case MEASUREMENTPORT_CODE:
						return super.getQuery(COLUMN_MEASUREMENT_PORT_ID);
					case DOMAIN_CODE:
						return super.getQuery(COLUMN_DOMAIN_ID);
					default:
						throw super.newExceptionLinkedEntityIllegal();
				}
			default:
				throw super.newExceptionEntityIllegal();
		}
	}
	
}
