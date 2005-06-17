/*
 * $Id: DatabaseLinkedIdsConditionImpl.java,v 1.14 2005/06/17 11:01:10 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.configuration;

import com.syrus.AMFICOM.administration.DomainMember;
import com.syrus.AMFICOM.general.AbstractDatabaseLinkedIdsCondition;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectWrapper;

/**
 * @version $Revision: 1.14 $, $Date: 2005/06/17 11:01:10 $
 * @author $Author: bass $
 * @module config_v1
 */
final class DatabaseLinkedIdsConditionImpl extends AbstractDatabaseLinkedIdsCondition {

	private DatabaseLinkedIdsConditionImpl(LinkedIdsCondition condition) {
		super(condition);
	}

	public String getSQLQuery() throws IllegalObjectEntityException {
		switch (super.condition.getEntityCode().shortValue()) {
			case ObjectEntities.CABLETHREAD_TYPE_CODE:
				return super.getQuery(CableThreadTypeWrapper.COLUMN_CABLE_LINK_TYPE_ID);
			case ObjectEntities.EQUIPMENT_CODE:
				return super.getQuery(DomainMember.COLUMN_DOMAIN_ID);
			case ObjectEntities.TRANSPATH_CODE:
				switch (super.condition.getLinkedEntityCode()) {
					case ObjectEntities.PORT_CODE:
						return StorableObjectDatabase.OPEN_BRACKET
								+ super.getQuery(TransmissionPathWrapper.COLUMN_START_PORT_ID)
								+ StorableObjectDatabase.SQL_OR
								+ super.getQuery(TransmissionPathWrapper.COLUMN_FINISH_PORT_ID)
								+ StorableObjectDatabase.CLOSE_BRACKET;
					default:
						return super.getQuery(DomainMember.COLUMN_DOMAIN_ID);
				}
			case ObjectEntities.KIS_CODE:
				switch (super.condition.getLinkedEntityCode()) {
					case ObjectEntities.MCM_CODE:
						return super.getQuery(KISWrapper.COLUMN_MCM_ID);
					default:
						return super.getQuery(DomainMember.COLUMN_DOMAIN_ID);
				}
			case ObjectEntities.MONITOREDELEMENT_CODE:
				return super.getQuery(DomainMember.COLUMN_DOMAIN_ID);
			case ObjectEntities.PORT_CODE:
				switch (super.condition.getLinkedEntityCode()) {
					case ObjectEntities.EQUIPMENT_CODE:
						return super.getQuery(PortWrapper.COLUMN_EQUIPMENT_ID);
					default:
						return super.getLinkedQuery(PortWrapper.COLUMN_EQUIPMENT_ID,
								StorableObjectWrapper.COLUMN_ID,
								DomainMember.COLUMN_DOMAIN_ID,
								ObjectEntities.EQUIPMENT);
				}
			case ObjectEntities.MEASUREMENTPORT_CODE:
				switch (super.condition.getLinkedEntityCode()) {
					case ObjectEntities.KIS_CODE:
						return super.getQuery(MeasurementPortWrapper.COLUMN_KIS_ID);
					case ObjectEntities.MCM_CODE:
						return super.getLinkedQuery(MeasurementPortWrapper.COLUMN_KIS_ID,
								StorableObjectWrapper.COLUMN_ID,
								KISWrapper.COLUMN_MCM_ID,
								ObjectEntities.KIS);
					default:
						return super.getLinkedQuery(MeasurementPortWrapper.COLUMN_KIS_ID,
								StorableObjectWrapper.COLUMN_ID,
								DomainMember.COLUMN_DOMAIN_ID,
								ObjectEntities.KIS);
				}
			default:
				throw new IllegalObjectEntityException("Unsupported entity type -- "
						+ super.condition.getEntityCode(), IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
		}
	}

}
