/*
 * $Id: StorableObjectCondition.java,v 1.5 2005/02/03 14:56:29 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

import java.util.List;


/**
 * @version $Revision: 1.5 $, $Date: 2005/02/03 14:56:29 $
 * @author $Author: bob $
 * @module general_v1
 */
public interface StorableObjectCondition extends TransferableObject {
	boolean isConditionTrue(Object object) throws ApplicationException;
	
	/**
	 * Returns false if all objects that match this condition are already
	 * present in the local pool, and true otherwise (i. e. when certain
	 * objects <em>do need</em> loading.
	 *
	 * @param list objects present in the local pool.
	 * @return true if certain objects need to be loaded, false otherwise.
	 * @throws ApplicationException
	 * @see StorableObjectPool#getStorableObjectsByConditionButIdsImpl(List, StorableObjectCondition, boolean)
	 */
	boolean isNeedMore(List list) throws ApplicationException;

	Short getEntityCode();
	
	void setEntityCode(Short entityCode);
}
