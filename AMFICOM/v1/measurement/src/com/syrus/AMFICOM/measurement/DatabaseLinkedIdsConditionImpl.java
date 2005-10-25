/*
 * $Id: DatabaseLinkedIdsConditionImpl.java,v 1.39 2005/10/25 12:02:12 arseniy Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.measurement;

import static com.syrus.AMFICOM.general.ObjectEntities.PORT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.ANALYSIS_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.DOMAIN_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.KIS_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MCM_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MEASUREMENTPORT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MEASUREMENTSETUP_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MEASUREMENT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MODELING_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MONITOREDELEMENT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.RESULT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.TEST_CODE;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.CLOSE_BRACKET;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.OPEN_BRACKET;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.SQL_FROM;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.SQL_IN;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.SQL_OR;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.SQL_SELECT;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.SQL_WHERE;
import static com.syrus.AMFICOM.general.TableNames.MEASUREMENTSETUP_ME_LINK;

import com.syrus.AMFICOM.administration.DomainMember;
import com.syrus.AMFICOM.general.AbstractDatabaseLinkedIdsCondition;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.TableNames;

/**
 * @version $Revision: 1.39 $, $Date: 2005/10/25 12:02:12 $
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
					default:
						throw super.newExceptionLinkedEntityIllegal();
				}
			case MEASUREMENTSETUP_CODE:
				switch (super.condition.getLinkedEntityCode()) {
					case MONITOREDELEMENT_CODE:
						return super.getLinkedQuery(MeasurementSetupWrapper.LINK_COLUMN_MEASUREMENT_SETUP_ID,
									MeasurementSetupWrapper.LINK_COLUMN_MONITORED_ELEMENT_ID,
									MEASUREMENTSETUP_ME_LINK);
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
						return stringBuffer.toString();
					case ANALYSIS_CODE:
						return super.getQuery(ResultWrapper.COLUMN_ANALYSIS_ID);
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
								TableNames.MEASUREMENTSETUP_TEST_LINK);
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
