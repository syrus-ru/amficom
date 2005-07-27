/*
 * $Id: DatabaseLinkedIdsConditionImpl.java,v 1.17 2005/07/27 15:59:22 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.configuration;

import static com.syrus.AMFICOM.general.ObjectEntities.CABLETHREAD_TYPE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.DOMAIN_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.EQUIPMENT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.KIS_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MCM_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MEASUREMENTPORT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MONITOREDELEMENT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.PORT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.TRANSPATH_CODE;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.CLOSE_BRACKET;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.OPEN_BRACKET;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.SQL_OR;

import com.syrus.AMFICOM.administration.DomainMember;
import com.syrus.AMFICOM.general.AbstractDatabaseLinkedIdsCondition;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectWrapper;

/**
 * @version $Revision: 1.17 $, $Date: 2005/07/27 15:59:22 $
 * @author $Author: bass $
 * @module config
 */
final class DatabaseLinkedIdsConditionImpl extends AbstractDatabaseLinkedIdsCondition {

	@SuppressWarnings("unused")
	private DatabaseLinkedIdsConditionImpl(final LinkedIdsCondition condition) {
		super(condition);
	}

	public String getSQLQuery() throws IllegalObjectEntityException {
		switch (super.condition.getEntityCode().shortValue()) {
			case CABLETHREAD_TYPE_CODE:
				return super.getQuery(CableThreadTypeWrapper.COLUMN_CABLE_LINK_TYPE_ID);
			case EQUIPMENT_CODE:
				return super.getQuery(DomainMember.COLUMN_DOMAIN_ID);
			case TRANSPATH_CODE:
				switch (super.condition.getLinkedEntityCode()) {
					case PORT_CODE:
						return OPEN_BRACKET
								+ super.getQuery(TransmissionPathWrapper.COLUMN_START_PORT_ID)
								+ SQL_OR
								+ super.getQuery(TransmissionPathWrapper.COLUMN_FINISH_PORT_ID)
								+ CLOSE_BRACKET;
					case DOMAIN_CODE:
						return super.getQuery(DomainMember.COLUMN_DOMAIN_ID);
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
				return super.getQuery(DomainMember.COLUMN_DOMAIN_ID);
			case PORT_CODE:
				switch (super.condition.getLinkedEntityCode()) {
					case EQUIPMENT_CODE:
						return super.getQuery(PortWrapper.COLUMN_EQUIPMENT_ID);
					case DOMAIN_CODE:
						return super.getLinkedQuery(PortWrapper.COLUMN_EQUIPMENT_ID,
								StorableObjectWrapper.COLUMN_ID,
								DomainMember.COLUMN_DOMAIN_ID,
								ObjectEntities.EQUIPMENT);
					default:
						throw super.newExceptionLinkedEntityIllegal();
				}
			case MEASUREMENTPORT_CODE:
				switch (super.condition.getLinkedEntityCode()) {
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
			default:
				throw super.newExceptionEntityIllegal();
		}
	}

}
