/*-
 * $Id: LinkedIdsConditionImpl.java,v 1.15 2005/12/09 11:36:10 arseniy Exp $
 *
 * Copyright � 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.mapview;

import java.util.Set;

import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObject;

/**
 * @version $Revision: 1.15 $
 * @author $Author: arseniy $
 * @module mapview
 */
final class LinkedIdsConditionImpl extends com.syrus.AMFICOM.general.LinkedIdsCondition {
	private static final long serialVersionUID = 2623692593535601296L;

	@SuppressWarnings("unused")
	private LinkedIdsConditionImpl(final Set<? extends Identifiable> linkedIdentifiables, final Short linkedEntityCode, final Short entityCode) {
		this.linkedIdentifiables = linkedIdentifiables;
		this.linkedEntityCode = linkedEntityCode.shortValue();
		this.entityCode = entityCode;
	}

	@Override
	public boolean isConditionTrue(final StorableObject storableObject) throws IllegalObjectEntityException {
		boolean condition = false;
		switch (this.entityCode.shortValue()) {
			case ObjectEntities.MAPVIEW_CODE:
				final MapView mapView = (MapView) storableObject;
				switch (super.linkedEntityCode) {
				case ObjectEntities.DOMAIN_CODE:
					return super.conditionTest(mapView.getDomainId());
				case ObjectEntities.MAP_CODE:
					return super.conditionTest(mapView.getMapId());
				default:
					throw newExceptionLinkedEntityIllegal();
				}				
			default:
				newExceptionEntityIllegal();
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
	public boolean isNeedMore(final Set<? extends Identifiable> identifiables) {
		return true;
	}
}