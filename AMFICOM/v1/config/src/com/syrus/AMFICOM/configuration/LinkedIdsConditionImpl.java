/*
 * $Id: LinkedIdsConditionImpl.java,v 1.2 2005/02/07 10:00:25 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

//import java.util.Iterator;
import java.util.List;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;

/**
 * @version $Revision: 1.2 $, $Date: 2005/02/07 10:00:25 $
 * @author $Author: bob $
 * @module config_v1
 */
class LinkedIdsConditionImpl extends com.syrus.AMFICOM.general.LinkedIdsCondition {

	private LinkedIdsConditionImpl(List linkedIds, Short entityCode) {
		this.linkedIds = linkedIds;
		this.entityCode = entityCode;
	}

	private LinkedIdsConditionImpl(Identifier identifier, Short entityCode) {
		this.identifier = identifier;
		this.entityCode = entityCode;
	}

	public boolean isConditionTrue(Object object) throws ApplicationException {
//		switch (this.entityCode.shortValue()) {
//			default:
				throw new UnsupportedOperationException("LinkedIdsConditionImpl.isConditionTrue | entityCode " + ObjectEntities.codeToString(this.entityCode.shortValue()) + " is unknown for this condition");
//		}
	}

	public void setEntityCode(Short entityCode) {
//		switch (entityCode.shortValue()) {
//			default:
				throw new UnsupportedOperationException("LinkedIdsConditionImpl.setEntityCode | entityCode " + ObjectEntities.codeToString(entityCode.shortValue()) + " is unknown for this condition");
//		}
	}
	
	
	public boolean isNeedMore(List list) {		
		return true;
	}	
}
