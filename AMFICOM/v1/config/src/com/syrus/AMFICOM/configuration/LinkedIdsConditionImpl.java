/*
 * $Id: LinkedIdsConditionImpl.java,v 1.1 2005/01/14 18:07:08 arseniy Exp $
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
 * @version $Revision: 1.1 $, $Date: 2005/01/14 18:07:08 $
 * @author $Author: arseniy $
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
		boolean condition = false;
		switch (this.entityCode.shortValue()) {
			default:
				throw new UnsupportedOperationException("entityCode " + ObjectEntities.codeToString(this.entityCode.shortValue()) + " is unknown for this condition");
		}

		//return condition;
	}

	public void setEntityCode(Short entityCode) {
		switch (entityCode.shortValue()) {
			default:
				throw new UnsupportedOperationException("entityCode " + ObjectEntities.codeToString(entityCode.shortValue()) + " is unknown for this condition");
		}
	}
	
	
	public boolean isNeedMore(List list) {		
		return true;
	}	
}
