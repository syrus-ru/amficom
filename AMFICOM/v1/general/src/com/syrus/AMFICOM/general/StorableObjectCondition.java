/*
 * $Id: StorableObjectCondition.java,v 1.13 2005/06/16 10:29:05 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

import java.util.Set;


/**
 * @version $Revision: 1.13 $, $Date: 2005/06/16 10:29:05 $
 * @author $Author: bass $
 * @module general_v1
 */
public interface StorableObjectCondition extends TransferableObject {
	boolean isConditionTrue(final StorableObject storableObject) throws IllegalObjectEntityException;
	
	/**
	 * Returns false if all objects that match this condition are already
	 * present in the local pool, and true otherwise (i. e. when certain
	 * objects <em>do need</em> loading.
	 *
	 * @param storableObjects objects present in the local pool.
	 * @return true if certain objects need to be loaded, false otherwise.
	 * @see StorableObjectPool#getStorableObjectsByConditionButIdsImpl(Set, StorableObjectCondition, boolean, boolean)
	 */
	boolean isNeedMore(final Set storableObjects);

	Short getEntityCode();
	
	void setEntityCode(final Short entityCode) throws IllegalObjectEntityException;
}
