/*
 * $Id: StringFieldConditionImpl.java,v 1.1 2005/01/24 10:45:43 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.configuration;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.corba.StringFieldSort;


/**
 * @version $Revision: 1.1 $, $Date: 2005/01/24 10:45:43 $
 * @author $Author: bob $
 * @module config_v1
 */
final class StringFieldConditionImpl extends com.syrus.AMFICOM.general.StringFieldCondition  {
	
	private StringFieldConditionImpl(String string, Short entityCode, StringFieldSort sort){
		this.string = string;
		this.entityCode = entityCode;		
		this.sort = sort.value();
	}

	public boolean isConditionTrue(Object object) throws ApplicationException {
		boolean condition = false;
	/**
	 * @todo write something expressive
	 */
		return condition;
	}

}
