/*
 * $Id: LinkedIdsConditionImpl.java,v 1.1 2005/01/14 18:05:13 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.administration;

import java.util.Iterator;
import java.util.List;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;

/**
 * @version $Revision: 1.1 $, $Date: 2005/01/14 18:05:13 $
 * @author $Author: arseniy $
 * @module admin_v1
 */
class LinkedIdsConditionImpl extends com.syrus.AMFICOM.general.LinkedIdsCondition {

	protected static final Short MCM_SHORT= new Short(ObjectEntities.MCM_ENTITY_CODE);

	private LinkedIdsConditionImpl(List linkedIds, Short entityCode) {
		this.linkedIds = linkedIds;
		this.entityCode = entityCode;
	}

	private LinkedIdsConditionImpl(Identifier identifier, Short entityCode) {
		this.identifier = identifier;
		this.entityCode = entityCode;
	}

	public boolean isConditionTrue(Object object) throws ApplicationException {
		boolean condition = false;
		switch (this.entityCode.shortValue()) {
			case ObjectEntities.MCM_ENTITY_CODE:
				{
				if (object instanceof MCM) {
					MCM mcm = (MCM) object;
					List kisIds = mcm.getKISIds();

					if (this.linkedIds == null) {
						Identifier kisId = this.identifier;
						for (Iterator it = kisIds.iterator(); it.hasNext();) {
							if (((Identifier)it.next()).equals(kisId)) {
								condition = true;
								break;
							}
						}
					}
					else {
						for (Iterator it = this.linkedIds.iterator(); it.hasNext();) {
							Identifier kisId = (Identifier) it.next();
							for (Iterator it1 = kisIds.iterator(); it1.hasNext();) {
								if (((Identifier)it1.next()).equals(kisId)) {
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
