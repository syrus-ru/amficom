/*
 * $Id: StorableObjectCondition.java,v 1.11 2005/04/02 17:33:48 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

import java.util.Set;


/**
 * @version $Revision: 1.11 $, $Date: 2005/04/02 17:33:48 $
 * @author $Author: arseniy $
 * @module general_v1
 */
public interface StorableObjectCondition extends TransferableObject {

	boolean isConditionTrue(final Object object) throws IllegalObjectEntityException;
	
	/**
	 * Returns false if all objects that match this condition are already
	 * present in the local pool, and true otherwise (i. e. when certain
	 * objects <em>do need</em> loading.
	 *
	 * @param objects objects present in the local pool.
	 * @return true if certain objects need to be loaded, false otherwise.
	 * @see StorableObjectPool#getStorableObjectsByConditionButIdsImpl(Set, StorableObjectCondition, boolean)
	 */
	boolean isNeedMore(final Set objects);

	Short getEntityCode();
	
	void setEntityCode(final Short entityCode) throws IllegalObjectEntityException;
}
