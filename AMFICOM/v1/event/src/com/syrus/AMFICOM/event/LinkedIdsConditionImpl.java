/*-
 * $Id: LinkedIdsConditionImpl.java,v 1.5 2006/07/03 13:03:33 bass Exp $
 *
 * Copyright ¿ 2004-2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.event;

import static com.syrus.AMFICOM.general.ObjectEntities.LINEMISMATCHEVENT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.PATHELEMENT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.REFLECTOGRAMMISMATCHEVENT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.UPDIKE_CODE;

import java.util.Set;

import com.syrus.AMFICOM.eventv2.LineMismatchEvent;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObject;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.5 $, $Date: 2006/07/03 13:03:33 $
 * @module event
 */
final class LinkedIdsConditionImpl extends LinkedIdsCondition {
	@SuppressWarnings("unused")
	private LinkedIdsConditionImpl(final Set<? extends Identifiable> linkedIdentifiables, final Short linkedEntityCode, final Short entityCode) {
		this.linkedIdentifiables = linkedIdentifiables;
		this.linkedEntityCode = linkedEntityCode.shortValue();
		this.entityCode = entityCode;
	}

	/**
	 * @param storableObject
	 * @throws IllegalObjectEntityException
	 * @see com.syrus.AMFICOM.general.StorableObjectCondition#isConditionTrue(StorableObject)
	 */
	@Override
	public boolean isConditionTrue(final StorableObject storableObject) throws IllegalObjectEntityException {
		switch (this.entityCode.shortValue()) {
		case LINEMISMATCHEVENT_CODE:
			final LineMismatchEvent lineMismatchEvent = (LineMismatchEvent) storableObject;
			switch (this.linkedEntityCode) {
			case LINEMISMATCHEVENT_CODE:
			case UPDIKE_CODE:
				return this.conditionTest(lineMismatchEvent.getParentLineMismatchEventId());
			case REFLECTOGRAMMISMATCHEVENT_CODE:
				return this.conditionTest(lineMismatchEvent.getReflectogramMismatchEventId());
			case PATHELEMENT_CODE:
				return this.conditionTest(lineMismatchEvent.getAffectedPathElementId());
			default:
				throw newIllegalObjectEntityException();
			}
		default:
			throw newIllegalObjectEntityException();
		}
	}

	/**
	 * @param entityCode
	 * @throws IllegalObjectEntityException
	 * @see com.syrus.AMFICOM.general.StorableObjectCondition#setEntityCode(Short)
	 */
	@Override
	public void setEntityCode(final Short entityCode) throws IllegalObjectEntityException {
		switch (entityCode.shortValue()) {
			case LINEMISMATCHEVENT_CODE:
				this.entityCode = entityCode;
				break;
			default:
				throw newIllegalObjectEntityException();
		}
	}

	/**
	 * @param identifiables
	 * @see com.syrus.AMFICOM.general.StorableObjectCondition#isNeedMore(Set)
	 */
	@Override
	public boolean isNeedMore(final Set<? extends Identifiable> identifiables) {
		return true;
	}

	private IllegalObjectEntityException newIllegalObjectEntityException() {
		return new IllegalObjectEntityException(
				ENTITY_CODE_NOT_REGISTERED + this.entityCode
				+ ", " + ObjectEntities.codeToString(this.entityCode),
				IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
	}
}
