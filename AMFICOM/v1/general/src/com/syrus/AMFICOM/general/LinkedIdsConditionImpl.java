/*
 * $Id: LinkedIdsConditionImpl.java,v 1.8 2005/04/01 06:34:57 bob Exp $
 * Copyright � 2004 Syrus Systems. ������-����������� �����. ������: �������.
 */

package com.syrus.AMFICOM.general;

import java.util.Set;

/**
 * @version $Revision: 1.8 $, $Date: 2005/04/01 06:34:57 $
 * @author $Author: bob $
 * @module general_v1
 */
class LinkedIdsConditionImpl extends LinkedIdsCondition {

	protected static final Short CHARACTERISTIC_SHORT = new Short(ObjectEntities.CHARACTERISTIC_ENTITY_CODE);

	private LinkedIdsConditionImpl(Set linkedIds, Short linkedEntityCode, Short entityCode) {
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
	public boolean isConditionTrue(Object object) throws IllegalObjectEntityException {
		boolean condition = false;
		switch (this.entityCode.shortValue()) {
			case ObjectEntities.CHARACTERISTIC_ENTITY_CODE:
				Characteristic characteristic = (Characteristic) object;
				Identifier id = characteristic.getCharacterizableId();
				condition = super.conditionTest(id);
				break;
			default:
				throw new IllegalObjectEntityException(ENTITY_CODE_NOT_REGISTERED + this.entityCode
						+ ", " + ObjectEntities.codeToString(this.entityCode),
						IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
		}

		return condition;
	}

	public void setEntityCode(Short entityCode) throws IllegalObjectEntityException {
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

	public boolean isNeedMore(Set set) {
		return true;
	}
}
