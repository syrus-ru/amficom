/*
 * $Id: DatabaseLinkedIdsConditionImpl.java,v 1.20.4.1 2006/04/04 09:31:56 arseniy Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.configuration;

import static com.syrus.AMFICOM.administration.DomainMember.COLUMN_DOMAIN_ID;
import static com.syrus.AMFICOM.configuration.CableThreadTypeWrapper.COLUMN_CABLE_LINK_TYPE_ID;
import static com.syrus.AMFICOM.configuration.PortWrapper.COLUMN_EQUIPMENT_ID;
import static com.syrus.AMFICOM.configuration.TransmissionPathWrapper.COLUMN_FINISH_PORT_ID;
import static com.syrus.AMFICOM.configuration.TransmissionPathWrapper.COLUMN_START_PORT_ID;
import static com.syrus.AMFICOM.general.ObjectEntities.CABLETHREAD_TYPE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.DOMAIN_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.EQUIPMENT;
import static com.syrus.AMFICOM.general.ObjectEntities.EQUIPMENT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.EQUIPMENT_TYPE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.PORT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.PROTOEQUIPMENT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.TRANSMISSIONPATH_CODE;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.CLOSE_BRACKET;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.OPEN_BRACKET;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.SQL_OR;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_TYPE_ID;

import com.syrus.AMFICOM.general.AbstractDatabaseLinkedIdsCondition;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.LinkedIdsCondition;

/**
 * @version $Revision: 1.20.4.1 $, $Date: 2006/04/04 09:31:56 $
 * @author $Author: arseniy $
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
				return super.getQuery(COLUMN_CABLE_LINK_TYPE_ID);
			case PROTOEQUIPMENT_CODE:
				switch (super.condition.getLinkedEntityCode()) {
					case EQUIPMENT_TYPE_CODE:
						return super.getQuery(COLUMN_TYPE_ID);
					default:
						throw super.newExceptionLinkedEntityIllegal();
				}
			case EQUIPMENT_CODE:
				return super.getQuery(COLUMN_DOMAIN_ID);
			case TRANSMISSIONPATH_CODE:
				switch (super.condition.getLinkedEntityCode()) {
					case PORT_CODE:
						return OPEN_BRACKET
								+ super.getQuery(COLUMN_START_PORT_ID)
								+ SQL_OR
								+ super.getQuery(COLUMN_FINISH_PORT_ID)
								+ CLOSE_BRACKET;
					case DOMAIN_CODE:
						return super.getQuery(COLUMN_DOMAIN_ID);
					default:
						throw super.newExceptionLinkedEntityIllegal();
				}
			case PORT_CODE:
				switch (super.condition.getLinkedEntityCode()) {
					case EQUIPMENT_CODE:
						return super.getQuery(COLUMN_EQUIPMENT_ID);
					case DOMAIN_CODE:
						return super.getLinkedQuery(COLUMN_EQUIPMENT_ID,
								COLUMN_ID,
								COLUMN_DOMAIN_ID,
								EQUIPMENT);
					default:
						throw super.newExceptionLinkedEntityIllegal();
				}
			default:
				throw super.newExceptionEntityIllegal();
		}
	}

}
