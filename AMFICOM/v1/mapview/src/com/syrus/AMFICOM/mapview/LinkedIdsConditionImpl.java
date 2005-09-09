/*-
 * $Id: LinkedIdsConditionImpl.java,v 1.12 2005/09/09 18:00:30 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.mapview;

import java.util.Set;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObject;

/**
 * @version $Revision: 1.12 $
 * @author $Author: arseniy $
 * @module mapview
 */
final class LinkedIdsConditionImpl extends com.syrus.AMFICOM.general.LinkedIdsCondition {
	private static final long serialVersionUID = 2623692593535601296L;

	@SuppressWarnings("unused")
	private LinkedIdsConditionImpl(final Set<Identifier> linkedIds, final Short linkedEntityCode, final Short entityCode) {
		this.linkedIds = linkedIds;
		this.linkedEntityCode = linkedEntityCode.shortValue();
		this.entityCode = entityCode;
	}

	@Override
	public boolean isConditionTrue(final StorableObject storableObject) {
		boolean condition = false;
		switch (this.entityCode.shortValue()) {
			case ObjectEntities.MAPVIEW_CODE:
				MapView mapView = (MapView) storableObject;
				super.conditionTest(mapView.getDomainId());
				break;
			default:
				throw new UnsupportedOperationException("LinkedIdsConditionImpl.isConditionTrue | entityCode "
						+ ObjectEntities.codeToString(this.entityCode.shortValue()) + " is unknown for this condition");
		}
		return condition;
	}

	@Override
	public void setEntityCode(final Short entityCode) throws IllegalObjectEntityException {
		switch (entityCode.shortValue()) {
			case ObjectEntities.MAPVIEW_CODE:
				this.entityCode = entityCode;
				break;
			default:
				throw new IllegalObjectEntityException("LinkedIdsConditionImpl.setEntityCode | entityCode "
						+ ObjectEntities.codeToString(entityCode.shortValue()) + " is unknown for this condition", IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
		}
	}

	@Override
	public boolean isNeedMore(final Set<? extends StorableObject> storableObjects) {
		return true;
	}
}
