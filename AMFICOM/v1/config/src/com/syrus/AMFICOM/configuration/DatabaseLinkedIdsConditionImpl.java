/*
 * $Id: DatabaseLinkedIdsConditionImpl.java,v 1.1 2005/02/10 08:20:00 bob Exp $
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
import com.syrus.AMFICOM.general.StorableObjectWrapper;

/**
 * @version $Revision: 1.1 $, $Date: 2005/02/10 08:20:00 $
 * @author $Author: bob $
 * @module config_v1
 */
final class DatabaseLinkedIdsConditionImpl extends AbstractDatabaseLinkedIdsCondition {

	private DatabaseLinkedIdsConditionImpl(LinkedIdsCondition condition) {
		super(condition);
	}

	public String getSQLQuery() throws IllegalDataException {
		String query = null;
		switch (super.condition.getEntityCode().shortValue()) {
			case ObjectEntities.EQUIPMENT_ENTITY_CODE:
			case ObjectEntities.TRANSPATH_ENTITY_CODE:
			case ObjectEntities.KIS_ENTITY_CODE:
			case ObjectEntities.ME_ENTITY_CODE:
				query = super.getQuery(DomainMember.COLUMN_DOMAIN_ID);
				break;				
			case ObjectEntities.PORT_ENTITY_CODE:
				query = super.getLinkedQuery(PortWrapper.COLUMN_EQUIPMENT_ID, 
					StorableObjectWrapper.COLUMN_ID, DomainMember.COLUMN_DOMAIN_ID,
					ObjectEntities.EQUIPMENT_ENTITY);
				break;
			case ObjectEntities.MEASUREMENTPORT_ENTITY_CODE:
				query = super.getLinkedQuery(MeasurementPortWrapper.COLUMN_KIS_ID, 
					StorableObjectWrapper.COLUMN_ID, DomainMember.COLUMN_DOMAIN_ID,
					ObjectEntities.KIS_ENTITY);
				break;
		}
		return query;
	}

}
