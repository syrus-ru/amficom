/*
 * $Id: StorableObjectCondition.java,v 1.7 2005/03/24 10:54:10 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

import java.util.Collection;


/**
 * @version $Revision: 1.7 $, $Date: 2005/03/24 10:54:10 $
 * @author $Author: arseniy $
 * @module general_v1
 */
public interface StorableObjectCondition extends TransferableObject {
	boolean isConditionTrue(Object object) throws IllegalObjectEntityException;
	
	/**
	 * Returns false if all objects that match this condition are already
	 * present in the local pool, and true otherwise (i. e. when certain
	 * objects <em>do need</em> loading.
	 *
	 * @param list objects present in the local pool.
	 * @return true if certain objects need to be loaded, false otherwise.
	 * @throws ApplicationException
	 * @see StorableObjectPool#getStorableObjectsByConditionButIdsImpl(Collection, StorableObjectCondition, boolean)
	 */
	boolean isNeedMore(Collection list) throws ApplicationException;

	Short getEntityCode();
	
	void setEntityCode(Short entityCode) throws IllegalObjectEntityException;
}
