/*
 * $Id: StorableObjectCondition.java,v 1.10 2005/04/01 06:34:57 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

import java.util.Set;


/**
 * @version $Revision: 1.10 $, $Date: 2005/04/01 06:34:57 $
 * @author $Author: bob $
 * @module general_v1
 */
public interface StorableObjectCondition extends TransferableObject {
	boolean isConditionTrue(Object object) throws IllegalObjectEntityException;
	
	/**
	 * Returns false if all objects that match this condition are already
	 * present in the local pool, and true otherwise (i. e. when certain
	 * objects <em>do need</em> loading.
	 *
	 * @param objects objects present in the local pool.
	 * @return true if certain objects need to be loaded, false otherwise.
	 * @see StorableObjectPool#getStorableObjectsByConditionButIdsImpl(Set, StorableObjectCondition, boolean)
	 */
	boolean isNeedMore(Set objects);

	Short getEntityCode();
	
	void setEntityCode(Short entityCode) throws IllegalObjectEntityException;
}
