/*-
 * $Id: LinkedIdsConditionImpl.java,v 1.13 2005/06/21 12:43:47 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import java.util.Set;

/**
 * @version $Revision: 1.13 $, $Date: 2005/06/21 12:43:47 $
 * @author $Author: bass $
 * @module general_v1
 */
final class LinkedIdsConditionImpl extends LinkedIdsCondition {

	protected static final Short CHARACTERISTIC_SHORT = new Short(ObjectEntities.CHARACTERISTIC_CODE);

	@SuppressWarnings("unusedPrivate")
	private LinkedIdsConditionImpl(final Set linkedIds, final Short linkedEntityCode, final Short entityCode) {
		this.linkedIds = linkedIds;
		this.linkedEntityCode = linkedEntityCode.shortValue();
		this.entityCode = entityCode;
	}

	/**
	 * @return <code>true</code>
	 *         <ul>
	 *         <li>if {@link #entityCode}is {@link Characteristic}for all
	 *         characteristics for StorableObject identifier which can have
	 *         characteristics in identifier;</li>
	 *         </ul>
	 */
	@Override
	public boolean isConditionTrue(final StorableObject storableObject) throws IllegalObjectEntityException {
		boolean condition = false;
		switch (this.entityCode.shortValue()) {
			case ObjectEntities.CHARACTERISTIC_CODE:
				Characteristic characteristic = (Characteristic) storableObject;
				switch (this.linkedEntityCode) {
					case ObjectEntities.CHARACTERISTIC_TYPE_CODE:
						condition = super.conditionTest(characteristic.getType().getId());
						break;
					default:
						condition = super.conditionTest(characteristic.getCharacterizableId());
				}
				break;
			default:
				throw new IllegalObjectEntityException(ENTITY_CODE_NOT_REGISTERED + this.entityCode
						+ ", " + ObjectEntities.codeToString(this.entityCode),
						IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
		}

		return condition;
	}

	@Override
	public void setEntityCode(final Short entityCode) throws IllegalObjectEntityException {
		switch (entityCode.shortValue()) {
			case ObjectEntities.CHARACTERISTIC_CODE:
				this.entityCode = CHARACTERISTIC_SHORT;
				break;
			default:
				throw new IllegalObjectEntityException(ENTITY_CODE_NOT_REGISTERED + this.entityCode
						+ ", " + ObjectEntities.codeToString(this.entityCode),
						IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
		}
	}

	@Override
	public boolean isNeedMore(final Set storableObjects) {
		return true;
	}
}
