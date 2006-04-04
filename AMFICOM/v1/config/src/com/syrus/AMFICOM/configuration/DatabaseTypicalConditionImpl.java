/*-
 * $Id: DatabaseTypicalConditionImpl.java,v 1.25.4.1 2006/04/04 09:32:32 arseniy Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.configuration;

import static com.syrus.AMFICOM.general.ObjectEntities.CABLELINK_TYPE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.CABLETHREAD_TYPE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.EQUIPMENT_TYPE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.LINK_TYPE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MONITOREDELEMENT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.PORT_TYPE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.PROTOEQUIPMENT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.TRANSPATH_TYPE_CODE;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_CODENAME;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_NAME;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_TYPE_CODE;

import com.syrus.AMFICOM.general.AbstractDatabaseTypicalCondition;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.TypicalCondition;


/**
 * @version $Revision: 1.25.4.1 $, $Date: 2006/04/04 09:32:32 $
 * @author $Author: arseniy $
 * @module config
 */
final class DatabaseTypicalConditionImpl extends AbstractDatabaseTypicalCondition {

	@SuppressWarnings("unused")
	private DatabaseTypicalConditionImpl(final TypicalCondition typicalCondition) {
		super(typicalCondition);
	}

	@Override
	protected String getLinkedThisColumnName() throws IllegalObjectEntityException {
		throw new IllegalObjectEntityException("Entity '" + ObjectEntities.codeToString(this.condition.getEntityCode())
				+ "' is not supported.", IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
	}

	@Override
	protected String getLinkedColumnName() throws IllegalObjectEntityException {
		throw new IllegalObjectEntityException("Entity '" + ObjectEntities.codeToString(this.condition.getEntityCode())
				+ "' is not supported.", IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
	}

	@Override
	protected String getLinkedTableName() throws IllegalObjectEntityException {
		throw new IllegalObjectEntityException("Entity '" + ObjectEntities.codeToString(this.condition.getEntityCode())
				+ "' is not supported.", IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
	}

	@Override
	protected boolean isKeySupported(final String key) {
		switch (this.condition.getEntityCode().shortValue()) {
			case EQUIPMENT_TYPE_CODE:
				return key == COLUMN_CODENAME;
			case PORT_TYPE_CODE:
				return key == PortTypeWrapper.COLUMN_SORT
					|| key == PortTypeWrapper.COLUMN_KIND
					|| key == COLUMN_CODENAME;
			case TRANSPATH_TYPE_CODE:
				return key == COLUMN_CODENAME;
			case PROTOEQUIPMENT_CODE:
				return key == ProtoEquipmentWrapper.COLUMN_MANUFACTURER_CODE
				|| key == COLUMN_TYPE_CODE;
			case MONITOREDELEMENT_CODE:
				return key == COLUMN_NAME;
			case CABLELINK_TYPE_CODE:
			case LINK_TYPE_CODE:
			case CABLETHREAD_TYPE_CODE:
				return key == COLUMN_CODENAME;
			default:
				return false;
		}
	}
}
