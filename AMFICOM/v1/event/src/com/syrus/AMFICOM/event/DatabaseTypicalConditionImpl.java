/*-
 * $Id: DatabaseTypicalConditionImpl.java,v 1.18 2006/07/03 12:29:40 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.event;

import static com.syrus.AMFICOM.event.DeliveryAttributesWrapper.COLUMN_SEVERITY;
import static com.syrus.AMFICOM.eventv2.LineMismatchEventWrapper.COLUMN_ALARM_STATUS;
import static com.syrus.AMFICOM.eventv2.LineMismatchEventWrapper.COLUMN_MISMATCH_OPTICAL_DISTANCE;
import static com.syrus.AMFICOM.eventv2.LineMismatchEventWrapper.COLUMN_MISMATCH_PHYSICAL_DISTANCE;
import static com.syrus.AMFICOM.eventv2.ReflectogramMismatchEventWrapper.COLUMN_ALARM_TYPE;
import static com.syrus.AMFICOM.general.ObjectEntities.DELIVERYATTRIBUTES_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.EVENT_TYPE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.LINEMISMATCHEVENT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.REFLECTOGRAMMISMATCHEVENT_CODE;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_CODENAME;

import com.syrus.AMFICOM.general.AbstractDatabaseTypicalCondition;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.TypicalCondition;

/**
 * @version $Revision: 1.18 $, $Date: 2006/07/03 12:29:40 $
 * @author $Author: bass $
 * @author Tashoyan Arseniy Feliksovich
 * @module event
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
		assert key != null && key == key.intern();
		switch (this.condition.getEntityCode().shortValue()) {
		case EVENT_TYPE_CODE:
			return key == COLUMN_CODENAME;
		case DELIVERYATTRIBUTES_CODE:
			return key == COLUMN_SEVERITY;
		case REFLECTOGRAMMISMATCHEVENT_CODE:
			return key == COLUMN_ALARM_TYPE
					|| key == COLUMN_SEVERITY;
		case LINEMISMATCHEVENT_CODE:
			return key == COLUMN_ALARM_STATUS
					|| key == COLUMN_MISMATCH_OPTICAL_DISTANCE
					|| key == COLUMN_MISMATCH_PHYSICAL_DISTANCE;
		default:
			return false;
		}
	}
}
