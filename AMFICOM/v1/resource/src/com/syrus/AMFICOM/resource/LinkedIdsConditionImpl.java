/*-
 * $Id: LinkedIdsConditionImpl.java,v 1.7 2006/06/28 10:34:12 arseniy Exp $
 *
 * Copyright ? 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.resource;

import static com.syrus.AMFICOM.general.ObjectEntities.LAYOUT_ITEM_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SYSTEMUSER_CODE;

import java.util.Set;

import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.StorableObject;

/**
 * @version $Revision: 1.7 $, $Date: 2006/06/28 10:34:12 $
 * @author $Author: arseniy $
 * @module resource
 */
final class LinkedIdsConditionImpl extends LinkedIdsCondition {
	protected static final Short LAYOUT_ITEM_SHORT = new Short(LAYOUT_ITEM_CODE);

	@SuppressWarnings("unused")
	private LinkedIdsConditionImpl(final Set<? extends Identifiable> linkedIdentifiables, final Short linkedEntityCode, final Short entityCode) {
		this.linkedIdentifiables = linkedIdentifiables;
		this.linkedEntityCode = linkedEntityCode.shortValue();
		this.entityCode = entityCode;
	}

	/**
	 * @return <code>true</code>
	 *         <ul>
	 *         <li>if {@link #entityCode} is {@link LayoutItem} for all
	 *         layoutItems for creator system user id;</li>
	 *         </ul>
	 */
	@Override
	public boolean isConditionTrue(final StorableObject storableObject) throws IllegalObjectEntityException {
		boolean condition = false;
		switch (this.entityCode.shortValue()) {
			case LAYOUT_ITEM_CODE:
				final LayoutItem layoutItem = (LayoutItem) storableObject;
				switch (this.linkedEntityCode) {
					/* General */
					case SYSTEMUSER_CODE:
						condition = super.conditionTest(layoutItem.getCreatorId());
						break;
					/* Resource */
					case LAYOUT_ITEM_CODE:
						condition = super.conditionTest(layoutItem.getParentId());
						break;
					default:
						throw super.newExceptionLinkedEntityIllegal();
				}
				break;
			default:
				throw super.newExceptionEntityIllegal();
		}
		return condition;
	}

	@Override
	public void setEntityCode(final Short entityCode) throws IllegalObjectEntityException {
		switch (entityCode.shortValue()) {
			case LAYOUT_ITEM_CODE:
				this.entityCode = LAYOUT_ITEM_SHORT;
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
