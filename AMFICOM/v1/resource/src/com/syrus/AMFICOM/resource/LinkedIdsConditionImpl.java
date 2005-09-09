/*-
 * $Id: LinkedIdsConditionImpl.java,v 1.2 2005/09/09 17:49:38 arseniy Exp $
 *
 * Copyright � 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.resource;

import static com.syrus.AMFICOM.general.ObjectEntities.LAYOUT_ITEM_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SYSTEMUSER_CODE;

import java.util.Set;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObject;

/**
 * @version $Revision: 1.2 $, $Date: 2005/09/09 17:49:38 $
 * @author $Author: arseniy $
 * @module resource
 */
final class LinkedIdsConditionImpl extends LinkedIdsCondition {
	private static final long serialVersionUID = 2022895734421025015L;

	protected static final Short LAYOUT_ITEM_SHORT = new Short(ObjectEntities.LAYOUT_ITEM_CODE);

	@SuppressWarnings("unused")
	private LinkedIdsConditionImpl(final Set<Identifier> linkedIds, final Short linkedEntityCode, final Short entityCode) {
		this.linkedIds = linkedIds;
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
						throw newExceptionLinkedEntityIllegal();
				}
				break;
			default:
				throw newExceptionEntityIllegal();
		}
		return condition;
	}

	@Override
	public void setEntityCode(final Short entityCode) throws IllegalObjectEntityException {
		switch (entityCode.shortValue()) {
			case ObjectEntities.LAYOUT_ITEM_CODE:
				this.entityCode = LAYOUT_ITEM_SHORT;
				break;
			default:
				throw newExceptionEntityIllegal();
		}
	}

	@Override
	public boolean isNeedMore(final Set<? extends StorableObject> storableObjects) {
		return true;
	}
}
