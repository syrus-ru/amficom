/*
 * $Id: LinkedIdsConditionImpl.java,v 1.2 2005/02/08 11:55:48 max Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;

/**
 * @version $Revision: 1.2 $, $Date: 2005/02/08 11:55:48 $
 * @author $Author: max $
 * @module general_v1
 */
class LinkedIdsConditionImpl extends com.syrus.AMFICOM.general.LinkedIdsCondition {

	protected static final Short CHARACTERISTIC_SHORT	= new Short(ObjectEntities.CHARACTERISTIC_ENTITY_CODE);

	private LinkedIdsConditionImpl(List linkedIds, Short entityCode) {
		this.linkedIds = linkedIds;
		this.entityCode = entityCode;
	}

	private LinkedIdsConditionImpl(Identifier identifier, Short entityCode) {
		this.linkedIds = Collections.singletonList(identifier);
		this.entityCode = entityCode;
	}

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
				if (object instanceof Characteristic) {
					Characteristic characteristic = (Characteristic) object;
					Identifier id = characteristic.getCharacterizedId();
					for (Iterator it = this.linkedIds.iterator(); it.hasNext();) {
						Identifier characterizedId = (Identifier) it.next();
						if (characterizedId.equals(id)) {
							condition = true;
							break;
						}
					}
				}
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

	public boolean isNeedMore(List list) {		
		return true;
	}	
}
