/*-
 * $Id: LinkedIdsConditionImpl.java,v 1.11 2005/05/24 17:18:55 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import java.util.Set;

/**
 * @version $Revision: 1.11 $, $Date: 2005/05/24 17:18:55 $
 * @author $Author: arseniy $
 * @module general_v1
 */
final class LinkedIdsConditionImpl extends LinkedIdsCondition {

	protected static final Short CHARACTERISTIC_SHORT = new Short(ObjectEntities.CHARACTERISTIC_ENTITY_CODE);

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
	public boolean isConditionTrue(final StorableObject storableObject) throws IllegalObjectEntityException {
		boolean condition = false;
		switch (this.entityCode.shortValue()) {
			case ObjectEntities.CHARACTERISTIC_ENTITY_CODE:
				Characteristic characteristic = (Characteristic) storableObject;
				switch (this.linkedEntityCode) {
					case ObjectEntities.CHARACTERISTICTYPE_ENTITY_CODE:
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

	public void setEntityCode(final Short entityCode) throws IllegalObjectEntityException {
		switch (entityCode.shortValue()) {
			case ObjectEntities.CHARACTERISTIC_ENTITY_CODE:
				this.entityCode = CHARACTERISTIC_SHORT;
				break;
			default:
				throw new IllegalObjectEntityException(ENTITY_CODE_NOT_REGISTERED + this.entityCode
						+ ", " + ObjectEntities.codeToString(this.entityCode),
						IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
		}
	}

	public boolean isNeedMore(final Set storableObjects) {
		return true;
	}
}
