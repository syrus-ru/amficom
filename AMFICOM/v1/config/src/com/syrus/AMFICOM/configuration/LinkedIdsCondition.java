/*
 * $Id: LinkedIdsCondition.java,v 1.13 2004/12/24 10:57:38 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.util.Iterator;
import java.util.List;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;

/**
 * @version $Revision: 1.13 $, $Date: 2004/12/24 10:57:38 $
 * @author $Author: bob $
 * @module config_v1
 */
class LinkedIdsCondition extends com.syrus.AMFICOM.general.LinkedIdsCondition {

	protected static final Short		CHARACTERISTIC_SHORT	= new Short(ObjectEntities.CHARACTERISTIC_ENTITY_CODE);
	protected static final Short		MCM_SHORT				= new Short(ObjectEntities.MCM_ENTITY_CODE);

	private LinkedIdsCondition(List linkedIds, Short entityCode) {
		this.linkedIds = linkedIds;
		this.entityCode = entityCode;
	}

	private LinkedIdsCondition(Identifier identifier, Short entityCode) {
		this.identifier = identifier;
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
					if (this.linkedIds == null) {
						Identifier characterizedId = this.identifier;
						if (characterizedId.equals(id)) {
							condition = true;
							break;
						}
					} else {
						for (Iterator it = this.linkedIds.iterator(); it.hasNext();) {
							Identifier characterizedId = (Identifier) it.next();
							if (characterizedId.equals(id)) {
								condition = true;
								break;
							}
						}
					}
				}
				break;
			case ObjectEntities.MCM_ENTITY_CODE:
				{
				if (object instanceof MCM) {
					MCM mcm = (MCM) object;
					List kiss = mcm.getKISs();
					if (this.linkedIds == null) {
						Identifier kisId = this.identifier;
						for (Iterator it = kiss.iterator(); it.hasNext();) {
							KIS kis = (KIS) it.next();
							if (kis.getId().equals(kisId)) {
								condition = true;
								break;
							}
						}
					} else {
						for (Iterator it = this.linkedIds.iterator(); it.hasNext();) {
							Identifier kisId = (Identifier) it.next();
							for (Iterator iter = kiss.iterator(); it.hasNext();) {
								KIS kis = (KIS) iter.next();
								if (kis.getId().equals(kisId)) {
									condition = true;
									break;
								}
							}
						}
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
			case ObjectEntities.MCM_ENTITY_CODE:
				this.entityCode = MCM_SHORT;
				break;
			default:
				throw new UnsupportedOperationException("entityCode " + ObjectEntities.codeToString(entityCode.shortValue()) + " is unknown for this condition");
		}
	}
	
	
	public boolean isNeedMore(List list) {		
		return true;
	}	
}
