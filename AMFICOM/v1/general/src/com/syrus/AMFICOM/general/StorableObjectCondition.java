/*
 * $Id: StorableObjectCondition.java,v 1.3 2004/10/21 08:00:10 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

import java.util.List;


/**
 * @version $Revision: 1.3 $, $Date: 2004/10/21 08:00:10 $
 * @author $Author: bob $
 * @module general_v1
 */
public interface StorableObjectCondition {

	boolean isConditionTrue(Object object) throws ApplicationException;
	
	boolean isNeedMore(List list) throws ApplicationException;

	Short getEntityCode();
	
	void setEntityCode(Short entityCode);
	
	Object getTransferable();
}
