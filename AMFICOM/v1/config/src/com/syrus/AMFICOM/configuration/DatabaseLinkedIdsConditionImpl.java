/*
 * $Id: DatabaseLinkedIdsConditionImpl.java,v 1.8 2005/03/23 19:06:13 arseniy Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.configuration;

import com.syrus.AMFICOM.administration.DomainMember;
import com.syrus.AMFICOM.general.AbstractDatabaseLinkedIdsCondition;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectWrapper;

/**
 * @version $Revision: 1.8 $, $Date: 2005/03/23 19:06:13 $
 * @author $Author: arseniy $
 * @module config_v1
 */
final class DatabaseLinkedIdsConditionImpl extends AbstractDatabaseLinkedIdsCondition {

	private DatabaseLinkedIdsConditionImpl(LinkedIdsCondition condition) {
		super(condition);
	}

	public String getSQLQuery() throws IllegalDataException {
		String query = null;
		switch (super.condition.getEntityCode().shortValue()) {
			case ObjectEntities.CABLETHREADTYPE_ENTITY_CODE:
				query = super.getQuery(CableThreadTypeWrapper.COLUMN_LINK_TYPE_ID);
				break;
			case ObjectEntities.EQUIPMENT_ENTITY_CODE:
				query = super.getQuery(DomainMember.COLUMN_DOMAIN_ID);
				break;
			case ObjectEntities.TRANSPATH_ENTITY_CODE:
				switch (super.condition.getLinkedEntityCode()) {
					case ObjectEntities.PORT_ENTITY_CODE:
						query = super.getQuery(TransmissionPathWrapper.COLUMN_START_PORT_ID)
								+ StorableObjectDatabase.SQL_OR
								+ super.getQuery(TransmissionPathWrapper.COLUMN_FINISH_PORT_ID);
						break;
					default:
						query = super.getQuery(DomainMember.COLUMN_DOMAIN_ID);
				}
				break;
			case ObjectEntities.KIS_ENTITY_CODE:
				switch (super.condition.getLinkedEntityCode()) {
					case ObjectEntities.MCM_ENTITY_CODE:
						query = super.getQuery(KISWrapper.COLUMN_MCM_ID);
						break;
					default:
						query = super.getQuery(DomainMember.COLUMN_DOMAIN_ID);
				}
				break;
			case ObjectEntities.ME_ENTITY_CODE:
				query = super.getQuery(DomainMember.COLUMN_DOMAIN_ID);
				break;
			case ObjectEntities.PORT_ENTITY_CODE:
				switch (super.condition.getLinkedEntityCode()) {
					case ObjectEntities.EQUIPMENT_ENTITY_CODE:
						query = super.getQuery(PortWrapper.COLUMN_EQUIPMENT_ID);
						break;
					default:
						query = super.getLinkedQuery(PortWrapper.COLUMN_EQUIPMENT_ID,
								StorableObjectWrapper.COLUMN_ID,
								DomainMember.COLUMN_DOMAIN_ID,
								ObjectEntities.EQUIPMENT_ENTITY);
				}
				break;
			case ObjectEntities.MEASUREMENTPORT_ENTITY_CODE:
				switch (super.condition.getLinkedEntityCode()) {
					case ObjectEntities.KIS_ENTITY_CODE:
						query = super.getQuery(MeasurementPortWrapper.COLUMN_KIS_ID);
						break;
					case ObjectEntities.MCM_ENTITY_CODE:
						query = super.getLinkedQuery(MeasurementPortWrapper.COLUMN_KIS_ID,
								StorableObjectWrapper.COLUMN_ID,
								KISWrapper.COLUMN_MCM_ID,
								ObjectEntities.KIS_ENTITY);
						break;
					default:
						query = super.getLinkedQuery(MeasurementPortWrapper.COLUMN_KIS_ID,
								StorableObjectWrapper.COLUMN_ID,
								DomainMember.COLUMN_DOMAIN_ID,
								ObjectEntities.KIS_ENTITY);
				}
				break;
		}
		return query;
	}

}
