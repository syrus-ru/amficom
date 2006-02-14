/*
 * $Id: DatabaseLinkedIdsConditionImpl.java,v 1.41.2.1 2006/02/14 01:09:56 arseniy Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.measurement;

import static com.syrus.AMFICOM.general.ObjectEntities.ACTIONTEMPLATE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.ANALYSISRESULTPARAMETER_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.ANALYSIS_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.DOMAIN_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.KIS_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MCM_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MEASUREMENTPORT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MEASUREMENTRESULTPARAMETER_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MEASUREMENT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MODELINGRESULTPARAMETER_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MODELING_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MONITOREDELEMENT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.PORT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.TEST_CODE;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.CLOSE_BRACKET;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.OPEN_BRACKET;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.SQL_FROM;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.SQL_IN;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.SQL_SELECT;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.SQL_WHERE;

import com.syrus.AMFICOM.administration.DomainMember;
import com.syrus.AMFICOM.general.AbstractDatabaseLinkedIdsCondition;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.TableNames;

/**
 * @version $Revision: 1.41.2.1 $, $Date: 2006/02/14 01:09:56 $
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
			case ANALYSIS_CODE:
				switch (super.condition.getLinkedEntityCode()) {
					case MEASUREMENT_CODE:
						return super.getQuery(AnalysisWrapper.COLUMN_MEASUREMENT_ID);
					default:
						throw super.newExceptionLinkedEntityIllegal();
				}
			case MEASUREMENT_CODE:
				switch (super.condition.getLinkedEntityCode()) {
					case TEST_CODE:
						return super.getQuery(MeasurementWrapper.COLUMN_TEST_ID);
					case MONITOREDELEMENT_CODE:
						return super.getQuery(MeasurementWrapper.COLUMN_MONITORED_ELEMENT_ID);
					default:
						throw super.newExceptionLinkedEntityIllegal();
				}
			case MODELING_CODE:
				switch (super.condition.getLinkedEntityCode()) {
					case MONITOREDELEMENT_CODE:
						return super.getQuery(ModelingWrapper.COLUMN_MONITORED_ELEMENT_ID);
					default:
						throw super.newExceptionLinkedEntityIllegal();
				}
			case ACTIONTEMPLATE_CODE:
				switch (super.condition.getLinkedEntityCode()) {
					case MONITOREDELEMENT_CODE:
						return super.getLinkedQuery(ActionTemplateWrapper.LINK_COLUMN_ACTION_TEMPLATE_ID,
								ActionTemplateWrapper.LINK_COLUMN_MONITORED_ELEMENT_ID,
								TableNames.ME_TMPL_LINK);
					default:
						throw super.newExceptionLinkedEntityIllegal();
				}
			case MEASUREMENTRESULTPARAMETER_CODE:
				switch (super.condition.getLinkedEntityCode()) {
					case MEASUREMENT_CODE:
						return super.getQuery(MeasurementResultParameterWrapper.COLUMN_MEASUREMENT_ID);
					default:
						throw super.newExceptionLinkedEntityIllegal();
				}
			case ANALYSISRESULTPARAMETER_CODE:
				switch (super.condition.getLinkedEntityCode()) {
					case ANALYSIS_CODE:
						return super.getQuery(AnalysisResultParameterWrapper.COLUMN_ANALYSIS_ID);
					default:
						throw super.newExceptionLinkedEntityIllegal();
				}
			case MODELINGRESULTPARAMETER_CODE:
				switch (super.condition.getLinkedEntityCode()) {
					case MODELING_CODE:
						return super.getQuery(ModelingResultParameterWrapper.COLUMN_MODELING_ID);
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
					case ACTIONTEMPLATE_CODE:
						return super.getLinkedQuery(TestWrapper.LINK_COLUMN_TEST_ID, 
								TestWrapper.LINK_COLUMN_MEASUREMENT_TEMPLATE_ID, 
								TableNames.TEST_MEASTMPL_LINK);
					case TEST_CODE:
						return super.getQuery(TestWrapper.COLUMN_GROUP_TEST_ID);
					default:
						throw super.newExceptionLinkedEntityIllegal();
				}
			case MEASUREMENTPORT_CODE:
				switch (super.condition.getLinkedEntityCode()) {
					case PORT_CODE:
						return super.getQuery(MeasurementPortWrapper.COLUMN_PORT_ID);
					case KIS_CODE:
						return super.getQuery(MeasurementPortWrapper.COLUMN_KIS_ID);
					case MCM_CODE:
						return super.getLinkedQuery(MeasurementPortWrapper.COLUMN_KIS_ID,
								StorableObjectWrapper.COLUMN_ID,
								KISWrapper.COLUMN_MCM_ID,
								ObjectEntities.KIS);
					case DOMAIN_CODE:
						return super.getLinkedQuery(MeasurementPortWrapper.COLUMN_KIS_ID,
								StorableObjectWrapper.COLUMN_ID,
								DomainMember.COLUMN_DOMAIN_ID,
								ObjectEntities.KIS);
					default:
						throw super.newExceptionLinkedEntityIllegal();
				}
			case KIS_CODE:
				switch (super.condition.getLinkedEntityCode()) {
					case MCM_CODE:
						return super.getQuery(KISWrapper.COLUMN_MCM_ID);
					case DOMAIN_CODE:
						return super.getQuery(DomainMember.COLUMN_DOMAIN_ID);
					default:
						throw super.newExceptionLinkedEntityIllegal();
				}
			case MONITOREDELEMENT_CODE:
				switch (super.condition.getLinkedEntityCode()) {
					case MEASUREMENTPORT_CODE:
						return super.getQuery(MonitoredElementWrapper.COLUMN_MEASUREMENT_PORT_ID);
					case DOMAIN_CODE:
						return super.getQuery(DomainMember.COLUMN_DOMAIN_ID);
					default:
						throw super.newExceptionLinkedEntityIllegal();
				}
			default:
				throw super.newExceptionEntityIllegal();
		}
	}
	
}
