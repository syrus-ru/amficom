/*-
 * $Id: LinkedIdsConditionImpl.java,v 1.18 2006/06/28 10:34:12 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.mapview;

import static com.syrus.AMFICOM.general.ObjectEntities.DOMAIN_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MAPVIEW_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MAP_CODE;

import java.util.Set;

import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.StorableObject;

/**
 * @version $Revision: 1.18 $
 * @author $Author: arseniy $
 * @module mapview
 */
final class LinkedIdsConditionImpl extends com.syrus.AMFICOM.general.LinkedIdsCondition {
	@SuppressWarnings("unused")
	private LinkedIdsConditionImpl(final Set<? extends Identifiable> linkedIdentifiables, final Short linkedEntityCode, final Short entityCode) {
		this.linkedIdentifiables = linkedIdentifiables;
		this.linkedEntityCode = linkedEntityCode.shortValue();
		this.entityCode = entityCode;
	}

	@Override
	public boolean isConditionTrue(final StorableObject storableObject) throws IllegalObjectEntityException {
		switch (this.entityCode.shortValue()) {
			case MAPVIEW_CODE:
				final MapView mapView = (MapView) storableObject;
				switch (super.linkedEntityCode) {
					case DOMAIN_CODE:
						return super.conditionTest(mapView.getDomainId());
					case MAP_CODE:
						return super.conditionTest(mapView.getMapId());
					default:
						throw super.newExceptionLinkedEntityIllegal();
				}
			default:
				throw super.newExceptionEntityIllegal();
		}
	}

	@Override
	public void setEntityCode(final Short entityCode) throws IllegalObjectEntityException {
		switch (entityCode.shortValue()) {
			case MAPVIEW_CODE:
				this.entityCode = entityCode;
				break;
			default:
				throw super.newExceptionEntityCodeToSetIsIllegal(entityCode);
		}
	}

	@Override
	public boolean isNeedMore(final Set<? extends Identifiable> identifiables) {
		return true;
	}
}
