/*
 * $Id: LinkedIdsConditionImpl.java,v 1.6 2005/03/05 21:33:00 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

import java.util.Collection;

/**
 * @version $Revision: 1.6 $, $Date: 2005/03/05 21:33:00 $
 * @author $Author: arseniy $
 * @module general_v1
 */
class LinkedIdsConditionImpl extends LinkedIdsCondition {

	protected static final Short CHARACTERISTIC_SHORT	= new Short(ObjectEntities.CHARACTERISTIC_ENTITY_CODE);

	private LinkedIdsConditionImpl(Collection linkedIds, Short linkedEntityCode, Short entityCode) {
		this.linkedIds = linkedIds;
		this.linkedEntityCode = linkedEntityCode.shortValue();
		this.entityCode = entityCode;
	}
//
//	private LinkedIdsConditionImpl(Identifier identifier, Short entityCode) {
//		this.linkedIds = Collections.singletonList(identifier);
//		this.entityCode = entityCode;
//	}

	/**
	 * @return <code>true</code>
	 *         <ul>
	 *         <li>if {@link #entityCode}is {@link Characteristic}for all
	 *         characteristics for StorableObject identifier which can have characteristics in identifier;</li>
	 *         </ul>
	 */
	public boolean isConditionTrue(Object object) throws ApplicationException {
		boolean condition = false;
		switch (this.entityCode.shortValue()) {
			case ObjectEntities.CHARACTERISTIC_ENTITY_CODE:
				Characteristic characteristic = (Characteristic) object;
				Identifier id = characteristic.getCharacterizableId();
				condition = super.conditionTest(id);				
				break;
			default:
				throw new UnsupportedOperationException("entityCode " + ObjectEntities.codeToString(this.entityCode.shortValue()) + " is unknown for this condition");
		}

		return condition;
	}

	public void setEntityCode(Short entityCode) {
		switch (entityCode.shortValue()) {
			case ObjectEntities.CHARACTERISTIC_ENTITY_CODE:
				this.entityCode = CHARACTERISTIC_SHORT;
				break;
			default:
				throw new UnsupportedOperationException("entityCode " + ObjectEntities.codeToString(entityCode.shortValue()) + " is unknown for this condition");
		}
	}

	public boolean isNeedMore(Collection list) {		
		return true;
	}	
}
